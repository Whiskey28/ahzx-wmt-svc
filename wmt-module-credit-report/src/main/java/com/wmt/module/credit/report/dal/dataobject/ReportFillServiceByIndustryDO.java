package com.wmt.module.credit.report.dal.dataobject;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.wmt.framework.mybatis.core.dataobject.BaseDO;
import lombok.*;

import java.math.BigDecimal;

/**
 * 报表填报 - 产品与服务提供情况（固定字段，1:1）
 * 每个字段与字典 industry_code（23项）一一对应：当年提供产品（服务）次数
 *
 * @author AHC源码
 */
@TableName("report_fill_service_by_industry")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReportFillServiceByIndustryDO extends BaseDO {

    /**
     * 主键
     */
    @TableId(type = IdType.ASSIGN_UUID)
    private String id;

    /**
     * 主记录id（= report_fill_basic_info.id）
     */
    private String recordId;

    // 金融、类金融机构（12个）
    private BigDecimal bankYearServiceCount;
    private BigDecimal securityYearServiceCount;
    private BigDecimal insuranceYearServiceCount;
    private BigDecimal trustYearServiceCount;
    private BigDecimal p2pLendingYearServiceCount;
    private BigDecimal paymentInstitutionYearServiceCount;
    private BigDecimal financialLeasingGuaranteeYearServiceCount;
    private BigDecimal microLoanCompanyYearServiceCount;
    private BigDecimal consumerFinanceCompanyYearServiceCount;
    private BigDecimal assetManagementCompanyYearServiceCount;
    private BigDecimal autoFinanceCompanyYearServiceCount;
    private BigDecimal commercialFactoringCompanyYearServiceCount;

    // 其他行业（11个）
    private BigDecimal governmentYearServiceCount;
    private BigDecimal publicUtilitiesYearServiceCount;
    private BigDecimal industryAssociationYearServiceCount;
    private BigDecimal courtYearServiceCount;
    private BigDecimal ecommercePlatformYearServiceCount;
    private BigDecimal agriculturalEnterpriseYearServiceCount;
    private BigDecimal otherCreditAgencyYearServiceCount;
    private BigDecimal dataServiceProviderYearServiceCount;
    private BigDecimal counterpartyYearServiceCount;
    private BigDecimal informationSubjectItselfYearServiceCount;
    private BigDecimal otherYearServiceCount;

}

