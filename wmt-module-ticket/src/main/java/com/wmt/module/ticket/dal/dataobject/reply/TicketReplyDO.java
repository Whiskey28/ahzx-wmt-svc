package com.wmt.module.ticket.dal.dataobject.reply;

import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.wmt.framework.mybatis.core.dataobject.BaseDO;
import lombok.Data;
import lombok.EqualsAndHashCode;

@TableName("ticket_reply")
@KeySequence("ticket_reply_seq")
@Data
@EqualsAndHashCode(callSuper = true)
public class TicketReplyDO extends BaseDO {

    @TableId
    private Long id;
    private Long ticketId;
    private Integer fromType;
    private Long userId;
    private Long adminUserId;
    private String content;
}
