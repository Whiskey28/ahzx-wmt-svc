package com.wmt.module.ticket.controller.admin.ticket.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Schema(description = "管理后台 - 工单回复 Request VO")
@Data
public class AdminTicketReplyReqVO {
    @Schema(description = "工单编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @NotNull
    private Long ticketId;
    @Schema(description = "回复内容", requiredMode = Schema.RequiredMode.REQUIRED, example = "请补充截图信息")
    @NotBlank
    private String content;
}
