package com.jkr;

public interface Constant {

    /**
     * 用于千问VL模型的提示词模板 - 从合同图片中提取关键信息
     * 格式：字段 中文名称（英文名称）：内容
     */
    String QWENVL_PROMPT_TEMPLATE = """
            你是一个专业的数据提取助手。请仔细分析上传的图片（可能包含合同、养殖证明、检疫证或统计报表），从中提取以下 106 个关键字段。
            
            ### 提取规则（必须严格遵守）：
            1. 输出格式：每一行必须严格按照 `序号. 中文名称（英文名称）：提取到的内容` 的格式输出。
            2. 空值处理：如果图片中完全没有提及该信息，或者信息模糊无法识别，请输出："未识别"。
            3. 日期格式：所有日期必须统一转化为 "yyyy-MM-dd" 格式（例如 2024-05-20）。
            4. 数值格式：金额、数量、重量、天数等数字字段，只保留数字和必要的小数点，不要单位。
            5. 布尔值：对于“是否”类字段，提取结果统一为 "是" 或 "否"。
            6. 逻辑一致：不要添加任何开场白、总结或额外的解释，直接从第 1 个字段开始输出。
            
            ### 字段列表：
            --- 合同信息 ---
            1. 合同编号（contractId）：
            2. 合同名称（contractName）：
            3. 合同签订日期（contractSignDate）：
            4. 合同起止日期（contractStartEndDate）：
            5. 合同状态（contractStatus）：
            
            --- 人员信息 ---
            6. 姓名（name）：
            7. 联系电话（phone）：
            8. 所属机构与职位（organizationAndPosition）：
            9. 主体名称（entityName）：
            10. 主体养殖经验（breedingExperience）：
            11. 生产经营单位代码（productionBusinessUnitCode）：
            12. 主体类型（entityType）：
            13. 社会统一信用代码（unifiedSocialCreditCode）：
            14. 法人姓名（legalPersonName）：
            15. 法人身份证号（legalPersonIdNumber）：
            16. 法人联系电话（legalPersonPhone）：
            17. 联系人姓名（contactPersonName）：
            18. 联系人电话（contactPersonPhone）：
            
            --- 养殖信息 ---
            19. 养殖地址（breedingAddress）：
            20. 养殖畜种（breedingSpecies）：
            21. 养殖品种（breedingVariety）：
            22. 设计存栏（designStock）：
            23. 实际存栏（actualStock）：
            24. 设计年出栏（designAnnualOutput）：
            25. 动物防疫条件合格证（animalQuarantineQualification）：
            26. 种畜禽生产经营许可证（seedLivestockProductionLicense）：
            27. 备注（remarks）：
            28. 经纬度信息（longitudeLatitude）：
            29. 登记时间（registrationTime）：
            
            --- 牛只个体信息 ---
            30. 牛脸特征采集（cowFaceFeatureCollection）：
            31. 耳标编号(多个)（earTagNumbers）：
            32. 出生日期（birthDate）：
            33. 月龄（monthAge）：
            34. 畜种（animalSpecies）：
            35. 品种（breed）：
            36. 性别（gender）：
            37. 重量（weight）：
            38. 是否抵押（isMortgaged）：
            39. 预计出栏时间（expectedSaleMonth）：
            
            --- 事件信息 ---
            40. 事件分类（eventCategory）：
            41. 事件内容（eventContent）：
            42. 事件主体（eventEntity）：
            43. 发生时间（eventTime）：
            44. 事件类型（eventType）：
            
            --- 预警信息 ---
            45. 预警主体（warningEntity）：
            46. 预警标题（warningTitle）：
            47. 预警类型（warningType）：
            48. 预警级别（warningLevel）：
            49. 预警内容（warningContent）：
            50. 预警时间（warningTime）：
            
            --- 统计信息 ---
            51. 主体总数（entityTotalCount）：
            52. 核定抵押量（approvedMortgageAmount）：
            53. 存栏数量（inventoryCount）：
            54. 报告生成时间（reportGenerationTime）：
            55. 报告内容（reportContent）：
            
            --- 个体详细信息 ---
            56. 个体编号（individualNumber）：
            57. 耳标编号（earTagNumber）：
            58. 主体名称（entityNameDetail）：
            59. 负责人（responsiblePerson）：
            60. 负责人电话（responsiblePersonPhone）：
            61. 是否在押（isUnderMortgage）：
            62. 牛脸图片（cowFaceImage）：
            
            --- 检疫证明信息 ---
            63. 证明号（certificateNumber）：
            64. 出证日期（certificateDate）：
            65. 检疫有效期（quarantineValidityDays）：
            66. 动物检疫合格证明详细类型名称（quarantineCertificateType）：
            67. 货主名称（cargoOwnerName）：
            68. 货主电话（cargoOwnerPhone）：
            69. 产品名称（productName）：
            70. 动物数量（animalQuantity）：
            71. 产品单位（productUnit）：
            72. 生产单位名称（productionUnitName）：
            73. 生产单位地址（productionUnitAddress）：
            74. 启运地（departureAddress）：
            75. 目的地（destinationAddress）：
            76. 目的地具体地址（destinationDetailAddress）：
            77. 运载工具牌号（vehicleLicensePlate）：
            78. 承运人（carrierName）：
            79. 承运人联系方式（carrierContact）：
            80. 运输方式（transportMode）：
            81. 消毒方式（disinfectionMethod）：
            82. 官方兽医（officialVeterinarian）：
            83. 是否有效（isValid）：
            84. 检疫标志号（quarantineMarkNumber）：
            85. 备注（certificateRemarks）：
            86. 创建时间（creationTime）：
            87. 用途（purpose）：
            
            --- 耳标信息 ---
            88. 耳标号（earTagId）：
            89. 耳标号数量（earTagCount）：
            
            --- 免疫信息 ---
            90. 养殖户名（farmerName）：
            91. 免疫时间（immunizationTime）：
            92. 免疫人员（immunizationPersonnel）：
            93. 免疫类型（immunizationType）：
            94. 圈舍名（penName）：
            95. 免疫当日存栏（immunizationDayStock）：
            96. 应免疫数量（shouldImmunizeQuantity）：
            97. 实际免疫数量（actualImmunizeQuantity）：
            98. 未免疫原因（notImmunizedReason）：
            99. 畜种小类（animalSubspecies）：
            100. 耳标号（earTagNumberVaccine）：
            101. 疫苗名称（vaccineName）：
            102. 疫苗厂家（vaccineManufacturer）：
            103. 疫苗型号（vaccineModel）：
            104. 免疫方法（immunizationMethod）：
            105. 免疫剂量（immunizationDose）：
            106. 免疫剂量单位（immunizationDoseUnit）：/no_think""";
    /**
     * 用于多模态模型的结构化输出提示词模板
     * 根据千问VL的识别结果，组合成Contract对象
     */
    String MULTIMODAL_PROMPT_TEMPLATE = """
            请根据以下千问VL模型识别的合同信息，生成一个完整的Contract对象JSON。
            以下是从合同图片中提取的信息：
            {extractedInfo}
            处理规则：
            1. 如果提取的信息中包含"未识别"，该字段值设为null
            2. 日期字段格式化为yyyy-MM-dd格式
            3. 数值字段转换为对应的整数类型
            4. 如果原始数据缺失，对应的字段值为null
            5. 确保所有字段都包含在JSON中，即使值为null
            6. 只输出JSON，不要有任何额外文本
            """;

    /**
     * 用于多模态模型的完整合同信息提取提示词模板
     * 包含所有Contract类字段
     */
    String COMPLETE_CONTRACT_PROMPT_TEMPLATE = """
            请根据千问VL模型从合同图片中提取的信息，生成完整的Contract对象JSON。
            
            千问VL提取结果：
            ${qwenvlResult}
            
            请将所有信息整理到以下JSON结构中，注意数据类型转换：
            
            {
              // 合同基本信息
              "contractId": "字符串或null",
              "contractName": "字符串或null",
              "contractSignDate": "日期字符串(yyyy-MM-dd)或null",
              "contractStartEndDate": "字符串或null",
              "contractStatus": "字符串或null",
            
              // 人员信息
              "name": "字符串或null",
              "phone": "字符串或null",
              "organizationAndPosition": "字符串或null",
              "entityName": "字符串或null",
              "breedingExperience": "字符串或null",
              "productionBusinessUnitCode": "字符串或null",
              "entityType": "字符串或null",
              "unifiedSocialCreditCode": "字符串或null",
              "legalPersonName": "字符串或null",
              "legalPersonIdNumber": "字符串或null",
              "legalPersonPhone": "字符串或null",
              "contactPersonName": "字符串或null",
              "contactPersonPhone": "字符串或null",
            
              // 养殖信息
              "breedingAddress": "字符串或null",
              "breedingSpecies": "字符串或null",
              "breedingVariety": "字符串或null",
              "designStock": "整数或null",
              "actualStock": "整数或null",
              "designAnnualOutput": "整数或null",
              "animalQuarantineQualification": "字符串或null",
              "seedLivestockProductionLicense": "字符串或null",
              "remarks": "字符串或null",
              "longitudeLatitude": "字符串或null",
              "registrationTime": "日期字符串(yyyy-MM-dd)或null",
            
              // 其他字段（如果提取结果中包含）
              "birthDate": "日期字符串(yyyy-MM-dd)或null",
              "monthAge": "整数或null",
              "gender": "字符串或null",
              "weight": "浮点数或null",
              "isMortgaged": "布尔值或null",
            
              // 扩展信息占位符
              "cattleInfo": "如果有多头牛只信息，存储为数组",
              "immunizationRecords": "如果有免疫记录，存储为数组",
              "quarantineCertificates": "如果有检疫证明，存储为数组"
            }
            
            要求：
            1. 从提取结果中正确解析每个字段
            2. 数据类型转换要正确（字符串、整数、浮点数、布尔值、日期）
            3. 日期统一格式化为yyyy-MM-dd
            4. 如果某个字段在提取结果中没有或标记为未识别，设置为null
            5. 只输出JSON格式的结果，不要有任何额外说明
            """;
}