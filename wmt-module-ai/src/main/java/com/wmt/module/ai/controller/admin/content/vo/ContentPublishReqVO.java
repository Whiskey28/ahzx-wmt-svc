package com.wmt.module.ai.controller.admin.content.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Schema(description = "管理后台 - 内容发布 Request VO")
@Data
public class ContentPublishReqVO {

    @Schema(description = "平台", requiredMode = Schema.RequiredMode.REQUIRED, example = "xiaohongshu")
    @NotNull(message = "平台不能为空")
    private String platform;

    @Schema(description = "标题", requiredMode = Schema.RequiredMode.REQUIRED, example = "这是一个标题")
    @NotEmpty(message = "标题不能为空")
    private String title;

    @Schema(description = "正文内容", requiredMode = Schema.RequiredMode.REQUIRED, example = "这是正文内容")
    @NotEmpty(message = "正文内容不能为空")
    private String content;

    @Schema(description = "图片 URL 列表", example = "[\"http://.../image1.jpg\"]")
    private List<String> images;

    @Schema(description = "标签列表", example = "[\"标签1\", \"标签2\"]")
    private List<String> tags;

    @Schema(description = "视频 URL", example = "http://.../video.mp4")
    private String videoUrl;

    @Schema(description = "数据 ID", example = "1")
    private Long dataId;

    @Schema(description = "是否定时发布", example = "false")
    private Boolean scheduled;

    @Schema(description = "定时发布时间（时间戳）", example = "1704067200000")
    private Long scheduledTime;
}
