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
     * <a href="http://127.0.0.1:8016/mcp/call?question=%E5%BD%93%E5%89%8D%E7%9A%84%E6%95%B0%E6%8D%AE%E5%BA%93%20public%20%E6%A8%A1%E5%BC%8F%E4%B8%8B%E6%9C%89%E5%A4%9A%E5%B0%91%E6%95%B0%E6%8D%AE%E8%A1%A8%EF%BC%8C%E5%9B%9B%E5%BC%A0%E8%A1%A8%E5%88%86%E5%88%AB%E6%98%AF%E4%BB%80%E4%B9%88%EF%BC%8C%E5%B8%AE%E6%88%91%E5%88%86%E5%88%AB%E7%BB%9F%E8%AE%A1%E4%BB%96%E4%BB%AC%E7%9A%84%E6%95%B0%E6%8D%AE%E6%95%B0%E9%87%8F">测试</a>
     * @param question 问题
     * @return 回复
     */
    @GetMapping("/call")
    public Flux<String> chat(String question) {
        return chatClient.prompt(question).stream().content();
    }

}

