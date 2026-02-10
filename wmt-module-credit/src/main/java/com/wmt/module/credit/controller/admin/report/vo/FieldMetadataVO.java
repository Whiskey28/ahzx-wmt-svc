package com.wmt.module.credit.controller.admin.report.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Map;

/**
 * 字段元数据 VO（用于前端展示）
 *
 * @author AHC源码
 */
@Schema(description = "字段元数据 VO（用于前端展示）")
@Data
public class FieldMetadataVO {

    @Schema(description = "字段编码", requiredMode = Schema.RequiredMode.REQUIRED, example = "total_assets_sum")
    private String fieldCode;

    @Schema(description = "字段名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "总资产")
    private String fieldName;

    @Schema(description = "前端展示配置（JSON格式）", example = "{\"groupName\": \"金融、类金融机构\", \"groupOrder\": 1}")
    private Map<String, Object> displayConfig;

}
