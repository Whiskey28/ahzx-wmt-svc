package com.wmt.module.pfb.dal.mysql;

import com.wmt.framework.mybatis.core.mapper.BaseMapperX;
import com.wmt.module.pfb.dal.dataobject.PfbLoanApplicationDO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface PfbLoanApplicationMapper extends BaseMapperX<PfbLoanApplicationDO> {
}
