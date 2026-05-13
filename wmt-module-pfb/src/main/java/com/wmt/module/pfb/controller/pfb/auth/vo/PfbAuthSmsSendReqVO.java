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

    @NotBlank
    @Mobile
    private String mobile;
    /** LOGIN / REGISTER / RESET_PASSWORD */
    @NotBlank
    private String scene;
}
