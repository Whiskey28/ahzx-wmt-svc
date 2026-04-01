package com.wmt.module.credit.report.controller.admin.report.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 季报（小微贷款）- 为放贷机构提供小微企业征信服务次数统计 Response VO
 */
@Schema(description = "管理后台 - 季报（小微贷款）为放贷机构提供小微企业征信服务次数统计 Response VO")
@Data
public class ReportQuarterMicroLoanServiceCountStatRespVO {

    @Schema(description = "季度周期（yyyyQn）", example = "2026Q1")
    private String periodId;

    @Schema(description = "报表模板id（jimu_report.id）", example = "1178489730218569728")
    private String reportId;

    private BigDecimal serviceCountTotalCurrent;
    private BigDecimal serviceCountTotalYtd;
    private BigDecimal serviceCountTotalYoyDelta;
    private BigDecimal serviceCountTotalYoyRate;
}

