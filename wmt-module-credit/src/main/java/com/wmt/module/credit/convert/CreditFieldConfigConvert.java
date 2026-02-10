package com.wmt.module.credit.convert;

import com.wmt.framework.common.pojo.PageResult;
import com.wmt.module.credit.controller.admin.field.vo.CreditFieldConfigRespVO;
import com.wmt.module.credit.controller.admin.field.vo.CreditFieldConfigSaveReqVO;
import com.wmt.module.credit.dal.dataobject.field.CreditFieldConfigDO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * 征信字段配置 Convert
 *
 * @author AHC源码
 */
@Mapper
public interface CreditFieldConfigConvert {

    CreditFieldConfigConvert INSTANCE = Mappers.getMapper(CreditFieldConfigConvert.class);

    CreditFieldConfigRespVO convert(CreditFieldConfigDO bean);

    CreditFieldConfigDO convert(CreditFieldConfigSaveReqVO bean);

    List<CreditFieldConfigRespVO> convertList(List<CreditFieldConfigDO> list);

    PageResult<CreditFieldConfigRespVO> convertPage(PageResult<CreditFieldConfigDO> page);

}
