package com.sicheng.langchain4j03bootintegration.controller;

import com.sicheng.langchain4j03bootintegration.dto.TitleSuggestRequest;
import com.sicheng.langchain4j03bootintegration.dto.TitleSuggestResponse;
import com.sicheng.langchain4j03bootintegration.service.TitleSuggestService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class TitleController {

    private final TitleSuggestService titleSuggestService;

    /**
     * 传入一段文本，返回精炼标题（默认中文、≤16 字）
     */
    @PostMapping("/title/suggest")
    public TitleSuggestResponse suggest(@RequestBody TitleSuggestRequest req) {
        String text = req.getText();
        int maxLen = req.getMaxLen() == null ? 16 : req.getMaxLen();
        String lang = req.getLang() == null ? "zh" : req.getLang();

        String title = titleSuggestService.suggestTitle(text, maxLen, lang);
        return new TitleSuggestResponse(title);
    }
}
