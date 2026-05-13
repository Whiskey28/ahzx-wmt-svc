package com.wmt.module.pfb.service.auth;

import com.wmt.module.system.controller.admin.auth.vo.AuthLoginRespVO;
import com.wmt.module.system.controller.admin.auth.vo.AuthResetPasswordReqVO;
import com.wmt.module.system.controller.admin.auth.vo.AuthSmsLoginReqVO;
import com.wmt.module.pfb.controller.pfb.auth.vo.PfbAuthLoginReqVO;
import com.wmt.module.pfb.controller.pfb.auth.vo.PfbAuthRegisterReqVO;
import com.wmt.module.pfb.controller.pfb.auth.vo.PfbAuthSmsSendReqVO;
import com.wmt.module.pfb.controller.pfb.auth.vo.PfbAuthValidateSmsReqVO;

public interface PfbAuthService {

    void sendSmsCode(PfbAuthSmsSendReqVO reqVO);

    void validateSmsCode(PfbAuthValidateSmsReqVO reqVO);

    AuthLoginRespVO login(PfbAuthLoginReqVO reqVO);

    AuthLoginRespVO smsLogin(AuthSmsLoginReqVO reqVO);

    AuthLoginRespVO register(PfbAuthRegisterReqVO reqVO);

    void resetPassword(AuthResetPasswordReqVO reqVO);

    AuthLoginRespVO refreshToken(String refreshToken);

    void logout(String token);

    void accountCancel(long sysUserId);
}
