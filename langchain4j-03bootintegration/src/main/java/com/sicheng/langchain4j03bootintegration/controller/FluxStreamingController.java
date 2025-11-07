package com.sicheng.langchain4j03bootintegration.controller;

import com.sicheng.langchain4j03bootintegration.service.ChatAssistant;
import com.sicheng.langchain4j03bootintegration.service.ChatPersistenceAssistant;
import com.sicheng.langchain4j03bootintegration.service.ChatWithMemoryAssistant;
import dev.langchain4j.model.chat.StreamingChatModel;
import dev.langchain4j.model.chat.response.ChatResponse;
import dev.langchain4j.model.chat.response.StreamingChatResponseHandler;
import dev.langchain4j.service.spring.AiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
public class FluxStreamingController {
    @Autowired
    private ChatAssistant chatAssistant;

    @Qualifier("deepseekStreamChatModel")
    @Autowired
    private StreamingChatModel streamingChatModel;

    @Qualifier("chatWithMemoryAssistantMessage")
    @Autowired
    private ChatWithMemoryAssistant chatWithMemoryAssistant;

    @Autowired
    private ChatPersistenceAssistant chatPersistenceAssistant;
//    @GetMapping(value = "/flux-stream/chat", produces = "text/event-stream;charset=UTF-8")
//    @GetMapping(value = "/flux-stream/chat", produces = "text/plain;charset=UTF-8")
//    @GetMapping(value = "/flux-stream/chat", produces = "application/stream+json")
@GetMapping(value = "/flux-stream/chat", produces = "text/plain;charset=UTF-8")
    public Flux<String> chat(@RequestParam(value = "question", defaultValue = "你好，1+2等于几？") String question) {
//        return chatAssistant.chatFlux(question);
        return Flux.create(emitter -> {
            streamingChatModel.chat(question, new StreamingChatResponseHandler() {
                @Override
                public void onPartialResponse(String PartialResponse) {
                    emitter.next(PartialResponse);
                }

                @Override
                public void onCompleteResponse(ChatResponse chatResponse) {
                    emitter.complete();
                }

                @Override
                public void onError(Throwable throwable) {
                    emitter.error(throwable);
                }
            });
        });
    }
    @GetMapping(value = "/flux-stream/chat2")
    public void chat2(@RequestParam(value = "question", defaultValue = "你好，1+2等于几？") String question) {
       streamingChatModel.chat(question, new StreamingChatResponseHandler() {
           @Override
           public void onPartialResponse(String s) {
               System.out.println(s);
           }

           @Override
           public void onCompleteResponse(ChatResponse chatResponse) {
               System.out.println(chatResponse);
           }

           @Override
           public void onError(Throwable throwable) {
               System.out.println(throwable.toString());
           }
       });
    }

    @GetMapping(value = "/flux-stream/chatWithMemory", produces = "text/plain;charset=UTF-8")
    public Flux<String> chatWithMemory(@RequestParam(value = "question", defaultValue = "你好，1+2等于几？") String question) {
        return chatWithMemoryAssistant.chatWithMemory(Long.valueOf(3),question);
    }

//    @GetMapping(value = "/flux-stream/chatWithMemoryPersistence", produces = "text/plain;charset=UTF-8")
    public Flux<String> chatWithMemoryPersistence(@RequestParam(value = "question", defaultValue = "你好，1+2等于几？") String question) {
        return chatPersistenceAssistant.chatWithMemory(String.valueOf(3),question);
    }

//    @GetMapping(value = "/flux-stream/chatWithMemoryPersistence", produces = "text/plain;charset=UTF-8")
    public Flux<String> chatWithMemoryPersistence(
            @RequestParam(value = "question", defaultValue = "你好，1+2等于几？") String question,
            @RequestParam(value = "userId", defaultValue = "3") String userId) {
        return chatPersistenceAssistant.chatWithMemory(userId, question);
    }

}
