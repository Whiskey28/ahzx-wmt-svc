package com.wmt.module.credit.report.controller.admin.report.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 积木报表 - 产品与服务提供情况（按行业）当年提供次数（字段展开）响应 VO
 *
 * 仅返回 yearServiceCount，按 industry_code 字典 item_value 一一展开为字段，方便积木报表直接绑定。
 *
 * @author Auto
 */
@Data
public class JmReportServiceByIndustryYearCountFieldsRespVO {

    @Schema(description = "填报周期", example = "2026-01")
    private String periodId;

    @Schema(description = "报表模板ID", example = "1178570056840228864")
    private String reportId;

    @Schema(description = "当年提供产品(服务)次数（总计）")
    private BigDecimal totalYearServiceCount;

    // 金融、类金融机构（12个）
    private BigDecimal bank;
    private BigDecimal security;
    private BigDecimal insurance;
    private BigDecimal trust;
    private BigDecimal p2pLending;
    private BigDecimal paymentInstitution;
    private BigDecimal financialLeasingGuarantee;
    private BigDecimal microLoanCompany;
    private BigDecimal consumerFinanceCompany;
    private BigDecimal assetManagementCompany;
    private BigDecimal autoFinanceCompany;
    private BigDecimal commercialFactoringCompany;

    // 其他行业（11个）
    private BigDecimal government;
    private BigDecimal publicUtilities;
    private BigDecimal industryAssociation;
    private BigDecimal court;
    private BigDecimal ecommercePlatform;
    private BigDecimal agriculturalEnterprise;
    private BigDecimal otherCreditAgency;
    private BigDecimal dataServiceProvider;
    private BigDecimal counterparty;
    private BigDecimal informationSubjectItself;
    private BigDecimal other;
}

