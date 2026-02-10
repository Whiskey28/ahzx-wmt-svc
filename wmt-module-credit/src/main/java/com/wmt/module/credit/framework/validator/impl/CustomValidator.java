package com.wmt.module.credit.framework.validator.impl;

import com.wmt.module.credit.dal.dataobject.validation.CreditValidationRuleDO;
import com.wmt.module.credit.enums.CreditValidationRuleTypeEnum;
import com.wmt.module.credit.framework.validator.CreditValidationResult;
import com.wmt.module.credit.framework.validator.CreditValidator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * 自定义校验器（使用SpEL表达式）
 * <p>
 * 规则表达式格式：SpEL表达式，例如：value > 0 或 value != null && value.length() > 0
 * <p>
 * 上下文变量：
 * - value: 当前字段值
 * - fieldCode: 当前字段编码
 * - formData: 整个表单数据Map
 *
 * @author AHC源码
 */
@Component
@Slf4j
public class CustomValidator implements CreditValidator {

    private final ExpressionParser parser = new SpelExpressionParser();

    @Override
    public boolean supports(String ruleType) {
        return CreditValidationRuleTypeEnum.CUSTOM.getType().equals(ruleType);
    }

    @Override
    public CreditValidationResult validate(String fieldCode, Object value, CreditValidationRuleDO rule, Map<String, Object> formData) {
        try {
            // 构建SpEL上下文
            EvaluationContext context = new StandardEvaluationContext();
            context.setVariable("value", value);
            context.setVariable("fieldCode", fieldCode);
            context.setVariable("formData", formData != null ? formData : Map.of());

            // 解析并执行SpEL表达式
            Expression expression = parser.parseExpression(rule.getRuleExpression());
            Object result = expression.getValue(context);

            // 判断结果（SpEL表达式应该返回boolean值）
            if (result instanceof Boolean) {
                if (Boolean.TRUE.equals(result)) {
                    return CreditValidationResult.success();
                } else {
                    String errorMessage = rule.getErrorMessage() != null && !rule.getErrorMessage().isEmpty()
                            ? rule.getErrorMessage()
                            : String.format("字段 %s 的自定义校验失败", fieldCode);
                    return CreditValidationResult.failure(errorMessage);
                }
            } else {
                log.warn("自定义校验表达式 {} 的返回值不是boolean类型，返回值：{}", rule.getRuleExpression(), result);
                return CreditValidationResult.failure(String.format("字段 %s 的自定义校验表达式返回值类型错误", fieldCode));
            }
        } catch (Exception e) {
            log.error("执行自定义校验表达式失败：{}", rule.getRuleExpression(), e);
            return CreditValidationResult.failure(String.format("字段 %s 的自定义校验执行失败：%s", fieldCode, e.getMessage()));
        }
    }

}
