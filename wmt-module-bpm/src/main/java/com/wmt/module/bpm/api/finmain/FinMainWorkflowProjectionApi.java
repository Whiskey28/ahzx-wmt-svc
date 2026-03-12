package com.wmt.module.bpm.api.finmain;

/**
 * FinMain 工作流投影 API：流程结束后将结果回写到 fin_main 表（STATUS 等）
 * <p>
 * 由拥有 fin_main 的模块（如 finance）实现；未实现时流程仍正常结束，仅不回写业务表。
 * </p>
 *
 * @author flowable-finmain-guide
 */
public interface FinMainWorkflowProjectionApi {

    /**
     * 根据流程结果更新 fin_main 状态（含拒绝原因）
     *
     * @param businessNo 业务主键（与流程 businessKey 一致）
     * @param finMainStatus fin_main 的 STATUS 取值，如 11=审批拒绝、12=审批通过
     * @param reason 拒绝原因/意见（通过时可为空）
     */
    void updateFinMainStatus(String businessNo, Integer finMainStatus, String reason);
}
