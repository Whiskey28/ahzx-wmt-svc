package com.wmt.module.credit.report.controller.admin.report;

import com.wmt.framework.common.pojo.CommonResult;
import com.wmt.module.credit.report.controller.admin.report.vo.*;
import com.wmt.module.credit.report.service.JimuReportDataService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.wmt.framework.common.pojo.CommonResult.success;

/**
 * 积木报表专用数据接口
 *
 * 将积木报表用到的 API 集中管理，避免在报表中书写不兼容 SQL。
 *
 * @author Auto
 */
@Tag(name = "积木报表 - 数据接口")
@RestController
@RequestMapping("/credit/jmreport/data")
@Validated
public class JimuReportDataController {

    @Resource
    private JimuReportDataService jimuReportDataService;

    @GetMapping("/info-source/status")
    @Operation(summary = "信息来源情况（按行业）数据集")
    public CommonResult<JmReportInfoSourceStatusRespVO> getInfoSourceStatus(
            @Valid JmReportInfoSourceStatusReqVO reqVO) {
        return success(jimuReportDataService.getInfoSourceStatus(reqVO));
    }

    @GetMapping("/service-by-industry")
    @Operation(summary = "产品与服务提供情况（按行业）数据集")
    public CommonResult<JmReportServiceByIndustryRespVO> getServiceByIndustry(
            @Valid JmReportServiceByIndustryReqVO reqVO) {
        return success(jimuReportDataService.getServiceByIndustry(reqVO));
    }

    @GetMapping("/service-by-industry/year-count-fields")
    @Operation(summary = "产品与服务提供情况（按行业）当年提供次数（字段展开）")
    public CommonResult<JmReportServiceByIndustryYearCountFieldsRespVO> getServiceByIndustryYearCountFields(
            @Valid JmReportServiceByIndustryReqVO reqVO) {
        return success(jimuReportDataService.getServiceByIndustryYearCountFields(reqVO));
    }

    @GetMapping("/service-by-industry/form-stat")
    @Operation(summary = "产品与服务提供情况（按行业）统计（来自表单填写）")
    public CommonResult<JmReportServiceByIndustryFormStatRespVO> getServiceByIndustryFormStat(
            @Valid JmReportServiceByIndustryReqVO reqVO) {
        return success(jimuReportDataService.getServiceByIndustryFormStat(reqVO));
    }

    @GetMapping("/service-by-industry/total")
    @Operation(summary = "产品与服务提供情况（按行业）总计")
    public CommonResult<JmReportServiceByIndustryTotalRespVO> getServiceByIndustryTotal(
            @Valid JmReportServiceByIndustryReqVO reqVO) {
        return success(jimuReportDataService.getServiceByIndustryTotal(reqVO));
    }

    @GetMapping("/product-service")
    @Operation(summary = "提供的征信产品(服务)数据集")
    public CommonResult<JmReportProductServiceRespVO> getProductService(
            @Valid JmReportProductServiceReqVO reqVO) {
        return success(jimuReportDataService.getProductService(reqVO));
    }

    @GetMapping("/product-service/total")
    @Operation(summary = "提供的征信产品(服务)三项合计数据集")
    public CommonResult<JmReportProductServiceTotalRespVO> getProductServiceTotal(
            @Valid JmReportProductServiceReqVO reqVO) {
        return success(jimuReportDataService.getProductServiceTotal(reqVO));
    }

    @GetMapping("/dict/industry-code-items")
    @Operation(summary = "查询产品与服务提供情况（industry_code）字典项列表")
    public CommonResult<java.util.List<JmDictItemSimpleRespVO>> getIndustryCodeDictItems() {
        return success(jimuReportDataService.getIndustryCodeDictItems());
    }

    @GetMapping("/info-user/tree")
    @Operation(summary = "信息使用者机构下拉树数据集")
    public JmReportInfoUserTreeRespVO getInfoUserTree() {
        return jimuReportDataService.getInfoUserTree();
    }

    @GetMapping("/info-user/tree-test")
    @Operation(summary = "信息使用者机构下拉树数据集（测试：数字 ID/parentId）")
    public JmReportInfoUserTreeRespVO getInfoUserTreeTest() {
        return jimuReportDataService.getInfoUserTreeTest();
    }
}
