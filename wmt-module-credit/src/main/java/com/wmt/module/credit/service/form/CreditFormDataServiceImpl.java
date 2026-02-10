package com.wmt.module.credit.service.form;

import com.wmt.framework.common.exception.util.ServiceExceptionUtil;
import com.wmt.framework.common.pojo.PageResult;
import com.wmt.framework.common.util.object.BeanUtils;
import com.wmt.module.credit.controller.admin.form.vo.CreditFormDataPageReqVO;
import com.wmt.module.credit.controller.admin.form.vo.CreditFormDataRespVO;
import com.wmt.module.credit.controller.admin.form.vo.CreditFormDataSaveReqVO;
import com.wmt.module.credit.controller.admin.form.vo.CreditFormDataSubmitReqVO;
import com.wmt.module.credit.convert.CreditFormDataConvert;
import com.wmt.module.credit.dal.dataobject.form.CreditFormDataDO;
import com.wmt.module.credit.dal.mysql.form.CreditFormDataMapper;
import com.wmt.module.credit.enums.CreditBizTypeEnum;
import com.wmt.module.credit.enums.CreditFormStatusEnum;
import com.wmt.module.credit.enums.CreditOperationTypeEnum;
import com.wmt.module.credit.enums.ErrorCodeConstants;
import com.wmt.module.credit.framework.audit.CreditAuditLog;
import com.wmt.module.credit.service.validation.CreditValidationService;
import com.wmt.module.system.api.user.AdminUserApi;
import com.wmt.module.system.api.user.dto.AdminUserRespDTO;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import static com.wmt.framework.common.exception.util.ServiceExceptionUtil.exception;

/**
 * 征信表单数据 Service 实现类
 *
 * @author AHC源码
 */
@Service
@Validated
@Slf4j
public class CreditFormDataServiceImpl implements CreditFormDataService {

    @Resource
    private CreditFormDataMapper formDataMapper;

    @Resource
    private AdminUserApi adminUserApi;

    @Resource
    private CreditValidationService validationService;

    // 审计日志通过AOP切面自动记录，无需手动注入

    @Override
    @Transactional(rollbackFor = Exception.class)
    @CreditAuditLog(bizType = "FORM_DATA", operationType = "CREATE", bizIdExpression = "#result",
            operationDescExpression = "'创建表单数据，部门：' + #createReqVO.deptId + '，周期：' + #createReqVO.reportPeriod")
    public Long createFormData(CreditFormDataSaveReqVO createReqVO, Long userId) {
        // 1. 校验部门权限（部门专员只能创建本部门数据）
        validateDeptPermission(createReqVO.getDeptId(), userId, "创建");
        // 2. 校验该周期是否已存在表单数据
        validateFormDataNotExists(createReqVO.getDeptId(), createReqVO.getReportPeriod(), createReqVO.getReportType());
        // 3. 插入表单数据（草稿状态）
        CreditFormDataDO formData = CreditFormDataConvert.INSTANCE.convert(createReqVO);
        formData.setStatus(CreditFormStatusEnum.DRAFT.getStatus());
        formDataMapper.insert(formData);
        return formData.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @CreditAuditLog(bizType = "FORM_DATA", operationType = "UPDATE", bizIdExpression = "#updateReqVO.id",
            operationDescExpression = "'更新表单数据，编号：' + #updateReqVO.id")
    public void updateFormData(CreditFormDataSaveReqVO updateReqVO, Long userId) {
        // 1. 校验存在
        CreditFormDataDO formData = validateFormDataExists(updateReqVO.getId());
        // 2. 校验部门权限
        validateDeptPermission(formData.getDeptId(), userId, "更新");
        // 3. 校验状态（只能更新草稿状态）
        if (!CreditFormStatusEnum.isDraft(formData.getStatus())) {
            throw exception(ErrorCodeConstants.FORM_DATA_UPDATE_FAIL_NOT_DRAFT);
        }
        // 4. 更新表单数据
        CreditFormDataDO updateObj = CreditFormDataConvert.INSTANCE.convert(updateReqVO);
        formDataMapper.updateById(updateObj);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @CreditAuditLog(bizType = "FORM_DATA", operationType = "SUBMIT", bizIdExpression = "#submitReqVO.id",
            operationDescExpression = "'提交表单数据，编号：' + #submitReqVO.id")
    public void submitFormData(CreditFormDataSubmitReqVO submitReqVO, Long userId) {
        // 1. 校验存在
        CreditFormDataDO formData = validateFormDataExists(submitReqVO.getId());
        // 2. 校验部门权限
        validateDeptPermission(formData.getDeptId(), userId, "提交");
        // 3. 校验状态（只能提交草稿状态）
        if (!CreditFormStatusEnum.isDraft(formData.getStatus())) {
            throw exception(ErrorCodeConstants.FORM_DATA_SUBMIT_FAIL_NOT_DRAFT);
        }
        // 4. 数据校验
        validationService.validateFormData(formData);
        // 5. 更新状态为已提交
        CreditFormDataDO updateObj = new CreditFormDataDO();
        updateObj.setId(formData.getId());
        updateObj.setStatus(CreditFormStatusEnum.SUBMITTED.getStatus());
        updateObj.setSubmitUserId(userId);
        updateObj.setSubmitTime(LocalDateTime.now());
        formDataMapper.updateById(updateObj);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @CreditAuditLog(bizType = "FORM_DATA", operationType = "DELETE", bizIdExpression = "#id",
            operationDescExpression = "'删除表单数据，编号：' + #id")
    public void deleteFormData(Long id, Long userId) {
        // 1. 校验存在
        CreditFormDataDO formData = validateFormDataExists(id);
        // 2. 校验部门权限
        validateDeptPermission(formData.getDeptId(), userId, "删除");
        // 3. 校验状态（只能删除草稿状态）
        if (!CreditFormStatusEnum.isDraft(formData.getStatus())) {
            throw exception(ErrorCodeConstants.FORM_DATA_DELETE_FAIL_NOT_DRAFT);
        }
        // 4. 删除表单数据
        formDataMapper.deleteById(id);
    }

    @Override
    public CreditFormDataDO getFormData(Long id) {
        return validateFormDataExists(id);
    }

    @Override
    public PageResult<CreditFormDataRespVO> getFormDataPage(CreditFormDataPageReqVO pageReqVO, Long userId) {
        // 数据权限过滤：如果pageReqVO中没有指定deptId，则根据用户部门ID过滤
        // 注意：超级管理员可以在Controller层不传deptId，这里不做强制过滤
        PageResult<CreditFormDataDO> pageResult = formDataMapper.selectPage(pageReqVO);
        PageResult<CreditFormDataRespVO> result = CreditFormDataConvert.INSTANCE.convertPage(pageResult);
        // 填充部门名称和提交人姓名（在Controller层填充）
        return result;
    }

    @Override
    public Map<Long, Map<String, Object>> getDeptFormDataMap(String reportPeriod, String reportType) {
        return formDataMapper.selectDeptFormDataMap(reportPeriod, reportType);
    }

    @Override
    public List<CreditFormDataDO> getFormDataListForCalculation(String reportPeriod, String reportType) {
        return formDataMapper.selectListForCalculation(reportPeriod, reportType);
    }

    /**
     * 校验表单数据是否存在
     *
     * @param id 表单数据编号
     */
    private CreditFormDataDO validateFormDataExists(Long id) {
        CreditFormDataDO formData = formDataMapper.selectById(id);
        if (formData == null) {
            throw exception(ErrorCodeConstants.FORM_DATA_NOT_EXISTS);
        }
        return formData;
    }

    /**
     * 校验该周期是否已存在表单数据
     *
     * @param deptId       部门ID
     * @param reportPeriod 报送周期
     * @param reportType   报表类型
     */
    private void validateFormDataNotExists(Long deptId, String reportPeriod, String reportType) {
        CreditFormDataDO formData = formDataMapper.selectByDeptIdAndPeriod(deptId, reportPeriod, reportType);
        if (formData != null) {
            throw exception(ErrorCodeConstants.FORM_DATA_DUPLICATE);
        }
    }

    /**
     * 校验部门权限（部门专员只能操作本部门数据）
     *
     * @param deptId  部门ID
     * @param userId  用户编号
     * @param action  操作名称（用于错误提示）
     */
    private void validateDeptPermission(Long deptId, Long userId, String action) {
        // 获取用户信息
        AdminUserRespDTO user = adminUserApi.getUser(userId);
        if (user == null) {
            throw exception(ErrorCodeConstants.FORM_DATA_DEPT_MISMATCH);
        }
        // 校验用户部门ID与表单数据的部门ID是否匹配
        // 注意：超级管理员的部门ID可能为null，这里不做特殊处理，由Controller层判断
        if (user.getDeptId() == null || !user.getDeptId().equals(deptId)) {
            throw exception(ErrorCodeConstants.FORM_DATA_DEPT_MISMATCH);
        }
    }


}
