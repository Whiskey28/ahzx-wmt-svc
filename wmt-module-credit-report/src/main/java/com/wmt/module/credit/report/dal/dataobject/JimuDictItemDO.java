package com.wmt.module.credit.report.dal.dataobject;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 积木报表字典项 DO
 *
 * 主要用于在代码端读取积木字典（如信息来源情况）并返回给报表数据接口。
 *
 * @author Auto
 */
@TableName("jimu_dict_item")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class JimuDictItemDO {

    /**
     * 主键
     */
    @TableId
    private String id;

    /**
     * 字典 ID（关联 jimu_dict.id）
     */
    private String dictId;

    /**
     * 字典项名称
     */
    private String itemText;

    /**
     * 字典项值
     */
    private String itemValue;

    /**
     * 描述
     */
    private String description;

    /**
     * 排序值
     */
    private Integer sortOrder;

    /**
     * 状态（1 启用，0 停用）
     */
    private Integer status;

}
