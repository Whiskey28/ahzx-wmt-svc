package com.wmt.module.pfb.controller.pfb.enterprise.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema(description = "PFB - 我的企业项")
@Data
public class PfbEnterpriseListItemVO {

    private String entId;
    private String enterpriseName;
    private String companyCreditCode;
    private Boolean isDefault;
}
