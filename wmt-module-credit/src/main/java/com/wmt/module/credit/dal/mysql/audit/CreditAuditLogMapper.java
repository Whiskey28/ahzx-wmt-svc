package com.wmt.module.credit.dal.mysql.audit;

import com.wmt.framework.common.pojo.PageResult;
import com.wmt.framework.mybatis.core.mapper.BaseMapperX;
import com.wmt.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.wmt.module.credit.controller.admin.audit.vo.CreditAuditLogPageReqVO;
import com.wmt.module.credit.dal.dataobject.audit.CreditAuditLogDO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 征信审计日志 Mapper
 *
 * @author AHC源码
 */
@Mapper
public interface CreditAuditLogMapper extends BaseMapperX<CreditAuditLogDO> {

    /**
     * 分页查询审计日志
     *
     * @param pageReqVO 分页查询条件
     * @return 分页结果
     */
    default PageResult<CreditAuditLogDO> selectPage(CreditAuditLogPageReqVO pageReqVO) {
        return selectPage(pageReqVO, new LambdaQueryWrapperX<CreditAuditLogDO>()
                .eqIfPresent(CreditAuditLogDO::getBizType, pageReqVO.getBizType())
                .eqIfPresent(CreditAuditLogDO::getBizId, pageReqVO.getBizId())
                .eqIfPresent(CreditAuditLogDO::getOperationType, pageReqVO.getOperationType())
                .eqIfPresent(CreditAuditLogDO::getOperationUserId, pageReqVO.getOperationUserId())
                .betweenIfPresent(CreditAuditLogDO::getCreateTime, pageReqVO.getCreateTime())
                .orderByDesc(CreditAuditLogDO::getCreateTime));
    }

    /**
     * 根据业务类型和ID查询审计日志列表
     *
     * @param bizType 业务类型
     * @param bizId 业务编号
     * @return 审计日志列表
     */
    default List<CreditAuditLogDO> selectListByBizTypeAndBizId(String bizType, Long bizId) {
        return selectList(new LambdaQueryWrapperX<CreditAuditLogDO>()
                .eq(CreditAuditLogDO::getBizType, bizType)
                .eq(CreditAuditLogDO::getBizId, bizId)
                .orderByDesc(CreditAuditLogDO::getCreateTime));
    }

}
