package com.wmt.module.credit.convert;

import com.wmt.framework.common.pojo.PageResult;
import com.wmt.module.credit.controller.admin.audit.vo.CreditAuditLogRespVO;
import com.wmt.module.credit.dal.dataobject.audit.CreditAuditLogDO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * 征信审计日志 Convert
 *
 * @author AHC源码
 */
@Mapper
public interface CreditAuditLogConvert {

    CreditAuditLogConvert INSTANCE = Mappers.getMapper(CreditAuditLogConvert.class);

    CreditAuditLogRespVO convert(CreditAuditLogDO bean);

    List<CreditAuditLogRespVO> convertList(List<CreditAuditLogDO> list);

    PageResult<CreditAuditLogRespVO> convertPage(PageResult<CreditAuditLogDO> page);

}
