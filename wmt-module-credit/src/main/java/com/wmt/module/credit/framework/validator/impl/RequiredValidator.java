package com.wmt.module.credit.framework.validator.impl;

import com.wmt.module.credit.dal.dataobject.validation.CreditValidationRuleDO;
import com.wmt.module.credit.enums.CreditValidationRuleTypeEnum;
import com.wmt.module.credit.framework.validator.CreditValidationResult;
import com.wmt.module.credit.framework.validator.CreditValidator;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Map;

/**
 * 必填校验器
 *
 * @author AHC源码
 */
@Component
public class RequiredValidator implements CreditValidator {

    @Override
    public boolean supports(String ruleType) {
        return CreditValidationRuleTypeEnum.REQUIRED.getType().equals(ruleType);
    }

    @Override
    public CreditValidationResult validate(String fieldCode, Object value, CreditValidationRuleDO rule, Map<String, Object> formData) {
        // 判断值是否为空
        if (isBlank(value)) {
            String errorMessage = rule.getErrorMessage() != null && !rule.getErrorMessage().isEmpty()
                    ? rule.getErrorMessage()
                    : String.format("字段 %s 不能为空", fieldCode);
            return CreditValidationResult.failure(errorMessage);
        }
        return CreditValidationResult.success();
    }

    /**
     * 判断值是否为空
     */
    private boolean isBlank(Object value) {
        if (value == null) {
            return true;
        }
        if (value instanceof String) {
            return ((String) value).trim().isEmpty();
        }
        if (value instanceof Collection) {
            return ((Collection<?>) value).isEmpty();
        }
        if (value instanceof Map) {
            return ((Map<?, ?>) value).isEmpty();
        }
        if (value instanceof Object[]) {
            return ((Object[]) value).length == 0;
        }
        return false;
    }

}
