package com.wmt.module.credit.report.dal.dataobject;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.wmt.framework.mybatis.core.dataobject.BaseDO;
import lombok.*;

import java.math.BigDecimal;

/**
 * 报表填报 - 企业基础信息块 DO（1:1 关联主记录）
 *
 * @author AHC源码
 */
@TableName("report_fill_enterprise_basic")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReportFillEnterpriseBasicDO extends BaseDO {

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
     * 企业名称
     */
    private String enterpriseName;

    /**
     * 统一社会信用代码
     */
    private String creditCode;

    /**
     * 注册地/地址
     */
    private String address;

    /**
     * 法定代表人
     */
    private String legalPerson;

    /**
     * 注册时间
     */
    private String registerDate;

    /**
     * 经营场所
     */
    private String bizPlace;

    /**
     * 备案编号
     */
    private String recordNo;

    /**
     * 备案时间
     */
    private String recordDate;

    /**
     * 备案分支行/机构
     */
    private String recordBranch;

    /**
     * 注册资本（单位：人民币万元）
     */
    private BigDecimal regCapMillion;

    /**
     * 实缴资本（单位：人民币万元）
     */
    private BigDecimal paidInCapMillion;

    /**
     * 信息系统等级保护评定安全等级
     */
    private String ismsLevel;

    /**
     * 服务网络形式
     */
    private String serviceNetworkType;

    /**
     * 分支机构数量
     */
    private BigDecimal branchCount;

    /**
     * 控股股东
     */
    private String controllingShareholder;

    /**
     * 控股股东股份占比（%）
     */
    private String controllingShareholderRatio;

    /**
     * 控股股东类型
     */
    private String controllingShareholderType;

    /**
     * 联系人
     */
    private String contactName;

    /**
     * 联系电话
     */
    private String contactPhone;

    /**
     * 联系邮箱
     */
    private String contactEmail;

}

