package com.wmt.module.credit.controller.admin.validation.vo;

import com.wmt.framework.common.validation.InEnum;
import com.wmt.module.credit.enums.CreditValidationRuleTypeEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * 征信校验规则新增/修改 Request VO
 *
 * @author AHC源码
 */
@Schema(description = "管理后台 - 征信校验规则新增/修改 Request VO")
@Data
public class CreditValidationRuleSaveReqVO {

    @Schema(description = "规则编号", example = "1")
    private Long id;

    @Schema(description = "字段编码", requiredMode = Schema.RequiredMode.REQUIRED, example = "assets")
    @NotBlank(message = "字段编码不能为空")
    private String fieldCode;

    @Schema(description = "规则类型", requiredMode = Schema.RequiredMode.REQUIRED, example = "REQUIRED")
    @NotBlank(message = "规则类型不能为空")
    @InEnum(CreditValidationRuleTypeEnum.class)
    private String ruleType;

    @Schema(description = "校验表达式", requiredMode = Schema.RequiredMode.REQUIRED, example = "{}")
    @NotBlank(message = "校验表达式不能为空")
    private String ruleExpression;

    @Schema(description = "错误提示信息", example = "字段不能为空")
    private String errorMessage;

    @Schema(description = "优先级", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @NotNull(message = "优先级不能为空")
    private Integer priority;

    @Schema(description = "状态", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @NotNull(message = "状态不能为空")
    private Integer status;

}
