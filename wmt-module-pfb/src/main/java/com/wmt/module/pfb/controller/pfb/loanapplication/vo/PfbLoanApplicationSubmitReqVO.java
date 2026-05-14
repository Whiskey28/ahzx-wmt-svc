package com.wmt.module.pfb.controller.pfb.loanapplication.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Schema(description = "PFB - 融资申请提交")
@Data
public class PfbLoanApplicationSubmitReqVO {

    @Schema(description = "贷款产品 UUID", requiredMode = Schema.RequiredMode.REQUIRED, example = "11111111-1111-1111-1111-111111111101")
    @NotBlank
    private String productId;
    @Schema(description = "绑定企业 UUID", requiredMode = Schema.RequiredMode.REQUIRED, example = "33333333-3333-3333-3333-333333333301")
    @NotBlank
    private String entId;
    @Schema(description = "申请金额（分），须为万元对应的分整数倍", requiredMode = Schema.RequiredMode.REQUIRED, example = "100000000")
    @NotNull
    @Min(1)
    private Long applyAmountCent;
    @Schema(description = "期限（月）", requiredMode = Schema.RequiredMode.REQUIRED, example = "12")
    @NotNull
    @Min(1)
    private Integer termMonths;
    @Schema(description = "还款方式编码或文案", requiredMode = Schema.RequiredMode.REQUIRED, example = "EQUAL_PRINCIPAL_INTEREST")
    @NotBlank
    private String repayMethod;
    @Schema(description = "是否接受征信查询报送授权 1是 0否", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @NotNull
    private Integer creditAuthAccepted;
}
