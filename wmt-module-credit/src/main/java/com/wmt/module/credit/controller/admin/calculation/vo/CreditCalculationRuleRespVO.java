package com.wmt.module.credit.controller.admin.calculation.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * 征信计算规则 Response VO
 *
 * @author AHC源码
 */
@Schema(description = "管理后台 - 征信计算规则 Response VO")
@Data
public class CreditCalculationRuleRespVO {

    @Schema(description = "规则编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    private Long id;

    @Schema(description = "目标字段编码", requiredMode = Schema.RequiredMode.REQUIRED, example = "totalAssets")
    private String targetFieldCode;

    @Schema(description = "目标字段名称（用于前端显示）", example = "总资产")
    private String targetFieldName;

    @Schema(description = "报表类型", requiredMode = Schema.RequiredMode.REQUIRED, example = "MONTHLY")
    private String reportType;

    @Schema(description = "规则类型", requiredMode = Schema.RequiredMode.REQUIRED, example = "FORMULA")
    private String ruleType;

    @Schema(description = "计算表达式", requiredMode = Schema.RequiredMode.REQUIRED, example = "dept1.assets + dept2.assets")
    private String ruleExpression;

    @Schema(description = "数据来源（JSON格式）")
    private Map<String, Object> dataSource;

    @Schema(description = "计算顺序", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    private Integer calculationOrder;

    @Schema(description = "规则描述", example = "计算总资产")
    private String description;

    @Schema(description = "前端展示配置（JSON格式）", example = "{\"groupName\": \"金融、类金融机构\", \"groupOrder\": 1}")
    private Map<String, Object> displayConfig;

    @Schema(description = "状态", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    private Integer status;

    @Schema(description = "创建时间", requiredMode = Schema.RequiredMode.REQUIRED)
    private LocalDateTime createTime;

    @Schema(description = "更新时间", requiredMode = Schema.RequiredMode.REQUIRED)
    private LocalDateTime updateTime;

}
