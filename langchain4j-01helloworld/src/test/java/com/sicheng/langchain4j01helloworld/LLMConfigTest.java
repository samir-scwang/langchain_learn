package com.sicheng.langchain4j01helloworld;

import com.sicheng.langchain4j01helloworld.config.LLMConfig;
import com.sicheng.langchain4j01helloworld.config.QwenProperties;
import dev.langchain4j.model.chat.ChatModel;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
public class LLMConfigTest {


    @Autowired
    private QwenProperties qwenProperties;

    @Test
    void testApiKeyLoadedFromEnv() {
        String apiKey = qwenProperties.getApiKey();

        System.out.println("读取到的 API Key: " + apiKey);

        // 验证是否正确读取
        assertThat(apiKey)
                .as("检查环境变量 ALI_QWEN_API 是否加载成功")
                .isNotNull()
                .isNotBlank();

    }
}
