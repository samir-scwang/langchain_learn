package com.sicheng.langchain4j02multimodeltogether.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "llm.deepseek")
@Data
public class DeepSeekProperties {
    private String apiKey;
    private String baseUrl;
    private String modelName;
}
