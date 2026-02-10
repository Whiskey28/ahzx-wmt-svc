package com.wmt.module.credit.dal.dataobject.audit;

import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * 征信审计日志 DO
 *
 * 注意：审计日志不继承BaseDO，因为审计日志不应该被删除
 *
 * @author AHC源码
 */
@TableName(value = "credit_audit_log", autoResultMap = true)
@KeySequence("credit_audit_log_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreditAuditLogDO {

    /**
     * 日志编号
     */
    @TableId
    private Long id;
    /**
     * 业务类型（FORM_DATA-表单数据，SUMMARY_REPORT-汇总报表）
     *
     * 枚举 {@link com.wmt.module.credit.enums.CreditBizTypeEnum}
     */
    private String bizType;
    /**
     * 业务编号
     */
    private Long bizId;
    /**
     * 操作类型（CREATE-创建，UPDATE-更新，SUBMIT-提交，CALCULATE-计算）
     *
     * 枚举 {@link com.wmt.module.credit.enums.CreditOperationTypeEnum}
     */
    private String operationType;
    /**
     * 操作用户编号
     *
     * 关联 {@link com.wmt.module.system.dal.dataobject.user.AdminUserDO#getId()}
     */
    private Long operationUserId;
    /**
     * 操作描述
     */
    private String operationDesc;
    /**
     * 变更前数据（JSONB格式）
     */
    @TableField(typeHandler = JacksonTypeHandler.class)
    private Map<String, Object> beforeData;
    /**
     * 变更后数据（JSONB格式）
     */
    @TableField(typeHandler = JacksonTypeHandler.class)
    private Map<String, Object> afterData;
    /**
     * 变更字段列表（逗号分隔）
     */
    private String changeFields;
    /**
     * IP地址
     */
    private String ipAddress;
    /**
     * 用户代理
     */
    private String userAgent;
    /**
     * 创建时间
     */
    private LocalDateTime createTime;
    /**
     * 租户编号
     */
    private Long tenantId;

}
