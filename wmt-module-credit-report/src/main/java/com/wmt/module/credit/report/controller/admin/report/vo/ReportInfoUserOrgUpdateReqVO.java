package com.wmt.module.credit.report.controller.admin.report.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * 信息使用者机构明细更新 Request VO
 *
 * @author Auto
 */
@Data
public class ReportInfoUserOrgUpdateReqVO {

    @Schema(description = "主键", requiredMode = Schema.RequiredMode.REQUIRED, example = "a8f1...")
    @NotBlank(message = "主键id不能为空")
    private String id;

    @Schema(description = "机构名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "某某商业保理有限公司")
    @NotBlank(message = "机构名称不能为空")
    private String orgName;

    @Schema(description = "行业代码（industry_code 字典 item_value）", requiredMode = Schema.RequiredMode.REQUIRED, example = "bank")
    @NotBlank(message = "行业代码不能为空")
    private String industryCode;

    @Schema(description = "是否当前提供服务（0-否，1-是）", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @NotNull(message = "是否当前提供服务不能为空")
    private Integer isCurrentService;

    @Schema(description = "排序号", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @NotBlank(message = "排序号不能为空")
    private String sortNo;
}

