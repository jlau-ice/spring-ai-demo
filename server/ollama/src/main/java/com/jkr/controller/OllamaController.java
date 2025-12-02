package com.jkr.controller;

import jakarta.annotation.Resource;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/ollama")
public class OllamaController {

    //方式1
    //@Resource(name = "ollamaChatModel")
    //private ChatModel chatModel;

    //方式2
    @Resource
    @Qualifier("ollamaChatModel")
    private ChatModel chatModel;

    public final ChatClient chatClient;
    public OllamaController(ChatClient.Builder builder) {
        this.chatClient = builder.build();
    }

    /**
     * <a href="http://127.0.0.1:8002/ollama/block">测试</a>
     *
     * @param question 询问信息
     * @return String
     */
    @GetMapping("/block")
    public String chat(@RequestParam(name = "question") String question) {
        return chatModel.call(question);
    }

    /**
     * <a href="http://127.0.0.1:8002/ollama/block">测试</a>
     *
     * @param question 询问信息
     * @return Flux<String>
     */
    @GetMapping("/stream")
    public Flux<String> stream(@RequestParam(name = "question", defaultValue = "你是谁") String question) {
        // 阻塞方式
        //return chatModel.call(question);
        // 流式方式
        return chatModel.stream(question);
    }

    /**
     * ChatClient 方式使用Ollama
     * <a href="http://127.0.0.1:8002/ollama/client">测试</a>
     * @param question 询问信息
     * @return Flux<String>
     */
    @GetMapping("/client")
    public Flux<String> client(@RequestParam(name = "question", defaultValue = "你是谁") String question) {
        // 阻塞方式
        //return chatClient.prompt().user(question).call().content();
        // 流式方式
        return chatClient.prompt().user(question).stream().content();
    }
}








