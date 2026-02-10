package com.wmt.module.credit.report.controller.admin.report.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 信息使用者机构按行业统计明细 Response VO
 *
 * @author Auto
 */
@Data
public class ReportInfoUserStatByIndustryItemRespVO {

    @Schema(description = "行业代码（industry_code 字典 item_value）", example = "bank")
    private String industryCode;

    @Schema(description = "行业名称", example = "银行")
    private String industryName;

    @Schema(description = "信息使用者机构总累计数（去重）」", example = "34")
    private BigDecimal userOrgTotal;

    @Schema(description = "当前使用服务的信息使用者机构数（去重）", example = "21")
    private BigDecimal userOrgCurrent;
}

