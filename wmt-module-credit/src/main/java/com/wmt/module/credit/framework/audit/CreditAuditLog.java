package com.wmt.module.credit.framework.audit;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 征信审计日志注解
 * <p>
 * 标注在Service方法上，用于记录审计日志
 *
 * @author AHC源码
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface CreditAuditLog {

    /**
     * 业务类型（FORM_DATA / SUMMARY_REPORT）
     */
    String bizType();

    /**
     * 操作类型（CREATE / UPDATE / DELETE / SUBMIT / CALCULATE）
     */
    String operationType();

    /**
     * 业务ID表达式（SpEL），例如 "#result" 或 "#id" 或 "#createReqVO.id"
     * <p>
     * 如果不指定，则从方法参数中查找id字段
     */
    String bizIdExpression() default "";

    /**
     * 操作描述表达式（SpEL），例如 "'创建表单数据，部门：' + #createReqVO.deptId"
     * <p>
     * 如果不指定，则使用默认描述
     */
    String operationDescExpression() default "";

}
