package com.sicheng.langchain4j02multimodeltogether.config;

import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.model.openai.OpenAiChatModel;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class LLMConfig {

    @Bean(name = "qwen")
    public ChatModel qwenModel(AliQwenProperties props) {
        return OpenAiChatModel.builder()
                .apiKey(props.getApiKey())
                .baseUrl(props.getBaseUrl())
                .modelName(props.getModelName())
                .build();
    }

    @Bean(name = "deepseek")
    public ChatModel deepseekModel(DeepSeekProperties props) {
        return OpenAiChatModel.builder()
                .apiKey(props.getApiKey())
                .baseUrl(props.getBaseUrl())
                .modelName(props.getModelName())
                .build();
    }
}
