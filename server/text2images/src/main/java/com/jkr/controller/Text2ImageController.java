package com.jkr.controller;

import com.alibaba.cloud.ai.dashscope.image.DashScopeImageOptions;
import jakarta.annotation.Resource;
import org.springframework.ai.image.ImageModel;
import org.springframework.ai.image.ImagePrompt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/t2i")
public class Text2ImageController {

    public static final String IMAGE_MODEL = "wan2.2-t2i-plus";

    @Resource
    private ImageModel imageModel;

    /**
     * <a href="http://127.0.0.1:8001/t2i/image">测试</a>
     * @param prompt prompt
     * @return url
     */
    @GetMapping(value = "/image")
    public String image(@RequestParam(name = "prompt", defaultValue = "戴眼镜的女生") String prompt) {
        return imageModel.call(
                        new ImagePrompt(prompt, DashScopeImageOptions.builder().withModel(IMAGE_MODEL).build())
                )
                .getResult()
                .getOutput()
                .getUrl();
    }
}


