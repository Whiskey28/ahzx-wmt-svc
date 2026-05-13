package com.wmt.module.pfb.dal.dataobject;

import com.wmt.framework.mybatis.core.dataobject.BaseDO;
import com.wmt.framework.tenant.core.aop.TenantIgnore;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

@TableName("pfb_individual_customer")
@Data
@EqualsAndHashCode(callSuper = true)
@TenantIgnore
public class PfbIndividualCustomerDO extends BaseDO {

    @TableId(type = IdType.INPUT)
    private String id;
    private String realName;
    private String idCardNo;
    private String address;
    private Integer isVerified;
    private String idCardFrontImage;
    private String idCardBackImage;
}
