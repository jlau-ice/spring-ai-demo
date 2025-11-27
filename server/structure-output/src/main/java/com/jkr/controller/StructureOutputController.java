package com.jkr.controller;

import com.jkr.records.StudentRecord;
import jakarta.annotation.Resource;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.function.Consumer;

@RestController
@RequestMapping("/struct")
public class StructureOutputController {

    @Resource(name = "qwenChatClient")
    private ChatClient qwenChatClient;

    /**
     * <a href="http://localhost:8007/struct/chat?name=%E6%9D%8E%E5%9B%9B&email=ice@jlau.com">...</a>
     *
     * @param name  姓名
     * @param email 邮箱
     * @return StudentRecord
     */
    @GetMapping("/chat")
    public StudentRecord chat(@RequestParam(name = "name", defaultValue = "王一一") String name,
                              @RequestParam(name = "email", defaultValue = "ice@gmail.com") String email) {
        return qwenChatClient.prompt().user(new Consumer<ChatClient.PromptUserSpec>() {
            @Override
            public void accept(ChatClient.PromptUserSpec promptUserSpec) {
                promptUserSpec.text("学号1001，我是{name},大学专业计算机科学与技术,邮箱{email}")
                        .param("name", name)
                        .param("email", email);
                //promptUserSpec.text("学号001，我是武汉市长江大桥,大学专业计算机科学与技术,邮箱xxx.ice@gmail.com");
                //promptUserSpec.text("学号001，我是李四不对我是张三的哥哥王五的弟弟武汉市长江大桥的哥哥王麻子的叔叔你爸爸&email=ice@jlau.com,大学专业计算机科学与技术,邮箱xxx.ice@gmail.com");
                //promptUserSpec.text("学号001，我是李四不对我是张三,大学专业计算机科学与技术,邮箱xxx.ice@gmail.com");
                //promptUserSpec.text("学号001，我是李四李四的爸爸李鬼,大学专业计算机科学与技术,邮箱xxx.ice@gmail.com");
            }
        }).call().entity(StudentRecord.class);
    }


    /**
     * <a href="http://localhost:8007/struct/chat2?name=%E7%8E%8B%E4%B8%80%E7%8B%97&email=ice@jlau.com">测试</a>
     *
     * @param name  姓名
     * @param email 邮箱
     * @return StudentRecord
     */
    @GetMapping("/chat2")
    public StudentRecord chat2(@RequestParam(name = "name", defaultValue = "王一狗") String name,
                               @RequestParam(name = "email", defaultValue = "ice@jlau.com") String email) {

        String stringTemplate = """
                学号1002，我叫{name},大学专业软件工程,邮箱{email}
                """;

        return qwenChatClient.prompt()
                .user(promptUserSpec -> promptUserSpec.text(stringTemplate)
                        .param("name", name)
                        .param("email", email))
                .call()
                .entity(StudentRecord.class);
    }

}


