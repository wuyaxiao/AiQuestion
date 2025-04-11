package com.Lynx.AiQuestion.config;

import com.Lynx.AiQuestion.utils.KeyConstant;
import com.zhipu.oapi.ClientV4;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "ai")
public class AiConfig {

        /**
         * apiKey,需要从平台获取
         */
        private String apiKey;

        @Bean
        public ClientV4 getClient(){
                return new ClientV4.Builder(KeyConstant.key).build();
        }
}
