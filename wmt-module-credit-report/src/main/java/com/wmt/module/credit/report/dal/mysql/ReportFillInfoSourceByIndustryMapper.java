package com.wmt.module.credit.report.dal.mysql;

import com.wmt.framework.mybatis.core.mapper.BaseMapperX;
import com.wmt.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.wmt.module.credit.report.dal.dataobject.ReportFillInfoSourceByIndustryDO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 报表填报 - 信息来源情况（按行业分类）Mapper
 *
 * @author AHC源码
 */
@Mapper
public interface ReportFillInfoSourceByIndustryMapper extends BaseMapperX<ReportFillInfoSourceByIndustryDO> {

    default List<ReportFillInfoSourceByIndustryDO> selectListByRecordId(String recordId) {
        return selectList(new LambdaQueryWrapperX<ReportFillInfoSourceByIndustryDO>()
                .eq(ReportFillInfoSourceByIndustryDO::getRecordId, recordId)
                .orderByAsc(ReportFillInfoSourceByIndustryDO::getIndustryCode));
    }

}

