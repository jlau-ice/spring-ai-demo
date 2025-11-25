package com.jkr.controller;

import jakarta.annotation.Resource;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/chat-model")
public class ChatModelController {

    //支持自动注入
    @Resource
    private ChatModel dashScopeChatModel;

    //ChatClient 不支持自动注入
    //@Resource
    //private ChatClient chatClient;

    /**
     * @param msg 询问信息
     * @return String
     */
    @GetMapping("/do-chat")
    public String doChat(@RequestParam(name = "msg", defaultValue = "你是谁") String msg) {
        return dashScopeChatModel.call(msg);
    }
}
