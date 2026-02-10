/*
 Credit Role Module Database Script for PostgreSQL 15
 征信模块（基于角色）数据库脚本 - 增量表结构
 
 说明：
 1. 本脚本创建基于角色的征信模块表结构
 2. 主要改动：将区分维度从 dept_id（部门）改为 role_id（角色）
 3. 保留 dept_id 作为辅助字段，用于权限控制和查询
 
 Date: 2025-01-XX
*/

-- ----------------------------
-- Table structure for credit_field_config_role
-- 征信字段配置表（基于角色）
-- ----------------------------
DROP TABLE IF EXISTS credit_field_config_role;
CREATE TABLE credit_field_config_role (
    id int8 NOT NULL,
    role_id int8 NOT NULL DEFAULT 0,
    dept_id int8 NULL,
    field_code varchar(64) NOT NULL,
    field_name varchar(128) NOT NULL,
    field_type varchar(32) NOT NULL,
    required int2 NOT NULL DEFAULT 0,
    default_value varchar(512) NULL,
    validation_rule jsonb NULL,
    display_order int4 NOT NULL DEFAULT 0,
    status int2 NOT NULL DEFAULT 1,
    creator varchar(64) NULL DEFAULT '',
    create_time timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updater varchar(64) NULL DEFAULT '',
    update_time timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
    deleted int2 NOT NULL DEFAULT 0,
    tenant_id int8 NOT NULL DEFAULT 0
);

ALTER TABLE credit_field_config_role ADD CONSTRAINT pk_credit_field_config_role PRIMARY KEY (id);

CREATE UNIQUE INDEX uk_credit_field_config_role_01 ON credit_field_config_role (role_id, field_code, deleted) WHERE deleted = 0;
CREATE INDEX idx_credit_field_config_role_role_id ON credit_field_config_role (role_id);
CREATE INDEX idx_credit_field_config_role_dept_id ON credit_field_config_role (dept_id);
CREATE INDEX idx_credit_field_config_role_status ON credit_field_config_role (status) WHERE deleted = 0;

COMMENT ON TABLE credit_field_config_role IS '征信字段配置表（基于角色）';
COMMENT ON COLUMN credit_field_config_role.id IS '字段编号';
COMMENT ON COLUMN credit_field_config_role.role_id IS '角色编号（0表示通用字段）';
COMMENT ON COLUMN credit_field_config_role.dept_id IS '部门编号（辅助字段，可选，用于查询和权限控制）';
COMMENT ON COLUMN credit_field_config_role.field_code IS '字段编码（唯一标识）';
COMMENT ON COLUMN credit_field_config_role.field_name IS '字段名称';
COMMENT ON COLUMN credit_field_config_role.field_type IS '字段类型（NUMBER/DECIMAL/TEXT/DATE等）';
COMMENT ON COLUMN credit_field_config_role.required IS '是否必填（0-否，1-是）';
COMMENT ON COLUMN credit_field_config_role.default_value IS '默认值';
COMMENT ON COLUMN credit_field_config_role.validation_rule IS '校验规则（JSON格式）';
COMMENT ON COLUMN credit_field_config_role.display_order IS '显示顺序';
COMMENT ON COLUMN credit_field_config_role.status IS '状态（0-禁用，1-启用）';
COMMENT ON COLUMN credit_field_config_role.creator IS '创建者';
COMMENT ON COLUMN credit_field_config_role.create_time IS '创建时间';
COMMENT ON COLUMN credit_field_config_role.updater IS '更新者';
COMMENT ON COLUMN credit_field_config_role.update_time IS '更新时间';
COMMENT ON COLUMN credit_field_config_role.deleted IS '是否删除';
COMMENT ON COLUMN credit_field_config_role.tenant_id IS '租户编号';

DROP SEQUENCE IF EXISTS credit_field_config_role_seq;
CREATE SEQUENCE credit_field_config_role_seq START 1;

-- ----------------------------
-- Table structure for credit_form_data_role
-- 征信表单数据表（基于角色）
-- ----------------------------
DROP TABLE IF EXISTS credit_form_data_role;
CREATE TABLE credit_form_data_role (
    id int8 NOT NULL,
    role_id int8 NOT NULL,
    dept_id int8 NULL,
    report_period varchar(7) NOT NULL,
    report_type varchar(16) NOT NULL,
    form_data jsonb NOT NULL,
    status int2 NOT NULL DEFAULT 0,
    submit_user_id int8 NULL,
    submit_time timestamp NULL,
    creator varchar(64) NULL DEFAULT '',
    create_time timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updater varchar(64) NULL DEFAULT '',
    update_time timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
    deleted int2 NOT NULL DEFAULT 0,
    tenant_id int8 NOT NULL DEFAULT 0
);

ALTER TABLE credit_form_data_role ADD CONSTRAINT pk_credit_form_data_role PRIMARY KEY (id);

CREATE UNIQUE INDEX uk_credit_form_data_role_01 ON credit_form_data_role (role_id, report_period, report_type, deleted) WHERE deleted = 0;
CREATE INDEX idx_credit_form_data_role_role_id ON credit_form_data_role (role_id);
CREATE INDEX idx_credit_form_data_role_dept_id ON credit_form_data_role (dept_id);
CREATE INDEX idx_credit_form_data_role_report_period ON credit_form_data_role (report_period);
CREATE INDEX idx_credit_form_data_role_report_type ON credit_form_data_role (report_type);
CREATE INDEX idx_credit_form_data_role_status ON credit_form_data_role (status) WHERE deleted = 0;

-- PostgreSQL JSONB GIN索引（用于高效查询JSON字段）
CREATE INDEX idx_credit_form_data_role_form_data_gin ON credit_form_data_role USING GIN (form_data);

COMMENT ON TABLE credit_form_data_role IS '征信表单数据表（基于角色）';
COMMENT ON COLUMN credit_form_data_role.id IS '表单编号';
COMMENT ON COLUMN credit_form_data_role.role_id IS '角色编号（主要区分字段）';
COMMENT ON COLUMN credit_form_data_role.dept_id IS '部门编号（辅助字段，记录填写人所属部门，用于权限控制和查询）';
COMMENT ON COLUMN credit_form_data_role.report_period IS '报送周期（格式：YYYY-MM或YYYY-Q1/Q2/Q3/Q4）';
COMMENT ON COLUMN credit_form_data_role.report_type IS '报表类型（MONTHLY-月报，QUARTERLY-季报）';
COMMENT ON COLUMN credit_form_data_role.form_data IS '表单数据（JSONB格式，存储所有字段值）';
COMMENT ON COLUMN credit_form_data_role.status IS '状态（0-草稿，1-已提交，2-已报送）';
COMMENT ON COLUMN credit_form_data_role.submit_user_id IS '提交人编号';
COMMENT ON COLUMN credit_form_data_role.submit_time IS '提交时间';
COMMENT ON COLUMN credit_form_data_role.creator IS '创建者';
COMMENT ON COLUMN credit_form_data_role.create_time IS '创建时间';
COMMENT ON COLUMN credit_form_data_role.updater IS '更新者';
COMMENT ON COLUMN credit_form_data_role.update_time IS '更新时间';
COMMENT ON COLUMN credit_form_data_role.deleted IS '是否删除';
COMMENT ON COLUMN credit_form_data_role.tenant_id IS '租户编号';

DROP SEQUENCE IF EXISTS credit_form_data_role_seq;
CREATE SEQUENCE credit_form_data_role_seq START 1;

-- ----------------------------
-- Table structure for credit_calculation_rule_role
-- 征信计算规则配置表（基于角色）
-- ----------------------------
DROP TABLE IF EXISTS credit_calculation_rule_role;
CREATE TABLE credit_calculation_rule_role (
    id int8 NOT NULL,
    target_field_code varchar(64) NOT NULL,
    report_type varchar(16) NOT NULL,
    rule_type varchar(32) NOT NULL,
    rule_expression text NOT NULL,
    data_source jsonb NULL,
    calculation_order int4 NOT NULL DEFAULT 0,
    description varchar(512) NULL,
    status int2 NOT NULL DEFAULT 1,
    creator varchar(64) NULL DEFAULT '',
    create_time timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updater varchar(64) NULL DEFAULT '',
    update_time timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
    deleted int2 NOT NULL DEFAULT 0,
    tenant_id int8 NOT NULL DEFAULT 0
);

ALTER TABLE credit_calculation_rule_role ADD CONSTRAINT pk_credit_calculation_rule_role PRIMARY KEY (id);

CREATE INDEX idx_credit_calculation_rule_role_target_field ON credit_calculation_rule_role (target_field_code, report_type) WHERE deleted = 0;
CREATE INDEX idx_credit_calculation_rule_role_calculation_order ON credit_calculation_rule_role (calculation_order) WHERE deleted = 0 AND status = 1;
CREATE INDEX idx_credit_calculation_rule_role_report_type ON credit_calculation_rule_role (report_type) WHERE deleted = 0 AND status = 1;

COMMENT ON TABLE credit_calculation_rule_role IS '征信计算规则配置表（基于角色）';
COMMENT ON COLUMN credit_calculation_rule_role.id IS '规则编号';
COMMENT ON COLUMN credit_calculation_rule_role.target_field_code IS '目标字段编码（计算结果字段）';
COMMENT ON COLUMN credit_calculation_rule_role.report_type IS '报表类型（MONTHLY-月报，QUARTERLY-季报）';
COMMENT ON COLUMN credit_calculation_rule_role.rule_type IS '规则类型（SUM-求和，AVG-平均，FORMULA-公式，AGGREGATE-聚合）';
COMMENT ON COLUMN credit_calculation_rule_role.rule_expression IS '计算表达式（SpEL表达式，使用 #role{id} 引用角色表单数据，例如：#sum({#role201[...], #role202[...]})）';
COMMENT ON COLUMN credit_calculation_rule_role.data_source IS '数据来源（JSON格式，定义从哪些字段或报表获取数据）';
COMMENT ON COLUMN credit_calculation_rule_role.calculation_order IS '计算顺序';
COMMENT ON COLUMN credit_calculation_rule_role.description IS '规则描述';
COMMENT ON COLUMN credit_calculation_rule_role.status IS '状态（0-禁用，1-启用）';
COMMENT ON COLUMN credit_calculation_rule_role.creator IS '创建者';
COMMENT ON COLUMN credit_calculation_rule_role.create_time IS '创建时间';
COMMENT ON COLUMN credit_calculation_rule_role.updater IS '更新者';
COMMENT ON COLUMN credit_calculation_rule_role.update_time IS '更新时间';
COMMENT ON COLUMN credit_calculation_rule_role.deleted IS '是否删除';
COMMENT ON COLUMN credit_calculation_rule_role.tenant_id IS '租户编号';

DROP SEQUENCE IF EXISTS credit_calculation_rule_role_seq;
CREATE SEQUENCE credit_calculation_rule_role_seq START 1;

-- ----------------------------
-- Table structure for credit_summary_report_role
-- 征信汇总报表表（基于角色）
-- ----------------------------
DROP TABLE IF EXISTS credit_summary_report_role;
CREATE TABLE credit_summary_report_role (
    id int8 NOT NULL,
    report_period varchar(7) NOT NULL,
    report_type varchar(16) NOT NULL,
    report_data jsonb NOT NULL,
    calculation_log text NULL,
    status int2 NOT NULL DEFAULT 0,
    generate_time timestamp NULL,
    creator varchar(64) NULL DEFAULT '',
    create_time timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updater varchar(64) NULL DEFAULT '',
    update_time timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
    deleted int2 NOT NULL DEFAULT 0,
    tenant_id int8 NOT NULL DEFAULT 0
);

ALTER TABLE credit_summary_report_role ADD CONSTRAINT pk_credit_summary_report_role PRIMARY KEY (id);

CREATE UNIQUE INDEX uk_credit_summary_report_role_01 ON credit_summary_report_role (report_period, report_type, deleted) WHERE deleted = 0;
CREATE INDEX idx_credit_summary_report_role_period ON credit_summary_report_role (report_period);
CREATE INDEX idx_credit_summary_report_role_type ON credit_summary_report_role (report_type);
CREATE INDEX idx_credit_summary_report_role_status ON credit_summary_report_role (status) WHERE deleted = 0;

-- PostgreSQL JSONB GIN索引（用于高效查询JSON字段）
CREATE INDEX idx_credit_summary_report_role_data_gin ON credit_summary_report_role USING GIN (report_data);

COMMENT ON TABLE credit_summary_report_role IS '征信汇总报表表（基于角色）';
COMMENT ON COLUMN credit_summary_report_role.id IS '报表编号';
COMMENT ON COLUMN credit_summary_report_role.report_period IS '报送周期';
COMMENT ON COLUMN credit_summary_report_role.report_type IS '报表类型（MONTHLY-月报，QUARTERLY-季报）';
COMMENT ON COLUMN credit_summary_report_role.report_data IS '报表数据（JSONB格式，存储所有汇总字段值）';
COMMENT ON COLUMN credit_summary_report_role.calculation_log IS '计算日志（记录计算过程）';
COMMENT ON COLUMN credit_summary_report_role.status IS '状态（0-计算中，1-已完成，2-已报送）';
COMMENT ON COLUMN credit_summary_report_role.generate_time IS '生成时间';
COMMENT ON COLUMN credit_summary_report_role.creator IS '创建者';
COMMENT ON COLUMN credit_summary_report_role.create_time IS '创建时间';
COMMENT ON COLUMN credit_summary_report_role.updater IS '更新者';
COMMENT ON COLUMN credit_summary_report_role.update_time IS '更新时间';
COMMENT ON COLUMN credit_summary_report_role.deleted IS '是否删除';
COMMENT ON COLUMN credit_summary_report_role.tenant_id IS '租户编号';

DROP SEQUENCE IF EXISTS credit_summary_report_role_seq;
CREATE SEQUENCE credit_summary_report_role_seq START 1;

-- ----------------------------
-- Table structure for credit_audit_log_role
-- 征信审计日志表（基于角色）
-- ----------------------------
DROP TABLE IF EXISTS credit_audit_log_role;
CREATE TABLE credit_audit_log_role (
    id int8 NOT NULL,
    biz_type varchar(32) NOT NULL,
    biz_id int8 NOT NULL,
    role_id int8 NULL,
    operation_type varchar(32) NOT NULL,
    operation_user_id int8 NOT NULL,
    operation_desc varchar(512) NULL,
    before_data jsonb NULL,
    after_data jsonb NULL,
    change_fields varchar(512) NULL,
    ip_address varchar(64) NULL,
    user_agent varchar(512) NULL,
    create_time timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
    tenant_id int8 NOT NULL DEFAULT 0
);

ALTER TABLE credit_audit_log_role ADD CONSTRAINT pk_credit_audit_log_role PRIMARY KEY (id);

CREATE INDEX idx_credit_audit_log_role_biz_type_id ON credit_audit_log_role (biz_type, biz_id);
CREATE INDEX idx_credit_audit_log_role_role_id ON credit_audit_log_role (role_id);
CREATE INDEX idx_credit_audit_log_role_operation_user ON credit_audit_log_role (operation_user_id);
CREATE INDEX idx_credit_audit_log_role_create_time ON credit_audit_log_role (create_time);
CREATE INDEX idx_credit_audit_log_role_operation_type ON credit_audit_log_role (operation_type);

COMMENT ON TABLE credit_audit_log_role IS '征信审计日志表（基于角色）';
COMMENT ON COLUMN credit_audit_log_role.id IS '日志编号';
COMMENT ON COLUMN credit_audit_log_role.biz_type IS '业务类型（FORM_DATA-表单数据，SUMMARY_REPORT-汇总报表）';
COMMENT ON COLUMN credit_audit_log_role.biz_id IS '业务编号';
COMMENT ON COLUMN credit_audit_log_role.role_id IS '角色编号（可选，用于记录操作涉及的角色）';
COMMENT ON COLUMN credit_audit_log_role.operation_type IS '操作类型（CREATE-创建，UPDATE-更新，SUBMIT-提交，CALCULATE-计算）';
COMMENT ON COLUMN credit_audit_log_role.operation_user_id IS '操作用户编号';
COMMENT ON COLUMN credit_audit_log_role.operation_desc IS '操作描述';
COMMENT ON COLUMN credit_audit_log_role.before_data IS '变更前数据（JSONB格式）';
COMMENT ON COLUMN credit_audit_log_role.after_data IS '变更后数据（JSONB格式）';
COMMENT ON COLUMN credit_audit_log_role.change_fields IS '变更字段列表（逗号分隔）';
COMMENT ON COLUMN credit_audit_log_role.ip_address IS 'IP地址';
COMMENT ON COLUMN credit_audit_log_role.user_agent IS '用户代理';
COMMENT ON COLUMN credit_audit_log_role.create_time IS '创建时间';
COMMENT ON COLUMN credit_audit_log_role.tenant_id IS '租户编号';

DROP SEQUENCE IF EXISTS credit_audit_log_role_seq;
CREATE SEQUENCE credit_audit_log_role_seq START 1;

-- ----------------------------
-- Table structure for credit_validation_rule_role
-- 征信校验规则配置表（基于角色）
-- ----------------------------
DROP TABLE IF EXISTS credit_validation_rule_role;
CREATE TABLE credit_validation_rule_role (
    id int8 NOT NULL,
    field_code varchar(64) NOT NULL,
    rule_type varchar(32) NOT NULL,
    rule_expression varchar(512) NOT NULL,
    error_message varchar(256) NOT NULL,
    priority int4 NOT NULL DEFAULT 0,
    status int2 NOT NULL DEFAULT 1,
    creator varchar(64) NULL DEFAULT '',
    create_time timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updater varchar(64) NULL DEFAULT '',
    update_time timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
    deleted int2 NOT NULL DEFAULT 0,
    tenant_id int8 NOT NULL DEFAULT 0
);

ALTER TABLE credit_validation_rule_role ADD CONSTRAINT pk_credit_validation_rule_role PRIMARY KEY (id);

CREATE INDEX idx_credit_validation_rule_role_field_code ON credit_validation_rule_role (field_code) WHERE deleted = 0 AND status = 1;
CREATE INDEX idx_credit_validation_rule_role_priority ON credit_validation_rule_role (priority) WHERE deleted = 0 AND status = 1;

COMMENT ON TABLE credit_validation_rule_role IS '征信校验规则配置表（基于角色）';
COMMENT ON COLUMN credit_validation_rule_role.id IS '规则编号';
COMMENT ON COLUMN credit_validation_rule_role.field_code IS '字段编码';
COMMENT ON COLUMN credit_validation_rule_role.rule_type IS '规则类型（REQUIRED-必填，RANGE-范围，REGEX-正则，CUSTOM-自定义）';
COMMENT ON COLUMN credit_validation_rule_role.rule_expression IS '校验表达式';
COMMENT ON COLUMN credit_validation_rule_role.error_message IS '错误提示信息';
COMMENT ON COLUMN credit_validation_rule_role.priority IS '优先级';
COMMENT ON COLUMN credit_validation_rule_role.status IS '状态（0-禁用，1-启用）';
COMMENT ON COLUMN credit_validation_rule_role.creator IS '创建者';
COMMENT ON COLUMN credit_validation_rule_role.create_time IS '创建时间';
COMMENT ON COLUMN credit_validation_rule_role.updater IS '更新者';
COMMENT ON COLUMN credit_validation_rule_role.update_time IS '更新时间';
COMMENT ON COLUMN credit_validation_rule_role.deleted IS '是否删除';
COMMENT ON COLUMN credit_validation_rule_role.tenant_id IS '租户编号';

DROP SEQUENCE IF EXISTS credit_validation_rule_role_seq;
CREATE SEQUENCE credit_validation_rule_role_seq START 1;
