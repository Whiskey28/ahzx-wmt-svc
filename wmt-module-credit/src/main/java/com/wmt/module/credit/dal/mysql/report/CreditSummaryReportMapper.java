package com.wmt.module.credit.dal.mysql.report;

import com.wmt.framework.common.pojo.PageResult;
import com.wmt.framework.mybatis.core.mapper.BaseMapperX;
import com.wmt.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.wmt.module.credit.controller.admin.report.vo.CreditSummaryReportPageReqVO;
import com.wmt.module.credit.dal.dataobject.report.CreditSummaryReportDO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 征信汇总报表 Mapper
 *
 * @author AHC源码
 */
@Mapper
public interface CreditSummaryReportMapper extends BaseMapperX<CreditSummaryReportDO> {

    /**
     * 分页查询汇总报表
     *
     * @param pageReqVO 分页查询条件
     * @return 分页结果
     */
    default PageResult<CreditSummaryReportDO> selectPage(CreditSummaryReportPageReqVO pageReqVO) {
        return selectPage(pageReqVO, new LambdaQueryWrapperX<CreditSummaryReportDO>()
                .eqIfPresent(CreditSummaryReportDO::getReportPeriod, pageReqVO.getReportPeriod())
                .eqIfPresent(CreditSummaryReportDO::getReportType, pageReqVO.getReportType())
                .eqIfPresent(CreditSummaryReportDO::getStatus, pageReqVO.getStatus())
                .orderByDesc(CreditSummaryReportDO::getCreateTime));
    }

    /**
     * 根据周期和类型查询汇总报表
     *
     * @param reportPeriod 报送周期
     * @param reportType 报表类型
     * @return 汇总报表
     */
    default CreditSummaryReportDO selectByPeriodAndType(String reportPeriod, String reportType) {
        return selectOne(new LambdaQueryWrapperX<CreditSummaryReportDO>()
                .eq(CreditSummaryReportDO::getReportPeriod, reportPeriod)
                .eq(CreditSummaryReportDO::getReportType, reportType));
    }

    /**
     * 根据周期范围查询汇总报表列表（用于季报计算）
     *
     * @param startPeriod 起始周期
     * @param endPeriod 结束周期
     * @param reportType 报表类型
     * @return 汇总报表列表
     */
    default List<CreditSummaryReportDO> selectListByPeriodRange(String startPeriod, String endPeriod, String reportType) {
        return selectList(new LambdaQueryWrapperX<CreditSummaryReportDO>()
                .ge(CreditSummaryReportDO::getReportPeriod, startPeriod)
                .le(CreditSummaryReportDO::getReportPeriod, endPeriod)
                .eq(CreditSummaryReportDO::getReportType, reportType)
                .eq(CreditSummaryReportDO::getStatus, 1) // 只查询已完成的
                .orderByAsc(CreditSummaryReportDO::getReportPeriod));
    }

}
