package com.wmt.module.credit.report.controller.admin.report.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 报表填报记录编辑URL Response VO
 *
 * @author AHC源码
 */
@Schema(description = "管理后台 - 报表填报记录编辑URL Response VO")
@Data
public class ReportFillRecordEditUrlRespVO {

    @Schema(description = "报表模板id（用于拼装URL）", requiredMode = Schema.RequiredMode.REQUIRED, example = "1173147297280667648")
    private String reportId;

    @Schema(description = "记录id（用于拼装URL，编辑时需要）", example = "9")
    private String recordId;

    @Schema(description = "编辑URL路径（不含token，前端自行拼装）", example = "/jmreport/view/1173147297280667648/edit/9")
    private String editUrl;

    @Schema(description = "查看URL路径（不含token，前端自行拼装）", example = "/jmreport/view/1173147297280667648")
    private String viewUrl;

}
