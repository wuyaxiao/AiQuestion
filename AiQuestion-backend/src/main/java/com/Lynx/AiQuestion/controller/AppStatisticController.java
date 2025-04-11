package com.Lynx.AiQuestion.controller;

import com.Lynx.AiQuestion.common.BaseResponse;
import com.Lynx.AiQuestion.common.ErrorCode;
import com.Lynx.AiQuestion.common.ResultUtils;
import com.Lynx.AiQuestion.exception.ThrowUtils;
import com.Lynx.AiQuestion.mapper.UserAnswerMapper;
import com.Lynx.AiQuestion.model.dto.statistic.AppAnswerCountDTO;
import com.Lynx.AiQuestion.model.dto.statistic.AppAnswerResultCountDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/app/statistic")
@Slf4j
public class AppStatisticController {

        @Resource
        private UserAnswerMapper userAnswerMapper;

        /**
         * 热门应用及回答分布统计
         *
         * @return
         */
        @GetMapping("/answer_count")
        public BaseResponse<List<AppAnswerCountDTO>> getAnswerCount() {
                return ResultUtils.success(userAnswerMapper.doAppAnswerCount());
        }

        /**
         *  某应用回答结果分布统计
         *
         * @param appId
         * @return
         */
        @GetMapping("/answer_result_count")
        public BaseResponse<List<AppAnswerResultCountDTO>> getAnswerResultCount(Long appId) {
                ThrowUtils.throwIf(appId == null || appId <= 0, ErrorCode.PARAMS_ERROR);
                return ResultUtils.success(userAnswerMapper.doAppAnswerResultCount(appId));
        }
}

