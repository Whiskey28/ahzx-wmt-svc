package com.wmt.module.credit.report.controller.admin.report.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * 季报（小微贷款）- 获取/初始化 Request VO
 */
@Schema(description = "管理后台 - 季报（小微贷款）获取/初始化 Request VO")
@Data
public class ReportQuarterMicroLoanGetReqVO {

    @NotBlank(message = "填报周期不能为空")
    @Schema(description = "填报周期（格式：yyyyQn）", example = "2026Q1", requiredMode = Schema.RequiredMode.REQUIRED)
    private String periodId;

    @NotBlank(message = "角色不能为空")
    @Schema(description = "角色id", example = "208", requiredMode = Schema.RequiredMode.REQUIRED)
    private String roleId;

    @NotBlank(message = "报表模板不能为空")
    @Schema(description = "报表模板id（jimu_report.id）", example = "1178489730218569728", requiredMode = Schema.RequiredMode.REQUIRED)
    private String reportId;
}

