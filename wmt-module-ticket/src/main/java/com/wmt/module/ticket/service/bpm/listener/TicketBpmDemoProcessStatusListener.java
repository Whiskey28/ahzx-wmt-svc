package com.wmt.module.ticket.service.bpm.listener;

import cn.hutool.core.util.StrUtil;
import com.wmt.module.bpm.api.event.BpmProcessInstanceStatusEvent;
import com.wmt.module.ticket.dal.dataobject.bpm.TicketBpmDemoDO;
import com.wmt.module.ticket.dal.mysql.bpm.TicketBpmDemoMapper;
import com.wmt.module.ticket.service.bpm.TicketBpmDemoService;
import jakarta.annotation.Resource;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.util.Objects;

/**
 * 流程结束时同步 ticket_bpm_demo 状态（业务归属工单模块）
 */
@Component
public class TicketBpmDemoProcessStatusListener implements ApplicationListener<BpmProcessInstanceStatusEvent> {

    @Resource
    private TicketBpmDemoMapper ticketBpmDemoMapper;
    @Resource
    private TicketBpmDemoService ticketBpmDemoService;

    @Override
    public void onApplicationEvent(BpmProcessInstanceStatusEvent event) {
        if (StrUtil.isBlank(event.getBusinessKey())) {
            return;
        }
        long id;
        try {
            id = Long.parseLong(event.getBusinessKey());
        } catch (NumberFormatException e) {
            return;
        }
        TicketBpmDemoDO row = ticketBpmDemoMapper.selectById(id);
        if (row == null) {
            return;
        }
        if (StrUtil.isNotBlank(row.getProcessInstanceId())
                && !Objects.equals(row.getProcessInstanceId(), event.getId())) {
            return;
        }
        ticketBpmDemoService.updateStatus(id, event.getStatus());
    }
}
