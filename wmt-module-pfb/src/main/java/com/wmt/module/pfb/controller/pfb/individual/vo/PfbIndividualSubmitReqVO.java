package com.wmt.module.pfb.controller.pfb.individual.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Schema(description = "PFB - 提交/更新自然人")
@Data
public class PfbIndividualSubmitReqVO {

    @NotBlank(message = "真实姓名不能为空")
    private String realName;
    @NotBlank(message = "身份证号不能为空")
    private String idCardNo;
    private String address;
    private String idCardFrontImage;
    private String idCardBackImage;
}
