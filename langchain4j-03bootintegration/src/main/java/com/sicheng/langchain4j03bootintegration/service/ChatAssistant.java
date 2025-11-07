package com.sicheng.langchain4j03bootintegration.service;

import dev.langchain4j.agent.tool.Tool;
import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.spring.AiService;
import dev.langchain4j.service.spring.AiServiceWiringMode;
import reactor.core.publisher.Flux;

//@AiService(wiringMode = AiServiceWiringMode.EXPLICIT, streamingChatModel = "qwenStreamChatModel")
public interface ChatAssistant {
    @SystemMessage("你的回答很凶,很不耐烦")
    String chat(String prompt);

    Flux<String> chatFlux(String prompt);

    // 添加一个工具方法
    @Tool("返回两个数字的和")
    int add(int a, int b);

}
