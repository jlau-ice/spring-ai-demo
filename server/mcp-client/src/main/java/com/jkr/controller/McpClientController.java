package com.jkr.controller;

import jakarta.annotation.Resource;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;


@RestController
@RequestMapping("/mcp")
public class McpClientController {
    @Resource
    private ChatClient chatClient;//使用mcp支持

    @Resource
    private ChatModel chatModel;//没有纳入tool支持，普通调用

    /**
     * <a href="http://localhost:8015/mcp/chat?question=%E4%B8%8A%E6%B5%B7">测试</a>
     *
     * @param question 问题
     * @return Flux<String>
     */
    @GetMapping("/chat")
    public Flux<String> chat(@RequestParam(name = "question", defaultValue = "北京") String question) {
        System.out.println("使用了mcp");
        return chatClient.prompt(question).stream().content();
    }

    /**
     * <a href="http://localhost:8015/mcp/no?question=%E4%B8%8A%E6%B5%B7">测试</a>
     *
     * @param question 问题
     * @return Flux<String>
     */
    @RequestMapping("/no")
    public Flux<String> chat2(@RequestParam(name = "question", defaultValue = "北京") String question) {
        System.out.println("未使用mcp");
        return chatModel.stream(question);
    }
}
