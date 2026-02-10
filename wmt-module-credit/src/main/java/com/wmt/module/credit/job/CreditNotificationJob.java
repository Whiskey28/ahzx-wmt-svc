package com.wmt.module.credit.job;

import com.wmt.module.credit.service.notification.CreditNotificationService;
import com.xxl.job.core.context.XxlJobHelper;
import com.xxl.job.core.handler.annotation.XxlJob;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * 征信邮件提醒定时任务（XXL-Job）
 *
 * 任务职责：
 * 1. 查询即将到期的填报任务（当前实现按当前月份报送周期发送提醒）
 * 2. 调用邮件通知服务发送提醒邮件
 *
 * @author AHC源码
 */
@Component
@Slf4j
public class CreditNotificationJob {

    @Resource
    private CreditNotificationService notificationService;

    /**
     * XXL-Job 处理器：征信填报提醒
     *
     * 调度配置示例（在 XXL-Job 管理台中配置 Cron 表达式）：
     * - 每天上午 9 点：0 0 9 * * ?
     */
    @XxlJob("creditFormDataReminderJob")
    public void execute() {
        String reportPeriod = calculateCurrentMonth();
        XxlJobHelper.log("开始执行征信填报提醒任务，报送周期：{}", reportPeriod);
        try {
            notificationService.sendFormDataReminder(reportPeriod);
            XxlJobHelper.log("征信填报提醒任务执行完成");
        } catch (Exception e) {
            log.error("征信填报提醒任务执行失败，周期：{}", reportPeriod, e);
            XxlJobHelper.log("征信填报提醒任务执行失败：{}", e.getMessage());
            XxlJobHelper.handleFail("征信填报提醒任务执行失败：" + e.getMessage());
        }
    }

    /**
     * 计算当前月份的报送周期，格式：YYYY-MM
     */
    private String calculateCurrentMonth() {
        LocalDate now = LocalDate.now().withDayOfMonth(1);
        return now.format(DateTimeFormatter.ofPattern("yyyy-MM"));
    }

}

