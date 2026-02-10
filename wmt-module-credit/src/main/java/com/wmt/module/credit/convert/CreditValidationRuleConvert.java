package com.wmt.module.credit.convert;

import com.wmt.framework.common.pojo.PageResult;
import com.wmt.module.credit.controller.admin.validation.vo.CreditValidationRuleRespVO;
import com.wmt.module.credit.controller.admin.validation.vo.CreditValidationRuleSaveReqVO;
import com.wmt.module.credit.dal.dataobject.validation.CreditValidationRuleDO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * 征信校验规则 Convert
 *
 * @author AHC源码
 */
@Mapper
public interface CreditValidationRuleConvert {

    CreditValidationRuleConvert INSTANCE = Mappers.getMapper(CreditValidationRuleConvert.class);

    CreditValidationRuleRespVO convert(CreditValidationRuleDO bean);

    CreditValidationRuleDO convert(CreditValidationRuleSaveReqVO bean);

    List<CreditValidationRuleRespVO> convertList(List<CreditValidationRuleDO> list);

    PageResult<CreditValidationRuleRespVO> convertPage(PageResult<CreditValidationRuleDO> page);

}
