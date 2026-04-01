package com.wmt.module.credit.report.mq.consumer;

import com.wmt.module.credit.report.dal.redis.RedisKeyConstants;
import com.wmt.framework.mq.redis.core.stream.AbstractRedisStreamMessageListener;
import com.wmt.module.credit.report.mq.message.ReportQuarterMicroLoanSavedStreamMessage;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.time.Duration;

/**
 * 季报（小微贷款）保存事件消费者（Redis Stream）
 */
@Component
@Slf4j
public class ReportQuarterMicroLoanSavedStreamConsumer
        extends AbstractRedisStreamMessageListener<ReportQuarterMicroLoanSavedStreamMessage> {

    private static final Duration IDEMPOTENT_TTL = Duration.ofHours(24);

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Override
    public void onMessage(ReportQuarterMicroLoanSavedStreamMessage message) {
        String idempotentKey = buildIdempotentKey(message);
        Boolean firstConsume = stringRedisTemplate.opsForValue().setIfAbsent(idempotentKey, "1", IDEMPOTENT_TTL);
        if (!Boolean.TRUE.equals(firstConsume)) {
            log.info("[onMessage][重复消息，已跳过 idempotentKey={} action={} recordId={} periodId={} reportId={} roleId={}]",
                    idempotentKey, message.getAction(), message.getRecordId(), message.getPeriodId(),
                    message.getReportId(), message.getRoleId());
            return;
        }

        log.info("[onMessage][收到 Redis Stream 消息 action={} recordId={} periodId={} reportId={} roleId={}]",
                message.getAction(), message.getRecordId(), message.getPeriodId(),
                message.getReportId(), message.getRoleId());
    }

    private String buildIdempotentKey(ReportQuarterMicroLoanSavedStreamMessage message) {
        String recordId = StringUtils.defaultIfBlank(message.getRecordId(), "unknown");
        String periodId = StringUtils.defaultIfBlank(message.getPeriodId(), "unknown");
        String action = StringUtils.defaultIfBlank(message.getAction(), "unknown");
        return String.format(RedisKeyConstants.CREDIT_REPORT_MQ_IDEMPOTENT_QUARTER_MICRO_LOAN_SAVED,
                recordId, periodId, action);
    }
}

