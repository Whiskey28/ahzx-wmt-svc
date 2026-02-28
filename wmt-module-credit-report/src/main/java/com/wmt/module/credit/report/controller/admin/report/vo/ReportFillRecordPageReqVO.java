package com.wmt.module.credit.report.controller.admin.report.vo;

import com.wmt.framework.common.pojo.SortablePageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * 报表填报记录分页查询 Request VO
 *
 * @author AHC源码
 */
@Schema(description = "管理后台 - 报表填报记录分页查询 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class ReportFillRecordPageReqVO extends SortablePageParam {

    @NotEmpty(message = "报表分类必需要选择")
    @Schema(description = "报表分类id（jimu_report_category.id）", example = "1011126161407836160")
    private String categoryId;


    @Schema(description = "报表模板id（jimu_report.id）", example = "1173147297280667648")
    private String reportId;

    @Schema(description = "报表模板名称（jimu_report.name）", example = "实习证明副本0642")
    private String reportName;

    @Schema(description = "填报周期（月度）", example = "2025-12-01")
    private String periodId;

    @Schema(description = "角色id", example = "1")
    private String roleId;

}
