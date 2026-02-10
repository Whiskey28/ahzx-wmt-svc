package com.wmt.module.credit.enums;

import com.wmt.framework.common.core.ArrayValuable;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

/**
 * 征信规则类型枚举
 *
 * @author AHC源码
 */
@Getter
@AllArgsConstructor
public enum CreditRuleTypeEnum implements ArrayValuable<String> {

    SUM("SUM", "求和"),
    AVG("AVG", "平均值"),
    MAX("MAX", "最大值"),
    MIN("MIN", "最小值"),
    FORMULA("FORMULA", "公式"),
    AGGREGATE("AGGREGATE", "聚合");

    public static final String[] ARRAYS = Arrays.stream(values()).map(CreditRuleTypeEnum::getType).toArray(String[]::new);

    /**
     * 规则类型
     */
    private final String type;
    /**
     * 类型名称
     */
    private final String name;

    @Override
    public String[] array() {
        return ARRAYS;
    }

}
