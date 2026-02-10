package com.wmt.module.credit.report.controller.admin.report.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 积木报表 - 信息来源情况明细行 VO
 *
 * @author Auto
 */
@Data
public class JmReportInfoSourceStatusItemRespVO {

    @Schema(description = "行业编码", example = "bank")
    private String industryCode;

    @Schema(description = "行业/来源名称", example = "银行")
    private String sourceTypeName;

    @Schema(description = "累计签约机构总数", example = "12")
    private BigDecimal providerOrgTotal;

    @Schema(description = "当前提供服务的机构数", example = "8")
    private BigDecimal providerOrgCurrent;
}
