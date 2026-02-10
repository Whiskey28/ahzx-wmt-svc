package com.wmt.module.credit.framework.calculator;

import com.wmt.module.credit.dal.dataobject.calculation.CreditCalculationRuleDO;
import com.wmt.module.credit.enums.ErrorCodeConstants;
import com.wmt.module.credit.service.calculation.CreditCalculationRuleService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.wmt.framework.common.exception.util.ServiceExceptionUtil.exception;

/**
 * 征信计算引擎核心类
 * 负责执行SpEL表达式计算
 *
 * @author AHC源码
 */
@Service
@Slf4j
public class CreditCalculationEngine {

    @Resource
    private CreditCalculationRuleService calculationRuleService;

    @Resource
    private CreditCalculationContextBuilder contextBuilder;

    private final ExpressionParser parser = new SpelExpressionParser();

    /**
     * 计算汇总报表
     *
     * @param reportPeriod 报送周期
     * @param reportType   报表类型
     * @return 计算结果Map，格式：Map<字段编码, 字段值>
     */
    public Map<String, Object> calculateSummaryReport(String reportPeriod, String reportType) {
        // 1. 获取计算规则列表（按计算顺序排序）
        List<CreditCalculationRuleDO> rules = calculationRuleService.getRulesByReportType(reportType);
        if (rules.isEmpty()) {
            log.warn("报表类型 {} 没有配置计算规则", reportType);
            return new HashMap<>();
        }

        // 2. 构建计算上下文
        EvaluationContext context = contextBuilder.buildContext(reportPeriod, reportType);

        // 3. 执行计算规则
        Map<String, Object> result = new HashMap<>();
        StringBuilder calculationLog = new StringBuilder();
        calculationLog.append("开始计算报表，周期：").append(reportPeriod).append("，类型：").append(reportType).append("\n");

        for (CreditCalculationRuleDO rule : rules) {
            try {
                // 执行单个计算规则
                Object value = executeCalculationRule(rule, context, result);
                result.put(rule.getTargetFieldCode(), value);
                calculationLog.append("字段：").append(rule.getTargetFieldCode())
                        .append("，表达式：").append(rule.getRuleExpression())
                        .append("，结果：").append(value).append("\n");
            } catch (Exception e) {
                String errorMsg = String.format("计算字段 %s 失败：%s", rule.getTargetFieldCode(), e.getMessage());
                log.error(errorMsg, e);
                calculationLog.append("错误：").append(errorMsg).append("\n");
                throw exception(ErrorCodeConstants.CALCULATION_EXECUTE_FAIL, errorMsg);
            }
        }

        calculationLog.append("计算完成\n");
        log.info("计算完成，周期：{}，类型：{}，结果字段数：{}", reportPeriod, reportType, result.size());
        log.debug("计算日志：\n{}", calculationLog);

        return result;
    }

    /**
     * 执行单个计算规则
     *
     * @param rule    计算规则
     * @param context SpEL上下文
     * @param result  已计算的结果（用于在表达式中引用已计算的字段）
     * @return 计算结果
     */
    private Object executeCalculationRule(CreditCalculationRuleDO rule, EvaluationContext context, Map<String, Object> result) {
        // 将已计算的结果添加到上下文中，以便在表达式中引用
        context.setVariable("result", result);

        // 解析SpEL表达式
        Expression expression = parser.parseExpression(rule.getRuleExpression());

        // 执行表达式
        Object value = expression.getValue(context);

        // 如果是数值类型，转换为BigDecimal统一处理
        if (value instanceof Number) {
            return new BigDecimal(value.toString());
        }

        return value;
    }

    /**
     * 校验计算结果
     *
     * @param result 计算结果
     * @return 校验是否通过
     */
    public boolean validateCalculationResult(Map<String, Object> result) {
        // TODO: 实现计算结果校验逻辑
        // 例如：检查必填字段是否有值，数值范围校验等
        return true;
    }

    /**
     * 验证SpEL表达式语法
     *
     * @param expression SpEL表达式
     * @return 是否有效
     */
    public boolean validateExpression(String expression) {
        try {
            Expression expr = parser.parseExpression(expression);
            // 创建一个空的上下文来验证表达式语法
            EvaluationContext context = contextBuilder.buildContext("2024-01", "MONTHLY");
            // 尝试解析，不执行
            return true;
        } catch (Exception e) {
            log.warn("SpEL表达式语法错误：{}", expression, e);
            return false;
        }
    }

}
