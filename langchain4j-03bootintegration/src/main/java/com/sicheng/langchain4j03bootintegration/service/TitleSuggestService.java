package com.sicheng.langchain4j03bootintegration.service;

public interface TitleSuggestService {
    String suggestTitle(String text, int maxLen, String lang);
}
