package com.wmt.module.pfb.dal.dataobject;

import com.wmt.framework.mybatis.core.dataobject.BaseDO;
import com.wmt.framework.tenant.core.aop.TenantIgnore;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

@TableName("pfb_banner")
@Data
@EqualsAndHashCode(callSuper = true)
@TenantIgnore
public class PfbBannerDO extends BaseDO {

    @TableId(type = IdType.INPUT)
    private String id;
    private String title;
    private String imageUrl;
    private String linkUrl;
    private Integer sortOrder;
    private Integer bannerStatus;
    private LocalDateTime effectiveBegin;
    private LocalDateTime effectiveEnd;
}
