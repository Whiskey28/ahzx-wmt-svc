package com.wmt.module.credit.controller.admin.calculation.vo;

import com.wmt.framework.common.validation.InEnum;
import com.wmt.module.credit.enums.CreditReportTypeEnum;
import com.wmt.module.credit.enums.CreditRuleTypeEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.Map;

/**
 * 征信计算规则新增/修改 Request VO
 *
 * @author AHC源码
 */
@Schema(description = "管理后台 - 征信计算规则新增/修改 Request VO")
@Data
public class CreditCalculationRuleSaveReqVO {

    @Schema(description = "规则编号", example = "1")
    private Long id;

    @Schema(description = "目标字段编码", requiredMode = Schema.RequiredMode.REQUIRED, example = "totalAssets")
    @NotBlank(message = "目标字段编码不能为空")
    private String targetFieldCode;

    @Schema(description = "目标字段名称（用于前端显示）", example = "总资产")
    private String targetFieldName;

    @Schema(description = "报表类型", requiredMode = Schema.RequiredMode.REQUIRED, example = "MONTHLY")
    @NotBlank(message = "报表类型不能为空")
    @InEnum(CreditReportTypeEnum.class)
    private String reportType;

    @Schema(description = "规则类型", requiredMode = Schema.RequiredMode.REQUIRED, example = "FORMULA")
    @NotBlank(message = "规则类型不能为空")
    @InEnum(CreditRuleTypeEnum.class)
    private String ruleType;

    @Schema(description = "计算表达式", requiredMode = Schema.RequiredMode.REQUIRED, example = "dept1.assets + dept2.assets")
    @NotBlank(message = "计算表达式不能为空")
    private String ruleExpression;

    @Schema(description = "数据来源（JSON格式）")
    private Map<String, Object> dataSource;

    @Schema(description = "计算顺序", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @NotNull(message = "计算顺序不能为空")
    private Integer calculationOrder;

    @Schema(description = "规则描述", example = "计算总资产")
    private String description;

    @Schema(description = "前端展示配置（JSON格式）", example = "{\"groupName\": \"金融、类金融机构\", \"groupOrder\": 1}")
    private Map<String, Object> displayConfig;

    @Schema(description = "状态", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @NotNull(message = "状态不能为空")
    private Integer status;

}
