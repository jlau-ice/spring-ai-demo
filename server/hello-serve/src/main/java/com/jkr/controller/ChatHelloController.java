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
     * 通用调用
     *
     * @param msg 询问信息
     * @return String
     */
    @GetMapping(value = "/block")
    public String doChat(@RequestParam(name = "msg", defaultValue = "你是谁") String msg) {
        return chatModel.call(msg);
    }

    /**
     * 流式返回调用
     *
     * @param msg 询问信息
     * @return Flux<String>
     */
    @GetMapping(value = "/stream")
    public Flux<String> stream(@RequestParam(name = "msg", defaultValue = "你是谁") String msg) {
        return chatModel.stream(msg);
    }
}
