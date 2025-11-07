package com.sicheng.langchain4j02multimodeltogether.controller;

import dev.langchain4j.model.chat.ChatModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class HelloLangChain4jController {
    @Qualifier("qwen")
    @Autowired
    private ChatModel qwenChatModel;

    @Qualifier("deepseek")
    @Autowired
    private ChatModel deepseekChatModel;

    @GetMapping("/langchain4j/qwen")
    public String helloQwen(@RequestParam(value = "question", defaultValue = "你是谁") String question) {
        log.info("hello langchain4j");
        String result = qwenChatModel.chat(question);
        System.out.println("调用大模型回复:" + result);
        return result;
    }

    @GetMapping("/langchain4j/deepseek")
    public String helloDeepseek(@RequestParam(value = "question", defaultValue = "你是谁") String question) {
        log.info("hello langchain4j");
        String result = deepseekChatModel.chat(question);
        System.out.println("调用大模型回复:" + result);
        return result;
    }
}
