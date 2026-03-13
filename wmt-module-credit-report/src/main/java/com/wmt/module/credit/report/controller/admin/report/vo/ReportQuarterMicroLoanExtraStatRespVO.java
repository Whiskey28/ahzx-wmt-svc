package com.wmt.module.credit.report.controller.admin.report.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 季报 - 额外两项指标统计 Response VO（字段全展开）
 *
 * - 数据库收录小微企业户数：来自月报信息采集情况表的 collect_micro_sme_total
 * - 为放贷机构提供小微企业征信服务次数：来自 service-by-industry/form-stat 的总计 yearServiceCount
 */
@Schema(description = "管理后台 - 季报额外两项指标统计 Response VO")
@Data
public class ReportQuarterMicroLoanExtraStatRespVO {

    @Schema(description = "季度周期（yyyyQn）", example = "2026Q1")
    private String periodId;

    @Schema(description = "角色id", example = "208")
    private String roleId;

    @Schema(description = "月报表单模板id（jimu_report.id）", example = "1191986555265200128")
    private String reportId;

    // 1) 数据库收录小微企业户数
    private BigDecimal collectMicroSmeTotalCurrent;
    private BigDecimal collectMicroSmeTotalYtd;
    private BigDecimal collectMicroSmeTotalYoyDelta;
    private BigDecimal collectMicroSmeTotalYoyRate;

    // 2) 为放贷机构提供小微企业征信服务次数
    private BigDecimal serviceCountTotalCurrent;
    private BigDecimal serviceCountTotalYtd;
    private BigDecimal serviceCountTotalYoyDelta;
    private BigDecimal serviceCountTotalYoyRate;
}

