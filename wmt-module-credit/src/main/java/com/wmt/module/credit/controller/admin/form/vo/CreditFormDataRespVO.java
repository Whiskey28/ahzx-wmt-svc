package com.wmt.module.credit.controller.admin.form.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * 征信表单数据 Response VO
 *
 * @author AHC源码
 */
@Schema(description = "管理后台 - 征信表单数据 Response VO")
@Data
public class CreditFormDataRespVO {

    @Schema(description = "表单编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    private Long id;

    @Schema(description = "部门编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    private Long deptId;

    @Schema(description = "部门名称", example = "财务部")
    private String deptName;

    @Schema(description = "报送周期", requiredMode = Schema.RequiredMode.REQUIRED, example = "2024-01")
    private String reportPeriod;

    @Schema(description = "报表类型", requiredMode = Schema.RequiredMode.REQUIRED, example = "MONTHLY")
    private String reportType;

    @Schema(description = "表单数据（JSON格式）")
    private Map<String, Object> formData;

    @Schema(description = "状态", requiredMode = Schema.RequiredMode.REQUIRED, example = "0")
    private Integer status;

    @Schema(description = "提交人编号", example = "1")
    private Long submitUserId;

    @Schema(description = "提交人姓名", example = "张三")
    private String submitUserName;

    @Schema(description = "提交时间")
    private LocalDateTime submitTime;

    @Schema(description = "创建时间", requiredMode = Schema.RequiredMode.REQUIRED)
    private LocalDateTime createTime;

    @Schema(description = "更新时间", requiredMode = Schema.RequiredMode.REQUIRED)
    private LocalDateTime updateTime;

    @Schema(description = "创建人", example = "admin")
    private String creator;

}
