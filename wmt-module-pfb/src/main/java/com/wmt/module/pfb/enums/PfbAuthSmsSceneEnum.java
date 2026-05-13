package com.wmt.module.pfb.enums;

import com.wmt.module.system.enums.sms.SmsSceneEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * PFB 认证接口对外暴露的短信场景（入参），映射到 {@link SmsSceneEnum} 独立场景号。
 */
@Getter
@AllArgsConstructor
public enum PfbAuthSmsSceneEnum {

    LOGIN("LOGIN"),
    REGISTER("REGISTER"),
    RESET_PASSWORD("RESET_PASSWORD");

    private final String scene;

    public static PfbAuthSmsSceneEnum of(String scene) {
        for (PfbAuthSmsSceneEnum e : values()) {
            if (e.scene.equals(scene)) {
                return e;
            }
        }
        return null;
    }

    public SmsSceneEnum toSmsScene() {
        return switch (this) {
            case LOGIN -> SmsSceneEnum.PFB_SMS_LOGIN;
            case REGISTER -> SmsSceneEnum.PFB_SMS_REGISTER;
            case RESET_PASSWORD -> SmsSceneEnum.PFB_SMS_RESET_PASSWORD;
        };
    }
}
