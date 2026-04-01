package com.wmt.module.ticket.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum TicketReplyFromTypeEnum {

    USER(10, "用户"),
    ADMIN(20, "管理员");

    private final Integer type;
    private final String name;
}
