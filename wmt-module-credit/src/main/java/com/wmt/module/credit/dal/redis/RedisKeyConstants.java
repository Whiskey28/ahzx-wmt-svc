package com.wmt.module.credit.dal.redis;

/**
 * Credit Redis Key 枚举类
 *
 * @author AHC源码
 */
public interface RedisKeyConstants {

    /**
     * 字段配置的缓存
     *
     * KEY 格式：credit:field_config:{id}
     * VALUE 数据类型：String(JSON) {@link com.wmt.module.credit.dal.dataobject.field.CreditFieldConfigDO}
     */
    String FIELD_CONFIG = "credit:field_config:%d";

    /**
     * 表单数据的缓存
     *
     * KEY 格式：credit:form_data:{deptId}:{reportPeriod}:{reportType}
     * VALUE 数据类型：String(JSON) {@link com.wmt.module.credit.dal.dataobject.form.CreditFormDataDO}
     */
    String FORM_DATA = "credit:form_data:%d:%s:%s";

    /**
     * 计算规则的缓存
     *
     * KEY 格式：credit:calculation_rule:{reportType}
     * VALUE 数据类型：String(JSON Array) {@link com.wmt.module.credit.dal.dataobject.calculation.CreditCalculationRuleDO} 列表
     */
    String CALCULATION_RULE = "credit:calculation_rule:%s";

    /**
     * 汇总报表的缓存
     *
     * KEY 格式：credit:summary_report:{reportPeriod}:{reportType}
     * VALUE 数据类型：String(JSON) {@link com.wmt.module.credit.dal.dataobject.report.CreditSummaryReportDO}
     */
    String SUMMARY_REPORT = "credit:summary_report:%s:%s";

}
