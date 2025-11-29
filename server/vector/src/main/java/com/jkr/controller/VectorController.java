package com.jkr.controller;


import com.alibaba.cloud.ai.dashscope.embedding.DashScopeEmbeddingOptions;
import jakarta.annotation.Resource;
import org.springframework.ai.document.Document;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.embedding.EmbeddingRequest;
import org.springframework.ai.embedding.EmbeddingResponse;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
     * <a href="http://127.0.0.1:8011/vector/add">测试</a>
     */
    @GetMapping("/add")
    public void add() {
        List<Document> documents = List.of(
                new Document("咖啡豆的最佳烘焙温度通常在 200°C 左右。"),
                new Document("今天的会议将在下午三点开始，主要讨论季度预算。"),
                new Document("大型语言模型（LLM）在自然语言处理领域取得了突破性进展。"),
                new Document("我最喜欢在清晨的湖边散步，那里的空气十分清新。"),
                new Document("红烧肉的秘诀在于小火慢炖和适量的冰糖。"),
                new Document("向量数据库是存储和检索高维向量数据的关键技术。"),
                new Document("如何通过梯度下降法优化神经网络的权重？"),
                new Document("制作美味拿铁的关键是牛奶的完美发泡。"),
                new Document("机器学习和深度学习是人工智能的两个主要分支。"),
                new Document("本年度的财务报告将于本周五前提交给董事会审阅。"));
        redisVectorStore.add(documents);
        pgVectorStore.add(documents);
    }


    /**
     * 相似度检索
     * <a href="http://127.0.0.1:8011/vector/get">测试</a>
     *
     * @param text 检索的文本
     * @return List<Document>
     */
    @GetMapping("/get")
    public Map<String, List<Document>> getAll(@RequestParam(name = "text", defaultValue = "AI 技术的核心发展方向是什么？") String text) {
        SearchRequest searchRequest = SearchRequest.builder().query(text).topK(2).build();
        System.out.println("redisVectorStore: " + redisVectorStore.similaritySearch(searchRequest));
        System.out.println("pgVectorStore: " + pgVectorStore.similaritySearch(searchRequest));
        redisVectorStore.similaritySearch(searchRequest);
        pgVectorStore.similaritySearch(searchRequest);
        HashMap<String, List<Document>> map = new HashMap<>();
        map.put("redisVectorStore", redisVectorStore.similaritySearch(searchRequest));
        map.put("pgVectorStore", pgVectorStore.similaritySearch(searchRequest));
        return map;
    }
}