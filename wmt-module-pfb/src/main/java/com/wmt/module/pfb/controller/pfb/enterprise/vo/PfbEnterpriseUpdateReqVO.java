package com.wmt.module.pfb.controller.pfb.enterprise.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema(description = "PFB - 编辑企业（入参）")
@Data
public class PfbEnterpriseUpdateReqVO {

    @Schema(description = "企业中文名称")
    private String enterpriseName;
    @Schema(description = "企业英文名称")
    private String englishName;
    @Schema(description = "核心联系方式")
    private String coreContact;
}
