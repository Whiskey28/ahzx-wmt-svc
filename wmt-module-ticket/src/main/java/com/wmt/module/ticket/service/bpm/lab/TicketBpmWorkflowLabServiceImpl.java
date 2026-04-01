package com.wmt.module.ticket.service.bpm.lab;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.wmt.module.bpm.api.task.BpmProcessInstanceApi;
import com.wmt.module.bpm.api.task.BpmProcessTaskApi;
import com.wmt.module.bpm.api.task.dto.BpmProcessInstanceCreateReqDTO;
import com.wmt.module.bpm.enums.task.BpmTaskStatusEnum;
import com.wmt.module.ticket.controller.admin.bpm.lab.vo.TicketBpmWorkflowLabCreateReqVO;
import com.wmt.module.ticket.dal.dataobject.bpm.TicketBpmWorkflowLabDO;
import com.wmt.module.ticket.dal.mysql.bpm.TicketBpmWorkflowLabMapper;
import com.wmt.module.ticket.enums.TicketBpmWorkflowLabTypeEnum;
import com.wmt.module.ticket.service.bpm.util.BpmProcessDefinitionKeyUtils;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.Objects;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.wmt.framework.common.exception.util.ServiceExceptionUtil.exception;
import static com.wmt.module.ticket.enums.ErrorCodeConstants.TICKET_BPM_WORKFLOW_LAB_AMOUNT_REQUIRED;
import static com.wmt.module.ticket.enums.ErrorCodeConstants.TICKET_BPM_WORKFLOW_LAB_NOT_EXISTS;
import static com.wmt.module.ticket.enums.ErrorCodeConstants.TICKET_BPM_WORKFLOW_LAB_NOT_RECEIVE_TYPE;
import static com.wmt.module.ticket.enums.ErrorCodeConstants.TICKET_BPM_WORKFLOW_LAB_PROCESS_NOT_BOUND;
import static com.wmt.module.ticket.enums.ErrorCodeConstants.TICKET_BPM_WORKFLOW_LAB_SELECT_ASSIGNEE_REQUIRED;
import static com.wmt.module.ticket.enums.ErrorCodeConstants.TICKET_BPM_WORKFLOW_LAB_TYPE_INVALID;
import static com.wmt.module.ticket.enums.ErrorCodeConstants.TICKET_NOT_OWNER;

@Service
@Validated
public class TicketBpmWorkflowLabServiceImpl implements TicketBpmWorkflowLabService {

    @Resource
    private TicketBpmWorkflowLabMapper ticketBpmWorkflowLabMapper;
    @Resource
    private BpmProcessInstanceApi processInstanceApi;
    @Resource
    private BpmProcessTaskApi processTaskApi;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long create(TicketBpmWorkflowLabCreateReqVO reqVO, Long userId) {
        TicketBpmWorkflowLabTypeEnum labType = TicketBpmWorkflowLabTypeEnum.valueOfType(reqVO.getLabType());
        if (labType == null) {
            throw exception(TICKET_BPM_WORKFLOW_LAB_TYPE_INVALID);
        }
        validateCreatePayload(labType, reqVO);

        TicketBpmWorkflowLabDO row = TicketBpmWorkflowLabDO.builder()
                .userId(userId)
                .labType(labType.getType())
                .title(reqVO.getTitle())
                .remark(reqVO.getRemark())
                .amount(reqVO.getAmount())
                .status(BpmTaskStatusEnum.RUNNING.getStatus())
                .build();
        ticketBpmWorkflowLabMapper.insert(row);

        String processDefinitionKey = resolveProcessDefinitionKey(labType, reqVO.getProcessDefinitionKey());
        Map<String, Object> variables = buildVariables(labType, reqVO, userId);

        BpmProcessInstanceCreateReqDTO dto = new BpmProcessInstanceCreateReqDTO()
                .setProcessDefinitionKey(processDefinitionKey)
                .setBusinessKey(String.valueOf(row.getId()))
                .setVariables(variables);
        if (labType == TicketBpmWorkflowLabTypeEnum.SELECT_USER) {
            dto.setStartUserSelectAssignees(reqVO.getStartUserSelectAssignees());
        }

        String processInstanceId = processInstanceApi.createProcessInstance(userId, dto);
        ticketBpmWorkflowLabMapper.updateById(new TicketBpmWorkflowLabDO().setId(row.getId()).setProcessInstanceId(processInstanceId));
        return row.getId();
    }

    private static void validateCreatePayload(TicketBpmWorkflowLabTypeEnum labType, TicketBpmWorkflowLabCreateReqVO reqVO) {
        if (labType == TicketBpmWorkflowLabTypeEnum.XOR) {
            if (reqVO.getAmount() == null) {
                throw exception(TICKET_BPM_WORKFLOW_LAB_AMOUNT_REQUIRED);
            }
        }
        if (labType == TicketBpmWorkflowLabTypeEnum.SELECT_USER) {
            Map<String, List<Long>> m = reqVO.getStartUserSelectAssignees();
            List<Long> assignees = m != null ? m.get(TicketBpmWorkflowLabConstants.SELECT_USER_TASK_ACTIVITY_ID) : null;
            if (CollUtil.isEmpty(assignees)) {
                throw exception(TICKET_BPM_WORKFLOW_LAB_SELECT_ASSIGNEE_REQUIRED);
            }
        }
    }

    private static Map<String, Object> buildVariables(TicketBpmWorkflowLabTypeEnum labType,
                                                      TicketBpmWorkflowLabCreateReqVO reqVO,
                                                      Long userId) {
        Map<String, Object> variables = new HashMap<>(8);
        variables.put("title", reqVO.getTitle());
        variables.put("startUserId", userId);
        if (labType == TicketBpmWorkflowLabTypeEnum.XOR && reqVO.getAmount() != null) {
            variables.put("amount", reqVO.getAmount().doubleValue());
        }
        return variables;
    }

    private static String resolveProcessDefinitionKey(TicketBpmWorkflowLabTypeEnum labType, String override) {
        if (StrUtil.isNotBlank(override)) {
            return BpmProcessDefinitionKeyUtils.normalize(override);
        }
        return switch (labType) {
            case XOR -> TicketBpmWorkflowLabConstants.PROCESS_KEY_XOR;
            case SELECT_USER -> TicketBpmWorkflowLabConstants.PROCESS_KEY_SELECT_USER;
            case PARALLEL -> TicketBpmWorkflowLabConstants.PROCESS_KEY_PARALLEL;
            case RECEIVE -> TicketBpmWorkflowLabConstants.PROCESS_KEY_RECEIVE;
        };
    }

    @Override
    public TicketBpmWorkflowLabDO get(Long id) {
        return validateExists(id);
    }

    @Override
    public void updateStatus(Long id, Integer status) {
        validateExists(id);
        ticketBpmWorkflowLabMapper.updateById(new TicketBpmWorkflowLabDO().setId(id).setStatus(status));
    }

    @Override
    public void triggerReceiveTask(Long id, Long userId) {
        TicketBpmWorkflowLabDO row = validateExists(id);
        if (!Objects.equals(row.getUserId(), userId)) {
            throw exception(TICKET_NOT_OWNER);
        }
        if (!TicketBpmWorkflowLabTypeEnum.RECEIVE.getType().equals(row.getLabType())) {
            throw exception(TICKET_BPM_WORKFLOW_LAB_NOT_RECEIVE_TYPE);
        }
        if (StrUtil.isBlank(row.getProcessInstanceId())) {
            throw exception(TICKET_BPM_WORKFLOW_LAB_PROCESS_NOT_BOUND);
        }
        processTaskApi.triggerTask(row.getProcessInstanceId(), TicketBpmWorkflowLabConstants.RECEIVE_TASK_ACTIVITY_ID);
    }

    private TicketBpmWorkflowLabDO validateExists(Long id) {
        TicketBpmWorkflowLabDO row = ticketBpmWorkflowLabMapper.selectById(id);
        if (row == null) {
            throw exception(TICKET_BPM_WORKFLOW_LAB_NOT_EXISTS);
        }
        return row;
    }
}
