package com.wmt.module.ticket.controller.app.ticket.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Schema(description = "用户 App - 创建工单 Request VO")
@Data
public class AppTicketCreateReqVO {
    @Schema(description = "分类编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @NotNull
    private Long categoryId;
    @Schema(description = "标题", requiredMode = Schema.RequiredMode.REQUIRED, example = "登录异常")
    @NotBlank
    private String title;
    @Schema(description = "问题描述", requiredMode = Schema.RequiredMode.REQUIRED, example = "无法登录系统")
    @NotBlank
    private String content;
    @Schema(description = "优先级(1-低,2-中,3-高)", requiredMode = Schema.RequiredMode.REQUIRED, example = "2")
    @NotNull
    private Integer priority;
}
