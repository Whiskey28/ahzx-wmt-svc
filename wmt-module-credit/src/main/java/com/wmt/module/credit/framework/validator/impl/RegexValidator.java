package com.wmt.module.credit.framework.validator.impl;

import com.wmt.module.credit.dal.dataobject.validation.CreditValidationRuleDO;
import com.wmt.module.credit.enums.CreditValidationRuleTypeEnum;
import com.wmt.module.credit.framework.validator.CreditValidationResult;
import com.wmt.module.credit.framework.validator.CreditValidator;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.regex.Pattern;

/**
 * 正则表达式校验器
 * <p>
 * 规则表达式格式：正则表达式字符串，例如：^[0-9]+$ 或 ^[A-Za-z]+$
 *
 * @author AHC源码
 */
@Component
public class RegexValidator implements CreditValidator {

    @Override
    public boolean supports(String ruleType) {
        return CreditValidationRuleTypeEnum.REGEX.getType().equals(ruleType);
    }

    @Override
    public CreditValidationResult validate(String fieldCode, Object value, CreditValidationRuleDO rule, Map<String, Object> formData) {
        if (value == null) {
            // 空值由RequiredValidator处理，这里直接通过
            return CreditValidationResult.success();
        }

        // 转换为字符串
        String strValue = value.toString();

        // 编译正则表达式
        Pattern pattern;
        try {
            pattern = Pattern.compile(rule.getRuleExpression());
        } catch (Exception e) {
            return CreditValidationResult.failure(String.format("字段 %s 的正则表达式配置错误：%s", fieldCode, e.getMessage()));
        }

        // 执行匹配
        if (!pattern.matcher(strValue).matches()) {
            String errorMessage = rule.getErrorMessage() != null && !rule.getErrorMessage().isEmpty()
                    ? rule.getErrorMessage()
                    : String.format("字段 %s 的值不符合格式要求", fieldCode);
            return CreditValidationResult.failure(errorMessage);
        }

        return CreditValidationResult.success();
    }

}
