package com.wmt.module.pfb.controller.pfb.auth.vo;

import com.wmt.framework.common.validation.Mobile;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Schema(description = "PFB - 校验短信验证码")
@Data
public class PfbAuthValidateSmsReqVO {

    @Schema(description = "手机号", requiredMode = Schema.RequiredMode.REQUIRED, example = "19000000001")
    @NotBlank
    @Mobile
    private String mobile;
    @Schema(description = "短信场景", requiredMode = Schema.RequiredMode.REQUIRED, allowableValues = {"LOGIN", "REGISTER", "RESET_PASSWORD"}, example = "LOGIN")
    @NotBlank
    private String scene;
    @Schema(description = "短信验证码", requiredMode = Schema.RequiredMode.REQUIRED, example = "9999")
    @NotBlank
    private String code;
}
