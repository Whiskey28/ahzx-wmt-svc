package com.wmt.module.ticket.dal.mysql.reply;

import com.wmt.framework.mybatis.core.mapper.BaseMapperX;
import com.wmt.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.wmt.module.ticket.dal.dataobject.reply.TicketReplyDO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface TicketReplyMapper extends BaseMapperX<TicketReplyDO> {

    default List<TicketReplyDO> selectListByTicketId(Long ticketId) {
        return selectList(new LambdaQueryWrapperX<TicketReplyDO>()
                .eq(TicketReplyDO::getTicketId, ticketId)
                .orderByAsc(TicketReplyDO::getId));
    }
}
