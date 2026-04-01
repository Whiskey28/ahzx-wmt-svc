package com.wmt.module.ticket.service.bpm.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class BpmProcessDefinitionKeyUtilsTest {

    @Test
    void normalize_fullId_returnsKey() {
        assertEquals("ticket_lab_xor",
                BpmProcessDefinitionKeyUtils.normalize("ticket_lab_xor:1:b8dfb392-1b5e-11f1-b5cf-dc46288cdbc3"));
    }

    @Test
    void normalize_plainKey_unchanged() {
        assertEquals("ticket_lab_xor", BpmProcessDefinitionKeyUtils.normalize("ticket_lab_xor"));
    }
}
