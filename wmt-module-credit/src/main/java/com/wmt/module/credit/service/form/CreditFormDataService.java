package com.wmt.module.credit.service.form;

import com.wmt.framework.common.pojo.PageResult;
import com.wmt.module.credit.controller.admin.form.vo.CreditFormDataPageReqVO;
import com.wmt.module.credit.controller.admin.form.vo.CreditFormDataRespVO;
import com.wmt.module.credit.controller.admin.form.vo.CreditFormDataSaveReqVO;
import com.wmt.module.credit.controller.admin.form.vo.CreditFormDataSubmitReqVO;
import com.wmt.module.credit.dal.dataobject.form.CreditFormDataDO;
import jakarta.validation.Valid;

import java.util.List;
import java.util.Map;

/**
 * 征信表单数据 Service 接口
 *
 * @author AHC源码
 */
public interface CreditFormDataService {

    /**
     * 创建表单数据（草稿状态）
     *
     * @param createReqVO 创建信息
     * @param userId      用户编号
     * @return 编号
     */
    Long createFormData(@Valid CreditFormDataSaveReqVO createReqVO, Long userId);

    /**
     * 更新表单数据（仅草稿状态）
     *
     * @param updateReqVO 更新信息
     * @param userId      用户编号
     */
    void updateFormData(@Valid CreditFormDataSaveReqVO updateReqVO, Long userId);

    /**
     * 提交表单数据
     *
     * @param submitReqVO 提交信息
     * @param userId      用户编号
     */
    void submitFormData(@Valid CreditFormDataSubmitReqVO submitReqVO, Long userId);

    /**
     * 删除表单数据（仅草稿状态）
     *
     * @param id     编号
     * @param userId 用户编号
     */
    void deleteFormData(Long id, Long userId);

    /**
     * 获得表单数据
     *
     * @param id 编号
     * @return 表单数据
     */
    CreditFormDataDO getFormData(Long id);

    /**
     * 获得表单数据分页
     *
     * @param pageReqVO 分页查询
     * @param userId    用户编号（用于数据权限过滤）
     * @return 表单数据分页
     */
    PageResult<CreditFormDataRespVO> getFormDataPage(CreditFormDataPageReqVO pageReqVO, Long userId);

    /**
     * 获取各部门表单数据Map（用于计算）
     *
     * @param reportPeriod 报送周期
     * @param reportType    报表类型
     * @return 各部门表单数据Map，格式：Map<部门ID, Map<字段编码, 字段值>>
     */
    Map<Long, Map<String, Object>> getDeptFormDataMap(String reportPeriod, String reportType);

    /**
     * 获取各部门表单数据列表（用于计算）
     *
     * @param reportPeriod 报送周期
     * @param reportType   报表类型
     * @return 表单数据列表
     */
    List<CreditFormDataDO> getFormDataListForCalculation(String reportPeriod, String reportType);

}
