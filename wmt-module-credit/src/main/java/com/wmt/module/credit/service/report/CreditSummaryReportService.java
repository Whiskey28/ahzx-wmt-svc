package com.wmt.module.credit.service.report;

import com.wmt.framework.common.pojo.PageResult;
import com.wmt.module.credit.controller.admin.report.vo.CreditSummaryReportPageReqVO;
import com.wmt.module.credit.dal.dataobject.report.CreditSummaryReportDO;

/**
 * 征信汇总报表 Service 接口
 * <p>
 * 阶段7：实现月报与季报生成、查询等能力
 *
 * @author AHC源码
 */
public interface CreditSummaryReportService {

    /**
     * 生成月报
     *
     * @param reportPeriod 报送周期（格式：YYYY-MM）
     * @return 报表编号
     */
    Long generateMonthlyReport(String reportPeriod);

    /**
     * 生成季报
     *
     * @param quarterPeriod 季度周期（格式：YYYY-Q1/Q2/Q3/Q4）
     * @return 报表编号
     */
    Long generateQuarterlyReport(String quarterPeriod);

    /**
     * 根据编号获取汇总报表
     *
     * @param id 报表编号
     * @return 汇总报表
     */
    CreditSummaryReportDO getSummaryReport(Long id);

    /**
     * 分页查询汇总报表
     *
     * @param pageReqVO 查询条件
     * @return 分页结果
     */
    PageResult<CreditSummaryReportDO> getSummaryReportPage(CreditSummaryReportPageReqVO pageReqVO);

    /**
     * 根据周期和类型查询报表
     *
     * @param reportPeriod 报送周期
     * @param reportType   报表类型
     * @return 报表数据
     */
    CreditSummaryReportDO getByPeriodAndType(String reportPeriod, String reportType);

}
