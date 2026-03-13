package com.wmt.module.credit.report.dal.dataobject;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.wmt.framework.mybatis.core.dataobject.BaseDO;
import lombok.*;

import java.math.BigDecimal;

/**
 * 报表填报 - 季报（小微贷款）DO（1:1 关联主记录）
 *
 * @author Auto
 */
@TableName("report_fill_quarter_micro_loan")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReportFillQuarterMicroLoanDO extends BaseDO {

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
     * 小微企业申请贷款户数（当季发生额）
     */
    private BigDecimal applyHouseholds;

    /**
     * 小微企业获得贷款户数（当季发生额）
     */
    private BigDecimal grantHouseholds;

    /**
     * 小微企业获得贷款金额（当季发生额）
     */
    private BigDecimal grantAmount;

    /**
     * 其中：信用贷款金额（当季发生额）
     */
    private BigDecimal creditGrantAmount;

    /**
     * 小微企业贷款平均年利率（按金额加权平均计算，百分数）
     */
    private BigDecimal avgAnnualRatePct;

    /**
     * 小微企业贷款不良率（按金额加权平均计算，百分数）
     */
    private BigDecimal nplRatePct;
}

