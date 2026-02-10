package com.wmt.module.credit.dal.dataobject.calculation;

import com.wmt.framework.mybatis.core.dataobject.BaseDO;
import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import lombok.*;

import java.util.Map;

/**
 * 征信计算规则配置 DO
 *
 * @author AHC源码
 */
@TableName(value = "credit_calculation_rule", autoResultMap = true)
@KeySequence("credit_calculation_rule_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreditCalculationRuleDO extends BaseDO {

    /**
     * 规则编号
     */
    @TableId
    private Long id;
    /**
     * 目标字段编码（计算结果字段）
     */
    private String targetFieldCode;
    /**
     * 目标字段名称（用于前端显示）
     */
    private String targetFieldName;
    /**
     * 报表类型（MONTHLY-月报，QUARTERLY-季报）
     *
     * 枚举 {@link com.wmt.module.credit.enums.CreditReportTypeEnum}
     */
    private String reportType;
    /**
     * 规则类型（SUM-求和，AVG-平均，FORMULA-公式，AGGREGATE-聚合）
     *
     * 枚举 {@link com.wmt.module.credit.enums.CreditRuleTypeEnum}
     */
    private String ruleType;
    /**
     * 计算表达式（SpEL表达式或SQL片段）
     */
    private String ruleExpression;
    /**
     * 数据来源（JSON格式，定义从哪些字段或报表获取数据）
     */
    @TableField(typeHandler = JacksonTypeHandler.class)
    private Map<String, Object> dataSource;
    /**
     * 计算顺序
     */
    private Integer calculationOrder;
    /**
     * 规则描述
     */
    private String description;
    /**
     * 前端展示配置（JSONB格式，存储字段分组、布局、样式等展示信息）
     * 例如：{"groupName": "金融、类金融机构", "groupOrder": 1, "columnType": "total_cumulative", ...}
     */
    @TableField(typeHandler = JacksonTypeHandler.class)
    private Map<String, Object> displayConfig;
    /**
     * 状态（0-禁用，1-启用）
     *
     * 枚举 {@link com.wmt.framework.common.enums.CommonStatusEnum}
     */
    private Integer status;

}
