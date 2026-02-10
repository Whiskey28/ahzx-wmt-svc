package com.wmt.module.credit.service.notification;

/**
 * 征信邮件通知 Service 接口
 *
 * 主要职责：
 * 1. 发送表单填报提醒
 * 2. 发送报表生成通知
 *
 * 真正的邮件发送委托给 system 模块的 MailSendService，
 * 本 Service 负责业务文案和接收人选择。
 *
 * @author AHC源码
 */
public interface CreditNotificationService {

    /**
     * 发送表单填报提醒
     *
     * @param reportPeriod 报送周期（例如：2024-01）
     */
    void sendFormDataReminder(String reportPeriod);

    /**
     * 发送报表生成通知
     *
     * @param reportPeriod 报送周期
     * @param reportType   报表类型（MONTHLY / QUARTERLY）
     */
    void sendReportGenerateNotification(String reportPeriod, String reportType);

}

