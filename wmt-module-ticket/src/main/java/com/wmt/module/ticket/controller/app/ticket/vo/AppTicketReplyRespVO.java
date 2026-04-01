package com.wmt.module.ticket.controller.app.ticket.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

@Schema(description = "用户 App - 工单回复 Response VO")
@Data
public class AppTicketReplyRespVO {
    @Schema(description = "回复编号", example = "1")
    private Long id;
    @Schema(description = "回复方类型(1-用户,2-管理员)", example = "1")
    private Integer fromType;
    @Schema(description = "回复内容", example = "补充说明信息")
    private String content;
    @Schema(description = "创建时间")
    private LocalDateTime createTime;
}
