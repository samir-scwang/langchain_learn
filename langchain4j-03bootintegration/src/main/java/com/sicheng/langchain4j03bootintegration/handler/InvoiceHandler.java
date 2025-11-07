package com.sicheng.langchain4j03bootintegration.handler;

import dev.langchain4j.agent.tool.P;
import dev.langchain4j.agent.tool.Tool;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class InvoiceHandler {

    @Tool("根据用户提交的开票信息进行开票")
    public String handle(@P("公司名称") String companyName,
                         @P("税号") String dutyNumber,
                         @P("金额保留两位有效数字") String amount)  throws Exception{
        System.out.println("FunctionCalling");
        return "开票成功";
    }
}
