package com.wmt.module.ticket.service.bpm;

import com.wmt.module.bpm.api.task.BpmProcessInstanceApi;
import com.wmt.module.bpm.api.task.dto.BpmProcessInstanceCreateReqDTO;
import com.wmt.module.bpm.enums.task.BpmTaskStatusEnum;
import com.wmt.module.ticket.controller.admin.bpm.vo.TicketBpmDemoCreateReqVO;
import com.wmt.module.ticket.dal.dataobject.bpm.TicketBpmDemoDO;
import com.wmt.module.ticket.dal.mysql.bpm.TicketBpmDemoMapper;
import com.wmt.module.ticket.service.bpm.util.BpmProcessDefinitionKeyUtils;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.util.HashMap;
import java.util.Map;

import static com.wmt.framework.common.exception.util.ServiceExceptionUtil.exception;
import static com.wmt.module.ticket.enums.ErrorCodeConstants.TICKET_BPM_DEMO_NOT_EXISTS;

@Service
@Validated
public class TicketBpmDemoServiceImpl implements TicketBpmDemoService {

    @Resource
    private TicketBpmDemoMapper ticketBpmDemoMapper;
    @Resource
    private BpmProcessInstanceApi processInstanceApi;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long create(TicketBpmDemoCreateReqVO reqVO, Long userId) {
        TicketBpmDemoDO row = TicketBpmDemoDO.builder()
                .userId(userId)
                .title(reqVO.getTitle())
                .remark(reqVO.getRemark())
                .status(BpmTaskStatusEnum.RUNNING.getStatus())
                .build();
        ticketBpmDemoMapper.insert(row);

        Map<String, Object> variables = new HashMap<>(2);
        variables.put("title", reqVO.getTitle());

        String processDefinitionKey = BpmProcessDefinitionKeyUtils.normalize(reqVO.getProcessDefinitionKey());
        String processInstanceId = processInstanceApi.createProcessInstance(userId,
                new BpmProcessInstanceCreateReqDTO()
                        .setProcessDefinitionKey(processDefinitionKey)
                        .setBusinessKey(String.valueOf(row.getId()))
                        .setVariables(variables)
                        .setStartUserSelectAssignees(reqVO.getStartUserSelectAssignees()));

        ticketBpmDemoMapper.updateById(new TicketBpmDemoDO().setId(row.getId()).setProcessInstanceId(processInstanceId));
        return row.getId();
    }

    @Override
    public TicketBpmDemoDO get(Long id) {
        return validateExists(id);
    }

    @Override
    public void updateStatus(Long id, Integer status) {
        validateExists(id);
        ticketBpmDemoMapper.updateById(new TicketBpmDemoDO().setId(id).setStatus(status));
    }

    private TicketBpmDemoDO validateExists(Long id) {
        TicketBpmDemoDO row = ticketBpmDemoMapper.selectById(id);
        if (row == null) {
            throw exception(TICKET_BPM_DEMO_NOT_EXISTS);
        }
        return row;
    }
}
