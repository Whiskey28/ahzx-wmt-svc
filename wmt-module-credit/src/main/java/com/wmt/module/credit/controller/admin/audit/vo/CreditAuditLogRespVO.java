package com.wmt.module.credit.controller.admin.audit.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * 征信审计日志 Response VO
 *
 * @author AHC源码
 */
@Schema(description = "管理后台 - 征信审计日志 Response VO")
@Data
public class CreditAuditLogRespVO {

    @Schema(description = "日志编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    private Long id;

    @Schema(description = "业务类型", requiredMode = Schema.RequiredMode.REQUIRED, example = "FORM_DATA")
    private String bizType;

    @Schema(description = "业务编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    private Long bizId;

    @Schema(description = "操作类型", requiredMode = Schema.RequiredMode.REQUIRED, example = "UPDATE")
    private String operationType;

    @Schema(description = "操作用户编号", example = "1")
    private Long operationUserId;

    @Schema(description = "操作描述", example = "更新表单数据")
    private String operationDesc;

    @Schema(description = "变更前数据（JSON格式）")
    private Map<String, Object> beforeData;

    @Schema(description = "变更后数据（JSON格式）")
    private Map<String, Object> afterData;

    @Schema(description = "变更字段列表", example = "formData,status")
    private String changeFields;

    @Schema(description = "IP地址", example = "127.0.0.1")
    private String ipAddress;

    @Schema(description = "用户代理", example = "Mozilla/5.0...")
    private String userAgent;

    @Schema(description = "创建时间", requiredMode = Schema.RequiredMode.REQUIRED)
    private LocalDateTime createTime;

}
