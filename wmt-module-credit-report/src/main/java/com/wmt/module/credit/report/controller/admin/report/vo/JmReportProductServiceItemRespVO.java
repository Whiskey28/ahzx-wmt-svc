package com.wmt.module.credit.report.controller.admin.report.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 积木报表 - 提供的征信产品(服务)明细项 VO
 *
 * @author Auto
 */
@Data
public class JmReportProductServiceItemRespVO {

    @Schema(description = "产品类型代码", example = "report_type")
    private String productTypeCode;

    @Schema(description = "产品类型名称", example = "信用报告产品名称")
    private String productTypeName;

    @Schema(description = "产品名称代码", example = "report")
    private String productNameCode;

    @Schema(description = "产品名称", example = "企业信用报告、招投标报告")
    private String productName;

    @Schema(description = "当年提供次数（聚合所有角色）")
    private BigDecimal yearCount;
}
