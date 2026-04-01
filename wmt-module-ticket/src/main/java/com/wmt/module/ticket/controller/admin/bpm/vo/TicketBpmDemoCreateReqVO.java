package com.wmt.module.ticket.controller.admin.bpm.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Schema(description = "管理后台 - 工单业务表单流程演示创建 Request VO")
@Data
public class TicketBpmDemoCreateReqVO {

    @Schema(description = "流程定义 Key（须与已部署模型一致）", requiredMode = Schema.RequiredMode.REQUIRED, example = "my_process")
    @NotEmpty(message = "流程定义 Key 不能为空")
    private String processDefinitionKey;

    @Schema(description = "标题", requiredMode = Schema.RequiredMode.REQUIRED, example = "演示申请")
    @NotEmpty(message = "标题不能为空")
    private String title;

    @Schema(description = "说明")
    private String remark;

    @Schema(description = "发起人自选审批人 Map", example = "{taskKey1: [1, 2]}")
    private Map<String, List<Long>> startUserSelectAssignees;
}
