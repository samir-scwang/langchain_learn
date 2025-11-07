package com.sicheng.langchain4j03bootintegration.config;

import com.sicheng.langchain4j03bootintegration.handler.InvoiceHandler;
import com.sicheng.langchain4j03bootintegration.properties.DeepseekChatModelProperties;
import com.sicheng.langchain4j03bootintegration.properties.QwenChatModelProperties;
import com.sicheng.langchain4j03bootintegration.properties.QwenLongChatModelProperties;
import com.sicheng.langchain4j03bootintegration.service.ChatAssistant;
import com.sicheng.langchain4j03bootintegration.service.ChatPersistenceAssistant;
import com.sicheng.langchain4j03bootintegration.service.ChatWithMemoryAssistant;
import com.sicheng.langchain4j03bootintegration.service.FunctionAssistant;
import dev.langchain4j.agent.tool.ToolSpecification;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.memory.chat.ChatMemoryProvider;
import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import dev.langchain4j.memory.chat.TokenWindowChatMemory;
import dev.langchain4j.http.client.HttpClientBuilder;
import dev.langchain4j.http.client.spring.restclient.SpringRestClientBuilderFactory;
import dev.langchain4j.model.TokenCountEstimator;
import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.model.chat.StreamingChatModel;
import dev.langchain4j.model.chat.request.json.JsonObjectSchema;
import dev.langchain4j.model.openai.OpenAiChatModel;
import dev.langchain4j.model.openai.OpenAiStreamingChatModel;
import dev.langchain4j.model.openai.OpenAiTokenCountEstimator;
import dev.langchain4j.rag.content.retriever.EmbeddingStoreContentRetriever;
import dev.langchain4j.service.AiServices;
import dev.langchain4j.service.spring.AiService;
import dev.langchain4j.service.tool.ToolExecutor;
import dev.langchain4j.store.embedding.EmbeddingStore;
import dev.langchain4j.store.embedding.inmemory.InMemoryEmbeddingStore;
import dev.langchain4j.store.memory.chat.InMemoryChatMemoryStore;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@Configuration
public class LangChainConfig {
    private final DeepseekChatModelProperties deepseekproperties;

    private final QwenChatModelProperties qwenProperties;

    private final QwenLongChatModelProperties qwenLongChatModelProperties;

    private final RedisChatMemoryStore redisChatMemoryStore;

    private final HttpClientBuilder httpClientBuilder;

    public LangChainConfig(DeepseekChatModelProperties properties, QwenChatModelProperties qwenProperties, QwenLongChatModelProperties qwenLongChatModelProperties, RedisChatMemoryStore redisChatMemoryStore) {
        this.deepseekproperties = properties;
        this.qwenProperties = qwenProperties;
        this.qwenLongChatModelProperties = qwenLongChatModelProperties;
        this.redisChatMemoryStore = redisChatMemoryStore;
        this.httpClientBuilder = new SpringRestClientBuilderFactory().create();
    }

    @Bean
    public ChatModel deepseekChatModel() {
        return OpenAiChatModel.builder()
                .apiKey(deepseekproperties.getApiKey())
                .modelName(deepseekproperties.getModelName())
                .baseUrl(deepseekproperties.getBaseUrl())
                .httpClientBuilder(httpClientBuilder)
                .build();
    }

    @Bean
    public ChatModel qwenChatModel() {
        return OpenAiChatModel.builder()
                .apiKey(qwenProperties.getApiKey())
                .baseUrl(qwenProperties.getBaseUrl())
                .modelName(qwenProperties.getModelName())
                .httpClientBuilder(httpClientBuilder)
                .build();
    }
    @Bean
    public StreamingChatModel deepseekStreamChatModel() {
        return OpenAiStreamingChatModel.builder()
                .apiKey(deepseekproperties.getApiKey())
                .baseUrl(deepseekproperties.getBaseUrl())
                .modelName(deepseekproperties.getModelName())
                .httpClientBuilder(httpClientBuilder)
                .build();
    }

    @Bean
    public StreamingChatModel qwenLongStreamChatModel() {
        return OpenAiStreamingChatModel.builder()
                .apiKey(qwenLongChatModelProperties.getApiKey())
                .baseUrl(qwenLongChatModelProperties.getBaseUrl())
                .modelName(qwenLongChatModelProperties.getModelName())
                .httpClientBuilder(httpClientBuilder)
                .build();
    }

    @Bean
    public ChatWithMemoryAssistant chatWithMemoryAssistantMessage(@Qualifier("qwenLongStreamChatModel") StreamingChatModel streamingChatModel) {

        return AiServices.builder(ChatWithMemoryAssistant.class)
                .streamingChatModel(streamingChatModel)
                .chatMemoryProvider(memoryid ->MessageWindowChatMemory.withMaxMessages(100))
                .build();
    }
    @Bean
    public ChatWithMemoryAssistant chatWithMemoryAssistantToken(@Qualifier("qwenLongStreamChatModel") StreamingChatModel streamingChatModel) {
        TokenCountEstimator openAiTokenCountEstimator = new OpenAiTokenCountEstimator("gpt-4");
        return AiServices.builder(ChatWithMemoryAssistant.class)
                .streamingChatModel(streamingChatModel)
                .chatMemoryProvider(memoryid -> TokenWindowChatMemory.withMaxTokens(100,openAiTokenCountEstimator))
                .build();
    }

    @Bean
    public ChatPersistenceAssistant chatPersistenceAssistant(@Qualifier("deepseekStreamChatModel") StreamingChatModel streamingChatModel) {
        ChatMemoryProvider chatMemoryProvider = memoryid -> MessageWindowChatMemory.builder().id(String.valueOf(memoryid)).maxMessages(1000).chatMemoryStore(redisChatMemoryStore).build();
//        ChatMemoryProvider chatMemoryProvider = new ChatMemoryProvider() {
//            @Override
//            public ChatMemory get(Object memoryId) {
//                return MessageWindowChatMemory.builder()
//                        .id(memoryId)
//                        .maxMessages(1000)
//                        .chatMemoryStore(redisChatMemoryStore)
//                        .build();
//            }
//        };
        return AiServices.builder(ChatPersistenceAssistant.class)
                .streamingChatModel(streamingChatModel)
                .chatMemoryProvider(chatMemoryProvider)
                .build();
    }

    @Bean
    public ChatAssistant chatAssistant(@Qualifier("deepseekChatModel") ChatModel streamingChatModel) {
        return AiServices.builder(ChatAssistant.class)
                .chatModel(streamingChatModel)
                .build();
    }

    //FunctionCalling
//    @Bean
//    public FunctionAssistant functionAssistant(@Qualifier("deepseekChatModel") ChatModel chatModel) {
////        ToolSpecification.builder()
//        ToolSpecification toolSpecification = ToolSpecification.builder()
//                .name("开具发票助手") // 工具名称
//                .description("根据用户提交的开票信息，开具发票") // 工具描述
//                .parameters(JsonObjectSchema.builder() // 定义参数
//                        .addStringProperty("companyName", "公司名称")
//                        .addStringProperty("dutyNumber", "税号序列")
//                        .addStringProperty("amount", "开票金额，保留两位有效数字")
//                        .build())
//                .build();
//        ToolExecutor toolExecutor = (toolExecutionRequest, memoryId) -> {
//            System.out.println(toolExecutionRequest.id());
//            System.out.println(toolExecutionRequest.name());
//            String arguments1 = toolExecutionRequest.arguments();
//            System.out.println("arguments1***》 " + arguments1);
//            return "开具成功";
//        };
//        return AiServices.builder(FunctionAssistant.class)
//                .chatModel(chatModel)
//                .tools(Map.of(toolSpecification, toolExecutor)) // 工具注册
//                .build();
//    }
    @Bean
    public FunctionAssistant functionAssistant(@Qualifier("deepseekChatModel") ChatModel chatModel) {

        return AiServices.builder(FunctionAssistant.class)
                .chatModel(chatModel)
                .tools(new InvoiceHandler()) // 工具注册
                .build();
    }

    //    @Bean
//    public InMemoryChatMemoryStore<TextSegment> embeddingStore() {
//        return new InMemoryChatMemoryStore<>();
//    }
    @Bean("embeddingStore")
    public EmbeddingStore<TextSegment> embeddingStore() {
        return new InMemoryEmbeddingStore<>();
    }


    @Bean
    public ChatAssistant RAGassistant(ChatModel deepseekChatModel,
                                      EmbeddingStore<TextSegment> embeddingStore) {
        return AiServices.builder(ChatAssistant.class)
                .chatModel(deepseekChatModel)
                .chatMemory(MessageWindowChatMemory.withMaxMessages(50))
                .contentRetriever(EmbeddingStoreContentRetriever.from(embeddingStore))
                .build();
    }
}
