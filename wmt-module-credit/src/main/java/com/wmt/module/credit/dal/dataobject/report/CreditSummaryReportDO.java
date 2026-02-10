package com.wmt.module.credit.dal.dataobject.report;

import com.wmt.framework.mybatis.core.dataobject.BaseDO;
import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import com.wmt.module.credit.framework.mybatis.JsonbTypeHandler;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * 征信汇总报表 DO
 *
 * @author AHC源码
 */
@TableName(value = "credit_summary_report", autoResultMap = true)
@KeySequence("credit_summary_report_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreditSummaryReportDO extends BaseDO {

    /**
     * 报表编号
     */
    @TableId
    private Long id;
    /**
     * 报送周期
     */
    private String reportPeriod;
    /**
     * 报表类型（MONTHLY-月报，QUARTERLY-季报）
     *
     * 枚举 {@link com.wmt.module.credit.enums.CreditReportTypeEnum}
     */
    private String reportType;
    /**
     * 报表数据（JSONB格式，存储所有汇总字段值）
     */
    @TableField(typeHandler = JsonbTypeHandler.class)
    private Map<String, Object> reportData;
    /**
     * 计算日志（记录计算过程）
     */
    private String calculationLog;
    /**
     * 状态（0-计算中，1-已完成，2-已报送）
     *
     * 枚举 {@link com.wmt.module.credit.enums.CreditReportStatusEnum}
     */
    private Integer status;
    /**
     * 生成时间
     */
    private LocalDateTime generateTime;

}
