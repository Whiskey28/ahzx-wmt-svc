package com.wmt.module.credit.report.dal.mysql;

import com.wmt.framework.mybatis.core.mapper.BaseMapperX;
import com.wmt.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.wmt.module.credit.report.dal.dataobject.ReportFillProductStatDO;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 报表填报 - 提供的征信产品/服务次数 Mapper
 *
 * @author AHC源码
 */
@Mapper
public interface ReportFillProductStatMapper extends BaseMapperX<ReportFillProductStatDO> {

    @Delete("DELETE FROM report_fill_product_stat WHERE record_id = #{recordId}")
    int deleteRealByRecordId(@Param("recordId") String recordId);

    default List<ReportFillProductStatDO> selectListByRecordId(String recordId) {
        return selectList(new LambdaQueryWrapperX<ReportFillProductStatDO>()
                .eq(ReportFillProductStatDO::getRecordId, recordId));
    }

    /**
     * 查询多个记录的征信产品/服务次数（用于聚合）
     *
     * @param recordIds 记录ID列表
     * @return 数据列表
     */
    default List<ReportFillProductStatDO> selectListByRecordIds(Collection<String> recordIds) {
        if (recordIds == null || recordIds.isEmpty()) {
            return List.of();
        }
        return selectList(new LambdaQueryWrapperX<ReportFillProductStatDO>()
                .in(ReportFillProductStatDO::getRecordId, recordIds));
    }



}

