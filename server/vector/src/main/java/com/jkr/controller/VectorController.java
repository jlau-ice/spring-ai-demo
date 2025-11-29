package com.jkr.controller;


import com.alibaba.cloud.ai.dashscope.embedding.DashScopeEmbeddingModel;
import com.alibaba.cloud.ai.dashscope.embedding.DashScopeEmbeddingOptions;
import jakarta.annotation.Resource;
import org.springframework.ai.document.Document;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.embedding.EmbeddingRequest;
import org.springframework.ai.embedding.EmbeddingResponse;
import org.springframework.ai.ollama.OllamaEmbeddingModel;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/vector")
public class VectorController {

    @Resource
    @Qualifier("ollamaEmbeddingModel")
    private EmbeddingModel ollamaEmbeddingModel;

    @Resource
    private EmbeddingModel dashScopeEmbeddingModel;

    @Resource
    @Qualifier("pgVectorStore")
    private VectorStore pgVectorStore;


    @Resource
    @Qualifier("redisVectorStore")
    private VectorStore redisVectorStore;

    /**
     * 本地ollama模型 获取文本的向量
     * <a href="http://127.0.0.1:8011/vector/text2embed?msg=%E4%BD%A0%E5%A5%BD">测试</a>
     *
     * @param text 文本
     * @return EmbeddingResponse
     */
    @GetMapping("/text2embed")
    public EmbeddingResponse text2Embed(@RequestParam(name = "text", defaultValue = "你好") String text) {
        EmbeddingResponse embeddingResponse = ollamaEmbeddingModel.call(new EmbeddingRequest(List.of(text),
                DashScopeEmbeddingOptions.builder().withModel("bge-m3:latest").build()));
        System.out.println(Arrays.toString(embeddingResponse.getResult().getOutput()));
        return embeddingResponse;
    }


    /**
     * 阿里百炼平台模型 获取文本的向量
     * <a href="http://127.0.0.1:8011/vector/text2embed2?msg=%E4%BD%A0%E5%A5%BD">测试</a>
     *
     * @param text 文本
     * @return EmbeddingResponse
     */
    @GetMapping("/text2embed2")
    public EmbeddingResponse text2Embed2(@RequestParam(name = "text", defaultValue = "你好") String text) {
        EmbeddingResponse embeddingResponse = dashScopeEmbeddingModel.call(new EmbeddingRequest(List.of(text),
                DashScopeEmbeddingOptions.builder().withModel("text-embedding-v4").build()));
        System.out.println(Arrays.toString(embeddingResponse.getResult().getOutput()));
        return embeddingResponse;
    }


    /**
     * 向量化存储
     */
    @GetMapping("/add")
    public void add() {
        List<Document> documents = List.of(
                new Document("i study LLM"),
                new Document("i love java")
        );
        redisVectorStore.add(documents);
    }

    @GetMapping("/add2")
    public void add2() {
        List<Document> documents = List.of(
                new Document("i study LLM"),
                new Document("i love java")
        );
        pgVectorStore.add(documents);
    }

}