package com.wmt.module.credit.controller.admin.form;

import com.wmt.framework.apilog.core.annotation.ApiAccessLog;
import com.wmt.framework.common.pojo.CommonResult;
import com.wmt.framework.common.pojo.PageResult;
import com.wmt.framework.common.util.collection.CollectionUtils;
import com.wmt.module.credit.controller.admin.form.vo.CreditFormDataPageReqVO;
import com.wmt.module.credit.controller.admin.form.vo.CreditFormDataRespVO;
import com.wmt.module.credit.controller.admin.form.vo.CreditFormDataSaveReqVO;
import com.wmt.module.credit.controller.admin.form.vo.CreditFormDataSubmitReqVO;
import com.wmt.module.credit.convert.CreditFormDataConvert;
import com.wmt.module.credit.dal.dataobject.form.CreditFormDataDO;
import com.wmt.module.credit.service.form.CreditFormDataService;
import com.wmt.module.system.api.dept.DeptApi;
import com.wmt.module.system.api.dept.dto.DeptRespDTO;
import com.wmt.module.system.api.user.AdminUserApi;
import com.wmt.module.system.api.user.dto.AdminUserRespDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

import static com.wmt.framework.apilog.core.enums.OperateTypeEnum.*;
import static com.wmt.framework.common.pojo.CommonResult.success;
import static com.wmt.framework.common.util.collection.CollectionUtils.convertSet;
import static com.wmt.framework.security.core.util.SecurityFrameworkUtils.getLoginUserId;

/**
 * 管理后台 - 征信表单数据
 *
 * @author AHC源码
 */
@Tag(name = "管理后台 - 征信表单数据")
@RestController
@RequestMapping("/credit/form-data")
@Validated
public class CreditFormDataController {

    @Resource
    private CreditFormDataService formDataService;

    @Resource
    private DeptApi deptApi;

    @Resource
    private AdminUserApi adminUserApi;

    @PostMapping("/create")
    @Operation(summary = "创建表单数据")
    @PreAuthorize("@ss.hasPermission('credit:form-data:create')")
    @ApiAccessLog(operateType = CREATE)
    public CommonResult<Long> createFormData(@Valid @RequestBody CreditFormDataSaveReqVO createReqVO) {
        return success(formDataService.createFormData(createReqVO, getLoginUserId()));
    }

    @PostMapping("/update")
    @Operation(summary = "更新表单数据")
    @PreAuthorize("@ss.hasPermission('credit:form-data:update')")
    @ApiAccessLog(operateType = UPDATE)
    public CommonResult<Boolean> updateFormData(@Valid @RequestBody CreditFormDataSaveReqVO updateReqVO) {
        formDataService.updateFormData(updateReqVO, getLoginUserId());
        return success(true);
    }

    @PostMapping("/submit")
    @Operation(summary = "提交表单数据")
    @PreAuthorize("@ss.hasPermission('credit:form-data:submit')")
    @ApiAccessLog(operateType = UPDATE)
    public CommonResult<Boolean> submitFormData(@Valid @RequestBody CreditFormDataSubmitReqVO submitReqVO) {
        formDataService.submitFormData(submitReqVO, getLoginUserId());
        return success(true);
    }

    @PostMapping("/delete")
    @Operation(summary = "删除表单数据")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('credit:form-data:delete')")
    @ApiAccessLog(operateType = DELETE)
    public CommonResult<Boolean> deleteFormData(@RequestParam("id") Long id) {
        formDataService.deleteFormData(id, getLoginUserId());
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得表单数据")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('credit:form-data:query')")
    public CommonResult<CreditFormDataRespVO> getFormData(@RequestParam("id") Long id) {
        CreditFormDataDO formData = formDataService.getFormData(id);
        CreditFormDataRespVO respVO = CreditFormDataConvert.INSTANCE.convert(formData);
        // 填充部门名称和提交人姓名
        fillDeptAndUserName(respVO);
        return success(respVO);
    }

    @GetMapping("/page")
    @Operation(summary = "获得表单数据分页")
    @PreAuthorize("@ss.hasPermission('credit:form-data:query')")
    public CommonResult<PageResult<CreditFormDataRespVO>> getFormDataPage(@Valid CreditFormDataPageReqVO pageReqVO) {
        // 数据权限过滤：非超级管理员只能查看本部门数据
        // 如果pageReqVO中没有指定deptId，则根据当前用户部门ID过滤
        if (pageReqVO.getDeptId() == null) {
            AdminUserRespDTO user = adminUserApi.getUser(getLoginUserId());
            if (user != null && user.getDeptId() != null) {
                pageReqVO.setDeptId(user.getDeptId());
            }
        }
        PageResult<CreditFormDataRespVO> pageResult = formDataService.getFormDataPage(pageReqVO, getLoginUserId());
        // 填充部门名称和提交人姓名
        fillDeptAndUserNames(pageResult.getList());
        return success(pageResult);
    }

    /**
     * 填充部门名称和提交人姓名（单个对象）
     */
    private void fillDeptAndUserName(CreditFormDataRespVO respVO) {
        if (respVO == null) {
            return;
        }
        // 填充部门名称
        if (respVO.getDeptId() != null) {
            DeptRespDTO dept = deptApi.getDept(respVO.getDeptId());
            if (dept != null) {
                respVO.setDeptName(dept.getName());
            }
        }
        // 填充提交人姓名
        if (respVO.getSubmitUserId() != null) {
            AdminUserRespDTO user = adminUserApi.getUser(respVO.getSubmitUserId());
            if (user != null) {
                respVO.setSubmitUserName(user.getNickname());
            }
        }
    }

    /**
     * 填充部门名称和提交人姓名（列表）
     */
    private void fillDeptAndUserNames(List<CreditFormDataRespVO> list) {
        if (list == null || list.isEmpty()) {
            return;
        }
        // 获取所有部门ID和用户ID
        List<Long> deptIds = list.stream()
                .map(CreditFormDataRespVO::getDeptId)
                .filter(java.util.Objects::nonNull)
                .distinct()
                .toList();
        List<Long> userIds = list.stream()
                .map(CreditFormDataRespVO::getSubmitUserId)
                .filter(java.util.Objects::nonNull)
                .distinct()
                .toList();
        // 批量查询部门和用户信息
        Map<Long, DeptRespDTO> deptMap = deptApi.getDeptMap(deptIds);
        Map<Long, AdminUserRespDTO> userMap = adminUserApi.getUserMap(userIds);
        // 填充部门名称和提交人姓名
        list.forEach(respVO -> {
            if (respVO.getDeptId() != null) {
                DeptRespDTO dept = deptMap.get(respVO.getDeptId());
                if (dept != null) {
                    respVO.setDeptName(dept.getName());
                }
            }
            if (respVO.getSubmitUserId() != null) {
                AdminUserRespDTO user = userMap.get(respVO.getSubmitUserId());
                if (user != null) {
                    respVO.setSubmitUserName(user.getNickname());
                }
            }
        });
    }

}
