package com.wmt.module.credit.service.audit;

import com.wmt.framework.common.pojo.PageResult;
import com.wmt.module.credit.controller.admin.audit.vo.CreditAuditLogPageReqVO;
import com.wmt.module.credit.dal.dataobject.audit.CreditAuditLogDO;

import java.util.List;

/**
 * 征信审计日志 Service 接口
 *
 * @author AHC源码
 */
public interface CreditAuditLogService {

    /**
     * 创建审计日志
     *
     * @param logDO 审计日志
     */
    void createLog(CreditAuditLogDO logDO);

    /**
     * 获得审计日志分页
     *
     * @param pageReqVO 分页查询
     * @return 审计日志分页
     */
    PageResult<CreditAuditLogDO> getAuditLogPage(CreditAuditLogPageReqVO pageReqVO);

    /**
     * 根据业务类型和ID查询审计日志列表
     *
     * @param bizType 业务类型
     * @param bizId   业务编号
     * @return 审计日志列表
     */
    List<CreditAuditLogDO> getAuditLogList(String bizType, Long bizId);

}
