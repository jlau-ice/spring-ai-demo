package com.jkr.controller;

import jakarta.annotation.Resource;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/client")
public class ChatClientController {

    @Resource
    private ChatModel dashScopeChatModel;

    @Resource
    private ChatClient dashScopeChatClient;

    //private final ChatClient dashScopeChatClient;
    //public ChatClientController(ChatModel dashScopeChatModel) {
    //    this.dashScopeChatClient = ChatClient.builder(dashScopeChatModel).build();
    //}

    /**
     * <a href="http://127.0.0.1:8003/client/client?question=%22%E4%BD%A0%E6%98%AF%E8%B0%81%22">测试</a>
     *
     * @param question question
     * @return String
     */
    @GetMapping("/client")
    public String chatClient(@RequestParam(name = "question", defaultValue = "2加9等于几") String question) {
        return dashScopeChatClient.prompt().user(question).call().content();
    }

    /**
     * <a href="http://127.0.0.1:8003/client/model?question=%22%E4%BD%A0%E6%98%AF%E8%B0%81%22">测试</a>
     *
     * @param question question
     * @return String
     */
    @GetMapping("/model")
    public String chatModel(@RequestParam(name = "question", defaultValue = "你是谁") String question) {
        return dashScopeChatModel.call(question);
    }
}


