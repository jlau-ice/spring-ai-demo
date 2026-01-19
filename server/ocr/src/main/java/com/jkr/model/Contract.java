package com.jkr.model;

import lombok.Data;

import java.util.Date;

@Data
public class Contract {
    private String contractId; // 合同编号
    private String contractName; // 合同名称
    private Date contractSignDate; // 合同签订日期
    private String contractStartEndDate; // 合同起止日期
    private String contractStatus; // 合同状态
    // 人员信息
    private String name; // 姓名
    private String phone; // 联系电话
    private String organizationAndPosition; // 所属机构与职位
    private String entityName; // 主体名称
    private String breedingExperience; // 主体养殖经验
    private String productionBusinessUnitCode; // 生产经营单位代码
    private String entityType; // 主体类型
    private String unifiedSocialCreditCode; // 社会统一信用代码
    private String legalPersonName; // 法人姓名
    private String legalPersonIdNumber; // 法人身份证号
    private String legalPersonPhone; // 法人联系电话
    private String contactPersonName; // 联系人姓名
    private String contactPersonPhone; // 联系人电话

    // 养殖信息
    private String breedingAddress; // 养殖地址
    private String breedingSpecies; // 养殖畜种
    private String breedingVariety; // 养殖品种
    private Integer designStock; // 设计存栏
    private Integer actualStock; // 实际存栏
    private Integer designAnnualOutput; // 设计年出栏
    private String animalQuarantineQualification; // 动物防疫条件合格证
    private String seedLivestockProductionLicense; // 种畜禽生产经营许可证
    private String remarks; // 备注
    private String longitudeLatitude; // 经纬度信息
    private Date registrationTime; // 登记时间
    // 牛只个体信息
    private Boolean cowFaceFeatureCollection; // 牛脸特征采集
    private String earTagNumbers; // 耳标编号(多个)
    private Date birthDate; // 出生日期
    private Integer monthAge; // 月龄
    private String animalSpecies; // 畜种
    private String breed; // 品种
    private String gender; // 性别
    private Double weight; // 重量
    private Boolean isMortgaged; // 是否抵押
    private String expectedSaleMonth; // 预计出栏时间（月份）
    // 事件信息
    private String eventCategory; // 事件分类
    private String eventContent; // 事件内容
    private String eventEntity; // 事件主体
    private Date eventTime; // 发生时间
    private String eventType; // 事件类型
    // 预警信息
    private String warningEntity; // 预警主体
    private String warningTitle; // 预警标题
    private String warningType; // 预警类型
    private String warningLevel; // 预警级别
    private String warningContent; // 预警内容
    private Date warningTime; // 预警时间
    // 统计信息
    private Integer entityTotalCount; // 主体总数
    private Integer approvedMortgageAmount; // 核定抵押量
    private Integer inventoryCount; // 存栏数量
    private Date reportGenerationTime; // 报告生成时间
    private String reportContent; // 报告内容
    // 个体详细信息
    private String individualNumber; // 个体编号
    private String earTagNumber; // 耳标编号
    private String entityNameDetail; // 主体名称
    private String responsiblePerson; // 负责人
    private String responsiblePersonPhone; // 负责人电话
    private Boolean isUnderMortgage; // 是否在押
    private String cowFaceImage; // 牛脸图片（可存储图片路径或Base64编码）
    // 检疫证明信息
    private String certificateNumber; // 证明号
    private Date certificateDate; // 出证日期
    private Integer quarantineValidityDays; // 检疫有效期（天）
    private String quarantineCertificateType; // 动物检疫合格证明详细类型名称
    private String cargoOwnerName; // 货主名称
    private String cargoOwnerPhone; // 货主电话
    private String productName; // 产品名称
    private Integer animalQuantity; // 动物数量
    private String productUnit; // 产品单位
    private String productionUnitName; // 生产单位名称
    private String productionUnitAddress; // 生产单位地址
    private String departureAddress; // 启运省市县乡镇村
    private String destinationAddress; // 目的省市县乡镇村
    private String destinationDetailAddress; // 目的地具体地址（不包括乡镇）
    private String vehicleLicensePlate; // 运载工具牌号
    private String carrierName; // 承运人
    private String carrierContact; // 承运人联系方式
    private String transportMode; // 运输方式
    private String disinfectionMethod; // 消毒方式
    private String officialVeterinarian; // 官方兽医
    private Boolean isValid; // 是否有效
    private String quarantineMarkNumber; // 检疫标志号
    private String certificateRemarks; // 备注
    private Date creationTime; // 创建时间
    private String purpose; // 用途
    // 耳标信息
    private String earTagId; // 耳标号
    private Integer earTagCount; // 耳标号数量
    // 免疫信息
    private String farmerName; // 养殖户名
    private Date immunizationTime; // 免疫时间
    private String immunizationPersonnel; // 免疫人员
    private String immunizationType; // 免疫类型
    private String penName; // 圈舍名
    private Integer immunizationDayStock; // 免疫当日存栏
    private Integer shouldImmunizeQuantity; // 应免疫数量
    private Integer actualImmunizeQuantity; // 实际免疫数量
    private String notImmunizedReason; // 未免疫原因
    private String animalSubspecies; // 畜种小类
    private String earTagNumberVaccine; // 耳标号
    private String vaccineName; // 疫苗名称
    private String vaccineManufacturer; // 疫苗厂家
    private String vaccineModel; // 疫苗型号
    private String immunizationMethod; // 免疫方法
    private Double immunizationDose; // 免疫剂量
    private String immunizationDoseUnit; // 免疫剂量单位
}


