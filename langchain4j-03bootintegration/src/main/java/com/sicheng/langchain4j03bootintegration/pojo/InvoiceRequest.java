package com.sicheng.langchain4j03bootintegration.pojo;

import lombok.Data;

@Data
public class InvoiceRequest {
    private String companyName;
    private String dutyNumber;
    private String amount;
}