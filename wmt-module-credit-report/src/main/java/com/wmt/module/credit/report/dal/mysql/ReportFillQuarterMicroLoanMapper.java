package com.wmt.module.credit.report.dal.mysql;

import com.wmt.framework.mybatis.core.mapper.BaseMapperX;
import com.wmt.module.credit.report.dal.dataobject.ReportFillQuarterMicroLoanDO;
import org.apache.ibatis.annotations.Mapper;

/**
 * 报表填报 - 季报（小微贷款）Mapper
 *
 * @author Auto
 */
@Mapper
public interface ReportFillQuarterMicroLoanMapper extends BaseMapperX<ReportFillQuarterMicroLoanDO> {
}

