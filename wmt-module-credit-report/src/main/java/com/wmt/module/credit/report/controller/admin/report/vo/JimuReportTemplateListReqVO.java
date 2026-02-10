package com.wmt.module.credit.report.controller.admin.report.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * 积木报表模板列表查询 Request VO
 *
 * @author AHC源码
 */
@Schema(description = "管理后台 - 积木报表模板列表查询 Request VO")
@Data
public class JimuReportTemplateListReqVO {

    @Schema(description = "报表分类id（jimu_report_category.id）", requiredMode = Schema.RequiredMode.REQUIRED, example = "1011126161407836160")
    @NotBlank(message = "报表分类id不能为空")
    private String categoryId;

    @Schema(description = "是否填报报表（0-数据报表，1-在线填报表单）。不传则默认查询在线填报表单（submitForm=1）", example = "1")
    private Integer submitForm;

}
