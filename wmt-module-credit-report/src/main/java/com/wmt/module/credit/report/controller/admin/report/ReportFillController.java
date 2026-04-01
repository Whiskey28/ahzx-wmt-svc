package com.wmt.module.credit.report.controller.admin.report;

import com.wmt.framework.common.pojo.CommonResult;
import com.wmt.framework.common.pojo.PageResult;
import com.wmt.module.credit.report.controller.admin.report.vo.*;
import com.wmt.module.credit.report.service.ReportFillService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.util.StringUtils;

import java.util.List;

import static com.wmt.framework.common.pojo.CommonResult.success;

/**
 * 管理后台 - 报表填报
 *
 * @author AHC源码
 */
@Tag(name = "管理后台 - 报表填报")
@RestController
@RequestMapping("/credit/report-fill")
@Validated
public class ReportFillController {

    @Resource
    private ReportFillService reportFillService;

    @GetMapping("/category/list")
    @Operation(summary = "查询积木报表分类列表")
    // @PreAuthorize("@ss.hasPermission('credit:report-fill:query')")
    public CommonResult<List<JimuReportCategoryRespVO>> getCategoryList() {
        return success(reportFillService.getCategoryList());
    }

    @GetMapping("/template/list")
    @Operation(summary = "根据分类id查询报表模板列表")
//    @PreAuthorize("@ss.hasPermission('credit:report-fill:query')")
    public CommonResult<List<JimuReportTemplateRespVO>> getTemplateList(@Valid JimuReportTemplateListReqVO reqVO) {
        List<JimuReportTemplateRespVO> list = reportFillService.getTemplateListByCategoryId(reqVO.getCategoryId(), reqVO.getSubmitForm());
        return success(list);
    }

    @GetMapping("/record/page")
    @Operation(summary = "分页查询填报记录")
//    @PreAuthorize("@ss.hasPermission('credit:report-fill:query')")
    public CommonResult<PageResult<ReportFillRecordRespVO>> getFillRecordPage(@Valid ReportFillRecordPageReqVO pageReqVO) {
        if (pageReqVO == null
                || (!StringUtils.hasText(pageReqVO.getCategoryId())
                && !StringUtils.hasText(pageReqVO.getReportId())
                && !StringUtils.hasText(pageReqVO.getReportName())
                && !StringUtils.hasText(pageReqVO.getPeriodId())
                && !StringUtils.hasText(pageReqVO.getRoleId()))) {
            return success(PageResult.empty());
        }
        PageResult<ReportFillRecordRespVO> pageResult = reportFillService.getFillRecordPage(pageReqVO);
        return success(pageResult);
    }

    @GetMapping("/record/{id}/edit-url")
    @Operation(summary = "根据记录id获取编辑URL信息")
    @Parameter(name = "id", description = "记录id", required = true, example = "1")
//    @PreAuthorize("@ss.hasPermission('credit:report-fill:query')")
    public CommonResult<ReportFillRecordEditUrlRespVO> getEditUrlByRecordId(@PathVariable("id") String id) {
        ReportFillRecordEditUrlRespVO respVO = reportFillService.getEditUrlByRecordId(id);
        return success(respVO);
    }

    @PostMapping("/record/delete/{id}")
    @Operation(summary = "物理删除填报记录")
    @Parameter(name = "id", description = "记录id", required = true, example = "1")
//    @PreAuthorize("@ss.hasPermission('credit:report-fill:delete')")
    public CommonResult<Boolean> deleteFillRecord(@PathVariable("id") @NotNull(message = "记录id不能为空") String id) {
        Boolean result = reportFillService.deleteFillRecord(id);
        return success(result);
    }

}
