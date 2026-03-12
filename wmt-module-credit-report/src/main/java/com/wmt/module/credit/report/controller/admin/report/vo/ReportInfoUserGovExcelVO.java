package com.wmt.module.credit.report.controller.admin.report.vo;

import cn.idev.excel.annotation.ExcelProperty;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 信息使用者政府机构明细 Excel VO
 *
 * @author Auto
 */
@Data
public class ReportInfoUserGovExcelVO {

    @ExcelProperty("政府机构名称")
    private String govOrgName;

    @ExcelProperty("是否使用服务(0-否,1-是)")
    private Integer isCurrentService;

    @ExcelProperty("排序号")
    private String sortNo;

    @ExcelProperty("创建时间")
    private LocalDateTime createTime;
}

