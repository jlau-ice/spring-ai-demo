package com.jkr.controller;

import com.jkr.Constant;
import com.jkr.model.Contract;
import com.jkr.service.OcrService;
import jakarta.annotation.Resource;
import lombok.val;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

import static com.jkr.Constant.MULTIMODAL_PROMPT_TEMPLATE;

@RestController
@RequestMapping("/ocr")
public class OcrController {
    @Resource(name = "qwenChatClient")
    private ChatClient qwenChatClient;

    public static OcrService ocrService;

    public OcrController(OcrService ocrService) {
        OcrController.ocrService = ocrService;
    }

    @GetMapping("/do-ocr")
    public Contract doOcr(@RequestParam(name = "pdfUrl", defaultValue = "http://192.168.172.108:9000/carbon/test2.pdf") String pdfUrl) throws IOException {
        String extractedInfo = ocrService.getOcrResult(pdfUrl);
        return qwenChatClient.prompt()
                .user(u -> u.text(Constant.MULTIMODAL_PROMPT_TEMPLATE)
                        .param("extractedInfo", extractedInfo))
                .call()
                .entity(Contract.class);
    }

    @GetMapping("/ocr-result")
    public String ocrResult(@RequestParam(name = "pdfUrl", defaultValue = "http://192.168.172.108:9000/carbon/test2.pdf") String pdfUrl) throws IOException {
        return ocrService.getOcrResult(pdfUrl);
    }

    @GetMapping("/entity")

    public Contract entity() {
        String extractedInfo = "合同编号（contractId）：吉林银行股份有限公司白山江源支行 2024 年小企业抵字第 100 号\n" +
                "合同名称（contractName）：抵押合同\n" +
                "合同签订日期（contractSignDate）：2024-12-16\n" +
                "合同起止日期（contractStartEndDate）：未识别\n" +
                "合同状态（contractStatus）：未识别\n" +
                "姓名（name）：未识别\n" +
                "联系电话（phone）：18610445777\n" +
                "所属机构与职位（organizationAndPosition）：未识别\n" +
                "主体名称（entityName）：吉林省吉源牧业有限公司\n" +
                "主体养殖经验（breedingExperience）：未识别\n" +
                "生产经营单位代码（productionBusinessUnitCode）：未识别\n" +
                "主体类型（entityType）：未识别\n" +
                "社会统一信用代码（unifiedSocialCreditCode）：91220605MA7D6R8Q1W\n" +
                "法人姓名（legalPersonName）：杨东青\n" +
                "法人身份证号（legalPersonIdNumber）：未识别\n" +
                "法人联系电话（legalPersonPhone）：未识别\n" +
                "联系人姓名（contactPersonName）：杨东青\n" +
                "联系人电话（contactPersonPhone）：18610445777\n" +
                "养殖地址（breedingAddress）：白山市江源区城墙街道新华村二社\n" +
                "养殖畜种（breedingSpecies）：西门塔尔牛\n" +
                "养殖品种（breedingVariety）：未识别\n" +
                "设计存栏（designStock）：未识别\n" +
                "实际存栏（actualStock）：70\n" +
                "设计年出栏（designAnnualOutput）：未识别\n" +
                "动物防疫条件合格证（animalQuarantineQualification）：未识别\n" +
                "种畜禽生产经营许可证（seedLivestockProductionLicense）：未识别\n" +
                "备注（remarks）：未识别\n" +
                "经纬度信息（longitudeLatitude）：未识别\n" +
                "登记时间（registrationTime）：未识别";
        return qwenChatClient.prompt()
                .user(u -> u.text(Constant.MULTIMODAL_PROMPT_TEMPLATE)
                        .param("extractedInfo", extractedInfo))
                .call()
                .entity(Contract.class);
    }

}


