package com.sicheng.langchain4j03bootintegration.controller;

import ch.qos.logback.core.model.Model;
import com.sicheng.langchain4j03bootintegration.service.ChatAssistant;
import dev.langchain4j.model.chat.ChatModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MultiModelController {
    @Qualifier("deepseekChatModel")
    @Autowired
    private ChatModel deepseekChatModel;

    @Qualifier("qwenChatModel")
    @Autowired
    private ChatModel qwenChatModel;

    @Qualifier("openAiChatModel")
    @Autowired
    private ChatModel bootChatModel;

    @Autowired
    private ChatAssistant chatAssistant;
    @GetMapping("/langchain4j/chat/qwen")
    public String chatQwen(@RequestParam(value = "question", defaultValue = "你是谁") String question) {
        String result = qwenChatModel.chat(question);
        return result;
    }

    @GetMapping("/langchain4j/chat/deepseek")
    public String chatDeepseek(@RequestParam(value = "question", defaultValue = "你是谁") String question) {
        String result = deepseekChatModel.chat(question);
        return result;
    }

    @GetMapping("/langchain4j/chat/boot")
    public String boot(@RequestParam(value = "question", defaultValue = "你是谁") String question) {
        String result = bootChatModel.chat(question);
        return result;
    }

    @GetMapping("/langchain4j/chat/assistant")
    public String assistant(@RequestParam(value = "question", defaultValue = "你是谁") String question) {
        String result = chatAssistant.chat(question);
        return result;
    }

    @GetMapping("/langchain4j/chat/add-test")
    public String chatAddTest() {
        // 这里我们直接问“1+2等于几”，LangChain4j 会把这个问题交给大模型，
        // 大模型可能会自动调用 @Tool("add") 方法来计算。
        return chatAssistant.chat("1+2等于几");
    }
}
