package com.wmt.module.credit.controller.admin.report.vo;

import cn.idev.excel.annotation.ExcelIgnoreUnannotated;
import cn.idev.excel.annotation.ExcelProperty;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * 征信汇总报表 Excel VO
 *
 * 使用项目中封装的 EasyExcel 组件（cn.idev.excel），
 * 与 system/infra 等模块保持一致。
 *
 * @author AHC源码
 */
@Data
@ExcelIgnoreUnannotated
public class CreditSummaryReportExcelVO {

    @ExcelProperty("报表编号")
    private Long id;

    @ExcelProperty("报送周期")
    private String reportPeriod;

    @ExcelProperty("报表类型")
    private String reportType;

    @ExcelProperty("状态")
    private String statusName;

    private Map<String, Object> reportData;

    @ExcelProperty("生成时间")
    private LocalDateTime generateTime;

    @ExcelProperty("创建时间")
    private LocalDateTime createTime;

}

