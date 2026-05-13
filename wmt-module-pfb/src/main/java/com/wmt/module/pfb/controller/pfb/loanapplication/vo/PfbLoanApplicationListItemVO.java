package com.wmt.module.pfb.controller.pfb.loanapplication.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

@Schema(description = "PFB - 申请列表项（一期只读 stub）")
@Data
public class PfbLoanApplicationListItemVO {

    private String id;
    private String productId;
    private String entId;
    private String status;
    private LocalDateTime createTime;
}
