package com.wmt.module.ticket.service.bpm.lab;

import java.util.Set;

/**
 * 工作流实验：流程定义 Key、节点 ID（须与 docs/bpmn/lab 下 BPMN 一致）
 */
public final class TicketBpmWorkflowLabConstants {

    private TicketBpmWorkflowLabConstants() {
    }

    /** 排他网关演示 */
    public static final String PROCESS_KEY_XOR = "ticket_lab_xor";
    /** 发起人自选审批人 */
    public static final String PROCESS_KEY_SELECT_USER = "ticket_lab_select";
    /** 并行网关 */
    public static final String PROCESS_KEY_PARALLEL = "ticket_lab_parallel";
    /** 接收任务 */
    public static final String PROCESS_KEY_RECEIVE = "ticket_lab_receive";

    /** 与 B 场景 BPMN 中 UserTask id 一致，用于 startUserSelectAssignees 的 Map key */
    public static final String SELECT_USER_TASK_ACTIVITY_ID = "Activity_select_approve";

    /** 与 D 场景 BPMN 中 receiveTask id 一致，用于 triggerTask */
    public static final String RECEIVE_TASK_ACTIVITY_ID = "Activity_receive_wait";

    private static final Set<String> LAB_PROCESS_KEYS = Set.of(
            PROCESS_KEY_XOR,
            PROCESS_KEY_SELECT_USER,
            PROCESS_KEY_PARALLEL,
            PROCESS_KEY_RECEIVE);

    public static boolean isLabProcessKey(String processDefinitionKey) {
        return processDefinitionKey != null && LAB_PROCESS_KEYS.contains(processDefinitionKey);
    }
}
