package com.wmt.module.pfb.service.auth;

import cn.hutool.core.util.StrUtil;
import com.anji.captcha.model.common.ResponseModel;
import com.anji.captcha.model.vo.CaptchaVO;
import com.anji.captcha.service.CaptchaService;
import com.wmt.framework.common.enums.CommonStatusEnum;
import com.wmt.framework.common.enums.UserTypeEnum;
import com.wmt.framework.common.util.monitor.TracerUtils;
import com.wmt.framework.common.util.object.BeanUtils;
import com.wmt.framework.common.util.servlet.ServletUtils;
import com.wmt.framework.common.util.validation.ValidationUtils;
import com.wmt.module.system.api.logger.dto.LoginLogCreateReqDTO;
import com.wmt.module.system.api.sms.SmsCodeApi;
import com.wmt.module.system.api.sms.dto.code.SmsCodeSendReqDTO;
import com.wmt.module.system.api.sms.dto.code.SmsCodeUseReqDTO;
import com.wmt.module.system.api.sms.dto.code.SmsCodeValidateReqDTO;
import com.wmt.module.system.controller.admin.auth.vo.AuthLoginRespVO;
import com.wmt.module.system.controller.admin.auth.vo.AuthResetPasswordReqVO;
import com.wmt.module.system.controller.admin.auth.vo.AuthSmsLoginReqVO;
import com.wmt.module.system.controller.admin.auth.vo.CaptchaVerificationReqVO;
import com.wmt.module.system.controller.admin.user.vo.user.UserSaveReqVO;
import com.wmt.module.system.dal.dataobject.oauth2.OAuth2AccessTokenDO;
import com.wmt.module.system.dal.dataobject.user.AdminUserDO;
import com.wmt.module.system.enums.logger.LoginLogTypeEnum;
import com.wmt.module.system.enums.logger.LoginResultEnum;
import com.wmt.module.system.enums.oauth2.OAuth2ClientConstants;
import com.wmt.module.system.enums.sms.SmsSceneEnum;
import com.wmt.module.system.service.logger.LoginLogService;
import com.wmt.module.system.service.oauth2.OAuth2TokenService;
import com.wmt.module.system.service.user.AdminUserService;
import com.wmt.module.pfb.controller.pfb.auth.vo.PfbAuthLoginReqVO;
import com.wmt.module.pfb.controller.pfb.auth.vo.PfbAuthRegisterReqVO;
import com.wmt.module.pfb.controller.pfb.auth.vo.PfbAuthSmsSendReqVO;
import com.wmt.module.pfb.controller.pfb.auth.vo.PfbAuthValidateSmsReqVO;
import com.wmt.module.pfb.enums.PfbAuthSmsSceneEnum;
import com.wmt.module.pfb.enums.PfbErrorCodeConstants;
import jakarta.annotation.Resource;
import jakarta.validation.Validator;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

import static com.wmt.framework.common.exception.util.ServiceExceptionUtil.exception;
import static com.wmt.framework.common.util.servlet.ServletUtils.getClientIP;
import static com.wmt.module.system.enums.ErrorCodeConstants.AUTH_LOGIN_BAD_CREDENTIALS;
import static com.wmt.module.system.enums.ErrorCodeConstants.AUTH_LOGIN_CAPTCHA_CODE_ERROR;
import static com.wmt.module.system.enums.ErrorCodeConstants.AUTH_LOGIN_USER_DISABLED;
import static com.wmt.module.system.enums.ErrorCodeConstants.AUTH_MOBILE_NOT_EXISTS;
import static com.wmt.module.system.enums.ErrorCodeConstants.USER_MOBILE_NOT_EXISTS;
import static com.wmt.module.system.enums.ErrorCodeConstants.AUTH_REGISTER_CAPTCHA_CODE_ERROR;
import static com.wmt.module.system.enums.ErrorCodeConstants.USER_MOBILE_EXISTS;

@Service
@Slf4j
public class PfbAuthServiceImpl implements PfbAuthService {

    @Resource
    private AdminUserService userService;
    @Resource
    private OAuth2TokenService oauth2TokenService;
    @Resource
    private LoginLogService loginLogService;
    @Resource
    private SmsCodeApi smsCodeApi;
    @Resource
    private CaptchaService captchaService;
    @Resource
    private Validator validator;

    @Value("${wmt.captcha.enable:true}")
    @Setter
    private Boolean captchaEnable;

    @Override
    public void sendSmsCode(PfbAuthSmsSendReqVO reqVO) {
        PfbAuthSmsSceneEnum sceneEnum = PfbAuthSmsSceneEnum.of(reqVO.getScene());
        if (sceneEnum == null) {
            throw exception(PfbErrorCodeConstants.PFB_SMS_SCENE_INVALID);
        }
        SmsSceneEnum smsScene = sceneEnum.toSmsScene();

        if (captchaEnable && Objects.equals(sceneEnum, PfbAuthSmsSceneEnum.RESET_PASSWORD)) {
            ResponseModel response = doValidateCaptcha(reqVO);
            if (!response.isSuccess()) {
                throw exception(AUTH_REGISTER_CAPTCHA_CODE_ERROR, response.getRepMsg());
            }
        }

        AdminUserDO user = userService.getUserByMobile(reqVO.getMobile());
        if (sceneEnum == PfbAuthSmsSceneEnum.LOGIN || sceneEnum == PfbAuthSmsSceneEnum.RESET_PASSWORD) {
            if (user == null) {
                throw exception(AUTH_MOBILE_NOT_EXISTS);
            }
        } else if (sceneEnum == PfbAuthSmsSceneEnum.REGISTER) {
            if (user != null) {
                throw exception(USER_MOBILE_EXISTS);
            }
        }

        SmsCodeSendReqDTO dto = new SmsCodeSendReqDTO();
        dto.setMobile(reqVO.getMobile());
        dto.setScene(smsScene.getScene());
        dto.setCreateIp(getClientIP());
        smsCodeApi.sendSmsCode(dto);
    }

    @Override
    public void validateSmsCode(PfbAuthValidateSmsReqVO reqVO) {
        PfbAuthSmsSceneEnum sceneEnum = PfbAuthSmsSceneEnum.of(reqVO.getScene());
        if (sceneEnum == null) {
            throw exception(PfbErrorCodeConstants.PFB_SMS_SCENE_INVALID);
        }
        SmsCodeValidateReqDTO dto = new SmsCodeValidateReqDTO();
        dto.setMobile(reqVO.getMobile());
        dto.setScene(sceneEnum.toSmsScene().getScene());
        dto.setCode(reqVO.getCode());
        smsCodeApi.validateSmsCode(dto);
    }

    @Override
    public AuthLoginRespVO login(PfbAuthLoginReqVO reqVO) {
        validateCaptchaIfNeeded(reqVO);
        AdminUserDO user = userService.getUserByMobile(reqVO.getMobile());
        if (user == null) {
            createLoginLog(null, reqVO.getMobile(), LoginLogTypeEnum.LOGIN_USERNAME, LoginResultEnum.BAD_CREDENTIALS);
            throw exception(AUTH_LOGIN_BAD_CREDENTIALS);
        }
        if (!userService.isPasswordMatch(reqVO.getPassword(), user.getPassword())) {
            createLoginLog(user.getId(), reqVO.getMobile(), LoginLogTypeEnum.LOGIN_USERNAME, LoginResultEnum.BAD_CREDENTIALS);
            throw exception(AUTH_LOGIN_BAD_CREDENTIALS);
        }
        if (CommonStatusEnum.isDisable(user.getStatus())) {
            createLoginLog(user.getId(), reqVO.getMobile(), LoginLogTypeEnum.LOGIN_USERNAME, LoginResultEnum.USER_DISABLED);
            throw exception(AUTH_LOGIN_USER_DISABLED);
        }
        // Task 3.1：手机号 + 密码 + 短信验证码；短信仅在密码校验通过后消费，避免错误密码刷掉验证码
        smsCodeApi.useSmsCode(new SmsCodeUseReqDTO()
                .setMobile(reqVO.getMobile())
                .setCode(reqVO.getSmsCode())
                .setScene(SmsSceneEnum.PFB_SMS_LOGIN.getScene())
                .setUsedIp(getClientIP()));
        return createTokenAfterLoginSuccess(user.getId(), reqVO.getMobile(), LoginLogTypeEnum.LOGIN_USERNAME);
    }

    @Override
    public AuthLoginRespVO smsLogin(AuthSmsLoginReqVO reqVO) {
        AdminUserDO user = userService.getUserByMobile(reqVO.getMobile());
        if (user == null) {
            throw exception(AUTH_MOBILE_NOT_EXISTS);
        }
        if (CommonStatusEnum.isDisable(user.getStatus())) {
            throw exception(AUTH_LOGIN_USER_DISABLED);
        }
        smsCodeApi.useSmsCode(new SmsCodeUseReqDTO()
                .setMobile(reqVO.getMobile())
                .setCode(reqVO.getCode())
                .setScene(SmsSceneEnum.PFB_SMS_LOGIN.getScene())
                .setUsedIp(getClientIP()));
        return createTokenAfterLoginSuccess(user.getId(), reqVO.getMobile(), LoginLogTypeEnum.LOGIN_MOBILE);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public AuthLoginRespVO register(PfbAuthRegisterReqVO reqVO) {
        validateCaptchaIfNeeded(reqVO);
        smsCodeApi.useSmsCode(new SmsCodeUseReqDTO()
                .setMobile(reqVO.getMobile())
                .setCode(reqVO.getSmsCode())
                .setScene(SmsSceneEnum.PFB_SMS_REGISTER.getScene())
                .setUsedIp(getClientIP()));

        UserSaveReqVO save = new UserSaveReqVO();
        save.setUsername(reqVO.getMobile());
        save.setNickname(StrUtil.blankToDefault(reqVO.getNickname(), "用户"));
        save.setMobile(reqVO.getMobile());
        save.setPassword(reqVO.getPassword());
        save.setDeptId(null);
        Long userId = userService.createUser(save);
        return createTokenAfterLoginSuccess(userId, reqVO.getMobile(), LoginLogTypeEnum.LOGIN_USERNAME);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void resetPassword(AuthResetPasswordReqVO reqVO) {
        AdminUserDO user = userService.getUserByMobile(reqVO.getMobile());
        if (user == null) {
            throw exception(USER_MOBILE_NOT_EXISTS);
        }
        smsCodeApi.useSmsCode(new SmsCodeUseReqDTO()
                .setMobile(reqVO.getMobile())
                .setCode(reqVO.getCode())
                .setScene(SmsSceneEnum.PFB_SMS_RESET_PASSWORD.getScene())
                .setUsedIp(getClientIP()));
        userService.updateUserPassword(user.getId(), reqVO.getPassword());
    }

    @Override
    public AuthLoginRespVO refreshToken(String refreshToken) {
        OAuth2AccessTokenDO accessTokenDO = oauth2TokenService.refreshAccessToken(refreshToken, OAuth2ClientConstants.CLIENT_ID_DEFAULT);
        return BeanUtils.toBean(accessTokenDO, AuthLoginRespVO.class);
    }

    @Override
    public void logout(String token) {
        OAuth2AccessTokenDO accessTokenDO = oauth2TokenService.removeAccessToken(token);
        if (accessTokenDO == null) {
            return;
        }
        LoginLogCreateReqDTO reqDTO = new LoginLogCreateReqDTO();
        reqDTO.setLogType(LoginLogTypeEnum.LOGOUT_SELF.getType());
        reqDTO.setTraceId(TracerUtils.getTraceId());
        reqDTO.setUserId(accessTokenDO.getUserId());
        reqDTO.setUserType(accessTokenDO.getUserType());
        AdminUserDO user = userService.getUser(accessTokenDO.getUserId());
        reqDTO.setUsername(user != null ? user.getMobile() : null);
        reqDTO.setUserAgent(ServletUtils.getUserAgent());
        reqDTO.setUserIp(ServletUtils.getClientIP());
        reqDTO.setResult(LoginResultEnum.SUCCESS.getResult());
        loginLogService.createLoginLog(reqDTO);
    }

    @Override
    public void accountCancel(long sysUserId) {
        throw exception(PfbErrorCodeConstants.PFB_ACCOUNT_CANCEL_TODO);
    }

    private void validateCaptchaIfNeeded(CaptchaVerificationReqVO reqVO) {
        if (!captchaEnable) {
            return;
        }
        ValidationUtils.validate(validator, reqVO, CaptchaVerificationReqVO.CodeEnableGroup.class);
        ResponseModel response = doValidateCaptcha(reqVO);
        if (!response.isSuccess()) {
            createLoginLog(null, safeMobile(reqVO), LoginLogTypeEnum.LOGIN_USERNAME, LoginResultEnum.CAPTCHA_CODE_ERROR);
            throw exception(AUTH_LOGIN_CAPTCHA_CODE_ERROR, response.getRepMsg());
        }
    }

    private String safeMobile(CaptchaVerificationReqVO reqVO) {
        if (reqVO instanceof PfbAuthLoginReqVO p) {
            return p.getMobile();
        }
        if (reqVO instanceof PfbAuthRegisterReqVO p) {
            return p.getMobile();
        }
        return "";
    }

    private ResponseModel doValidateCaptcha(CaptchaVerificationReqVO reqVO) {
        if (!captchaEnable) {
            return ResponseModel.success();
        }
        CaptchaVO captchaVO = new CaptchaVO();
        captchaVO.setCaptchaVerification(reqVO.getCaptchaVerification());
        return captchaService.verification(captchaVO);
    }

    private AuthLoginRespVO createTokenAfterLoginSuccess(Long userId, String displayName, LoginLogTypeEnum logType) {
        createLoginLog(userId, displayName, logType, LoginResultEnum.SUCCESS);
        OAuth2AccessTokenDO accessTokenDO = oauth2TokenService.createAccessToken(
                userId, UserTypeEnum.PFB.getValue(), OAuth2ClientConstants.CLIENT_ID_DEFAULT, null);
        return BeanUtils.toBean(accessTokenDO, AuthLoginRespVO.class);
    }

    private void createLoginLog(Long userId, String username, LoginLogTypeEnum logTypeEnum, LoginResultEnum loginResult) {
        LoginLogCreateReqDTO reqDTO = new LoginLogCreateReqDTO();
        reqDTO.setLogType(logTypeEnum.getType());
        reqDTO.setTraceId(TracerUtils.getTraceId());
        reqDTO.setUserId(userId);
        reqDTO.setUserType(UserTypeEnum.PFB.getValue());
        reqDTO.setUsername(username);
        reqDTO.setUserAgent(ServletUtils.getUserAgent());
        reqDTO.setUserIp(ServletUtils.getClientIP());
        reqDTO.setResult(loginResult.getResult());
        loginLogService.createLoginLog(reqDTO);
        if (userId != null && Objects.equals(LoginResultEnum.SUCCESS.getResult(), loginResult.getResult())) {
            userService.updateUserLogin(userId, ServletUtils.getClientIP());
        }
    }
}
