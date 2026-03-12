package com.wmt.module.credit.report.dal.dataobject;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.wmt.framework.mybatis.core.dataobject.BaseDO;
import lombok.*;

import java.math.BigDecimal;

/**
 * 报表填报 - 长三角征信链（数据部，固定字段 1:1）
 *
 * 对应表：report_fill_yangtze_credit_chain
 *
 * @author Auto
 */
@TableName("report_fill_yangtze_credit_chain")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReportFillYangtzeCreditChainDO extends BaseDO {

    /**
     * 主键（32位UUID，无连字符）
     */
    @TableId(type = IdType.ASSIGN_UUID)
    private String id;

    /**
     * 主记录id（= report_fill_basic_info.id）
     */
    private String recordId;

    /**
     * 工商数据-爬虫
     */
    private BigDecimal yangtzeIndustryCrawler;

    /**
     * 工商数据-商采
     */
    private BigDecimal yangtzeIndustryPurchase;

    /**
     * 税务数据-平台
     */
    private BigDecimal yangtzeTaxPlatform;

    /**
     * 司法数据-商采
     */
    private BigDecimal yangtzeJudicialPurchase;

    /**
     * 税务违法数据-爬虫
     */
    private BigDecimal yangtzeTaxViolationCrawler;

    /**
     * 双公示数据-爬虫
     */
    private BigDecimal yangtzeDoublePublicityCrawler;

    /**
     * 全国风险库-商采
     */
    private BigDecimal yangtzeNationalRiskPurchase;
}

