package com.wmt.module.credit.report.dal.mysql;

import com.wmt.framework.mybatis.core.mapper.BaseMapperX;
import com.wmt.module.credit.report.dal.dataobject.ReportFillBizStatHrDO;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 报表填报 - 经营情况（人员口径）Mapper
 *
 * @author AHC源码
 */
@Mapper
public interface ReportFillBizStatHrMapper extends BaseMapperX<ReportFillBizStatHrDO> {

    @Delete("DELETE FROM report_fill_biz_stat_hr WHERE parent_id = #{parentId}")
    int deleteRealByParentId(@Param("parentId") String parentId);
}

