package com.wmt.module.ai.controller.admin.content.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Schema(description = "管理后台 - 完整内容生成 Request VO")
@Data
public class ContentFullGenerateReqVO {

    @Schema(description = "数据 ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @NotNull(message = "数据 ID 不能为空")
    private Long dataId;

    @Schema(description = "源数据 JSON", example = "{\"title\": \"...\", \"content\": \"...\"}")
    private String sourceData;

    @Schema(description = "目标平台", requiredMode = Schema.RequiredMode.REQUIRED, example = "xiaohongshu")
    @NotNull(message = "目标平台不能为空")
    private String platform;

    @Schema(description = "是否生成图片", example = "true")
    private Boolean generateImage = true;

    @Schema(description = "图片生成配置")
    private ContentImageGenerateReqVO imageConfig;
}
