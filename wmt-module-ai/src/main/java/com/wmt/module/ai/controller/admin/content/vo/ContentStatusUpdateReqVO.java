package com.wmt.module.ai.controller.admin.content.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Schema(description = "管理后台 - 数据状态更新 Request VO")
@Data
public class ContentStatusUpdateReqVO {

    @Schema(description = "数据 ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @NotNull(message = "数据 ID 不能为空")
    private Long dataId;

    @Schema(description = "是否已处理", example = "true")
    private Boolean processed;

    @Schema(description = "是否已发布", example = "true")
    private Boolean published;

    @Schema(description = "发布时间", example = "1704067200000")
    private Long publishTime;

    @Schema(description = "备注", example = "已发布到小红书和抖音")
    private String remark;
}
