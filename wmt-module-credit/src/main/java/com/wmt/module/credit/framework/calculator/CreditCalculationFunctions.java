package com.wmt.module.credit.framework.calculator;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 征信计算自定义函数
 * 用于在SpEL表达式中调用
 *
 * @author AHC源码
 */
public class CreditCalculationFunctions {

    /**
     * 求和函数
     *
     * @param values 数值集合
     * @return 求和结果
     */
    public static BigDecimal sum(Collection<Number> values) {
        if (values == null || values.isEmpty()) {
            return BigDecimal.ZERO;
        }
        return values.stream()
                .map(v -> v == null ? BigDecimal.ZERO : new BigDecimal(v.toString()))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    /**
     * 平均值函数
     *
     * @param values 数值集合
     * @return 平均值
     */
    public static BigDecimal avg(Collection<Number> values) {
        if (values == null || values.isEmpty()) {
            return BigDecimal.ZERO;
        }
        List<BigDecimal> decimals = values.stream()
                .map(v -> v == null ? BigDecimal.ZERO : new BigDecimal(v.toString()))
                .collect(Collectors.toList());
        BigDecimal sum = decimals.stream().reduce(BigDecimal.ZERO, BigDecimal::add);
        return sum.divide(new BigDecimal(decimals.size()), 2, RoundingMode.HALF_UP);
    }

    /**
     * 最大值函数
     *
     * @param values 数值集合
     * @return 最大值
     */
    public static BigDecimal max(Collection<Number> values) {
        if (values == null || values.isEmpty()) {
            return BigDecimal.ZERO;
        }
        return values.stream()
                .map(v -> v == null ? BigDecimal.ZERO : new BigDecimal(v.toString()))
                .max(BigDecimal::compareTo)
                .orElse(BigDecimal.ZERO);
    }

    /**
     * 最小值函数
     *
     * @param values 数值集合
     * @return 最小值
     */
    public static BigDecimal min(Collection<Number> values) {
        if (values == null || values.isEmpty()) {
            return BigDecimal.ZERO;
        }
        return values.stream()
                .map(v -> v == null ? BigDecimal.ZERO : new BigDecimal(v.toString()))
                .min(BigDecimal::compareTo)
                .orElse(BigDecimal.ZERO);
    }

    /**
     * 从缓存获取字段值（预留接口，后续可扩展）
     *
     * @param deptId     部门ID
     * @param fieldCode  字段编码
     * @param period     周期
     * @return 字段值
     */
    public static Object getCachedField(Long deptId, String fieldCode, String period) {
        // TODO: 从Redis缓存获取字段值
        // 暂时返回null，后续实现缓存功能时再完善
        return null;
    }

    /**
     * 安全获取Map中的值（避免空指针）
     *
     * @param map  Map对象
     * @param key  键
     * @return 值，如果不存在返回0
     */
    public static BigDecimal safeGet(Map<String, Object> map, String key) {
        if (map == null || key == null) {
            return BigDecimal.ZERO;
        }
        Object value = map.get(key);
        if (value == null) {
            return BigDecimal.ZERO;
        }
        if (value instanceof Number) {
            return new BigDecimal(value.toString());
        }
        try {
            return new BigDecimal(value.toString());
        } catch (Exception e) {
            return BigDecimal.ZERO;
        }
    }

    /**
     * 安全除法函数（避免除零）
     *
     * @param dividend 被除数
     * @param divisor  除数
     * @return 除法结果，如果除数为0则返回0
     */
    public static BigDecimal safeDivide(Number dividend, Number divisor) {
        if (divisor == null || divisor.doubleValue() == 0) {
            return BigDecimal.ZERO;
        }
        BigDecimal dividendBD = dividend == null ? BigDecimal.ZERO : new BigDecimal(dividend.toString());
        BigDecimal divisorBD = new BigDecimal(divisor.toString());
        return dividendBD.divide(divisorBD, 4, RoundingMode.HALF_UP);
    }

}
