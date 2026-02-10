package com.wmt.module.credit.dal.mysql.field;

import com.wmt.framework.common.pojo.PageResult;
import com.wmt.framework.mybatis.core.mapper.BaseMapperX;
import com.wmt.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.wmt.module.credit.controller.admin.field.vo.CreditFieldConfigPageReqVO;
import com.wmt.module.credit.dal.dataobject.field.CreditFieldConfigDO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 征信字段配置 Mapper
 *
 * @author AHC源码
 */
@Mapper
public interface CreditFieldConfigMapper extends BaseMapperX<CreditFieldConfigDO> {

    /**
     * 分页查询字段配置
     *
     * @param pageReqVO 分页查询条件
     * @return 分页结果
     */
    default PageResult<CreditFieldConfigDO> selectPage(CreditFieldConfigPageReqVO pageReqVO) {
        return selectPage(pageReqVO, new LambdaQueryWrapperX<CreditFieldConfigDO>()
                .eqIfPresent(CreditFieldConfigDO::getDeptId, pageReqVO.getDeptId())
                .likeIfPresent(CreditFieldConfigDO::getFieldCode, pageReqVO.getFieldCode())
                .likeIfPresent(CreditFieldConfigDO::getFieldName, pageReqVO.getFieldName())
                .eqIfPresent(CreditFieldConfigDO::getFieldType, pageReqVO.getFieldType())
                .eqIfPresent(CreditFieldConfigDO::getStatus, pageReqVO.getStatus())
                .orderByAsc(CreditFieldConfigDO::getDisplayOrder)
                .orderByDesc(CreditFieldConfigDO::getId));
    }

    /**
     * 根据部门ID查询字段配置列表
     *
     * @param deptId 部门ID（0表示通用字段）
     * @return 字段配置列表
     */
    default List<CreditFieldConfigDO> selectListByDeptId(Long deptId) {
        return selectList(new LambdaQueryWrapperX<CreditFieldConfigDO>()
                .and(wrapper -> wrapper.eq(CreditFieldConfigDO::getDeptId, 0L)
                        .or().eq(CreditFieldConfigDO::getDeptId, deptId))
                .eq(CreditFieldConfigDO::getStatus, 1)
                .orderByAsc(CreditFieldConfigDO::getDisplayOrder));
    }

    /**
     * 根据报表类型查询字段配置列表
     *
     * @param reportType 报表类型（可选，如果为null则查询所有）
     * @param deptId 部门ID（可选）
     * @return 字段配置列表
     */
    default List<CreditFieldConfigDO> selectListByReportType(String reportType, Long deptId) {
        LambdaQueryWrapperX<CreditFieldConfigDO> wrapper = new LambdaQueryWrapperX<CreditFieldConfigDO>()
                .eq(CreditFieldConfigDO::getStatus, 1);
        
        if (deptId != null) {
            wrapper.and(w -> w.eq(CreditFieldConfigDO::getDeptId, 0L)
                    .or().eq(CreditFieldConfigDO::getDeptId, deptId));
        }
        
        return selectList(wrapper.orderByAsc(CreditFieldConfigDO::getDisplayOrder));
    }

    /**
     * 根据部门和字段编码查询字段配置
     *
     * @param deptId 部门ID
     * @param fieldCode 字段编码
     * @return 字段配置
     */
    default CreditFieldConfigDO selectByDeptIdAndFieldCode(Long deptId, String fieldCode) {
        return selectOne(new LambdaQueryWrapperX<CreditFieldConfigDO>()
                .eq(CreditFieldConfigDO::getDeptId, deptId)
                .eq(CreditFieldConfigDO::getFieldCode, fieldCode));
    }

}
