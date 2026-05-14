package com.wmt.module.pfb.controller.pfb.individual.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Schema(description = "PFB - 提交/更新自然人（入参）")
@Data
public class PfbIndividualSubmitReqVO {

    @Schema(description = "真实姓名", requiredMode = Schema.RequiredMode.REQUIRED, example = "张三")
    @NotBlank(message = "真实姓名不能为空")
    private String realName;
    @Schema(description = "身份证号", requiredMode = Schema.RequiredMode.REQUIRED, example = "110101199001011234")
    @NotBlank(message = "身份证号不能为空")
    private String idCardNo;
    @Schema(description = "联系地址")
    private String address;
    @Schema(description = "身份证正面图 URL")
    private String idCardFrontImage;
    @Schema(description = "身份证反面图 URL")
    private String idCardBackImage;
}
