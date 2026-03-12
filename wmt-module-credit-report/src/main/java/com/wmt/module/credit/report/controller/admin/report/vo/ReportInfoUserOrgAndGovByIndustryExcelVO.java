package com.wmt.module.credit.report.controller.admin.report.vo;

import cn.idev.excel.annotation.ExcelProperty;
import lombok.Data;

/**
 * 信息使用者机构 + 政府机构 按行业分列导出 Excel VO
 *
 * 如图所示：每一列代表一个行业或政府机构类型，每一行是该列下的一家机构名称。
 *
 * @author Auto
 */
@Data
public class ReportInfoUserOrgAndGovByIndustryExcelVO {

    @ExcelProperty("银行")
    private String bankOrgName;

    @ExcelProperty("其他")
    private String otherOrgName;

    @ExcelProperty("融资租赁及担保类公司")
    private String financingLeasingGuaranteeOrgName;

    @ExcelProperty("其他征信机构")
    private String otherCreditAgencyOrgName;

    @ExcelProperty("政府机构")
    private String governmentOrgName;
}

