package com.Lynx.AiQuestion.manager;

import com.zhipu.oapi.ClientV4;
import com.zhipu.oapi.Constants;
import com.zhipu.oapi.service.v4.model.*;
import io.reactivex.Flowable;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Component
public class AiManager {

        @Resource
        private  ClientV4 client;

        //较稳定的随机数
        private static final float STABLE_TEMPERATURE = 0.05f;

        //较不稳定的随机数
        private static final float UNSTABLE_TEMPERATURE = 0.99f;

        /**
         * 通用流式请求（简化消息传递）
         *
         * @param systemMessage
         * @param userMessage
         * @param temperature
         * @return
         */
        public Flowable<ModelData> doStreamRequest(String systemMessage, String userMessage, Float temperature) {
                // 构造请求
                List<ChatMessage> messages = new ArrayList<>();
                ChatMessage systemChatMessage = new ChatMessage(ChatMessageRole.SYSTEM.value(), systemMessage);
                ChatMessage userChatMessage = new ChatMessage(ChatMessageRole.USER.value(), userMessage);
                messages.add(systemChatMessage);
                messages.add(userChatMessage);
                return doStreamRequest(messages, temperature);
        }

        /**
         * 通用流式请求
         *
         * @param messages
         * @param temperature
         * @return
         */
        public Flowable<ModelData> doStreamRequest(List<ChatMessage> messages, Float temperature) {
                // 构造请求
                ChatCompletionRequest chatCompletionRequest = ChatCompletionRequest.builder()
                                .model(Constants.ModelChatGLM4)
                                .stream(Boolean.TRUE)
                                .invokeMethod(Constants.invokeMethod)
                                .temperature(temperature)
                                .messages(messages)
                                .build();
                ModelApiResponse invokeModelApiResp = client.invokeModelApi(chatCompletionRequest);
                return invokeModelApiResp.getFlowable();
        }

        /**
         * 同步调用(答案较稳定)
         *
         * @param systemMessage
         * @param userMessage
         * @return
         */
        public String doSyncStableRequest(String systemMessage, String userMessage) {
                return doSyncRequest(systemMessage, userMessage, STABLE_TEMPERATURE);
        }

        /**
         * 同步调用(答案较不稳定)
         *
         * @param systemMessage
         * @param userMessage
         * @return
         */
        public String doSyncUnstableRequest(String systemMessage, String userMessage) {
                return doSyncRequest(systemMessage, userMessage, UNSTABLE_TEMPERATURE);
        }

        /**
         * 调用同步请求
         *
         * @param systemMessage
         * @param userMessage
         * @param temperature
         * @return
         */
        public String doSyncRequest(String systemMessage, String userMessage, Float temperature) {
                return doRequest(systemMessage, userMessage, Boolean.FALSE, temperature);
        }

        /**
         * 通用请求，简化消息传递
         *
         * @param systemMessage
         * @param userMessage
         * @param stream
         * @param temperature
         * @return
         */
        public String doRequest(String systemMessage, String userMessage, Boolean stream, Float temperature) {
                // 构造请求
                List<ChatMessage> messages = new ArrayList<>();
                ChatMessage systemChatMessage = new ChatMessage(ChatMessageRole.SYSTEM.value(), systemMessage);
                ChatMessage userChatMessage = new ChatMessage(ChatMessageRole.USER.value(), userMessage);
                messages.add(systemChatMessage);
                messages.add(userChatMessage);
                return doRequest(messages, stream, temperature);
        }

        /**
         * 最通用的方法，可自定义消息，随机数，是否流式
         * @param messages
         * @param stream
         * @param temperature
         * @return
         */
        public String  doRequest(List<ChatMessage> messages,Boolean stream,Float temperature){
                //String requestId = String.format(requestIdTemplate, System.currentTimeMillis());
                ChatCompletionRequest chatCompletionRequest = ChatCompletionRequest.builder()
                                .model(Constants.ModelChatGLM4)
                                .stream(stream)
                                .temperature(temperature)
                                .invokeMethod(Constants.invokeMethod)
                                .messages(messages)
                                .build();
                ModelApiResponse invokeModelApiResp = client.invokeModelApi(chatCompletionRequest);
                ChatMessage result=invokeModelApiResp.getData().getChoices().get(0).getMessage();
                return result.getContent().toString();
        }
}
