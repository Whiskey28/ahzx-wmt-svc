package com.wmt.module.credit.report.controller.admin.report.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 积木报表 - 提供的征信产品(服务)三项合计 响应 VO
 *
 * 对应下半部分三行：
 * - 企业信用报告类（report_year_count）
 * - 信用分类（credit_year_count）
 * - 反欺诈类（anti_year_count）
 *
 * 每项为：汇总求和 + 3000 × 当前月数。
 *
 * @author Auto
 */
@Schema(description = "积木报表 - 提供的征信产品(服务)三项合计 响应 VO")
@Data
public class JmReportProductServiceTotalRespVO {

    @Schema(description = "企业信用报告、招投标报告：当年提供次数合计（含 +3000×当前月数）")
    private BigDecimal reportYearTotal;

    @Schema(description = "信用分：当年提供次数合计（含 +3000×当前月数）")
    private BigDecimal creditYearTotal;

    @Schema(description = "反欺诈：当年提供次数合计（含 +3000×当前月数）")
    private BigDecimal antiYearTotal;
}

