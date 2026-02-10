package com.wmt.module.ai.controller.admin.content.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

@Schema(description = "管理后台 - 敏感词检查 Response VO")
@Data
public class ContentSensitivityCheckRespVO {

    @Schema(description = "是否包含敏感词", example = "false")
    private Boolean hasSensitiveWords;

    @Schema(description = "敏感词列表", example = "[\"敏感词1\", \"敏感词2\"]")
    private List<String> sensitiveWords;

    @Schema(description = "检查后的安全内容", example = "这是检查后的内容")
    private String safeContent;
}
