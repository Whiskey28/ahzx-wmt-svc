package com.wmt.module.credit.report.controller.admin.report;

import com.wmt.framework.common.pojo.CommonResult;
import com.wmt.framework.common.pojo.PageResult;
import com.wmt.module.credit.report.controller.admin.report.vo.*;
import com.wmt.module.credit.report.service.ReportInfoUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.wmt.framework.common.pojo.CommonResult.success;

/**
 * 管理后台 - 信息使用者机构明细 & 按行业统计
 *
 * @author Auto
 */
@Tag(name = "管理后台 - 信息使用者机构")
@RestController
@RequestMapping("/credit/report-info-user")
@Validated
public class ReportInfoUserController {

    @Resource
    private ReportInfoUserService reportInfoUserService;

    // ========== 机构明细 ==========

    @GetMapping("/org/page")
    @Operation(summary = "分页查询信息使用者机构明细")
    public CommonResult<PageResult<ReportInfoUserOrgRespVO>> getOrgPage(@Valid ReportInfoUserOrgPageReqVO pageReqVO) {
        return success(reportInfoUserService.getOrgPage(pageReqVO));
    }

    @PostMapping("/org/create")
    @Operation(summary = "创建信息使用者机构明细")
    public CommonResult<String> createOrg(@Valid @RequestBody ReportInfoUserOrgSaveReqVO reqVO) {
        return success(reportInfoUserService.createOrg(reqVO));
    }

    @PostMapping("/org/update")
    @Operation(summary = "更新信息使用者机构明细")
    public CommonResult<Boolean> updateOrg(@Valid @RequestBody ReportInfoUserOrgUpdateReqVO reqVO) {
        return success(reportInfoUserService.updateOrg(reqVO));
    }

    @PostMapping("/org/delete")
    @Operation(summary = "删除信息使用者机构明细")
    public CommonResult<Boolean> deleteOrg(@NotBlank(message = "主键id不能为空") @RequestParam("id") String id) {
        return success(reportInfoUserService.deleteOrg(id));
    }

    @PostMapping("/org/delete-batch")
    @Operation(summary = "批量删除信息使用者机构明细")
    public CommonResult<Boolean> deleteOrgBatch(@NotEmpty(message = "主键id列表不能为空") @RequestBody List<String> ids) {
        return success(reportInfoUserService.deleteOrgBatch(ids));
    }

    // ========== 政府机构明细 ==========

    @GetMapping("/gov/page")
    @Operation(summary = "分页查询信息使用者政府机构明细")
    public CommonResult<PageResult<ReportInfoUserGovRespVO>> getGovPage(@Valid ReportInfoUserGovPageReqVO pageReqVO) {
        return success(reportInfoUserService.getGovPage(pageReqVO));
    }

    @PostMapping("/gov/create")
    @Operation(summary = "创建信息使用者政府机构明细")
    public CommonResult<String> createGov(@Valid @RequestBody ReportInfoUserGovSaveReqVO reqVO) {
        return success(reportInfoUserService.createGov(reqVO));
    }

    @PostMapping("/gov/update")
    @Operation(summary = "更新信息使用者政府机构明细")
    public CommonResult<Boolean> updateGov(@Valid @RequestBody ReportInfoUserGovUpdateReqVO reqVO) {
        return success(reportInfoUserService.updateGov(reqVO));
    }

    @PostMapping("/gov/delete")
    @Operation(summary = "删除信息使用者政府机构明细")
    public CommonResult<Boolean> deleteGov(@NotBlank(message = "主键id不能为空") @RequestParam("id") String id) {
        return success(reportInfoUserService.deleteGov(id));
    }

    @PostMapping("/gov/delete-batch")
    @Operation(summary = "批量删除信息使用者政府机构明细")
    public CommonResult<Boolean> deleteGovBatch(@NotEmpty(message = "主键id列表不能为空") @RequestBody List<String> ids) {
        return success(reportInfoUserService.deleteGovBatch(ids));
    }

    // ========== 按行业统计 ==========

    @GetMapping("/stat/by-industry")
    @Operation(summary = "按行业统计信息使用者机构数量（含总计，基于全表数据）")
    public CommonResult<ReportInfoUserStatByIndustryRespVO> getStatByIndustry() {
        return success(reportInfoUserService.getStatByIndustry());
    }
}

