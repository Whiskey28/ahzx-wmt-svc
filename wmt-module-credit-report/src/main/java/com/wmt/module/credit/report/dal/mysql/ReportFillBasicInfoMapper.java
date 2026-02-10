package com.wmt.module.credit.report.dal.mysql;

import cn.hutool.core.collection.CollUtil;
import com.wmt.framework.common.pojo.PageResult;
import com.wmt.framework.common.pojo.SortablePageParam;
import com.wmt.framework.common.pojo.SortingField;
import com.wmt.framework.mybatis.core.mapper.BaseMapperX;
import com.wmt.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.wmt.module.credit.report.controller.admin.report.vo.ReportFillRecordPageReqVO;
import com.wmt.module.credit.report.dal.dataobject.ReportFillBasicInfoDO;
import org.apache.ibatis.annotations.Mapper;

import java.util.Collection;
import java.util.List;

/**
 * 报表填报基础信息 Mapper
 *
 * @author AHC源码
 */
@Mapper
public interface ReportFillBasicInfoMapper extends BaseMapperX<ReportFillBasicInfoDO> {

    /**
     * 分页查询填报记录
     *
     * @param pageParam 分页参数
     * @param reqVO 查询条件
     * @param reportIds 报表ID列表（可选，用于支持分类/名称查询）
     * @return 分页结果
     */
    default PageResult<ReportFillBasicInfoDO> selectPage(SortablePageParam pageParam, ReportFillRecordPageReqVO reqVO,
                                                        Collection<String> reportIds) {
        LambdaQueryWrapperX<ReportFillBasicInfoDO> wrapper = new LambdaQueryWrapperX<ReportFillBasicInfoDO>()
                .eqIfPresent(ReportFillBasicInfoDO::getPeriodId, reqVO.getPeriodId())
                .eqIfPresent(ReportFillBasicInfoDO::getRoleId, reqVO.getRoleId())
                .inIfPresent(ReportFillBasicInfoDO::getReportId, reportIds);
        return selectPage(pageParam, wrapper);
    }

    /**
     * 查询指定周期、报表的最新填报记录（按创建时间倒序取第一条）
     *
     * @param periodId 填报周期
     * @param reportId 报表模板ID
     * @return 最新的填报记录，若不存在则返回 null
     */
    default ReportFillBasicInfoDO selectLatestByPeriodAndReport(String periodId, String reportId) {
        SortablePageParam pageParam = new SortablePageParam();
        pageParam.setPageNo(1);
        pageParam.setPageSize(1);
        pageParam.setSortingFields(List.of(new SortingField("create_time", SortingField.ORDER_DESC)));

        LambdaQueryWrapperX<ReportFillBasicInfoDO> wrapper = new LambdaQueryWrapperX<ReportFillBasicInfoDO>()
                .eqIfPresent(ReportFillBasicInfoDO::getPeriodId, periodId)
                .eqIfPresent(ReportFillBasicInfoDO::getReportId, reportId);

        PageResult<ReportFillBasicInfoDO> pageResult = selectPage(pageParam, pageParam.getSortingFields(), wrapper);
        return CollUtil.getFirst(pageResult.getList());
    }

    /**
     * 查询指定周期和报表的所有记录ID（用于聚合）
     *
     * @param periodId 填报周期
     * @param reportId 报表模板ID
     * @return 记录ID列表
     */
    default List<String> selectRecordIdsByPeriodAndReport(String periodId, String reportId) {
        return selectList(new LambdaQueryWrapperX<ReportFillBasicInfoDO>()
                .eq(ReportFillBasicInfoDO::getPeriodId, periodId)
                .eq(ReportFillBasicInfoDO::getReportId, reportId))
                .stream()
                .map(ReportFillBasicInfoDO::getId)
                .collect(java.util.stream.Collectors.toList());
    }

    /**
     * 查询指定周期、多个报表和多个角色的记录ID（用于聚合）
     *
     * @param periodId  填报周期
     * @param reportIds 报表模板ID列表
     * @param roleIds   角色ID列表
     * @return 记录ID列表
     */
    default List<String> selectRecordIdsByPeriodAndReportsAndRoles(String periodId,
                                                                   Collection<String> reportIds,
                                                                   Collection<String> roleIds) {
        if (CollUtil.isEmpty(reportIds) || CollUtil.isEmpty(roleIds)) {
            return List.of();
        }
        return selectList(new LambdaQueryWrapperX<ReportFillBasicInfoDO>()
                .eq(ReportFillBasicInfoDO::getPeriodId, periodId)
                .in(ReportFillBasicInfoDO::getReportId, reportIds)
                .in(ReportFillBasicInfoDO::getRoleId, roleIds))
                .stream()
                .map(ReportFillBasicInfoDO::getId)
                .collect(java.util.stream.Collectors.toList());
    }

    /**
     * 查询指定周期、报表和角色的记录ID（用于聚合）
     *
     * @param periodId 填报周期
     * @param reportId 报表模板ID
     * @param roleIds 角色ID列表
     * @return 记录ID列表
     */
    default List<String> selectRecordIdsByPeriodAndReportAndRoles(String periodId, String reportId, Collection<String> roleIds) {
        if (CollUtil.isEmpty(roleIds)) {
            return List.of();
        }
        return selectList(new LambdaQueryWrapperX<ReportFillBasicInfoDO>()
                .eq(ReportFillBasicInfoDO::getPeriodId, periodId)
                .eq(ReportFillBasicInfoDO::getReportId, reportId)
                .in(ReportFillBasicInfoDO::getRoleId, roleIds))
                .stream()
                .map(ReportFillBasicInfoDO::getId)
                .collect(java.util.stream.Collectors.toList());
    }

}
