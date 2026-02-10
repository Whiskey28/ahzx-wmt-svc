package com.wmt.module.credit.framework.validator;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 征信校验结果
 *
 * @author AHC源码
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreditValidationResult {

    /**
     * 是否通过校验
     */
    private boolean passed;

    /**
     * 错误信息（校验失败时返回）
     */
    private String errorMessage;

    /**
     * 创建通过结果
     */
    public static CreditValidationResult success() {
        return new CreditValidationResult(true, null);
    }

    /**
     * 创建失败结果
     */
    public static CreditValidationResult failure(String errorMessage) {
        return new CreditValidationResult(false, errorMessage);
    }

}
