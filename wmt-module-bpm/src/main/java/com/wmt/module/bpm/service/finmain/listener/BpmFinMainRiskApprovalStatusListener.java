package com.wmt.module.bpm.service.finmain.listener;

import com.wmt.module.bpm.api.event.BpmProcessInstanceStatusEvent;
import com.wmt.module.bpm.api.event.BpmProcessInstanceStatusEventListener;
import com.wmt.module.bpm.api.finmain.FinMainWorkflowProjectionApi;
import com.wmt.module.bpm.enums.task.BpmProcessInstanceStatusEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * FinMain 风险审批子流程（finMainRiskApproval）状态投影监听器
 * <p>
 * 流程结束时将 BPM 状态映射为 fin_main.STATUS 并回写：
 * 审批通过(2) -> 12，审批拒绝(3) -> 11。
 * </p>
 *
 * @author flowable-finmain-guide
 */
@Component
@Slf4j
public class BpmFinMainRiskApprovalStatusListener extends BpmProcessInstanceStatusEventListener {

    public static final String PROCESS_KEY = "finMainRiskApproval";

    /** fin_main 业务状态：审批拒绝 */
    private static final int FIN_MAIN_STATUS_REJECTED = 11;
    /** fin_main 业务状态：审批通过 */
    private static final int FIN_MAIN_STATUS_APPROVED = 12;

    @Autowired(required = false)
    private FinMainWorkflowProjectionApi finMainWorkflowProjectionApi;

    @Override
    protected String getProcessDefinitionKey() {
        return PROCESS_KEY;
    }

    @Override
    protected void onEvent(BpmProcessInstanceStatusEvent event) {
        if (finMainWorkflowProjectionApi == null) {
            log.debug("[onEvent][finMainRiskApproval 流程结束，未实现 FinMainWorkflowProjectionApi，跳过回写] businessKey={}, status={}",
                    event.getBusinessKey(), event.getStatus());
            return;
        }
        Integer bpmStatus = event.getStatus();
        Integer finMainStatus = toFinMainStatus(bpmStatus);
        if (finMainStatus == null) {
            log.debug("[onEvent][非终态，不回写] businessKey={}, bpmStatus={}", event.getBusinessKey(), bpmStatus);
            return;
        }
        try {
            finMainWorkflowProjectionApi.updateFinMainStatus(
                    event.getBusinessKey(),
                    finMainStatus,
                    event.getReason());
        } catch (Exception e) {
            log.error("[onEvent][回写 fin_main 失败] businessKey={}, finMainStatus={}", event.getBusinessKey(), finMainStatus, e);
        }
    }

    /**
     * BPM 流程实例状态 -> fin_main.STATUS
     */
    private static Integer toFinMainStatus(Integer bpmStatus) {
        if (BpmProcessInstanceStatusEnum.APPROVE.getStatus().equals(bpmStatus)) {
            return FIN_MAIN_STATUS_APPROVED;
        }
        if (BpmProcessInstanceStatusEnum.REJECT.getStatus().equals(bpmStatus)) {
            return FIN_MAIN_STATUS_REJECTED;
        }
        return null;
    }
}
