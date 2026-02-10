package com.wmt.module.credit.job;

import com.wmt.module.credit.service.report.CreditSummaryReportService;
import com.xxl.job.core.context.XxlJobHelper;
import com.xxl.job.core.handler.annotation.XxlJob;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

/**
 * 征信季报生成定时任务（XXL-Job）
 *
 * 任务职责：
 * 1. 计算上个季度的报送周期（YYYY-Q1/Q2/Q3/Q4）
 * 2. 调用报表生成服务生成季报
 *
 * @author AHC源码
 */
@Component
@Slf4j
public class CreditQuarterlyReportGenerateJob {

    @Resource
    private CreditSummaryReportService summaryReportService;

    /**
     * XXL-Job 处理器：征信季报生成
     *
     * 调度配置示例（在 XXL-Job 管理台中配置 Cron 表达式）：
     * - 每季度第 1 个月 1 号 凌晨 3 点：0 0 3 1 1,4,7,10 ?
     */
    @XxlJob("creditQuarterlyReportGenerateJob")
    public void execute() {
        String quarterPeriod = calculateLastQuarter();
        XxlJobHelper.log("开始生成征信季报，报送周期：{}", quarterPeriod);
        try {
            Long reportId = summaryReportService.generateQuarterlyReport(quarterPeriod);
            XxlJobHelper.log("生成征信季报成功，报表编号：{}", reportId);
        } catch (Exception e) {
            log.error("生成征信季报失败，周期：{}", quarterPeriod, e);
            XxlJobHelper.log("生成征信季报失败：{}", e.getMessage());
            XxlJobHelper.handleFail("生成征信季报失败：" + e.getMessage());
        }
    }

    /**
     * 计算上个季度的报送周期，格式：YYYY-Q1/Q2/Q3/Q4
     */
    private String calculateLastQuarter() {
        LocalDate now = LocalDate.now();
        int year = now.getYear();
        int currentQuarter = (now.getMonthValue() - 1) / 3 + 1;
        int lastQuarter = currentQuarter - 1;
        if (lastQuarter == 0) {
            lastQuarter = 4;
            year = year - 1;
        }
        return year + "-Q" + lastQuarter;
    }

}

