package com.wmt.module.pfb.controller.pfb.enterprise;

import com.wmt.framework.common.pojo.CommonResult;
import com.wmt.framework.security.core.util.SecurityFrameworkUtils;
import com.wmt.module.pfb.controller.pfb.enterprise.vo.PfbEnterpriseCreateReqVO;
import com.wmt.module.pfb.controller.pfb.enterprise.vo.PfbEnterpriseListItemVO;
import com.wmt.module.pfb.controller.pfb.enterprise.vo.PfbEnterpriseUpdateReqVO;
import com.wmt.module.pfb.service.enterprise.PfbEnterpriseService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.wmt.framework.common.pojo.CommonResult.success;

@Tag(name = "普惠金融 PFB - 企业", description = "需登录；企业主键为 UUID 字符串")
@RestController
@RequestMapping("/pfb/enterprise")
@Validated
public class PfbEnterpriseController {

    @Resource
    private PfbEnterpriseService pfbEnterpriseService;

    @GetMapping("/list")
    @Operation(summary = "我的企业列表", description = "含默认企业标记")
    public CommonResult<List<PfbEnterpriseListItemVO>> list() {
        return success(pfbEnterpriseService.listMine(SecurityFrameworkUtils.getLoginUserId()));
    }

    @PostMapping
    @Operation(summary = "添加企业并绑定", description = "POST 路径为 /pfb/enterprise（无额外 path）；统一社会信用代码 18 位校验")
    public CommonResult<String> create(@Valid @RequestBody PfbEnterpriseCreateReqVO reqVO) {
        return success(pfbEnterpriseService.createAndBind(SecurityFrameworkUtils.getLoginUserId(), reqVO));
    }

    @PutMapping("/{id}")
    @Operation(summary = "编辑企业", description = "仅能修改本人已绑定企业")
    public CommonResult<Boolean> update(
            @Parameter(description = "企业 UUID", required = true) @PathVariable("id") String id,
            @Valid @RequestBody PfbEnterpriseUpdateReqVO reqVO) {
        pfbEnterpriseService.updateEnt(SecurityFrameworkUtils.getLoginUserId(), id, reqVO);
        return success(true);
    }

    @PostMapping("/{id}/set-default")
    @Operation(summary = "设为默认企业")
    public CommonResult<Boolean> setDefault(
            @Parameter(description = "企业 UUID", required = true) @PathVariable("id") String id) {
        pfbEnterpriseService.setDefault(SecurityFrameworkUtils.getLoginUserId(), id);
        return success(true);
    }

    @PostMapping("/{id}/legal-verify")
    @Operation(summary = "法人核验占位", description = "一期返回业务提示「能力尚未开通」等")
    public CommonResult<Boolean> legalVerify(
            @Parameter(description = "企业 UUID", required = true) @PathVariable("id") String id) {
        pfbEnterpriseService.legalVerifyStub(SecurityFrameworkUtils.getLoginUserId(), id);
        return success(true);
    }
}
