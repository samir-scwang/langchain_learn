package com.sicheng.langchain4j01helloworld.controller;

import dev.langchain4j.model.chat.ChatModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class HelloLangChain4jController {
    @Autowired
    private ChatModel chatModel;

    @GetMapping("/langchain4j/hello")
    public String hello(@RequestParam(value = "question",defaultValue = "你是谁") String question) {
        log.info("hello langchain4j");
        String result = chatModel.chat(question);
        System.out.println("调用大模型回复:" + result);
        return result;
    }
}
