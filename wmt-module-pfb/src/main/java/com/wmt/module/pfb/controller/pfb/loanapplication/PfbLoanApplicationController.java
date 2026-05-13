package com.wmt.module.pfb.controller.pfb.loanapplication;

import com.wmt.framework.common.pojo.CommonResult;
import com.wmt.framework.security.core.util.SecurityFrameworkUtils;
import com.wmt.module.pfb.controller.pfb.loanapplication.vo.PfbLoanApplicationListItemVO;
import com.wmt.module.pfb.controller.pfb.loanapplication.vo.PfbLoanApplicationSubmitReqVO;
import com.wmt.module.pfb.service.loanapplication.PfbLoanApplicationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.wmt.framework.common.pojo.CommonResult.success;

@Tag(name = "普惠金融 PFB - 融资申请")
@RestController
@RequestMapping("/pfb/loan-application")
@Validated
public class PfbLoanApplicationController {

    @Resource
    private PfbLoanApplicationService pfbLoanApplicationService;

    @PostMapping("/submit")
    @Operation(summary = "提交融资申请")
    public CommonResult<Boolean> submit(@Valid @RequestBody PfbLoanApplicationSubmitReqVO reqVO) {
        pfbLoanApplicationService.submit(SecurityFrameworkUtils.getLoginUserId(), reqVO);
        return success(true);
    }

    @GetMapping("/list")
    @Operation(summary = "我的申请列表（一期只读）")
    public CommonResult<List<PfbLoanApplicationListItemVO>> list() {
        return success(pfbLoanApplicationService.listMine(SecurityFrameworkUtils.getLoginUserId()));
    }
}
