package com.sicheng.langchain4j03bootintegration.dto;

import lombok.Data;

@Data
public class TitleSuggestRequest {
    private String text;     // 必填：用来摘要成标题的内容（建议用“第一条用户消息”）
    private Integer maxLen;  // 选填：最大字数（默认 16）
    private String lang;     // 选填：语言（"zh" | "en"... 默认 zh）
}
