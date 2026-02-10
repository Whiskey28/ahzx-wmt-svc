package com.wmt.module.credit.dal.dataobject.field;

import com.wmt.framework.mybatis.core.dataobject.BaseDO;
import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import lombok.*;

import java.util.Map;

/**
 * 征信字段配置 DO
 *
 * @author AHC源码
 */
@TableName(value = "credit_field_config", autoResultMap = true)
@KeySequence("credit_field_config_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreditFieldConfigDO extends BaseDO {

    /**
     * 字段编号
     */
    @TableId
    private Long id;
    /**
     * 部门编号（0表示通用字段）
     *
     * 关联 {@link com.wmt.module.system.dal.dataobject.dept.DeptDO#getId()}
     */
    private Long deptId;
    /**
     * 字段编码（唯一标识）
     */
    private String fieldCode;
    /**
     * 字段名称
     */
    private String fieldName;
    /**
     * 字段类型（NUMBER/DECIMAL/TEXT/DATE等）
     *
     * 枚举 {@link com.wmt.module.credit.enums.CreditFieldTypeEnum}
     */
    private String fieldType;
    /**
     * 是否必填（0-否，1-是）
     */
    private Boolean required;
    /**
     * 默认值
     */
    private String defaultValue;
    /**
     * 校验规则（JSON格式）
     */
    @TableField(typeHandler = JacksonTypeHandler.class)
    private Map<String, Object> validationRule;
    /**
     * 前端展示配置（JSONB格式，存储字段分组、布局、样式等展示信息）
     * 例如：{"groupName": "市场部填写", "groupOrder": 1, "layout": {...}, ...}
     */
    @TableField(typeHandler = JacksonTypeHandler.class)
    private Map<String, Object> displayConfig;
    /**
     * 显示顺序
     */
    private Integer displayOrder;
    /**
     * 状态（0-禁用，1-启用）
     *
     * 枚举 {@link com.wmt.framework.common.enums.CommonStatusEnum}
     */
    private Integer status;

}
