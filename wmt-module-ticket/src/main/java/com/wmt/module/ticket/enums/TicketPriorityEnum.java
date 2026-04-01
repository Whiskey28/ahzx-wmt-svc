package com.wmt.module.ticket.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum TicketPriorityEnum {

    LOW(10, "µÍ"),
    MEDIUM(20, "ÖÐ"),
    HIGH(30, "¸ß"),
    URGENT(40, "½ô¼±");

    private final Integer type;
    private final String name;
}
