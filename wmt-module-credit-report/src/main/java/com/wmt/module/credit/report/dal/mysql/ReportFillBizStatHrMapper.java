package com.wmt.module.credit.report.dal.mysql;

import com.wmt.framework.mybatis.core.mapper.BaseMapperX;
import com.wmt.module.credit.report.dal.dataobject.ReportFillBizStatHrDO;
import org.apache.ibatis.annotations.Mapper;

/**
 * 报表填报 - 经营情况（人员口径）Mapper
 *
 * @author AHC源码
 */
@Mapper
public interface ReportFillBizStatHrMapper extends BaseMapperX<ReportFillBizStatHrDO> {
}

