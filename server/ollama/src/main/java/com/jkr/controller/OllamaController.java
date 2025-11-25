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
     * @param msg 询问信息
     * @return String
     */
    @GetMapping("/block")
    public String chat(@RequestParam(name = "msg") String msg) {
        return chatModel.call(msg);
    }

    /**
     * @param msg 询问信息
     * @return Flux<String>
     */
    @GetMapping("/stream")
    public Flux<String> stream(@RequestParam(name = "msg", defaultValue = "你是谁") String msg) {
        return chatModel.stream(msg);
    }
}








