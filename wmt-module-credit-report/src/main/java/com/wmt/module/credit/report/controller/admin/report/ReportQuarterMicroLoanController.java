package com.wmt.module.credit.report.controller.admin.report;

import com.wmt.framework.common.pojo.CommonResult;
import com.wmt.module.credit.report.controller.admin.report.vo.*;
import com.wmt.module.credit.report.service.ReportQuarterMicroLoanService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.wmt.framework.common.pojo.CommonResult.success;

/**
 * 管理后台 - 季报（小微贷款）
 */
@Tag(name = "管理后台 - 季报（小微贷款）")
@RestController
@RequestMapping("/credit/report-quarter/micro-loan")
@Validated
public class ReportQuarterMicroLoanController {

    @Resource
    private ReportQuarterMicroLoanService reportQuarterMicroLoanService;

    @GetMapping("/get-or-init")
    @Operation(summary = "获取/初始化当季填报数据（小微贷款）")
    public CommonResult<ReportQuarterMicroLoanRespVO> getOrInit(@Valid ReportQuarterMicroLoanGetReqVO reqVO) {
        return success(reportQuarterMicroLoanService.getOrInit(reqVO));
    }

    @PostMapping("/save")
    @Operation(summary = "保存当季发生额（小微贷款）")
    public CommonResult<Boolean> save(@Valid @RequestBody ReportQuarterMicroLoanSaveReqVO reqVO) {
        return success(reportQuarterMicroLoanService.save(reqVO));
    }

    @GetMapping("/stat")
    @Operation(summary = "展示统计（当季/累计/同比增量/同比增速，小微贷款）")
    public CommonResult<ReportQuarterMicroLoanStatRespVO> stat(@Valid ReportQuarterMicroLoanStatReqVO reqVO) {
        return success(reportQuarterMicroLoanService.stat(reqVO));
    }

    @GetMapping("/stat-collect-micro-sme")
    @Operation(summary = "数据库收录小微企业户数统计（当季/累计/同比增量/同比增速）")
    public CommonResult<ReportQuarterMicroLoanCollectMicroSmeStatRespVO> statCollectMicroSme(
            @Valid ReportQuarterMicroLoanCollectMicroSmeStatReqVO reqVO) {
        return success(reportQuarterMicroLoanService.statCollectMicroSme(reqVO));
    }

    @GetMapping("/stat-service-count-total")
    @Operation(summary = "为放贷机构提供小微企业征信服务次数统计（当季/累计/同比增量/同比增速）")
    public CommonResult<ReportQuarterMicroLoanServiceCountStatRespVO> statServiceCountTotal(
            @Valid ReportQuarterMicroLoanServiceCountStatReqVO reqVO) {
        return success(reportQuarterMicroLoanService.statServiceCountTotal(reqVO));
    }
}

