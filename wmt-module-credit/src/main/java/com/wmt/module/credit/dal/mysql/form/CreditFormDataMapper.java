package com.wmt.module.credit.dal.mysql.form;

import com.wmt.framework.common.pojo.PageResult;
import com.wmt.framework.mybatis.core.mapper.BaseMapperX;
import com.wmt.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.wmt.module.credit.controller.admin.form.vo.CreditFormDataPageReqVO;
import com.wmt.module.credit.dal.dataobject.form.CreditFormDataDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 征信表单数据 Mapper
 *
 * @author AHC源码
 */
@Mapper
public interface CreditFormDataMapper extends BaseMapperX<CreditFormDataDO> {

    /**
     * 分页查询表单数据
     *
     * @param pageReqVO 分页查询条件
     * @return 分页结果
     */
    default PageResult<CreditFormDataDO> selectPage(CreditFormDataPageReqVO pageReqVO) {
        return selectPage(pageReqVO, new LambdaQueryWrapperX<CreditFormDataDO>()
                .eqIfPresent(CreditFormDataDO::getDeptId, pageReqVO.getDeptId())
                .eqIfPresent(CreditFormDataDO::getReportPeriod, pageReqVO.getReportPeriod())
                .eqIfPresent(CreditFormDataDO::getReportType, pageReqVO.getReportType())
                .eqIfPresent(CreditFormDataDO::getStatus, pageReqVO.getStatus())
                .orderByDesc(CreditFormDataDO::getCreateTime));
    }

    /**
     * 根据部门和周期查询表单数据
     *
     * @param deptId 部门ID
     * @param reportPeriod 报送周期
     * @param reportType 报表类型
     * @return 表单数据
     */
    default CreditFormDataDO selectByDeptIdAndPeriod(Long deptId, String reportPeriod, String reportType) {
        return selectOne(new LambdaQueryWrapperX<CreditFormDataDO>()
                .eq(CreditFormDataDO::getDeptId, deptId)
                .eq(CreditFormDataDO::getReportPeriod, reportPeriod)
                .eq(CreditFormDataDO::getReportType, reportType));
    }

    /**
     * 根据周期和类型查询表单数据列表
     *
     * @param reportPeriod 报送周期
     * @param reportType 报表类型
     * @return 表单数据列表
     */
    default List<CreditFormDataDO> selectListByPeriodAndType(String reportPeriod, String reportType) {
        return selectList(new LambdaQueryWrapperX<CreditFormDataDO>()
                .eq(CreditFormDataDO::getReportPeriod, reportPeriod)
                .eq(CreditFormDataDO::getReportType, reportType)
                .eq(CreditFormDataDO::getStatus, 1)); // 只查询已提交的
    }

    /**
     * 查询各部门表单数据Map（用于计算）
     * 返回格式：Map<部门ID, Map<字段编码, 字段值>>
     *
     * @param reportPeriod 报送周期
     * @param reportType 报表类型
     * @return 各部门表单数据Map
     */
    default Map<Long, Map<String, Object>> selectDeptFormDataMap(String reportPeriod, String reportType) {
        // 查询所有已提交的表单数据
        List<CreditFormDataDO> formDataList = selectListByPeriodAndType(reportPeriod, reportType);
        
        // 转换为Map格式：Map<部门ID, 表单数据Map>
        return formDataList.stream()
                .collect(java.util.stream.Collectors.toMap(
                        CreditFormDataDO::getDeptId,
                        CreditFormDataDO::getFormData,
                        (existing, replacement) -> existing // 如果有重复的部门ID，保留第一个
                ));
    }

    /**
     * 查询各部门表单数据列表（用于计算）
     * 返回格式：List<CreditFormDataDO>
     *
     * @param reportPeriod 报送周期
     * @param reportType 报表类型
     * @return 表单数据列表
     */
    default List<CreditFormDataDO> selectListForCalculation(String reportPeriod, String reportType) {
        return selectListByPeriodAndType(reportPeriod, reportType);
    }

}
