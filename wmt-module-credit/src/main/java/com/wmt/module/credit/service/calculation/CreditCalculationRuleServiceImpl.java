package com.wmt.module.credit.service.calculation;

import com.wmt.framework.common.pojo.PageResult;
import com.wmt.module.credit.controller.admin.calculation.vo.CreditCalculationRulePageReqVO;
import com.wmt.module.credit.controller.admin.calculation.vo.CreditCalculationRuleRespVO;
import com.wmt.module.credit.controller.admin.calculation.vo.CreditCalculationRuleSaveReqVO;
import com.wmt.module.credit.convert.CreditCalculationRuleConvert;
import com.wmt.module.credit.dal.dataobject.calculation.CreditCalculationRuleDO;
import com.wmt.module.credit.dal.mysql.calculation.CreditCalculationRuleMapper;
import com.wmt.module.credit.enums.ErrorCodeConstants;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.util.List;

import static com.wmt.framework.common.exception.util.ServiceExceptionUtil.exception;

/**
 * 征信计算规则 Service 实现类
 *
 * @author AHC源码
 */
@Service
@Validated
@Slf4j
public class CreditCalculationRuleServiceImpl implements CreditCalculationRuleService {

    @Resource
    private CreditCalculationRuleMapper calculationRuleMapper;

    /**
     * SpEL 表达式解析器，用于在创建 / 更新规则时做语法校验。
     * 注意：这里只做语法校验，不依赖计算引擎，以避免循环依赖。
     */
    private final ExpressionParser parser = new SpelExpressionParser();

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long createCalculationRule(CreditCalculationRuleSaveReqVO createReqVO) {
        // 1. 校验目标字段编码唯一性（同一报表类型下不能重复）
        validateTargetFieldCodeUnique(null, createReqVO.getTargetFieldCode(), createReqVO.getReportType());
        // 2. 可选：校验SpEL表达式语法
        if (!validateExpression(createReqVO.getRuleExpression())) {
            throw exception(ErrorCodeConstants.CALCULATION_EXPRESSION_INVALID, createReqVO.getRuleExpression());
        }
        // 3. 插入计算规则
        CreditCalculationRuleDO calculationRule = CreditCalculationRuleConvert.INSTANCE.convert(createReqVO);
        calculationRuleMapper.insert(calculationRule);
        return calculationRule.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateCalculationRule(CreditCalculationRuleSaveReqVO updateReqVO) {
        // 1. 校验存在
        validateCalculationRuleExists(updateReqVO.getId());
        // 2. 校验目标字段编码唯一性
        validateTargetFieldCodeUnique(updateReqVO.getId(), updateReqVO.getTargetFieldCode(), updateReqVO.getReportType());
        // 3. 可选：校验SpEL表达式语法
        if (!validateExpression(updateReqVO.getRuleExpression())) {
            throw exception(ErrorCodeConstants.CALCULATION_EXPRESSION_INVALID, updateReqVO.getRuleExpression());
        }
        // 4. 更新计算规则
        CreditCalculationRuleDO updateObj = CreditCalculationRuleConvert.INSTANCE.convert(updateReqVO);
        calculationRuleMapper.updateById(updateObj);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteCalculationRule(Long id) {
        // 1. 校验存在
        validateCalculationRuleExists(id);
        // 2. 删除计算规则
        calculationRuleMapper.deleteById(id);
    }

    @Override
    public CreditCalculationRuleDO getCalculationRule(Long id) {
        return validateCalculationRuleExists(id);
    }

    @Override
    public PageResult<CreditCalculationRuleRespVO> getCalculationRulePage(CreditCalculationRulePageReqVO pageReqVO) {
        PageResult<CreditCalculationRuleDO> pageResult = calculationRuleMapper.selectPage(pageReqVO);
        return CreditCalculationRuleConvert.INSTANCE.convertPage(pageResult);
    }

    @Override
    public List<CreditCalculationRuleDO> getRulesByReportType(String reportType) {
        return calculationRuleMapper.selectListByReportType(reportType);
    }

    /**
     * 校验计算规则是否存在
     *
     * @param id 计算规则编号
     */
    private CreditCalculationRuleDO validateCalculationRuleExists(Long id) {
        CreditCalculationRuleDO calculationRule = calculationRuleMapper.selectById(id);
        if (calculationRule == null) {
            throw exception(ErrorCodeConstants.CALCULATION_RULE_NOT_EXISTS);
        }
        return calculationRule;
    }

    /**
     * 校验目标字段编码唯一性
     *
     * @param id             计算规则编号（更新时传入）
     * @param targetFieldCode 目标字段编码
     * @param reportType     报表类型
     */
    private void validateTargetFieldCodeUnique(Long id, String targetFieldCode, String reportType) {
        CreditCalculationRuleDO calculationRule = calculationRuleMapper.selectByTargetFieldCode(targetFieldCode, reportType);
        if (calculationRule == null) {
            return;
        }
        // 如果 id 为空，说明不用比较是否为相同 id 的计算规则
        if (id == null) {
            throw exception(ErrorCodeConstants.CALCULATION_RULE_TARGET_FIELD_DUPLICATE);
        }
        if (!calculationRule.getId().equals(id)) {
            throw exception(ErrorCodeConstants.CALCULATION_RULE_TARGET_FIELD_DUPLICATE);
        }
    }

    /**
     * 校验 SpEL 表达式语法是否有效。
     * 这里只做语法解析，不实际执行，避免引入计算引擎依赖导致循环注入。
     */
    private boolean validateExpression(String expression) {
        try {
            Expression expr = parser.parseExpression(expression);
            // 仅解析即可，如果有语法错误会抛异常
            return expr != null;
        } catch (Exception e) {
            log.warn("SpEL 表达式语法错误：{}", expression, e);
            return false;
        }
    }

}
