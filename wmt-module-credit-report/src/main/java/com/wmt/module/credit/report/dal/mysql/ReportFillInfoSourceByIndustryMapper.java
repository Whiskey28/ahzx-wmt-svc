package com.wmt.module.credit.report.dal.mysql;

import com.wmt.framework.mybatis.core.mapper.BaseMapperX;
import com.wmt.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.wmt.module.credit.report.dal.dataobject.ReportFillInfoSourceByIndustryDO;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 报表填报 - 信息来源情况（按行业分类）Mapper
 *
 * @author AHC源码
 */
@Mapper
public interface ReportFillInfoSourceByIndustryMapper extends BaseMapperX<ReportFillInfoSourceByIndustryDO> {

    @Delete("DELETE FROM report_fill_info_source_by_industry WHERE record_id = #{recordId}")
    int deleteRealByRecordId(@Param("recordId") String recordId);

    default List<ReportFillInfoSourceByIndustryDO> selectListByRecordId(String recordId) {
        return selectList(new LambdaQueryWrapperX<ReportFillInfoSourceByIndustryDO>()
                .eq(ReportFillInfoSourceByIndustryDO::getRecordId, recordId)
                .orderByAsc(ReportFillInfoSourceByIndustryDO::getIndustryCode));
    }

}

