package com.wmt.module.ticket.dal.mysql.category;

import com.wmt.framework.mybatis.core.mapper.BaseMapperX;
import com.wmt.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.wmt.module.ticket.dal.dataobject.category.TicketCategoryDO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface TicketCategoryMapper extends BaseMapperX<TicketCategoryDO> {

    default List<TicketCategoryDO> selectEnabledList() {
        return selectList(new LambdaQueryWrapperX<TicketCategoryDO>()
                .eq(TicketCategoryDO::getStatus, 0)
                .orderByAsc(TicketCategoryDO::getSort)
                .orderByAsc(TicketCategoryDO::getId));
    }
}
