package com.sicheng.langchain4j03bootintegration.controller;

import dev.langchain4j.model.chat.StreamingChatModel;
import dev.langchain4j.model.chat.response.ChatResponse;
import dev.langchain4j.model.chat.response.StreamingChatResponseHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RestController
public class StreamingController {
//
//    @Qualifier("openAiStreamingChatModel")
//    @Autowired
//    private StreamingChatModel streamingChatModel;

//    @GetMapping(value = "/stream", produces = "text/event-stream;charset=UTF-8")
//    public SseEmitter stream(@RequestParam(defaultValue = "你好，1+2等于几？") String question) {
//        SseEmitter emitter = new SseEmitter(0L); // 不超时
//
//        streamingChatModel.chat(question, new StreamingChatResponseHandler() {
//            @Override
//            public void onPartialResponse(String partial) {
//                try {
//                    emitter.send(
//                            SseEmitter.event()
//                                    .name("delta")
//                                    .data(partial, MediaType.valueOf("text/plain;charset=UTF-8"))
//                    );
//                } catch (Exception e) {
//                    emitter.completeWithError(e);
//                }
//            }
//
//            @Override
//            public void onCompleteResponse(ChatResponse complete) {
//                try {
//                    emitter.send(SseEmitter.event().name("done").data(""));
//                } catch (Exception ignored) {
//                } finally {
//                    emitter.complete();
//                }
//            }
//
//            @Override
//            public void onError(Throwable error) {
//                try {
//                    emitter.send(
//                            SseEmitter.event()
//                                    .name("error")
//                                    .data("⚠️ AI 罢工了: " + error.getMessage(),
//                                            MediaType.valueOf("text/plain;charset=UTF-8"))
//                    );
//                } catch (Exception ignored) {
//                } finally {
//                    emitter.completeWithError(error);
//                }
//            }
//        });
//
//        return emitter;
//    }
}
