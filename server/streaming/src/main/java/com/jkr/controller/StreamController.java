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
@RequestMapping("/stream")
public class StreamController {

    // 通过ChatModel实现stream实现流式输出
    @Resource(name = "deepseek")
    private ChatModel deepseekChatModel;
    @Resource(name = "qwen")
    private ChatModel qwenChatModel;

    @GetMapping(value = "/chat/deepseek")
    public Flux<String> chatDeepSeekFlux(@RequestParam(name = "question", defaultValue = "你是谁") String question) {
        return deepseekChatModel.stream(question);
    }

    @GetMapping(value = "/chat/qwen")
    public Flux<String> chatQwenflux(@RequestParam(name = "question", defaultValue = "你是谁") String question) {
        return qwenChatModel.stream(question);
    }

    //通过ChatClient实现stream实现流式输出
    @Resource(name = "deepseekChatClient")
    private ChatClient deepseekChatClient;
    @Resource(name = "qwenChatClient")
    private ChatClient qwenChatClient;

    /**
     * <a href="http://127.0.0.1:8004/stream/client/deepseek?msg=%22%E4%BD%A0%E6%98%AF%E8%B0%81%22">测试</a>
     * @param question question
     * @return Flux<String>
     */
    @GetMapping(value = "/client/deepseek")
    public Flux<String> chatClientDeepSeekFlux(@RequestParam(name = "question", defaultValue = "你是谁") String question) {
        return deepseekChatClient.prompt(question).stream().content();
    }

    /**
     * <a href="http://127.0.0.1:8004/stream/client/qwen?msg=%22%E4%BD%A0%E6%98%AF%E8%B0%81%22">测试</a>
     * @param question question
     * @return Flux<String>
     */
    @GetMapping(value = "/client/qwen")
    public Flux<String> chatClientQwenFlux(@RequestParam(name = "question", defaultValue = "你是谁") String question) {
        return qwenChatClient.prompt(question).stream().content();
    }
}


