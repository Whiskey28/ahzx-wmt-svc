package com.wmt.module.credit.service.validation;

import com.wmt.framework.common.pojo.PageResult;
import com.wmt.module.credit.controller.admin.validation.vo.CreditValidationRulePageReqVO;
import com.wmt.module.credit.controller.admin.validation.vo.CreditValidationRuleSaveReqVO;
import com.wmt.module.credit.convert.CreditValidationRuleConvert;
import com.wmt.module.credit.dal.dataobject.validation.CreditValidationRuleDO;
import com.wmt.module.credit.dal.mysql.validation.CreditValidationRuleMapper;
import com.wmt.module.credit.enums.ErrorCodeConstants;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import static com.wmt.framework.common.exception.util.ServiceExceptionUtil.exception;

/**
 * 征信校验规则 Service 实现类
 *
 * @author AHC源码
 */
@Service
@Validated
@Slf4j
public class CreditValidationRuleServiceImpl implements CreditValidationRuleService {

    @Resource
    private CreditValidationRuleMapper validationRuleMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long createValidationRule(CreditValidationRuleSaveReqVO createReqVO) {
        // 插入
        CreditValidationRuleDO rule = CreditValidationRuleConvert.INSTANCE.convert(createReqVO);
        validationRuleMapper.insert(rule);
        // 返回
        return rule.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateValidationRule(CreditValidationRuleSaveReqVO updateReqVO) {
        // 校验存在
        validateValidationRuleExists(updateReqVO.getId());
        // 更新
        CreditValidationRuleDO updateObj = CreditValidationRuleConvert.INSTANCE.convert(updateReqVO);
        validationRuleMapper.updateById(updateObj);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteValidationRule(Long id) {
        // 校验存在
        validateValidationRuleExists(id);
        // 删除
        validationRuleMapper.deleteById(id);
    }

    @Override
    public CreditValidationRuleDO getValidationRule(Long id) {
        return validateValidationRuleExists(id);
    }

    @Override
    public PageResult<CreditValidationRuleDO> getValidationRulePage(CreditValidationRulePageReqVO pageReqVO) {
        return validationRuleMapper.selectPage(pageReqVO);
    }

    /**
     * 校验校验规则是否存在
     *
     * @param id 规则编号
     */
    private CreditValidationRuleDO validateValidationRuleExists(Long id) {
        CreditValidationRuleDO rule = validationRuleMapper.selectById(id);
        if (rule == null) {
            throw exception(ErrorCodeConstants.VALIDATION_RULE_NOT_EXISTS);
        }
        return rule;
    }

}
