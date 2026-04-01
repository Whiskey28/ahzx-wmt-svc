package com.wmt.module.ticket.controller.app.ticket.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Schema(description = "用户 App - 工单回复 Request VO")
@Data
public class AppTicketReplyReqVO {
    @Schema(description = "工单编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @NotNull
    private Long ticketId;
    @Schema(description = "回复内容", requiredMode = Schema.RequiredMode.REQUIRED, example = "补充说明信息")
    @NotBlank
    private String content;
}
