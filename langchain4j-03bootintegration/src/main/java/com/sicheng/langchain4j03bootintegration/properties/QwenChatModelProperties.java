package com.sicheng.langchain4j03bootintegration.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "langchain4j.qwen.chat-model")
public class QwenChatModelProperties {

    /**
     * API Key, 从 application.properties 读取
     */
    private String apiKey;

    /**
     * 模型名称
     */
    private String modelName;

    /**
     * 接口地址
     */
    private String baseUrl;

    // getter / setter
    public String getApiKey() {
        return apiKey;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    public String getModelName() {
        return modelName;
    }

    public void setModelName(String modelName) {
        this.modelName = modelName;
    }

    public String getBaseUrl() {
        return baseUrl;
    }

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }
}
