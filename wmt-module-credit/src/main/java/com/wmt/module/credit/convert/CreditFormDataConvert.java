package com.wmt.module.credit.convert;

import com.wmt.framework.common.pojo.PageResult;
import com.wmt.module.credit.controller.admin.form.vo.CreditFormDataRespVO;
import com.wmt.module.credit.controller.admin.form.vo.CreditFormDataSaveReqVO;
import com.wmt.module.credit.dal.dataobject.form.CreditFormDataDO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * 征信表单数据 Convert
 *
 * @author AHC源码
 */
@Mapper
public interface CreditFormDataConvert {

    CreditFormDataConvert INSTANCE = Mappers.getMapper(CreditFormDataConvert.class);

    CreditFormDataRespVO convert(CreditFormDataDO bean);

    CreditFormDataDO convert(CreditFormDataSaveReqVO bean);

    List<CreditFormDataRespVO> convertList(List<CreditFormDataDO> list);

    PageResult<CreditFormDataRespVO> convertPage(PageResult<CreditFormDataDO> page);

}
