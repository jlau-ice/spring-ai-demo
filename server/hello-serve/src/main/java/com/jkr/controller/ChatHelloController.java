package com.jkr.controller;

import jakarta.annotation.Resource;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/chat")
public class ChatHelloController {

    /**
     * 对话模型，调用阿里云百炼平台
     */
    @Resource
    private ChatModel chatModel;

    /**
     * <a href="http://127.0.0.1:8001/chat/block">测试</a>
     * 通用调用
     * @param question 询问信息
     * @return String
     */
    @GetMapping(value = "/block")
    public String doChat(@RequestParam(name = "question", defaultValue = "你是谁") String question) {
        return chatModel.call(question);
    }

    /**
     * <a href="http://127.0.0.1:8001/chat/stream">测试</a>
     * 流式返回调用
     * @param question 询问信息
     * @return Flux<String>
     */
    @GetMapping(value = "/stream")
    public Flux<String> stream(@RequestParam(name = "question", defaultValue = "你是谁") String question) {
        return chatModel.stream(question);
    }
}
