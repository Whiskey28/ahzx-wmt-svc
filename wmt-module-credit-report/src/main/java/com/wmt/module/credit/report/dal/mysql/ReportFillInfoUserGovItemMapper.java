package com.wmt.module.credit.report.dal.mysql;

import com.wmt.framework.mybatis.core.mapper.BaseMapperX;
import com.wmt.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.wmt.module.credit.report.dal.dataobject.ReportFillInfoUserGovItemDO;
import org.apache.ibatis.annotations.Mapper;

import java.util.Collection;
import java.util.List;

/**
 * 报表填报 - 信息使用者机构+政府明细 Mapper
 *
 * @author Auto
 */
@Mapper
public interface ReportFillInfoUserGovItemMapper extends BaseMapperX<ReportFillInfoUserGovItemDO> {

    default List<ReportFillInfoUserGovItemDO> selectAll() {
        return selectList(new LambdaQueryWrapperX<ReportFillInfoUserGovItemDO>());
    }
}
