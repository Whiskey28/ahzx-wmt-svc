package com.wmt.module.ticket.dal.dataobject.category;

import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.wmt.framework.mybatis.core.dataobject.BaseDO;
import lombok.Data;
import lombok.EqualsAndHashCode;

@TableName("ticket_category")
@KeySequence("ticket_category_seq")
@Data
@EqualsAndHashCode(callSuper = true)
public class TicketCategoryDO extends BaseDO {

    @TableId
    private Long id;

    private String name;

    private Integer sort;

    private Integer status;
}
