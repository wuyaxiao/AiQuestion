package com.Lynx.AiQuestion.scoring;

import cn.hutool.crypto.digest.DigestUtil;
import cn.hutool.json.JSONUtil;
import com.Lynx.AiQuestion.manager.AiManager;
import com.Lynx.AiQuestion.model.dto.question.QuestionAnswerDTO;
import com.Lynx.AiQuestion.model.dto.question.QuestionContentDTO;
import com.Lynx.AiQuestion.model.entity.App;
import com.Lynx.AiQuestion.model.entity.Question;
import com.Lynx.AiQuestion.model.entity.UserAnswer;
import com.Lynx.AiQuestion.model.vo.QuestionVO;
import com.Lynx.AiQuestion.service.QuestionService;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.github.benmanes.caffeine.cache.Caffeine;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;


/**
 * AI测评类应用评分策略
 */
@ScoringStrategyConfig(appType = 1, scoringStrategy = 1)
public class AiTestScoringStrategy implements ScoringStrategy {

        @Resource
        private QuestionService questionService;

        @Resource
        private AiManager aiManager;

        //分布式锁的key
        private static final String AI_ANSWER_LOCK = "AI_ANSWER_LOCK";

        @Resource
        private RedissonClient redissonClient;

        private final com.github.benmanes.caffeine.cache.Cache<String, String> answerCacheMap =
                        Caffeine.newBuilder().initialCapacity(1024)
                                        // 缓存5分钟移除
                                        .expireAfterAccess(5L, TimeUnit.MINUTES)
                                        .build();

        private static final String AI_TEST_SCORING_SYSTEM_MESSAGE = "你是一位严谨的判题专家，我会给你如下信息：\n" +
                        "```\n" +
                        "应用名称，\n" +
                        "【【【应用描述】】】，\n" +
                        "题目和用户回答的列表：格式为 [{\"title\": \"题目\",\"answer\": \"用户回答\"}]\n" +
                        "```\n" +
                        "\n" +
                        "请你根据上述信息，按照以下步骤来对用户进行评价：\n" +
                        "1. 要求：需要给出一个明确的评价结果，包括评价名称（尽量简短）和评价描述（尽量详细，大于 200 字）\n" +
                        "2. 严格按照下面的 json 格式输出评价名称和评价描述\n" +
                        "```\n" +
                        "{\"resultName\": \"评价名称\", \"resultDesc\": \"评价描述\"}\n" +
                        "```\n" +
                        "3. 返回格式必须为 JSON 对象\n" +
                        "4. 尽可能捕捉应用的关键词并参考网上相似测评的评分结果表述";

        private String getAiTestScoringUserMessage(App app, List<QuestionContentDTO> questionContentDTOList, List<String> choices) {
                StringBuilder userMessage = new StringBuilder();
                userMessage.append(app.getAppName()).append("\n");
                userMessage.append(app.getAppDesc()).append("\n");
                List<QuestionAnswerDTO> questionAnswerDTOList = new ArrayList<>();
                for (int i = 0; i < questionContentDTOList.size(); i++) {
                        QuestionAnswerDTO questionAnswerDTO = new QuestionAnswerDTO();
                        QuestionContentDTO questionContentDTO =questionContentDTOList.get(i);
                        questionAnswerDTO.setTitle(questionContentDTO.getTitle());
                        questionAnswerDTO.setUserAnswer(questionContentDTO.getOptions().get(choices.get(i).charAt(0)-'A').getValue());
                        questionAnswerDTOList.add(questionAnswerDTO);
                }
                userMessage.append(JSONUtil.toJsonStr(questionAnswerDTOList));
                return userMessage.toString();
        }

        @Override
        public UserAnswer doScore(List<String> choices, App app) throws Exception {
                Long appId = app.getId();
                String choicesStr = JSONUtil.toJsonStr(choices);
                String cacheKey = buildCacheKey(appId, choicesStr);
                String answerJson = answerCacheMap.getIfPresent(cacheKey);
                // 命中缓存则直接返回结果
                if (answerJson!=null) {
                        UserAnswer userAnswer=JSONUtil.toBean(answerJson, UserAnswer.class);
                        userAnswer.setAppType(app.getAppType());
                        userAnswer.setScoringStrategy(app.getScoringStrategy());
                        userAnswer.setChoices(choicesStr);

                        return userAnswer;
                }


                RLock lock = redissonClient.getLock(AI_ANSWER_LOCK + cacheKey);

                try {
                        // 竞争分布式锁，等待 3 秒，15 秒自动释放
                        boolean res = lock.tryLock(3, 15, TimeUnit.SECONDS);
                        if (res){
                                // 抢到锁的业务才能执行 AI 调用
                                // 1. 根据 id 查询到题目
                                Question question = questionService.getOne(
                                                Wrappers.lambdaQuery(Question.class).eq(Question::getAppId, appId)
                                );
                                QuestionVO questionVO = QuestionVO.objToVo(question);
                                List<QuestionContentDTO> questionContent = questionVO.getQuestionContent();
                                // 2. 调用 AI 获取结果
                                // 封装 Prompt
                                String userMessage = getAiTestScoringUserMessage(app, questionContent, choices);
                                // AI 生成
                                String result = aiManager.doSyncStableRequest(AI_TEST_SCORING_SYSTEM_MESSAGE, userMessage);
                                // 结果处理
                                int start = result.indexOf("{");
                                int end = result.lastIndexOf("}");
                                String json = result.substring(start, end + 1);

                                //缓存结果
                                answerCacheMap.put(cacheKey, json);

                                // 3. 构造返回值，填充答案对象的属性
                                UserAnswer userAnswer=JSONUtil.toBean(json, UserAnswer.class);
                                userAnswer.setAppType(app.getAppType());
                                userAnswer.setScoringStrategy(app.getScoringStrategy());
                                userAnswer.setChoices(JSONUtil.toJsonStr(choices));
                                return userAnswer;
                        }
                } finally {
                        if (lock != null && lock.isLocked()) {
                                if(lock.isHeldByCurrentThread()) {
                                        lock.unlock();
                                }
                        }
                }
                return null;
        }

        private String buildCacheKey(Long appId, String choicesStr) {
                return DigestUtil.md5Hex(appId + ":" + choicesStr);
        }
}
