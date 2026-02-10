package com.wmt.module.credit.report.dal.dataobject;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.wmt.framework.mybatis.core.dataobject.BaseDO;
import lombok.*;

import java.math.BigDecimal;

/**
 * 报表填报 - 提供的征信产品/服务次数 DO（固定字段，1:1）
 *
 * 字段与字典一一对应（按 sort_order 绑定），不再存 product_type / product_name 的可扩展 code。
 *
 * @author AHC源码
 */
@TableName("report_fill_product_stat")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReportFillProductStatDO extends BaseDO {

    /**
     * 主键
     */
    @TableId(type = IdType.ASSIGN_UUID)
    private String id;

    /**
     * 主记录id（= report_fill_basic_info.id）
     */
    private String recordId;

    /**
     * 企业信用报告、招投标报告：当年提供次数（字典一一对应：report_type/report）
     */
    private BigDecimal reportYearCount;

    /**
     * 信用分：当年提供次数（字典一一对应：credit_type/credit）
     */
    private BigDecimal creditYearCount;

    /**
     * 反欺诈：当年提供次数（字典一一对应：anti_type/anti）
     */
    private BigDecimal antiYearCount;

}

