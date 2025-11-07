package com.sicheng.langchain4j03bootintegration.config;

import dev.langchain4j.data.message.ChatMessage;
import dev.langchain4j.data.message.ChatMessageDeserializer;
import dev.langchain4j.data.message.ChatMessageSerializer;
import dev.langchain4j.data.message.ChatMessageType;
import dev.langchain4j.data.message.UserMessage;
import dev.langchain4j.data.message.Content;
import dev.langchain4j.data.message.TextContent; // ★ 关键：引入文本内容类型
import dev.langchain4j.store.memory.chat.ChatMemoryStore;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class RedisChatMemoryStore implements ChatMemoryStore {

    private static final String CHAT_MEMORY_PREFIX = "CHAT_MEMORY:";
    private static final String ZSET_SESSIONS = "CHAT_SESSIONS";
    private static final String SESSION_NAME_KEY = "CHAT_SESSION:%s:NAME";

    private final RedisTemplate<String, String> redisTemplate;

    @Override
    public List<ChatMessage> getMessages(Object memoryId) {
        String ret = redisTemplate.opsForValue().get(CHAT_MEMORY_PREFIX + memoryId);
        return ChatMessageDeserializer.messagesFromJson(ret);
    }

    @Override
    public void updateMessages(Object memoryId, List<ChatMessage> list) {
        String key = CHAT_MEMORY_PREFIX + memoryId;
        redisTemplate.opsForValue().set(key, ChatMessageSerializer.messagesToJson(list));

        // 维护会话列表/标题/最近时间
        long now = System.currentTimeMillis();
        String sessionId = String.valueOf(memoryId);
        redisTemplate.opsForZSet().add(ZSET_SESSIONS, sessionId, now);

        String titleKey = String.format(SESSION_NAME_KEY, sessionId);
        if (!Boolean.TRUE.equals(redisTemplate.hasKey(titleKey))) {
            String title = firstUserText(list);
            if (title == null || title.isBlank()) {
                title = "未命名会话";
            } else {
                title = title.strip();
                if (title.length() > 30) title = title.substring(0, 30) + "…";
            }
            redisTemplate.opsForValue().setIfAbsent(titleKey, title);
        }
    }

    @Override
    public void deleteMessages(Object memoryId) {
        redisTemplate.delete(CHAT_MEMORY_PREFIX + memoryId);
    }

    /** 取第一条 USER 文本：先 singleText()，再遍历 contents() 中的 TextContent */
    private static String firstUserText(List<ChatMessage> messages) {
        if (messages == null || messages.isEmpty()) return null;
        for (ChatMessage m : messages) {
            if (m == null) continue;
            if (m.type() == ChatMessageType.USER) {
                UserMessage um = (UserMessage) m;

                // 1) 优先单段文本
                String t = um.singleText();
                if (t != null && !t.isBlank()) return t;

                // 2) 否则从 contents() 里找纯文本片段
                if (um.contents() != null && !um.contents().isEmpty()) {
                    StringBuilder sb = new StringBuilder();
                    for (Content c : um.contents()) {
                        if (c instanceof TextContent tc && tc.text() != null) {
                            sb.append(tc.text());
                        }
                    }
                    String joined = sb.toString();
                    if (!joined.isBlank()) return joined;
                }

                // 找到第一条 USER 即可退出
                break;
            }
        }
        return null;
    }
}
