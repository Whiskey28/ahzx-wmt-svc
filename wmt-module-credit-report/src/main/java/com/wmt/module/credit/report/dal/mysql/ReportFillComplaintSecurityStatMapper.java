package com.wmt.module.credit.report.dal.mysql;

import com.wmt.framework.mybatis.core.mapper.BaseMapperX;
import com.wmt.module.credit.report.dal.dataobject.ReportFillComplaintSecurityStatDO;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 报表填报 - 异议投诉处理与信息安全情况 Mapper
 *
 * @author AHC源码
 */
@Mapper
public interface ReportFillComplaintSecurityStatMapper extends BaseMapperX<ReportFillComplaintSecurityStatDO> {

    @Delete("DELETE FROM report_fill_complaint_security_stat WHERE parent_id = #{parentId}")
    int deleteRealByParentId(@Param("parentId") String parentId);
}

