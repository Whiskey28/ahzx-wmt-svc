package com.wmt.module.credit.controller.admin.report;

import com.wmt.framework.apilog.core.annotation.ApiAccessLog;
import com.wmt.framework.common.pojo.CommonResult;
import com.wmt.framework.common.pojo.PageParam;
import com.wmt.framework.common.pojo.PageResult;
import com.wmt.framework.excel.core.util.ExcelUtils;
import com.wmt.module.credit.controller.admin.report.vo.CreditSummaryReportExcelVO;
import com.wmt.module.credit.controller.admin.report.vo.CreditSummaryReportGenerateReqVO;
import com.wmt.module.credit.controller.admin.report.vo.CreditSummaryReportPageReqVO;
import com.wmt.module.credit.controller.admin.report.vo.CreditSummaryReportRespVO;
import com.wmt.module.credit.controller.admin.report.vo.FieldMetadataVO;
import com.wmt.module.credit.convert.CreditSummaryReportConvert;
import com.wmt.module.credit.dal.dataobject.calculation.CreditCalculationRuleDO;
import com.wmt.module.credit.dal.dataobject.report.CreditSummaryReportDO;
import com.wmt.module.credit.enums.CreditReportTypeEnum;
import com.wmt.module.credit.service.calculation.CreditCalculationRuleService;
import com.wmt.module.credit.service.report.CreditSummaryReportService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import static com.wmt.framework.apilog.core.enums.OperateTypeEnum.*;
import static com.wmt.framework.common.pojo.CommonResult.success;

/**
 * 管理后台 - 征信汇总报表
 *
 * @author AHC源码
 */
@Tag(name = "管理后台 - 征信汇总报表")
@RestController
@RequestMapping("/credit/summary-report")
@Validated
public class CreditSummaryReportController {

    @Resource
    private CreditSummaryReportService summaryReportService;

    @Resource
    private CreditCalculationRuleService calculationRuleService;

    @PostMapping("/generate")
    @Operation(summary = "生成汇总报表")
    @PreAuthorize("@ss.hasPermission('credit:summary-report:generate')")
    @ApiAccessLog(operateType = CREATE)
    public CommonResult<Long> generateSummaryReport(@Valid @RequestBody CreditSummaryReportGenerateReqVO generateReqVO) {
        Long reportId;
        if (CreditReportTypeEnum.isMonthly(generateReqVO.getReportType())) {
            reportId = summaryReportService.generateMonthlyReport(generateReqVO.getReportPeriod());
        } else if (CreditReportTypeEnum.isQuarterly(generateReqVO.getReportType())) {
            reportId = summaryReportService.generateQuarterlyReport(generateReqVO.getReportPeriod());
        } else {
            throw new IllegalArgumentException("不支持的报表类型：" + generateReqVO.getReportType());
        }
        return success(reportId);
    }

    @GetMapping("/get")
    @Operation(summary = "获得汇总报表")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('credit:summary-report:query')")
    public CommonResult<CreditSummaryReportRespVO> getSummaryReport(@RequestParam("id") Long id) {
        CreditSummaryReportDO report = summaryReportService.getSummaryReport(id);
        CreditSummaryReportRespVO respVO = CreditSummaryReportConvert.INSTANCE.convert(report);
        
        // 查询字段元数据（包含展示配置）
        List<CreditCalculationRuleDO> rules = calculationRuleService.getRulesByReportType(report.getReportType());
        List<FieldMetadataVO> fieldMetadata = rules.stream()
                .filter(rule -> rule.getStatus() != null && rule.getStatus() == 1)  // 只返回启用的规则
                .map(rule -> {
                    FieldMetadataVO vo = new FieldMetadataVO();
                    vo.setFieldCode(rule.getTargetFieldCode());
                    vo.setFieldName(rule.getTargetFieldName());  // 从独立字段获取
                    vo.setDisplayConfig(rule.getDisplayConfig());  // 从JSON字段获取
                    return vo;
                })
                .collect(Collectors.toList());
        respVO.setFieldMetadata(fieldMetadata);
        
        return success(respVO);
    }

    @GetMapping("/page")
    @Operation(summary = "获得汇总报表分页")
    @PreAuthorize("@ss.hasPermission('credit:summary-report:query')")
    public CommonResult<PageResult<CreditSummaryReportRespVO>> getSummaryReportPage(@Valid CreditSummaryReportPageReqVO pageReqVO) {
        PageResult<CreditSummaryReportDO> pageResult = summaryReportService.getSummaryReportPage(pageReqVO);
        return success(CreditSummaryReportConvert.INSTANCE.convertPage(pageResult));
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出汇总报表 Excel")
    @PreAuthorize("@ss.hasPermission('credit:summary-report:export')")
    @ApiAccessLog(operateType = EXPORT)
    public void exportSummaryReportExcel(@Valid CreditSummaryReportPageReqVO exportReqVO,
                                        HttpServletResponse response) throws IOException {
        exportReqVO.setPageSize(PageParam.PAGE_SIZE_NONE); // 不分页
        PageResult<CreditSummaryReportDO> pageResult = summaryReportService.getSummaryReportPage(exportReqVO);
        List<CreditSummaryReportDO> list = pageResult.getList();
        // 导出 Excel
        List<CreditSummaryReportExcelVO> excelList = CreditSummaryReportConvert.INSTANCE.convertExcelList(list);
        ExcelUtils.write(response, "征信汇总报表.xls", "数据", CreditSummaryReportExcelVO.class, excelList);
    }

}
