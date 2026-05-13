package com.wmt.module.pfb.controller.pfb.loanproduct.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Schema(description = "PFB - 贷款产品")
@Data
public class PfbLoanProductRespVO {

    private String id;
    private String productCode;
    private String productName;
    private String productTerm;
    private String repaymentMethod;
    private String productTags;
    private Integer isHot;
    private Integer sortOrder;
    private BigDecimal maxAmountLimit;
    private BigDecimal minRateLimit;
    private LocalDateTime productEffectiveDate;
    private String productDesc;
}
