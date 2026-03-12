package com.wmt.module.credit.report.controller.admin.report.vo;

import cn.idev.excel.annotation.ExcelProperty;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 信息使用者机构明细 Excel VO
 *
 * @author Auto
 */
@Data
public class ReportInfoUserOrgExcelVO {

    @ExcelProperty("机构名称")
    private String orgName;

    @ExcelProperty("行业代码")
    private String industryCode;

    @ExcelProperty("是否使用服务(0-否,1-是)")
    private Integer isCurrentService;

    @ExcelProperty("排序号")
    private String sortNo;

    @ExcelProperty("创建时间")
    private LocalDateTime createTime;
}

