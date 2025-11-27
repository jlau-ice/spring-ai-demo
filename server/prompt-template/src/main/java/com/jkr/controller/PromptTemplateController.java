package com.jkr.controller;

import jakarta.annotation.Resource;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.chat.prompt.SystemPromptTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/template")
public class PromptTemplateController {

    @Resource(name = "deepseek")
    private ChatModel deepseekChatModel;
    @Resource(name = "qwen")
    private ChatModel qwenChatModel;

    @Resource(name = "deepseekChatClient")
    private ChatClient deepseekChatClient;
    @Resource(name = "qwenChatClient")
    private ChatClient qwenChatClient;

    @Value("classpath:/prompt-template/template.txt")
    private org.springframework.core.io.Resource userTemplate;

    /**
     * <a href="http://127.0.0.1:8006/template/chat?topic=%22%E9%AC%BC%22&outputFormat=%22html%22&wordCount=500">测试</a>
     *
     * @param topic        主题
     * @param outputFormat 输出格式
     * @param wordCount    字数
     * @return Flux<String>
     */
    @GetMapping("/chat")
    public Flux<String> chat(@RequestParam(name = "topic", defaultValue = "鬼") String topic,
                             @RequestParam(name = "outputFormat", defaultValue = "html") String outputFormat,
                             @RequestParam(name = "wordCount", defaultValue = "300") String wordCount) {
        PromptTemplate promptTemplate = new PromptTemplate(
                "讲一个关于{topic}的故事" +
                        "并以{output_format}格式输出，" +
                        "字数在{word_count}左右");
        Prompt prompt = promptTemplate.create(Map.of(
                "topic", topic,
                "output_format", outputFormat,
                "word_count", wordCount));
        // 阻塞式返回
        //return deepseekChatModel.call(prompt).getResult().getOutput().getText();
        // 流式返回
        return deepseekChatClient.prompt(prompt).stream().content();
    }

    /**
     * <a href="http://127.0.0.1:8006/template/chat2?topic=%22%E9%AC%BC%22&outputFormat=%22html%22&wordCount=500">测试</a>
     *
     * @param topic        主题
     * @param outputFormat 输出格式
     * @return Flux<String>
     */
    @GetMapping("/chat2")
    public Flux<String> chat2(@RequestParam(name = "topic", defaultValue = "鬼") String topic,
                              @RequestParam(name = "outputFormat", defaultValue = "html") String outputFormat,
                              @RequestParam(name = "wordCount", defaultValue = "300") String wordCount) {
        PromptTemplate promptTemplate = new PromptTemplate(userTemplate);
        Prompt prompt = promptTemplate.create(Map.of("topic", topic, "output_format", outputFormat, "word_count", wordCount));
        // 阻塞式返回
        //return deepseekChatClient.prompt(prompt).call().content();
        // 流式返回
        return deepseekChatModel.stream(prompt).mapNotNull(
                chatResponse -> chatResponse.getResult().getOutput().getText()
        );
    }


    /**
     *
     * <a href="http://127.0.0.1:8006/template/chat3?userTopic=%22%E8%A5%BF%E7%BA%A2%E6%9F%BF%E7%82%92%E9%B8%A1%E8%9B%8B%E6%80%8E%E4%B9%88%E5%81%9A%E5%A5%BD%E5%90%83%22">测试</a>
     *
     * @param sysTopic  系统主题
     * @param userTopic 用户主题
     * @return Flux<String>
     */
    @GetMapping("/chat3")
    public Flux<String> chat3(@RequestParam(name = "sysTopic", defaultValue = "法律") String sysTopic,
                              @RequestParam(name = "userTopic", defaultValue = "劳动法第57条") String userTopic) {
        // 1.SystemPromptTemplate
        SystemPromptTemplate systemPromptTemplate = new SystemPromptTemplate("你是{system_topic}助手，只回答{system_topic}其它无可奉告，以HTML格式的结果。");
        Message sysMessage = systemPromptTemplate.createMessage(Map.of("system_topic", sysTopic));
        // 2.PromptTemplate
        PromptTemplate userPromptTemplate = new PromptTemplate("解释一下{user_topic}");
        Message userMessage = userPromptTemplate.createMessage(Map.of("user_topic", userTopic));
        // 3.组合【关键】 多个 Message -> Prompt
        Prompt prompt = new Prompt(List.of(sysMessage, userMessage));
        //Prompt prompt = new Prompt(sysMessage, userMessage);
        // 4.调用 LLM
        // 阻塞式返回
        //return deepseekChatClient.prompt(prompt).call().content();
        // 流式返回
        return deepseekChatModel.stream(prompt).mapNotNull(
                chatResponse -> chatResponse.getResult().getOutput().getText()
        );
    }


    /**
     * <a href="http://127.0.0.1:8006/template/chat4?question=%22%E8%AF%B4%E8%AF%B4HashMap%E5%BA%95%E5%B1%82%E5%8E%9F%E7%90%86%EF%BC%8C%E4%BB%A5html%E6%A0%BC%E5%BC%8F%E8%BF%94%E5%9B%9E%E7%BB%99%E6%88%91%22">测试</a>
     *
     * @param question question
     * @return Flux<String>
     */
    @GetMapping("/chat4")
    public Flux<String> chat4(@RequestParam(name = "question", defaultValue = "讲个故事") String question) {
        //1 系统消息
        SystemMessage systemMessage = new SystemMessage("你是一个Java编程助手，拒绝回答非技术问题。");
        //2 用户消息
        UserMessage userMessage = new UserMessage(question);
        //3 系统消息+用户消息=完整提示词
        //Prompt prompt = new Prompt(systemMessage, userMessage);
        Prompt prompt = new Prompt(List.of(systemMessage, userMessage));
        //4 调用LLM
        // 阻塞式返回
        //return deepseekChatModel.call(prompt).getResult().getOutput().getText();
        // 流式返回
        return deepseekChatModel.stream(prompt).mapNotNull(
                chatResponse -> chatResponse.getResult().getOutput().getText()
        );
    }

    /**
     * <a href="http://127.0.0.1:8006/template/chat5?question=%22%E8%AF%B4%E8%AF%B4HashMap%E5%BA%95%E5%B1%82%E5%8E%9F%E7%90%86%EF%BC%8C%E4%BB%A5html%E6%A0%BC%E5%BC%8F%E8%BF%94%E5%9B%9E%E7%BB%99%E6%88%91%22">测试</a>
     *
     * @param question question
     * @return Flux<String>
     */
    @GetMapping("/chat5")
    public Flux<String> chat5(@RequestParam(name = "question", defaultValue = "讲个故事") String question) {
        // ChatClient 调用
        return deepseekChatClient.prompt()
                .system("你是一个Java编程助手，拒绝回答非技术问题。")
                .user(question)
                .stream()
                .content();
        // ChatModel 调用
        //SystemMessage systemMessage = new SystemMessage("你是一个Java编程助手，拒绝回答非技术问题。");
        //UserMessage userMessage = new UserMessage(question);
        //Prompt prompt = new Prompt(systemMessage, userMessage);
        //return deepseekChatModel.stream(prompt).mapNotNull(
        //        chatResponse -> chatResponse.getResult().getOutput().getText()
        //);
    }
}