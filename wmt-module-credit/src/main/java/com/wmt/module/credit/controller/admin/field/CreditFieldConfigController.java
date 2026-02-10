package com.wmt.module.credit.controller.admin.field;

import com.wmt.framework.apilog.core.annotation.ApiAccessLog;
import com.wmt.framework.common.pojo.CommonResult;
import com.wmt.framework.common.pojo.PageResult;
import com.wmt.module.credit.controller.admin.field.vo.CreditFieldConfigPageReqVO;
import com.wmt.module.credit.controller.admin.field.vo.CreditFieldConfigRespVO;
import com.wmt.module.credit.controller.admin.field.vo.CreditFieldConfigSaveReqVO;
import com.wmt.module.credit.convert.CreditFieldConfigConvert;
import com.wmt.module.credit.dal.dataobject.field.CreditFieldConfigDO;
import com.wmt.module.credit.service.field.CreditFieldConfigService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.wmt.framework.apilog.core.enums.OperateTypeEnum.*;
import static com.wmt.framework.common.pojo.CommonResult.success;

/**
 * 管理后台 - 征信字段配置
 *
 * @author AHC源码
 */
@Tag(name = "管理后台 - 征信字段配置")
@RestController
@RequestMapping("/credit/field-config")
@Validated
public class CreditFieldConfigController {

    @Resource
    private CreditFieldConfigService fieldConfigService;

    @PostMapping("/create")
    @Operation(summary = "创建字段配置")
    @PreAuthorize("@ss.hasPermission('credit:field-config:create')")
    @ApiAccessLog(operateType = CREATE)
    public CommonResult<Long> createFieldConfig(@Valid @RequestBody CreditFieldConfigSaveReqVO createReqVO) {
        return success(fieldConfigService.createFieldConfig(createReqVO));
    }

    @PostMapping("/update")
    @Operation(summary = "更新字段配置")
    @PreAuthorize("@ss.hasPermission('credit:field-config:update')")
    @ApiAccessLog(operateType = UPDATE)
    public CommonResult<Boolean> updateFieldConfig(@Valid @RequestBody CreditFieldConfigSaveReqVO updateReqVO) {
        fieldConfigService.updateFieldConfig(updateReqVO);
        return success(true);
    }

    @PostMapping("/delete")
    @Operation(summary = "删除字段配置")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('credit:field-config:delete')")
    @ApiAccessLog(operateType = DELETE)
    public CommonResult<Boolean> deleteFieldConfig(@RequestParam("id") Long id) {
        fieldConfigService.deleteFieldConfig(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得字段配置")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('credit:field-config:query')")
    public CommonResult<CreditFieldConfigRespVO> getFieldConfig(@RequestParam("id") Long id) {
        CreditFieldConfigDO fieldConfig = fieldConfigService.getFieldConfig(id);
        return success(CreditFieldConfigConvert.INSTANCE.convert(fieldConfig));
    }

    @GetMapping("/page")
    @Operation(summary = "获得字段配置分页")
    @PreAuthorize("@ss.hasPermission('credit:field-config:query')")
    public CommonResult<PageResult<CreditFieldConfigRespVO>> getFieldConfigPage(@Valid CreditFieldConfigPageReqVO pageReqVO) {
        PageResult<CreditFieldConfigRespVO> pageResult = fieldConfigService.getFieldConfigPage(pageReqVO);
        return success(pageResult);
    }

    @GetMapping("/list")
    @Operation(summary = "获得字段配置列表")
    @Parameter(name = "deptId", description = "部门ID（0表示通用字段）", example = "1")
    @Parameter(name = "reportType", description = "报表类型", example = "MONTHLY")
    @PreAuthorize("@ss.hasPermission('credit:field-config:query')")
    public CommonResult<List<CreditFieldConfigDO>> getFieldConfigList(
            @RequestParam(value = "deptId", required = false) Long deptId,
            @RequestParam(value = "reportType", required = false) String reportType) {
        List<CreditFieldConfigDO> list = fieldConfigService.getFieldConfigList(deptId, reportType);
        return success(list);
    }

}
