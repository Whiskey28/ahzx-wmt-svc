package com.wmt.module.credit.controller.admin.field.vo;

import com.wmt.framework.common.validation.InEnum;
import com.wmt.module.credit.enums.CreditFieldTypeEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.Map;

/**
 * 征信字段配置新增/修改 Request VO
 *
 * @author AHC源码
 */
@Schema(description = "管理后台 - 征信字段配置新增/修改 Request VO")
@Data
public class CreditFieldConfigSaveReqVO {

    @Schema(description = "字段编号", example = "1")
    private Long id;

    @Schema(description = "部门编号（0表示通用字段）", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @NotNull(message = "部门编号不能为空")
    private Long deptId;

    @Schema(description = "字段编码", requiredMode = Schema.RequiredMode.REQUIRED, example = "assets")
    @NotBlank(message = "字段编码不能为空")
    private String fieldCode;

    @Schema(description = "字段名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "资产")
    @NotBlank(message = "字段名称不能为空")
    private String fieldName;

    @Schema(description = "字段类型", requiredMode = Schema.RequiredMode.REQUIRED, example = "DECIMAL")
    @NotBlank(message = "字段类型不能为空")
    @InEnum(CreditFieldTypeEnum.class)
    private String fieldType;

    @Schema(description = "是否必填", requiredMode = Schema.RequiredMode.REQUIRED, example = "true")
    @NotNull(message = "是否必填不能为空")
    private Boolean required;

    @Schema(description = "默认值", example = "0")
    private String defaultValue;

    @Schema(description = "校验规则（JSON格式）")
    private Map<String, Object> validationRule;

    @Schema(description = "前端展示配置（JSON格式）", example = "{\"groupName\": \"市场部填写\", \"groupOrder\": 1}")
    private Map<String, Object> displayConfig;

    @Schema(description = "显示顺序", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @NotNull(message = "显示顺序不能为空")
    private Integer displayOrder;

    @Schema(description = "状态", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @NotNull(message = "状态不能为空")
    private Integer status;

}
