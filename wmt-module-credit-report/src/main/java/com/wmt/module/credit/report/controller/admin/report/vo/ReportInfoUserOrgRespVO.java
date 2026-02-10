package com.wmt.module.credit.report.controller.admin.report.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 信息使用者机构明细 Response VO
 *
 * @author Auto
 */
@Data
public class ReportInfoUserOrgRespVO {

    @Schema(description = "主键", example = "a8f1...")
    private String id;

    @Schema(description = "机构名称", example = "某某商业保理有限公司")
    private String orgName;

    @Schema(description = "行业代码（industry_code 字典 item_value）", example = "bank")
    private String industryCode;

    @Schema(description = "是否当前提供服务（0-否，1-是）", example = "1")
    private Integer isCurrentService;

    @Schema(description = "排序号", example = "1")
    private String sortNo;

    @Schema(description = "创建时间")
    private LocalDateTime createTime;
}

