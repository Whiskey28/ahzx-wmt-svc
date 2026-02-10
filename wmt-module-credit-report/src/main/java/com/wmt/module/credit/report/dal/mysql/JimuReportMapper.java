package com.wmt.module.credit.report.dal.mysql;

import com.wmt.framework.mybatis.core.mapper.BaseMapperX;
import com.wmt.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.wmt.module.credit.report.dal.dataobject.JimuReportDO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 积木报表模板 Mapper
 *
 * @author AHC源码
 */
@Mapper
public interface JimuReportMapper extends BaseMapperX<JimuReportDO> {

    /**
     * 根据分类id查询填报报表模板列表
     *
     * @param categoryId 分类id
     * @return 报表模板列表（默认只查询在线填报表单，submitForm=1）
     */
    default List<JimuReportDO> selectListByCategoryId(String categoryId) {
        return selectListByCategoryId(categoryId, 1);
    }

    /**
     * 根据分类id和submitForm查询报表模板列表
     *
     * @param categoryId 分类id
     * @param submitForm 是否填报报表（0-数据报表，1-在线填报表单）。如果为null，则不过滤submitForm
     * @return 报表模板列表
     */
    default List<JimuReportDO> selectListByCategoryId(String categoryId, Integer submitForm) {
        LambdaQueryWrapperX<JimuReportDO> wrapper = new LambdaQueryWrapperX<JimuReportDO>()
                .eq(JimuReportDO::getType, categoryId)
                .eq(JimuReportDO::getDelFlag, 0)
                .orderByDesc(JimuReportDO::getCreateTime);
        if (submitForm != null) {
            wrapper.eq(JimuReportDO::getSubmitForm, submitForm);
        }
        return selectList(wrapper);
    }

}
