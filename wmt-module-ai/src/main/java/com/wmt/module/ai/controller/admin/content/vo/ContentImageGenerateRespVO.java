package com.wmt.module.ai.controller.admin.content.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

@Schema(description = "管理后台 - 图片生成 Response VO")
@Data
public class ContentImageGenerateRespVO {

    @Schema(description = "生成的图片 URL 列表", example = "[\"http://.../image1.jpg\"]")
    private List<String> imageUrls;

    @Schema(description = "图片 ID 列表", example = "[1, 2]")
    private List<Long> imageIds;

    @Schema(description = "生成时间戳", example = "1704067200000")
    private Long generateTime;
}
