package com.wmt.module.pfb.dal.dataobject;

import com.wmt.framework.mybatis.core.dataobject.BaseDO;
import com.wmt.framework.tenant.core.aop.TenantIgnore;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

@TableName("pfb_loan_application")
@Data
@EqualsAndHashCode(callSuper = true)
@TenantIgnore
public class PfbLoanApplicationDO extends BaseDO {

    @TableId(type = IdType.INPUT)
    private String id;
    private Long sysUserId;
    private String individualCustomerId;
    private String entId;
    private String enterpriseNameSnapshot;
    private String companyCreditCodeSnapshot;
    private String productId;
    private Long applyAmountCent;
    private Integer termMonths;
    private String repayMethod;
    private Integer creditAuthAccepted;
    private String status;
}
