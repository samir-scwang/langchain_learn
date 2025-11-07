package com.sicheng.langchain4j03bootintegration.service.impl;

import com.sicheng.langchain4j03bootintegration.config.RedisChatMemoryStore;
import com.sicheng.langchain4j03bootintegration.dto.SessionSummary;
import com.sicheng.langchain4j03bootintegration.service.RedisChatSessionService;
import dev.langchain4j.data.message.ChatMessage;
import dev.langchain4j.data.message.ChatMessageType;
import dev.langchain4j.data.message.UserMessage;
import dev.langchain4j.data.message.Content;
import dev.langchain4j.data.message.TextContent;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Service
public class RedisChatSessionServiceImpl implements RedisChatSessionService {
    private static final String ZSET_SESSIONS = "CHAT_SESSIONS";               // ZSET: sessionId -> lastUpdated
    private static final String SESSION_NAME_KEY = "CHAT_SESSION:%s:NAME";     // String
    private static final String CHAT_MEMORY_PREFIX = "CHAT_MEMORY:";           // 复用你现有的消息存储前缀

    private final RedisTemplate<String, String> redisTemplate;
    private final RedisChatMemoryStore redisChatMemoryStore;

    public RedisChatSessionServiceImpl(RedisTemplate<String, String> redisTemplate,
                                       RedisChatMemoryStore redisChatMemoryStore) {
        this.redisTemplate = redisTemplate;
        this.redisChatMemoryStore = redisChatMemoryStore;
    }

    public SessionSummary createNewSession(String name) {
        String sessionId = UUID.randomUUID().toString();
        long now = System.currentTimeMillis();
        if (name == null || name.isBlank()) name = "新的会话";
        redisTemplate.opsForZSet().add(ZSET_SESSIONS, sessionId, now);
        redisTemplate.opsForValue().set(String.format(SESSION_NAME_KEY, sessionId), name);
        return new SessionSummary(sessionId, name, now);
    }

    public void rename(String sessionId, String name) {
        redisTemplate.opsForValue().set(String.format(SESSION_NAME_KEY, sessionId), name);
        touch(sessionId); // 更新最近时间，保持排序
    }

    public void delete(String sessionId) {
        redisTemplate.opsForZSet().remove(ZSET_SESSIONS, sessionId);
        redisTemplate.delete(String.format(SESSION_NAME_KEY, sessionId));
        redisTemplate.delete(CHAT_MEMORY_PREFIX + sessionId); // 删除历史
    }

    public List<SessionSummary> listSessions(int limit) {
        Set<ZSetOperations.TypedTuple<String>> tuples =
                redisTemplate.opsForZSet().reverseRangeWithScores(ZSET_SESSIONS, 0, limit - 1);
        if (tuples == null) return List.of();
        List<SessionSummary> list = new ArrayList<>();
        for (ZSetOperations.TypedTuple<String> t : tuples) {
            String id = t.getValue();
            long updated = t.getScore() == null ? 0L : t.getScore().longValue();
            String name = redisTemplate.opsForValue().get(String.format(SESSION_NAME_KEY, id));
            list.add(new SessionSummary(id, name, updated));
        }
        return list;
    }

    public List<ChatMessage> getHistory(String sessionId) {
        return redisChatMemoryStore.getMessages(sessionId);
    }

    /** 更新“最近使用时间” */
    public void touch(String sessionId) {
        redisTemplate.opsForZSet().add(ZSET_SESSIONS, sessionId, System.currentTimeMillis());
    }

    /** 首次出现时，为会话生成标题（用第一句用户问题的前若干字） */
    public void ensureTitleOnFirstMessage(String sessionId, List<ChatMessage> messages) {
        String key = String.format(SESSION_NAME_KEY, sessionId);
        if (Boolean.TRUE.equals(redisTemplate.hasKey(key))) return;

        String title = "未命名会话";
        if (messages != null && !messages.isEmpty()) {
            // 找第一条 USER 消息作为标题来源
            for (ChatMessage m : messages) {
                if (m != null && m.type() == ChatMessageType.USER) {
                    UserMessage um = (UserMessage) m;

                    // 1) 优先单段文本
                    String t = um.singleText();
                    if (t != null && !t.isBlank()) {
                        title = t.strip();
                    } else if (um.contents() != null && !um.contents().isEmpty()) {
                        // 2) 否则从 contents() 里找纯文本片段
                        StringBuilder sb = new StringBuilder();
                        for (Content c : um.contents()) {
                            if (c instanceof TextContent tc && tc.text() != null) {
                                sb.append(tc.text());
                            }
                        }
                        String joined = sb.toString();
                        if (!joined.isBlank()) {
                            title = joined.strip();
                        }
                    }

                    // 截断到 30 字符
                    if (title.length() > 30) {
                        title = title.substring(0, 30) + "…";
                    }
                    break;
                }
            }
        }
        redisTemplate.opsForValue().setIfAbsent(key, title);
    }
}
