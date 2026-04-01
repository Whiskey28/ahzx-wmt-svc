package com.wmt.module.ticket.service.bpm;

import com.wmt.module.ticket.controller.admin.bpm.vo.TicketBpmDemoCreateReqVO;
import com.wmt.module.ticket.dal.dataobject.bpm.TicketBpmDemoDO;
import jakarta.validation.Valid;

public interface TicketBpmDemoService {

    Long create(@Valid TicketBpmDemoCreateReqVO reqVO, Long userId);

    TicketBpmDemoDO get(Long id);

    void updateStatus(Long id, Integer status);
}
