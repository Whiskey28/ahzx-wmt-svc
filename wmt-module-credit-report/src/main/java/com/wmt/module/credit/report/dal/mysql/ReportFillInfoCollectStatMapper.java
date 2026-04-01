package com.wmt.module.credit.report.dal.mysql;

import com.wmt.framework.mybatis.core.mapper.BaseMapperX;
import com.wmt.module.credit.report.dal.dataobject.ReportFillInfoCollectStatDO;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 报表填报 - 信息采集情况 Mapper
 *
 * @author AHC源码
 */
@Mapper
public interface ReportFillInfoCollectStatMapper extends BaseMapperX<ReportFillInfoCollectStatDO> {

    @Delete("DELETE FROM report_fill_info_collect_stat WHERE parent_id = #{parentId}")
    int deleteRealByParentId(@Param("parentId") String parentId);
}

