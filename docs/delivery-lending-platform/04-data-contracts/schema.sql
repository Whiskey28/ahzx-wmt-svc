/*
  Lending Digital Platform — Core Schema (PostgreSQL v15)

  - Fits WMT existing Postgres style: id int8 + sequence, audit columns, tenant_id, deleted (0/1)
  - Designed for: /app-api (MEMBER) + /admin-api (ADMIN) + postloan subdomain
  - Note: infra/system tables (system_users, system_oauth2_access_token, infra_file, etc.) already exist in base schema.
*/

-- ============================
-- 0. Conventions / helpers
-- ============================

-- Keep everything in public; use table name prefix fin_
-- Soft delete: deleted int2 NOT NULL DEFAULT 0 (0=false, 1=true)
-- Multi-tenant: tenant_id int8 NOT NULL DEFAULT 0
-- Audit columns align with BaseDO/TenantBaseDO

-- ============================
-- 1. Customer & identity (app)
-- ============================

DROP TABLE IF EXISTS fin_customer_wechat_bind;
DROP TABLE IF EXISTS fin_customer_profile;
DROP TABLE IF EXISTS fin_customer;

CREATE TABLE fin_customer (
  id int8 NOT NULL,
  -- link to system OAuth2 tokens by user_id (UserTypeEnum.MEMBER=1). Not FK to keep decoupled.
  user_id int8 NULL DEFAULT NULL,

  -- sensitive fields: store encrypted and/or masked form; do not store plaintext.
  mobile_enc varchar(256) NULL DEFAULT NULL,
  mobile_masked varchar(32) NULL DEFAULT NULL,
  id_no_enc varchar(512) NULL DEFAULT NULL,
  id_no_masked varchar(64) NULL DEFAULT NULL,
  real_name_enc varchar(256) NULL DEFAULT NULL,
  real_name_masked varchar(64) NULL DEFAULT NULL,

  -- status
  status int2 NOT NULL DEFAULT 0, -- 0=normal, 1=disabled

  creator varchar(64) NULL DEFAULT '',
  create_time timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updater varchar(64) NULL DEFAULT '',
  update_time timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  deleted int2 NOT NULL DEFAULT 0,
  tenant_id int8 NOT NULL DEFAULT 0
);

ALTER TABLE fin_customer ADD CONSTRAINT pk_fin_customer PRIMARY KEY (id);

CREATE INDEX idx_fin_customer_user_id ON fin_customer (user_id);
CREATE INDEX idx_fin_customer_create_time ON fin_customer (create_time);

COMMENT ON TABLE fin_customer IS '客户表（C端会员，敏感字段加密/脱敏存储）';
COMMENT ON COLUMN fin_customer.user_id IS 'system_oauth2_access_token.user_id（UserType=MEMBER），可为空（未绑定）';
COMMENT ON COLUMN fin_customer.mobile_enc IS '手机号密文（可用于精确匹配需配合加密策略）';
COMMENT ON COLUMN fin_customer.mobile_masked IS '手机号脱敏展示';
COMMENT ON COLUMN fin_customer.id_no_enc IS '证件号密文';
COMMENT ON COLUMN fin_customer.id_no_masked IS '证件号脱敏展示';
COMMENT ON COLUMN fin_customer.real_name_enc IS '姓名密文';
COMMENT ON COLUMN fin_customer.real_name_masked IS '姓名脱敏展示';
COMMENT ON COLUMN fin_customer.status IS '状态（0正常 1停用）';

DROP SEQUENCE IF EXISTS fin_customer_seq;
CREATE SEQUENCE fin_customer_seq START 1;

CREATE TABLE fin_customer_profile (
  id int8 NOT NULL,
  customer_id int8 NOT NULL,
  birthday date NULL DEFAULT NULL,
  sex int2 NULL DEFAULT 0, -- align SexEnum if needed
  address_enc varchar(1024) NULL DEFAULT NULL,
  address_masked varchar(256) NULL DEFAULT NULL,
  ext_json varchar(4000) NULL DEFAULT NULL,

  creator varchar(64) NULL DEFAULT '',
  create_time timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updater varchar(64) NULL DEFAULT '',
  update_time timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  deleted int2 NOT NULL DEFAULT 0,
  tenant_id int8 NOT NULL DEFAULT 0
);

ALTER TABLE fin_customer_profile ADD CONSTRAINT pk_fin_customer_profile PRIMARY KEY (id);
ALTER TABLE fin_customer_profile ADD CONSTRAINT fk_fin_customer_profile_customer
  FOREIGN KEY (customer_id) REFERENCES fin_customer (id);
CREATE UNIQUE INDEX uk_fin_customer_profile_customer_id ON fin_customer_profile (customer_id);

COMMENT ON TABLE fin_customer_profile IS '客户扩展资料（敏感字段加密/脱敏）';

DROP SEQUENCE IF EXISTS fin_customer_profile_seq;
CREATE SEQUENCE fin_customer_profile_seq START 1;

CREATE TABLE fin_customer_wechat_bind (
  id int8 NOT NULL,
  customer_id int8 NOT NULL,
  app_id varchar(64) NOT NULL DEFAULT '',
  open_id varchar(64) NOT NULL DEFAULT '',
  union_id varchar(64) NULL DEFAULT NULL,
  bind_time timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,

  creator varchar(64) NULL DEFAULT '',
  create_time timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updater varchar(64) NULL DEFAULT '',
  update_time timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  deleted int2 NOT NULL DEFAULT 0,
  tenant_id int8 NOT NULL DEFAULT 0
);

ALTER TABLE fin_customer_wechat_bind ADD CONSTRAINT pk_fin_customer_wechat_bind PRIMARY KEY (id);
ALTER TABLE fin_customer_wechat_bind ADD CONSTRAINT fk_fin_customer_wechat_bind_customer
  FOREIGN KEY (customer_id) REFERENCES fin_customer (id);

CREATE UNIQUE INDEX uk_fin_customer_wechat_bind_app_open ON fin_customer_wechat_bind (app_id, open_id);
CREATE INDEX idx_fin_customer_wechat_bind_customer_id ON fin_customer_wechat_bind (customer_id);

COMMENT ON TABLE fin_customer_wechat_bind IS '客户-微信小程序绑定';

DROP SEQUENCE IF EXISTS fin_customer_wechat_bind_seq;
CREATE SEQUENCE fin_customer_wechat_bind_seq START 1;

-- ============================
-- 2. Consent / authorization & materials
-- ============================

DROP TABLE IF EXISTS fin_auth_consent;
CREATE TABLE fin_auth_consent (
  id int8 NOT NULL,
  customer_id int8 NOT NULL,
  consent_type varchar(64) NOT NULL, -- e.g. identity, credit, contact, file
  consent_version varchar(32) NOT NULL DEFAULT '',
  consented bool NOT NULL DEFAULT '1',
  consent_time timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  terminal int4 NOT NULL DEFAULT 0, -- TerminalEnum
  ip varchar(64) NULL DEFAULT '',
  user_agent varchar(512) NULL DEFAULT '',
  extra_json varchar(4000) NULL DEFAULT NULL,

  creator varchar(64) NULL DEFAULT '',
  create_time timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updater varchar(64) NULL DEFAULT '',
  update_time timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  deleted int2 NOT NULL DEFAULT 0,
  tenant_id int8 NOT NULL DEFAULT 0
);
ALTER TABLE fin_auth_consent ADD CONSTRAINT pk_fin_auth_consent PRIMARY KEY (id);
ALTER TABLE fin_auth_consent ADD CONSTRAINT fk_fin_auth_consent_customer
  FOREIGN KEY (customer_id) REFERENCES fin_customer (id);

CREATE INDEX idx_fin_auth_consent_customer_time ON fin_auth_consent (customer_id, consent_time);
CREATE INDEX idx_fin_auth_consent_type ON fin_auth_consent (consent_type);

COMMENT ON TABLE fin_auth_consent IS '授权与合规留痕（授权类型/版本/终端/IP/UA）';
DROP SEQUENCE IF EXISTS fin_auth_consent_seq;
CREATE SEQUENCE fin_auth_consent_seq START 1;

-- materials reference infra_file.id (already exists)
DROP TABLE IF EXISTS fin_application_material;
CREATE TABLE fin_application_material (
  id int8 NOT NULL,
  application_id int8 NOT NULL,
  material_type varchar(64) NOT NULL, -- id_card_front, bank_statement, etc.
  file_id int8 NOT NULL, -- infra_file.id
  file_name varchar(256) NULL DEFAULT NULL,
  file_url varchar(1024) NULL DEFAULT NULL, -- optional snapshot, prefer signed url on read
  status int2 NOT NULL DEFAULT 0, -- 0=uploaded,1=verified,2=rejected
  remark varchar(512) NULL DEFAULT NULL,

  creator varchar(64) NULL DEFAULT '',
  create_time timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updater varchar(64) NULL DEFAULT '',
  update_time timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  deleted int2 NOT NULL DEFAULT 0,
  tenant_id int8 NOT NULL DEFAULT 0
);
ALTER TABLE fin_application_material ADD CONSTRAINT pk_fin_application_material PRIMARY KEY (id);
CREATE INDEX idx_fin_application_material_app ON fin_application_material (application_id);
CREATE INDEX idx_fin_application_material_file ON fin_application_material (file_id);
COMMENT ON TABLE fin_application_material IS '申请材料（引用 infra_file）';
DROP SEQUENCE IF EXISTS fin_application_material_seq;
CREATE SEQUENCE fin_application_material_seq START 1;

-- ============================
-- 3. Loan application (case)
-- ============================

DROP TABLE IF EXISTS fin_loan_application_timeline;
DROP TABLE IF EXISTS fin_credit_assessment;
DROP TABLE IF EXISTS fin_loan_application;

CREATE TABLE fin_loan_application (
  id int8 NOT NULL,
  application_no varchar(32) NOT NULL, -- business friendly number
  customer_id int8 NOT NULL,
  channel varchar(32) NOT NULL DEFAULT 'WECHAT_MINI_PROGRAM',
  terminal int4 NOT NULL DEFAULT 0, -- TerminalEnum

  -- requested / calculated fields
  product_code varchar(64) NULL DEFAULT NULL,
  amount_requested numeric(18, 2) NULL DEFAULT NULL,
  term_months int4 NULL DEFAULT NULL,

  -- state machine
  status int2 NOT NULL DEFAULT 0,
  -- 0=draft,10=submitted,20=material_pending,30=reviewing,40=approved,50=rejected,60=withdrawn

  -- admin ownership (system_users.id)
  owner_user_id int8 NULL DEFAULT NULL,
  owner_dept_id int8 NULL DEFAULT NULL,

  -- decision summary
  approved_amount numeric(18, 2) NULL DEFAULT NULL,
  approved_term_months int4 NULL DEFAULT NULL,
  decision_reason varchar(512) NULL DEFAULT NULL,

  ext_json varchar(4000) NULL DEFAULT NULL,

  creator varchar(64) NULL DEFAULT '',
  create_time timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updater varchar(64) NULL DEFAULT '',
  update_time timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  deleted int2 NOT NULL DEFAULT 0,
  tenant_id int8 NOT NULL DEFAULT 0
);

ALTER TABLE fin_loan_application ADD CONSTRAINT pk_fin_loan_application PRIMARY KEY (id);
ALTER TABLE fin_loan_application ADD CONSTRAINT fk_fin_loan_application_customer
  FOREIGN KEY (customer_id) REFERENCES fin_customer (id);

CREATE UNIQUE INDEX uk_fin_loan_application_no ON fin_loan_application (application_no);
CREATE INDEX idx_fin_loan_application_customer ON fin_loan_application (customer_id, create_time);
CREATE INDEX idx_fin_loan_application_status ON fin_loan_application (status, update_time);
CREATE INDEX idx_fin_loan_application_owner ON fin_loan_application (owner_user_id);

COMMENT ON TABLE fin_loan_application IS '贷款申请/进件（案件主表）';
COMMENT ON COLUMN fin_loan_application.status IS '状态：0草稿 10已提交 20待补件 30审批中 40通过 50拒绝 60撤回';

DROP SEQUENCE IF EXISTS fin_loan_application_seq;
CREATE SEQUENCE fin_loan_application_seq START 1;

-- credit assessment (CAP-C-002)
CREATE TABLE fin_credit_assessment (
  id int8 NOT NULL,
  application_id int8 NOT NULL,
  assessment_type varchar(32) NOT NULL DEFAULT 'PRE', -- PRE/FINAL
  result_code varchar(32) NOT NULL DEFAULT '',
  result_msg varchar(512) NULL DEFAULT NULL,
  score numeric(10, 2) NULL DEFAULT NULL,
  quota_suggested numeric(18, 2) NULL DEFAULT NULL,
  extra_json varchar(4000) NULL DEFAULT NULL,
  assessed_time timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,

  creator varchar(64) NULL DEFAULT '',
  create_time timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updater varchar(64) NULL DEFAULT '',
  update_time timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  deleted int2 NOT NULL DEFAULT 0,
  tenant_id int8 NOT NULL DEFAULT 0
);
ALTER TABLE fin_credit_assessment ADD CONSTRAINT pk_fin_credit_assessment PRIMARY KEY (id);
ALTER TABLE fin_credit_assessment ADD CONSTRAINT fk_fin_credit_assessment_application
  FOREIGN KEY (application_id) REFERENCES fin_loan_application (id);
CREATE INDEX idx_fin_credit_assessment_app_time ON fin_credit_assessment (application_id, assessed_time);
COMMENT ON TABLE fin_credit_assessment IS '额度测算/风控评估结果（可对接外部策略/风控系统）';
DROP SEQUENCE IF EXISTS fin_credit_assessment_seq;
CREATE SEQUENCE fin_credit_assessment_seq START 1;

-- timeline for app status query (CAP-C-006)
CREATE TABLE fin_loan_application_timeline (
  id int8 NOT NULL,
  application_id int8 NOT NULL,
  event_type varchar(64) NOT NULL, -- SUBMITTED, MATERIAL_UPLOADED, NEED_MORE, APPROVED, REJECTED, etc.
  event_title varchar(128) NOT NULL DEFAULT '',
  event_detail varchar(1024) NULL DEFAULT NULL,
  operator_user_id int8 NULL DEFAULT NULL, -- admin user or null for system
  event_time timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,

  creator varchar(64) NULL DEFAULT '',
  create_time timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updater varchar(64) NULL DEFAULT '',
  update_time timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  deleted int2 NOT NULL DEFAULT 0,
  tenant_id int8 NOT NULL DEFAULT 0
);
ALTER TABLE fin_loan_application_timeline ADD CONSTRAINT pk_fin_loan_application_timeline PRIMARY KEY (id);
ALTER TABLE fin_loan_application_timeline ADD CONSTRAINT fk_fin_loan_application_timeline_application
  FOREIGN KEY (application_id) REFERENCES fin_loan_application (id);
CREATE INDEX idx_fin_loan_application_timeline_app_time ON fin_loan_application_timeline (application_id, event_time);
COMMENT ON TABLE fin_loan_application_timeline IS '申请进度时间线（C端查询）';
DROP SEQUENCE IF EXISTS fin_loan_application_timeline_seq;
CREATE SEQUENCE fin_loan_application_timeline_seq START 1;

-- now that fin_loan_application exists, add FK on materials table
ALTER TABLE fin_application_material
  ADD CONSTRAINT fk_fin_application_material_application
  FOREIGN KEY (application_id) REFERENCES fin_loan_application (id);

-- ============================
-- 4. Loan account (post-approval) & post-loan
-- ============================

DROP TABLE IF EXISTS fin_loan_account;
CREATE TABLE fin_loan_account (
  id int8 NOT NULL,
  loan_no varchar(32) NOT NULL,
  application_id int8 NOT NULL,
  customer_id int8 NOT NULL,
  principal numeric(18, 2) NOT NULL,
  term_months int4 NOT NULL,
  rate_annual numeric(10, 6) NULL DEFAULT NULL,
  disburse_time timestamp NULL DEFAULT NULL,
  status int2 NOT NULL DEFAULT 0, -- 0=active, 1=closed, 2=overdue
  extra_json varchar(4000) NULL DEFAULT NULL,

  creator varchar(64) NULL DEFAULT '',
  create_time timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updater varchar(64) NULL DEFAULT '',
  update_time timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  deleted int2 NOT NULL DEFAULT 0,
  tenant_id int8 NOT NULL DEFAULT 0
);
ALTER TABLE fin_loan_account ADD CONSTRAINT pk_fin_loan_account PRIMARY KEY (id);
ALTER TABLE fin_loan_account ADD CONSTRAINT fk_fin_loan_account_application
  FOREIGN KEY (application_id) REFERENCES fin_loan_application (id);
ALTER TABLE fin_loan_account ADD CONSTRAINT fk_fin_loan_account_customer
  FOREIGN KEY (customer_id) REFERENCES fin_customer (id);
CREATE UNIQUE INDEX uk_fin_loan_account_loan_no ON fin_loan_account (loan_no);
CREATE INDEX idx_fin_loan_account_customer ON fin_loan_account (customer_id);
COMMENT ON TABLE fin_loan_account IS '借据/贷款账户（贷后主数据最小版）';
DROP SEQUENCE IF EXISTS fin_loan_account_seq;
CREATE SEQUENCE fin_loan_account_seq START 1;

DROP TABLE IF EXISTS fin_postloan_monitor_task;
CREATE TABLE fin_postloan_monitor_task (
  id int8 NOT NULL,
  task_code varchar(64) NOT NULL,
  task_name varchar(128) NOT NULL,
  enabled bool NOT NULL DEFAULT '1',
  cron varchar(64) NULL DEFAULT NULL,
  last_run_time timestamp NULL DEFAULT NULL,
  last_run_status int2 NULL DEFAULT 0, -- 0=success,1=fail
  extra_json varchar(4000) NULL DEFAULT NULL,

  creator varchar(64) NULL DEFAULT '',
  create_time timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updater varchar(64) NULL DEFAULT '',
  update_time timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  deleted int2 NOT NULL DEFAULT 0,
  tenant_id int8 NOT NULL DEFAULT 0
);
ALTER TABLE fin_postloan_monitor_task ADD CONSTRAINT pk_fin_postloan_monitor_task PRIMARY KEY (id);
CREATE UNIQUE INDEX uk_fin_postloan_monitor_task_code ON fin_postloan_monitor_task (task_code);
COMMENT ON TABLE fin_postloan_monitor_task IS '贷后监测任务（定时跑批配置）';
DROP SEQUENCE IF EXISTS fin_postloan_monitor_task_seq;
CREATE SEQUENCE fin_postloan_monitor_task_seq START 1;

DROP TABLE IF EXISTS fin_postloan_alert;
CREATE TABLE fin_postloan_alert (
  id int8 NOT NULL,
  alert_no varchar(32) NOT NULL,
  customer_id int8 NOT NULL,
  loan_account_id int8 NULL DEFAULT NULL,
  alert_level int2 NOT NULL DEFAULT 1, -- 1/2/3
  alert_type varchar(64) NOT NULL, -- overdue, abnormal_txn, etc.
  title varchar(128) NOT NULL DEFAULT '',
  detail varchar(1024) NULL DEFAULT NULL,
  status int2 NOT NULL DEFAULT 0, -- 0=open,1=processing,2=closed
  occurred_time timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,

  owner_user_id int8 NULL DEFAULT NULL,

  creator varchar(64) NULL DEFAULT '',
  create_time timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updater varchar(64) NULL DEFAULT '',
  update_time timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  deleted int2 NOT NULL DEFAULT 0,
  tenant_id int8 NOT NULL DEFAULT 0
);
ALTER TABLE fin_postloan_alert ADD CONSTRAINT pk_fin_postloan_alert PRIMARY KEY (id);
ALTER TABLE fin_postloan_alert ADD CONSTRAINT fk_fin_postloan_alert_customer
  FOREIGN KEY (customer_id) REFERENCES fin_customer (id);
ALTER TABLE fin_postloan_alert ADD CONSTRAINT fk_fin_postloan_alert_loan_account
  FOREIGN KEY (loan_account_id) REFERENCES fin_loan_account (id);
CREATE UNIQUE INDEX uk_fin_postloan_alert_no ON fin_postloan_alert (alert_no);
CREATE INDEX idx_fin_postloan_alert_status_time ON fin_postloan_alert (status, occurred_time);
CREATE INDEX idx_fin_postloan_alert_owner ON fin_postloan_alert (owner_user_id);
COMMENT ON TABLE fin_postloan_alert IS '贷后风险预警事件';
DROP SEQUENCE IF EXISTS fin_postloan_alert_seq;
CREATE SEQUENCE fin_postloan_alert_seq START 1;

-- ============================
-- 5. Collections (case/action)
-- ============================

DROP TABLE IF EXISTS fin_collection_action;
DROP TABLE IF EXISTS fin_collection_case;

CREATE TABLE fin_collection_case (
  id int8 NOT NULL,
  case_no varchar(32) NOT NULL,
  customer_id int8 NOT NULL,
  loan_account_id int8 NOT NULL,
  source_alert_id int8 NULL DEFAULT NULL, -- fin_postloan_alert.id
  status int2 NOT NULL DEFAULT 0, -- 0=open,1=processing,2=closed
  priority int2 NOT NULL DEFAULT 3, -- 1=high..5=low
  owner_user_id int8 NULL DEFAULT NULL,
  open_time timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  close_time timestamp NULL DEFAULT NULL,

  creator varchar(64) NULL DEFAULT '',
  create_time timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updater varchar(64) NULL DEFAULT '',
  update_time timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  deleted int2 NOT NULL DEFAULT 0,
  tenant_id int8 NOT NULL DEFAULT 0
);

ALTER TABLE fin_collection_case ADD CONSTRAINT pk_fin_collection_case PRIMARY KEY (id);
ALTER TABLE fin_collection_case ADD CONSTRAINT fk_fin_collection_case_customer
  FOREIGN KEY (customer_id) REFERENCES fin_customer (id);
ALTER TABLE fin_collection_case ADD CONSTRAINT fk_fin_collection_case_loan_account
  FOREIGN KEY (loan_account_id) REFERENCES fin_loan_account (id);
ALTER TABLE fin_collection_case ADD CONSTRAINT fk_fin_collection_case_source_alert
  FOREIGN KEY (source_alert_id) REFERENCES fin_postloan_alert (id);

CREATE UNIQUE INDEX uk_fin_collection_case_no ON fin_collection_case (case_no);
CREATE INDEX idx_fin_collection_case_status ON fin_collection_case (status, update_time);
CREATE INDEX idx_fin_collection_case_owner ON fin_collection_case (owner_user_id);

COMMENT ON TABLE fin_collection_case IS '催收案件/工单';
DROP SEQUENCE IF EXISTS fin_collection_case_seq;
CREATE SEQUENCE fin_collection_case_seq START 1;

CREATE TABLE fin_collection_action (
  id int8 NOT NULL,
  case_id int8 NOT NULL,
  action_type varchar(64) NOT NULL, -- CALL/SMS/VISIT/OTHER
  action_result varchar(64) NULL DEFAULT NULL,
  note varchar(1024) NULL DEFAULT NULL,
  action_time timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  operator_user_id int8 NULL DEFAULT NULL,

  creator varchar(64) NULL DEFAULT '',
  create_time timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updater varchar(64) NULL DEFAULT '',
  update_time timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  deleted int2 NOT NULL DEFAULT 0,
  tenant_id int8 NOT NULL DEFAULT 0
);

ALTER TABLE fin_collection_action ADD CONSTRAINT pk_fin_collection_action PRIMARY KEY (id);
ALTER TABLE fin_collection_action ADD CONSTRAINT fk_fin_collection_action_case
  FOREIGN KEY (case_id) REFERENCES fin_collection_case (id);

CREATE INDEX idx_fin_collection_action_case_time ON fin_collection_action (case_id, action_time);
COMMENT ON TABLE fin_collection_action IS '催收动作留痕（强审计）';
DROP SEQUENCE IF EXISTS fin_collection_action_seq;
CREATE SEQUENCE fin_collection_action_seq START 1;

-- ============================
-- 6. Regulatory reporting (phase)
-- ============================

DROP TABLE IF EXISTS fin_reg_report_task;
CREATE TABLE fin_reg_report_task (
  id int8 NOT NULL,
  batch_no varchar(64) NOT NULL,
  report_type varchar(64) NOT NULL,
  status int2 NOT NULL DEFAULT 0, -- 0=pending,1=running,2=success,3=fail
  file_id int8 NULL DEFAULT NULL, -- infra_file.id or exported file reference
  error_summary varchar(1024) NULL DEFAULT NULL,
  start_time timestamp NULL DEFAULT NULL,
  end_time timestamp NULL DEFAULT NULL,

  creator varchar(64) NULL DEFAULT '',
  create_time timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updater varchar(64) NULL DEFAULT '',
  update_time timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  deleted int2 NOT NULL DEFAULT 0,
  tenant_id int8 NOT NULL DEFAULT 0
);

ALTER TABLE fin_reg_report_task ADD CONSTRAINT pk_fin_reg_report_task PRIMARY KEY (id);
CREATE UNIQUE INDEX uk_fin_reg_report_task_batch ON fin_reg_report_task (batch_no);
CREATE INDEX idx_fin_reg_report_task_status_time ON fin_reg_report_task (status, create_time);
COMMENT ON TABLE fin_reg_report_task IS '监管报送任务（分期实现）';
DROP SEQUENCE IF EXISTS fin_reg_report_task_seq;
CREATE SEQUENCE fin_reg_report_task_seq START 1;

