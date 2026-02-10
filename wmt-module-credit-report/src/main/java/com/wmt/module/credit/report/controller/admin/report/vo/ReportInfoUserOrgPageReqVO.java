package com.wmt.module.credit.report.controller.admin.report.vo;

import com.wmt.framework.common.pojo.SortablePageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * 信息使用者机构明细分页查询 Request VO
 *
 * @author Auto
 */
@Schema(description = "管理后台 - 信息使用者机构明细分页查询 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class ReportInfoUserOrgPageReqVO extends SortablePageParam {

    @Schema(description = "机构名称（模糊匹配）", example = "某某公司")
    private String orgName;

    @Schema(description = "行业代码（industry_code 字典 item_value）", example = "bank")
    private String industryCode;

    @Schema(description = "是否当前提供服务（0-否，1-是）", example = "1")
    private Integer isCurrentService;
}

