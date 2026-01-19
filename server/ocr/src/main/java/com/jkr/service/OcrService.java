package com.jkr.service;// dashscope SDK的版本 >= 2.21.10

import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URI;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.*;

import com.jkr.Constant;
import lombok.val;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.dashscope.common.Role;
import com.alibaba.dashscope.exception.ApiException;
import com.alibaba.dashscope.exception.NoApiKeyException;
import io.reactivex.Flowable;

import com.alibaba.dashscope.aigc.multimodalconversation.MultiModalConversation;
import com.alibaba.dashscope.aigc.multimodalconversation.MultiModalConversationParam;
import com.alibaba.dashscope.aigc.multimodalconversation.MultiModalConversationResult;
import com.alibaba.dashscope.common.MultiModalMessage;
import com.alibaba.dashscope.exception.UploadFileException;
import com.alibaba.dashscope.exception.InputRequiredException;

import java.lang.System;
import java.util.stream.Collectors;

import com.alibaba.dashscope.utils.Constants;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;

@Service
public class OcrService {

    private static final Logger logger = LoggerFactory.getLogger(OcrService.class);


    private void handleGenerationResult(MultiModalConversationResult message, StringBuilder reasoningContent, StringBuilder finalContent, boolean[] isFirstPrint) {
        String re = message.getOutput().getChoices().getFirst().getMessage().getReasoningContent();
        String reasoning = Objects.isNull(re) ? "" : re;
        List<Map<String, Object>> content = message.getOutput().getChoices().getFirst().getMessage().getContent();
        if (!reasoning.isEmpty()) {
            reasoningContent.append(reasoning);
            if (isFirstPrint[0]) {
                System.out.println("====================思考过程====================");
                isFirstPrint[0] = false;
            }
            System.out.print(reasoning);
        }
        if (Objects.nonNull(content) && !content.isEmpty()) {
            Object text = content.getFirst().get("text");
            finalContent.append(content.getFirst().get("text"));
            if (!isFirstPrint[0]) {
                System.out.println("\n====================完整回复====================");
                isFirstPrint[0] = true;
            }
            System.out.print(text);
        }
    }

    private void streamCallWithMessage(MultiModalConversation conv, MultiModalMessage Msg, StringBuilder reasoningContent, StringBuilder finalContent, boolean[] isFirstPrint)
            throws NoApiKeyException, ApiException, InputRequiredException, UploadFileException {
        MultiModalConversationParam param = buildMultiModalConversationParam(Msg);
        Flowable<MultiModalConversationResult> result = conv.streamCall(param);
        result.blockingForEach(resultMessage -> handleGenerationResult(resultMessage, reasoningContent, finalContent, isFirstPrint));
    }

    private MultiModalConversationParam buildMultiModalConversationParam(MultiModalMessage Msg) {
        return MultiModalConversationParam.builder()
                .apiKey(System.getenv("DASH_SCOPE_API_KEY"))
                .model("qwen3-vl-plus")
                .messages(Collections.singletonList(Msg))
                // 是否开启思考
                .enableThinking(false)
                .thinkingBudget(500)
                .incrementalOutput(true)
                .build();
    }

    public String askWithImages(List<String> images, String prompt) {
        StringBuilder reasoningContent = new StringBuilder();
        StringBuilder finalContent = new StringBuilder();
        boolean[] isFirstPrint = {true};
        try {
            List<Map<String, Object>> contentList = images.stream()
                    .map(image -> {
                        Map<String, Object> map = new HashMap<>();
                        map.put("image", image);
                        return map;
                    })
                    .collect(Collectors.toList());
            Map<String, Object> textMap = new HashMap<>();
            textMap.put("text", prompt);
            contentList.add(textMap);
            MultiModalConversation conv = new MultiModalConversation();
            MultiModalMessage userMsg = MultiModalMessage.builder()
                    .role(Role.USER.getValue())
                    .content(contentList)
                    .build();
            streamCallWithMessage(conv, userMsg, reasoningContent, finalContent, isFirstPrint);
        } catch (ApiException | NoApiKeyException | UploadFileException | InputRequiredException e) {
            logger.error("An exception occurred: {}", e.getMessage());
        }
        return finalContent.toString();
    }

    public String getOcrResult(String filePath) {
        try {
            InputStream is = URI.create(filePath).toURL().openStream();
            List<String> base64Strings = pdf2images(is);
            return askWithImages(base64Strings, Constant.QWENVL_PROMPT_TEMPLATE);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    public static List<String> pdf2images(InputStream inputStream) throws IOException {
        List<String> base64Images = new ArrayList<>();
        try (PDDocument document = PDDocument.load(inputStream)) {
            PDFRenderer renderer = new PDFRenderer(document);
            for (int i = 0; i < document.getNumberOfPages(); i++) {
                BufferedImage image = renderer.renderImageWithDPI(i, 150);
                try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
                    ImageIO.write(image, "PNG", baos);
                    String base64 = Base64.getEncoder().encodeToString(baos.toByteArray());
                    base64Images.add("data:image/png;base64," + base64);
                }
            }
        } finally {
            if (inputStream != null) {
                inputStream.close();
            }
        }
        return base64Images;
    }
}