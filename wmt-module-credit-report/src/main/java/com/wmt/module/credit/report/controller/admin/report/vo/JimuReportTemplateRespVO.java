package com.wmt.module.credit.report.controller.admin.report.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 积木报表模板 Response VO
 *
 * @author AHC源码
 */
@Schema(description = "管理后台 - 积木报表模板 Response VO")
@Data
public class JimuReportTemplateRespVO {

    @Schema(description = "报表模板id", requiredMode = Schema.RequiredMode.REQUIRED, example = "1173147297280667648")
    private String id;

    @Schema(description = "编码", example = "20210108104603__0642")
    private String code;

    @Schema(description = "名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "实习证明副本0642")
    private String name;

    @Schema(description = "说明", example = "报表说明")
    private String note;

    @Schema(description = "创建时间")
    private LocalDateTime createTime;

    @Schema(description = "更新时间")
    private LocalDateTime updateTime;

}
