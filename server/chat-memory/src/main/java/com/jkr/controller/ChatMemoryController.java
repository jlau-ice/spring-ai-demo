package com.jkr.controller;

import jakarta.annotation.Resource;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import static org.springframework.ai.chat.memory.ChatMemory.CONVERSATION_ID;
import static org.springframework.ai.chat.memory.ChatMemory.DEFAULT_CONVERSATION_ID;

@RestController
@RequestMapping("/memory")
public class ChatMemoryController {

    @Resource(name = "deepseekChatClient")
    private ChatClient deepseekChatClient;

    @Resource(name = "qwenChatClient")
    private ChatClient qwenChatClient;

    /**
     * <a href="http://localhost:8008/memory/chat?question=%221+1%E7%AD%89%E4%BA%8E%E5%87%A0%22&userId=1&conversationId=1">测试</a>
     * 带记忆模式
     *
     * @param question       提问
     * @param userId         用户ID
     * @param conversationId 会话ID
     * @return Flux<String>
     */
    @GetMapping("/chat")
    public Flux<String> redisMemory(@RequestParam(name = "question", defaultValue = "1+1等于几") String question,
                             String userId,
                             String conversationId) {
        return deepseekChatClient
                .prompt(question)
                .advisors(advisorSpec ->
                        advisorSpec.param(CONVERSATION_ID, userId + ":" + conversationId))
                .stream().content();
    }


    /**
     * <a href="http://localhost:8008/memory/chat2?question=%221+1%E7%AD%89%E4%BA%8E%E5%87%A0%22&userId=1&conversationId=1">测试</a>
     * postgresql 带记忆模式
     *
     * @param question question
     * @return Flux<String>
     */
    @GetMapping("/chat2")
    public Flux<String> postgresqlMemory(@RequestParam(name = "question", defaultValue = "1+1等于几") String question,
                              String userId,
                              String conversationId) {
        return qwenChatClient.prompt(question).advisors(
                advisorSpec -> advisorSpec.param(CONVERSATION_ID, userId + "-" + conversationId))
        .stream().content();
    }

}


