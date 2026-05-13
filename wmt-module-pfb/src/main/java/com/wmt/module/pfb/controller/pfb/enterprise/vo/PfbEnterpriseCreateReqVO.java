package com.wmt.module.pfb.controller.pfb.enterprise.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Schema(description = "PFB - 新增企业")
@Data
public class PfbEnterpriseCreateReqVO {

    @NotBlank
    private String enterpriseName;
    @NotBlank
    private String companyCreditCode;
    private String coreContact;
}
