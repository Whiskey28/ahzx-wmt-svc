package com.wmt.module.ai.controller.admin.content.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

@Schema(description = "管理后台 - 内容生成 Response VO")
@Data
public class ContentGenerateRespVO {

    @Schema(description = "生成的标题", example = "这是一个吸引人的标题")
    private String title;

    @Schema(description = "生成的正文", example = "这是生成的正文内容...")
    private String content;

    @Schema(description = "生成的标签列表", example = "[\"标签1\", \"标签2\"]")
    private List<String> tags;

    @Schema(description = "生成时间戳", example = "1704067200000")
    private Long generateTime;
}
