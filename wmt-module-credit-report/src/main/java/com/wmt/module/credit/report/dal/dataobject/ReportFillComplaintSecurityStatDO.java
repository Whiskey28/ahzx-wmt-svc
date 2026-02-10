package com.wmt.module.credit.report.dal.dataobject;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.wmt.framework.mybatis.core.dataobject.BaseDO;
import lombok.*;

import java.math.BigDecimal;

/**
 * 报表填报 - 异议投诉处理与信息安全情况 DO（1:1 关联主记录）
 *
 * @author AHC源码
 */
@TableName("report_fill_complaint_security_stat")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReportFillComplaintSecurityStatDO extends BaseDO {

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
     * 企业投诉处理次数
     */
    private BigDecimal complaintHandleCount;

    /**
     * 异议受理次数
     */
    private BigDecimal disputeHandleCount;

    /**
     * 异议核实后准确的数量
     */
    private BigDecimal disputeConfirmedCount;

    /**
     * 异议核实后更正或删除的数量
     */
    private BigDecimal disputeCorrectedCount;

    /**
     * 异议核实无法确认的数量
     */
    private BigDecimal disputeUnconfirmedCount;

    /**
     * 发生信息安全事件数
     */
    private BigDecimal securityIncidentCount;

    /**
     * 发生信息泄露事件数
     */
    private BigDecimal leakIncidentCount;

    /**
     * 信息泄露的企业数
     */
    private BigDecimal leakEnterpriseCount;

    /**
     * 信息泄露的董事、监事、高级管理人员数
     */
    private BigDecimal leakPersonCount;

}

