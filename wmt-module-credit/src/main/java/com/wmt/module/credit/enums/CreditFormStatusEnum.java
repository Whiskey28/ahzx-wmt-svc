package com.wmt.module.credit.enums;

import com.wmt.framework.common.core.ArrayValuable;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

/**
 * 征信表单状态枚举
 *
 * @author AHC源码
 */
@Getter
@AllArgsConstructor
public enum CreditFormStatusEnum implements ArrayValuable<Integer> {

    DRAFT(0, "草稿"),
    SUBMITTED(1, "已提交"),
    REPORTED(2, "已报送");

    public static final Integer[] ARRAYS = Arrays.stream(values()).map(CreditFormStatusEnum::getStatus).toArray(Integer[]::new);

    /**
     * 状态
     */
    private final Integer status;
    /**
     * 状态名
     */
    private final String name;

    @Override
    public Integer[] array() {
        return ARRAYS;
    }

    /**
     * 判断是否为草稿状态
     */
    public static boolean isDraft(Integer status) {
        return DRAFT.getStatus().equals(status);
    }

    /**
     * 判断是否为已提交状态
     */
    public static boolean isSubmitted(Integer status) {
        return SUBMITTED.getStatus().equals(status);
    }

    /**
     * 判断是否为已报送状态
     */
    public static boolean isReported(Integer status) {
        return REPORTED.getStatus().equals(status);
    }

}
