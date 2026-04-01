package com.wmt.module.credit.report.controller.admin.report.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

/**
 * 积木报表 - 产品与服务提供情况（按行业）统计（来自表单填写）响应 VO
 *
 * - 信息使用者机构总累计数、当前使用服务数：
 *   - 非政府：取市场部最新填报（经营情况-信用体系建设表单：report_fill_biz_stat_credit_build）
 *   - 政府：取 report_fill_service_by_industry.user_org_total_government / user_org_current_government（数据管理中心：roleId=208）
 * - 当年提供产品(服务)次数：来自 report_fill_service_by_industry，按 数据管理中心+企信部+普惠部 聚合
 *
 * @author Auto
 */
@Data
public class JmReportServiceByIndustryFormStatRespVO {

    @Schema(description = "填报周期", example = "2026-01")
    private String periodId;

    @Schema(description = "报表模板ID（用于统计当年提供次数）", example = "1178570056840228864")
    private String reportId;

    @Schema(description = "明细行列表（政府行在最后）")
    private List<JmReportServiceByIndustryItemRespVO> items;
}

