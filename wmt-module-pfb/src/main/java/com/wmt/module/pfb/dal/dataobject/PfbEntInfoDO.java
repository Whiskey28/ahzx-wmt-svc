package com.wmt.module.pfb.dal.dataobject;

import com.wmt.framework.mybatis.core.dataobject.BaseDO;
import com.wmt.framework.tenant.core.aop.TenantIgnore;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

@TableName("pfb_ent_info")
@Data
@EqualsAndHashCode(callSuper = true)
@TenantIgnore
public class PfbEntInfoDO extends BaseDO {

    @TableId(type = IdType.INPUT)
    private String id;
    private String enterpriseName;
    private String englishName;
    private String companyCreditCode;
    private String parentCompanyName;
    private String parentCompanyCreditCode;
    private String industryNature;
    private String coreContact;
    private String fictitiousPerson;
    private String fictitiousPersonId;
}
