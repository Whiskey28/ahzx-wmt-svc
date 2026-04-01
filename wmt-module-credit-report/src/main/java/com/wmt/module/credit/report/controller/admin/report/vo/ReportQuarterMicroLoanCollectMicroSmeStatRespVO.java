package com.wmt.module.credit.report.controller.admin.report.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 季报（小微贷款）- 数据库收录小微企业户数统计 Response VO
 */
@Schema(description = "管理后台 - 季报（小微贷款）数据库收录小微企业户数统计 Response VO")
@Data
public class ReportQuarterMicroLoanCollectMicroSmeStatRespVO {

    @Schema(description = "季度周期（yyyyQn）", example = "2026Q1")
    private String periodId;

    @Schema(description = "角色id", example = "208")
    private String roleId;

    @Schema(description = "报表模板id（jimu_report.id）", example = "1178489730218569728")
    private String reportId;

    private BigDecimal collectMicroSmeTotalCurrent;
    private BigDecimal collectMicroSmeTotalYtd;
    private BigDecimal collectMicroSmeTotalYoyDelta;
    private BigDecimal collectMicroSmeTotalYoyRate;
}

