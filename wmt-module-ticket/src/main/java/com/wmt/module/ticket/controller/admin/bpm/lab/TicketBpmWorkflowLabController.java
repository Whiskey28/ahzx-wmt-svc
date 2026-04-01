package com.wmt.module.ticket.controller.admin.bpm.lab;

import com.wmt.framework.common.pojo.CommonResult;
import com.wmt.framework.common.util.object.BeanUtils;
import com.wmt.module.ticket.controller.admin.bpm.lab.vo.TicketBpmWorkflowLabCreateReqVO;
import com.wmt.module.ticket.controller.admin.bpm.lab.vo.TicketBpmWorkflowLabRespVO;
import com.wmt.module.ticket.dal.dataobject.bpm.TicketBpmWorkflowLabDO;
import com.wmt.module.ticket.service.bpm.lab.TicketBpmWorkflowLabService;
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

@Tag(name = "管理后台 - 工单工作流多模式实验")
@RestController
@RequestMapping("/ticket/bpm-lab")
@Validated
public class TicketBpmWorkflowLabController {

    @Resource
    private TicketBpmWorkflowLabService ticketBpmWorkflowLabService;

    @PostMapping("/create")
    @PreAuthorize("@ss.hasPermission('bpm:process-instance:create')")
    @Operation(summary = "创建工作流实验数据并发起流程")
    public CommonResult<Long> create(@Valid @RequestBody TicketBpmWorkflowLabCreateReqVO reqVO) {
        return success(ticketBpmWorkflowLabService.create(reqVO, getLoginUserId()));
    }

    @GetMapping("/get")
    @PreAuthorize("@ss.hasPermission('bpm:process-instance:query')")
    @Operation(summary = "获取工作流实验详情")
    @Parameter(name = "id", description = "业务主键（与流程 businessKey 一致）", required = true)
    public CommonResult<TicketBpmWorkflowLabRespVO> get(@RequestParam("id") Long id) {
        TicketBpmWorkflowLabDO row = ticketBpmWorkflowLabService.get(id);
        return success(BeanUtils.toBean(row, TicketBpmWorkflowLabRespVO.class));
    }

    @PostMapping("/trigger-receive")
    @PreAuthorize("@ss.hasPermission('bpm:process-instance:create')")
    @Operation(summary = "接收任务实验：模拟外部系统完成，触发 Receive Task")
    @Parameter(name = "id", description = "业务主键", required = true)
    public CommonResult<Boolean> triggerReceive(@RequestParam("id") Long id) {
        ticketBpmWorkflowLabService.triggerReceiveTask(id, getLoginUserId());
        return success(true);
    }
}
