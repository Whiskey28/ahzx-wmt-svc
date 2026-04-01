package com.wmt.module.ticket.service.ticket;

import com.wmt.framework.common.pojo.PageResult;
import com.wmt.module.ticket.controller.admin.ticket.vo.AdminTicketPageReqVO;
import com.wmt.module.ticket.controller.app.ticket.vo.AppTicketCreateReqVO;
import com.wmt.module.ticket.controller.app.ticket.vo.AppTicketPageReqVO;
import com.wmt.module.ticket.dal.dataobject.ticket.TicketDO;
import com.wmt.module.ticket.dal.mysql.ticket.TicketMapper;
import com.wmt.module.ticket.enums.ErrorCodeConstants;
import com.wmt.module.ticket.enums.TicketSourceEnum;
import com.wmt.module.ticket.enums.TicketStatusEnum;
import com.wmt.module.ticket.service.category.TicketCategoryService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.concurrent.ThreadLocalRandom;

import static com.wmt.framework.common.exception.util.ServiceExceptionUtil.exception;

@Service
public class TicketServiceImpl implements TicketService {

    @Resource
    private TicketMapper ticketMapper;
    @Resource
    private TicketCategoryService ticketCategoryService;

    @Override
    public Long createTicket(Long userId, AppTicketCreateReqVO reqVO) {
        ticketCategoryService.validateCategory(reqVO.getCategoryId());
        TicketDO ticket = new TicketDO();
        ticket.setTicketNo("TK" + System.currentTimeMillis() + ThreadLocalRandom.current().nextInt(100, 1000));
        ticket.setUserId(userId);
        ticket.setCategoryId(reqVO.getCategoryId());
        ticket.setTitle(reqVO.getTitle());
        ticket.setContent(reqVO.getContent());
        ticket.setPriority(reqVO.getPriority());
        ticket.setSource(TicketSourceEnum.APP.getType());
        ticket.setStatus(TicketStatusEnum.CREATED.getStatus());
        ticket.setLastReplyTime(LocalDateTime.now());
        ticketMapper.insert(ticket);
        return ticket.getId();
    }

    @Override
    public PageResult<TicketDO> getAppTicketPage(Long userId, AppTicketPageReqVO reqVO) {
        return ticketMapper.selectAppPage(userId, reqVO);
    }

    @Override
    public PageResult<TicketDO> getAdminTicketPage(AdminTicketPageReqVO reqVO) {
        return ticketMapper.selectAdminPage(reqVO);
    }

    @Override
    public TicketDO getAppTicket(Long userId, Long id) {
        TicketDO ticket = ticketMapper.selectByIdAndUserId(id, userId);
        if (ticket == null) {
            throw exception(ErrorCodeConstants.TICKET_NOT_OWNER);
        }
        return ticket;
    }

    @Override
    public TicketDO getAdminTicket(Long id) {
        return validateTicketExists(id);
    }

    @Override
    public TicketDO validateTicketExists(Long id) {
        TicketDO ticket = ticketMapper.selectById(id);
        if (ticket == null) {
            throw exception(ErrorCodeConstants.TICKET_NOT_EXISTS);
        }
        return ticket;
    }

    @Override
    public void assignTicket(Long id, Long adminUserId) {
        TicketDO ticket = validateTicketExists(id);
        ticket.setAssignedAdminUserId(adminUserId);
        ticketMapper.updateById(ticket);
    }

    @Override
    public void updateStatus(Long id, Integer status) {
        if (!TicketStatusEnum.isValid(status)) {
            throw exception(ErrorCodeConstants.TICKET_STATUS_INVALID);
        }
        TicketDO ticket = validateTicketExists(id);
        ticket.setStatus(status);
        ticket.setClosedTime(TicketStatusEnum.CLOSED.getStatus().equals(status) ? LocalDateTime.now() : null);
        ticketMapper.updateById(ticket);
    }

    @Override
    public void touchAfterReply(Long id, boolean adminReply) {
        TicketDO ticket = validateTicketExists(id);
        if (TicketStatusEnum.CLOSED.getStatus().equals(ticket.getStatus())) {
            throw exception(ErrorCodeConstants.TICKET_CLOSED_CANNOT_REPLY);
        }
        ticket.setLastReplyTime(LocalDateTime.now());
        if (adminReply && TicketStatusEnum.CREATED.getStatus().equals(ticket.getStatus())) {
            ticket.setStatus(TicketStatusEnum.PROCESSING.getStatus());
        }
        ticketMapper.updateById(ticket);
    }
}
