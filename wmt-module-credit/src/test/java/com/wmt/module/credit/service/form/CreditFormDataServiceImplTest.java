package com.wmt.module.credit.service.form;

import com.wmt.framework.common.pojo.PageResult;
import com.wmt.framework.test.core.ut.BaseDbUnitTest;
import com.wmt.module.credit.controller.admin.form.vo.CreditFormDataPageReqVO;
import com.wmt.module.credit.controller.admin.form.vo.CreditFormDataRespVO;
import com.wmt.module.credit.controller.admin.form.vo.CreditFormDataSaveReqVO;
import com.wmt.module.credit.controller.admin.form.vo.CreditFormDataSubmitReqVO;
import com.wmt.module.credit.dal.dataobject.form.CreditFormDataDO;
import com.wmt.module.credit.dal.mysql.form.CreditFormDataMapper;
import com.wmt.module.credit.enums.CreditFormStatusEnum;
import com.wmt.module.credit.enums.ErrorCodeConstants;
import com.wmt.module.credit.service.validation.CreditValidationService;
import com.wmt.module.system.api.user.AdminUserApi;
import com.wmt.module.system.api.user.dto.AdminUserRespDTO;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;

import java.util.Map;

import static com.wmt.framework.test.core.util.AssertUtils.assertServiceException;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

/**
 * {@link CreditFormDataServiceImpl} 的最小可运行单元测试示例
 *
 * 覆盖典型流程：
 * - 创建表单数据
 * - 提交表单数据（含校验服务调用）
 * - 分页查询
 *
 * 外部依赖（AdminUserApi、CreditValidationService）通过 @MockBean 注入。
 *
 * @author AHC源码
 */
@Import({CreditFormDataServiceImpl.class, CreditFormDataMapper.class})
public class CreditFormDataServiceImplTest extends BaseDbUnitTest {

    @Resource
    private CreditFormDataServiceImpl formDataService;

    @Resource
    private CreditFormDataMapper formDataMapper;

    @MockBean
    private AdminUserApi adminUserApi;

    @MockBean
    private CreditValidationService validationService;

    @Test
    public void testCreateAndSubmitFormData_success() {
        Long userId = 100L;

        // mock 用户信息：部门ID=1
        AdminUserRespDTO user = new AdminUserRespDTO();
        user.setId(userId);
        user.setDeptId(1L);
        when(adminUserApi.getUser(userId)).thenReturn(user);

        // 1. 创建草稿
        CreditFormDataSaveReqVO createReqVO = new CreditFormDataSaveReqVO();
        createReqVO.setDeptId(1L);
        createReqVO.setReportPeriod("2025-01");
        createReqVO.setReportType("MONTHLY");
        createReqVO.setFormData(Map.of("field1", 123));

        Long formId = formDataService.createFormData(createReqVO, userId);
        assertNotNull(formId);

        CreditFormDataDO created = formDataMapper.selectById(formId);
        assertNotNull(created);
        assertEquals(CreditFormStatusEnum.DRAFT.getStatus(), created.getStatus());

        // 2. 提交表单（mock 校验通过）
        Mockito.doNothing().when(validationService).validateFormData(any(CreditFormDataDO.class));

        CreditFormDataSubmitReqVO submitReqVO = new CreditFormDataSubmitReqVO();
        submitReqVO.setId(formId);

        formDataService.submitFormData(submitReqVO, userId);

        CreditFormDataDO submitted = formDataMapper.selectById(formId);
        assertEquals(CreditFormStatusEnum.SUBMITTED.getStatus(), submitted.getStatus());
        assertEquals(userId, submitted.getSubmitUserId());
        assertNotNull(submitted.getSubmitTime());
    }

    @Test
    public void testSubmitFormData_notDraft_error() {
        Long userId = 101L;

        // mock 用户信息：部门ID=2
        AdminUserRespDTO user = new AdminUserRespDTO();
        user.setId(userId);
        user.setDeptId(2L);
        when(adminUserApi.getUser(userId)).thenReturn(user);

        // 插入一条已提交状态的数据
        CreditFormDataDO formData = new CreditFormDataDO();
        formData.setDeptId(2L);
        formData.setReportPeriod("2025-02");
        formData.setReportType("MONTHLY");
        formData.setStatus(CreditFormStatusEnum.SUBMITTED.getStatus());
        formDataMapper.insert(formData);

        CreditFormDataSubmitReqVO submitReqVO = new CreditFormDataSubmitReqVO();
        submitReqVO.setId(formData.getId());

        // 断言：提交非草稿状态会抛业务异常
        assertServiceException(() -> formDataService.submitFormData(submitReqVO, userId),
                ErrorCodeConstants.FORM_DATA_SUBMIT_FAIL_NOT_DRAFT);
    }

    @Test
    public void testGetFormDataPage_success() {
        // 插入一条表单数据
        CreditFormDataDO formData = new CreditFormDataDO();
        formData.setDeptId(3L);
        formData.setReportPeriod("2025-03");
        formData.setReportType("MONTHLY");
        formData.setStatus(CreditFormStatusEnum.DRAFT.getStatus());
        formDataMapper.insert(formData);

        CreditFormDataPageReqVO reqVO = new CreditFormDataPageReqVO();
        reqVO.setDeptId(3L);

        PageResult<CreditFormDataRespVO> pageResult = formDataService.getFormDataPage(reqVO, null);
        assertEquals(1, pageResult.getTotal());
        assertEquals(1, pageResult.getList().size());
        assertEquals(formData.getDeptId(), pageResult.getList().get(0).getDeptId());
    }

}

