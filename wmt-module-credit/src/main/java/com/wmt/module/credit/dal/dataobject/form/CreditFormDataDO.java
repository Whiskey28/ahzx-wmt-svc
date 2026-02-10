package com.wmt.module.credit.dal.dataobject.form;

import com.wmt.framework.mybatis.core.dataobject.BaseDO;
import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * 征信表单数据 DO
 *
 * @author AHC源码
 */
@TableName(value = "credit_form_data", autoResultMap = true)
@KeySequence("credit_form_data_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreditFormDataDO extends BaseDO {

    /**
     * 表单编号
     */
    @TableId
    private Long id;
    /**
     * 部门编号
     *
     * 关联 {@link com.wmt.module.system.dal.dataobject.dept.DeptDO#getId()}
     */
    private Long deptId;
    /**
     * 报送周期（格式：YYYY-MM或YYYY-Q1/Q2/Q3/Q4）
     */
    private String reportPeriod;
    /**
     * 报表类型（MONTHLY-月报，QUARTERLY-季报）
     *
     * 枚举 {@link com.wmt.module.credit.enums.CreditReportTypeEnum}
     */
    private String reportType;
    /**
     * 表单数据（JSONB格式，存储所有字段值）
     */
    @TableField(typeHandler = JacksonTypeHandler.class)
    private Map<String, Object> formData;
    /**
     * 状态（0-草稿，1-已提交，2-已报送）
     *
     * 枚举 {@link com.wmt.module.credit.enums.CreditFormStatusEnum}
     */
    private Integer status;
    /**
     * 提交人编号
     *
     * 关联 {@link com.wmt.module.system.dal.dataobject.user.AdminUserDO#getId()}
     */
    private Long submitUserId;
    /**
     * 提交时间
     */
    private LocalDateTime submitTime;

}
