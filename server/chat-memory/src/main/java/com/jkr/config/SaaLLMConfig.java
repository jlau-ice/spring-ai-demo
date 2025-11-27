package com.jkr.config;

import com.alibaba.cloud.ai.dashscope.api.DashScopeApi;
import com.alibaba.cloud.ai.dashscope.chat.DashScopeChatModel;
import com.alibaba.cloud.ai.dashscope.chat.DashScopeChatOptions;
import com.alibaba.cloud.ai.memory.redis.RedisChatMemoryRepository;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.memory.MessageWindowChatMemory;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.prompt.ChatOptions;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SaaLLMConfig {

    private final String DEEPSEEK_MODEL = "deepseek-v3.1";
    private final String QWEN_MODEL = "qwen-plus-2025-09-11";

    @Bean(name = "deepseek")
    public ChatModel deepSeek() {
        return DashScopeChatModel
                .builder()
                .dashScopeApi(DashScopeApi.builder().apiKey(System.getenv("DASH_SCOPE_API_KEY")).build())
                .defaultOptions(DashScopeChatOptions.builder().withModel(DEEPSEEK_MODEL).build())
                .build();
    }

    @Bean(name = "qwen")
    public ChatModel qwen() {
        return DashScopeChatModel
                .builder()
                .dashScopeApi(DashScopeApi.builder().apiKey(System.getenv("DASH_SCOPE_API_KEY")).build())
                .defaultOptions(DashScopeChatOptions.builder().withModel(QWEN_MODEL).build())
                .build();
    }

    @Bean(name = "deepseekChatClient")
    public ChatClient deepseekChatClient(@Qualifier("deepseek") ChatModel deepseek,
                                         RedisChatMemoryRepository redisChatMemoryRepository) {

        MessageWindowChatMemory windowChatMemory = MessageWindowChatMemory
                .builder()
                .chatMemoryRepository(redisChatMemoryRepository)
                .maxMessages(100)
                .build();
        return ChatClient
                .builder(deepseek)
                .defaultOptions(ChatOptions.builder().model(DEEPSEEK_MODEL).build())
                .defaultAdvisors(MessageChatMemoryAdvisor.builder(windowChatMemory).build())
                .build();
    }

    @Bean(name = "qwenChatClient")
    public ChatClient qwenChatClient(@Qualifier("qwen") ChatModel qwen) {
        return ChatClient
                .builder(qwen)
                .defaultOptions(ChatOptions.builder().model(QWEN_MODEL).build())
                .build();
    }
}