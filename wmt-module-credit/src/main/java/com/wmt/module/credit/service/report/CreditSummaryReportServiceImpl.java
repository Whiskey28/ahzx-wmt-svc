package com.wmt.module.credit.service.report;

import com.wmt.framework.common.pojo.PageResult;
import com.wmt.module.credit.controller.admin.report.vo.CreditSummaryReportPageReqVO;
import com.wmt.module.credit.dal.dataobject.form.CreditFormDataDO;
import com.wmt.module.credit.dal.dataobject.report.CreditSummaryReportDO;
import com.wmt.module.credit.dal.mysql.form.CreditFormDataMapper;
import com.wmt.module.credit.dal.mysql.report.CreditSummaryReportMapper;
import com.wmt.module.credit.enums.CreditReportStatusEnum;
import com.wmt.module.credit.enums.CreditReportTypeEnum;
import com.wmt.module.credit.enums.ErrorCodeConstants;
import com.wmt.module.credit.framework.calculator.CreditCalculationEngine;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import static com.wmt.framework.common.exception.util.ServiceExceptionUtil.exception;

/**
 * 征信汇总报表 Service 实现类
 *
 * @author AHC源码
 */
@Service
@Slf4j
public class CreditSummaryReportServiceImpl implements CreditSummaryReportService {

    @Resource
    private CreditSummaryReportMapper summaryReportMapper;

    @Resource
    private CreditFormDataMapper formDataMapper;

    @Resource
    private CreditCalculationEngine calculationEngine;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long generateMonthlyReport(String reportPeriod) {
        // 1. 校验当期月报是否已存在
        CreditSummaryReportDO exist = summaryReportMapper.selectByPeriodAndType(reportPeriod,
                CreditReportTypeEnum.MONTHLY.getType());
        if (exist != null) {
            throw exception(ErrorCodeConstants.SUMMARY_REPORT_DUPLICATE);
        }

        // 2. 校验是否存在已提交的表单数据（至少一条）
        List<CreditFormDataDO> formDataList = formDataMapper.selectListForCalculation(reportPeriod,
                CreditReportTypeEnum.MONTHLY.getType());
        if (formDataList == null || formDataList.isEmpty()) {
            throw exception(ErrorCodeConstants.SUMMARY_REPORT_GENERATE_FAIL_FORM_NOT_SUBMITTED);
        }

        // 3. 调用计算引擎生成报表数据
        Map<String, Object> reportData = calculationEngine.calculateSummaryReport(reportPeriod,
                CreditReportTypeEnum.MONTHLY.getType());

        // 4. 保存报表数据
        CreditSummaryReportDO report = new CreditSummaryReportDO();
        report.setReportPeriod(reportPeriod);
        report.setReportType(CreditReportTypeEnum.MONTHLY.getType());
        report.setReportData(reportData);
        report.setCalculationLog("生成月报成功");
        report.setStatus(CreditReportStatusEnum.COMPLETED.getStatus());
        report.setGenerateTime(LocalDateTime.now());
        summaryReportMapper.insert(report);

        log.info("生成月报成功，周期：{}，报表编号：{}", reportPeriod, report.getId());
        return report.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long generateQuarterlyReport(String quarterPeriod) {
        // 1. 校验当期季报是否已存在
        CreditSummaryReportDO exist = summaryReportMapper.selectByPeriodAndType(quarterPeriod,
                CreditReportTypeEnum.QUARTERLY.getType());
        if (exist != null) {
            throw exception(ErrorCodeConstants.SUMMARY_REPORT_DUPLICATE);
        }

        // 2. 校验相关月报是否全部生成
        validateMonthlyReportsGenerated(quarterPeriod);

        // 3. 调用计算引擎生成报表数据
        Map<String, Object> reportData = calculationEngine.calculateSummaryReport(quarterPeriod,
                CreditReportTypeEnum.QUARTERLY.getType());

        // 4. 保存报表数据
        CreditSummaryReportDO report = new CreditSummaryReportDO();
        report.setReportPeriod(quarterPeriod);
        report.setReportType(CreditReportTypeEnum.QUARTERLY.getType());
        report.setReportData(reportData);
        report.setCalculationLog("生成季报成功");
        report.setStatus(CreditReportStatusEnum.COMPLETED.getStatus());
        report.setGenerateTime(LocalDateTime.now());
        summaryReportMapper.insert(report);

        log.info("生成季报成功，周期：{}，报表编号：{}", quarterPeriod, report.getId());
        return report.getId();
    }

    @Override
    public CreditSummaryReportDO getSummaryReport(Long id) {
        CreditSummaryReportDO report = summaryReportMapper.selectById(id);
        if (report == null) {
            throw exception(ErrorCodeConstants.SUMMARY_REPORT_NOT_EXISTS);
        }
        return report;
    }

    @Override
    public PageResult<CreditSummaryReportDO> getSummaryReportPage(CreditSummaryReportPageReqVO pageReqVO) {
        return summaryReportMapper.selectPage(pageReqVO);
    }

    @Override
    public CreditSummaryReportDO getByPeriodAndType(String reportPeriod, String reportType) {
        return summaryReportMapper.selectByPeriodAndType(reportPeriod, reportType);
    }

    /**
     * 校验季报对应的月报是否全部生成
     *
     * @param quarterPeriod 季度周期（格式：YYYY-Q1）
     */
    private void validateMonthlyReportsGenerated(String quarterPeriod) {
        List<String> monthlyPeriods = parseQuarterToMonths(quarterPeriod);
        for (String monthly : monthlyPeriods) {
            CreditSummaryReportDO monthlyReport = summaryReportMapper.selectByPeriodAndType(
                    monthly, CreditReportTypeEnum.MONTHLY.getType());
            if (monthlyReport == null || !CreditReportStatusEnum.isCompleted(monthlyReport.getStatus())) {
                throw exception(ErrorCodeConstants.SUMMARY_REPORT_GENERATE_FAIL_MONTHLY_NOT_GENERATED);
            }
        }
    }

    /**
     * 解析季度周期为月份列表，例如 2024-Q1 -> [2024-01, 2024-02, 2024-03]
     *
     * @param quarterPeriod 季度周期
     * @return 月份列表
     */
    private List<String> parseQuarterToMonths(String quarterPeriod) {
        String[] parts = quarterPeriod.split("-Q");
        if (parts.length != 2) {
            throw exception(ErrorCodeConstants.SUMMARY_REPORT_GENERATE_FAIL,
                    "季度周期格式错误，正确格式示例：2024-Q1");
        }
        int year = Integer.parseInt(parts[0]);
        int quarter = Integer.parseInt(parts[1]);
        int startMonth = (quarter - 1) * 3 + 1;
        return List.of(
                String.format("%d-%02d", year, startMonth),
                String.format("%d-%02d", year, startMonth + 1),
                String.format("%d-%02d", year, startMonth + 2)
        );
    }

}
