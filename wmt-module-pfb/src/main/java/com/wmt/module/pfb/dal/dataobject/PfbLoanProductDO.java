package com.wmt.module.pfb.dal.dataobject;

import com.wmt.framework.mybatis.core.dataobject.BaseDO;
import com.wmt.framework.tenant.core.aop.TenantIgnore;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@TableName("pfb_loan_product")
@Data
@EqualsAndHashCode(callSuper = true)
@TenantIgnore
public class PfbLoanProductDO extends BaseDO {

    @TableId(type = IdType.INPUT)
    private String id;
    private String productCode;
    private String productName;
    private String productTerm;
    private String repaymentMethod;
    private String productTags;
    private Integer isHot;
    private Integer productStatus;
    private Integer sortOrder;
    private BigDecimal maxAmountLimit;
    private BigDecimal minRateLimit;
    private LocalDateTime productEffectiveDate;
    private String productDesc;
}
