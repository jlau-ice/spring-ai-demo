package com.jkr.controller;

import jakarta.annotation.Resource;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.ToolResponseMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/prompt")
public class PromptController {
    @Resource(name = "deepseek")
    private ChatModel deepseekChatModel;
    @Resource(name = "qwen")
    private ChatModel qwenChatModel;

    @Resource(name = "deepseekChatClient")
    private ChatClient deepseekChatClient;
    @Resource(name = "qwenChatClient")
    private ChatClient qwenChatClient;


    /**
     * @param question question
     * @return Flux<String>
     */
    @GetMapping("/law")
    public Flux<String> law(@RequestParam(name = "question", defaultValue = "你是谁") String question) {
        return deepseekChatClient.prompt()
                // 能力边界
                .system("你是一个法律助手，只回答法律问题，其它问题回复，我只能回答法律相关问题，其它无可奉告")
                .user(question)
                .stream()
                .content();
    }

    /**
     * @param question question
     * @return Flux<ChatResponse>
     */
    @GetMapping("/story")
    public Flux<ChatResponse> story(@RequestParam(name = "question", defaultValue = "你是谁") String question) {
        // 系统消息
        SystemMessage systemMessage = new SystemMessage("你是一个讲故事的助手,每个故事控制在300字以内");
        // 用户消息
        UserMessage userMessage = new UserMessage(question);
        Prompt prompt = new Prompt(userMessage, systemMessage);
        return deepseekChatModel.stream(prompt);
    }

    /**
     * @param question question
     * @return Flux<String>
     */
    @GetMapping("/story/html")
    public Flux<String> storyHtml(@RequestParam(name = "question", defaultValue = "讲个故事") String question) {
        // 系统消息
        SystemMessage systemMessage = new SystemMessage("你是一个讲故事的助手," +
                "每个故事控制在600字以内且以HTML格式返回");
        // 用户消息
        UserMessage userMessage = new UserMessage(question);
        Prompt prompt = new Prompt(userMessage, systemMessage);
        return deepseekChatModel.stream(prompt)
                .mapNotNull(response ->
                        response.getResults().getFirst().getOutput().getText()
                );
    }

    /**
     * @param question question
     * @return String
     */
    @GetMapping("/call")
    public String storyCall(@RequestParam(name = "question", defaultValue = "你是谁") String question) {
        AssistantMessage assistantMessage = Objects.requireNonNull(deepseekChatClient.prompt()
                        .system("你是一个讲故事的助手，专门将鬼故事，其他的一律不讲。")
                        .user(question)
                        .call()
                        .chatResponse())
                .getResult()
                .getOutput();
        return assistantMessage.getText();
    }


    /**
     * @param city city
     * @return String
     */
    @GetMapping("/city")
    public String city(@RequestParam(name = "question", defaultValue = "你是谁") String city) {
        String answer = Objects.requireNonNull(deepseekChatClient.prompt()
                        .user(city + "未来3天天气情况如何?")
                        .call()
                        .chatResponse())
                .getResult()
                .getOutput()
                .getText();
        ToolResponseMessage toolResponseMessage =
                new ToolResponseMessage(
                        List.of(new ToolResponseMessage.ToolResponse("1", "获得天气", city)
                        )
                );
        String toolResponse = toolResponseMessage.getText();
        return answer + toolResponse;
    }
}
