package com.jkr.controller;


import com.jkr.utils.DateTimeTools;
import jakarta.annotation.Resource;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.ChatOptions;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.model.tool.ToolCallingChatOptions;
import org.springframework.ai.support.ToolCallbacks;
import org.springframework.ai.tool.ToolCallback;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/tool")
public class ToolCallingController {

    @Resource
    private ChatModel qwenChatModel;

    @Resource
    private ChatClient qwenChatClient;

    @GetMapping("/no")
    public Flux<String> noTool(@RequestParam(name = "question", defaultValue = "你是谁现在几点?") String question) {
        return qwenChatModel.stream(question);
    }

    @GetMapping("/model")
    public Flux<String> chat(@RequestParam(name = "question", defaultValue = "你是谁现在几点?") String question) {
        // 1.工具注册到工具集合里
        ToolCallback[] tools = ToolCallbacks.from(new DateTimeTools());
        // 2.将工具集配置进ChatOptions对象
        ChatOptions options = ToolCallingChatOptions.builder().toolCallbacks(tools).build();
        // 3.构建提示词
        Prompt prompt = new Prompt(question, options);
        // 4.调用大模型
        // return qwenChatModel.call(prompt).getResult().getOutput().getText();
        return qwenChatModel.stream(prompt).mapNotNull(
                chatResponse -> chatResponse.getResult().getOutput().getText()
        );
    }


    @GetMapping("/client")
    public Flux<String> chat2(@RequestParam(name = "question", defaultValue = "你是谁现在几点?") String question) {
        return qwenChatClient.prompt(question)
                .tools(new DateTimeTools())
                .stream()
                .content();
    }

}
