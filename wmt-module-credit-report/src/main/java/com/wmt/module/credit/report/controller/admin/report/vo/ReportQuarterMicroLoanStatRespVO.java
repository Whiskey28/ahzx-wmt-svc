package com.wmt.module.credit.report.controller.admin.report.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 季报（小微贷款）- 展示统计 Response VO
 *
 * 要求：字段全展开，不使用数组，便于前端/报表直接绑定。
 */
@Schema(description = "管理后台 - 季报（小微贷款）展示统计 Response VO")
@Data
public class ReportQuarterMicroLoanStatRespVO {

    @Schema(description = "填报周期（格式：yyyyQn）", example = "2026Q1")
    private String periodId;

    @Schema(description = "角色id", example = "208")
    private String roleId;

    @Schema(description = "报表模板id（jimu_report.id）")
    private String reportId;

    // 1) 申请贷款户数
    private BigDecimal applyHouseholdsCurrent;
    private BigDecimal applyHouseholdsYtd;
    private BigDecimal applyHouseholdsYoyDelta;
    private BigDecimal applyHouseholdsYoyRate;

    // 2) 获得贷款户数
    private BigDecimal grantHouseholdsCurrent;
    private BigDecimal grantHouseholdsYtd;
    private BigDecimal grantHouseholdsYoyDelta;
    private BigDecimal grantHouseholdsYoyRate;

    // 3) 获得贷款金额
    private BigDecimal grantAmountCurrent;
    private BigDecimal grantAmountYtd;
    private BigDecimal grantAmountYoyDelta;
    private BigDecimal grantAmountYoyRate;

    // 4) 其中：信用贷款金额
    private BigDecimal creditGrantAmountCurrent;
    private BigDecimal creditGrantAmountYtd;
    private BigDecimal creditGrantAmountYoyDelta;
    private BigDecimal creditGrantAmountYoyRate;

    // 5) 平均年利率（百分数）
    private BigDecimal avgAnnualRatePctCurrent;
    private BigDecimal avgAnnualRatePctYtd;
    // 同期增长量：currentPct - lastPct（不乘100，按小数形式输出）
    private BigDecimal avgAnnualRatePctYoyDelta;
    private BigDecimal avgAnnualRatePctYoyRate;

    // 6) 不良率（百分数）
    private BigDecimal nplRatePctCurrent;
    private BigDecimal nplRatePctYtd;
    // 同期增长量：currentPct - lastPct（不乘100，按小数形式输出）
    private BigDecimal nplRatePctYoyDelta;
    private BigDecimal nplRatePctYoyRate;
}

