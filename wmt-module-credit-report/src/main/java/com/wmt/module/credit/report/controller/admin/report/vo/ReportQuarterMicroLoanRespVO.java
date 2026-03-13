package com.wmt.module.credit.report.controller.admin.report.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 季报（小微贷款）- 填报数据 Response VO（仅当季发生额字段）
 */
@Schema(description = "管理后台 - 季报（小微贷款）填报数据 Response VO")
@Data
public class ReportQuarterMicroLoanRespVO {

    @Schema(description = "主记录id（report_fill_basic_info.id）", example = "a1b2c3")
    private String recordId;

    @Schema(description = "填报周期（格式：yyyyQn）", example = "2026Q1")
    private String periodId;

    @Schema(description = "角色id", example = "208")
    private String roleId;

    @Schema(description = "报表模板id（jimu_report.id）", example = "1178489730218569728")
    private String reportId;

    @Schema(description = "小微企业申请贷款户数（当季发生额）")
    private BigDecimal applyHouseholds;

    @Schema(description = "小微企业获得贷款户数（当季发生额）")
    private BigDecimal grantHouseholds;

    @Schema(description = "小微企业获得贷款金额（当季发生额）")
    private BigDecimal grantAmount;

    @Schema(description = "其中：信用贷款金额（当季发生额）")
    private BigDecimal creditGrantAmount;

    @Schema(description = "平均年利率（按金额加权平均计算，百分数）")
    private BigDecimal avgAnnualRatePct;

    @Schema(description = "不良率（按金额加权平均计算，百分数）")
    private BigDecimal nplRatePct;
}

