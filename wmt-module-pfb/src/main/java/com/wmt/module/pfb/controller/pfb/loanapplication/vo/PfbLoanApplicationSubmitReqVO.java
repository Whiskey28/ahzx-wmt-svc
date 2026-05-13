package com.wmt.module.pfb.controller.pfb.loanapplication.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Schema(description = "PFB - 融资申请提交")
@Data
public class PfbLoanApplicationSubmitReqVO {

    @NotBlank
    private String productId;
    @NotBlank
    private String entId;
    @NotNull
    @Min(1)
    private Long applyAmountCent;
    @NotNull
    @Min(1)
    private Integer termMonths;
    @NotBlank
    private String repayMethod;
    @NotNull
    private Integer creditAuthAccepted;
}
