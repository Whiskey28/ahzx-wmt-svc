package com.wmt.module.credit.report.controller.admin.report.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

/**
 * 积木报表 - 信息使用者机构下拉树响应 VO
 *
 * 对应积木报表下拉树组件示例返回结构：
 *
 * {
 *   "data": [ { ...节点... } ],
 *   "total": null
 * }
 *
 * @author Auto
 */
@Schema(description = "积木报表 - 信息使用者机构下拉树响应 VO")
@Data
public class JmReportInfoUserTreeRespVO {

    @Schema(description = "下拉树节点列表")
    private List<JmReportInfoUserTreeNodeRespVO> data;

    @Schema(description = "总数，可选字段。此处保持与官方示例一致，返回 null。")
    private Long total;
}

