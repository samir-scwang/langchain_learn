package com.sicheng.langchain4j03bootintegration.service;

import dev.langchain4j.service.MemoryId;
import dev.langchain4j.service.UserMessage;
import reactor.core.publisher.Flux;

public interface ChatPersistenceAssistant {
    Flux<String> chatWithMemory(@MemoryId String Userid, @UserMessage String prompt);
}
