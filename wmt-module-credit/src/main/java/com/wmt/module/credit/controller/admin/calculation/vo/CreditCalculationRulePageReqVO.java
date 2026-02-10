package com.wmt.module.credit.controller.admin.calculation.vo;

import com.wmt.framework.common.pojo.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * 征信计算规则分页查询 Request VO
 *
 * @author AHC源码
 */
@Schema(description = "管理后台 - 征信计算规则分页查询 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class CreditCalculationRulePageReqVO extends PageParam {

    @Schema(description = "目标字段编码", example = "totalAssets")
    private String targetFieldCode;

    @Schema(description = "报表类型", example = "MONTHLY")
    private String reportType;

    @Schema(description = "规则类型", example = "SUM")
    private String ruleType;

    @Schema(description = "状态", example = "1")
    private Integer status;

}
