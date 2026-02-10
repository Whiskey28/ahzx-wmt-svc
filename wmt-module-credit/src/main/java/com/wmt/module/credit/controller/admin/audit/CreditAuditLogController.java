package com.wmt.module.credit.controller.admin.audit;

import com.wmt.framework.apilog.core.annotation.ApiAccessLog;
import com.wmt.framework.common.pojo.CommonResult;
import com.wmt.framework.common.pojo.PageResult;
import com.wmt.module.credit.controller.admin.audit.vo.CreditAuditLogPageReqVO;
import com.wmt.module.credit.controller.admin.audit.vo.CreditAuditLogRespVO;
import com.wmt.module.credit.convert.CreditAuditLogConvert;
import com.wmt.module.credit.dal.dataobject.audit.CreditAuditLogDO;
import com.wmt.module.credit.service.audit.CreditAuditLogService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.wmt.framework.apilog.core.enums.OperateTypeEnum.GET;
import static com.wmt.framework.common.pojo.CommonResult.success;

/**
 * 管理后台 - 征信审计日志
 *
 * @author AHC源码
 */
@Tag(name = "管理后台 - 征信审计日志")
@RestController
@RequestMapping("/credit/audit-log")
@Validated
public class CreditAuditLogController {

    @Resource
    private CreditAuditLogService auditLogService;

    @GetMapping("/page")
    @Operation(summary = "获得审计日志分页")
    @PreAuthorize("@ss.hasPermission('credit:audit-log:query')")
    @ApiAccessLog(operateType = GET)
    public CommonResult<PageResult<CreditAuditLogRespVO>> getAuditLogPage(@Valid CreditAuditLogPageReqVO pageReqVO) {
        PageResult<CreditAuditLogDO> pageResult = auditLogService.getAuditLogPage(pageReqVO);
        return success(CreditAuditLogConvert.INSTANCE.convertPage(pageResult));
    }

    @GetMapping("/list-by-biz")
    @Operation(summary = "根据业务类型和ID查询审计日志列表")
    @Parameter(name = "bizType", description = "业务类型", required = true, example = "FORM_DATA")
    @Parameter(name = "bizId", description = "业务编号", required = true, example = "1")
    @PreAuthorize("@ss.hasPermission('credit:audit-log:query')")
    @ApiAccessLog(operateType = GET)
    public CommonResult<List<CreditAuditLogRespVO>> getAuditLogListByBiz(
            @RequestParam("bizType") String bizType,
            @RequestParam("bizId") Long bizId) {
        List<CreditAuditLogDO> list = auditLogService.getAuditLogList(bizType, bizId);
        return success(CreditAuditLogConvert.INSTANCE.convertList(list));
    }

}
