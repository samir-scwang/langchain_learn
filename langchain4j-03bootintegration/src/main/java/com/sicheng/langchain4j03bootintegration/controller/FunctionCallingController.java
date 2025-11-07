package com.sicheng.langchain4j03bootintegration.controller;

import com.sicheng.langchain4j03bootintegration.pojo.InvoiceRequest;
import com.sicheng.langchain4j03bootintegration.service.FunctionAssistant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/invoice")
public class FunctionCallingController {

    @Autowired
    private FunctionAssistant functionAssistant;


    @PostMapping("/create")
    public String createInvoice(@RequestBody InvoiceRequest request) {
        // 拼接用户请求，让 LLM 触发工具调用
        String userMessage = String.format("帮我开一张发票，公司名称：%s，税号：%s，金额：%s",
                request.getCompanyName(),
                request.getDutyNumber(),
                request.getAmount());

        return functionAssistant.chat(userMessage);
    }
}
