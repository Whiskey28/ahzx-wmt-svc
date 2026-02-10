package com.wmt.module.credit.dal.mysql.calculation;

import com.wmt.framework.common.pojo.PageResult;
import com.wmt.framework.mybatis.core.mapper.BaseMapperX;
import com.wmt.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.wmt.module.credit.controller.admin.calculation.vo.CreditCalculationRulePageReqVO;
import com.wmt.module.credit.dal.dataobject.calculation.CreditCalculationRuleDO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 征信计算规则配置 Mapper
 *
 * @author AHC源码
 */
@Mapper
public interface CreditCalculationRuleMapper extends BaseMapperX<CreditCalculationRuleDO> {

    /**
     * 分页查询计算规则
     *
     * @param pageReqVO 分页查询条件
     * @return 分页结果
     */
    default PageResult<CreditCalculationRuleDO> selectPage(CreditCalculationRulePageReqVO pageReqVO) {
        return selectPage(pageReqVO, new LambdaQueryWrapperX<CreditCalculationRuleDO>()
                .eqIfPresent(CreditCalculationRuleDO::getTargetFieldCode, pageReqVO.getTargetFieldCode())
                .eqIfPresent(CreditCalculationRuleDO::getReportType, pageReqVO.getReportType())
                .eqIfPresent(CreditCalculationRuleDO::getRuleType, pageReqVO.getRuleType())
                .eqIfPresent(CreditCalculationRuleDO::getStatus, pageReqVO.getStatus())
                .orderByAsc(CreditCalculationRuleDO::getCalculationOrder)
                .orderByDesc(CreditCalculationRuleDO::getId));
    }

    /**
     * 根据报表类型查询计算规则列表（按计算顺序排序）
     *
     * @param reportType 报表类型
     * @return 计算规则列表
     */
    default List<CreditCalculationRuleDO> selectListByReportType(String reportType) {
        return selectList(new LambdaQueryWrapperX<CreditCalculationRuleDO>()
                .eq(CreditCalculationRuleDO::getReportType, reportType)
                .eq(CreditCalculationRuleDO::getStatus, 1)
                .orderByAsc(CreditCalculationRuleDO::getCalculationOrder));
    }

    /**
     * 根据目标字段编码查询计算规则
     *
     * @param targetFieldCode 目标字段编码
     * @param reportType 报表类型
     * @return 计算规则
     */
    default CreditCalculationRuleDO selectByTargetFieldCode(String targetFieldCode, String reportType) {
        return selectOne(new LambdaQueryWrapperX<CreditCalculationRuleDO>()
                .eq(CreditCalculationRuleDO::getTargetFieldCode, targetFieldCode)
                .eq(CreditCalculationRuleDO::getReportType, reportType)
                .eq(CreditCalculationRuleDO::getStatus, 1));
    }

}
