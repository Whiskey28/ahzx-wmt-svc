package com.wmt.module.credit.service.field;

import com.wmt.framework.common.pojo.PageResult;
import com.wmt.module.credit.controller.admin.field.vo.CreditFieldConfigPageReqVO;
import com.wmt.module.credit.controller.admin.field.vo.CreditFieldConfigRespVO;
import com.wmt.module.credit.controller.admin.field.vo.CreditFieldConfigSaveReqVO;
import com.wmt.module.credit.dal.dataobject.field.CreditFieldConfigDO;
import jakarta.validation.Valid;

import java.util.List;

/**
 * 征信字段配置 Service 接口
 *
 * @author AHC源码
 */
public interface CreditFieldConfigService {

    /**
     * 创建字段配置
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    Long createFieldConfig(@Valid CreditFieldConfigSaveReqVO createReqVO);

    /**
     * 更新字段配置
     *
     * @param updateReqVO 更新信息
     */
    void updateFieldConfig(@Valid CreditFieldConfigSaveReqVO updateReqVO);

    /**
     * 删除字段配置
     *
     * @param id 编号
     */
    void deleteFieldConfig(Long id);

    /**
     * 获得字段配置
     *
     * @param id 编号
     * @return 字段配置
     */
    CreditFieldConfigDO getFieldConfig(Long id);

    /**
     * 获得字段配置分页
     *
     * @param pageReqVO 分页查询
     * @return 字段配置分页
     */
    PageResult<CreditFieldConfigRespVO> getFieldConfigPage(CreditFieldConfigPageReqVO pageReqVO);

    /**
     * 获得字段配置列表（根据部门ID和报表类型）
     *
     * @param deptId     部门ID（0表示通用字段）
     * @param reportType 报表类型（可选）
     * @return 字段配置列表
     */
    List<CreditFieldConfigDO> getFieldConfigList(Long deptId, String reportType);

}
