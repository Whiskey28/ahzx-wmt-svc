package com.wmt.module.credit.controller.admin.form.vo;

import com.wmt.framework.common.pojo.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * 征信表单数据分页查询 Request VO
 *
 * @author AHC源码
 */
@Schema(description = "管理后台 - 征信表单数据分页查询 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class CreditFormDataPageReqVO extends PageParam {

    @Schema(description = "部门编号", example = "1")
    private Long deptId;

    @Schema(description = "报送周期", example = "2025-01")
    private String reportPeriod;

    @Schema(description = "报表类型", example = "MONTHLY")
    private String reportType;

    @Schema(description = "状态", example = "1")
    private Integer status;

}
