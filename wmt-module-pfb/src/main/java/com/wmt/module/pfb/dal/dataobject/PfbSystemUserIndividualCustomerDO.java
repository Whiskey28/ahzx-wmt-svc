package com.wmt.module.pfb.dal.dataobject;

import com.wmt.framework.mybatis.core.dataobject.BaseDO;
import com.wmt.framework.tenant.core.aop.TenantIgnore;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

@TableName("pfb_system_user_individual_customer")
@Data
@EqualsAndHashCode(callSuper = true)
@TenantIgnore
public class PfbSystemUserIndividualCustomerDO extends BaseDO {

    @TableId(type = IdType.INPUT)
    private String id;
    private Long sysUserId;
    private String individualCustomerId;
}
