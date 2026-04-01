package com.wmt.module.ticket.controller.admin.bpm;

import com.wmt.framework.common.pojo.CommonResult;
import com.wmt.framework.common.util.object.BeanUtils;
import com.wmt.module.ticket.controller.admin.bpm.vo.TicketBpmDemoCreateReqVO;
import com.wmt.module.ticket.controller.admin.bpm.vo.TicketBpmDemoRespVO;
import com.wmt.module.ticket.dal.dataobject.bpm.TicketBpmDemoDO;
import com.wmt.module.ticket.service.bpm.TicketBpmDemoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import static com.wmt.framework.common.pojo.CommonResult.success;
import static com.wmt.framework.security.core.util.SecurityFrameworkUtils.getLoginUserId;

/**
 * 工单模块：业务表单 + BPM 联调演示（数据与监听均在 ticket，仅调用 BPM 发起流程）
 */
@Tag(name = "管理后台 - 工单业务表单流程演示")
@RestController
@RequestMapping("/ticket/bpm-demo")
@Validated
public class TicketBpmDemoController {

    @Resource
    private TicketBpmDemoService ticketBpmDemoService;

    @PostMapping("/create")
    @PreAuthorize("@ss.hasPermission('bpm:process-instance:create')")
    @Operation(summary = "创建演示业务并发起流程")
    public CommonResult<Long> create(@Valid @RequestBody TicketBpmDemoCreateReqVO reqVO) {
        return success(ticketBpmDemoService.create(reqVO, getLoginUserId()));
    }

    @GetMapping("/get")
    @PreAuthorize("@ss.hasPermission('bpm:process-instance:query')")
    @Operation(summary = "获取演示业务详情")
    @Parameter(name = "id", description = "业务主键（与流程 businessKey 一致）", required = true)
    public CommonResult<TicketBpmDemoRespVO> get(@RequestParam("id") Long id) {
        TicketBpmDemoDO row = ticketBpmDemoService.get(id);
        return success(BeanUtils.toBean(row, TicketBpmDemoRespVO.class));
    }
}
