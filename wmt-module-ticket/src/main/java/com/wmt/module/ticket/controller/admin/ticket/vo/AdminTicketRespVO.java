package com.wmt.module.ticket.controller.admin.ticket.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

@Schema(description = "管理后台 - 工单 Response VO")
@Data
public class AdminTicketRespVO {
    @Schema(description = "工单编号", example = "1")
    private Long id;
    @Schema(description = "工单号", example = "TK20260327001")
    private String ticketNo;
    @Schema(description = "用户编号", example = "100")
    private Long userId;
    @Schema(description = "分类编号", example = "1")
    private Long categoryId;
    @Schema(description = "标题", example = "登录异常")
    private String title;
    @Schema(description = "内容", example = "无法登录系统")
    private String content;
    @Schema(description = "状态", example = "20")
    private Integer status;
    @Schema(description = "优先级", example = "2")
    private Integer priority;
    @Schema(description = "指派管理员编号", example = "200")
    private Long assignedAdminUserId;
    @Schema(description = "最后回复时间")
    private LocalDateTime lastReplyTime;
    @Schema(description = "创建时间")
    private LocalDateTime createTime;
}
