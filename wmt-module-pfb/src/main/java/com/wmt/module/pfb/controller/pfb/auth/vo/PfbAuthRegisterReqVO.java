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

    @NotBlank
    @Mobile
    private String mobile;
    @NotBlank
    @Length(min = 1, max = 30)
    private String nickname;
    @NotBlank
    @Length(min = 4, max = 16)
    private String password;
    @NotBlank
    private String smsCode;
}
