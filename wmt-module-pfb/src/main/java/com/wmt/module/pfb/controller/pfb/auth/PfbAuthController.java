package com.wmt.module.pfb.controller.pfb.auth;

import com.wmt.framework.common.pojo.CommonResult;
import com.wmt.framework.security.config.SecurityProperties;
import com.wmt.framework.security.core.util.SecurityFrameworkUtils;
import com.wmt.module.system.controller.admin.auth.vo.AuthLoginRespVO;
import com.wmt.module.system.controller.admin.auth.vo.AuthResetPasswordReqVO;
import com.wmt.module.system.controller.admin.auth.vo.AuthSmsLoginReqVO;
import com.wmt.module.pfb.controller.pfb.auth.vo.PfbAuthLoginReqVO;
import com.wmt.module.pfb.controller.pfb.auth.vo.PfbAuthRegisterReqVO;
import com.wmt.module.pfb.controller.pfb.auth.vo.PfbAuthSmsSendReqVO;
import com.wmt.module.pfb.controller.pfb.auth.vo.PfbAuthValidateSmsReqVO;
import com.wmt.module.pfb.service.auth.PfbAuthService;
import cn.hutool.core.util.StrUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.annotation.security.PermitAll;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import static com.wmt.framework.common.pojo.CommonResult.success;

@Tag(name = "普惠金融 PFB - 认证")
@RestController
@RequestMapping("/pfb/auth")
@Validated
public class PfbAuthController {

    @Resource
    private PfbAuthService pfbAuthService;
    @Resource
    private SecurityProperties securityProperties;

    @PostMapping("/send-sms-code")
    @PermitAll
    @Operation(summary = "发送短信验证码")
    public CommonResult<Boolean> sendSmsCode(@Valid @RequestBody PfbAuthSmsSendReqVO reqVO) {
        pfbAuthService.sendSmsCode(reqVO);
        return success(true);
    }

    @PostMapping("/validate-sms-code")
    @PermitAll
    @Operation(summary = "校验短信验证码")
    public CommonResult<Boolean> validateSmsCode(@Valid @RequestBody PfbAuthValidateSmsReqVO reqVO) {
        pfbAuthService.validateSmsCode(reqVO);
        return success(true);
    }

    @PostMapping("/login")
    @PermitAll
    @Operation(summary = "手机号+密码+短信验证码登录（US-02）；wmt.captcha.enable=true 时另需图形验证码参数")
    public CommonResult<AuthLoginRespVO> login(@Valid @RequestBody PfbAuthLoginReqVO reqVO) {
        return success(pfbAuthService.login(reqVO));
    }

    @PostMapping("/sms-login")
    @PermitAll
    @Operation(summary = "手机号+短信验证码登录")
    public CommonResult<AuthLoginRespVO> smsLogin(@Valid @RequestBody AuthSmsLoginReqVO reqVO) {
        return success(pfbAuthService.smsLogin(reqVO));
    }

    @PostMapping("/register")
    @PermitAll
    @Operation(summary = "注册")
    public CommonResult<AuthLoginRespVO> register(@Valid @RequestBody PfbAuthRegisterReqVO reqVO) {
        return success(pfbAuthService.register(reqVO));
    }

    @PostMapping("/reset-password")
    @PermitAll
    @Operation(summary = "重置密码")
    public CommonResult<Boolean> resetPassword(@Valid @RequestBody AuthResetPasswordReqVO reqVO) {
        pfbAuthService.resetPassword(reqVO);
        return success(true);
    }

    @PostMapping("/refresh-token")
    @PermitAll
    @Operation(summary = "刷新令牌")
    public CommonResult<AuthLoginRespVO> refreshToken(@RequestParam("refreshToken") String refreshToken) {
        return success(pfbAuthService.refreshToken(refreshToken));
    }

    @PostMapping("/logout")
    @PermitAll
    @Operation(summary = "登出")
    public CommonResult<Boolean> logout(HttpServletRequest request) {
        String token = SecurityFrameworkUtils.obtainAuthorization(request,
                securityProperties.getTokenHeader(), securityProperties.getTokenParameter());
        if (StrUtil.isNotBlank(token)) {
            pfbAuthService.logout(token);
        }
        return success(true);
    }

    @PostMapping("/account-cancel")
    @Operation(summary = "注销账号（一期占位）")
    public CommonResult<Boolean> accountCancel() {
        pfbAuthService.accountCancel(SecurityFrameworkUtils.getLoginUserId());
        return success(true);
    }
}
