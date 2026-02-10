package com.wmt.module.credit.controller.admin.form.vo;

import com.wmt.framework.common.validation.InEnum;
import com.wmt.module.credit.enums.CreditReportTypeEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.Map;

/**
 * 征信表单数据新增/修改 Request VO
 *
 * @author AHC源码
 */
@Schema(description = "管理后台 - 征信表单数据新增/修改 Request VO")
@Data
public class CreditFormDataSaveReqVO {

    @Schema(description = "表单编号", example = "1")
    private Long id;

    @Schema(description = "部门编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @NotNull(message = "部门编号不能为空")
    private Long deptId;

    @Schema(description = "报送周期", requiredMode = Schema.RequiredMode.REQUIRED, example = "2024-01")
    @NotBlank(message = "报送周期不能为空")
    private String reportPeriod;

    @Schema(description = "报表类型", requiredMode = Schema.RequiredMode.REQUIRED, example = "MONTHLY")
    @NotBlank(message = "报表类型不能为空")
    @InEnum(CreditReportTypeEnum.class)
    private String reportType;

    @Schema(description = "表单数据（JSON格式）")
    private Map<String, Object> formData;

}
