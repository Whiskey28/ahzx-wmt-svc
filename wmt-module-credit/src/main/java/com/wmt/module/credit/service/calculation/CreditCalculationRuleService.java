package com.wmt.module.credit.service.calculation;

import com.wmt.framework.common.pojo.PageResult;
import com.wmt.module.credit.controller.admin.calculation.vo.CreditCalculationRulePageReqVO;
import com.wmt.module.credit.controller.admin.calculation.vo.CreditCalculationRuleRespVO;
import com.wmt.module.credit.controller.admin.calculation.vo.CreditCalculationRuleSaveReqVO;
import com.wmt.module.credit.dal.dataobject.calculation.CreditCalculationRuleDO;
import jakarta.validation.Valid;

import java.util.List;

/**
 * 征信计算规则 Service 接口
 *
 * @author AHC源码
 */
public interface CreditCalculationRuleService {

    /**
     * 创建计算规则
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    Long createCalculationRule(@Valid CreditCalculationRuleSaveReqVO createReqVO);

    /**
     * 更新计算规则
     *
     * @param updateReqVO 更新信息
     */
    void updateCalculationRule(@Valid CreditCalculationRuleSaveReqVO updateReqVO);

    /**
     * 删除计算规则
     *
     * @param id 编号
     */
    void deleteCalculationRule(Long id);

    /**
     * 获得计算规则
     *
     * @param id 编号
     * @return 计算规则
     */
    CreditCalculationRuleDO getCalculationRule(Long id);

    /**
     * 获得计算规则分页
     *
     * @param pageReqVO 分页查询
     * @return 计算规则分页
     */
    PageResult<CreditCalculationRuleRespVO> getCalculationRulePage(CreditCalculationRulePageReqVO pageReqVO);

    /**
     * 根据报表类型查询计算规则列表（按计算顺序排序）
     *
     * @param reportType 报表类型
     * @return 计算规则列表
     */
    List<CreditCalculationRuleDO> getRulesByReportType(String reportType);

}
