package com.wmt.module.ticket.service.bpm.listener;

import cn.hutool.core.util.StrUtil;
import com.wmt.module.bpm.api.event.BpmProcessInstanceStatusEvent;
import com.wmt.module.ticket.dal.dataobject.bpm.TicketBpmWorkflowLabDO;
import com.wmt.module.ticket.dal.mysql.bpm.TicketBpmWorkflowLabMapper;
import com.wmt.module.ticket.service.bpm.lab.TicketBpmWorkflowLabConstants;
import com.wmt.module.ticket.service.bpm.lab.TicketBpmWorkflowLabService;
import jakarta.annotation.Resource;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.util.Objects;

/**
 * 工作流实验：按流程定义 Key 将实例终态同步到 ticket_bpm_workflow_lab
 */
@Component
public class TicketBpmWorkflowLabStatusListener implements ApplicationListener<BpmProcessInstanceStatusEvent> {

    @Resource
    private TicketBpmWorkflowLabMapper ticketBpmWorkflowLabMapper;
    @Resource
    private TicketBpmWorkflowLabService ticketBpmWorkflowLabService;

    @Override
    public void onApplicationEvent(BpmProcessInstanceStatusEvent event) {
        if (!TicketBpmWorkflowLabConstants.isLabProcessKey(event.getProcessDefinitionKey())) {
            return;
        }
        if (StrUtil.isBlank(event.getBusinessKey())) {
            return;
        }
        long id;
        try {
            id = Long.parseLong(event.getBusinessKey());
        } catch (NumberFormatException e) {
            return;
        }
        TicketBpmWorkflowLabDO row = ticketBpmWorkflowLabMapper.selectById(id);
        if (row == null) {
            return;
        }
        if (StrUtil.isNotBlank(row.getProcessInstanceId())
                && !Objects.equals(row.getProcessInstanceId(), event.getId())) {
            return;
        }
        ticketBpmWorkflowLabService.updateStatus(id, event.getStatus());
    }
}
