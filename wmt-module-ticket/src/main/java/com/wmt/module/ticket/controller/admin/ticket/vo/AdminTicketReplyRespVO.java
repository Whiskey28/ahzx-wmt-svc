package com.wmt.module.ticket.controller.admin.ticket.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

@Schema(description = "管理后台 - 工单回复 Response VO")
@Data
public class AdminTicketReplyRespVO {
    @Schema(description = "回复编号", example = "1")
    private Long id;
    @Schema(description = "回复方类型(1-用户,2-管理员)", example = "2")
    private Integer fromType;
    @Schema(description = "用户编号", example = "100")
    private Long userId;
    @Schema(description = "管理员编号", example = "200")
    private Long adminUserId;
    @Schema(description = "回复内容", example = "请补充截图信息")
    private String content;
    @Schema(description = "创建时间")
    private LocalDateTime createTime;
}
