package com.wmt.module.credit.report.controller.admin.report.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 积木报表 - 信息使用者机构下拉树节点 VO
 *
 * 对应积木报表下拉树组件期望的字段：
 * - id：节点唯一标识
 * - parentId：父节点标识
 * - departName：显示名称
 * - isLeaf：是否叶子节点（0-否，1-是）
 *
 * @author Auto
 */
@Schema(description = "积木报表 - 信息使用者机构下拉树节点 VO")
@Data
public class JmReportInfoUserTreeNodeRespVO {

    @Schema(description = "节点唯一标识", example = "root_info_user")
    private String id;

    @Schema(description = "父节点标识，根节点为空字符串", example = "")
    private String parentId;

    @Schema(description = "节点显示名称", example = "产品与服务提供情况")
    private String departName;

    @Schema(description = "是否叶子节点（0-否，1-是）", example = "0")
    private Integer isLeaf;
}

