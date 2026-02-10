package com.wmt.module.credit.controller.admin.form.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * 征信表单数据提交 Request VO
 *
 * @author AHC源码
 */
@Schema(description = "管理后台 - 征信表单数据提交 Request VO")
@Data
public class CreditFormDataSubmitReqVO {

    @Schema(description = "表单编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @NotNull(message = "表单编号不能为空")
    private Long id;

}
