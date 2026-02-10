package com.wmt.module.credit.enums;

import com.wmt.framework.common.core.ArrayValuable;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

/**
 * 征信报表类型枚举
 *
 * @author AHC源码
 */
@Getter
@AllArgsConstructor
public enum CreditReportTypeEnum implements ArrayValuable<String> {

    MONTHLY("MONTHLY", "月报"),
    QUARTERLY("QUARTERLY", "季报");

    public static final String[] ARRAYS = Arrays.stream(values()).map(CreditReportTypeEnum::getType).toArray(String[]::new);

    /**
     * 报表类型
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

    /**
     * 判断是否为月报
     */
    public static boolean isMonthly(String type) {
        return MONTHLY.getType().equals(type);
    }

    /**
     * 判断是否为季报
     */
    public static boolean isQuarterly(String type) {
        return QUARTERLY.getType().equals(type);
    }

}
