package com.sicheng.langchain4j03bootintegration.controller;

import com.sicheng.langchain4j03bootintegration.service.ChatAssistant;
import dev.langchain4j.data.document.Document;
import dev.langchain4j.data.document.parser.apache.tika.ApacheTikaDocumentParser;
import dev.langchain4j.rag.content.retriever.EmbeddingStoreContentRetriever;
import dev.langchain4j.store.embedding.EmbeddingStore;
import dev.langchain4j.store.embedding.EmbeddingStoreIngestor;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

@RestController

public class RAGController {
private final ChatAssistant chatAssistant;
private final EmbeddingStore embeddingStore;

@Autowired
public RAGController(
        @Qualifier("RAGassistant") ChatAssistant chatAssistant,
        @Qualifier("embeddingStore") EmbeddingStore embeddingStore) {
    this.chatAssistant = chatAssistant;
    this.embeddingStore = embeddingStore;
}

    @GetMapping(value = "rag/add")
    public String testAdd() throws FileNotFoundException {
//"C:\Users\SichengWang\Desktop\国奖\2023－2024学年经济管理学院汪斯城国奖候选人材料.docx"
        FileInputStream fileInputStream = new FileInputStream("C:\\Users\\SichengWang\\Desktop\\国奖\\2023－2024学年经济管理学院汪斯城国奖候选人材料.docx");
        Document document = new ApacheTikaDocumentParser().parse(fileInputStream);
        EmbeddingStoreIngestor.ingest(document, embeddingStore);

        String result = chatAssistant.chat("汪斯城有哪些荣誉？");
        System.out.println(result);
        return result;
    }
}
