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

@Tag(name = "普惠金融 PFB - 贷款产品", description = "列表/热门/详情为匿名可读（见 permit-all_urls）")
@RestController
@RequestMapping("/pfb/loan-product")
@Validated
public class PfbLoanProductController {

    @Resource
    private PfbLoanProductService pfbLoanProductService;

    @GetMapping("/list")
    @PermitAll
    @Operation(summary = "上架产品列表", description = "product_status=1 且未逻辑删除；按 sort_order、id 升序")
    public CommonResult<List<PfbLoanProductRespVO>> list() {
        return success(pfbLoanProductService.listOnShelf());
    }

    @GetMapping("/hot-list")
    @PermitAll
    @Operation(summary = "热门产品", description = "上架且 is_hot=1；limit 默认 20，服务端有上限裁剪")
    public CommonResult<List<PfbLoanProductRespVO>> hotList(
            @Parameter(description = "条数上限，默认 20", example = "10")
            @RequestParam(value = "limit", required = false) Integer limit) {
        return success(pfbLoanProductService.listHot(limit == null ? 20 : limit));
    }

    @GetMapping("/get")
    @PermitAll
    @Operation(summary = "产品详情", description = "仅返回上架产品；UUID 主键")
    public CommonResult<PfbLoanProductRespVO> get(
            @Parameter(description = "产品 UUID", required = true, example = "11111111-1111-1111-1111-111111111101")
            @NotBlank @RequestParam("id") String id) {
        return success(pfbLoanProductService.getOnShelf(id));
    }
}
