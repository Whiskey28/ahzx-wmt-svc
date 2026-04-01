package com.wmt.module.ticket.controller.admin.bpm.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

@Schema(description = "管理后台 - 工单业务表单流程演示 Response VO")
@Data
public class TicketBpmDemoRespVO {

    @Schema(description = "编号", example = "1")
    private Long id;
    private String title;
    private String remark;
    @Schema(description = "审批状态")
    private Integer status;
    private String processInstanceId;
    private LocalDateTime createTime;
}
