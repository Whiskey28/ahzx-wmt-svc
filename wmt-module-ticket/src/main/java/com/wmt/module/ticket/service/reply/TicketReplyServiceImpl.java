package com.wmt.module.ticket.service.reply;

import com.wmt.module.ticket.dal.dataobject.reply.TicketReplyDO;
import com.wmt.module.ticket.dal.mysql.reply.TicketReplyMapper;
import com.wmt.module.ticket.enums.TicketReplyFromTypeEnum;
import com.wmt.module.ticket.service.ticket.TicketService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TicketReplyServiceImpl implements TicketReplyService {

    @Resource
    private TicketReplyMapper ticketReplyMapper;
    @Resource
    private TicketService ticketService;

    @Override
    public void createAppReply(Long userId, Long ticketId, String content) {
        ticketService.getAppTicket(userId, ticketId);
        TicketReplyDO reply = new TicketReplyDO();
        reply.setTicketId(ticketId);
        reply.setFromType(TicketReplyFromTypeEnum.USER.getType());
        reply.setUserId(userId);
        reply.setContent(content);
        ticketReplyMapper.insert(reply);
        ticketService.touchAfterReply(ticketId, false);
    }

    @Override
    public void createAdminReply(Long adminUserId, Long ticketId, String content) {
        ticketService.validateTicketExists(ticketId);
        TicketReplyDO reply = new TicketReplyDO();
        reply.setTicketId(ticketId);
        reply.setFromType(TicketReplyFromTypeEnum.ADMIN.getType());
        reply.setAdminUserId(adminUserId);
        reply.setContent(content);
        ticketReplyMapper.insert(reply);
        ticketService.touchAfterReply(ticketId, true);
    }

    @Override
    public List<TicketReplyDO> getReplyList(Long ticketId) {
        ticketService.validateTicketExists(ticketId);
        return ticketReplyMapper.selectListByTicketId(ticketId);
    }
}
