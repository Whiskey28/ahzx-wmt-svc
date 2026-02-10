package com.wmt.module.credit.enums;

import com.wmt.framework.common.core.ArrayValuable;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

/**
 * 征信业务类型枚举
 *
 * @author AHC源码
 */
@Getter
@AllArgsConstructor
public enum CreditBizTypeEnum implements ArrayValuable<String> {

    FORM_DATA("FORM_DATA", "表单数据"),
    SUMMARY_REPORT("SUMMARY_REPORT", "汇总报表");

    public static final String[] ARRAYS = Arrays.stream(values()).map(CreditBizTypeEnum::getType).toArray(String[]::new);

    /**
     * 业务类型
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
