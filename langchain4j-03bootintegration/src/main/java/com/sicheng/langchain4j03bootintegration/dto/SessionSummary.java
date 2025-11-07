package com.sicheng.langchain4j03bootintegration.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SessionSummary {
    private String id;
    private String name;
    private long updatedAt;
}