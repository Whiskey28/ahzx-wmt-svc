package com.wmt.module.pfb.controller.pfb.auth.vo;

import com.wmt.module.system.controller.admin.auth.vo.CaptchaVerificationReqVO;
import com.wmt.framework.common.validation.Mobile;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.validator.constraints.Length;

@Schema(description = "PFB - 注册")
@Data
@EqualsAndHashCode(callSuper = true)
public class PfbAuthRegisterReqVO extends CaptchaVerificationReqVO {

    @Schema(description = "手机号", requiredMode = Schema.RequiredMode.REQUIRED, example = "19000000002")
    @NotBlank
    @Mobile
    private String mobile;
    @Schema(description = "昵称", requiredMode = Schema.RequiredMode.REQUIRED, example = "自测用户")
    @NotBlank
    @Length(min = 1, max = 30)
    private String nickname;
    @Schema(description = "密码", requiredMode = Schema.RequiredMode.REQUIRED, example = "a1234567")
    @NotBlank
    @Length(min = 4, max = 16)
    private String password;
    @Schema(description = "短信验证码（REGISTER 场景）", requiredMode = Schema.RequiredMode.REQUIRED, example = "9999")
    @NotBlank
    private String smsCode;
}
