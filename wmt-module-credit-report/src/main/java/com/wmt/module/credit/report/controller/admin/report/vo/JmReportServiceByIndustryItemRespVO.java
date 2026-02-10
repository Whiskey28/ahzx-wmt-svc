package com.wmt.module.credit.report.controller.admin.report.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 积木报表 - 产品与服务提供情况（按行业）明细项 VO
 *
 * @author Auto
 */
@Data
public class JmReportServiceByIndustryItemRespVO {

    @Schema(description = "行业代码", example = "bank")
    private String industryCode;

    @Schema(description = "行业名称", example = "银行")
    private String industryName;

    @Schema(description = "信息使用者机构总累计数（按机构名称去重）")
    private BigDecimal userOrgTotal;

    @Schema(description = "当前使用服务的信息使用者机构数（按机构名称去重）")
    private BigDecimal userOrgCurrent;

    @Schema(description = "当年提供产品(服务)次数（聚合所有角色）")
    private BigDecimal yearServiceCount;
}
