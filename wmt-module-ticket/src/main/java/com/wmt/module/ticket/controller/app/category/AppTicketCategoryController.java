package com.wmt.module.ticket.controller.app.category;

import com.wmt.framework.common.pojo.CommonResult;
import com.wmt.framework.common.util.object.BeanUtils;
import com.wmt.module.ticket.controller.app.category.vo.AppTicketCategoryRespVO;
import com.wmt.module.ticket.service.category.TicketCategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static com.wmt.framework.common.pojo.CommonResult.success;

@Tag(name = "用户 App - 工单分类")
@RestController
@RequestMapping("/ticket/category")
@Validated
public class AppTicketCategoryController {

    @Resource
    private TicketCategoryService ticketCategoryService;

    @GetMapping("/list")
    @Operation(summary = "查询启用分类")
    public CommonResult<List<AppTicketCategoryRespVO>> getEnabledCategoryList() {
        return success(BeanUtils.toBean(ticketCategoryService.getEnableCategoryList(), AppTicketCategoryRespVO.class));
    }
}
