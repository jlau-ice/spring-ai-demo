package com.jkr.controller;

import jakarta.annotation.Resource;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.rag.advisor.RetrievalAugmentationAdvisor;
import org.springframework.ai.rag.retrieval.search.VectorStoreDocumentRetriever;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

/**
 * <a href="https://docs.spring.io/spring-ai/reference/api/retrieval-augmented-generation.html#_advanced_rag">doc</a>
 */
@RestController
@RequestMapping("/rag")
public class RagController {


    @Resource(name = "qwenChatClient")
    private ChatClient chatClient;
    @Resource
    @Qualifier("pgVectorStore")
    private VectorStore pgVectorStore;


    @Resource
    @Qualifier("redisVectorStore")
    private VectorStore redisVectorStore;


    /**
     * <a href="http://localhost:8012/rag/redis?code=00000">测试</a>
     *
     * @param code 错误码
     * @return Flux<String>
     */
    @GetMapping("/redis")
    public Flux<String> redisRag(String code) {
        String systemInfo = """
                你是一个运维工程师,按照给出的编码给出对应故障解释,否则回复找不到信息。
                """;

        RetrievalAugmentationAdvisor advisor = RetrievalAugmentationAdvisor.builder()
                .documentRetriever(VectorStoreDocumentRetriever.builder().vectorStore(redisVectorStore).build())
                .build();

        return chatClient
                .prompt()
                .system(systemInfo)
                .user(code)
                .advisors(advisor)
                .stream()
                .content();
    }


    /**
     * <a href="http://localhost:8012/rag/pg?code=00000">测试</a>
     *
     * @param code 错误码
     * @return Flux<String>
     */
    @GetMapping("/pg")
    public Flux<String> pgRag(String code) {
        String systemInfo = """
                你是一个运维工程师,按照给出的编码给出对应故障解释,否则回复找不到信息。
                """;

        RetrievalAugmentationAdvisor advisor = RetrievalAugmentationAdvisor.builder()
                .documentRetriever(VectorStoreDocumentRetriever.builder().vectorStore(pgVectorStore).build())
                .build();

        return chatClient
                .prompt()
                .system(systemInfo)
                .user(code)
                .advisors(advisor)
                .stream()
                .content();
    }

}
