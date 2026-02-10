package com.wmt.module.credit.enums;

import com.wmt.framework.common.core.ArrayValuable;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

/**
 * 征信报表状态枚举
 *
 * @author AHC源码
 */
@Getter
@AllArgsConstructor
public enum CreditReportStatusEnum implements ArrayValuable<Integer> {

    CALCULATING(0, "计算中"),
    COMPLETED(1, "已完成"),
    REPORTED(2, "已报送");

    public static final Integer[] ARRAYS = Arrays.stream(values()).map(CreditReportStatusEnum::getStatus).toArray(Integer[]::new);

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
     * 判断是否为计算中状态
     */
    public static boolean isCalculating(Integer status) {
        return CALCULATING.getStatus().equals(status);
    }

    /**
     * 判断是否为已完成状态
     */
    public static boolean isCompleted(Integer status) {
        return COMPLETED.getStatus().equals(status);
    }

    /**
     * 判断是否为已报送状态
     */
    public static boolean isReported(Integer status) {
        return REPORTED.getStatus().equals(status);
    }

    /**
     * 根据状态值获取枚举
     *
     * @param status 状态值
     * @return 枚举
     */
    public static CreditReportStatusEnum fromStatus(Integer status) {
        for (CreditReportStatusEnum statusEnum : values()) {
            if (statusEnum.getStatus().equals(status)) {
                return statusEnum;
            }
        }
        return null;
    }

}
