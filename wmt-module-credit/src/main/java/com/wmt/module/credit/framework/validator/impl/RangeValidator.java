package com.wmt.module.credit.framework.validator.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wmt.module.credit.dal.dataobject.validation.CreditValidationRuleDO;
import com.wmt.module.credit.enums.CreditValidationRuleTypeEnum;
import com.wmt.module.credit.framework.validator.CreditValidationResult;
import com.wmt.module.credit.framework.validator.CreditValidator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Map;

/**
 * 范围校验器
 * <p>
 * 规则表达式格式：JSON格式，例如：{"min": 0, "max": 100} 或 {"min": 0} 或 {"max": 100}
 *
 * @author AHC源码
 */
@Component
@Slf4j
public class RangeValidator implements CreditValidator {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public boolean supports(String ruleType) {
        return CreditValidationRuleTypeEnum.RANGE.getType().equals(ruleType);
    }

    @Override
    public CreditValidationResult validate(String fieldCode, Object value, CreditValidationRuleDO rule, Map<String, Object> formData) {
        if (value == null) {
            // 空值由RequiredValidator处理，这里直接通过
            return CreditValidationResult.success();
        }

        // 解析规则表达式
        Map<String, Object> rangeConfig;
        try {
            rangeConfig = objectMapper.readValue(rule.getRuleExpression(), new TypeReference<Map<String, Object>>() {});
        } catch (Exception e) {
            log.error("解析范围校验规则表达式失败：{}", rule.getRuleExpression(), e);
            return CreditValidationResult.failure(String.format("字段 %s 的范围校验规则配置错误", fieldCode));
        }

        // 转换为数值
        BigDecimal numValue;
        try {
            if (value instanceof Number) {
                numValue = new BigDecimal(value.toString());
            } else if (value instanceof String) {
                numValue = new BigDecimal((String) value);
            } else {
                return CreditValidationResult.failure(String.format("字段 %s 的值不是数值类型", fieldCode));
            }
        } catch (NumberFormatException e) {
            return CreditValidationResult.failure(String.format("字段 %s 的值不是有效的数值", fieldCode));
        }

        // 校验最小值
        if (rangeConfig.containsKey("min")) {
            BigDecimal min = new BigDecimal(rangeConfig.get("min").toString());
            if (numValue.compareTo(min) < 0) {
                String errorMessage = rule.getErrorMessage() != null && !rule.getErrorMessage().isEmpty()
                        ? rule.getErrorMessage()
                        : String.format("字段 %s 的值不能小于 %s", fieldCode, min);
                return CreditValidationResult.failure(errorMessage);
            }
        }

        // 校验最大值
        if (rangeConfig.containsKey("max")) {
            BigDecimal max = new BigDecimal(rangeConfig.get("max").toString());
            if (numValue.compareTo(max) > 0) {
                String errorMessage = rule.getErrorMessage() != null && !rule.getErrorMessage().isEmpty()
                        ? rule.getErrorMessage()
                        : String.format("字段 %s 的值不能大于 %s", fieldCode, max);
                return CreditValidationResult.failure(errorMessage);
            }
        }

        return CreditValidationResult.success();
    }

}
