package com.wmt.module.ai.controller.admin.content.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Schema(description = "管理后台 - 文案生成 Request VO")
@Data
public class ContentTextGenerateReqVO {

    @Schema(description = "数据 ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @NotNull(message = "数据 ID 不能为空")
    private Long dataId;

    @Schema(description = "源数据 JSON", example = "{\"title\": \"...\", \"content\": \"...\"}")
    private String sourceData;

    @Schema(description = "目标平台", requiredMode = Schema.RequiredMode.REQUIRED, example = "xiaohongshu")
    @NotNull(message = "目标平台不能为空")
    private String platform;

    @Schema(description = "文案风格", example = "1")
    private Integer style;

    @Schema(description = "文案长度", example = "500")
    private Integer length;

    @Schema(description = "额外提示词", example = "要求：吸引眼球、有互动性")
    private String extraPrompt;
}
