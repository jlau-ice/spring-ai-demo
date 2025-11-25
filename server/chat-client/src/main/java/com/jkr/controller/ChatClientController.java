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
     * @param msg question
     * @return String
     */
    @GetMapping("/client")
    public String chatClient(@RequestParam(name = "msg", defaultValue = "2加9等于几") String msg) {
        return dashScopeChatClient.prompt().user(msg).call().content();
    }

    /**
     * @param msg question
     * @return String
     */
    @GetMapping("/model")
    public String chatModel(@RequestParam(name = "msg", defaultValue = "你是谁") String msg) {
        return dashScopeChatModel.call(msg);
    }
}


