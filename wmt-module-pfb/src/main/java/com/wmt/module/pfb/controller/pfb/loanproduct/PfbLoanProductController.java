package com.wmt.module.pfb.controller.pfb.loanproduct;

import com.wmt.framework.common.pojo.CommonResult;
import com.wmt.module.pfb.controller.pfb.loanproduct.vo.PfbLoanProductRespVO;
import com.wmt.module.pfb.service.loanproduct.PfbLoanProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.annotation.security.PermitAll;
import jakarta.validation.constraints.NotBlank;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static com.wmt.framework.common.pojo.CommonResult.success;

@Tag(name = "普惠金融 PFB - 贷款产品")
@RestController
@RequestMapping("/pfb/loan-product")
@Validated
public class PfbLoanProductController {

    @Resource
    private PfbLoanProductService pfbLoanProductService;

    @GetMapping("/list")
    @PermitAll
    @Operation(summary = "上架产品列表")
    public CommonResult<List<PfbLoanProductRespVO>> list() {
        return success(pfbLoanProductService.listOnShelf());
    }

    @GetMapping("/hot-list")
    @PermitAll
    @Operation(summary = "热门产品")
    public CommonResult<List<PfbLoanProductRespVO>> hotList(
            @Parameter(description = "条数上限，默认 20") @RequestParam(value = "limit", required = false) Integer limit) {
        return success(pfbLoanProductService.listHot(limit == null ? 20 : limit));
    }

    @GetMapping("/get")
    @PermitAll
    @Operation(summary = "产品详情")
    public CommonResult<PfbLoanProductRespVO> get(@NotBlank @RequestParam("id") String id) {
        return success(pfbLoanProductService.getOnShelf(id));
    }
}
