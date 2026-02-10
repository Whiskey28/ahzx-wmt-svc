package com.wmt.module.credit.report.dal.dataobject;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.wmt.framework.mybatis.core.dataobject.BaseDO;
import lombok.*;

import java.math.BigDecimal;

/**
 * 报表填报 - 经营情况（财务口径）DO（1:1 关联主记录）
 *
 * @author AHC源码
 */
@TableName("report_fill_biz_stat_finance")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReportFillBizStatFinanceDO extends BaseDO {

    /**
     * 主键（UUID）
     */
    @TableId(type = IdType.ASSIGN_UUID)
    private String id;

    /**
     * 父记录id（= report_fill_basic_info.id）
     */
    private String parentId;

    /**
     * 资产（单位：人民币元）
     */
    private BigDecimal assetAmount;

    /**
     * 负债（单位：人民币元）
     */
    private BigDecimal liabilityAmount;

    /**
     * 本年度总收入（单位：人民币元）
     */
    private BigDecimal yearIncomeAmount;

    /**
     * 本年度企业征信业务收入（单位：人民币元）
     */
    private BigDecimal yearCreditIncomeAmount;

    /**
     * 本年度净利润（单位：人民币元）
     */
    private BigDecimal yearNetProfitAmount;

}

