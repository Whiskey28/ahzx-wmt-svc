package com.wmt.module.ticket.controller.admin.bpm.lab.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Schema(description = "管理后台 - 工单工作流实验创建")
@Data
public class TicketBpmWorkflowLabCreateReqVO {

    @Schema(description = "实验类型：XOR / SELECT_USER / PARALLEL / RECEIVE", requiredMode = Schema.RequiredMode.REQUIRED, example = "XOR")
    @NotBlank(message = "实验类型不能为空")
    private String labType;

    @Schema(description = "标题", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "标题不能为空")
    private String title;

    @Schema(description = "说明")
    private String remark;

    @Schema(description = "金额（排他网关 XOR 必填）")
    private BigDecimal amount;

    @Schema(description = "覆盖流程定义 Key（默认按实验类型映射，一般无需填写）")
    private String processDefinitionKey;

    @Schema(description = "发起人自选审批人：key 为 BPMN 用户任务节点 id（Activity_select_approve），value 为审批人用户编号列表")
    private Map<String, List<Long>> startUserSelectAssignees;
}
