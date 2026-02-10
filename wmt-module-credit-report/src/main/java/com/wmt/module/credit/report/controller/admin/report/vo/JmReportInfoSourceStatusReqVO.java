package com.wmt.module.credit.report.controller.admin.report.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * 积木报表 - 信息来源情况数据请求 VO
 *
 * @author Auto
 */
@Data
public class JmReportInfoSourceStatusReqVO {

    @Schema(description = "报表模板ID（jimu_report.id）", example = "1178553700000000999")
    @NotBlank(message = "报表模板ID不能为空")
    private String reportId;

    @Schema(description = "填报周期（格式：yyyy-MM）", example = "2026-01")
    @NotBlank(message = "填报周期不能为空")
    private String periodId;

    @Schema(description = "指定填报记录ID，可选。若不传则自动获取该周期最新记录", example = "a9f1b1c2d3e4f5")
    private String recordId;
}
