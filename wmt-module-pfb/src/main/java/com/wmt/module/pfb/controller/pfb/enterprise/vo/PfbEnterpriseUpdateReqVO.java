package com.wmt.module.pfb.controller.pfb.enterprise.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema(description = "PFB - 编辑企业")
@Data
public class PfbEnterpriseUpdateReqVO {

    private String enterpriseName;
    private String englishName;
    private String coreContact;
}
