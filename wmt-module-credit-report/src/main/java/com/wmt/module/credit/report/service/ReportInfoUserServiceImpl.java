package com.wmt.module.credit.report.service;

import cn.hutool.core.collection.CollUtil;
import com.wmt.framework.common.exception.enums.GlobalErrorCodeConstants;
import com.wmt.framework.common.exception.util.ServiceExceptionUtil;
import com.wmt.framework.common.pojo.PageResult;
import com.wmt.framework.common.util.collection.CollectionUtils;
import com.wmt.framework.common.util.object.BeanUtils;
import com.wmt.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.wmt.module.credit.report.controller.admin.report.vo.*;
import com.wmt.module.credit.report.dal.dataobject.JimuDictItemDO;
import com.wmt.module.credit.report.dal.dataobject.ReportFillInfoUserGovItemDO;
import com.wmt.module.credit.report.dal.dataobject.ReportFillInfoUserOrgItemDO;
import com.wmt.module.credit.report.dal.mysql.JimuDictItemMapper;
import com.wmt.module.credit.report.dal.mysql.ReportFillInfoUserGovItemMapper;
import com.wmt.module.credit.report.dal.mysql.ReportFillInfoUserOrgItemMapper;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.wmt.framework.common.exception.util.ServiceExceptionUtil.exception;

/**
 * 信息使用者机构相关 Service 实现
 *
 * @author Auto
 */
@Service
@Slf4j
public class ReportInfoUserServiceImpl implements ReportInfoUserService {

    /**
     * 产品与服务提供情况字典 ID（industry_code）
     */
    private static final String INDUSTRY_CODE_DICT_ID = "1178553094177271808";

    @Resource
    private ReportFillInfoUserOrgItemMapper orgItemMapper;

    @Resource
    private ReportFillInfoUserGovItemMapper govItemMapper;

    @Resource
    private JimuDictItemMapper jimuDictItemMapper;

    // ========== 机构明细 ==========

    @Override
    public PageResult<ReportInfoUserOrgRespVO> getOrgPage(ReportInfoUserOrgPageReqVO pageReqVO) {
        // 使用 JOIN 查询，按行业字典 sort_order 和 sort_no 排序
        PageResult<ReportFillInfoUserOrgItemDO> pageResult =
                orgItemMapper.selectPageWithIndustryOrder(pageReqVO);
        return CollectionUtils.convertPage(pageResult,
                item -> BeanUtils.toBean(item, ReportInfoUserOrgRespVO.class));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String createOrg(ReportInfoUserOrgSaveReqVO reqVO) {
        ReportFillInfoUserOrgItemDO entity = BeanUtils.toBean(reqVO, ReportFillInfoUserOrgItemDO.class);
        orgItemMapper.insert(entity);
        return entity.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean updateOrg(ReportInfoUserOrgUpdateReqVO reqVO) {
        ReportFillInfoUserOrgItemDO exist = orgItemMapper.selectById(reqVO.getId());
        if (exist == null) {
            throw exception(GlobalErrorCodeConstants.NOT_FOUND, "机构明细记录不存在");
        }
        ReportFillInfoUserOrgItemDO update = BeanUtils.toBean(reqVO, ReportFillInfoUserOrgItemDO.class);
        return orgItemMapper.updateById(update) > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean deleteOrg(String id) {
        if (orgItemMapper.selectById(id) == null) {
            throw exception(GlobalErrorCodeConstants.NOT_FOUND, "机构明细记录不存在");
        }
        return orgItemMapper.deleteById(id) > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean deleteOrgBatch(List<String> ids) {
        if (CollUtil.isEmpty(ids)) {
            return Boolean.TRUE;
        }
        orgItemMapper.deleteByIds(ids);
        return Boolean.TRUE;
    }

    // ========== 政府机构明细 ==========

    @Override
    public PageResult<ReportInfoUserGovRespVO> getGovPage(ReportInfoUserGovPageReqVO pageReqVO) {
        LambdaQueryWrapperX<ReportFillInfoUserGovItemDO> wrapper = new LambdaQueryWrapperX<ReportFillInfoUserGovItemDO>()
                .likeIfPresent(ReportFillInfoUserGovItemDO::getGovOrgName, pageReqVO.getGovOrgName())
                .eqIfPresent(ReportFillInfoUserGovItemDO::getIsCurrentService, pageReqVO.getIsCurrentService());
        wrapper.orderByAsc(ReportFillInfoUserGovItemDO::getSortNo);

        PageResult<ReportFillInfoUserGovItemDO> pageResult =
                govItemMapper.selectPage(pageReqVO, wrapper);
        return CollectionUtils.convertPage(pageResult,
                item -> BeanUtils.toBean(item, ReportInfoUserGovRespVO.class));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String createGov(ReportInfoUserGovSaveReqVO reqVO) {
        ReportFillInfoUserGovItemDO entity = BeanUtils.toBean(reqVO, ReportFillInfoUserGovItemDO.class);
        govItemMapper.insert(entity);
        return entity.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean updateGov(ReportInfoUserGovUpdateReqVO reqVO) {
        ReportFillInfoUserGovItemDO exist = govItemMapper.selectById(reqVO.getId());
        if (exist == null) {
            throw exception(GlobalErrorCodeConstants.NOT_FOUND, "政府机构明细记录不存在");
        }
        ReportFillInfoUserGovItemDO update = BeanUtils.toBean(reqVO, ReportFillInfoUserGovItemDO.class);
        return govItemMapper.updateById(update) > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean deleteGov(String id) {
        if (govItemMapper.selectById(id) == null) {
            throw exception(GlobalErrorCodeConstants.NOT_FOUND, "政府机构明细记录不存在");
        }
        return govItemMapper.deleteById(id) > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean deleteGovBatch(List<String> ids) {
        if (CollUtil.isEmpty(ids)) {
            return Boolean.TRUE;
        }
        govItemMapper.deleteByIds(ids);
        return Boolean.TRUE;
    }

    // ========== 按行业统计 ==========

    @Override
    public ReportInfoUserStatByIndustryRespVO getStatByIndustry() {
        // 1. 读取行业字典，保证顺序一致
        List<JimuDictItemDO> dictItems = jimuDictItemMapper.selectEnabledListByDictId(INDUSTRY_CODE_DICT_ID);
        if (CollUtil.isEmpty(dictItems)) {
            throw ServiceExceptionUtil.exception(GlobalErrorCodeConstants.NOT_FOUND, "产品与服务提供情况字典未配置或未启用");
        }

        // 2. 查询机构明细和政府明细（全表，不再区分 recordId）
        List<ReportFillInfoUserOrgItemDO> orgItems = orgItemMapper.selectAll();
        List<ReportFillInfoUserGovItemDO> govItems = govItemMapper.selectAll();

        // 3. 先对 orgItems 按行业分组，再做去重统计（过滤掉没有行业编码的脏数据，避免 NPE）
        Map<String, List<ReportFillInfoUserOrgItemDO>> orgGroupMap = orgItems.stream()
                .filter(item -> item.getIndustryCode() != null && !item.getIndustryCode().trim().isEmpty())
                .collect(Collectors.groupingBy(ReportFillInfoUserOrgItemDO::getIndustryCode));

        List<ReportInfoUserStatByIndustryItemRespVO> items = new ArrayList<>(dictItems.size());
        BigDecimal totalUserOrgTotal = BigDecimal.ZERO;
        BigDecimal totalUserOrgCurrent = BigDecimal.ZERO;

        for (JimuDictItemDO dictItem : dictItems) {
            String industryCode = dictItem.getItemValue();
            ReportInfoUserStatByIndustryItemRespVO itemVO = new ReportInfoUserStatByIndustryItemRespVO();
            itemVO.setIndustryCode(industryCode);
            itemVO.setIndustryName(dictItem.getItemText());

            BigDecimal userOrgTotal;
            BigDecimal userOrgCurrent;

            if ("government".equals(industryCode)) {
                // 政府：只统计 govItems
                long total = govItems.stream()
                        .map(ReportFillInfoUserGovItemDO::getGovOrgName)
                        .filter(name -> name != null && !name.trim().isEmpty())
                        .distinct()
                        .count();
                long current = govItems.stream()
                        .filter(item -> item.getIsCurrentService() != null && item.getIsCurrentService() == 1)
                        .map(ReportFillInfoUserGovItemDO::getGovOrgName)
                        .filter(name -> name != null && !name.trim().isEmpty())
                        .distinct()
                        .count();
                userOrgTotal = BigDecimal.valueOf(total);
                userOrgCurrent = BigDecimal.valueOf(current);
            } else {
                // 其他行业：只统计 orgItems 中对应 industryCode
                List<ReportFillInfoUserOrgItemDO> list = orgGroupMap.getOrDefault(industryCode, List.of());
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
                userOrgTotal = BigDecimal.valueOf(total);
                userOrgCurrent = BigDecimal.valueOf(current);
            }

            itemVO.setUserOrgTotal(userOrgTotal);
            itemVO.setUserOrgCurrent(userOrgCurrent);

            totalUserOrgTotal = totalUserOrgTotal.add(userOrgTotal);
            totalUserOrgCurrent = totalUserOrgCurrent.add(userOrgCurrent);

            items.add(itemVO);
        }

        ReportInfoUserStatByIndustryRespVO respVO = new ReportInfoUserStatByIndustryRespVO();
        respVO.setItems(items);
        respVO.setTotalUserOrgTotal(totalUserOrgTotal);
        respVO.setTotalUserOrgCurrent(totalUserOrgCurrent);
        return respVO;
    }
}

