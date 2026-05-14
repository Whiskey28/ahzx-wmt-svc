package com.wmt.module.pfb.controller.pfb.auth.vo;

import com.wmt.module.system.controller.admin.auth.vo.CaptchaVerificationReqVO;
import com.wmt.framework.common.validation.Mobile;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Schema(description = "PFB - 发送短信验证码")
@Data
@EqualsAndHashCode(callSuper = true)
public class PfbAuthSmsSendReqVO extends CaptchaVerificationReqVO {

    @Schema(description = "手机号", requiredMode = Schema.RequiredMode.REQUIRED, example = "19000000001")
    @NotBlank
    @Mobile
    private String mobile;
    @Schema(description = "短信场景", requiredMode = Schema.RequiredMode.REQUIRED, allowableValues = {"LOGIN", "REGISTER", "RESET_PASSWORD"}, example = "LOGIN")
    @NotBlank
    private String scene;
}
