package com.wmt.module.credit.controller.admin.field.vo;

import com.wmt.framework.common.pojo.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * 征信字段配置分页查询 Request VO
 *
 * @author AHC源码
 */
@Schema(description = "管理后台 - 征信字段配置分页查询 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class CreditFieldConfigPageReqVO extends PageParam {

    @Schema(description = "部门编号", example = "1")
    private Long deptId;

    @Schema(description = "字段编码", example = "assets")
    private String fieldCode;

    @Schema(description = "字段名称", example = "资产")
    private String fieldName;

    @Schema(description = "字段类型", example = "DECIMAL")
    private String fieldType;

    @Schema(description = "状态", example = "1")
    private Integer status;

}
