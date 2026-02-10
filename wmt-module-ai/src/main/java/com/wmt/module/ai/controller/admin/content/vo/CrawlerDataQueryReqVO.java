package com.wmt.module.ai.controller.admin.content.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Data;

import java.util.List;

@Schema(description = "管理后台 - mediaCrawler 数据查询 Request VO")
@Data
public class CrawlerDataQueryReqVO {

    @Schema(description = "关键词列表", example = "[\"科技\", \"AI\"]")
    private List<String> keywords;

    @Schema(description = "平台", example = "douyin")
    private String platform;

    @Schema(description = "是否已处理", example = "false")
    private Boolean processed;

    @Schema(description = "查询数量限制", example = "10")
    @Min(value = 1, message = "查询数量至少为 1")
    @Max(value = 100, message = "查询数量最多为 100")
    private Integer limit = 10;

    @Schema(description = "最小点赞数", example = "100")
    private Integer minLikeCount;

    @Schema(description = "最小评论数", example = "10")
    private Integer minCommentCount;
}
