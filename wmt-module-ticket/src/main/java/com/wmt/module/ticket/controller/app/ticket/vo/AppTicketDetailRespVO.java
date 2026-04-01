package com.wmt.module.ticket.controller.app.ticket.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

@Schema(description = "用户 App - 工单详情 Response VO")
@Data
public class AppTicketDetailRespVO {
    @Schema(description = "工单信息")
    private AppTicketRespVO ticket;
    @Schema(description = "回复列表")
    private List<AppTicketReplyRespVO> replies;
}
