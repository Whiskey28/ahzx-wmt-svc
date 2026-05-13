package com.wmt.module.pfb.util;

import cn.hutool.core.util.StrUtil;

import java.util.regex.Pattern;

/**
 * 统一社会信用代码（18 位）基础格式校验。
 */
public final class PfbCreditCodeUtils {

    private static final Pattern PATTERN = Pattern.compile("^[0-9A-Z]{18}$");

    private PfbCreditCodeUtils() {
    }

    public static boolean isValid(String code) {
        if (StrUtil.isBlank(code) || code.length() != 18) {
            return false;
        }
        return PATTERN.matcher(code.trim().toUpperCase()).matches();
    }
}
