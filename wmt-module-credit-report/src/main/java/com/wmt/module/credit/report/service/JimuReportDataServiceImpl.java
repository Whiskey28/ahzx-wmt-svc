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
import com.wmt.module.credit.report.dal.dataobject.ReportFillInfoSourceByIndustryDO;
import com.wmt.module.credit.report.dal.dataobject.ReportFillInfoUserOrgItemDO;
import com.wmt.module.credit.report.dal.dataobject.ReportFillInfoUserGovItemDO;
import com.wmt.module.credit.report.dal.dataobject.ReportFillProductStatDO;
import com.wmt.module.credit.report.dal.mysql.JimuDictItemMapper;
import com.wmt.module.credit.report.dal.mysql.ReportFillBasicInfoMapper;
import com.wmt.module.credit.report.dal.mysql.ReportFillInfoSourceByIndustryMapper;
import com.wmt.module.credit.report.dal.mysql.ReportFillInfoUserOrgItemMapper;
import com.wmt.module.credit.report.dal.mysql.ReportFillInfoUserGovItemMapper;
import com.wmt.module.credit.report.dal.mysql.ReportFillServiceByIndustryMapper;
import com.wmt.module.credit.report.dal.mysql.ReportFillProductStatMapper;
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

    @Resource
    private JimuDictItemMapper jimuDictItemMapper;

    @Resource
    private ReportFillBasicInfoMapper reportFillBasicInfoMapper;

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
        List<String> serviceRecordIds = reportFillBasicInfoMapper.selectRecordIdsByPeriodAndReportsAndRoles(
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
    public JmReportInfoUserTreeRespVO getInfoUserTree() {
        // 1. 根节点：产品与服务提供情况
        final String rootId = "root_product_service_info_user";
        JmReportInfoUserTreeNodeRespVO rootNode = new JmReportInfoUserTreeNodeRespVO();
        rootNode.setId(rootId);
        rootNode.setParentId("");
        rootNode.setDepartName("产品与服务提供情况");
        rootNode.setIsLeaf(0);

        List<JmReportInfoUserTreeNodeRespVO> nodes = new ArrayList<>();
        nodes.add(rootNode);

        // 2. 行业字典（第二级节点）
        List<JimuDictItemDO> dictItems = jimuDictItemMapper.selectEnabledListByDictId(INDUSTRY_CODE_DICT_ID);
        if (CollUtil.isEmpty(dictItems)) {
            throw exception(GlobalErrorCodeConstants.NOT_FOUND, "产品与服务提供情况字典未配置或未启用");
        }

        // 3. 查询机构明细和政府明细（全表）
        List<ReportFillInfoUserOrgItemDO> orgItems = reportFillInfoUserOrgItemMapper.selectAll();
        List<ReportFillInfoUserGovItemDO> govItems = reportFillInfoUserGovItemMapper.selectAll();

        // 4. 按行业分组 orgItems，过滤掉没有行业编码的记录
        Map<String, List<ReportFillInfoUserOrgItemDO>> orgGroupMap = orgItems.stream()
                .filter(item -> item.getIndustryCode() != null && !item.getIndustryCode().trim().isEmpty())
                .collect(Collectors.groupingBy(ReportFillInfoUserOrgItemDO::getIndustryCode));

        // 5. 组装行业节点和机构名称节点
        for (JimuDictItemDO dictItem : dictItems) {
            String industryCode = dictItem.getItemValue();
            String industryName = dictItem.getItemText();

            // 二级行业节点
            JmReportInfoUserTreeNodeRespVO industryNode = new JmReportInfoUserTreeNodeRespVO();
            industryNode.setId(industryCode);
            industryNode.setParentId(rootId);
            industryNode.setDepartName(industryName);
            industryNode.setIsLeaf(0);
            nodes.add(industryNode);

            // 三级机构名称节点
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
                            leaf.setIsLeaf(1);
                            nodes.add(leaf);
                        });
            } else {
                List<ReportFillInfoUserOrgItemDO> list = orgGroupMap.get(industryCode);
                if (CollUtil.isEmpty(list)) {
                    continue;
                }
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
                            leaf.setIsLeaf(1);
                            nodes.add(leaf);
                        });
            }
        }

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

        // 3. 按规则：汇总求和后 + 3000 × periodId 中的月数
        int month = parseMonthFromPeriodId(periodId);
        BigDecimal monthDelta = BigDecimal.valueOf(3000L).multiply(BigDecimal.valueOf(month));

        JmReportProductServiceTotalRespVO respVO = new JmReportProductServiceTotalRespVO();
        respVO.setReportYearTotal(reportSum.add(monthDelta));
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
