package com.wmt.module.ticket.service.reply;

import com.wmt.module.ticket.dal.dataobject.reply.TicketReplyDO;

import java.util.List;

public interface TicketReplyService {

    void createAppReply(Long userId, Long ticketId, String content);

    void createAdminReply(Long adminUserId, Long ticketId, String content);

    List<TicketReplyDO> getReplyList(Long ticketId);
}
