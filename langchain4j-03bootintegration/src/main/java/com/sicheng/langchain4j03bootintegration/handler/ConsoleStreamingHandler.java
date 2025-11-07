package com.sicheng.langchain4j03bootintegration.handler;

import dev.langchain4j.model.chat.response.*;

public class ConsoleStreamingHandler implements StreamingChatResponseHandler {

    private final StringBuilder buffer = new StringBuilder();

    @Override
    public void onPartialResponse(String partialResponse) {
        System.out.print(partialResponse); // 打字机式输出
        buffer.append(partialResponse);
    }

    @Override
    public void onPartialThinking(PartialThinking partialThinking) {
        System.out.println("\n[PartialThinking] " + partialThinking.text());
    }

    @Override
    public void onPartialToolCall(PartialToolCall partialToolCall) {
        System.out.println("\n[PartialToolCall] " + partialToolCall.name()
                + " -> " + partialToolCall.partialArguments());
    }

    @Override
    public void onCompleteToolCall(CompleteToolCall completeToolCall) {
        System.out.println("\n[CompleteToolCall] " + completeToolCall.toString()
                + " -> " + completeToolCall.toolExecutionRequest());
    }

    @Override
    public void onCompleteResponse(ChatResponse completeResponse) {
        System.out.println("\n\n✅ 最终回答: " + buffer);
    }

    @Override
    public void onError(Throwable error) {
        System.err.println("\n❌ 出错了: " + error.getMessage());
        error.printStackTrace();
    }
}
