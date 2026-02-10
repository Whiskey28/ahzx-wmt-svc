package com.wmt.module.credit.report.controller.admin.report.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * 信息使用者政府机构明细更新 Request VO
 *
 * @author Auto
 */
@Data
public class ReportInfoUserGovUpdateReqVO {

    @Schema(description = "主键", requiredMode = Schema.RequiredMode.REQUIRED, example = "a8f1...")
    @NotBlank(message = "主键id不能为空")
    private String id;

    @Schema(description = "政府机构名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "某某局")
    @NotBlank(message = "政府机构名称不能为空")
    private String govOrgName;

    @Schema(description = "是否当前提供服务（0-否，1-是）", example = "1")
    private Integer isCurrentService;

    @Schema(description = "排序号", example = "1")
    private String sortNo;
}

