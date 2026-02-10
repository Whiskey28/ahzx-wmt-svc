package com.wmt.module.credit.report.controller.admin.report.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * 积木报表 - 提供的征信产品(服务)数据请求 VO
 *
 * @author Auto
 */
@Data
public class JmReportProductServiceReqVO {

    @Schema(description = "报表模板ID（jimu_report.id）", example = "111")
    @NotBlank(message = "报表模板ID不能为空")
    private String reportId;

    @Schema(description = "填报周期（格式：yyyy-MM）", example = "2026-01")
    @NotBlank(message = "填报周期不能为空")
    private String periodId;

    @Schema(description = "指定填报记录ID，可选。若不传则聚合该周期+报表下所有角色的数据", example = "a9f1b1c2d3e4f5")
    private String recordId;
}
