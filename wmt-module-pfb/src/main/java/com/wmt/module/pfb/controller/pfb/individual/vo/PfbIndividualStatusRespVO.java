package com.wmt.module.pfb.controller.pfb.individual.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema(description = "PFB - 自然人实名/绑定状态")
@Data
public class PfbIndividualStatusRespVO {

    @Schema(description = "是否完成 L1（库内 is_verified=1）")
    private Boolean l1Verified;
    @Schema(description = "L2 实人（一期占位，默认 false）")
    private Boolean l2Verified;
    @Schema(description = "当前自然人客户主键，可能为空")
    private String currentIndividualCustomerId;
}
