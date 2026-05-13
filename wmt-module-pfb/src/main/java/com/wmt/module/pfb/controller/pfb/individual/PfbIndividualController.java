package com.wmt.module.pfb.controller.pfb.individual;

import com.wmt.framework.common.pojo.CommonResult;
import com.wmt.framework.security.core.util.SecurityFrameworkUtils;
import com.wmt.module.pfb.controller.pfb.individual.vo.PfbIndividualStatusRespVO;
import com.wmt.module.pfb.controller.pfb.individual.vo.PfbIndividualSubmitReqVO;
import com.wmt.module.pfb.service.individual.PfbIndividualService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import static com.wmt.framework.common.pojo.CommonResult.success;

@Tag(name = "普惠金融 PFB - 自然人")
@RestController
@RequestMapping("/pfb/individual")
@Validated
public class PfbIndividualController {

    @Resource
    private PfbIndividualService pfbIndividualService;

    @GetMapping("/status")
    @Operation(summary = "实名/绑定状态摘要")
    public CommonResult<PfbIndividualStatusRespVO> status() {
        return success(pfbIndividualService.getStatus(SecurityFrameworkUtils.getLoginUserId()));
    }

    @PostMapping("/submit")
    @Operation(summary = "提交/更新自然人并绑定")
    public CommonResult<Boolean> submit(@Valid @RequestBody PfbIndividualSubmitReqVO reqVO) {
        pfbIndividualService.submitOrUpdate(SecurityFrameworkUtils.getLoginUserId(), reqVO);
        return success(true);
    }

    @PostMapping("/l2/callback")
    @Operation(summary = "L2 回调占位")
    public CommonResult<Boolean> l2Callback() {
        pfbIndividualService.l2CallbackStub();
        return success(true);
    }
}
