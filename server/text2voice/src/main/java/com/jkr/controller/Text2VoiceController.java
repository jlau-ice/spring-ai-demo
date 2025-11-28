package com.jkr.controller;

import com.alibaba.cloud.ai.dashscope.audio.DashScopeSpeechSynthesisOptions;
import com.alibaba.cloud.ai.dashscope.audio.synthesis.SpeechSynthesisModel;
import com.alibaba.cloud.ai.dashscope.audio.synthesis.SpeechSynthesisPrompt;
import com.alibaba.cloud.ai.dashscope.audio.synthesis.SpeechSynthesisResponse;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.FileOutputStream;
import java.nio.ByteBuffer;
import java.util.UUID;

@RestController
@RequestMapping("/t2v")
public class Text2VoiceController {

    @Resource
    private SpeechSynthesisModel speechSynthesisModel;

    public static final String VOICE_MODEL = "cosyvoice-v3-flash";

    public static final String VOICE_TIMBER = "longanyang";

    @GetMapping("/voice")
    public String voice(@RequestParam(name = "text", defaultValue = "你好很高兴认识你。") String text) {
        String filePath = UUID.randomUUID() + ".mp3";
        //1 语音参数设置
        DashScopeSpeechSynthesisOptions options = DashScopeSpeechSynthesisOptions.builder()
                .model(VOICE_MODEL)
                .voice(VOICE_TIMBER)
                .build();
        //2 调用大模型语音生成对象
        SpeechSynthesisResponse response = speechSynthesisModel.call(new SpeechSynthesisPrompt(text, options));
        //3 字节流语音转换
        ByteBuffer byteBuffer = response.getResult().getOutput().getAudio();
        //4 文件生成
        try (FileOutputStream fileOutputStream = new FileOutputStream(filePath)) {
            fileOutputStream.write(byteBuffer.array());
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        //5 生成路径OK
        return filePath;
    }

}


