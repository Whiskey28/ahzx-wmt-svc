package com.wmt.module.pfb.enums;

import com.wmt.framework.common.exception.ErrorCode;

/**
 * 普惠金融进件模块错误码（1_030_000_000 段）
 */
public interface PfbErrorCodeConstants {

    ErrorCode PFB_SMS_SCENE_INVALID = new ErrorCode(1_030_000_001, "短信场景不合法");
    ErrorCode PFB_CREDIT_CODE_INVALID = new ErrorCode(1_030_000_002, "统一社会信用代码格式不正确");
    ErrorCode PFB_ENT_NOT_FOUND = new ErrorCode(1_030_000_003, "企业不存在");
    ErrorCode PFB_ENT_NO_PERMISSION = new ErrorCode(1_030_000_004, "无权操作该企业");
    ErrorCode PFB_PRODUCT_NOT_ONLINE = new ErrorCode(1_030_000_005, "贷款产品未上架或不存在");
    ErrorCode PFB_GATE_INDIVIDUAL = new ErrorCode(1_030_000_006, "请先完成实名认证");
    ErrorCode PFB_GATE_ENTERPRISE = new ErrorCode(1_030_000_007, "请先绑定企业");
    ErrorCode PFB_ENT_NAME_EMPTY = new ErrorCode(1_030_000_008, "企业名称为空，无法提交申请");
    ErrorCode PFB_APPLY_AMOUNT_NOT_WAN = new ErrorCode(1_030_000_009, "申请金额须为「万元」整数倍（以分为单位校验）");
    ErrorCode PFB_ID_CARD_BOUND_OTHER_USER = new ErrorCode(1_030_000_010, "该身份证号已绑定其他账号");
    ErrorCode PFB_LEGAL_VERIFY_NOT_IMPLEMENTED = new ErrorCode(1_030_000_011, "法人核验能力尚未开通，敬请期待");
    ErrorCode PFB_ACCOUNT_CANCEL_TODO = new ErrorCode(1_030_000_012, "账号注销流程待产品/安全定案，暂未开放");
    ErrorCode PFB_ENT_CREDIT_ALREADY_BOUND = new ErrorCode(1_030_000_013, "您已绑定该统一社会信用代码的企业");
    ErrorCode PFB_CREDIT_AUTH_NOT_ACCEPTED = new ErrorCode(1_030_000_014, "请先勾选征信查询报送授权");
}
