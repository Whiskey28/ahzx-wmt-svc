package com.wmt.module.credit.convert;

import com.wmt.framework.common.pojo.PageResult;
import com.wmt.module.credit.controller.admin.calculation.vo.CreditCalculationRuleRespVO;
import com.wmt.module.credit.controller.admin.calculation.vo.CreditCalculationRuleSaveReqVO;
import com.wmt.module.credit.dal.dataobject.calculation.CreditCalculationRuleDO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * 征信计算规则 Convert
 *
 * @author AHC源码
 */
@Mapper
public interface CreditCalculationRuleConvert {

    CreditCalculationRuleConvert INSTANCE = Mappers.getMapper(CreditCalculationRuleConvert.class);

    CreditCalculationRuleRespVO convert(CreditCalculationRuleDO bean);

    CreditCalculationRuleDO convert(CreditCalculationRuleSaveReqVO bean);

    List<CreditCalculationRuleRespVO> convertList(List<CreditCalculationRuleDO> list);

    PageResult<CreditCalculationRuleRespVO> convertPage(PageResult<CreditCalculationRuleDO> page);

}
