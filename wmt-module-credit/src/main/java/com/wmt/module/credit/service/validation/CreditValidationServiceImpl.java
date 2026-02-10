package com.wmt.module.credit.service.validation;

import com.wmt.module.credit.dal.dataobject.form.CreditFormDataDO;
import com.wmt.module.credit.dal.dataobject.validation.CreditValidationRuleDO;
import com.wmt.module.credit.dal.mysql.validation.CreditValidationRuleMapper;
import com.wmt.module.credit.enums.ErrorCodeConstants;
import com.wmt.module.credit.framework.validator.CreditValidationResult;
import com.wmt.module.credit.framework.validator.CreditValidator;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

import static com.wmt.framework.common.exception.util.ServiceExceptionUtil.exception;

/**
 * 征信校验 Service 实现类
 *
 * @author AHC源码
 */
@Service
@Slf4j
public class CreditValidationServiceImpl implements CreditValidationService {

    @Resource
    private CreditValidationRuleMapper validationRuleMapper;

    @Resource
    private List<CreditValidator> validators;

    @Override
    public void validateFormData(CreditFormDataDO formData) {
        if (formData == null || formData.getFormData() == null) {
            return;
        }

        Map<String, Object> formDataMap = formData.getFormData();
        for (Map.Entry<String, Object> entry : formDataMap.entrySet()) {
            String fieldCode = entry.getKey();
            Object value = entry.getValue();
            validateField(fieldCode, value, formDataMap);
        }
    }

    @Override
    public void validateField(String fieldCode, Object value, Map<String, Object> formData) {
        // 1. 加载该字段的校验规则（按优先级排序）
        List<CreditValidationRuleDO> rules = validationRuleMapper.selectListByFieldCode(fieldCode);
        if (rules.isEmpty()) {
            // 没有配置校验规则，直接通过
            return;
        }

        // 2. 按优先级依次执行校验
        for (CreditValidationRuleDO rule : rules) {
            // 找到支持该规则类型的校验器
            CreditValidator validator = findValidator(rule.getRuleType());
            if (validator == null) {
                log.warn("字段 {} 的校验规则类型 {} 没有对应的校验器", fieldCode, rule.getRuleType());
                continue;
            }

            // 执行校验
            CreditValidationResult result = validator.validate(fieldCode, value, rule, formData);
            if (!result.isPassed()) {
                // 校验失败，抛出异常
                throw exception(ErrorCodeConstants.VALIDATION_FAIL, result.getErrorMessage());
            }
        }
    }

    /**
     * 查找支持指定规则类型的校验器
     *
     * @param ruleType 规则类型
     * @return 校验器，如果找不到返回null
     */
    private CreditValidator findValidator(String ruleType) {
        if (validators == null || validators.isEmpty()) {
            return null;
        }
        for (CreditValidator validator : validators) {
            if (validator.supports(ruleType)) {
                return validator;
            }
        }
        return null;
    }

}
