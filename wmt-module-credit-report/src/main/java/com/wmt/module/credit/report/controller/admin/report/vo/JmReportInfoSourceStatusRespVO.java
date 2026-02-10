package com.wmt.module.credit.report.controller.admin.report.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * 积木报表 - 信息来源情况响应 VO
 *
 * @author Auto
 */
@Data
public class JmReportInfoSourceStatusRespVO {

    @Schema(description = "填报周期", example = "2026-01")
    private String periodId;

    @Schema(description = "报表模板ID", example = "1178553700000000999")
    private String reportId;

    @Schema(description = "填报记录ID（未找到时返回 null）", example = "a9f1b1c2d3e4f5")
    private String recordId;

    @Schema(description = "累计签约机构总计")
    private BigDecimal totalProviderOrgTotal;

    @Schema(description = "当前提供服务机构总计")
    private BigDecimal totalProviderOrgCurrent;

    @Schema(description = "明细行列表（按字典顺序）")
    private List<JmReportInfoSourceStatusItemRespVO> items;
}
