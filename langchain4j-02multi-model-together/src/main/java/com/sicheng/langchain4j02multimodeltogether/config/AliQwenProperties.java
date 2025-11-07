package com.sicheng.langchain4j02multimodeltogether.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "llm.aliqwen")
@Data
public class AliQwenProperties {
    private String apiKey;
    private String baseUrl;
    private String modelName;
}
