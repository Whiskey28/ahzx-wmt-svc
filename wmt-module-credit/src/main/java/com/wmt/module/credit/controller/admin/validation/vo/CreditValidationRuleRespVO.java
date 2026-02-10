package com.wmt.module.credit.controller.admin.validation.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 征信校验规则 Response VO
 *
 * @author AHC源码
 */
@Schema(description = "管理后台 - 征信校验规则 Response VO")
@Data
public class CreditValidationRuleRespVO {

    @Schema(description = "规则编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    private Long id;

    @Schema(description = "字段编码", requiredMode = Schema.RequiredMode.REQUIRED, example = "assets")
    private String fieldCode;

    @Schema(description = "规则类型", requiredMode = Schema.RequiredMode.REQUIRED, example = "REQUIRED")
    private String ruleType;

    @Schema(description = "校验表达式", requiredMode = Schema.RequiredMode.REQUIRED, example = "{}")
    private String ruleExpression;

    @Schema(description = "错误提示信息", example = "字段不能为空")
    private String errorMessage;

    @Schema(description = "优先级", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    private Integer priority;

    @Schema(description = "状态", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    private Integer status;

    @Schema(description = "创建时间", requiredMode = Schema.RequiredMode.REQUIRED)
    private LocalDateTime createTime;

    @Schema(description = "更新时间", requiredMode = Schema.RequiredMode.REQUIRED)
    private LocalDateTime updateTime;

    @Schema(description = "创建人", example = "admin")
    private String creator;

}
