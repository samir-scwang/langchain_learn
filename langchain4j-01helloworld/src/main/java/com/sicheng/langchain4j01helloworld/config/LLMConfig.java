package com.sicheng.langchain4j01helloworld.config;

import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.model.openai.OpenAiChatModel;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class LLMConfig {

    private final QwenProperties qwenProperties;

    public LLMConfig(QwenProperties qwenProperties) {
        this.qwenProperties = qwenProperties;
    }

    @Bean
    public ChatModel chatModelQwen() {
        return OpenAiChatModel.builder()
                .apiKey(qwenProperties.getApiKey())
                .modelName(qwenProperties.getModelName())
                .baseUrl(qwenProperties.getBaseUrl())
                .build();
    }
}
