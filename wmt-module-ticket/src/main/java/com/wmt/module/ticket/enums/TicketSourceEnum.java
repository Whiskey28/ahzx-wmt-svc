package com.wmt.module.ticket.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum TicketSourceEnum {

    APP(10, "APP"),
    ADMIN(20, "π‹¿Ì∂À");

    private final Integer type;
    private final String name;
}
