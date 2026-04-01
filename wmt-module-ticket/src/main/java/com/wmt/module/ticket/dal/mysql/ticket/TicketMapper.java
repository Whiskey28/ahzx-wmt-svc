package com.wmt.module.ticket.dal.mysql.ticket;

import com.wmt.framework.common.pojo.PageResult;
import com.wmt.framework.mybatis.core.mapper.BaseMapperX;
import com.wmt.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.wmt.module.ticket.controller.admin.ticket.vo.AdminTicketPageReqVO;
import com.wmt.module.ticket.controller.app.ticket.vo.AppTicketPageReqVO;
import com.wmt.module.ticket.dal.dataobject.ticket.TicketDO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface TicketMapper extends BaseMapperX<TicketDO> {

    default PageResult<TicketDO> selectAppPage(Long userId, AppTicketPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<TicketDO>()
                .eq(TicketDO::getUserId, userId)
                .eqIfPresent(TicketDO::getStatus, reqVO.getStatus())
                .orderByDesc(TicketDO::getId));
    }

    default PageResult<TicketDO> selectAdminPage(AdminTicketPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<TicketDO>()
                .likeIfPresent(TicketDO::getTicketNo, reqVO.getTicketNo())
                .likeIfPresent(TicketDO::getTitle, reqVO.getTitle())
                .eqIfPresent(TicketDO::getStatus, reqVO.getStatus())
                .eqIfPresent(TicketDO::getCategoryId, reqVO.getCategoryId())
                .eqIfPresent(TicketDO::getAssignedAdminUserId, reqVO.getAssignedAdminUserId())
                .eqIfPresent(TicketDO::getUserId, reqVO.getUserId())
                .orderByDesc(TicketDO::getId));
    }

    default TicketDO selectByIdAndUserId(Long id, Long userId) {
        return selectOne(new LambdaQueryWrapperX<TicketDO>()
                .eq(TicketDO::getId, id)
                .eq(TicketDO::getUserId, userId));
    }
}
