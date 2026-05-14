package com.wmt.module.pfb.controller.pfb.banner.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema(description = "PFB - Banner 列表项（出参）")
@Data
public class PfbBannerRespVO {

    @Schema(description = "主键 UUID", example = "22222222-2222-2222-2222-222222222201")
    private String id;
    @Schema(description = "标题")
    private String title;
    @Schema(description = "图片 URL")
    private String imageUrl;
    @Schema(description = "跳转链接")
    private String linkUrl;
    @Schema(description = "排序")
    private Integer sortOrder;
}
