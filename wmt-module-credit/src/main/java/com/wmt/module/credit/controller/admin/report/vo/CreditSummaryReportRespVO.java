package com.wmt.module.credit.controller.admin.report.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * 征信汇总报表 Response VO
 *
 * @author AHC源码
 */
@Schema(description = "管理后台 - 征信汇总报表 Response VO")
@Data
public class CreditSummaryReportRespVO {

    @Schema(description = "报表编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    private Long id;

    @Schema(description = "报送周期", requiredMode = Schema.RequiredMode.REQUIRED, example = "2024-01")
    private String reportPeriod;

    @Schema(description = "报表类型", requiredMode = Schema.RequiredMode.REQUIRED, example = "MONTHLY")
    private String reportType;

    @Schema(description = "报表数据（JSON格式）")
    private Map<String, Object> reportData;

    @Schema(description = "字段元数据列表（用于前端展示）")
    private List<FieldMetadataVO> fieldMetadata;

    @Schema(description = "计算日志", example = "生成月报成功")
    private String calculationLog;

    @Schema(description = "状态", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    private Integer status;

    @Schema(description = "生成时间")
    private LocalDateTime generateTime;

    @Schema(description = "创建时间", requiredMode = Schema.RequiredMode.REQUIRED)
    private LocalDateTime createTime;

    @Schema(description = "更新时间", requiredMode = Schema.RequiredMode.REQUIRED)
    private LocalDateTime updateTime;

    @Schema(description = "创建人", example = "admin")
    private String creator;

}
