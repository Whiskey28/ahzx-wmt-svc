package com.wmt.module.credit.report.controller.admin.report.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 报表填报记录 Response VO
 *
 * @author AHC源码
 */
@Schema(description = "管理后台 - 报表填报记录 Response VO")
@Data
public class ReportFillRecordRespVO {

    @Schema(description = "记录id", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    private String id;

    @Schema(description = "填报周期", example = "2025-01")
    private String periodId;

    @Schema(description = "角色id", example = "1")
    private String roleId;

    @Schema(description = "项目id", example = "11")
    private String projectId;

    @Schema(description = "报表模板id", example = "1173147297280667648")
    private String reportId;

    @Schema(description = "创建时间")
    private LocalDateTime createTime;

    @Schema(description = "更新时间")
    private LocalDateTime updateTime;

    @Schema(description = "创建人", example = "1")
    private String creator;

    @Schema(description = "填报人（更新人）", example = "1")
    private String updater;

    // ========== 关联数据：分类信息 ==========
    @Schema(description = "分类id", example = "1011126161407836160")
    private String categoryId;

    @Schema(description = "分类名称", example = "填报报表")
    private String categoryName;

    // ========== 关联数据：报表模板信息 ==========
    @Schema(description = "报表模板名称", example = "实习证明副本0642")
    private String reportName;

    @Schema(description = "报表模板编码", example = "report_001")
    private String reportCode;

    @Schema(description = "报表模板说明", example = "用于填报实习证明")
    private String reportNote;

}
