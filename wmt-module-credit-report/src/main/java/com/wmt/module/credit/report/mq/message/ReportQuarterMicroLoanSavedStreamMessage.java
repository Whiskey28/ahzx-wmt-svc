package com.wmt.module.credit.report.mq.message;

import com.wmt.framework.mq.redis.core.stream.AbstractRedisStreamMessage;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 季报（小微贷款）保存事件（Redis Stream）
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class ReportQuarterMicroLoanSavedStreamMessage extends AbstractRedisStreamMessage {

    /**
     * 主记录 ID（report_fill_basic_info.id）
     */
    private String recordId;

    /**
     * 周期，格式 yyyyQn
     */
    private String periodId;

    /**
     * 报表模板 ID
     */
    private String reportId;

    /**
     * 角色 ID
     */
    private String roleId;

    /**
     * 动作：INSERT/UPDATE
     */
    private String action;
}

