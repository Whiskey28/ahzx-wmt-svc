package com.wmt.module.credit.report.dal.mysql;

import com.wmt.framework.mybatis.core.mapper.BaseMapperX;
import com.wmt.module.credit.report.dal.dataobject.ReportFillYangtzeCreditChainDO;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.math.BigDecimal;

/**
 * 报表填报 - 长三角征信链 Mapper
 * <p>
 * 统计方法使用标准 SQL（COALESCE/SUM/JOIN），PostgreSQL、MySQL 通用；
 * 若换为其他数据源，仅需检查本 Mapper 中的 SQL 是否有方言差异。
 *
 * @author Auto
 */
@Mapper
public interface ReportFillYangtzeCreditChainMapper extends BaseMapperX<ReportFillYangtzeCreditChainDO> {

    @Delete("DELETE FROM report_fill_yangtze_credit_chain WHERE record_id = #{recordId}")
    int deleteRealByRecordId(@Param("recordId") String recordId);

    /**
     * 统计指定周期 + 报表模板 + 角色 下，长三角征信链 7 个字段的总和
     * <p>
     * SQL 为标准语法，无需随 PG/MySQL 切换而修改。
     *
     * @param periodId 周期ID
     * @param reportId 报表模板ID
     * @param roleId   角色ID（例如 208：数据管理中心）
     * @return 总和（可能为 null，调用方需处理为 0）
     */
    @Select("""
            SELECT COALESCE(SUM(
                           COALESCE(y.yangtze_industry_crawler, 0)
                         + COALESCE(y.yangtze_industry_purchase, 0)
                         + COALESCE(y.yangtze_tax_platform, 0)
                         + COALESCE(y.yangtze_judicial_purchase, 0)
                         + COALESCE(y.yangtze_tax_violation_crawler, 0)
                         + COALESCE(y.yangtze_double_publicity_crawler, 0)
                         + COALESCE(y.yangtze_national_risk_purchase, 0)
                   ), 0) AS total
            FROM report_fill_yangtze_credit_chain y
                     JOIN report_fill_basic_info b ON y.record_id = b.id
            WHERE b.deleted = 0
              AND y.deleted = 0
              AND b.period_id = #{periodId}
              AND b.report_id = #{reportId}
              AND b.role_id = #{roleId}
            """)
    BigDecimal selectTotalCountByPeriodAndReportAndRole(@Param("periodId") String periodId,
                                                        @Param("reportId") String reportId,
                                                        @Param("roleId") String roleId);
}

