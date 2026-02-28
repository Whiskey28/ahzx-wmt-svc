/*
 Credit Module Database Script for PostgreSQL 15
 征信模块数据库脚本
 
 Date: 2025-01-XX
*/

-- ----------------------------
-- Table structure for credit_field_config
-- 征信字段配置表
-- ----------------------------
DROP TABLE IF EXISTS credit_field_config;
CREATE TABLE credit_field_config (
    id int8 NOT NULL,
    dept_id int8 NOT NULL DEFAULT 0,
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

ALTER TABLE credit_field_config ADD CONSTRAINT pk_credit_field_config PRIMARY KEY (id);

CREATE UNIQUE INDEX uk_credit_field_config_01 ON credit_field_config (dept_id, field_code, deleted) WHERE deleted = 0;
CREATE INDEX idx_credit_field_config_dept_id ON credit_field_config (dept_id);
CREATE INDEX idx_credit_field_config_status ON credit_field_config (status) WHERE deleted = 0;

COMMENT ON TABLE credit_field_config IS '征信字段配置表';
COMMENT ON COLUMN credit_field_config.id IS '字段编号';
COMMENT ON COLUMN credit_field_config.dept_id IS '部门编号（0表示通用字段）';
COMMENT ON COLUMN credit_field_config.field_code IS '字段编码（唯一标识）';
COMMENT ON COLUMN credit_field_config.field_name IS '字段名称';
COMMENT ON COLUMN credit_field_config.field_type IS '字段类型（NUMBER/DECIMAL/TEXT/DATE等）';
COMMENT ON COLUMN credit_field_config.required IS '是否必填（0-否，1-是）';
COMMENT ON COLUMN credit_field_config.default_value IS '默认值';
COMMENT ON COLUMN credit_field_config.validation_rule IS '校验规则（JSON格式）';
COMMENT ON COLUMN credit_field_config.display_order IS '显示顺序';
COMMENT ON COLUMN credit_field_config.status IS '状态（0-禁用，1-启用）';
COMMENT ON COLUMN credit_field_config.creator IS '创建者';
COMMENT ON COLUMN credit_field_config.create_time IS '创建时间';
COMMENT ON COLUMN credit_field_config.updater IS '更新者';
COMMENT ON COLUMN credit_field_config.update_time IS '更新时间';
COMMENT ON COLUMN credit_field_config.deleted IS '是否删除';
COMMENT ON COLUMN credit_field_config.tenant_id IS '租户编号';

DROP SEQUENCE IF EXISTS credit_field_config_seq;
CREATE SEQUENCE credit_field_config_seq START 1;

-- ----------------------------
-- Table structure for credit_form_data
-- 征信表单数据表
-- ----------------------------
DROP TABLE IF EXISTS credit_form_data;
CREATE TABLE credit_form_data (
    id int8 NOT NULL,
    dept_id int8 NOT NULL,
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

ALTER TABLE credit_form_data ADD CONSTRAINT pk_credit_form_data PRIMARY KEY (id);

CREATE UNIQUE INDEX uk_credit_form_data_01 ON credit_form_data (dept_id, report_period, report_type, deleted) WHERE deleted = 0;
CREATE INDEX idx_credit_form_data_report_period ON credit_form_data (report_period);
CREATE INDEX idx_credit_form_data_report_type ON credit_form_data (report_type);
CREATE INDEX idx_credit_form_data_status ON credit_form_data (status) WHERE deleted = 0;
CREATE INDEX idx_credit_form_data_dept_id ON credit_form_data (dept_id);

-- PostgreSQL JSONB GIN索引（用于高效查询JSON字段）
CREATE INDEX idx_credit_form_data_form_data_gin ON credit_form_data USING GIN (form_data);

COMMENT ON TABLE credit_form_data IS '征信表单数据表';
COMMENT ON COLUMN credit_form_data.id IS '表单编号';
COMMENT ON COLUMN credit_form_data.dept_id IS '部门编号';
COMMENT ON COLUMN credit_form_data.report_period IS '报送周期（格式：YYYY-MM或YYYY-Q1/Q2/Q3/Q4）';
COMMENT ON COLUMN credit_form_data.report_type IS '报表类型（MONTHLY-月报，QUARTERLY-季报）';
COMMENT ON COLUMN credit_form_data.form_data IS '表单数据（JSONB格式，存储所有字段值）';
COMMENT ON COLUMN credit_form_data.status IS '状态（0-草稿，1-已提交，2-已报送）';
COMMENT ON COLUMN credit_form_data.submit_user_id IS '提交人编号';
COMMENT ON COLUMN credit_form_data.submit_time IS '提交时间';
COMMENT ON COLUMN credit_form_data.creator IS '创建者';
COMMENT ON COLUMN credit_form_data.create_time IS '创建时间';
COMMENT ON COLUMN credit_form_data.updater IS '更新者';
COMMENT ON COLUMN credit_form_data.update_time IS '更新时间';
COMMENT ON COLUMN credit_form_data.deleted IS '是否删除';
COMMENT ON COLUMN credit_form_data.tenant_id IS '租户编号';

DROP SEQUENCE IF EXISTS credit_form_data_seq;
CREATE SEQUENCE credit_form_data_seq START 1;

-- ----------------------------
-- Table structure for credit_calculation_rule
-- 征信计算规则配置表
-- ----------------------------
DROP TABLE IF EXISTS credit_calculation_rule;
CREATE TABLE credit_calculation_rule (
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

ALTER TABLE credit_calculation_rule ADD CONSTRAINT pk_credit_calculation_rule PRIMARY KEY (id);

CREATE INDEX idx_credit_calculation_rule_target_field ON credit_calculation_rule (target_field_code, report_type) WHERE deleted = 0;
CREATE INDEX idx_credit_calculation_rule_calculation_order ON credit_calculation_rule (calculation_order) WHERE deleted = 0 AND status = 1;
CREATE INDEX idx_credit_calculation_rule_report_type ON credit_calculation_rule (report_type) WHERE deleted = 0 AND status = 1;

COMMENT ON TABLE credit_calculation_rule IS '征信计算规则配置表';
COMMENT ON COLUMN credit_calculation_rule.id IS '规则编号';
COMMENT ON COLUMN credit_calculation_rule.target_field_code IS '目标字段编码（计算结果字段）';
COMMENT ON COLUMN credit_calculation_rule.report_type IS '报表类型（MONTHLY-月报，QUARTERLY-季报）';
COMMENT ON COLUMN credit_calculation_rule.rule_type IS '规则类型（SUM-求和，AVG-平均，FORMULA-公式，AGGREGATE-聚合）';
COMMENT ON COLUMN credit_calculation_rule.rule_expression IS '计算表达式（SpEL表达式或SQL片段）';
COMMENT ON COLUMN credit_calculation_rule.data_source IS '数据来源（JSON格式，定义从哪些字段或报表获取数据）';
COMMENT ON COLUMN credit_calculation_rule.calculation_order IS '计算顺序';
COMMENT ON COLUMN credit_calculation_rule.description IS '规则描述';
COMMENT ON COLUMN credit_calculation_rule.status IS '状态（0-禁用，1-启用）';
COMMENT ON COLUMN credit_calculation_rule.creator IS '创建者';
COMMENT ON COLUMN credit_calculation_rule.create_time IS '创建时间';
COMMENT ON COLUMN credit_calculation_rule.updater IS '更新者';
COMMENT ON COLUMN credit_calculation_rule.update_time IS '更新时间';
COMMENT ON COLUMN credit_calculation_rule.deleted IS '是否删除';
COMMENT ON COLUMN credit_calculation_rule.tenant_id IS '租户编号';

DROP SEQUENCE IF EXISTS credit_calculation_rule_seq;
CREATE SEQUENCE credit_calculation_rule_seq START 1;

-- ----------------------------
-- Table structure for credit_summary_report
-- 征信汇总报表表
-- ----------------------------
DROP TABLE IF EXISTS credit_summary_report;
CREATE TABLE credit_summary_report (
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

ALTER TABLE credit_summary_report ADD CONSTRAINT pk_credit_summary_report PRIMARY KEY (id);

CREATE UNIQUE INDEX uk_credit_summary_report_01 ON credit_summary_report (report_period, report_type, deleted) WHERE deleted = 0;
CREATE INDEX idx_credit_summary_report_period ON credit_summary_report (report_period);
CREATE INDEX idx_credit_summary_report_type ON credit_summary_report (report_type);
CREATE INDEX idx_credit_summary_report_status ON credit_summary_report (status) WHERE deleted = 0;

-- PostgreSQL JSONB GIN索引（用于高效查询JSON字段）
CREATE INDEX idx_credit_summary_report_data_gin ON credit_summary_report USING GIN (report_data);

COMMENT ON TABLE credit_summary_report IS '征信汇总报表表';
COMMENT ON COLUMN credit_summary_report.id IS '报表编号';
COMMENT ON COLUMN credit_summary_report.report_period IS '报送周期';
COMMENT ON COLUMN credit_summary_report.report_type IS '报表类型（MONTHLY-月报，QUARTERLY-季报）';
COMMENT ON COLUMN credit_summary_report.report_data IS '报表数据（JSONB格式，存储所有汇总字段值）';
COMMENT ON COLUMN credit_summary_report.calculation_log IS '计算日志（记录计算过程）';
COMMENT ON COLUMN credit_summary_report.status IS '状态（0-计算中，1-已完成，2-已报送）';
COMMENT ON COLUMN credit_summary_report.generate_time IS '生成时间';
COMMENT ON COLUMN credit_summary_report.creator IS '创建者';
COMMENT ON COLUMN credit_summary_report.create_time IS '创建时间';
COMMENT ON COLUMN credit_summary_report.updater IS '更新者';
COMMENT ON COLUMN credit_summary_report.update_time IS '更新时间';
COMMENT ON COLUMN credit_summary_report.deleted IS '是否删除';
COMMENT ON COLUMN credit_summary_report.tenant_id IS '租户编号';

DROP SEQUENCE IF EXISTS credit_summary_report_seq;
CREATE SEQUENCE credit_summary_report_seq START 1;

-- ----------------------------
-- Table structure for credit_audit_log
-- 征信审计日志表
-- ----------------------------
DROP TABLE IF EXISTS credit_audit_log;
CREATE TABLE credit_audit_log (
    id int8 NOT NULL,
    biz_type varchar(32) NOT NULL,
    biz_id int8 NOT NULL,
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

ALTER TABLE credit_audit_log ADD CONSTRAINT pk_credit_audit_log PRIMARY KEY (id);

CREATE INDEX idx_credit_audit_log_biz_type_id ON credit_audit_log (biz_type, biz_id);
CREATE INDEX idx_credit_audit_log_operation_user ON credit_audit_log (operation_user_id);
CREATE INDEX idx_credit_audit_log_create_time ON credit_audit_log (create_time);
CREATE INDEX idx_credit_audit_log_operation_type ON credit_audit_log (operation_type);

COMMENT ON TABLE credit_audit_log IS '征信审计日志表';
COMMENT ON COLUMN credit_audit_log.id IS '日志编号';
COMMENT ON COLUMN credit_audit_log.biz_type IS '业务类型（FORM_DATA-表单数据，SUMMARY_REPORT-汇总报表）';
COMMENT ON COLUMN credit_audit_log.biz_id IS '业务编号';
COMMENT ON COLUMN credit_audit_log.operation_type IS '操作类型（CREATE-创建，UPDATE-更新，SUBMIT-提交，CALCULATE-计算）';
COMMENT ON COLUMN credit_audit_log.operation_user_id IS '操作用户编号';
COMMENT ON COLUMN credit_audit_log.operation_desc IS '操作描述';
COMMENT ON COLUMN credit_audit_log.before_data IS '变更前数据（JSONB格式）';
COMMENT ON COLUMN credit_audit_log.after_data IS '变更后数据（JSONB格式）';
COMMENT ON COLUMN credit_audit_log.change_fields IS '变更字段列表（逗号分隔）';
COMMENT ON COLUMN credit_audit_log.ip_address IS 'IP地址';
COMMENT ON COLUMN credit_audit_log.user_agent IS '用户代理';
COMMENT ON COLUMN credit_audit_log.create_time IS '创建时间';
COMMENT ON COLUMN credit_audit_log.tenant_id IS '租户编号';

DROP SEQUENCE IF EXISTS credit_audit_log_seq;
CREATE SEQUENCE credit_audit_log_seq START 1;

-- ----------------------------
-- Table structure for credit_validation_rule
-- 征信校验规则配置表
-- ----------------------------
DROP TABLE IF EXISTS credit_validation_rule;
CREATE TABLE credit_validation_rule (
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

ALTER TABLE credit_validation_rule ADD CONSTRAINT pk_credit_validation_rule PRIMARY KEY (id);

CREATE INDEX idx_credit_validation_rule_field_code ON credit_validation_rule (field_code) WHERE deleted = 0 AND status = 1;
CREATE INDEX idx_credit_validation_rule_priority ON credit_validation_rule (priority) WHERE deleted = 0 AND status = 1;

COMMENT ON TABLE credit_validation_rule IS '征信校验规则配置表';
COMMENT ON COLUMN credit_validation_rule.id IS '规则编号';
COMMENT ON COLUMN credit_validation_rule.field_code IS '字段编码';
COMMENT ON COLUMN credit_validation_rule.rule_type IS '规则类型（REQUIRED-必填，RANGE-范围，REGEX-正则，CUSTOM-自定义）';
COMMENT ON COLUMN credit_validation_rule.rule_expression IS '校验表达式';
COMMENT ON COLUMN credit_validation_rule.error_message IS '错误提示信息';
COMMENT ON COLUMN credit_validation_rule.priority IS '优先级';
COMMENT ON COLUMN credit_validation_rule.status IS '状态（0-禁用，1-启用）';
COMMENT ON COLUMN credit_validation_rule.creator IS '创建者';
COMMENT ON COLUMN credit_validation_rule.create_time IS '创建时间';
COMMENT ON COLUMN credit_validation_rule.updater IS '更新者';
COMMENT ON COLUMN credit_validation_rule.update_time IS '更新时间';
COMMENT ON COLUMN credit_validation_rule.deleted IS '是否删除';
COMMENT ON COLUMN credit_validation_rule.tenant_id IS '租户编号';

DROP SEQUENCE IF EXISTS credit_validation_rule_seq;
CREATE SEQUENCE credit_validation_rule_seq START 1;

-- ----------------------------
-- 初始化数据示例（可选）
-- ----------------------------

-- 示例：通用字段配置（dept_id = 0）
-- @formatter:off
BEGIN;
-- 这里可以插入一些初始化的通用字段配置
-- INSERT INTO credit_field_config (id, dept_id, field_code, field_name, field_type, required, display_order, status, creator, create_time, updater, update_time, deleted, tenant_id)
-- VALUES (1, 0, 'assets', '资产', 'DECIMAL', 1, 1, 1, 'system', CURRENT_TIMESTAMP, 'system', CURRENT_TIMESTAMP, 0, 0);
COMMIT;
-- @formatter:on
