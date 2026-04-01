package com.wmt.module.credit.report.dal.mysql;

import com.wmt.framework.mybatis.core.mapper.BaseMapperX;
import com.wmt.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.wmt.module.credit.report.dal.dataobject.ReportFillEnterpriseBasicDO;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Collection;
import java.util.List;

/**
 * 报表填报 - 企业基础信息块 Mapper
 *
 * @author AHC源码
 */
@Mapper
public interface ReportFillEnterpriseBasicMapper extends BaseMapperX<ReportFillEnterpriseBasicDO> {

    @Delete("DELETE FROM report_fill_enterprise_basic WHERE parent_id = #{parentId}")
    int deleteRealByParentId(@Param("parentId") String parentId);

    default List<ReportFillEnterpriseBasicDO> selectListByParentIds(Collection<String> parentIds) {
        return selectList(new LambdaQueryWrapperX<ReportFillEnterpriseBasicDO>()
                .inIfPresent(ReportFillEnterpriseBasicDO::getParentId, parentIds));
    }

    default List<String> selectParentIdsByEnterpriseNameLike(String enterpriseName) {
        return selectList(new LambdaQueryWrapperX<ReportFillEnterpriseBasicDO>()
                .likeIfPresent(ReportFillEnterpriseBasicDO::getEnterpriseName, enterpriseName))
                .stream().map(ReportFillEnterpriseBasicDO::getParentId).toList();
    }

}

