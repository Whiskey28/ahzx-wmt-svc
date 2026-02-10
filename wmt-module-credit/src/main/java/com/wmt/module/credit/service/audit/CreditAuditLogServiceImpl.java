package com.wmt.module.credit.service.audit;

import com.wmt.framework.common.pojo.PageResult;
import com.wmt.module.credit.controller.admin.audit.vo.CreditAuditLogPageReqVO;
import com.wmt.module.credit.dal.dataobject.audit.CreditAuditLogDO;
import com.wmt.module.credit.dal.mysql.audit.CreditAuditLogMapper;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 征信审计日志 Service 实现类
 *
 * @author AHC源码
 */
@Service
@Slf4j
public class CreditAuditLogServiceImpl implements CreditAuditLogService {

    @Resource
    private CreditAuditLogMapper auditLogMapper;

    @Override
    public void createLog(CreditAuditLogDO logDO) {
        auditLogMapper.insert(logDO);
    }

    @Override
    public PageResult<CreditAuditLogDO> getAuditLogPage(CreditAuditLogPageReqVO pageReqVO) {
        return auditLogMapper.selectPage(pageReqVO);
    }

    @Override
    public List<CreditAuditLogDO> getAuditLogList(String bizType, Long bizId) {
        return auditLogMapper.selectListByBizTypeAndBizId(bizType, bizId);
    }

}
