package com.wmt.module.ticket.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@Getter
@AllArgsConstructor
public enum TicketStatusEnum {

    CREATED(10, "待处理"),
    PROCESSING(20, "处理中"),
    CLOSED(40, "已关闭");

    private final Integer status;
    private final String name;

    public static boolean isValid(Integer status) {
        return Arrays.stream(values()).anyMatch(item -> item.status.equals(status));
    }
}
