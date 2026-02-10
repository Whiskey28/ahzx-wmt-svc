package com.wmt.module.credit.report.controller.admin.report.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 积木报表 - 产品与服务提供情况（按行业）总计响应 VO
 *
 * @author Auto
 */
@Data
public class JmReportServiceByIndustryTotalRespVO {

    @Schema(description = "填报周期", example = "2026-01")
    private String periodId;

    @Schema(description = "报表模板ID", example = "111")
    private String reportId;

    @Schema(description = "信息使用者机构总累计数（总计）")
    private BigDecimal totalUserOrgTotal;

    @Schema(description = "当前使用服务的信息使用者机构数（总计）")
    private BigDecimal totalUserOrgCurrent;

    @Schema(description = "当年提供产品(服务)次数（总计）")
    private BigDecimal totalYearServiceCount;
}
