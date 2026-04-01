package com.wmt.module.credit.report.dal.mysql;

import com.wmt.framework.mybatis.core.mapper.BaseMapperX;
import com.wmt.module.credit.report.dal.dataobject.ReportFillQuarterMicroLoanDO;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 报表填报 - 季报（小微贷款）Mapper
 *
 * @author Auto
 */
@Mapper
public interface ReportFillQuarterMicroLoanMapper extends BaseMapperX<ReportFillQuarterMicroLoanDO> {

    @Delete("DELETE FROM report_fill_quarter_micro_loan WHERE parent_id = #{parentId}")
    int deleteRealByParentId(@Param("parentId") String parentId);
}

