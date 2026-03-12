package com.wmt.module.credit.report.controller.admin.report;

import cn.idev.excel.FastExcelFactory;
import com.wmt.framework.common.pojo.CommonResult;
import com.wmt.framework.common.pojo.PageParam;
import com.wmt.framework.common.pojo.PageResult;
import com.wmt.framework.common.util.http.HttpUtils;
import com.wmt.framework.excel.core.util.ExcelUtils;
import com.wmt.module.credit.report.controller.admin.report.vo.*;
import com.wmt.module.credit.report.framework.excel.MinColumnWidthMatchStyleStrategy;
import com.wmt.module.credit.report.service.ReportInfoUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.wmt.framework.common.pojo.CommonResult.success;
import static com.wmt.framework.common.util.collection.CollectionUtils.convertList;

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

    @GetMapping("/org/export-excel")
    @Operation(summary = "导出信息使用者机构明细（按行业名称分列，含政府机构）")
    public void exportOrgExcel(@Valid ReportInfoUserOrgPageReqVO exportReqVO,
                               HttpServletResponse response) throws IOException {
        // 1）查询所有机构明细（不过滤分页）
        exportReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<ReportInfoUserOrgRespVO> orgList = reportInfoUserService.getOrgPage(exportReqVO).getList();

        // 2）行业代码 -> 行业名称（按字典顺序），并把 orgList 按行业分桶（每个行业一列）
        List<ReportInfoUserStatByIndustryItemRespVO> industryItems = Optional.ofNullable(reportInfoUserService.getStatByIndustry())
                .map(ReportInfoUserStatByIndustryRespVO::getItems)
                .orElse(List.of());

        // LinkedHashMap 保持列顺序：字典顺序 -> Excel 列顺序
        Map<String, List<String>> industryNameToOrgNames = new LinkedHashMap<>();
        for (ReportInfoUserStatByIndustryItemRespVO item : industryItems) {
            industryNameToOrgNames.put(item.getIndustryName(), new ArrayList<>());
        }

        // 兜底：如果某些机构行业代码不在字典统计里，也要导出成单独列（按出现顺序追加在末尾）
        Map<String, String> industryCodeToName = industryItems.stream()
                .collect(Collectors.toMap(ReportInfoUserStatByIndustryItemRespVO::getIndustryCode,
                        ReportInfoUserStatByIndustryItemRespVO::getIndustryName, (v1, v2) -> v1));

        for (ReportInfoUserOrgRespVO org : orgList) {
            String industryName = industryCodeToName.getOrDefault(org.getIndustryCode(), org.getIndustryCode());
            industryNameToOrgNames.computeIfAbsent(industryName, k -> new ArrayList<>()).add(org.getOrgName());
        }

        // 3）追加政府机构的所有机构，单独一列
        ReportInfoUserGovPageReqVO govExportReqVO = new ReportInfoUserGovPageReqVO();
        govExportReqVO.setPageNo(1);
        govExportReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<String> governmentOrgNames = reportInfoUserService.getGovPage(govExportReqVO).getList()
                .stream()
                .map(ReportInfoUserGovRespVO::getGovOrgName)
                .collect(Collectors.toList());

        // 4）动态表头：全部行业列 + 政府机构列
        List<List<String>> heads = new ArrayList<>();
        List<String> industryNamesInOrder = new ArrayList<>(industryNameToOrgNames.keySet());
        for (String industryName : industryNamesInOrder) {
            heads.add(List.of("序号"));
            heads.add(List.of(industryName));
        }
        heads.add(List.of("序号"));
        heads.add(List.of("政府机构"));

        // 5）动态行数据：每行各列分别填一条机构名称（不足补空）
        int maxSize = governmentOrgNames.size();
        for (List<String> names : industryNameToOrgNames.values()) {
            maxSize = Math.max(maxSize, names.size());
        }

        List<List<Object>> rows = new ArrayList<>();
        for (int rowIndex = 0; rowIndex < maxSize; rowIndex++) {
            List<Object> row = new ArrayList<>();
            for (String industryName : industryNamesInOrder) {
                List<String> names = industryNameToOrgNames.get(industryName);
                row.add(rowIndex < names.size() ? (rowIndex + 1) : null);
                row.add(rowIndex < names.size() ? names.get(rowIndex) : null);
            }
            row.add(rowIndex < governmentOrgNames.size() ? (rowIndex + 1) : null);
            row.add(rowIndex < governmentOrgNames.size() ? governmentOrgNames.get(rowIndex) : null);
            rows.add(row);
        }

        // 6）输出 Excel（动态表头），直接用 FastExcelFactory，避免依赖新版 ExcelUtils jar
        FastExcelFactory.write(response.getOutputStream())
                .autoCloseStream(false)
                .head(heads)
                // 序号列/名称列分别设置更大的最小列宽（同时保留自适应逻辑）
                .registerWriteHandler(new MinColumnWidthMatchStyleStrategy(8, 30))
                .sheet("数据")
                .doWrite(rows);
        response.addHeader("Content-Disposition", "attachment;filename=" + HttpUtils.encodeUtf8("信息使用者机构及政府机构按行业分列.xls"));
        response.setContentType("application/vnd.ms-excel;charset=UTF-8");
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

    @GetMapping("/gov/export-excel")
    @Operation(summary = "导出信息使用者政府机构明细")
    public void exportGovExcel(@Valid ReportInfoUserGovPageReqVO exportReqVO,
                               HttpServletResponse response) throws IOException {
        exportReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<ReportInfoUserGovRespVO> list = reportInfoUserService.getGovPage(exportReqVO).getList();
        ExcelUtils.write(response, "信息使用者政府机构明细.xls", "数据", ReportInfoUserGovExcelVO.class,
                convertList(list, item -> {
                    ReportInfoUserGovExcelVO vo = new ReportInfoUserGovExcelVO();
                    vo.setGovOrgName(item.getGovOrgName());
                    vo.setIsCurrentService(item.getIsCurrentService());
                    vo.setSortNo(item.getSortNo());
                    vo.setCreateTime(item.getCreateTime());
                    return vo;
                }));
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

