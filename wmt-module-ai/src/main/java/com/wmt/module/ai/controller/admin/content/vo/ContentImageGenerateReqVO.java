package com.wmt.module.ai.controller.admin.content.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Schema(description = "管理后台 - 图片生成 Request VO")
@Data
public class ContentImageGenerateReqVO {

    @Schema(description = "提示词", requiredMode = Schema.RequiredMode.REQUIRED, example = "科技感十足的配图")
    @NotEmpty(message = "提示词不能为空")
    private String prompt;

    @Schema(description = "模型 ID", example = "1024")
    private Long modelId;

    @Schema(description = "图片宽度", example = "1024")
    @NotNull(message = "图片宽度不能为空")
    private Integer width = 1024;

    @Schema(description = "图片高度", example = "1024")
    @NotNull(message = "图片高度不能为空")
    private Integer height = 1024;

    @Schema(description = "生成数量", example = "1")
    private Integer count = 1;
}
