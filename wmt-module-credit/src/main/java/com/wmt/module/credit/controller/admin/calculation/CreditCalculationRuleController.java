package com.wmt.module.credit.controller.admin.calculation;

import com.wmt.framework.apilog.core.annotation.ApiAccessLog;
import com.wmt.framework.common.pojo.CommonResult;
import com.wmt.framework.common.pojo.PageResult;
import com.wmt.module.credit.controller.admin.calculation.vo.CreditCalculationRulePageReqVO;
import com.wmt.module.credit.controller.admin.calculation.vo.CreditCalculationRuleRespVO;
import com.wmt.module.credit.controller.admin.calculation.vo.CreditCalculationRuleSaveReqVO;
import com.wmt.module.credit.convert.CreditCalculationRuleConvert;
import com.wmt.module.credit.dal.dataobject.calculation.CreditCalculationRuleDO;
import com.wmt.module.credit.service.calculation.CreditCalculationRuleService;
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
 * 管理后台 - 征信计算规则
 *
 * @author AHC源码
 */
@Tag(name = "管理后台 - 征信计算规则")
@RestController
@RequestMapping("/credit/calculation-rule")
@Validated
public class CreditCalculationRuleController {

    @Resource
    private CreditCalculationRuleService calculationRuleService;

    @PostMapping("/create")
    @Operation(summary = "创建计算规则")
    @PreAuthorize("@ss.hasPermission('credit:calculation-rule:create')")
    @ApiAccessLog(operateType = CREATE)
    public CommonResult<Long> createCalculationRule(@Valid @RequestBody CreditCalculationRuleSaveReqVO createReqVO) {
        return success(calculationRuleService.createCalculationRule(createReqVO));
    }

    @PostMapping("/update")
    @Operation(summary = "更新计算规则")
    @PreAuthorize("@ss.hasPermission('credit:calculation-rule:update')")
    @ApiAccessLog(operateType = UPDATE)
    public CommonResult<Boolean> updateCalculationRule(@Valid @RequestBody CreditCalculationRuleSaveReqVO updateReqVO) {
        calculationRuleService.updateCalculationRule(updateReqVO);
        return success(true);
    }

    @PostMapping("/delete")
    @Operation(summary = "删除计算规则")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('credit:calculation-rule:delete')")
    @ApiAccessLog(operateType = DELETE)
    public CommonResult<Boolean> deleteCalculationRule(@RequestParam("id") Long id) {
        calculationRuleService.deleteCalculationRule(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得计算规则")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('credit:calculation-rule:query')")
    public CommonResult<CreditCalculationRuleRespVO> getCalculationRule(@RequestParam("id") Long id) {
        CreditCalculationRuleDO calculationRule = calculationRuleService.getCalculationRule(id);
        return success(CreditCalculationRuleConvert.INSTANCE.convert(calculationRule));
    }

    @GetMapping("/page")
    @Operation(summary = "获得计算规则分页")
    @PreAuthorize("@ss.hasPermission('credit:calculation-rule:query')")
    public CommonResult<PageResult<CreditCalculationRuleRespVO>> getCalculationRulePage(@Valid CreditCalculationRulePageReqVO pageReqVO) {
        PageResult<CreditCalculationRuleRespVO> pageResult = calculationRuleService.getCalculationRulePage(pageReqVO);
        return success(pageResult);
    }

}
