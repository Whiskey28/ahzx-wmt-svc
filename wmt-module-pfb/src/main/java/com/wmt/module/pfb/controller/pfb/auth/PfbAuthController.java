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
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.annotation.security.PermitAll;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import static com.wmt.framework.common.pojo.CommonResult.success;

@Tag(name = "普惠金融 PFB - 认证", description = "PFB 小程序认证；多数接口匿名；注销需登录")
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
    @Operation(summary = "发送短信验证码", description = "scene：LOGIN（已注册用户）、REGISTER（未注册）、RESET_PASSWORD（已注册）；RESET_PASSWORD 且开启图形验证码时需传 captchaVerification")
    public CommonResult<Boolean> sendSmsCode(@Valid @RequestBody PfbAuthSmsSendReqVO reqVO) {
        pfbAuthService.sendSmsCode(reqVO);
        return success(true);
    }

    @PostMapping("/validate-sms-code")
    @PermitAll
    @Operation(summary = "校验短信验证码", description = "仅校验不消费；消费在 login/register/reset-password 时完成")
    public CommonResult<Boolean> validateSmsCode(@Valid @RequestBody PfbAuthValidateSmsReqVO reqVO) {
        pfbAuthService.validateSmsCode(reqVO);
        return success(true);
    }

    @PostMapping("/login")
    @PermitAll
    @Operation(summary = "手机号+密码+短信验证码登录（US-02）", description = "短信场景为 PFB 独立 LOGIN；wmt.captcha.enable=true 时需传 captchaVerification 等图形验证码字段")
    public CommonResult<AuthLoginRespVO> login(@Valid @RequestBody PfbAuthLoginReqVO reqVO) {
        return success(pfbAuthService.login(reqVO));
    }

    @PostMapping("/sms-login")
    @PermitAll
    @Operation(summary = "手机号+短信验证码登录", description = "与 Admin 短信登录字段一致：mobile、code；短信场景为 PFB LOGIN")
    public CommonResult<AuthLoginRespVO> smsLogin(@Valid @RequestBody AuthSmsLoginReqVO reqVO) {
        return success(pfbAuthService.smsLogin(reqVO));
    }

    @PostMapping("/register")
    @PermitAll
    @Operation(summary = "注册", description = "创建 system_users 并签发 PFB 类型 Token；需 REGISTER 场景短信验证码；可选图形验证码")
    public CommonResult<AuthLoginRespVO> register(@Valid @RequestBody PfbAuthRegisterReqVO reqVO) {
        return success(pfbAuthService.register(reqVO));
    }

    @PostMapping("/reset-password")
    @PermitAll
    @Operation(summary = "重置密码", description = "需 RESET_PASSWORD 场景短信；发送短信时若开启验证码，需先通过图形验证")
    public CommonResult<Boolean> resetPassword(@Valid @RequestBody AuthResetPasswordReqVO reqVO) {
        pfbAuthService.resetPassword(reqVO);
        return success(true);
    }

    @PostMapping("/refresh-token")
    @PermitAll
    @Operation(summary = "刷新令牌", description = "使用 OAuth2 refreshToken 换发新的 accessToken")
    public CommonResult<AuthLoginRespVO> refreshToken(
            @Parameter(description = "刷新令牌", required = true, example = "eyJhbGciOiJIUzI1NiJ9...")
            @RequestParam("refreshToken") String refreshToken) {
        return success(pfbAuthService.refreshToken(refreshToken));
    }

    @PostMapping("/logout")
    @PermitAll
    @Operation(summary = "登出", description = "请求头携带 accessToken 时吊销；无 Token 亦返回成功")
    public CommonResult<Boolean> logout(HttpServletRequest request) {
        String token = SecurityFrameworkUtils.obtainAuthorization(request,
                securityProperties.getTokenHeader(), securityProperties.getTokenParameter());
        if (StrUtil.isNotBlank(token)) {
            pfbAuthService.logout(token);
        }
        return success(true);
    }

    @PostMapping("/account-cancel")
    @Operation(summary = "注销账号（一期占位）", description = "需登录；当前固定返回业务错误「流程待产品/安全定案」")
    public CommonResult<Boolean> accountCancel() {
        pfbAuthService.accountCancel(SecurityFrameworkUtils.getLoginUserId());
        return success(true);
    }
}
