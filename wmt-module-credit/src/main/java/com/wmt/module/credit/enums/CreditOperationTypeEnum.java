package com.wmt.module.credit.enums;

import com.wmt.framework.common.core.ArrayValuable;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

/**
 * 征信操作类型枚举
 *
 * @author AHC源码
 */
@Getter
@AllArgsConstructor
public enum CreditOperationTypeEnum implements ArrayValuable<String> {

    CREATE("CREATE", "创建"),
    UPDATE("UPDATE", "更新"),
    DELETE("DELETE", "删除"),
    SUBMIT("SUBMIT", "提交"),
    CALCULATE("CALCULATE", "计算"),
    EXPORT("EXPORT", "导出");

    public static final String[] ARRAYS = Arrays.stream(values()).map(CreditOperationTypeEnum::getType).toArray(String[]::new);

    /**
     * 操作类型
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
