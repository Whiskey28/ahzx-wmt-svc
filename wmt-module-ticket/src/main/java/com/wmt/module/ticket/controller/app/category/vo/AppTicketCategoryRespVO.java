package com.wmt.module.ticket.controller.app.category.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema(description = "用户 App - 工单分类 Response VO")
@Data
public class AppTicketCategoryRespVO {
    @Schema(description = "分类编号", example = "1")
    private Long id;
    @Schema(description = "分类名称", example = "账号问题")
    private String name;
}
