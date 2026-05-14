package com.wmt.module.pfb.controller.pfb.banner;

import com.wmt.framework.common.pojo.CommonResult;
import com.wmt.module.pfb.controller.pfb.banner.vo.PfbBannerRespVO;
import com.wmt.module.pfb.service.banner.PfbBannerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.annotation.security.PermitAll;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static com.wmt.framework.common.pojo.CommonResult.success;

@Tag(name = "普惠金融 PFB - Banner", description = "首页轮播；匿名可读")
@RestController
@RequestMapping("/pfb/banner")
@Validated
public class PfbBannerController {

    @Resource
    private PfbBannerService pfbBannerService;

    @GetMapping("/list")
    @PermitAll
    @Operation(summary = "首页 Banner 列表", description = "上架、未删除，且在 effective_begin / effective_end 时间窗内（空表示不限制）")
    public CommonResult<List<PfbBannerRespVO>> list() {
        return success(pfbBannerService.listActiveBanners());
    }
}
