package com.wmt.module.credit.report.dal.mysql;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.wmt.framework.mybatis.core.mapper.BaseMapperX;
import com.wmt.module.credit.report.dal.dataobject.JimuDictItemDO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 积木报表字典项 Mapper
 *
 * @author Auto
 */
@Mapper
public interface JimuDictItemMapper extends BaseMapperX<JimuDictItemDO> {

    /**
     * 根据字典 ID 查询启用的字典项列表，按 sortOrder 升序
     */
    default List<JimuDictItemDO> selectEnabledListByDictId(String dictId) {
        LambdaQueryWrapper<JimuDictItemDO> wrapper = new LambdaQueryWrapper<JimuDictItemDO>()
                .eq(JimuDictItemDO::getDictId, dictId)
                .eq(JimuDictItemDO::getStatus, 1)
                .orderByAsc(JimuDictItemDO::getSortOrder);
        return selectList(wrapper);
    }
}
