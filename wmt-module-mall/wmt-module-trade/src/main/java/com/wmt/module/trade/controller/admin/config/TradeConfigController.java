package com.wmt.module.trade.controller.admin.config;

import com.wmt.framework.common.pojo.CommonResult;
import com.wmt.module.trade.controller.admin.config.vo.TradeConfigRespVO;
import com.wmt.module.trade.controller.admin.config.vo.TradeConfigSaveReqVO;
import com.wmt.module.trade.convert.config.TradeConfigConvert;
import com.wmt.module.trade.dal.dataobject.config.TradeConfigDO;
import com.wmt.module.trade.service.config.TradeConfigService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.annotation.Resource;
import jakarta.validation.Valid;

import static com.wmt.framework.common.pojo.CommonResult.success;

@Tag(name = "管理后台 - 交易中心配置")
@RestController
@RequestMapping("/trade/config")
@Validated
public class TradeConfigController {

    @Resource
    private TradeConfigService tradeConfigService;

    @Value("${wmt.tencent-lbs-key}")
    private String tencentLbsKey;

    @PostMapping("/save")
    @Operation(summary = "更新交易中心配置")
    @PreAuthorize("@ss.hasPermission('trade:config:save')")
    public CommonResult<Boolean> updateConfig(@Valid @RequestBody TradeConfigSaveReqVO updateReqVO) {
        tradeConfigService.saveTradeConfig(updateReqVO);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得交易中心配置")
    @PreAuthorize("@ss.hasPermission('trade:config:query')")
    public CommonResult<TradeConfigRespVO> getConfig() {
        TradeConfigDO config = tradeConfigService.getTradeConfig();
        TradeConfigRespVO configVO = TradeConfigConvert.INSTANCE.convert(config);
        if (configVO != null) {
            configVO.setTencentLbsKey(tencentLbsKey);
        }
        return success(configVO);
    }

}
