package com.wmt.module.infra.dal.mysql.db;

import com.wmt.framework.mybatis.core.mapper.BaseMapperX;
import com.wmt.module.infra.dal.dataobject.db.DataSourceConfigDO;
import org.apache.ibatis.annotations.Mapper;

/**
 * 数据源配置 Mapper
 *
 * @author AHC源码
 */
@Mapper
public interface DataSourceConfigMapper extends BaseMapperX<DataSourceConfigDO> {
}
