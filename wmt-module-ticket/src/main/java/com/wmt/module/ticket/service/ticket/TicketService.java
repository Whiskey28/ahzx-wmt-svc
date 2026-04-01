package com.wmt.module.ticket.service.ticket;

import com.wmt.framework.common.pojo.PageResult;
import com.wmt.module.ticket.controller.admin.ticket.vo.AdminTicketPageReqVO;
import com.wmt.module.ticket.controller.app.ticket.vo.AppTicketCreateReqVO;
import com.wmt.module.ticket.controller.app.ticket.vo.AppTicketPageReqVO;
import com.wmt.module.ticket.dal.dataobject.ticket.TicketDO;

public interface TicketService {

    Long createTicket(Long userId, AppTicketCreateReqVO reqVO);

    PageResult<TicketDO> getAppTicketPage(Long userId, AppTicketPageReqVO reqVO);

    PageResult<TicketDO> getAdminTicketPage(AdminTicketPageReqVO reqVO);

    TicketDO getAppTicket(Long userId, Long id);

    TicketDO getAdminTicket(Long id);

    TicketDO validateTicketExists(Long id);

    void assignTicket(Long id, Long adminUserId);

    void updateStatus(Long id, Integer status);

    void touchAfterReply(Long id, boolean adminReply);
}
