package com.wmt.module.credit.report.framework.jmreport.vo;

import lombok.Data;

import java.util.List;

/**
 * 选项结果包装类
 * 用于积木报表选项数据返回
 *
 * @author Auto
 */
@Data
public class OptionsResult<T> {
    /**
     * 是否成功
     */
    private Boolean success;
    
    /**
     * 错误信息
     */
    private String message;
    
    /**
     * 数据列表
     */
    private List<T> data;
    
    /**
     * 成功返回
     */
    public static <T> OptionsResult<T> success(List<T> data) {
        OptionsResult<T> result = new OptionsResult<>();
        result.setSuccess(true);
        result.setData(data);
        return result;
    }
    
    /**
     * 错误返回
     */
    public static <T> OptionsResult<T> error(String message) {
        OptionsResult<T> result = new OptionsResult<>();
        result.setSuccess(false);
        result.setMessage(message);
        return result;
    }
}
