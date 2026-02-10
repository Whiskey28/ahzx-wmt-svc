package com.wmt.module.credit.report.dal.mysql;

import com.wmt.framework.mybatis.core.mapper.BaseMapperX;
import com.wmt.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.wmt.module.credit.report.dal.dataobject.ReportFillServiceByIndustryDO;
import org.apache.ibatis.annotations.Mapper;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * 报表填报 - 产品与服务提供情况（按行业分类）Mapper
 *
 * @author AHC源码
 */
@Mapper
public interface ReportFillServiceByIndustryMapper extends BaseMapperX<ReportFillServiceByIndustryDO> {

    default List<ReportFillServiceByIndustryDO> selectListByRecordId(String recordId) {
        return selectList(new LambdaQueryWrapperX<ReportFillServiceByIndustryDO>()
                .eq(ReportFillServiceByIndustryDO::getRecordId, recordId));
    }

    /**
     * 查询多个记录的产品与服务提供情况（用于聚合）
     *
     * @param recordIds 记录ID列表
     * @return 数据列表
     */
    default List<ReportFillServiceByIndustryDO> selectListByRecordIds(Collection<String> recordIds) {
        if (recordIds == null || recordIds.isEmpty()) {
            return List.of();
        }
        return selectList(new LambdaQueryWrapperX<ReportFillServiceByIndustryDO>()
                .in(ReportFillServiceByIndustryDO::getRecordId, recordIds));
    }

    /**
     * 按行业聚合产品与服务提供次数（聚合多个记录，固定字段结构）
     * 字典顺序：bank, security, insurance, trust, p2p_lending, payment_institution,
     * financial_leasing_guarantee, micro_loan_company, consumer_finance_company,
     * asset_management_company, auto_finance_company, commercial_factoring_company,
     * government, public_utilities, industry_association, court, ecommerce_platform,
     * agricultural_enterprise, other_credit_agency, data_service_provider,
     * counterparty, information_subject_itself, other
     *
     * @param recordIds 记录ID列表
     * @return Map<industryCode, 总次数>
     */
    default Map<String, BigDecimal> selectYearServiceCountByIndustryCode(Collection<String> recordIds) {
        List<ReportFillServiceByIndustryDO> list = selectListByRecordIds(recordIds);
        Map<String, BigDecimal> result = new java.util.HashMap<>();
        
        // 对每个固定字段求和
        result.put("bank", list.stream()
                .map(ReportFillServiceByIndustryDO::getBankYearServiceCount)
                .filter(Objects::nonNull)
                .reduce(BigDecimal.ZERO, BigDecimal::add));
        
        result.put("security", list.stream()
                .map(ReportFillServiceByIndustryDO::getSecurityYearServiceCount)
                .filter(Objects::nonNull)
                .reduce(BigDecimal.ZERO, BigDecimal::add));
        
        result.put("insurance", list.stream()
                .map(ReportFillServiceByIndustryDO::getInsuranceYearServiceCount)
                .filter(Objects::nonNull)
                .reduce(BigDecimal.ZERO, BigDecimal::add));
        
        result.put("trust", list.stream()
                .map(ReportFillServiceByIndustryDO::getTrustYearServiceCount)
                .filter(Objects::nonNull)
                .reduce(BigDecimal.ZERO, BigDecimal::add));
        
        result.put("p2p_lending", list.stream()
                .map(ReportFillServiceByIndustryDO::getP2pLendingYearServiceCount)
                .filter(Objects::nonNull)
                .reduce(BigDecimal.ZERO, BigDecimal::add));
        
        result.put("payment_institution", list.stream()
                .map(ReportFillServiceByIndustryDO::getPaymentInstitutionYearServiceCount)
                .filter(Objects::nonNull)
                .reduce(BigDecimal.ZERO, BigDecimal::add));
        
        result.put("financial_leasing_guarantee", list.stream()
                .map(ReportFillServiceByIndustryDO::getFinancialLeasingGuaranteeYearServiceCount)
                .filter(Objects::nonNull)
                .reduce(BigDecimal.ZERO, BigDecimal::add));
        
        result.put("micro_loan_company", list.stream()
                .map(ReportFillServiceByIndustryDO::getMicroLoanCompanyYearServiceCount)
                .filter(Objects::nonNull)
                .reduce(BigDecimal.ZERO, BigDecimal::add));
        
        result.put("consumer_finance_company", list.stream()
                .map(ReportFillServiceByIndustryDO::getConsumerFinanceCompanyYearServiceCount)
                .filter(Objects::nonNull)
                .reduce(BigDecimal.ZERO, BigDecimal::add));
        
        result.put("asset_management_company", list.stream()
                .map(ReportFillServiceByIndustryDO::getAssetManagementCompanyYearServiceCount)
                .filter(Objects::nonNull)
                .reduce(BigDecimal.ZERO, BigDecimal::add));
        
        result.put("auto_finance_company", list.stream()
                .map(ReportFillServiceByIndustryDO::getAutoFinanceCompanyYearServiceCount)
                .filter(Objects::nonNull)
                .reduce(BigDecimal.ZERO, BigDecimal::add));
        
        result.put("commercial_factoring_company", list.stream()
                .map(ReportFillServiceByIndustryDO::getCommercialFactoringCompanyYearServiceCount)
                .filter(Objects::nonNull)
                .reduce(BigDecimal.ZERO, BigDecimal::add));
        
        result.put("government", list.stream()
                .map(ReportFillServiceByIndustryDO::getGovernmentYearServiceCount)
                .filter(Objects::nonNull)
                .reduce(BigDecimal.ZERO, BigDecimal::add));
        
        result.put("public_utilities", list.stream()
                .map(ReportFillServiceByIndustryDO::getPublicUtilitiesYearServiceCount)
                .filter(Objects::nonNull)
                .reduce(BigDecimal.ZERO, BigDecimal::add));
        
        result.put("industry_association", list.stream()
                .map(ReportFillServiceByIndustryDO::getIndustryAssociationYearServiceCount)
                .filter(Objects::nonNull)
                .reduce(BigDecimal.ZERO, BigDecimal::add));
        
        result.put("court", list.stream()
                .map(ReportFillServiceByIndustryDO::getCourtYearServiceCount)
                .filter(Objects::nonNull)
                .reduce(BigDecimal.ZERO, BigDecimal::add));
        
        result.put("ecommerce_platform", list.stream()
                .map(ReportFillServiceByIndustryDO::getEcommercePlatformYearServiceCount)
                .filter(Objects::nonNull)
                .reduce(BigDecimal.ZERO, BigDecimal::add));
        
        result.put("agricultural_enterprise", list.stream()
                .map(ReportFillServiceByIndustryDO::getAgriculturalEnterpriseYearServiceCount)
                .filter(Objects::nonNull)
                .reduce(BigDecimal.ZERO, BigDecimal::add));
        
        result.put("other_credit_agency", list.stream()
                .map(ReportFillServiceByIndustryDO::getOtherCreditAgencyYearServiceCount)
                .filter(Objects::nonNull)
                .reduce(BigDecimal.ZERO, BigDecimal::add));
        
        result.put("data_service_provider", list.stream()
                .map(ReportFillServiceByIndustryDO::getDataServiceProviderYearServiceCount)
                .filter(Objects::nonNull)
                .reduce(BigDecimal.ZERO, BigDecimal::add));
        
        result.put("counterparty", list.stream()
                .map(ReportFillServiceByIndustryDO::getCounterpartyYearServiceCount)
                .filter(Objects::nonNull)
                .reduce(BigDecimal.ZERO, BigDecimal::add));
        
        result.put("information_subject_itself", list.stream()
                .map(ReportFillServiceByIndustryDO::getInformationSubjectItselfYearServiceCount)
                .filter(Objects::nonNull)
                .reduce(BigDecimal.ZERO, BigDecimal::add));
        
        result.put("other", list.stream()
                .map(ReportFillServiceByIndustryDO::getOtherYearServiceCount)
                .filter(Objects::nonNull)
                .reduce(BigDecimal.ZERO, BigDecimal::add));
        
        return result;
    }

    /**
     * 统计所有行业的总次数（聚合多个记录，固定字段结构）
     *
     * @param recordIds 记录ID列表
     * @return 总次数
     */
    default BigDecimal selectTotalYearServiceCount(Collection<String> recordIds) {
        List<ReportFillServiceByIndustryDO> list = selectListByRecordIds(recordIds);
        BigDecimal total = BigDecimal.ZERO;
        
        // 对所有固定字段求和
        for (ReportFillServiceByIndustryDO item : list) {
            if (item.getBankYearServiceCount() != null) total = total.add(item.getBankYearServiceCount());
            if (item.getSecurityYearServiceCount() != null) total = total.add(item.getSecurityYearServiceCount());
            if (item.getInsuranceYearServiceCount() != null) total = total.add(item.getInsuranceYearServiceCount());
            if (item.getTrustYearServiceCount() != null) total = total.add(item.getTrustYearServiceCount());
            if (item.getP2pLendingYearServiceCount() != null) total = total.add(item.getP2pLendingYearServiceCount());
            if (item.getPaymentInstitutionYearServiceCount() != null) total = total.add(item.getPaymentInstitutionYearServiceCount());
            if (item.getFinancialLeasingGuaranteeYearServiceCount() != null) total = total.add(item.getFinancialLeasingGuaranteeYearServiceCount());
            if (item.getMicroLoanCompanyYearServiceCount() != null) total = total.add(item.getMicroLoanCompanyYearServiceCount());
            if (item.getConsumerFinanceCompanyYearServiceCount() != null) total = total.add(item.getConsumerFinanceCompanyYearServiceCount());
            if (item.getAssetManagementCompanyYearServiceCount() != null) total = total.add(item.getAssetManagementCompanyYearServiceCount());
            if (item.getAutoFinanceCompanyYearServiceCount() != null) total = total.add(item.getAutoFinanceCompanyYearServiceCount());
            if (item.getCommercialFactoringCompanyYearServiceCount() != null) total = total.add(item.getCommercialFactoringCompanyYearServiceCount());
            if (item.getGovernmentYearServiceCount() != null) total = total.add(item.getGovernmentYearServiceCount());
            if (item.getPublicUtilitiesYearServiceCount() != null) total = total.add(item.getPublicUtilitiesYearServiceCount());
            if (item.getIndustryAssociationYearServiceCount() != null) total = total.add(item.getIndustryAssociationYearServiceCount());
            if (item.getCourtYearServiceCount() != null) total = total.add(item.getCourtYearServiceCount());
            if (item.getEcommercePlatformYearServiceCount() != null) total = total.add(item.getEcommercePlatformYearServiceCount());
            if (item.getAgriculturalEnterpriseYearServiceCount() != null) total = total.add(item.getAgriculturalEnterpriseYearServiceCount());
            if (item.getOtherCreditAgencyYearServiceCount() != null) total = total.add(item.getOtherCreditAgencyYearServiceCount());
            if (item.getDataServiceProviderYearServiceCount() != null) total = total.add(item.getDataServiceProviderYearServiceCount());
            if (item.getCounterpartyYearServiceCount() != null) total = total.add(item.getCounterpartyYearServiceCount());
            if (item.getInformationSubjectItselfYearServiceCount() != null) total = total.add(item.getInformationSubjectItselfYearServiceCount());
            if (item.getOtherYearServiceCount() != null) total = total.add(item.getOtherYearServiceCount());
        }
        
        return total;
    }

}

