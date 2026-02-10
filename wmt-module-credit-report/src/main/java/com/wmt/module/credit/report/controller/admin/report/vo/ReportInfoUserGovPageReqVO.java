package com.wmt.module.credit.report.controller.admin.report.vo;

import com.wmt.framework.common.pojo.SortablePageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * 信息使用者政府机构明细分页查询 Request VO
 *
 * @author Auto
 */
@Schema(description = "管理后台 - 信息使用者政府机构明细分页查询 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class ReportInfoUserGovPageReqVO extends SortablePageParam {

    @Schema(description = "政府机构名称（模糊匹配）", example = "某某局")
    private String govOrgName;

    @Schema(description = "是否当前提供服务（0-否，1-是）", example = "1")
    private Integer isCurrentService;
}

