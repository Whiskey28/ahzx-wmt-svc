package com.wmt.module.credit.report.dal.mysql;

import com.wmt.framework.mybatis.core.mapper.BaseMapperX;
import com.wmt.module.credit.report.dal.dataobject.ReportFillBizStatFinanceDO;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 报表填报 - 经营情况（财务口径）Mapper
 *
 * @author AHC源码
 */
@Mapper
public interface ReportFillBizStatFinanceMapper extends BaseMapperX<ReportFillBizStatFinanceDO> {

    @Delete("DELETE FROM report_fill_biz_stat_finance WHERE parent_id = #{parentId}")
    int deleteRealByParentId(@Param("parentId") String parentId);
}

