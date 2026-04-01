package com.wmt.module.ticket.controller.admin.ticket;

import com.wmt.framework.common.pojo.CommonResult;
import com.wmt.framework.common.pojo.PageResult;
import com.wmt.framework.common.util.object.BeanUtils;
import com.wmt.module.ticket.controller.admin.ticket.vo.*;
import com.wmt.module.ticket.dal.dataobject.reply.TicketReplyDO;
import com.wmt.module.ticket.dal.dataobject.ticket.TicketDO;
import com.wmt.module.ticket.service.reply.TicketReplyService;
import com.wmt.module.ticket.service.ticket.TicketService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.wmt.framework.common.pojo.CommonResult.success;
import static com.wmt.framework.security.core.util.SecurityFrameworkUtils.getLoginUserId;

@Tag(name = "Admin - Ticket")
@RestController
@RequestMapping("/ticket/admin")
@Validated
public class AdminTicketController {

    @Resource
    private TicketService ticketService;
    @Resource
    private TicketReplyService ticketReplyService;

    @GetMapping("/page")
    @Operation(summary = "Ticket pagination")
    @PreAuthorize("@ss.hasPermission('ticket:ticket:query')")
    public CommonResult<PageResult<AdminTicketRespVO>> getTicketPage(@Valid AdminTicketPageReqVO reqVO) {
        PageResult<TicketDO> page = ticketService.getAdminTicketPage(reqVO);
        return success(BeanUtils.toBean(page, AdminTicketRespVO.class));
    }

    @GetMapping("/get")
    @Operation(summary = "Ticket detail")
    @Parameter(name = "id", description = "Ticket id", required = true, example = "1")
    @PreAuthorize("@ss.hasPermission('ticket:ticket:query')")
    public CommonResult<AdminTicketDetailRespVO> getTicket(@RequestParam("id") Long id) {
        TicketDO ticket = ticketService.getAdminTicket(id);
        List<TicketReplyDO> replies = ticketReplyService.getReplyList(id);
        AdminTicketDetailRespVO resp = new AdminTicketDetailRespVO();
        resp.setTicket(BeanUtils.toBean(ticket, AdminTicketRespVO.class));
        resp.setReplies(BeanUtils.toBean(replies, AdminTicketReplyRespVO.class));
        return success(resp);
    }

    @PostMapping("/assign")
    @Operation(summary = "Assign ticket")
    @PreAuthorize("@ss.hasPermission('ticket:ticket:update')")
    public CommonResult<Boolean> assignTicket(@Valid @RequestBody AdminTicketAssignReqVO reqVO) {
        ticketService.assignTicket(reqVO.getId(), reqVO.getAssignedAdminUserId());
        return success(true);
    }

    @PostMapping("/update-status")
    @Operation(summary = "Update ticket status")
    @PreAuthorize("@ss.hasPermission('ticket:ticket:update')")
    public CommonResult<Boolean> updateStatus(@Valid @RequestBody AdminTicketUpdateStatusReqVO reqVO) {
        ticketService.updateStatus(reqVO.getId(), reqVO.getStatus());
        return success(true);
    }

    @PostMapping("/reply")
    @Operation(summary = "Reply to ticket")
    @PreAuthorize("@ss.hasPermission('ticket:ticket:update')")
    public CommonResult<Boolean> reply(@Valid @RequestBody AdminTicketReplyReqVO reqVO) {
        ticketReplyService.createAdminReply(getLoginUserId(), reqVO.getTicketId(), reqVO.getContent());
        return success(true);
    }
}
