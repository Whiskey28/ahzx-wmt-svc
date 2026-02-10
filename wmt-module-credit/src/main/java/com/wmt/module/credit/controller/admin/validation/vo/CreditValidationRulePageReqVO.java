package com.wmt.module.credit.controller.admin.validation.vo;

import com.wmt.framework.common.pojo.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * 征信校验规则分页查询 Request VO
 *
 * @author AHC源码
 */
@Schema(description = "管理后台 - 征信校验规则分页查询 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class CreditValidationRulePageReqVO extends PageParam {

    @Schema(description = "字段编码", example = "assets")
    private String fieldCode;

    @Schema(description = "规则类型", example = "REQUIRED")
    private String ruleType;

    @Schema(description = "状态", example = "1")
    private Integer status;

}
