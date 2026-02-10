package com.wmt.module.credit.report.controller.admin.report.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 积木报表分类 Response VO
 */
@Schema(description = "管理后台 - 积木报表分类 Response VO")
@Data
public class JimuReportCategoryRespVO {

    @Schema(description = "分类id", example = "1011126161407836160")
    private String id;

    @Schema(description = "分类名称", example = "填报报表")
    private String name;

    @Schema(description = "父级id", example = "0")
    private String parentId;
}
