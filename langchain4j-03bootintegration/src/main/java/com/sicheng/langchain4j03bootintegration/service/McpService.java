package com.sicheng.langchain4j03bootintegration.service;

import reactor.core.publisher.Flux;

public interface McpService {
    Flux<String> chat(String question);
}
