package com.wmt.module.credit.enums;

import com.wmt.framework.common.exception.ErrorCode;

/**
 * Credit 错误码枚举类
 * <p>
 * credit 系统，使用 1-050-000-000 段
 */
public interface ErrorCodeConstants {

    // ========== 字段配置管理 1-050-000-000 ==========
    ErrorCode FIELD_CONFIG_NOT_EXISTS = new ErrorCode(1_050_000_000, "字段配置不存在");
    ErrorCode FIELD_CONFIG_CODE_DUPLICATE = new ErrorCode(1_050_000_001, "字段编码已存在");
    ErrorCode FIELD_CONFIG_DELETE_FAIL_USED = new ErrorCode(1_050_000_002, "删除字段配置失败，原因：字段已被使用");

    // ========== 表单数据管理 1-050-001-000 ==========
    ErrorCode FORM_DATA_NOT_EXISTS = new ErrorCode(1_050_001_000, "表单数据不存在");
    ErrorCode FORM_DATA_DEPT_MISMATCH = new ErrorCode(1_050_001_001, "操作失败，原因：您只能操作本部门的数据");
    ErrorCode FORM_DATA_STATUS_ERROR = new ErrorCode(1_050_001_002, "操作失败，原因：表单状态不正确");
    ErrorCode FORM_DATA_UPDATE_FAIL_NOT_DRAFT = new ErrorCode(1_050_001_003, "更新表单数据失败，原因：表单不是草稿状态");
    ErrorCode FORM_DATA_DELETE_FAIL_NOT_DRAFT = new ErrorCode(1_050_001_004, "删除表单数据失败，原因：表单不是草稿状态");
    ErrorCode FORM_DATA_SUBMIT_FAIL_NOT_DRAFT = new ErrorCode(1_050_001_005, "提交表单数据失败，原因：表单不是草稿状态");
    ErrorCode FORM_DATA_DUPLICATE = new ErrorCode(1_050_001_006, "该周期已存在表单数据");
    ErrorCode FORM_DATA_VALIDATION_FAIL = new ErrorCode(1_050_001_007, "表单数据校验失败：{}");

    // ========== 计算规则管理 1-050-002-000 ==========
    ErrorCode CALCULATION_RULE_NOT_EXISTS = new ErrorCode(1_050_002_000, "计算规则不存在");
    ErrorCode CALCULATION_RULE_TARGET_FIELD_DUPLICATE = new ErrorCode(1_050_002_001, "目标字段编码已存在计算规则");
    ErrorCode CALCULATION_EXECUTE_FAIL = new ErrorCode(1_050_002_002, "计算执行失败：{}");
    ErrorCode CALCULATION_EXPRESSION_INVALID = new ErrorCode(1_050_002_003, "计算表达式无效：{}");

    // ========== 汇总报表管理 1-050-003-000 ==========
    ErrorCode SUMMARY_REPORT_NOT_EXISTS = new ErrorCode(1_050_003_000, "汇总报表不存在");
    ErrorCode SUMMARY_REPORT_GENERATE_FAIL = new ErrorCode(1_050_003_001, "生成汇总报表失败：{}");
    ErrorCode SUMMARY_REPORT_GENERATE_FAIL_FORM_NOT_SUBMITTED = new ErrorCode(1_050_003_002, "生成汇总报表失败，原因：存在未提交的表单数据");
    ErrorCode SUMMARY_REPORT_GENERATE_FAIL_MONTHLY_NOT_GENERATED = new ErrorCode(1_050_003_003, "生成汇总报表失败，原因：相关月报尚未生成");
    ErrorCode SUMMARY_REPORT_DUPLICATE = new ErrorCode(1_050_003_004, "该周期已存在汇总报表");

    // ========== 校验规则管理 1-050-004-000 ==========
    ErrorCode VALIDATION_RULE_NOT_EXISTS = new ErrorCode(1_050_004_000, "校验规则不存在");
    ErrorCode VALIDATION_FAIL = new ErrorCode(1_050_004_001, "数据校验失败：{}");

    // ========== 审计日志 1-050-005-000 ==========
    ErrorCode AUDIT_LOG_NOT_EXISTS = new ErrorCode(1_050_005_000, "审计日志不存在");

}
