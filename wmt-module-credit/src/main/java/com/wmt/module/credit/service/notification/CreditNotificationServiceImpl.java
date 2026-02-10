package com.wmt.module.credit.service.notification;

import com.wmt.framework.common.enums.UserTypeEnum;
import com.wmt.module.system.service.mail.MailSendService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * 征信邮件通知 Service 实现类
 *
 * 说明：
 * - 目前系统中尚未定义“部门专员邮箱”等字段，这里先保留业务骨架和日志，
 *   后续可根据实际字段完善接收人选择和模板编码。
 *
 * @author AHC源码
 */
@Service
@Slf4j
public class CreditNotificationServiceImpl implements CreditNotificationService {

    @Resource
    private MailSendService mailSendService;

    @Override
    public void sendFormDataReminder(String reportPeriod) {
        // TODO 后续可以根据部门配置、用户邮箱等信息，构造真正的收件人列表和模板参数
        log.info("[sendFormDataReminder][报送周期：{}] 触发表单填报提醒任务（邮件发送逻辑待完善）", reportPeriod);

        // 示例：调用 MailSendService（当前使用空收件人，只记录调用示例，不真正发送）
        Map<String, Object> params = new HashMap<>();
        params.put("reportPeriod", reportPeriod);
        params.put("title", "征信报表填报提醒");
        // mailSendService.sendSingleMailToAdmin(null, Collections.emptyList(), Collections.emptyList(),
        //         Collections.emptyList(), "CREDIT_FORM_REMINDER", params);
    }

    @Override
    public void sendReportGenerateNotification(String reportPeriod, String reportType) {
        log.info("[sendReportGenerateNotification][报送周期：{} 报表类型：{}] 触发报表生成通知任务（邮件发送逻辑待完善）",
                reportPeriod, reportType);

        Map<String, Object> params = new HashMap<>();
        params.put("reportPeriod", reportPeriod);
        params.put("reportType", reportType);
        params.put("title", "征信报表生成通知");
        // mailSendService.sendSingleMailToAdmin(null, Collections.emptyList(), Collections.emptyList(),
        //         Collections.emptyList(), "CREDIT_REPORT_GENERATE", params);
    }

}

