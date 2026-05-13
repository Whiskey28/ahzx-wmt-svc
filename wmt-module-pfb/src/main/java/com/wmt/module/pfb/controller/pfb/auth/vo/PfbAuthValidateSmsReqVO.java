package com.wmt.module.pfb.controller.pfb.auth.vo;

import com.wmt.framework.common.validation.Mobile;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Schema(description = "PFB - 校验短信验证码")
@Data
public class PfbAuthValidateSmsReqVO {

    @NotBlank
    @Mobile
    private String mobile;
    @NotBlank
    private String scene;
    @NotBlank
    private String code;
}
