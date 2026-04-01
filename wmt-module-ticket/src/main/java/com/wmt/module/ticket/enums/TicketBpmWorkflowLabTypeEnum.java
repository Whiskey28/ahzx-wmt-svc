package com.wmt.module.ticket.enums;

import cn.hutool.core.util.ArrayUtil;
import com.wmt.framework.common.core.ArrayValuable;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

/**
 * 工单模块 BPM 实验类型（与表 ticket_bpm_workflow_lab.lab_type 一致）
 */
@Getter
@AllArgsConstructor
public enum TicketBpmWorkflowLabTypeEnum implements ArrayValuable<String> {

    /** 排他网关：启动变量 amount */
    XOR("XOR"),
    /** 发起人自选审批人 */
    SELECT_USER("SELECT_USER"),
    /** 并行网关 */
    PARALLEL("PARALLEL"),
    /** 接收任务 + 外部 trigger */
    RECEIVE("RECEIVE");

    public static final String[] ARRAYS = Arrays.stream(values()).map(TicketBpmWorkflowLabTypeEnum::getType).toArray(String[]::new);

    private final String type;

    public static TicketBpmWorkflowLabTypeEnum valueOfType(String type) {
        return ArrayUtil.firstMatch(v -> v.getType().equals(type), values());
    }

    @Override
    public String[] array() {
        return ARRAYS;
    }
}
