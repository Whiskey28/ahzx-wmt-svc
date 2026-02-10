package com.wmt.module.credit.report.dal.dataobject;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 积木报表分类 DO
 *
 * @author AHC源码
 */
@TableName("jimu_report_category")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class JimuReportCategoryDO implements Serializable {

    /**
     * 主键
     */
    @TableId
    private String id;

    /**
     * 分类名称
     */
    private String name;

    /**
     * 父级id
     */
    private String parentId;

    /**
     * 是否为叶子节点(0 否 1是)
     */
    private Integer izLeaf;

    /**
     * 来源类型( report 积木报表 screen 大屏  drag 仪表盘)
     */
    private String sourceType;

    /**
     * 租户id
     */
    private String tenantId;

    /**
     * 删除状态(0未删除，1已删除，2临时删除)
     */
    private Integer delFlag;

    /**
     * 排序
     */
    private Integer sortNo;

    /**
     * 创建人
     */
    private String createBy;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新人
     */
    private String updateBy;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;

}
