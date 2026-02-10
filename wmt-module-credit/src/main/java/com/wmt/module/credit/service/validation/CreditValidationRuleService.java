package com.wmt.module.credit.service.validation;

import com.wmt.framework.common.pojo.PageResult;
import com.wmt.module.credit.controller.admin.validation.vo.CreditValidationRulePageReqVO;
import com.wmt.module.credit.controller.admin.validation.vo.CreditValidationRuleSaveReqVO;
import com.wmt.module.credit.dal.dataobject.validation.CreditValidationRuleDO;
import jakarta.validation.Valid;

/**
 * 征信校验规则 Service 接口
 *
 * @author AHC源码
 */
public interface CreditValidationRuleService {

    /**
     * 创建校验规则
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    Long createValidationRule(@Valid CreditValidationRuleSaveReqVO createReqVO);

    /**
     * 更新校验规则
     *
     * @param updateReqVO 更新信息
     */
    void updateValidationRule(@Valid CreditValidationRuleSaveReqVO updateReqVO);

    /**
     * 删除校验规则
     *
     * @param id 编号
     */
    void deleteValidationRule(Long id);

    /**
     * 获得校验规则
     *
     * @param id 编号
     * @return 校验规则
     */
    CreditValidationRuleDO getValidationRule(Long id);

    /**
     * 获得校验规则分页
     *
     * @param pageReqVO 分页查询
     * @return 校验规则分页
     */
    PageResult<CreditValidationRuleDO> getValidationRulePage(CreditValidationRulePageReqVO pageReqVO);

}
