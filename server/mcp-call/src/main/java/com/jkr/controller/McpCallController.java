package com.jkr.controller;

import jakarta.annotation.Resource;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;


@RestController
@RequestMapping("/mcp")
public class McpCallController {
    @Resource
    private ChatClient chatClient; //添加了MCP调用能力

    @Resource
    private ChatModel chatModel; //没有添加MCP调用能力

    /**
     * 添加了MCP调用能力
     *
     * @param question 问题
     * @return 回复
     */
    @GetMapping("/call")
    public Flux<String> chat(String question) {
        return chatClient.prompt(question).stream().content();
    }

}

