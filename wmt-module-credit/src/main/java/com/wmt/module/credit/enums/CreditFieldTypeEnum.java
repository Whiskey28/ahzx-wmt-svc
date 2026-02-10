package com.wmt.module.credit.enums;

import com.wmt.framework.common.core.ArrayValuable;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

/**
 * 征信字段类型枚举
 *
 * @author AHC源码
 */
@Getter
@AllArgsConstructor
public enum CreditFieldTypeEnum implements ArrayValuable<String> {

    NUMBER("NUMBER", "整数"),
    DECIMAL("DECIMAL", "小数"),
    TEXT("TEXT", "文本"),
    DATE("DATE", "日期"),
    DATETIME("DATETIME", "日期时间"),
    BOOLEAN("BOOLEAN", "布尔值");

    public static final String[] ARRAYS = Arrays.stream(values()).map(CreditFieldTypeEnum::getType).toArray(String[]::new);

    /**
     * 字段类型
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
