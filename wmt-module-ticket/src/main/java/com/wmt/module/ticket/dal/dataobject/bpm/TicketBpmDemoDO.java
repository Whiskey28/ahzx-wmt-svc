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

/**
 * 工单模块：业务表单 + 流程引擎联调演示（业务数据归属 ticket，仅依赖 BPM API 发起流程）
 */
@TableName("ticket_bpm_demo")
@KeySequence("ticket_bpm_demo_seq")
@Data
@EqualsAndHashCode(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TicketBpmDemoDO extends BaseDO {

    @TableId
    private Long id;
    private Long userId;
    private String title;
    private String remark;
    /** 审批结果，与流程实例状态取值一致 */
    private Integer status;
    private String processInstanceId;
}
