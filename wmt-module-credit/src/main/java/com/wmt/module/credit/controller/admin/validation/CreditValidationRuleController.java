package com.wmt.module.credit.controller.admin.validation;

import com.wmt.framework.apilog.core.annotation.ApiAccessLog;
import com.wmt.framework.common.pojo.CommonResult;
import com.wmt.framework.common.pojo.PageResult;
import com.wmt.module.credit.controller.admin.validation.vo.CreditValidationRulePageReqVO;
import com.wmt.module.credit.controller.admin.validation.vo.CreditValidationRuleRespVO;
import com.wmt.module.credit.controller.admin.validation.vo.CreditValidationRuleSaveReqVO;
import com.wmt.module.credit.convert.CreditValidationRuleConvert;
import com.wmt.module.credit.dal.dataobject.validation.CreditValidationRuleDO;
import com.wmt.module.credit.service.validation.CreditValidationRuleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import static com.wmt.framework.apilog.core.enums.OperateTypeEnum.*;
import static com.wmt.framework.common.pojo.CommonResult.success;

/**
 * 管理后台 - 征信校验规则
 *
 * @author AHC源码
 */
@Tag(name = "管理后台 - 征信校验规则")
@RestController
@RequestMapping("/credit/validation-rule")
@Validated
public class CreditValidationRuleController {

    @Resource
    private CreditValidationRuleService validationRuleService;

    @PostMapping("/create")
    @Operation(summary = "创建校验规则")
    @PreAuthorize("@ss.hasPermission('credit:validation-rule:create')")
    @ApiAccessLog(operateType = CREATE)
    public CommonResult<Long> createValidationRule(@Valid @RequestBody CreditValidationRuleSaveReqVO createReqVO) {
        return success(validationRuleService.createValidationRule(createReqVO));
    }

    @PostMapping("/update")
    @Operation(summary = "更新校验规则")
    @PreAuthorize("@ss.hasPermission('credit:validation-rule:update')")
    @ApiAccessLog(operateType = UPDATE)
    public CommonResult<Boolean> updateValidationRule(@Valid @RequestBody CreditValidationRuleSaveReqVO updateReqVO) {
        validationRuleService.updateValidationRule(updateReqVO);
        return success(true);
    }

    @PostMapping("/delete")
    @Operation(summary = "删除校验规则")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('credit:validation-rule:delete')")
    @ApiAccessLog(operateType = DELETE)
    public CommonResult<Boolean> deleteValidationRule(@RequestParam("id") Long id) {
        validationRuleService.deleteValidationRule(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得校验规则")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('credit:validation-rule:query')")
    public CommonResult<CreditValidationRuleRespVO> getValidationRule(@RequestParam("id") Long id) {
        CreditValidationRuleDO rule = validationRuleService.getValidationRule(id);
        return success(CreditValidationRuleConvert.INSTANCE.convert(rule));
    }

    @GetMapping("/page")
    @Operation(summary = "获得校验规则分页")
    @PreAuthorize("@ss.hasPermission('credit:validation-rule:query')")
    public CommonResult<PageResult<CreditValidationRuleRespVO>> getValidationRulePage(@Valid CreditValidationRulePageReqVO pageReqVO) {
        PageResult<CreditValidationRuleDO> pageResult = validationRuleService.getValidationRulePage(pageReqVO);
        return success(CreditValidationRuleConvert.INSTANCE.convertPage(pageResult));
    }

}
