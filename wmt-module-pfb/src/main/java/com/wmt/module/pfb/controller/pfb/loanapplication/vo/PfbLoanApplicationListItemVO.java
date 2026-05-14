package com.wmt.module.pfb.controller.pfb.loanapplication.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

@Schema(description = "PFB - 申请列表项（一期只读 stub）")
@Data
public class PfbLoanApplicationListItemVO {

    @Schema(description = "申请 UUID")
    private String id;
    @Schema(description = "产品 UUID")
    private String productId;
    @Schema(description = "企业 UUID")
    private String entId;
    @Schema(description = "申请状态")
    private String status;
    @Schema(description = "创建时间")
    private LocalDateTime createTime;
}
