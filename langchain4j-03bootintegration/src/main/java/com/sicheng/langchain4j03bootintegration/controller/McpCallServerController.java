package com.sicheng.langchain4j03bootintegration.controller;

import com.sicheng.langchain4j03bootintegration.service.McpService;
import dev.langchain4j.mcp.McpToolProvider;
import dev.langchain4j.mcp.client.DefaultMcpClient;
import dev.langchain4j.mcp.client.McpClient;
import dev.langchain4j.mcp.client.transport.McpTransport;
import dev.langchain4j.mcp.client.transport.stdio.StdioMcpTransport;
import dev.langchain4j.model.chat.StreamingChatModel;
import dev.langchain4j.service.AiServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.util.List;
import java.util.Map;

@RestController
public class McpCallServerController {
    @Qualifier("deepseekStreamChatModel")
    @Autowired
    private StreamingChatModel streamingChatModel;
    /** 1. 构建 McpTransport 协议
     * 1.1 cmd：启动 Windows 命令行解释器。
     * 1.2 /c：告诉 cmd 执行完后面的命令后关闭自身。
     * 1.3 npx：npx = npm execute package，Node.js 的一个工具，用于执行 npm 包中的可执行文件。
     * 1.4 -y 或 --yes：自动确认操作（类似于默认接受所有提示）。
     * 1.5 @baidumap/mcp-server-baidu-map：要通过 npx 执行的 npm 包名。
     * 1.6 BAIDU_MAP_API_KEY 是访问百度地图开放平台 API 的 AK。
     */

    @GetMapping("/mcp/chat")
    public Flux<String> chat(@RequestParam("question") String question) {
        Map<String, String> baiduMapApiKey = Map.of("BAIDU_MAP_API_KEY", System.getenv("BAIDU_MAP_API_KEY"));
        System.out.println(baiduMapApiKey);
        McpTransport transport = new StdioMcpTransport.Builder()
                .command(List.of("cmd", "/c", "npx", "-y", "@baidumap/mcp-server-baidu-map"))
                .environment(Map.of("BAIDU_MAP_API_KEY", System.getenv("BAIDU_MAP_API_KEY")))
                .build();

        McpClient mcpClient = new DefaultMcpClient.Builder()
                .transport(transport)
                .build();
        McpToolProvider toolProvider = McpToolProvider.builder()
                .mcpClients(mcpClient)
                .build();

        McpService mcpService = AiServices.builder(McpService.class)
                .streamingChatModel(streamingChatModel)
                .toolProvider(toolProvider)
                .build();

        return mcpService.chat(question);
    }
}
