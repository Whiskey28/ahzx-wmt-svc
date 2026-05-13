package com.wmt.module.pfb.dal.dataobject;

import com.wmt.framework.mybatis.core.dataobject.BaseDO;
import com.wmt.framework.tenant.core.aop.TenantIgnore;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

@TableName("pfb_system_user_ext")
@Data
@EqualsAndHashCode(callSuper = true)
@TenantIgnore
public class PfbSystemUserExtDO extends BaseDO {

    @TableId(value = "sys_user_id", type = IdType.INPUT)
    private Long sysUserId;
    private String currentIndividualCustomerId;
    private String defaultEntId;
}
