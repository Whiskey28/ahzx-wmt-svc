package com.wmt.module.credit.report.controller.admin.report.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * 信息使用者机构按行业统计 Response VO
 *
 * @author Auto
 */
@Data
public class ReportInfoUserStatByIndustryRespVO {

    @Schema(description = "各行业明细列表（按字典顺序）")
    private List<ReportInfoUserStatByIndustryItemRespVO> items;

    @Schema(description = "全部行业的信息使用者机构总累计数（去重）", example = "183")
    private BigDecimal totalUserOrgTotal;

    @Schema(description = "全部行业的当前使用服务的信息使用者机构数（去重）", example = "97")
    private BigDecimal totalUserOrgCurrent;
}

