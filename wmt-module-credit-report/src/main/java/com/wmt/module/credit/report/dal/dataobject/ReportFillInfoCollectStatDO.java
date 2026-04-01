package com.wmt.module.credit.report.dal.dataobject;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.wmt.framework.mybatis.core.dataobject.BaseDO;
import lombok.*;

import java.math.BigDecimal;

/**
 * 报表填报 - 信息采集情况 DO（1:1 关联主记录）
 *
 * 注意：字段较多，后续可按最终口径清单继续扩展列。
 *
 * @author AHC源码
 */
@TableName("report_fill_info_collect_stat")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReportFillInfoCollectStatDO extends BaseDO {

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
     * 收录企业（含个体工商户、小微企业）总户数
     */
    private BigDecimal collectEnterpriseTotal;

    /**
     * 收录借贷信息的企业户数
     */
    private BigDecimal collectBorrowTotal;

    /**
     * 收录商业交易信息的企业户数
     */
    private BigDecimal collectTradeTotal;

    /**
     * 收录财务信息的企业户数
     */
    private BigDecimal collectFinanceTotal;

    /**
     * 收录税务信息的企业户数
     */
    private BigDecimal collectTaxTotal;

    /**
     * 收录进出口信息的企业户数
     */
    private BigDecimal collectCustomTotal;

    /**
     * 收录水电气等公用事业信息的企业户数
     */
    private BigDecimal collectPublicUtilityTotal;

    /**
     * 收录场内企业户数
     */
    private BigDecimal collectFieldSmeTotal;

    /**
     * 收录场内小微企业总户数
     */
    private BigDecimal collectMicroSmeTotal;

    /**
     * 收录个体工商户总户数
     */
    private BigDecimal collectIndividualTotal;

    /**
     * 收录自然人股东信息：公司董事、监事、高管人员人数
     */
    private BigDecimal collectNaturalPersonShareholderTotal;

    /**
     * 收录自然人股东信息：公司董事、监事、高管人员人数（口径扩展备用）
     */
//    private BigDecimal collectKeyPersonTotal;

}

