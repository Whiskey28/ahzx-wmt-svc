package com.wmt.module.ticket.dal.dataobject.ticket;

import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.wmt.framework.mybatis.core.dataobject.BaseDO;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

@TableName("ticket")
@KeySequence("ticket_seq")
@Data
@EqualsAndHashCode(callSuper = true)
public class TicketDO extends BaseDO {

    @TableId
    private Long id;
    private String ticketNo;
    private Long userId;
    private Long categoryId;
    private String title;
    private String content;
    private Integer status;
    private Integer priority;
    private Integer source;
    private Long assignedAdminUserId;
    private LocalDateTime lastReplyTime;
    private LocalDateTime closedTime;
}
