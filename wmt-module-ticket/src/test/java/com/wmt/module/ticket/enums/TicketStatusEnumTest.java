package com.wmt.module.ticket.enums;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Smoke/unit tests for ticket enums (no Spring context).
 */
class TicketStatusEnumTest {

    @Test
    void isValid_acceptsDefinedStatuses() {
        assertTrue(TicketStatusEnum.isValid(TicketStatusEnum.CREATED.getStatus()));
        assertTrue(TicketStatusEnum.isValid(TicketStatusEnum.PROCESSING.getStatus()));
        assertTrue(TicketStatusEnum.isValid(TicketStatusEnum.CLOSED.getStatus()));
    }

    @Test
    void isValid_rejectsUnknownStatus() {
        assertFalse(TicketStatusEnum.isValid(-1));
    }
}
