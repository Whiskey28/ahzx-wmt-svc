package com.wmt.module.ticket.service.bpm.lab;

import com.wmt.module.ticket.controller.admin.bpm.lab.vo.TicketBpmWorkflowLabCreateReqVO;
import com.wmt.module.ticket.dal.dataobject.bpm.TicketBpmWorkflowLabDO;
import jakarta.validation.Valid;

public interface TicketBpmWorkflowLabService {

    Long create(@Valid TicketBpmWorkflowLabCreateReqVO reqVO, Long userId);

    TicketBpmWorkflowLabDO get(Long id);

    void updateStatus(Long id, Integer status);

    /** 接收任务实验：触发外部回调，推进流程 */
    void triggerReceiveTask(Long id, Long userId);
}
