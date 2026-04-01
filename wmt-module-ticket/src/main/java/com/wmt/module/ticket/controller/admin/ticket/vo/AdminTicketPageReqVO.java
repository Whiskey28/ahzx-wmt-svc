package com.wmt.module.ticket.controller.admin.ticket.vo;

import com.wmt.framework.common.pojo.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Schema(description = "管理后台 - 工单分页 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
public class AdminTicketPageReqVO extends PageParam {
    @Schema(description = "工单号", example = "TK20260327001")
    private String ticketNo;
    @Schema(description = "标题关键字", example = "登录")
    private String title;
    @Schema(description = "工单状态", example = "20")
    private Integer status;
    @Schema(description = "分类编号", example = "1")
    private Long categoryId;
    @Schema(description = "用户编号", example = "100")
    private Long userId;
    @Schema(description = "指派管理员编号", example = "200")
    private Long assignedAdminUserId;
}
