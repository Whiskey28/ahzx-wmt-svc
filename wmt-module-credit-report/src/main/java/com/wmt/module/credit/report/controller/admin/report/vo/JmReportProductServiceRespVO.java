package com.wmt.module.credit.report.controller.admin.report.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * 积木报表 - 提供的征信产品(服务)响应 VO
 *
 * @author Auto
 */
@Data
public class JmReportProductServiceRespVO {

    @Schema(description = "填报周期", example = "2026-01")
    private String periodId;

    @Schema(description = "报表模板ID", example = "111")
    private String reportId;

    @Schema(description = "总计次数（从 report_fill_service_by_industry 聚合所有角色）")
    private BigDecimal totalYearServiceCount;

    @Schema(description = "征信产品次数（从 report_fill_product_stat 聚合所有角色）")
    private BigDecimal totalProductCount;

    @Schema(description = "其他征信服务产品次数（计算得出：totalYearServiceCount - totalProductCount）")
    private BigDecimal otherServiceCount;

    @Schema(description = "明细行列表（包含其他征信服务产品名称行）")
    private List<JmReportProductServiceItemRespVO> items;
}
