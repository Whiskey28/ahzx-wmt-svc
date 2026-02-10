package com.wmt.module.credit.report.dal.dataobject;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.wmt.framework.mybatis.core.dataobject.BaseDO;
import lombok.*;

import java.math.BigDecimal;

/**
 * 报表填报 - 信息来源情况（按行业分类）DO（1:N）
 *
 * @author AHC源码
 */
@TableName("report_fill_info_source_by_industry")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReportFillInfoSourceByIndustryDO extends BaseDO {

    /**
     * 主键
     */
    @TableId(type = IdType.ASSIGN_UUID)
    private String id;

    /**
     * 主记录id
     */
    private String recordId;

    /**
     * 行业分类（字典 code，可扩展）
     */
    private String industryCode;

    /**
     * 信息提供者机构总累计数
     */
    private BigDecimal providerOrgTotal;

    /**
     * 当前提供服务的信息提供者机构数
     */
    private BigDecimal providerOrgCurrent;

}

