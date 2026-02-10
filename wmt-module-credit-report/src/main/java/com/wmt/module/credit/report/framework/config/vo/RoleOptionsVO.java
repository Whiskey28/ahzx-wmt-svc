package com.wmt.module.credit.report.framework.config.vo;

import lombok.Data;

/**
 * 角色选项 VO
 * 用于积木报表角色组件
 *
 * @author Auto
 */
@Data
public class RoleOptionsVO {
    /**
     * 角色ID
     */
    private String id;

    /**
     * 角色名称
     */
    private String name;

    /**
     * 角色编码
     */
    private String code;
}
