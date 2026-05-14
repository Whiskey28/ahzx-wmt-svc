package com.wmt.module.pfb.service.auth;

import cn.hutool.core.util.ReflectUtil;
import com.wmt.framework.common.enums.CommonStatusEnum;
import com.wmt.framework.common.enums.UserTypeEnum;
import com.wmt.framework.common.util.monitor.TracerUtils;
import com.wmt.framework.common.util.servlet.ServletUtils;
import com.wmt.module.pfb.controller.pfb.auth.vo.PfbAuthLoginReqVO;
import com.wmt.module.pfb.controller.pfb.auth.vo.PfbAuthSmsSendReqVO;
import com.wmt.module.pfb.enums.PfbErrorCodeConstants;
import com.wmt.module.system.api.sms.SmsCodeApi;
import com.wmt.module.system.controller.admin.auth.vo.AuthLoginRespVO;
import com.wmt.module.system.dal.dataobject.oauth2.OAuth2AccessTokenDO;
import com.wmt.module.system.dal.dataobject.user.AdminUserDO;
import com.wmt.module.system.enums.oauth2.OAuth2ClientConstants;
import com.wmt.module.system.service.logger.LoginLogService;
import com.wmt.module.system.service.oauth2.OAuth2TokenService;
import com.wmt.module.system.service.user.AdminUserService;
import com.anji.captcha.service.CaptchaService;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import static com.wmt.framework.test.core.util.AssertUtils.assertServiceException;
import static com.wmt.module.system.enums.ErrorCodeConstants.AUTH_LOGIN_BAD_CREDENTIALS;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PfbAuthServiceImplTest {

    @InjectMocks
    private PfbAuthServiceImpl pfbAuthService;

    @Mock
    private AdminUserService userService;
    @Mock
    private OAuth2TokenService oauth2TokenService;
    @Mock
    private LoginLogService loginLogService;
    @Mock
    private SmsCodeApi smsCodeApi;
    @Mock
    private CaptchaService captchaService;
    @Mock
    private Validator validator;

    @BeforeEach
    void setUp() {
        pfbAuthService.setCaptchaEnable(false);
        ReflectUtil.setFieldValue(pfbAuthService, "validator",
                Validation.buildDefaultValidatorFactory().getValidator());
    }

    @Test
    void sendSmsCode_invalidScene_throws() {
        try (MockedStatic<ServletUtils> servlet = mockStatic(ServletUtils.class)) {
            servlet.when(ServletUtils::getClientIP).thenReturn("127.0.0.1");
            PfbAuthSmsSendReqVO req = new PfbAuthSmsSendReqVO();
            req.setMobile("13800138000");
            req.setScene("UNKNOWN");
            assertServiceException(() -> pfbAuthService.sendSmsCode(req), PfbErrorCodeConstants.PFB_SMS_SCENE_INVALID);
        }
    }

    @Test
    void login_wrongPassword_throws() {
        try (MockedStatic<ServletUtils> servlet = mockStatic(ServletUtils.class);
             MockedStatic<TracerUtils> tracer = mockStatic(TracerUtils.class)) {
            servlet.when(ServletUtils::getUserAgent).thenReturn("JUnit");
            servlet.when(ServletUtils::getClientIP).thenReturn("127.0.0.1");
            tracer.when(TracerUtils::getTraceId).thenReturn("trace-1");

            AdminUserDO user = new AdminUserDO();
            user.setId(10L);
            user.setMobile("13800138000");
            user.setPassword("hash");
            user.setStatus(CommonStatusEnum.ENABLE.getStatus());
            when(userService.getUserByMobile("13800138000")).thenReturn(user);
            when(userService.isPasswordMatch("wrong", "hash")).thenReturn(false);

            PfbAuthLoginReqVO req = new PfbAuthLoginReqVO();
            req.setMobile("13800138000");
            req.setPassword("wrong");
            req.setSmsCode("9999");

            assertServiceException(() -> pfbAuthService.login(req), AUTH_LOGIN_BAD_CREDENTIALS);
            verify(smsCodeApi, never()).useSmsCode(any());
        }
    }

    @Test
    void login_success_returnsToken() {
        try (MockedStatic<ServletUtils> servlet = mockStatic(ServletUtils.class);
             MockedStatic<TracerUtils> tracer = mockStatic(TracerUtils.class)) {
            servlet.when(ServletUtils::getUserAgent).thenReturn("JUnit");
            servlet.when(ServletUtils::getClientIP).thenReturn("127.0.0.1");
            tracer.when(TracerUtils::getTraceId).thenReturn("trace-1");

            AdminUserDO user = new AdminUserDO();
            user.setId(10L);
            user.setMobile("13800138000");
            user.setPassword("hash");
            user.setStatus(CommonStatusEnum.ENABLE.getStatus());
            when(userService.getUserByMobile("13800138000")).thenReturn(user);
            when(userService.isPasswordMatch("secret", "hash")).thenReturn(true);

            OAuth2AccessTokenDO token = new OAuth2AccessTokenDO();
            token.setAccessToken("access-token");
            token.setRefreshToken("refresh-token");
            when(oauth2TokenService.createAccessToken(eq(10L), eq(UserTypeEnum.PFB.getValue()),
                    eq(OAuth2ClientConstants.CLIENT_ID_DEFAULT), isNull())).thenReturn(token);

            PfbAuthLoginReqVO req = new PfbAuthLoginReqVO();
            req.setMobile("13800138000");
            req.setPassword("secret");
            req.setSmsCode("8888");

            AuthLoginRespVO resp = pfbAuthService.login(req);
            assertNotNull(resp);
            assertEquals("access-token", resp.getAccessToken());
            verify(smsCodeApi).useSmsCode(any());
        }
    }
}
