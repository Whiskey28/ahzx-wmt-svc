package com.wmt.module.credit.controller.admin.audit.vo;

import com.wmt.framework.common.pojo.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.time.LocalDateTime;

/**
 * 征信审计日志分页查询 Request VO
 *
 * @author AHC源码
 */
@Schema(description = "管理后台 - 征信审计日志分页查询 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class CreditAuditLogPageReqVO extends PageParam {

    @Schema(description = "业务类型", example = "FORM_DATA")
    private String bizType;

    @Schema(description = "业务编号", example = "1")
    private Long bizId;

    @Schema(description = "操作类型", example = "UPDATE")
    private String operationType;

    @Schema(description = "操作用户编号", example = "1")
    private Long operationUserId;

    @Schema(description = "创建时间")
    private LocalDateTime[] createTime;

}
