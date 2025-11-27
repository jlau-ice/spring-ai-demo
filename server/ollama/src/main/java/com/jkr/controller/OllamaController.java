package com.jkr.controller;

import jakarta.annotation.Resource;
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
        return chatModel.stream(question);
    }
}








