package com.wmt.module.credit.dal.dataobject.validation;

import com.wmt.framework.mybatis.core.dataobject.BaseDO;
import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

/**
 * 征信校验规则配置 DO
 *
 * @author AHC源码
 */
@TableName("credit_validation_rule")
@KeySequence("credit_validation_rule_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreditValidationRuleDO extends BaseDO {

    /**
     * 规则编号
     */
    @TableId
    private Long id;
    /**
     * 字段编码
     */
    private String fieldCode;
    /**
     * 规则类型（REQUIRED-必填，RANGE-范围，REGEX-正则，CUSTOM-自定义）
     *
     * 枚举 {@link com.wmt.module.credit.enums.CreditValidationRuleTypeEnum}
     */
    private String ruleType;
    /**
     * 校验表达式
     */
    private String ruleExpression;
    /**
     * 错误提示信息
     */
    private String errorMessage;
    /**
     * 优先级
     */
    private Integer priority;
    /**
     * 状态（0-禁用，1-启用）
     *
     * 枚举 {@link com.wmt.framework.common.enums.CommonStatusEnum}
     */
    private Integer status;

}
