package com.wmt.module.credit.controller.admin.report.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * 征信汇总报表生成 Request VO
 *
 * @author AHC源码
 */
@Schema(description = "管理后台 - 征信汇总报表生成 Request VO")
@Data
public class CreditSummaryReportGenerateReqVO {

    @Schema(description = "报送周期", requiredMode = Schema.RequiredMode.REQUIRED, example = "2024-01")
    @NotBlank(message = "报送周期不能为空")
    private String reportPeriod;

    @Schema(description = "报表类型", requiredMode = Schema.RequiredMode.REQUIRED, example = "MONTHLY", 
            allowableValues = {"MONTHLY", "QUARTERLY"})
    @NotBlank(message = "报表类型不能为空")
    private String reportType;

}
