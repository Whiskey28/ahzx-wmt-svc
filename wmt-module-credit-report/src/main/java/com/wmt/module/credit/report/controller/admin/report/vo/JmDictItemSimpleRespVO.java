package com.wmt.module.credit.report.controller.admin.report.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 简单字典项 Response VO（用于积木报表查询字典条目）
 *
 * @author Auto
 */
@Data
public class JmDictItemSimpleRespVO {

    @Schema(description = "字典项文本", example = "银行")
    private String itemText;

    @Schema(description = "字典项值", example = "bank")
    private String itemValue;

    @Schema(description = "排序值", example = "1")
    private Integer sortOrder;
}

