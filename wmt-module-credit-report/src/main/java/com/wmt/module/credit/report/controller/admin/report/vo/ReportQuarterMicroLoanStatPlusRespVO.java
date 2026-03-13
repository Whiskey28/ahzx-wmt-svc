package com.wmt.module.credit.report.controller.admin.report.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 季报（小微贷款）- 展示统计（扩展版）Response VO
 *
 * 在基础 stat 的基础上，补充：
 * 1) 数据库收录小微企业户数
 * 2) 为放贷机构提供小微企业征信服务次数
 *
 * 要求：字段全展开，不使用数组。
 */
@Schema(description = "管理后台 - 季报（小微贷款）展示统计（扩展版）Response VO")
@Data
public class ReportQuarterMicroLoanStatPlusRespVO extends ReportQuarterMicroLoanStatRespVO {

    // 0) 数据库收录小微企业户数
    private BigDecimal collectMicroSmeTotalCurrent;
    private BigDecimal collectMicroSmeTotalYtd;
    private BigDecimal collectMicroSmeTotalYoyDelta;
    private BigDecimal collectMicroSmeTotalYoyRate;

    // 0) 为放贷机构提供小微企业征信服务次数
    private BigDecimal serviceCountTotalCurrent;
    private BigDecimal serviceCountTotalYtd;
    private BigDecimal serviceCountTotalYoyDelta;
    private BigDecimal serviceCountTotalYoyRate;
}

