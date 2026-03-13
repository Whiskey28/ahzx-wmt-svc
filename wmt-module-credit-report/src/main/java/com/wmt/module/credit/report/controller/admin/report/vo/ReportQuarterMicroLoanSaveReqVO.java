package com.wmt.module.credit.report.controller.admin.report.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 季报（小微贷款）- 保存 Request VO
 */
@Schema(description = "管理后台 - 季报（小微贷款）保存 Request VO")
@Data
public class ReportQuarterMicroLoanSaveReqVO {

    @NotBlank(message = "填报周期不能为空")
    @Schema(description = "填报周期（格式：yyyyQn）", example = "2026Q1", requiredMode = Schema.RequiredMode.REQUIRED)
    private String periodId;

    @NotBlank(message = "角色不能为空")
    @Schema(description = "角色id", example = "208", requiredMode = Schema.RequiredMode.REQUIRED)
    private String roleId;

    @NotBlank(message = "报表模板不能为空")
    @Schema(description = "报表模板id（jimu_report.id）", example = "1178489730218569728", requiredMode = Schema.RequiredMode.REQUIRED)
    private String reportId;

    @Schema(description = "小微企业申请贷款户数（当季发生额）")
    @DecimalMin(value = "0", message = "申请贷款户数不能小于0")
    private BigDecimal applyHouseholds;

    @Schema(description = "小微企业获得贷款户数（当季发生额）")
    @DecimalMin(value = "0", message = "获得贷款户数不能小于0")
    private BigDecimal grantHouseholds;

    @Schema(description = "小微企业获得贷款金额（当季发生额）")
    @DecimalMin(value = "0", message = "获得贷款金额不能小于0")
    private BigDecimal grantAmount;

    @Schema(description = "其中：信用贷款金额（当季发生额）")
    @DecimalMin(value = "0", message = "信用贷款金额不能小于0")
    private BigDecimal creditGrantAmount;

    @Schema(description = "平均年利率（按金额加权平均计算，百分数）")
    @DecimalMin(value = "0", message = "平均年利率不能小于0")
    private BigDecimal avgAnnualRatePct;

    @Schema(description = "不良率（按金额加权平均计算，百分数）")
    @DecimalMin(value = "0", message = "不良率不能小于0")
    private BigDecimal nplRatePct;
}

