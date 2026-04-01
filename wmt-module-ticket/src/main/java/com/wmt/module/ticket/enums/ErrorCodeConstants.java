package com.wmt.module.ticket.enums;

import com.wmt.framework.common.exception.ErrorCode;

public interface ErrorCodeConstants {

    ErrorCode TICKET_NOT_EXISTS = new ErrorCode(1_090_000_000, "工单不存在");
    ErrorCode TICKET_NOT_OWNER = new ErrorCode(1_090_000_001, "您无权访问该工单");
    ErrorCode TICKET_CATEGORY_NOT_EXISTS = new ErrorCode(1_090_000_002, "工单分类不存在或未启用");
    ErrorCode TICKET_CLOSED_CANNOT_REPLY = new ErrorCode(1_090_000_003, "工单已关闭，无法继续回复");
    ErrorCode TICKET_STATUS_INVALID = new ErrorCode(1_090_000_004, "工单状态不合法");
    /** 工单模块内「业务表单 + 流程」演示数据 */
    ErrorCode TICKET_BPM_DEMO_NOT_EXISTS = new ErrorCode(1_090_000_005, "工单流程演示数据不存在");
    ErrorCode TICKET_BPM_WORKFLOW_LAB_NOT_EXISTS = new ErrorCode(1_090_000_006, "工单工作流实验数据不存在");
    ErrorCode TICKET_BPM_WORKFLOW_LAB_TYPE_INVALID = new ErrorCode(1_090_000_007, "工单工作流实验类型不合法");
    ErrorCode TICKET_BPM_WORKFLOW_LAB_AMOUNT_REQUIRED = new ErrorCode(1_090_000_008, "排他网关实验需填写金额");
    ErrorCode TICKET_BPM_WORKFLOW_LAB_SELECT_ASSIGNEE_REQUIRED = new ErrorCode(1_090_000_009, "发起人自选实验需选择审批人");
    ErrorCode TICKET_BPM_WORKFLOW_LAB_NOT_RECEIVE_TYPE = new ErrorCode(1_090_000_010, "仅接收任务实验可触发该操作");
    ErrorCode TICKET_BPM_WORKFLOW_LAB_PROCESS_NOT_BOUND = new ErrorCode(1_090_000_011, "流程实例未绑定，无法触发");
}
