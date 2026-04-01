package com.wmt.module.ticket.controller.admin.ticket.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Schema(description = "管理后台 - 工单指派 Request VO")
@Data
public class AdminTicketAssignReqVO {
    @Schema(description = "工单编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @NotNull
    private Long id;
    @Schema(description = "指派管理员编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "1001")
    @NotNull
    private Long assignedAdminUserId;
}
