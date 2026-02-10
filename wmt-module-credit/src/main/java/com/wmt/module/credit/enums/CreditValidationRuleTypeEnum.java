package com.wmt.module.credit.enums;

import com.wmt.framework.common.core.ArrayValuable;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

/**
 * 征信校验规则类型枚举
 *
 * @author AHC源码
 */
@Getter
@AllArgsConstructor
public enum CreditValidationRuleTypeEnum implements ArrayValuable<String> {

    REQUIRED("REQUIRED", "必填"),
    RANGE("RANGE", "范围"),
    REGEX("REGEX", "正则"),
    CUSTOM("CUSTOM", "自定义");

    public static final String[] ARRAYS = Arrays.stream(values()).map(CreditValidationRuleTypeEnum::getType).toArray(String[]::new);

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
