package com.wmt.module.pfb.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * {@link PfbCreditCodeUtils} 纯逻辑单测（无 Spring）。
 */
class PfbCreditCodeUtilsTest {

    @Test
    void isValid_acceptsUppercaseAlphanumeric18() {
        assertTrue(PfbCreditCodeUtils.isValid("91110000100000001X"));
        assertTrue(PfbCreditCodeUtils.isValid("91110000100000001x"));
    }

    @Test
    void isValid_rejectsWrongLengthOrChars() {
        assertFalse(PfbCreditCodeUtils.isValid(null));
        assertFalse(PfbCreditCodeUtils.isValid(""));
        assertFalse(PfbCreditCodeUtils.isValid("91110000100000001"));
        assertFalse(PfbCreditCodeUtils.isValid("91110000100000001XY"));
        assertFalse(PfbCreditCodeUtils.isValid("91110000100000001-"));
    }
}
