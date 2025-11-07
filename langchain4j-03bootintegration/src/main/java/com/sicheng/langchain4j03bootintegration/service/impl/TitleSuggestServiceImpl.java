package com.sicheng.langchain4j03bootintegration.service.impl;

import com.sicheng.langchain4j03bootintegration.service.ChatAssistant;
import com.sicheng.langchain4j03bootintegration.service.TitleAssistant;
import com.sicheng.langchain4j03bootintegration.service.TitleSuggestService;
import dev.langchain4j.data.message.AiMessage;
import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.model.output.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
public class TitleSuggestServiceImpl implements TitleSuggestService {
    @Autowired
    private TitleAssistant chatModel; // 非流式

    @Override
    public String suggestTitle(String text, int maxLen, String lang) {
        if (text == null || text.isBlank()) return "未命名会话";
        if (maxLen <= 0) maxLen = 16;
        if (lang == null || lang.isBlank()) lang = "zh";

        String title = chatModel.suggest(text, maxLen, lang);

        // 轻度清洗与兜底
        if (title == null || title.isBlank()) title = "未命名会话";
        title = title.replaceAll("[\\r\\n\\t\"'`]+", "").trim();
        if (title.length() > maxLen) title = title.substring(0, maxLen);
        return title.isBlank() ? "未命名会话" : title;
    }

}
