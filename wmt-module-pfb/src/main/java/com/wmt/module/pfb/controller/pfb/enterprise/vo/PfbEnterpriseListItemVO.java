package com.wmt.module.pfb.controller.pfb.enterprise.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema(description = "PFB - 我的企业项（出参）")
@Data
public class PfbEnterpriseListItemVO {

    @Schema(description = "企业 UUID", example = "33333333-3333-3333-3333-333333333301")
    private String entId;
    @Schema(description = "企业名称")
    private String enterpriseName;
    @Schema(description = "统一社会信用代码", example = "91110000123456789X")
    private String companyCreditCode;
    @Schema(description = "是否默认企业")
    private Boolean isDefault;
}
