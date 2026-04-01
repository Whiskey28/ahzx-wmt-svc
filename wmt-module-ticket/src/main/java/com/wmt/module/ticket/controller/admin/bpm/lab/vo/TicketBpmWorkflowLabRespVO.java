package com.wmt.module.ticket.controller.admin.bpm.lab.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Schema(description = "管理后台 - 工单工作流实验详情")
@Data
public class TicketBpmWorkflowLabRespVO {

    @Schema(description = "主键")
    private Long id;
    @Schema(description = "实验类型")
    private String labType;
    @Schema(description = "标题")
    private String title;
    @Schema(description = "说明")
    private String remark;
    @Schema(description = "金额")
    private BigDecimal amount;
    @Schema(description = "状态")
    private Integer status;
    @Schema(description = "流程实例编号")
    private String processInstanceId;
    @Schema(description = "创建时间")
    private LocalDateTime createTime;
}
