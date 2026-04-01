package com.wmt.module.credit.report.service;

import cn.hutool.core.collection.CollUtil;
import com.wmt.framework.common.exception.enums.GlobalErrorCodeConstants;
import com.wmt.framework.common.pojo.PageResult;
import com.wmt.framework.common.pojo.SortablePageParam;
import com.wmt.framework.common.pojo.SortingField;
import com.wmt.framework.common.util.collection.CollectionUtils;
import com.wmt.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.wmt.module.credit.report.controller.admin.report.vo.*;
import com.wmt.module.credit.report.dal.dataobject.JimuDictItemDO;
import com.wmt.module.credit.report.dal.dataobject.ReportFillBasicInfoDO;
import com.wmt.module.credit.report.dal.dataobject.ReportFillBizStatCreditBuildDO;
import com.wmt.module.credit.report.dal.dataobject.ReportFillInfoSourceByIndustryDO;
import com.wmt.module.credit.report.dal.dataobject.ReportFillInfoUserOrgItemDO;
import com.wmt.module.credit.report.dal.dataobject.ReportFillInfoUserGovItemDO;
import com.wmt.module.credit.report.dal.dataobject.ReportFillServiceByIndustryDO;
import com.wmt.module.credit.report.dal.dataobject.ReportFillProductStatDO;
import com.wmt.module.credit.report.dal.mysql.JimuDictItemMapper;
import com.wmt.module.credit.report.dal.mysql.ReportFillBasicInfoMapper;
import com.wmt.module.credit.report.dal.mysql.ReportFillBizStatCreditBuildMapper;
import com.wmt.module.credit.report.dal.mysql.ReportFillInfoSourceByIndustryMapper;
import com.wmt.module.credit.report.dal.mysql.ReportFillInfoUserOrgItemMapper;
import com.wmt.module.credit.report.dal.mysql.ReportFillInfoUserGovItemMapper;
import com.wmt.module.credit.report.dal.mysql.ReportFillServiceByIndustryMapper;
import com.wmt.module.credit.report.dal.mysql.ReportFillProductStatMapper;
import com.wmt.module.credit.report.dal.mysql.ReportFillYangtzeCreditChainMapper;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.wmt.framework.common.exception.util.ServiceExceptionUtil.exception;

/**
 * 积木报表数据接口 Service 实现
 *
 * 将积木报表用到的查询集中管理，减少跨数据库兼容的 SQL 写法。
 *
 * @author Auto
 */
@Service
@Validated
@Slf4j
public class JimuReportDataServiceImpl implements JimuReportDataService {

    /**
     * 信息来源情况字典 ID（info_source_status）
     */
    private static final String INFO_SOURCE_STATUS_DICT_ID = "1178553700000000001";

    /**
     * 产品与服务提供情况字典 ID（industry_code）
     */
    private static final String INDUSTRY_CODE_DICT_ID = "1178553094177271808";

    /**
     * 征信产品类别字典 ID（credit_product_type）
     */
    private static final String CREDIT_PRODUCT_TYPE_DICT_ID = "1178468627483963392";

    /**
     * 提供的信用产品字典 ID（credit_pro）
     */
    private static final String CREDIT_PRO_DICT_ID = "1178145433573109760";

    /**
     * 上半部分：产品与服务提供情况（按行业）统计时，参与汇总的报表模板ID
     * （原 3 个 ID，其中 1178565077740007424 已替换为 1179654501395574784）
     */
    private static final List<String> SERVICE_REPORT_IDS = List.of(
            "1178570056840228864",
            "1178507517267783680",
            "1179654501395574784"
    );

    /**
     * 下半部分：提供的征信产品/服务次数统计时，参与汇总的报表模板ID
     * 1178471891197734912、1178507517267783680、1179654501395574784
     */
    private static final List<String> PRODUCT_REPORT_IDS = List.of(
            "1178471891197734912",
            "1178507517267783680",
            "1179654501395574784"
    );

    /**
     * 角色ID常量（对应 system_role.id）
     */
    private static final String ROLE_ID_MARKET_DEPT = "204";              // 市场部填报人
    private static final String ROLE_ID_DATA_CENTER = "208";              // 数据管理中心填报人
    private static final String ROLE_ID_ENTERPRISE_CREDIT = "206";         // 企业信用部填报人
    private static final String ROLE_ID_INCLUSIVE_CREDIT = "207";         // 普惠信用部填报人
    private static final String ROLE_ID_RD_CENTER = "203";                // 创新研发中心填报人

    /**
     * “经营情况-信用体系建设”报表模板ID（用于获取信息使用者机构总累计数/当前使用服务数）
     * 说明：该模板由市场部填报；政府口径由数据管理中心填报。
     */
    private static final String OPERATING_CREDIT_BUILD_REPORT_ID = "1178489730218569728";

    @Resource
    private JimuDictItemMapper jimuDictItemMapper;

    @Resource
    private ReportFillBasicInfoMapper reportFillBasicInfoMapper;

    @Resource
    private ReportFillBizStatCreditBuildMapper reportFillBizStatCreditBuildMapper;

    @Resource
    private ReportFillInfoSourceByIndustryMapper reportFillInfoSourceByIndustryMapper;

    @Resource
    private ReportFillInfoUserOrgItemMapper reportFillInfoUserOrgItemMapper;

    @Resource
    private ReportFillInfoUserGovItemMapper reportFillInfoUserGovItemMapper;

    @Resource
    private ReportFillServiceByIndustryMapper reportFillServiceByIndustryMapper;

    @Resource
    private ReportFillProductStatMapper reportFillProductStatMapper;

    @Resource
    private ReportFillYangtzeCreditChainMapper reportFillYangtzeCreditChainMapper;

    @Override
    public JmReportInfoSourceStatusRespVO getInfoSourceStatus(JmReportInfoSourceStatusReqVO reqVO) {
        // 1. 读取信息来源字典，保证固定的21个行业顺序
        List<JimuDictItemDO> dictItems = jimuDictItemMapper.selectEnabledListByDictId(INFO_SOURCE_STATUS_DICT_ID);
        if (CollUtil.isEmpty(dictItems)) {
            throw exception(GlobalErrorCodeConstants.NOT_FOUND, "信息来源情况字典未配置或未启用");
        }

        // 2. 确定要使用的填报记录：只统计数据管理中心填报人（roleId=208）的记录
        //    优先 recordId，其次按周期+报表+数据部角色取最新
        ReportFillBasicInfoDO targetRecord = resolveRecordForInfoSource(reqVO);
        Map<String, ReportFillInfoSourceByIndustryDO> dataMap = Collections.emptyMap();
        if (targetRecord != null) {
            List<ReportFillInfoSourceByIndustryDO> dataList =
                    reportFillInfoSourceByIndustryMapper.selectListByRecordId(targetRecord.getId());
            dataMap = CollectionUtils.convertMap(dataList, ReportFillInfoSourceByIndustryDO::getIndustryCode);
        }

        // 3. 组装明细并计算总计
        BigDecimal totalProviderOrgTotal = BigDecimal.ZERO;
        BigDecimal totalProviderOrgCurrent = BigDecimal.ZERO;
        List<JmReportInfoSourceStatusItemRespVO> items = new ArrayList<>(dictItems.size());

        for (JimuDictItemDO dictItem : dictItems) {
            ReportFillInfoSourceByIndustryDO data = dataMap.get(dictItem.getItemValue());
            BigDecimal providerOrgTotal = getNumberOrZero(data, ReportFillInfoSourceByIndustryDO::getProviderOrgTotal);
            BigDecimal providerOrgCurrent = getNumberOrZero(data, ReportFillInfoSourceByIndustryDO::getProviderOrgCurrent);

            totalProviderOrgTotal = totalProviderOrgTotal.add(providerOrgTotal);
            totalProviderOrgCurrent = totalProviderOrgCurrent.add(providerOrgCurrent);

            JmReportInfoSourceStatusItemRespVO itemVO = new JmReportInfoSourceStatusItemRespVO();
            itemVO.setIndustryCode(dictItem.getItemValue());
            itemVO.setSourceTypeName(dictItem.getItemText());
            itemVO.setProviderOrgTotal(providerOrgTotal);
            itemVO.setProviderOrgCurrent(providerOrgCurrent);
            items.add(itemVO);
        }

        // 4. 组装响应
        JmReportInfoSourceStatusRespVO respVO = new JmReportInfoSourceStatusRespVO();
        respVO.setPeriodId(reqVO.getPeriodId());
        respVO.setReportId(reqVO.getReportId());
        respVO.setRecordId(targetRecord != null ? targetRecord.getId() : null);
        respVO.setTotalProviderOrgTotal(totalProviderOrgTotal);
        respVO.setTotalProviderOrgCurrent(totalProviderOrgCurrent);
        respVO.setItems(items);
        return respVO;
    }

    /**
     * 根据请求解析目标填报记录（信息来源情况专用，只统计数据管理中心填报人）：
     * 1) 如果指定了 recordId，则校验周期、报表、角色是否匹配；否则抛出异常。
     * 2) 未指定 recordId 时，按周期+报表+数据部角色查询最新一条填报记录；若不存在则返回 null。
     */
    private ReportFillBasicInfoDO resolveRecordForInfoSource(JmReportInfoSourceStatusReqVO reqVO) {
        if (StringUtils.isNotBlank(reqVO.getRecordId())) {
            ReportFillBasicInfoDO record = reportFillBasicInfoMapper.selectById(reqVO.getRecordId());
            if (record == null) {
                throw exception(GlobalErrorCodeConstants.NOT_FOUND, "指定的填报记录不存在");
            }
            if (!Objects.equals(reqVO.getReportId(), record.getReportId())) {
                throw exception(GlobalErrorCodeConstants.BAD_REQUEST, "填报记录不属于该报表模板");
            }
            if (!Objects.equals(reqVO.getPeriodId(), record.getPeriodId())) {
                throw exception(GlobalErrorCodeConstants.BAD_REQUEST, "填报记录的周期与请求参数不一致");
            }
            // 检查角色是否为数据管理中心填报人
            if (!Objects.equals(ROLE_ID_DATA_CENTER, record.getRoleId())) {
                throw exception(GlobalErrorCodeConstants.BAD_REQUEST, "信息来源情况只统计数据管理中心填报人的数据");
            }
            return record;
        }
        // 按周期+报表+数据部角色查询最新记录
        SortablePageParam pageParam = new SortablePageParam();
        pageParam.setPageNo(1);
        pageParam.setPageSize(1);
        pageParam.setSortingFields(List.of(new SortingField("create_time", SortingField.ORDER_DESC)));

        LambdaQueryWrapperX<ReportFillBasicInfoDO> wrapper = new LambdaQueryWrapperX<ReportFillBasicInfoDO>()
                .eq(ReportFillBasicInfoDO::getPeriodId, reqVO.getPeriodId())
                .eq(ReportFillBasicInfoDO::getReportId, reqVO.getReportId())
                .eq(ReportFillBasicInfoDO::getRoleId, ROLE_ID_DATA_CENTER);

        PageResult<ReportFillBasicInfoDO> pageResult = reportFillBasicInfoMapper.selectPage(pageParam, pageParam.getSortingFields(), wrapper);
        return CollUtil.getFirst(pageResult.getList());
    }

    private BigDecimal getNumberOrZero(ReportFillInfoSourceByIndustryDO data,
                                       Function<ReportFillInfoSourceByIndustryDO, BigDecimal> getter) {
        if (data == null) {
            return BigDecimal.ZERO;
        }
        BigDecimal value = getter.apply(data);
        return value == null ? BigDecimal.ZERO : value;
    }

    @Override
    public JmReportServiceByIndustryRespVO getServiceByIndustry(JmReportServiceByIndustryReqVO reqVO) {
        // 1. 读取行业字典，保证固定的23个行业顺序
        List<JimuDictItemDO> dictItems = jimuDictItemMapper.selectEnabledListByDictId(INDUSTRY_CODE_DICT_ID);
        if (CollUtil.isEmpty(dictItems)) {
            throw exception(GlobalErrorCodeConstants.NOT_FOUND, "产品与服务提供情况字典未配置或未启用");
        }

        // 2. 查询机构明细和政府明细（全局公共信息，不再按角色/recordId 细分）
        List<ReportFillInfoUserOrgItemDO> orgItems = reportFillInfoUserOrgItemMapper.selectAll();
        List<ReportFillInfoUserGovItemDO> govItems = reportFillInfoUserGovItemMapper.selectAll();

        // 3. 当年提供产品(服务)次数：按指定 3 个报表模板 + 数据管理部+普惠部+企信部角色聚合
        // 说明：与 form-stat 保持一致 —— 每个角色只取在这 3 个模板中最新一条记录，再进行求和
        List<String> serviceRecordIds = resolveLatestRecordIdsByPeriodAndReportsAndRoles(
                reqVO.getPeriodId(),
                SERVICE_REPORT_IDS,
                List.of(ROLE_ID_DATA_CENTER, ROLE_ID_INCLUSIVE_CREDIT, ROLE_ID_ENTERPRISE_CREDIT)
        );

        // 4. 统计非政府机构（从 org_item，按 org_name 去重）
        Map<String, UserOrgStat> orgStats = orgItems.stream()
                .filter(item -> item.getIndustryCode() != null && !"government".equals(item.getIndustryCode()))
                .collect(Collectors.groupingBy(
                        ReportFillInfoUserOrgItemDO::getIndustryCode,
                        Collectors.collectingAndThen(
                                Collectors.toList(),
                                list -> {
                                    long total = list.stream()
                                            .map(ReportFillInfoUserOrgItemDO::getOrgName)
                                            .filter(name -> name != null && !name.trim().isEmpty())
                                            .distinct()
                                            .count();
                                    long current = list.stream()
                                            .filter(item -> item.getIsCurrentService() != null && item.getIsCurrentService() == 1)
                                            .map(ReportFillInfoUserOrgItemDO::getOrgName)
                                            .filter(name -> name != null && !name.trim().isEmpty())
                                            .distinct()
                                            .count();
                                    return new UserOrgStat(BigDecimal.valueOf(total), BigDecimal.valueOf(current));
                                }
                        )
                ));

        // 5. 统计政府机构（从 gov_item，按 gov_org_name 去重，基于全表）
        long govTotal = govItems.stream()
                .map(ReportFillInfoUserGovItemDO::getGovOrgName)
                .filter(name -> name != null && !name.trim().isEmpty())
                .distinct()
                .count();
        long govCurrent = govItems.stream()
                .filter(item -> item.getIsCurrentService() != null && item.getIsCurrentService() == 1)
                .map(ReportFillInfoUserGovItemDO::getGovOrgName)
                .filter(name -> name != null && !name.trim().isEmpty())
                .distinct()
                .count();
        UserOrgStat govStat = new UserOrgStat(BigDecimal.valueOf(govTotal), BigDecimal.valueOf(govCurrent));

        // 6. 统计当年提供产品(服务)次数（从 service_by_industry，按 industry_code 聚合，只统计数据管理部+普惠部+企信部）
        Map<String, BigDecimal> serviceCountMap = reportFillServiceByIndustryMapper.selectYearServiceCountByIndustryCode(serviceRecordIds);

        // 7. 组装明细并在循环中累计总计
        List<JmReportServiceByIndustryItemRespVO> items = new ArrayList<>(dictItems.size());
        JmReportServiceByIndustryItemRespVO govItem = null;
        BigDecimal totalUserOrgTotal = BigDecimal.ZERO;
        BigDecimal totalUserOrgCurrent = BigDecimal.ZERO;
        BigDecimal totalYearServiceCount = reportFillServiceByIndustryMapper.selectTotalYearServiceCount(serviceRecordIds);

        for (JimuDictItemDO dictItem : dictItems) {
            String industryCode = dictItem.getItemValue();
            boolean isGovernment = "government".equals(industryCode);

            UserOrgStat stat = isGovernment ? govStat : orgStats.get(industryCode);
            BigDecimal userOrgTotal = stat != null ? stat.getUserOrgTotal() : BigDecimal.ZERO;
            BigDecimal userOrgCurrent = stat != null ? stat.getUserOrgCurrent() : BigDecimal.ZERO;
            BigDecimal yearServiceCount = serviceCountMap.getOrDefault(industryCode, BigDecimal.ZERO);

            totalUserOrgTotal = totalUserOrgTotal.add(userOrgTotal);
            totalUserOrgCurrent = totalUserOrgCurrent.add(userOrgCurrent);

            JmReportServiceByIndustryItemRespVO itemVO = new JmReportServiceByIndustryItemRespVO();
            itemVO.setIndustryCode(industryCode);
            itemVO.setIndustryName(dictItem.getItemText());
            itemVO.setUserOrgTotal(userOrgTotal);
            itemVO.setUserOrgCurrent(userOrgCurrent);
            itemVO.setYearServiceCount(yearServiceCount);

            if (isGovernment) {
                govItem = itemVO; // 保存政府行，最后添加
            } else {
                items.add(itemVO);
            }
        }

        // 8. 确保政府行在最后
        if (govItem != null) {
            items.add(govItem);
        }

        // 9. 组装响应
        JmReportServiceByIndustryRespVO respVO = new JmReportServiceByIndustryRespVO();
        respVO.setPeriodId(reqVO.getPeriodId());
        respVO.setReportId(reqVO.getReportId());
        respVO.setTotalUserOrgTotal(totalUserOrgTotal);
        respVO.setTotalUserOrgCurrent(totalUserOrgCurrent);
        respVO.setTotalYearServiceCount(totalYearServiceCount);
        respVO.setItems(items);
        return respVO;
    }

    @Override
    public JmReportServiceByIndustryYearCountFieldsRespVO getServiceByIndustryYearCountFields(JmReportServiceByIndustryReqVO reqVO) {
        // 统计口径：与 getServiceByIndustry 的“当年提供产品(服务)次数”一致
        // 但这里必须按请求的 periodId + reportId +（数据管理中心/普惠/企信）角色聚合，不再使用固定 SERVICE_REPORT_IDS

        String periodId = reqVO.getPeriodId();
        String reportId = reqVO.getReportId();

        List<String> serviceRecordIds = resolveLatestRecordIdsByPeriodAndReportAndRoles(
                periodId, reportId,
                List.of(ROLE_ID_DATA_CENTER, ROLE_ID_INCLUSIVE_CREDIT, ROLE_ID_ENTERPRISE_CREDIT)
        );

        Map<String, BigDecimal> serviceCountMap = reportFillServiceByIndustryMapper.selectYearServiceCountByIndustryCode(serviceRecordIds);
        BigDecimal totalYearServiceCount = reportFillServiceByIndustryMapper.selectTotalYearServiceCount(serviceRecordIds);

        JmReportServiceByIndustryYearCountFieldsRespVO respVO = new JmReportServiceByIndustryYearCountFieldsRespVO();
        respVO.setPeriodId(periodId);
        respVO.setReportId(reportId);
        respVO.setTotalYearServiceCount(totalYearServiceCount);

        respVO.setBank(serviceCountMap.getOrDefault("bank", BigDecimal.ZERO));
        respVO.setSecurity(serviceCountMap.getOrDefault("security", BigDecimal.ZERO));
        respVO.setInsurance(serviceCountMap.getOrDefault("insurance", BigDecimal.ZERO));
        respVO.setTrust(serviceCountMap.getOrDefault("trust", BigDecimal.ZERO));
        respVO.setP2pLending(serviceCountMap.getOrDefault("p2p_lending", BigDecimal.ZERO));
        respVO.setPaymentInstitution(serviceCountMap.getOrDefault("payment_institution", BigDecimal.ZERO));
        respVO.setFinancialLeasingGuarantee(serviceCountMap.getOrDefault("financial_leasing_guarantee", BigDecimal.ZERO));
        respVO.setMicroLoanCompany(serviceCountMap.getOrDefault("micro_loan_company", BigDecimal.ZERO));
        respVO.setConsumerFinanceCompany(serviceCountMap.getOrDefault("consumer_finance_company", BigDecimal.ZERO));
        respVO.setAssetManagementCompany(serviceCountMap.getOrDefault("asset_management_company", BigDecimal.ZERO));
        respVO.setAutoFinanceCompany(serviceCountMap.getOrDefault("auto_finance_company", BigDecimal.ZERO));
        respVO.setCommercialFactoringCompany(serviceCountMap.getOrDefault("commercial_factoring_company", BigDecimal.ZERO));

        respVO.setGovernment(serviceCountMap.getOrDefault("government", BigDecimal.ZERO));
        respVO.setPublicUtilities(serviceCountMap.getOrDefault("public_utilities", BigDecimal.ZERO));
        respVO.setIndustryAssociation(serviceCountMap.getOrDefault("industry_association", BigDecimal.ZERO));
        respVO.setCourt(serviceCountMap.getOrDefault("court", BigDecimal.ZERO));
        respVO.setEcommercePlatform(serviceCountMap.getOrDefault("ecommerce_platform", BigDecimal.ZERO));
        respVO.setAgriculturalEnterprise(serviceCountMap.getOrDefault("agricultural_enterprise", BigDecimal.ZERO));
        respVO.setOtherCreditAgency(serviceCountMap.getOrDefault("other_credit_agency", BigDecimal.ZERO));
        respVO.setDataServiceProvider(serviceCountMap.getOrDefault("data_service_provider", BigDecimal.ZERO));
        respVO.setCounterparty(serviceCountMap.getOrDefault("counterparty", BigDecimal.ZERO));
        respVO.setInformationSubjectItself(serviceCountMap.getOrDefault("information_subject_itself", BigDecimal.ZERO));
        respVO.setOther(serviceCountMap.getOrDefault("other", BigDecimal.ZERO));

        return respVO;
    }

    @Override
    public JmReportServiceByIndustryFormStatRespVO getServiceByIndustryFormStat(JmReportServiceByIndustryReqVO reqVO) {
        // 1) 行业字典（23项，保证顺序；政府行最后）
        List<JimuDictItemDO> dictItems = jimuDictItemMapper.selectEnabledListByDictId(INDUSTRY_CODE_DICT_ID);
        if (CollUtil.isEmpty(dictItems)) {
            throw exception(GlobalErrorCodeConstants.NOT_FOUND, "产品与服务提供情况字典未配置或未启用");
        }

        String periodId = reqVO.getPeriodId();
        String reportId = reqVO.getReportId();

        // 2) 当年提供次数：
        //    - 若传入的 reportId 属于上半部分统计的 3 个模板（SERVICE_REPORT_IDS），则按这 3 个模板合并统计，
        //      并且每个角色只取“这3个模板里最新的一条”记录再求和，避免漏算（例如企信部填在另一个模板上）
        //    - 否则仅统计传入的单个 reportId
        List<String> reportIdsToSum = SERVICE_REPORT_IDS.contains(reportId) ? SERVICE_REPORT_IDS : List.of(reportId);
        List<String> serviceRecordIds = resolveLatestRecordIdsByPeriodAndReportsAndRoles(
                periodId, reportIdsToSum,
                List.of(ROLE_ID_DATA_CENTER, ROLE_ID_INCLUSIVE_CREDIT, ROLE_ID_ENTERPRISE_CREDIT)
        );
        Map<String, BigDecimal> serviceCountMap = reportFillServiceByIndustryMapper.selectYearServiceCountByIndustryCode(serviceRecordIds);

        // 3) 机构总累计/当前使用：
        //    - 非政府：市场部最新填报（经营情况-信用体系建设表单）
        //    - 政府：数据管理中心最新 report_fill_service_by_industry.user_org_total_government / user_org_current_government
        ReportFillBizStatCreditBuildDO marketCreditBuild = selectLatestCreditBuildByPeriodAndRole(periodId, ROLE_ID_MARKET_DEPT);

        // 政府行口径：从 report_fill_service_by_industry 直接读取“政府用户机构总累计/当前使用服务”字段
        // 注意：此处固定只取 roleId=208（数据管理中心）对应的“最新一条”填报记录。
        // 政府口径严格使用本次请求的 reportId（例如 1178570056840228864），不参与上半部分 3 个模板的汇总逻辑。
        List<String> dataCenterServiceRecordIds = resolveLatestRecordIdsByPeriodAndReportAndRoles(
                periodId,
                reportId,
                List.of(ROLE_ID_DATA_CENTER)
        );
        ReportFillServiceByIndustryDO dataCenterService = CollUtil.isNotEmpty(dataCenterServiceRecordIds) ?
                CollUtil.getFirst(reportFillServiceByIndustryMapper.selectListByRecordIds(dataCenterServiceRecordIds)) :
                null;

        // 4) 组装 items（政府行最后）
        List<JmReportServiceByIndustryItemRespVO> items = new ArrayList<>(dictItems.size());
        JmReportServiceByIndustryItemRespVO govItem = null;
        BigDecimal totalUserOrgTotal = BigDecimal.ZERO;
        BigDecimal totalUserOrgCurrent = BigDecimal.ZERO;
        BigDecimal totalYearServiceCount = BigDecimal.ZERO;

        for (JimuDictItemDO dictItem : dictItems) {
            String industryCode = dictItem.getItemValue();
            // “产品与服务调用情况”只是表头标题，不参与明细
            if ("product_service_usage".equals(industryCode)) {
                continue;
            }
            boolean isGovernment = "government".equals(industryCode);

            BigDecimal userOrgTotal;
            BigDecimal userOrgCurrent;
            if (isGovernment) {
                userOrgTotal = dataCenterService != null && dataCenterService.getUserOrgTotalGovernment() != null ?
                        dataCenterService.getUserOrgTotalGovernment() : BigDecimal.ZERO;
                userOrgCurrent = dataCenterService != null && dataCenterService.getUserOrgCurrentGovernment() != null ?
                        dataCenterService.getUserOrgCurrentGovernment() : BigDecimal.ZERO;
            } else {
                userOrgTotal = getUserOrgTotalByIndustryCode(marketCreditBuild, industryCode);
                userOrgCurrent = getUserOrgCurrentByIndustryCode(marketCreditBuild, industryCode);
            }
            BigDecimal yearServiceCount = serviceCountMap.getOrDefault(industryCode, BigDecimal.ZERO);

            totalUserOrgTotal = totalUserOrgTotal.add(userOrgTotal);
            totalUserOrgCurrent = totalUserOrgCurrent.add(userOrgCurrent);
            totalYearServiceCount = totalYearServiceCount.add(yearServiceCount);

            JmReportServiceByIndustryItemRespVO itemVO = new JmReportServiceByIndustryItemRespVO();
            itemVO.setIndustryCode(industryCode);
            itemVO.setIndustryName(dictItem.getItemText());
            itemVO.setUserOrgTotal(userOrgTotal);
            itemVO.setUserOrgCurrent(userOrgCurrent);
            itemVO.setYearServiceCount(yearServiceCount);

            if (isGovernment) {
                govItem = itemVO;
            } else {
                items.add(itemVO);
            }
        }
        if (govItem != null) {
            items.add(govItem);
        }

        // 5) 政府行之后追加总计行
        JmReportServiceByIndustryItemRespVO totalItem = new JmReportServiceByIndustryItemRespVO();
        totalItem.setIndustryCode("total");
        totalItem.setIndustryName("总计");
        totalItem.setUserOrgTotal(totalUserOrgTotal);
        totalItem.setUserOrgCurrent(totalUserOrgCurrent);
        totalItem.setYearServiceCount(totalYearServiceCount);
        items.add(totalItem);

        JmReportServiceByIndustryFormStatRespVO respVO = new JmReportServiceByIndustryFormStatRespVO();
        respVO.setPeriodId(periodId);
        respVO.setReportId(reportId);
        respVO.setItems(items);
        return respVO;
    }

    /**
     * 解析“该周期 + 该报表 + 多角色”的最新记录ID列表：
     * 每个角色只取最新一条（按 create_time 倒序）。
     */
    private List<String> resolveLatestRecordIdsByPeriodAndReportAndRoles(String periodId, String reportId, List<String> roleIds) {
        if (StringUtils.isBlank(periodId) || StringUtils.isBlank(reportId) || CollUtil.isEmpty(roleIds)) {
            return List.of();
        }
        SortablePageParam pageParam = new SortablePageParam();
        pageParam.setPageNo(1);
        pageParam.setPageSize(1);
        pageParam.setSortingFields(List.of(new SortingField("create_time", SortingField.ORDER_DESC)));

        List<String> recordIds = new ArrayList<>(roleIds.size());
        for (String roleId : roleIds) {
            LambdaQueryWrapperX<ReportFillBasicInfoDO> wrapper = new LambdaQueryWrapperX<ReportFillBasicInfoDO>()
                    .eq(ReportFillBasicInfoDO::getPeriodId, periodId)
                    .eq(ReportFillBasicInfoDO::getReportId, reportId)
                    .eq(ReportFillBasicInfoDO::getRoleId, roleId);
            PageResult<ReportFillBasicInfoDO> pageResult = reportFillBasicInfoMapper.selectPage(pageParam, pageParam.getSortingFields(), wrapper);
            ReportFillBasicInfoDO record = CollUtil.getFirst(pageResult.getList());
            if (record != null) {
                recordIds.add(record.getId());
            }
        }
        return recordIds;
    }

    /**
     * 解析“该周期 + 多报表 + 多角色”的最新记录ID列表：
     * 每个角色只取在这些报表中最新一条（按 create_time 倒序）。
     */
    private List<String> resolveLatestRecordIdsByPeriodAndReportsAndRoles(String periodId, List<String> reportIds, List<String> roleIds) {
        if (StringUtils.isBlank(periodId) || CollUtil.isEmpty(reportIds) || CollUtil.isEmpty(roleIds)) {
            return List.of();
        }
        SortablePageParam pageParam = new SortablePageParam();
        pageParam.setPageNo(1);
        pageParam.setPageSize(1);
        pageParam.setSortingFields(List.of(new SortingField("create_time", SortingField.ORDER_DESC)));

        List<String> recordIds = new ArrayList<>(roleIds.size());
        for (String roleId : roleIds) {
            LambdaQueryWrapperX<ReportFillBasicInfoDO> wrapper = new LambdaQueryWrapperX<ReportFillBasicInfoDO>()
                    .eq(ReportFillBasicInfoDO::getPeriodId, periodId)
                    .in(ReportFillBasicInfoDO::getReportId, reportIds)
                    .eq(ReportFillBasicInfoDO::getRoleId, roleId);
            PageResult<ReportFillBasicInfoDO> pageResult = reportFillBasicInfoMapper.selectPage(pageParam, pageParam.getSortingFields(), wrapper);
            ReportFillBasicInfoDO record = CollUtil.getFirst(pageResult.getList());
            if (record != null) {
                recordIds.add(record.getId());
            }
        }
        return recordIds;
    }

    /**
     * 查询“经营情况-信用体系建设”最新填报记录（指定周期 + 指定角色）。
     */
    private ReportFillBizStatCreditBuildDO selectLatestCreditBuildByPeriodAndRole(String periodId, String roleId) {
        SortablePageParam pageParam = new SortablePageParam();
        pageParam.setPageNo(1);
        pageParam.setPageSize(1);
        pageParam.setSortingFields(List.of(new SortingField("create_time", SortingField.ORDER_DESC)));

        LambdaQueryWrapperX<ReportFillBasicInfoDO> wrapper = new LambdaQueryWrapperX<ReportFillBasicInfoDO>()
                .eq(ReportFillBasicInfoDO::getPeriodId, periodId)
                .eq(ReportFillBasicInfoDO::getReportId, OPERATING_CREDIT_BUILD_REPORT_ID)
                .eq(ReportFillBasicInfoDO::getRoleId, roleId);
        PageResult<ReportFillBasicInfoDO> pageResult = reportFillBasicInfoMapper.selectPage(pageParam, pageParam.getSortingFields(), wrapper);
        ReportFillBasicInfoDO record = CollUtil.getFirst(pageResult.getList());
        if (record == null) {
            return null;
        }
        return reportFillBizStatCreditBuildMapper.selectOne(new LambdaQueryWrapperX<ReportFillBizStatCreditBuildDO>()
                .eq(ReportFillBizStatCreditBuildDO::getParentId, record.getId()));
    }

    private BigDecimal getUserOrgTotalByIndustryCode(ReportFillBizStatCreditBuildDO data, String industryCode) {
        if (data == null) {
            return BigDecimal.ZERO;
        }
        return Optional.ofNullable(switch (industryCode) {
            case "bank" -> data.getUserOrgTotalBank();
            case "security" -> data.getUserOrgTotalSecurities();
            case "insurance" -> data.getUserOrgTotalInsurance();
            case "trust" -> data.getUserOrgTotalTrust();
            case "p2p_lending" -> data.getUserOrgTotalP2pLendingIntermediary();
            case "payment_institution" -> data.getUserOrgTotalPaymentInstitution();
            case "financial_leasing_guarantee" -> data.getUserOrgTotalFinancingLeasingGuarantee();
            case "micro_loan_company" -> data.getUserOrgTotalMicroLoanCompany();
            case "consumer_finance_company" -> data.getUserOrgTotalConsumerFinanceCompany();
            case "asset_management_company" -> data.getUserOrgTotalAssetManagementCompany();
            case "auto_finance_company" -> data.getUserOrgTotalAutoFinanceCompany();
            case "commercial_factoring_company" -> data.getUserOrgTotalCommercialFactoringCompany();
            case "government" -> data.getUserOrgTotalGovernment();
            case "public_utilities" -> data.getUserOrgTotalPublicUtility();
            case "industry_association" -> data.getUserOrgTotalIndustryAssociation();
            case "court" -> data.getUserOrgTotalCourt();
            case "ecommerce_platform" -> data.getUserOrgTotalEcommercePlatform();
            case "agricultural_enterprise" -> data.getUserOrgTotalAgricultureRelatedEnterprise();
            case "other_credit_agency" -> data.getUserOrgTotalOtherCreditReportingAgency();
            case "data_service_provider" -> data.getUserOrgTotalDataServiceProvider();
            case "counterparty" -> data.getUserOrgTotalTradingCounterparty();
            case "information_subject_itself" -> data.getUserOrgTotalInformationSubjectSelf();
            case "other" -> data.getUserOrgTotalOther();
            default -> null;
        }).orElse(BigDecimal.ZERO);
    }

    private BigDecimal getUserOrgCurrentByIndustryCode(ReportFillBizStatCreditBuildDO data, String industryCode) {
        if (data == null) {
            return BigDecimal.ZERO;
        }
        return Optional.ofNullable(switch (industryCode) {
            case "bank" -> data.getUserOrgCurrentBank();
            case "security" -> data.getUserOrgCurrentSecurities();
            case "insurance" -> data.getUserOrgCurrentInsurance();
            case "trust" -> data.getUserOrgCurrentTrust();
            case "p2p_lending" -> data.getUserOrgCurrentP2pLendingIntermediary();
            case "payment_institution" -> data.getUserOrgCurrentPaymentInstitution();
            case "financial_leasing_guarantee" -> data.getUserOrgCurrentFinancingLeasingGuarantee();
            case "micro_loan_company" -> data.getUserOrgCurrentMicroLoanCompany();
            case "consumer_finance_company" -> data.getUserOrgCurrentConsumerFinanceCompany();
            case "asset_management_company" -> data.getUserOrgCurrentAssetManagementCompany();
            case "auto_finance_company" -> data.getUserOrgCurrentAutoFinanceCompany();
            case "commercial_factoring_company" -> data.getUserOrgCurrentCommercialFactoringCompany();
            case "government" -> data.getUserOrgCurrentGovernment();
            case "public_utilities" -> data.getUserOrgCurrentPublicUtility();
            case "industry_association" -> data.getUserOrgCurrentIndustryAssociation();
            case "court" -> data.getUserOrgCurrentCourt();
            case "ecommerce_platform" -> data.getUserOrgCurrentEcommercePlatform();
            case "agricultural_enterprise" -> data.getUserOrgCurrentAgricultureRelatedEnterprise();
            case "other_credit_agency" -> data.getUserOrgCurrentOtherCreditReportingAgency();
            case "data_service_provider" -> data.getUserOrgCurrentDataServiceProvider();
            case "counterparty" -> data.getUserOrgCurrentTradingCounterparty();
            case "information_subject_itself" -> data.getUserOrgCurrentInformationSubjectSelf();
            case "other" -> data.getUserOrgCurrentOther();
            default -> null;
        }).orElse(BigDecimal.ZERO);
    }

    @Override
    public JmReportInfoUserTreeRespVO getInfoUserTree() {
        // 1. 行业字典（后续作为第二级节点）
        List<JimuDictItemDO> dictItems = jimuDictItemMapper.selectEnabledListByDictId(INDUSTRY_CODE_DICT_ID);
        if (CollUtil.isEmpty(dictItems)) {
            throw exception(GlobalErrorCodeConstants.NOT_FOUND, "产品与服务提供情况字典未配置或未启用");
        }

        // 2. 查询机构明细和政府明细（全表）
        List<ReportFillInfoUserOrgItemDO> orgItems = reportFillInfoUserOrgItemMapper.selectAll();
        List<ReportFillInfoUserGovItemDO> govItems = reportFillInfoUserGovItemMapper.selectAll();

        // 3. 按行业分组 orgItems，过滤掉没有行业编码的记录
        Map<String, List<ReportFillInfoUserOrgItemDO>> orgGroupMap = orgItems.stream()
                .filter(item -> item.getIndustryCode() != null && !item.getIndustryCode().trim().isEmpty())
                .collect(Collectors.groupingBy(ReportFillInfoUserOrgItemDO::getIndustryCode));

        List<JmReportInfoUserTreeNodeRespVO> nodes = new ArrayList<>();

        // 4. 组装行业节点（根节点）和机构名称节点（子节点）
        for (JimuDictItemDO dictItem : dictItems) {
            String industryCode = dictItem.getItemValue();
            String industryName = dictItem.getItemText();

            // “产品与服务调用情况”只是表头标题，不参与下拉树数据
            if ("product_service_usage".equals(industryCode)) {
                continue;
            }

            // 统计该行业下的机构数量，用于判断是否为叶子节点
            int orgCount = 0;
            if ("government".equals(industryCode)) {
                orgCount = (int) govItems.stream()
                        .filter(item -> item.getGovOrgName() != null && !item.getGovOrgName().trim().isEmpty())
                        .count();
            } else {
                List<ReportFillInfoUserOrgItemDO> list = orgGroupMap.get(industryCode);
                if (list != null) {
                    orgCount = (int) list.stream()
                            .filter(item -> item.getOrgName() != null && !item.getOrgName().trim().isEmpty())
                            .count();
                }
            }

            // 行业节点：作为根节点（parentId 为空字符串，必然还有子节点，所以 izLeaf 固定为 0）
            JmReportInfoUserTreeNodeRespVO industryNode = new JmReportInfoUserTreeNodeRespVO();
            industryNode.setId(industryCode);
            industryNode.setParentId("");
            industryNode.setDepartName(industryName);
            industryNode.setIzLeaf(0);
            nodes.add(industryNode);

            // 机构名称节点（parentId 为行业 code）
            if ("government".equals(industryCode)) {
                // 政府：全部 govItems，按 sortNo 排序
                govItems.stream()
                        .filter(item -> item.getGovOrgName() != null && !item.getGovOrgName().trim().isEmpty())
                        .sorted((a, b) -> {
                            String sa = a.getSortNo();
                            String sb = b.getSortNo();
                            if (sa == null && sb == null) return 0;
                            if (sa == null) return 1;
                            if (sb == null) return -1;
                            return sa.compareTo(sb);
                        })
                        .forEach(item -> {
                            JmReportInfoUserTreeNodeRespVO leaf = new JmReportInfoUserTreeNodeRespVO();
                            leaf.setId(item.getId());
                            leaf.setParentId(industryCode);
                            leaf.setDepartName(item.getGovOrgName());
                            leaf.setIzLeaf(1);
                            nodes.add(leaf);
                        });
            } else {
                List<ReportFillInfoUserOrgItemDO> list = orgGroupMap.get(industryCode);
                if (CollUtil.isNotEmpty(list)) {
                    list.stream()
                            .filter(item -> item.getOrgName() != null && !item.getOrgName().trim().isEmpty())
                            .sorted((a, b) -> {
                                String sa = a.getSortNo();
                                String sb = b.getSortNo();
                                if (sa == null && sb == null) return 0;
                                if (sa == null) return 1;
                                if (sb == null) return -1;
                                return sa.compareTo(sb);
                            })
                            .forEach(item -> {
                                JmReportInfoUserTreeNodeRespVO leaf = new JmReportInfoUserTreeNodeRespVO();
                                leaf.setId(item.getId());
                                leaf.setParentId(industryCode);
                                leaf.setDepartName(item.getOrgName());
                            leaf.setIzLeaf(1);
                                nodes.add(leaf);
                            });
                }
            }
        }

        JmReportInfoUserTreeRespVO respVO = new JmReportInfoUserTreeRespVO();
        respVO.setData(nodes);
        respVO.setTotal(null);
        return respVO;
    }

    @Override
    public JmReportInfoUserTreeRespVO getInfoUserTreeTest() {
        // 使用纯数字 ID / parentId 构造一棵简单的三层树：
        // 1  根：产品与服务调用情况
        // 11 二级：银行
        // 12 二级：政府
        // 111 三级：交通银行
        // 121 三级：合肥市金融局
        List<JmReportInfoUserTreeNodeRespVO> nodes = new ArrayList<>();

        // 根节点
//        JmReportInfoUserTreeNodeRespVO root = new JmReportInfoUserTreeNodeRespVO();
//        root.setId("1");
//        root.setParentId("");
//        root.setDepartName("产品与服务调用情况（测试数字ID）");
//        root.setIzLeaf(0);
//        nodes.add(root);

        // 二级：银行
        JmReportInfoUserTreeNodeRespVO bank = new JmReportInfoUserTreeNodeRespVO();
        bank.setId("11");
        bank.setParentId("");
        bank.setDepartName("银行");
        bank.setIzLeaf(0);
        nodes.add(bank);

        // 三级：交通银行
        JmReportInfoUserTreeNodeRespVO bankChild = new JmReportInfoUserTreeNodeRespVO();
        bankChild.setId("111");
        bankChild.setParentId("11");
        bankChild.setDepartName("交通银行（测试数字ID）");
        bankChild.setIzLeaf(1);
        nodes.add(bankChild);

        // 二级：政府
        JmReportInfoUserTreeNodeRespVO gov = new JmReportInfoUserTreeNodeRespVO();
        gov.setId("12");
        gov.setParentId("");
        gov.setDepartName("政府");
        gov.setIzLeaf(0);
        nodes.add(gov);



        // 三级：合肥市金融局
        JmReportInfoUserTreeNodeRespVO govChild = new JmReportInfoUserTreeNodeRespVO();
        govChild.setId("121");
        govChild.setParentId("12");
        govChild.setDepartName("合肥市金融局（测试数字ID）");
        govChild.setIzLeaf(1);
        nodes.add(govChild);

        JmReportInfoUserTreeRespVO respVO = new JmReportInfoUserTreeRespVO();
        respVO.setData(nodes);
        respVO.setTotal(null);
        return respVO;
    }

    @Override
    public JmReportProductServiceTotalRespVO getProductServiceTotal(JmReportProductServiceReqVO reqVO) {
        // 1. 解析当前周期 & 参与统计的记录ID（periodId + 3 个报表 + 3 个角色）
        String periodId = reqVO.getPeriodId();
        if (StringUtils.isBlank(periodId)) {
            throw exception(GlobalErrorCodeConstants.BAD_REQUEST, "填报周期不能为空");
        }
        List<String> recordIds = reportFillBasicInfoMapper.selectRecordIdsByPeriodAndReportsAndRoles(
                periodId,
                PRODUCT_REPORT_IDS,
                List.of(ROLE_ID_RD_CENTER, ROLE_ID_ENTERPRISE_CREDIT, ROLE_ID_INCLUSIVE_CREDIT)
        );

        List<ReportFillProductStatDO> list = reportFillProductStatMapper.selectListByRecordIds(recordIds);

        // 2. 直接对固定字段求和（不再通过 productType 过滤）
        BigDecimal reportSum = list.stream()
                .map(ReportFillProductStatDO::getReportYearCount)
                .filter(Objects::nonNull)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal creditSum = list.stream()
                .map(ReportFillProductStatDO::getCreditYearCount)
                .filter(Objects::nonNull)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal antiSum = list.stream()
                .map(ReportFillProductStatDO::getAntiYearCount)
                .filter(Objects::nonNull)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        // 3. 计算长三角征信链补充次数（仅数据管理中心 + 特定报表）
        BigDecimal yangtzeExtra = BigDecimal.ZERO;
        String reportId = reqVO.getReportId();
        if (StringUtils.isNotBlank(reportId)) {
            yangtzeExtra = reportFillYangtzeCreditChainMapper
                    .selectTotalCountByPeriodAndReportAndRole(periodId, reportId, ROLE_ID_DATA_CENTER);
            if (yangtzeExtra == null) {
                yangtzeExtra = BigDecimal.ZERO;
            }
        }

        // 4. 按规则：汇总求和后 + 3000 × periodId 中的月数
        int month = parseMonthFromPeriodId(periodId);
        BigDecimal monthDelta = BigDecimal.valueOf(3000L).multiply(BigDecimal.valueOf(month));

        JmReportProductServiceTotalRespVO respVO = new JmReportProductServiceTotalRespVO();
        // “当年信用报告产品提供次数” = 原统计结果 + 长三角征信链补充次数
        respVO.setReportYearTotal(reportSum.add(monthDelta).add(yangtzeExtra));
        respVO.setCreditYearTotal(creditSum.add(monthDelta));
        respVO.setAntiYearTotal(antiSum.add(monthDelta));
        return respVO;
    }

    @Override
    public JmReportServiceByIndustryTotalRespVO getServiceByIndustryTotal(JmReportServiceByIndustryReqVO reqVO) {
        // 复用 getServiceByIndustry 的逻辑，只返回总计
        JmReportServiceByIndustryRespVO detail = getServiceByIndustry(reqVO);
        JmReportServiceByIndustryTotalRespVO respVO = new JmReportServiceByIndustryTotalRespVO();
        respVO.setPeriodId(detail.getPeriodId());
        respVO.setReportId(detail.getReportId());
        respVO.setTotalUserOrgTotal(detail.getTotalUserOrgTotal());
        respVO.setTotalUserOrgCurrent(detail.getTotalUserOrgCurrent());
        respVO.setTotalYearServiceCount(detail.getTotalYearServiceCount());
        return respVO;
    }

    @Override
    public JmReportProductServiceRespVO getProductService(JmReportProductServiceReqVO reqVO) {
        // 1. 获取产品类别字典（按 sort_order 排序）
        List<JimuDictItemDO> productTypes = jimuDictItemMapper.selectEnabledListByDictId(CREDIT_PRODUCT_TYPE_DICT_ID);
        if (CollUtil.isEmpty(productTypes)) {
            throw exception(GlobalErrorCodeConstants.NOT_FOUND, "征信产品类别字典未配置或未启用");
        }

        // 2. 获取产品名称字典（按 sort_order 排序）
        List<JimuDictItemDO> productNames = jimuDictItemMapper.selectEnabledListByDictId(CREDIT_PRO_DICT_ID);
        if (CollUtil.isEmpty(productNames)) {
            throw exception(GlobalErrorCodeConstants.NOT_FOUND, "提供的信用产品字典未配置或未启用");
        }

        // 3. 确定要聚合的记录ID列表（只统计创新研发中心+企信部+普惠部的数据）
        List<String> productRecordIds = resolveRecordIdsByRolesForProduct(reqVO,
                List.of(ROLE_ID_RD_CENTER, ROLE_ID_ENTERPRISE_CREDIT, ROLE_ID_INCLUSIVE_CREDIT));

        // 4. 查询所有记录并聚合固定字段（表结构已改为固定字段：report_year_count, credit_year_count, anti_year_count）
        List<ReportFillProductStatDO> productStatList = reportFillProductStatMapper.selectListByRecordIds(productRecordIds);

        // 按固定字段聚合（三个字段分别对应字典的前三项）
        BigDecimal reportYearTotal = productStatList.stream()
                .map(ReportFillProductStatDO::getReportYearCount)
                .filter(Objects::nonNull)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal creditYearTotal = productStatList.stream()
                .map(ReportFillProductStatDO::getCreditYearCount)
                .filter(Objects::nonNull)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal antiYearTotal = productStatList.stream()
                .map(ReportFillProductStatDO::getAntiYearCount)
                .filter(Objects::nonNull)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        // 5. 统计总计次数（从 service_by_industry，数据管理部+普惠部+企信部）
        List<String> serviceRecordIds = resolveRecordIdsByRolesForProduct(reqVO,
                List.of(ROLE_ID_DATA_CENTER, ROLE_ID_INCLUSIVE_CREDIT, ROLE_ID_ENTERPRISE_CREDIT));
        BigDecimal totalYearServiceCount = reportFillServiceByIndustryMapper.selectTotalYearServiceCount(serviceRecordIds);

        // 6. 组装明细（按字典顺序一一对应，固定字段对应前三个字典项）
        BigDecimal totalProductCount = BigDecimal.ZERO;
        List<JmReportProductServiceItemRespVO> items = new ArrayList<>();
        int maxSize = Math.max(productTypes.size(), productNames.size());

        for (int i = 0; i < maxSize; i++) {
            JimuDictItemDO productType = i < productTypes.size() ? productTypes.get(i) : null;
            JimuDictItemDO productName = i < productNames.size() ? productNames.get(i) : null;

            if (productType == null || productName == null) {
                continue; // 跳过不匹配的项
            }

            // 固定字段按顺序对应：第0项=report_year_count, 第1项=credit_year_count, 第2项=anti_year_count
            BigDecimal yearCount = BigDecimal.ZERO;
            if (i == 0) {
                yearCount = reportYearTotal;
            } else if (i == 1) {
                yearCount = creditYearTotal;
            } else if (i == 2) {
                yearCount = antiYearTotal;
            }
            // i >= 3 的情况，yearCount 保持为 0（因为表结构只有三个固定字段）

            totalProductCount = totalProductCount.add(yearCount);

            JmReportProductServiceItemRespVO itemVO = new JmReportProductServiceItemRespVO();
            itemVO.setProductTypeCode(productType.getItemValue());
            itemVO.setProductTypeName(productType.getItemText());
            itemVO.setProductNameCode(productName.getItemValue());
            itemVO.setProductName(productName.getItemText());
            itemVO.setYearCount(yearCount);
            items.add(itemVO);
        }

        // 7. 计算其他征信服务产品次数
        BigDecimal otherServiceCount = totalYearServiceCount.subtract(totalProductCount);

        // 8. 添加"其他征信服务产品名称"行
        JmReportProductServiceItemRespVO otherItem = new JmReportProductServiceItemRespVO();
        otherItem.setProductTypeCode("other");
        otherItem.setProductTypeName("其他征信服务产品名称");
        otherItem.setProductNameCode("");
        otherItem.setProductName("其他征信服务产品名称");
        otherItem.setYearCount(otherServiceCount);
        items.add(otherItem);

        // 9. 组装响应
        JmReportProductServiceRespVO respVO = new JmReportProductServiceRespVO();
        respVO.setPeriodId(reqVO.getPeriodId());
        respVO.setReportId(reqVO.getReportId());
        respVO.setTotalYearServiceCount(totalYearServiceCount);
        respVO.setTotalProductCount(totalProductCount);
        respVO.setOtherServiceCount(otherServiceCount);
        respVO.setItems(items);
        return respVO;
    }

    @Override
    public java.util.List<JmDictItemSimpleRespVO> getIndustryCodeDictItems() {
        List<JimuDictItemDO> dictItems = jimuDictItemMapper.selectEnabledListByDictId(INDUSTRY_CODE_DICT_ID);
        if (CollUtil.isEmpty(dictItems)) {
            throw exception(GlobalErrorCodeConstants.NOT_FOUND, "产品与服务提供情况字典未配置或未启用");
        }
        return CollectionUtils.convertList(dictItems, item -> {
            JmDictItemSimpleRespVO vo = new JmDictItemSimpleRespVO();
            vo.setItemText(item.getItemText());
            vo.setItemValue(item.getItemValue());
            vo.setSortOrder(item.getSortOrder());
            return vo;
        });
    }

    /**
     * 解析记录ID列表（用于聚合）
     */
    private List<String> resolveRecordIds(JmReportServiceByIndustryReqVO reqVO) {
        if (StringUtils.isNotBlank(reqVO.getRecordId())) {
            // 如果指定了 recordId，只查询该记录
            ReportFillBasicInfoDO record = reportFillBasicInfoMapper.selectById(reqVO.getRecordId());
            if (record == null) {
                throw exception(GlobalErrorCodeConstants.NOT_FOUND, "指定的填报记录不存在");
            }
            if (!Objects.equals(reqVO.getReportId(), record.getReportId())) {
                throw exception(GlobalErrorCodeConstants.BAD_REQUEST, "填报记录不属于该报表模板");
            }
            if (!Objects.equals(reqVO.getPeriodId(), record.getPeriodId())) {
                throw exception(GlobalErrorCodeConstants.BAD_REQUEST, "填报记录的周期与请求参数不一致");
            }
            return List.of(reqVO.getRecordId());
        }
        // 否则聚合该周期+报表下所有角色的数据
        return reportFillBasicInfoMapper.selectRecordIdsByPeriodAndReport(reqVO.getPeriodId(), reqVO.getReportId());
    }

    /**
     * 解析记录ID列表（用于产品服务聚合，按指定角色过滤）
     */
    private List<String> resolveRecordIdsByRolesForProduct(JmReportProductServiceReqVO reqVO, List<String> roleIds) {
        if (StringUtils.isNotBlank(reqVO.getRecordId())) {
            ReportFillBasicInfoDO record = reportFillBasicInfoMapper.selectById(reqVO.getRecordId());
            if (record == null) {
                throw exception(GlobalErrorCodeConstants.NOT_FOUND, "指定的填报记录不存在");
            }
            if (!Objects.equals(reqVO.getReportId(), record.getReportId())) {
                throw exception(GlobalErrorCodeConstants.BAD_REQUEST, "填报记录不属于该报表模板");
            }
            if (!Objects.equals(reqVO.getPeriodId(), record.getPeriodId())) {
                throw exception(GlobalErrorCodeConstants.BAD_REQUEST, "填报记录的周期与请求参数不一致");
            }
            // 如果指定了 recordId，检查角色是否匹配
            if (roleIds != null && !roleIds.contains(record.getRoleId())) {
                return List.of(); // 角色不匹配，返回空列表
            }
            return List.of(reqVO.getRecordId());
        }
        return reportFillBasicInfoMapper.selectRecordIdsByPeriodAndReportAndRoles(reqVO.getPeriodId(), reqVO.getReportId(), roleIds);
    }

    /**
     * 解析记录ID列表（按指定角色过滤）
     */
    private List<String> resolveRecordIdsByRoles(JmReportServiceByIndustryReqVO reqVO, List<String> roleIds) {
        if (StringUtils.isNotBlank(reqVO.getRecordId())) {
            ReportFillBasicInfoDO record = reportFillBasicInfoMapper.selectById(reqVO.getRecordId());
            if (record == null) {
                throw exception(GlobalErrorCodeConstants.NOT_FOUND, "指定的填报记录不存在");
            }
            if (!Objects.equals(reqVO.getReportId(), record.getReportId())) {
                throw exception(GlobalErrorCodeConstants.BAD_REQUEST, "填报记录不属于该报表模板");
            }
            if (!Objects.equals(reqVO.getPeriodId(), record.getPeriodId())) {
                throw exception(GlobalErrorCodeConstants.BAD_REQUEST, "填报记录的周期与请求参数不一致");
            }
            // 如果指定了 recordId，检查角色是否匹配
            if (roleIds != null && !roleIds.contains(record.getRoleId())) {
                return List.of(); // 角色不匹配，返回空列表
            }
            return List.of(reqVO.getRecordId());
        }
        return reportFillBasicInfoMapper.selectRecordIdsByPeriodAndReportAndRoles(reqVO.getPeriodId(), reqVO.getReportId(), roleIds);
    }

    /**
     * 从 periodId 中解析月份数字
     * 支持格式：YYYY-MM 或 YYYY-MM-DD
     *
     * @param periodId 填报周期，如 "2026-01" 或 "2026-01-15"
     * @return 月份数字（1-12）
     * @throws ServiceException 如果格式不正确
     */
    private int parseMonthFromPeriodId(String periodId) {
        if (StringUtils.isBlank(periodId)) {
            throw exception(GlobalErrorCodeConstants.BAD_REQUEST, "填报周期不能为空");
        }

        try {
            // 尝试解析 YYYY-MM 或 YYYY-MM-DD 格式
            String[] parts = periodId.split("-");
            if (parts.length < 2) {
                throw exception(GlobalErrorCodeConstants.BAD_REQUEST, "填报周期格式不正确，应为 YYYY-MM 或 YYYY-MM-DD");
            }

            int month = Integer.parseInt(parts[1]);
            if (month < 1 || month > 12) {
                throw exception(GlobalErrorCodeConstants.BAD_REQUEST, "填报周期中的月份必须在 1-12 之间");
            }

            return month;
        } catch (NumberFormatException e) {
            throw exception(GlobalErrorCodeConstants.BAD_REQUEST, "填报周期格式不正确，无法解析月份");
        }
    }

    /**
     * 用户机构统计内部类
     */
    private static class UserOrgStat {
        private final BigDecimal userOrgTotal;
        private final BigDecimal userOrgCurrent;

        public UserOrgStat(BigDecimal userOrgTotal, BigDecimal userOrgCurrent) {
            this.userOrgTotal = userOrgTotal;
            this.userOrgCurrent = userOrgCurrent;
        }

        public BigDecimal getUserOrgTotal() {
            return userOrgTotal;
        }

        public BigDecimal getUserOrgCurrent() {
            return userOrgCurrent;
        }
    }
}
