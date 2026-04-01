package com.wmt.module.ticket.controller.admin.ticket.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

@Schema(description = "管理后台 - 工单详情 Response VO")
@Data
public class AdminTicketDetailRespVO {
    @Schema(description = "工单信息")
    private AdminTicketRespVO ticket;
    @Schema(description = "回复列表")
    private List<AdminTicketReplyRespVO> replies;
}
