package com.wmt.module.credit.report.dal.dataobject;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.wmt.framework.mybatis.core.dataobject.BaseDO;
import lombok.*;

/**
 * 报表填报 - 信息使用者机构明细 DO（1:N）
 *
 * @author AHC源码
 */
@TableName("report_fill_info_user_org_item")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReportFillInfoUserOrgItemDO extends BaseDO {

    /**
     * 主键
     */
    @TableId(type = IdType.ASSIGN_UUID)
    private String id;

    /**
     * 机构名称
     */
    private String orgName;

    /**
     * 行业分类（字典 code，可扩展）
     */
    private String industryCode;

    /**
     * 是否当前提供服务（0-否，1-是）
     */
    private Integer isCurrentService;

    /**
     * 排序号
     */
    private String sortNo;

}
