package com.wmt.module.pfb.controller.pfb.enterprise.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Schema(description = "PFB - 新增企业（入参）")
@Data
public class PfbEnterpriseCreateReqVO {

    @Schema(description = "企业中文名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "自测科技有限公司")
    @NotBlank
    private String enterpriseName;
    @Schema(description = "统一社会信用代码 18 位", requiredMode = Schema.RequiredMode.REQUIRED, example = "91110000MA012TEST01")
    @NotBlank
    private String companyCreditCode;
    @Schema(description = "核心联系方式")
    private String coreContact;
}
