package com.wmt.module.credit.dal.mysql.validation;

import com.wmt.framework.common.pojo.PageResult;
import com.wmt.framework.mybatis.core.mapper.BaseMapperX;
import com.wmt.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.wmt.module.credit.controller.admin.validation.vo.CreditValidationRulePageReqVO;
import com.wmt.module.credit.dal.dataobject.validation.CreditValidationRuleDO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 征信校验规则配置 Mapper
 *
 * @author AHC源码
 */
@Mapper
public interface CreditValidationRuleMapper extends BaseMapperX<CreditValidationRuleDO> {

    /**
     * 分页查询校验规则
     *
     * @param pageReqVO 分页查询条件
     * @return 分页结果
     */
    default PageResult<CreditValidationRuleDO> selectPage(CreditValidationRulePageReqVO pageReqVO) {
        return selectPage(pageReqVO, new LambdaQueryWrapperX<CreditValidationRuleDO>()
                .eqIfPresent(CreditValidationRuleDO::getFieldCode, pageReqVO.getFieldCode())
                .eqIfPresent(CreditValidationRuleDO::getRuleType, pageReqVO.getRuleType())
                .eqIfPresent(CreditValidationRuleDO::getStatus, pageReqVO.getStatus())
                .orderByAsc(CreditValidationRuleDO::getPriority)
                .orderByDesc(CreditValidationRuleDO::getId));
    }

    /**
     * 根据字段编码查询校验规则列表（按优先级排序）
     *
     * @param fieldCode 字段编码
     * @return 校验规则列表
     */
    default List<CreditValidationRuleDO> selectListByFieldCode(String fieldCode) {
        return selectList(new LambdaQueryWrapperX<CreditValidationRuleDO>()
                .eq(CreditValidationRuleDO::getFieldCode, fieldCode)
                .eq(CreditValidationRuleDO::getStatus, 1)
                .orderByAsc(CreditValidationRuleDO::getPriority));
    }

}
