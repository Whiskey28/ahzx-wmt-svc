package com.wmt.module.credit.report.framework.config.vo;

import lombok.Data;

/**
 * 部门选项 VO
 * 用于积木报表部门组件
 *
 * @author Auto
 */
@Data
public class DeptOptionsVO {
    /**
     * 部门ID
     */
    private String id;

    /**
     * 部门名称
     */
    private String name;

    /**
     * 父部门ID
     */
    private String parentId;
}
