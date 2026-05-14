package com.wmt.module.pfb.controller.pfb.auth.vo;

import com.wmt.module.system.controller.admin.auth.vo.CaptchaVerificationReqVO;
import com.wmt.framework.common.validation.Mobile;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.validator.constraints.Length;

@Schema(description = "PFB - 手机号+密码+短信验证码登录（US-02）；可选图形验证码见 wmt.captcha.enable")
@Data
@EqualsAndHashCode(callSuper = true)
public class PfbAuthLoginReqVO extends CaptchaVerificationReqVO {

    @Schema(description = "手机号", requiredMode = Schema.RequiredMode.REQUIRED, example = "19000000001")
    @NotBlank
    @Mobile
    private String mobile;

    @Schema(description = "密码", requiredMode = Schema.RequiredMode.REQUIRED, example = "a1234567")
    @NotBlank
    @Length(min = 4, max = 32, message = "密码长度不合法")
    private String password;

    @Schema(description = "短信验证码（LOGIN 场景 send-sms-code 下发）", requiredMode = Schema.RequiredMode.REQUIRED, example = "9999")
    @NotBlank(message = "短信验证码不能为空")
    private String smsCode;
}
