package com.sicheng.langchain4j03bootintegration.service;


import com.sicheng.langchain4j03bootintegration.dto.SessionSummary;
import dev.langchain4j.data.message.ChatMessage;

import java.util.List;

public interface RedisChatSessionService {
    SessionSummary createNewSession(String name);

    void rename(String sessionId, String name);

    void delete(String sessionId);

    List<SessionSummary> listSessions(int limit);

    List<ChatMessage> getHistory(String sessionId);
}
