package com.sicheng.langchain4j03bootintegration.controller;

import com.sicheng.langchain4j03bootintegration.dto.SessionSummary;
import com.sicheng.langchain4j03bootintegration.service.ChatPersistenceAssistant;
import com.sicheng.langchain4j03bootintegration.service.RedisChatSessionService;
import com.sicheng.langchain4j03bootintegration.service.impl.RedisChatSessionServiceImpl;
import dev.langchain4j.data.message.ChatMessage;
import dev.langchain4j.data.message.ChatMessageSerializer;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

import java.util.List;

// ChatController.java
@RestController
@RequestMapping
@RequiredArgsConstructor
public class ChatController {

    private final ChatPersistenceAssistant chatPersistenceAssistant;
    private final RedisChatSessionService chatSessionService; // 见第3节

    /**
     * 流式对话（带记忆）
     * 前端必须传 sessionId，同一 sessionId 下会话会持续累积
     */

    private final RedisChatSessionServiceImpl sessionService;

    @GetMapping(value = "/flux-stream/chatWithMemoryPersistence", produces = "text/plain;charset=UTF-8")
    public Flux<String> chatWithMemoryPersistence(
            @RequestParam String sessionId,
            @RequestParam String question) {

        // 容错：前端若传入 "undefined"/空值，则新建一个会话并返回其输出
        String effectiveSessionId = sessionId;
        if (effectiveSessionId == null || effectiveSessionId.isBlank() || "undefined".equalsIgnoreCase(effectiveSessionId)) {
            // 不阻塞：直接生成一个新的会话 ID 供当前请求使用，但不在这里重命名
            effectiveSessionId = java.util.UUID.randomUUID().toString();
        }

        // 可选：更新“最近使用时间”，用于侧边栏按时间排序
        sessionService.touch(effectiveSessionId);

        // 直接用字符串 sessionId 作为 memoryId
        return chatPersistenceAssistant.chatWithMemory(effectiveSessionId, question);
    }

    // ===== 侧边栏需要的会话管理接口 =====

    /** 创建新会话（返回新 sessionId 和名称） */
    @PostMapping("/session/new")
    public SessionSummary createSession(@RequestParam(required = false) String name) {
        return chatSessionService.createNewSession(name);
    }

    /** 重命名会话 */
    @PostMapping("/session/rename")
    public void rename(@RequestParam String sessionId, @RequestParam String name) {
        chatSessionService.rename(sessionId, name);
    }

    /** 删除会话（含历史消息） */
    @DeleteMapping("/session/{sessionId}")
    public void delete(@PathVariable String sessionId) {
        chatSessionService.delete(sessionId);
    }

    /** 列出会话（按最近更新时间倒序） */
    @GetMapping("/sessions")
    public List<SessionSummary> sessions(
            @RequestParam(defaultValue = "50") int limit) {
        return chatSessionService.listSessions(limit);
    }

    /** 读取某个会话的历史消息（给前端初始化渲染） */
    @GetMapping(value = "/history", produces = "application/json;charset=UTF-8")
    public String history(@RequestParam String sessionId) {
        List<ChatMessage> messages = chatSessionService.getHistory(sessionId);
        return ChatMessageSerializer.messagesToJson(messages);
    }
}
