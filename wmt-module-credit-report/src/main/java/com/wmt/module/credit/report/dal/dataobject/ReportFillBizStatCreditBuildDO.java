package com.wmt.module.credit.report.dal.dataobject;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.wmt.framework.mybatis.core.dataobject.BaseDO;
import lombok.*;

import java.math.BigDecimal;

/**
 * 报表填报 - 经营情况（信用体系建设）DO（1:1 关联主记录）
 *
 * @author AHC源码
 */
@TableName("report_fill_biz_stat_credit_build")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReportFillBizStatCreditBuildDO extends BaseDO {

    /**
     * 主键（UUID）
     */
    @TableId(type = IdType.ASSIGN_UUID)
    private String id;

    /**
     * 父记录id（= report_fill_basic_info.id）
     */
    private String parentId;

    /**
     * 企业参与行业、地方信用体系建设项目数
     */
    private BigDecimal projectCount;

    /**
     * 企业参与行业、地方信用体系建设收入（单位：人民币元）
     */
    private BigDecimal incomeAmount;

    /**
     * 银行-信息使用者机构总累计数
     */
    private BigDecimal userOrgTotalBank;
    /**
     * 银行-当前使用服务的信息使用者机构数
     */
    private BigDecimal userOrgCurrentBank;

    /**
     * 证券-信息使用者机构总累计数
     */
    private BigDecimal userOrgTotalSecurities;
    /**
     * 证券-当前使用服务的信息使用者机构数
     */
    private BigDecimal userOrgCurrentSecurities;

    /**
     * 保险-信息使用者机构总累计数
     */
    private BigDecimal userOrgTotalInsurance;
    /**
     * 保险-当前使用服务的信息使用者机构数
     */
    private BigDecimal userOrgCurrentInsurance;

    /**
     * 信托-信息使用者机构总累计数
     */
    private BigDecimal userOrgTotalTrust;
    /**
     * 信托-当前使用服务的信息使用者机构数
     */
    private BigDecimal userOrgCurrentTrust;

    /**
     * P2P网络借贷中介-信息使用者机构总累计数
     */
    private BigDecimal userOrgTotalP2pLendingIntermediary;
    /**
     * P2P网络借贷中介-当前使用服务的信息使用者机构数
     */
    private BigDecimal userOrgCurrentP2pLendingIntermediary;

    /**
     * 支付机构-信息使用者机构总累计数
     */
    private BigDecimal userOrgTotalPaymentInstitution;
    /**
     * 支付机构-当前使用服务的信息使用者机构数
     */
    private BigDecimal userOrgCurrentPaymentInstitution;

    /**
     * 融资租赁及担保类公司-信息使用者机构总累计数
     */
    private BigDecimal userOrgTotalFinancingLeasingGuarantee;
    /**
     * 融资租赁及担保类公司-当前使用服务的信息使用者机构数
     */
    private BigDecimal userOrgCurrentFinancingLeasingGuarantee;

    /**
     * 小额贷款公司-信息使用者机构总累计数
     */
    private BigDecimal userOrgTotalMicroLoanCompany;
    /**
     * 小额贷款公司-当前使用服务的信息使用者机构数
     */
    private BigDecimal userOrgCurrentMicroLoanCompany;

    /**
     * 消费金融公司-信息使用者机构总累计数
     */
    private BigDecimal userOrgTotalConsumerFinanceCompany;
    /**
     * 消费金融公司-当前使用服务的信息使用者机构数
     */
    private BigDecimal userOrgCurrentConsumerFinanceCompany;

    /**
     * 资产管理公司-信息使用者机构总累计数
     */
    private BigDecimal userOrgTotalAssetManagementCompany;
    /**
     * 资产管理公司-当前使用服务的信息使用者机构数
     */
    private BigDecimal userOrgCurrentAssetManagementCompany;

    /**
     * 汽车金融公司-信息使用者机构总累计数
     */
    private BigDecimal userOrgTotalAutoFinanceCompany;
    /**
     * 汽车金融公司-当前使用服务的信息使用者机构数
     */
    private BigDecimal userOrgCurrentAutoFinanceCompany;

    /**
     * 商业保理公司-信息使用者机构总累计数
     */
    private BigDecimal userOrgTotalCommercialFactoringCompany;
    /**
     * 商业保理公司-当前使用服务的信息使用者机构数
     */
    private BigDecimal userOrgCurrentCommercialFactoringCompany;

    /**
     * 政府-信息使用者机构总累计数
     */
    private BigDecimal userOrgTotalGovernment;
    /**
     * 政府-当前使用服务的信息使用者机构数
     */
    private BigDecimal userOrgCurrentGovernment;

    /**
     * 公用事业单位（水、电、气、通讯等）-信息使用者机构总累计数
     */
    private BigDecimal userOrgTotalPublicUtility;
    /**
     * 公用事业单位（水、电、气、通讯等）-当前使用服务的信息使用者机构数
     */
    private BigDecimal userOrgCurrentPublicUtility;

    /**
     * 行业协会-信息使用者机构总累计数
     */
    private BigDecimal userOrgTotalIndustryAssociation;
    /**
     * 行业协会-当前使用服务的信息使用者机构数
     */
    private BigDecimal userOrgCurrentIndustryAssociation;

    /**
     * 法院-信息使用者机构总累计数
     */
    private BigDecimal userOrgTotalCourt;
    /**
     * 法院-当前使用服务的信息使用者机构数
     */
    private BigDecimal userOrgCurrentCourt;

    /**
     * 电子商务平台-信息使用者机构总累计数
     */
    private BigDecimal userOrgTotalEcommercePlatform;
    /**
     * 电子商务平台-当前使用服务的信息使用者机构数
     */
    private BigDecimal userOrgCurrentEcommercePlatform;

    /**
     * 涉农企业-信息使用者机构总累计数
     */
    private BigDecimal userOrgTotalAgricultureRelatedEnterprise;
    /**
     * 涉农企业-当前使用服务的信息使用者机构数
     */
    private BigDecimal userOrgCurrentAgricultureRelatedEnterprise;

    /**
     * 其他征信机构-信息使用者机构总累计数
     */
    private BigDecimal userOrgTotalOtherCreditReportingAgency;
    /**
     * 其他征信机构-当前使用服务的信息使用者机构数
     */
    private BigDecimal userOrgCurrentOtherCreditReportingAgency;

    /**
     * 数据服务商-信息使用者机构总累计数
     */
    private BigDecimal userOrgTotalDataServiceProvider;
    /**
     * 数据服务商-当前使用服务的信息使用者机构数
     */
    private BigDecimal userOrgCurrentDataServiceProvider;

    /**
     * 交易对手方-信息使用者机构总累计数
     */
    private BigDecimal userOrgTotalTradingCounterparty;
    /**
     * 交易对手方-当前使用服务的信息使用者机构数
     */
    private BigDecimal userOrgCurrentTradingCounterparty;

    /**
     * 信息主体自身-信息使用者机构总累计数
     */
    private BigDecimal userOrgTotalInformationSubjectSelf;
    /**
     * 信息主体自身-当前使用服务的信息使用者机构数
     */
    private BigDecimal userOrgCurrentInformationSubjectSelf;

    /**
     * 其他-信息使用者机构总累计数
     */
    private BigDecimal userOrgTotalOther;
    /**
     * 其他-当前使用服务的信息使用者机构数
     */
    private BigDecimal userOrgCurrentOther;

}

