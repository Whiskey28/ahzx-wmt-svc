package com.wmt.module.member.convert.auth;

import com.wmt.module.member.controller.app.auth.vo.*;
import com.wmt.module.member.controller.app.social.vo.AppSocialUserUnbindReqVO;
import com.wmt.module.member.controller.app.user.vo.AppMemberUserResetPasswordReqVO;
import com.wmt.framework.common.biz.system.oauth2.dto.OAuth2AccessTokenRespDTO;
import com.wmt.module.system.api.sms.dto.code.SmsCodeSendReqDTO;
import com.wmt.module.system.api.sms.dto.code.SmsCodeUseReqDTO;
import com.wmt.module.system.api.sms.dto.code.SmsCodeValidateReqDTO;
import com.wmt.module.system.api.social.dto.SocialUserBindReqDTO;
import com.wmt.module.system.api.social.dto.SocialUserUnbindReqDTO;
import com.wmt.module.system.api.social.dto.SocialWxJsapiSignatureRespDTO;
import com.wmt.module.system.enums.sms.SmsSceneEnum;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface AuthConvert {

    AuthConvert INSTANCE = Mappers.getMapper(AuthConvert.class);

    SocialUserBindReqDTO convert(Long userId, Integer userType, AppAuthSocialLoginReqVO reqVO);
    SocialUserUnbindReqDTO convert(Long userId, Integer userType, AppSocialUserUnbindReqVO reqVO);

    SmsCodeSendReqDTO convert(AppAuthSmsSendReqVO reqVO);
    SmsCodeUseReqDTO convert(AppMemberUserResetPasswordReqVO reqVO, SmsSceneEnum scene, String usedIp);
    SmsCodeUseReqDTO convert(AppAuthSmsLoginReqVO reqVO, Integer scene, String usedIp);

    AppAuthLoginRespVO convert(OAuth2AccessTokenRespDTO bean, String openid);

    SmsCodeValidateReqDTO convert(AppAuthSmsValidateReqVO bean);

    SocialWxJsapiSignatureRespDTO convert(SocialWxJsapiSignatureRespDTO bean);

}
