package com.wmt.module.ticket.controller.admin.ticket.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Schema(description = "管理后台 - 更新工单状态 Request VO")
@Data
public class AdminTicketUpdateStatusReqVO {
    @Schema(description = "工单编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @NotNull
    private Long id;
    @Schema(description = "工单状态(10-已创建,20-处理中,40-已关闭)", requiredMode = Schema.RequiredMode.REQUIRED, example = "20")
    @NotNull
    private Integer status;
}
