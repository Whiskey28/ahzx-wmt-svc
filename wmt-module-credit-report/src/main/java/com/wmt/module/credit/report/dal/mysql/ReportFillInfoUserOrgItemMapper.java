package com.wmt.module.credit.report.dal.mysql;

import com.github.yulichang.wrapper.MPJLambdaWrapper;
import com.wmt.framework.common.pojo.PageResult;
import com.wmt.framework.mybatis.core.mapper.BaseMapperX;
import com.wmt.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.wmt.framework.mybatis.core.query.MPJLambdaWrapperX;
import com.wmt.module.credit.report.controller.admin.report.vo.ReportInfoUserOrgPageReqVO;
import com.wmt.module.credit.report.dal.dataobject.JimuDictItemDO;
import com.wmt.module.credit.report.dal.dataobject.ReportFillInfoUserOrgItemDO;
import org.apache.ibatis.annotations.Mapper;

import java.util.Collection;
import java.util.List;

/**
 * 报表填报 - 信息使用者机构明细 Mapper
 *
 * @author Auto
 */
@Mapper
public interface ReportFillInfoUserOrgItemMapper extends BaseMapperX<ReportFillInfoUserOrgItemDO> {

    /**
     * 产品与服务提供情况字典 ID（industry_code）
     */
    String INDUSTRY_CODE_DICT_ID = "1178553094177271808";

    default List<ReportFillInfoUserOrgItemDO> selectAll() {
        return selectList(new LambdaQueryWrapperX<ReportFillInfoUserOrgItemDO>()
                .orderByAsc(ReportFillInfoUserOrgItemDO::getIndustryCode));
    }

    /**
     * 分页查询机构明细（按行业字典 sort_order 和 sort_no 排序）
     *
     * @param pageReqVO 分页查询条件
     * @return 分页结果
     */
    default PageResult<ReportFillInfoUserOrgItemDO> selectPageWithIndustryOrder(ReportInfoUserOrgPageReqVO pageReqVO) {
        MPJLambdaWrapper<ReportFillInfoUserOrgItemDO> wrapper = new MPJLambdaWrapperX<ReportFillInfoUserOrgItemDO>()
                .selectAll(ReportFillInfoUserOrgItemDO.class)
                .likeIfPresent(ReportFillInfoUserOrgItemDO::getOrgName, pageReqVO.getOrgName())
                .eqIfPresent(ReportFillInfoUserOrgItemDO::getIndustryCode, pageReqVO.getIndustryCode())
                .eqIfPresent(ReportFillInfoUserOrgItemDO::getIsCurrentService, pageReqVO.getIsCurrentService())
                .leftJoin(JimuDictItemDO.class, JimuDictItemDO::getItemValue, ReportFillInfoUserOrgItemDO::getIndustryCode)
                .eq(JimuDictItemDO::getDictId, INDUSTRY_CODE_DICT_ID)
                .eq(JimuDictItemDO::getStatus, 1) // 只关联启用的字典项
                // 按字典 sort_order 升序，然后按 sort_no 升序
                .orderByAsc(JimuDictItemDO::getSortOrder)
                .orderByAsc(ReportFillInfoUserOrgItemDO::getSortNo);

        return selectJoinPage(pageReqVO, ReportFillInfoUserOrgItemDO.class, wrapper);
    }
}
