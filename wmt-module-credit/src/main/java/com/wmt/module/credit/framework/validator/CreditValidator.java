package com.wmt.module.credit.framework.validator;

import com.wmt.module.credit.dal.dataobject.validation.CreditValidationRuleDO;

/**
 * 征信校验器接口
 *
 * @author AHC源码
 */
public interface CreditValidator {

    /**
     * 是否支持该规则类型
     *
     * @param ruleType 规则类型
     * @return 是否支持
     */
    boolean supports(String ruleType);

    /**
     * 对单个字段进行校验
     *
     * @param fieldCode 字段编码
     * @param value     字段值
     * @param rule      校验规则
     * @param formData  表单数据（用于自定义校验时访问其他字段）
     * @return 校验结果
     */
    CreditValidationResult validate(String fieldCode, Object value, CreditValidationRuleDO rule, java.util.Map<String, Object> formData);

}
