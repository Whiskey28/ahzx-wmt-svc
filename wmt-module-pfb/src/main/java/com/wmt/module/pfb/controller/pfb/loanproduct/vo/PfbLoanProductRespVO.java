package com.wmt.module.pfb.controller.pfb.loanproduct.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Schema(description = "PFB - 贷款产品")
@Data
public class PfbLoanProductRespVO {

    @Schema(description = "产品 UUID", example = "11111111-1111-1111-1111-111111111101")
    private String id;
    @Schema(description = "产品编号")
    private String productCode;
    @Schema(description = "产品名称", example = "自测经营贷")
    private String productName;
    @Schema(description = "产品期限（展示）")
    private String productTerm;
    @Schema(description = "还款方式（展示）")
    private String repaymentMethod;
    @Schema(description = "产品标签")
    private String productTags;
    @Schema(description = "是否热门 0否 1是", example = "1")
    private Integer isHot;
    @Schema(description = "排序，越小越靠前", example = "10")
    private Integer sortOrder;
    @Schema(description = "额度上限（元）")
    private BigDecimal maxAmountLimit;
    @Schema(description = "最低利率（小数）")
    private BigDecimal minRateLimit;
    @Schema(description = "产品生效时间")
    private LocalDateTime productEffectiveDate;
    @Schema(description = "产品简介")
    private String productDesc;
}
