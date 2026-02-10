package com.wmt.module.ai.controller.admin.content.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema(description = "管理后台 - 内容发布 Response VO")
@Data
public class ContentPublishRespVO {

    @Schema(description = "发布是否成功", example = "true")
    private Boolean success;

    @Schema(description = "平台发布 ID", example = "123456789")
    private String platformPublishId;

    @Schema(description = "发布 URL", example = "https://xiaohongshu.com/...")
    private String publishUrl;

    @Schema(description = "错误信息", example = "发布失败：...")
    private String errorMessage;

    @Schema(description = "发布时间戳", example = "1704067200000")
    private Long publishTime;
}
