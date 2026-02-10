package com.wmt.module.credit.job;

import com.wmt.module.credit.service.report.CreditSummaryReportService;
import com.xxl.job.core.context.XxlJobHelper;
import com.xxl.job.core.handler.annotation.XxlJob;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * 征信月报生成定时任务（XXL-Job）
 *
 * 任务职责：
 * 1. 计算上个月的报送周期（YYYY-MM）
 * 2. 调用报表生成服务生成月报
 *
 * @author AHC源码
 */
@Component
@Slf4j
public class CreditMonthlyReportGenerateJob {

    @Resource
    private CreditSummaryReportService summaryReportService;

    /**
     * XXL-Job 处理器：征信月报生成
     *
     * 调度配置示例（在 XXL-Job 管理台中配置 Cron 表达式）：
     * - 每月 1 号 凌晨 2 点：0 0 2 1 * ?
     */
    @XxlJob("creditMonthlyReportGenerateJob")
    public void execute() {
        String reportPeriod = calculateLastMonth();
        XxlJobHelper.log("开始生成征信月报，报送周期：{}", reportPeriod);
        try {
            Long reportId = summaryReportService.generateMonthlyReport(reportPeriod);
            XxlJobHelper.log("生成征信月报成功，报表编号：{}", reportId);
        } catch (Exception e) {
            log.error("生成征信月报失败，周期：{}", reportPeriod, e);
            XxlJobHelper.log("生成征信月报失败：{}", e.getMessage());
            XxlJobHelper.handleFail("生成征信月报失败：" + e.getMessage());
        }
    }

    /**
     * 计算上个月的报送周期，格式：YYYY-MM
     */
    private String calculateLastMonth() {
        LocalDate now = LocalDate.now();
        LocalDate lastMonth = now.minusMonths(1).withDayOfMonth(1);
        return lastMonth.format(DateTimeFormatter.ofPattern("yyyy-MM"));
    }

}

