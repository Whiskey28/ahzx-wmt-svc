package com.wmt.module.ticket.dal.dataobject.bpm;

import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.wmt.framework.mybatis.core.dataobject.BaseDO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * 工单：多模式 Flowable 实验业务数据
 */
@TableName("ticket_bpm_workflow_lab")
@KeySequence("ticket_bpm_workflow_lab_seq")
@Data
@EqualsAndHashCode(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TicketBpmWorkflowLabDO extends BaseDO {

    @TableId
    private Long id;
    private Long userId;
    /** 见 {@link com.wmt.module.ticket.enums.TicketBpmWorkflowLabTypeEnum} */
    private String labType;
    private String title;
    private String remark;
    private BigDecimal amount;
    private String payloadJson;
    private Integer status;
    private String processInstanceId;
}
