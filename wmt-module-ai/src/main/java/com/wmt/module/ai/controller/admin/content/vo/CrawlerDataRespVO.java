package com.wmt.module.ai.controller.admin.content.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Schema(description = "管理后台 - mediaCrawler 数据 Response VO")
@Data
public class CrawlerDataRespVO {

    @Schema(description = "数据 ID", example = "1")
    private Long id;

    @Schema(description = "平台", example = "douyin")
    private String platform;

    @Schema(description = "标题", example = "这是一个标题")
    private String title;

    @Schema(description = "内容", example = "这是内容")
    private String content;

    @Schema(description = "作者", example = "作者名")
    private String author;

    @Schema(description = "点赞数", example = "1000")
    private Integer likeCount;

    @Schema(description = "评论数", example = "100")
    private Integer commentCount;

    @Schema(description = "转发数", example = "50")
    private Integer shareCount;

    @Schema(description = "图片 URL 列表")
    private List<String> imageUrls;

    @Schema(description = "视频 URL")
    private String videoUrl;

    @Schema(description = "发布时间")
    private LocalDateTime publishTime;

    @Schema(description = "是否已处理", example = "false")
    private Boolean processed;

    @Schema(description = "原始数据 JSON")
    private String rawData;
}
