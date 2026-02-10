package com.wmt.module.credit.controller.admin.field.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * 征信字段配置 Response VO
 *
 * @author AHC源码
 */
@Schema(description = "管理后台 - 征信字段配置 Response VO")
@Data
public class CreditFieldConfigRespVO {

    @Schema(description = "字段编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    private Long id;

    @Schema(description = "部门编号（0表示通用字段）", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    private Long deptId;

    @Schema(description = "字段编码", requiredMode = Schema.RequiredMode.REQUIRED, example = "assets")
    private String fieldCode;

    @Schema(description = "字段名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "资产")
    private String fieldName;

    @Schema(description = "字段类型", requiredMode = Schema.RequiredMode.REQUIRED, example = "DECIMAL")
    private String fieldType;

    @Schema(description = "是否必填", requiredMode = Schema.RequiredMode.REQUIRED, example = "true")
    private Boolean required;

    @Schema(description = "默认值", example = "0")
    private String defaultValue;

    @Schema(description = "校验规则（JSON格式）")
    private Map<String, Object> validationRule;

    @Schema(description = "前端展示配置（JSON格式）", example = "{\"groupName\": \"市场部填写\", \"groupOrder\": 1}")
    private Map<String, Object> displayConfig;

    @Schema(description = "显示顺序", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    private Integer displayOrder;

    @Schema(description = "状态", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    private Integer status;

    @Schema(description = "创建时间", requiredMode = Schema.RequiredMode.REQUIRED)
    private LocalDateTime createTime;

}
