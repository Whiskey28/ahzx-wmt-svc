package com.wmt.module.ticket.service.category;

import com.wmt.module.ticket.dal.dataobject.category.TicketCategoryDO;
import com.wmt.module.ticket.dal.mysql.category.TicketCategoryMapper;
import com.wmt.module.ticket.enums.ErrorCodeConstants;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.wmt.framework.common.exception.util.ServiceExceptionUtil.exception;

@Service
public class TicketCategoryServiceImpl implements TicketCategoryService {

    @Resource
    private TicketCategoryMapper ticketCategoryMapper;

    @Override
    public List<TicketCategoryDO> getEnableCategoryList() {
        return ticketCategoryMapper.selectEnabledList();
    }

    @Override
    public TicketCategoryDO validateCategory(Long categoryId) {
        TicketCategoryDO category = ticketCategoryMapper.selectById(categoryId);
        if (category == null || !Integer.valueOf(0).equals(category.getStatus())) {
            throw exception(ErrorCodeConstants.TICKET_CATEGORY_NOT_EXISTS);
        }
        return category;
    }
}
