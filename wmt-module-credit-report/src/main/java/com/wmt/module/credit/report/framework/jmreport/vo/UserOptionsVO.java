package com.wmt.module.credit.report.framework.jmreport.vo;

import lombok.Data;

/**
 * 用户选项 VO
 * 用于积木报表用户组件
 *
 * @author Auto
 */
@Data
public class UserOptionsVO {
    /**
     * 用户ID
     */
    private String id;
    
    /**
     * 用户名
     */
    private String username;
    
    /**
     * 用户昵称
     */
    private String nickname;
    
    /**
     * 部门ID
     */
    private String deptId;
}
