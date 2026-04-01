package com.wmt.module.ticket.controller.app.ticket.vo;

import com.wmt.framework.common.pojo.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Schema(description = "用户 App - 工单分页 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
public class AppTicketPageReqVO extends PageParam {
    @Schema(description = "工单状态", example = "20")
    private Integer status;
}
