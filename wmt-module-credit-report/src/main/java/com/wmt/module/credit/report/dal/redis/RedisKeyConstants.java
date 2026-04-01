package com.wmt.module.credit.report.dal.redis;

/**
 * Credit Report Redis Key 常量
 */
public interface RedisKeyConstants {

    /**
     * 服务次数（按月）总计缓存
     *
     * KEY 格式：credit_report:service_count_total:{reportId}:{monthPeriodId}
     * VALUE 数据类型：String（BigDecimal 字符串）
     */
    String CREDIT_REPORT_SERVICE_COUNT_TOTAL_BY_MONTH = "credit_report:service_count_total:%s:%s";

    /**
     * Stream 消费幂等键
     *
     * KEY 格式：credit_report:mq_idempotent:quarter_micro_loan_saved:{recordId}:{periodId}:{action}
     * VALUE 数据类型：String（占位）
     */
    String CREDIT_REPORT_MQ_IDEMPOTENT_QUARTER_MICRO_LOAN_SAVED =
            "credit_report:mq_idempotent:quarter_micro_loan_saved:%s:%s:%s";
}

