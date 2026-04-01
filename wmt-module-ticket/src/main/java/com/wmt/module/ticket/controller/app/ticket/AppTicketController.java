package com.wmt.module.ticket.controller.app.ticket;

import com.wmt.framework.common.pojo.CommonResult;
import com.wmt.framework.common.pojo.PageResult;
import com.wmt.framework.common.util.object.BeanUtils;
import com.wmt.module.ticket.controller.app.ticket.vo.*;
import com.wmt.module.ticket.dal.dataobject.reply.TicketReplyDO;
import com.wmt.module.ticket.dal.dataobject.ticket.TicketDO;
import com.wmt.module.ticket.service.reply.TicketReplyService;
import com.wmt.module.ticket.service.ticket.TicketService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.wmt.framework.common.pojo.CommonResult.success;
import static com.wmt.framework.security.core.util.SecurityFrameworkUtils.getLoginUserId;

@Tag(name = "App - Ticket")
@RestController
@RequestMapping("/ticket")
@Validated
public class AppTicketController {

    @Resource
    private TicketService ticketService;
    @Resource
    private TicketReplyService ticketReplyService;

    @PostMapping("/create")
    @Operation(summary = "Create ticket")
    public CommonResult<Long> createTicket(@Valid @RequestBody AppTicketCreateReqVO reqVO) {
        return success(ticketService.createTicket(getLoginUserId(), reqVO));
    }

    @GetMapping("/page")
    @Operation(summary = "My tickets (paged)")
    public CommonResult<PageResult<AppTicketRespVO>> getMyTicketPage(@Valid AppTicketPageReqVO reqVO) {
        PageResult<TicketDO> page = ticketService.getAppTicketPage(getLoginUserId(), reqVO);
        return success(BeanUtils.toBean(page, AppTicketRespVO.class));
    }

    @GetMapping("/get")
    @Operation(summary = "Ticket detail")
    @Parameter(name = "id", description = "Ticket id", required = true, example = "1")
    public CommonResult<AppTicketDetailRespVO> getTicket(@RequestParam("id") Long id) {
        TicketDO ticket = ticketService.getAppTicket(getLoginUserId(), id);
        List<TicketReplyDO> replies = ticketReplyService.getReplyList(id);
        AppTicketDetailRespVO resp = new AppTicketDetailRespVO();
        resp.setTicket(BeanUtils.toBean(ticket, AppTicketRespVO.class));
        resp.setReplies(BeanUtils.toBean(replies, AppTicketReplyRespVO.class));
        return success(resp);
    }

    @PostMapping("/reply")
    @Operation(summary = "Reply to ticket")
    public CommonResult<Boolean> replyTicket(@Valid @RequestBody AppTicketReplyReqVO reqVO) {
        ticketReplyService.createAppReply(getLoginUserId(), reqVO.getTicketId(), reqVO.getContent());
        return success(true);
    }
}
