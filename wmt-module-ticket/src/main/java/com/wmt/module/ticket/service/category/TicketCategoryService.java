package com.wmt.module.ticket.service.category;

import com.wmt.module.ticket.dal.dataobject.category.TicketCategoryDO;

import java.util.List;

public interface TicketCategoryService {

    List<TicketCategoryDO> getEnableCategoryList();

    TicketCategoryDO validateCategory(Long categoryId);
}
