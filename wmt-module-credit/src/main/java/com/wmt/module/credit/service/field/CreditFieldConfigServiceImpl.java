package com.wmt.module.credit.service.field;

import com.wmt.framework.common.exception.util.ServiceExceptionUtil;
import com.wmt.framework.common.pojo.PageResult;
import com.wmt.module.credit.controller.admin.field.vo.CreditFieldConfigPageReqVO;
import com.wmt.module.credit.controller.admin.field.vo.CreditFieldConfigRespVO;
import com.wmt.module.credit.controller.admin.field.vo.CreditFieldConfigSaveReqVO;
import com.wmt.module.credit.convert.CreditFieldConfigConvert;
import com.wmt.module.credit.dal.dataobject.field.CreditFieldConfigDO;
import com.wmt.module.credit.dal.mysql.field.CreditFieldConfigMapper;
import com.wmt.module.credit.enums.ErrorCodeConstants;
import com.wmt.module.system.api.dept.DeptApi;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.util.List;

import static com.wmt.framework.common.exception.util.ServiceExceptionUtil.exception;

/**
 * 征信字段配置 Service 实现类
 *
 * @author AHC源码
 */
@Service
@Validated
@Slf4j
public class CreditFieldConfigServiceImpl implements CreditFieldConfigService {

    @Resource
    private CreditFieldConfigMapper fieldConfigMapper;

    @Resource
    private DeptApi deptApi;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long createFieldConfig(CreditFieldConfigSaveReqVO createReqVO) {
        // 1. 校验部门ID存在性（如果deptId不为0）
        if (createReqVO.getDeptId() != null && createReqVO.getDeptId() != 0) {
            deptApi.validateDeptList(List.of(createReqVO.getDeptId()));
        }
        // 2. 校验字段编码唯一性
        validateFieldCodeUnique(null, createReqVO.getDeptId(), createReqVO.getFieldCode());
        // 3. 插入字段配置
        CreditFieldConfigDO fieldConfig = CreditFieldConfigConvert.INSTANCE.convert(createReqVO);
        fieldConfigMapper.insert(fieldConfig);
        return fieldConfig.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateFieldConfig(CreditFieldConfigSaveReqVO updateReqVO) {
        // 1. 校验存在
        validateFieldConfigExists(updateReqVO.getId());
        // 2. 校验部门ID存在性（如果deptId不为0）
        if (updateReqVO.getDeptId() != null && updateReqVO.getDeptId() != 0) {
            deptApi.validateDeptList(List.of(updateReqVO.getDeptId()));
        }
        // 3. 校验字段编码唯一性
        validateFieldCodeUnique(updateReqVO.getId(), updateReqVO.getDeptId(), updateReqVO.getFieldCode());
        // 4. 更新字段配置
        CreditFieldConfigDO updateObj = CreditFieldConfigConvert.INSTANCE.convert(updateReqVO);
        fieldConfigMapper.updateById(updateObj);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteFieldConfig(Long id) {
        // 1. 校验存在
        validateFieldConfigExists(id);
        // 2. TODO 校验是否被使用（表单数据、计算规则等）
        // 3. 删除字段配置
        fieldConfigMapper.deleteById(id);
    }

    @Override
    public CreditFieldConfigDO getFieldConfig(Long id) {
        return fieldConfigMapper.selectById(id);
    }

    @Override
    public PageResult<CreditFieldConfigRespVO> getFieldConfigPage(CreditFieldConfigPageReqVO pageReqVO) {
        PageResult<CreditFieldConfigDO> pageResult = fieldConfigMapper.selectPage(pageReqVO);
        return CreditFieldConfigConvert.INSTANCE.convertPage(pageResult);
    }

    @Override
    public List<CreditFieldConfigDO> getFieldConfigList(Long deptId, String reportType) {
        return fieldConfigMapper.selectListByReportType(reportType, deptId);
    }

    /**
     * 校验字段配置是否存在
     *
     * @param id 字段配置编号
     */
    private CreditFieldConfigDO validateFieldConfigExists(Long id) {
        CreditFieldConfigDO fieldConfig = fieldConfigMapper.selectById(id);
        if (fieldConfig == null) {
            throw exception(ErrorCodeConstants.FIELD_CONFIG_NOT_EXISTS);
        }
        return fieldConfig;
    }

    /**
     * 校验字段编码唯一性
     *
     * @param id        字段配置编号（更新时传入）
     * @param deptId    部门ID
     * @param fieldCode 字段编码
     */
    private void validateFieldCodeUnique(Long id, Long deptId, String fieldCode) {
        CreditFieldConfigDO fieldConfig = fieldConfigMapper.selectByDeptIdAndFieldCode(deptId, fieldCode);
        if (fieldConfig == null) {
            return;
        }
        // 如果 id 为空，说明不用比较是否为相同 id 的字段配置
        if (id == null) {
            throw exception(ErrorCodeConstants.FIELD_CONFIG_CODE_DUPLICATE);
        }
        if (!fieldConfig.getId().equals(id)) {
            throw exception(ErrorCodeConstants.FIELD_CONFIG_CODE_DUPLICATE);
        }
    }

}
