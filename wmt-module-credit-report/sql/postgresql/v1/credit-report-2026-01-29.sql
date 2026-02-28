-- 信用报送（积木表单落库：A 方案）- PostgreSQL v15 - 2026-01-29
-- 说明：
-- 1) report_fill_basic_info 作为"填报主记录（record）锚点"，唯一约束：role_id + period_id + report_id
-- 2) 企业基础信息等放入 1:1 块表 report_fill_enterprise_basic（主键与主记录 id 一致）
-- 3) 明细表统一使用 BaseDO 的审计/逻辑删除字段（create_time/update_time/creator/updater/deleted）

-- ============================
-- report_fill_basic_info（主记录锚点）
-- ============================
DROP SEQUENCE IF EXISTS report_fill_basic_info_seq;
CREATE SEQUENCE report_fill_basic_info_seq START 1;

CREATE TABLE IF NOT EXISTS report_fill_basic_info (
  id          BIGINT NOT NULL DEFAULT nextval('report_fill_basic_info_seq'),
  period_id   VARCHAR(16) NOT NULL,
  role_id     VARCHAR(64) NOT NULL,
  project_id  VARCHAR(64),
  report_id   VARCHAR(64) NOT NULL,
  create_time TIMESTAMP,
  update_time TIMESTAMP,
  creator     VARCHAR(64),
  updater     VARCHAR(64),
  deleted     int2 NOT NULL DEFAULT 0
);

CREATE UNIQUE INDEX IF NOT EXISTS uk_report_fill_basic_info_role_period_report
  ON report_fill_basic_info (role_id, period_id, report_id);

COMMENT ON TABLE report_fill_basic_info IS '报表填报主记录（锚点）';
COMMENT ON COLUMN report_fill_basic_info.id IS '主键（record_id）';
COMMENT ON COLUMN report_fill_basic_info.period_id IS '填报周期（如 2025-01 / 2025-Q1）';
COMMENT ON COLUMN report_fill_basic_info.role_id IS '角色id';
COMMENT ON COLUMN report_fill_basic_info.project_id IS '项目id（可选，不参与唯一约束）';
COMMENT ON COLUMN report_fill_basic_info.report_id IS '报表模板id（jimu_report.id）';
COMMENT ON COLUMN report_fill_basic_info.create_time IS '创建时间';
COMMENT ON COLUMN report_fill_basic_info.update_time IS '更新时间';
COMMENT ON COLUMN report_fill_basic_info.creator IS '创建者';
COMMENT ON COLUMN report_fill_basic_info.updater IS '更新者';
COMMENT ON COLUMN report_fill_basic_info.deleted IS '是否删除';

ALTER TABLE report_fill_basic_info ADD CONSTRAINT pk_report_fill_basic_info PRIMARY KEY (id);

-- ============================
-- report_fill_enterprise_basic（企业基础信息块 1:1）
-- ============================
DROP SEQUENCE IF EXISTS report_fill_enterprise_basic_seq;
CREATE SEQUENCE report_fill_enterprise_basic_seq START 1;

CREATE TABLE IF NOT EXISTS report_fill_enterprise_basic (
  id                            BIGINT NOT NULL DEFAULT nextval('report_fill_enterprise_basic_seq'),
  enterprise_name               VARCHAR(256),
  credit_code                   VARCHAR(64),
  address                       VARCHAR(512),
  legal_person                  VARCHAR(128),
  register_date                 VARCHAR(32),
  biz_place                     VARCHAR(512),
  record_no                     VARCHAR(128),
  record_date                   VARCHAR(32),
  record_branch                 VARCHAR(256),
  reg_cap_million               NUMERIC(18, 2),
  paid_in_cap_million           NUMERIC(18, 2),
  isms_level                    VARCHAR(64),
  service_network_type          VARCHAR(64),
  branch_count                  NUMERIC(18, 0),
  controlling_shareholder       VARCHAR(256),
  controlling_shareholder_ratio VARCHAR(32),
  controlling_shareholder_type  VARCHAR(64),
  contact_name                  VARCHAR(128),
  contact_phone                 VARCHAR(64),
  contact_email                 VARCHAR(128),
  create_time                   TIMESTAMP,
  update_time                   TIMESTAMP,
  creator                       VARCHAR(64),
  updater                       VARCHAR(64),
  deleted                       int2 NOT NULL DEFAULT 0
);

COMMENT ON TABLE report_fill_enterprise_basic IS '报表填报-企业基础信息块（1:1）';
COMMENT ON COLUMN report_fill_enterprise_basic.id IS '主键（= report_fill_basic_info.id）';

ALTER TABLE report_fill_enterprise_basic ADD CONSTRAINT pk_report_fill_enterprise_basic PRIMARY KEY (id);

-- ============================
-- report_fill_info_user_org_item（信息使用者机构明细 1:N）
-- ============================
DROP SEQUENCE IF EXISTS report_fill_info_user_org_item_seq;
CREATE SEQUENCE report_fill_info_user_org_item_seq START 1;

CREATE TABLE IF NOT EXISTS report_fill_info_user_org_item (
  id                 BIGINT NOT NULL DEFAULT nextval('report_fill_info_user_org_item_seq'),
  record_id           BIGINT NOT NULL,
  org_name           VARCHAR(256) NOT NULL,
  industry_code      VARCHAR(64),
  is_current_service int2 NOT NULL DEFAULT 0,
  sort_no            VARCHAR(32),
  create_time        TIMESTAMP,
  update_time        TIMESTAMP,
  creator            VARCHAR(64),
  updater            VARCHAR(64),
  deleted            int2 NOT NULL DEFAULT 0
);

CREATE INDEX IF NOT EXISTS idx_report_fill_info_user_org_item_record_id
  ON report_fill_info_user_org_item (record_id);

COMMENT ON TABLE report_fill_info_user_org_item IS '报表填报-信息使用者机构明细';
COMMENT ON COLUMN report_fill_info_user_org_item.record_id IS '主记录id';
COMMENT ON COLUMN report_fill_info_user_org_item.industry_code IS '行业分类（字典，可扩展）';

ALTER TABLE report_fill_info_user_org_item ADD CONSTRAINT pk_report_fill_info_user_org_item PRIMARY KEY (id);

-- ============================
-- report_fill_info_user_gov_item（信息使用者机构+政府 明细 1:N）
-- ============================
DROP SEQUENCE IF EXISTS report_fill_info_user_gov_item_seq;
CREATE SEQUENCE report_fill_info_user_gov_item_seq START 1;

CREATE TABLE IF NOT EXISTS report_fill_info_user_gov_item (
  id                 BIGINT NOT NULL DEFAULT nextval('report_fill_info_user_gov_item_seq'),
  record_id           BIGINT NOT NULL,
  gov_org_name       VARCHAR(256) NOT NULL,
  is_current_service int2 NOT NULL DEFAULT 0,
  sort_no            VARCHAR(32),
  create_time        TIMESTAMP,
  update_time        TIMESTAMP,
  creator            VARCHAR(64),
  updater            VARCHAR(64),
  deleted            int2 NOT NULL DEFAULT 0
);

CREATE INDEX IF NOT EXISTS idx_report_fill_info_user_gov_item_record_id
  ON report_fill_info_user_gov_item (record_id);

COMMENT ON TABLE report_fill_info_user_gov_item IS '报表填报-信息使用者机构+政府明细';
COMMENT ON COLUMN report_fill_info_user_gov_item.record_id IS '主记录id';

ALTER TABLE report_fill_info_user_gov_item ADD CONSTRAINT pk_report_fill_info_user_gov_item PRIMARY KEY (id);

-- ============================
-- report_fill_biz_stat_finance（经营情况-财务口径 1:1）
-- ============================
DROP SEQUENCE IF EXISTS report_fill_biz_stat_finance_seq;
CREATE SEQUENCE report_fill_biz_stat_finance_seq START 1;

CREATE TABLE IF NOT EXISTS report_fill_biz_stat_finance (
  id                      BIGINT NOT NULL DEFAULT nextval('report_fill_biz_stat_finance_seq'),
  asset_amount            NUMERIC(18, 2),
  liability_amount        NUMERIC(18, 2),
  year_income_amount      NUMERIC(18, 2),
  year_credit_income_amount NUMERIC(18, 2),
  year_net_profit_amount  NUMERIC(18, 2),
  create_time             TIMESTAMP,
  update_time             TIMESTAMP,
  creator                 VARCHAR(64),
  updater                 VARCHAR(64),
  deleted                 int2 NOT NULL DEFAULT 0
);

COMMENT ON TABLE report_fill_biz_stat_finance IS '报表填报-经营情况（财务口径，1:1）';
COMMENT ON COLUMN report_fill_biz_stat_finance.id IS '主键（= report_fill_basic_info.id）';

ALTER TABLE report_fill_biz_stat_finance ADD CONSTRAINT pk_report_fill_biz_stat_finance PRIMARY KEY (id);

-- ============================
-- report_fill_biz_stat_hr（经营情况-人员口径 1:1）
-- ============================
DROP SEQUENCE IF EXISTS report_fill_biz_stat_hr_seq;
CREATE SEQUENCE report_fill_biz_stat_hr_seq START 1;

CREATE TABLE IF NOT EXISTS report_fill_biz_stat_hr (
  id                 BIGINT NOT NULL DEFAULT nextval('report_fill_biz_stat_hr_seq'),
  employee_total     NUMERIC(18, 0),
  employee_credit_biz NUMERIC(18, 0),
  create_time        TIMESTAMP,
  update_time        TIMESTAMP,
  creator            VARCHAR(64),
  updater            VARCHAR(64),
  deleted            int2 NOT NULL DEFAULT 0
);

COMMENT ON TABLE report_fill_biz_stat_hr IS '报表填报-经营情况（人员口径，1:1）';
COMMENT ON COLUMN report_fill_biz_stat_hr.id IS '主键（= report_fill_basic_info.id）';

ALTER TABLE report_fill_biz_stat_hr ADD CONSTRAINT pk_report_fill_biz_stat_hr PRIMARY KEY (id);

-- ============================
-- report_fill_biz_stat_credit_build（经营情况-信用体系建设口径 1:1）
-- ============================
DROP SEQUENCE IF EXISTS report_fill_biz_stat_credit_build_seq;
CREATE SEQUENCE report_fill_biz_stat_credit_build_seq START 1;

CREATE TABLE IF NOT EXISTS report_fill_biz_stat_credit_build (
  id            BIGINT NOT NULL DEFAULT nextval('report_fill_biz_stat_credit_build_seq'),
  project_count NUMERIC(18, 0),
  income_amount NUMERIC(18, 2),
  create_time   TIMESTAMP,
  update_time   TIMESTAMP,
  creator       VARCHAR(64),
  updater       VARCHAR(64),
  deleted       int2 NOT NULL DEFAULT 0
);

COMMENT ON TABLE report_fill_biz_stat_credit_build IS '报表填报-经营情况（信用体系建设，1:1）';
COMMENT ON COLUMN report_fill_biz_stat_credit_build.id IS '主键（= report_fill_basic_info.id）';

ALTER TABLE report_fill_biz_stat_credit_build ADD CONSTRAINT pk_report_fill_biz_stat_credit_build PRIMARY KEY (id);

-- ============================
-- report_fill_product_stat（提供的征信产品/服务次数 1:N）
-- product_type：建议存字典/枚举 code（如 credit_report/credit_score/anti_fraud...）
-- ============================
DROP SEQUENCE IF EXISTS report_fill_product_stat_seq;
CREATE SEQUENCE report_fill_product_stat_seq START 1;

CREATE TABLE IF NOT EXISTS report_fill_product_stat (
  id           BIGINT NOT NULL DEFAULT nextval('report_fill_product_stat_seq'),
  record_id    BIGINT NOT NULL,
  product_type VARCHAR(64) NOT NULL,
  product_name VARCHAR(256),
  year_count   NUMERIC(18, 0),
  create_time  TIMESTAMP,
  update_time  TIMESTAMP,
  creator      VARCHAR(64),
  updater      VARCHAR(64),
  deleted      int2 NOT NULL DEFAULT 0
);

CREATE INDEX IF NOT EXISTS idx_report_fill_product_stat_record_id
  ON report_fill_product_stat (record_id);

COMMENT ON TABLE report_fill_product_stat IS '报表填报-提供的征信产品/服务次数（1:N）';
COMMENT ON COLUMN report_fill_product_stat.record_id IS '主记录id';

ALTER TABLE report_fill_product_stat ADD CONSTRAINT pk_report_fill_product_stat PRIMARY KEY (id);

-- ============================
-- report_fill_service_by_industry（按行业分类：产品与服务提供次数 1:N）
-- industry_code：行业分类字典 code（可扩展）
-- ============================
DROP SEQUENCE IF EXISTS report_fill_service_by_industry_seq;
CREATE SEQUENCE report_fill_service_by_industry_seq START 1;

CREATE TABLE IF NOT EXISTS report_fill_service_by_industry (
  id                 BIGINT NOT NULL DEFAULT nextval('report_fill_service_by_industry_seq'),
  record_id           BIGINT NOT NULL,
  industry_code      VARCHAR(64) NOT NULL,
  year_service_count NUMERIC(18, 0),
  create_time        TIMESTAMP,
  update_time        TIMESTAMP,
  creator            VARCHAR(64),
  updater            VARCHAR(64),
  deleted            int2 NOT NULL DEFAULT 0
);

CREATE INDEX IF NOT EXISTS idx_report_fill_service_by_industry_record_id
  ON report_fill_service_by_industry (record_id);

COMMENT ON TABLE report_fill_service_by_industry IS '报表填报-产品与服务提供情况（按行业分类，1:N）';
COMMENT ON COLUMN report_fill_service_by_industry.record_id IS '主记录id';

ALTER TABLE report_fill_service_by_industry ADD CONSTRAINT pk_report_fill_service_by_industry PRIMARY KEY (id);

-- ============================
-- report_fill_info_source_by_industry（信息来源情况：按行业分类 1:N）
-- provider_org_total：信息提供者机构总累计数
-- provider_org_current：当前提供服务的信息提供者机构数
-- ============================
DROP SEQUENCE IF EXISTS report_fill_info_source_by_industry_seq;
CREATE SEQUENCE report_fill_info_source_by_industry_seq START 1;

CREATE TABLE IF NOT EXISTS report_fill_info_source_by_industry (
  id                  BIGINT NOT NULL DEFAULT nextval('report_fill_info_source_by_industry_seq'),
  record_id            BIGINT NOT NULL,
  industry_code       VARCHAR(64) NOT NULL,
  provider_org_total  NUMERIC(18, 0),
  provider_org_current NUMERIC(18, 0),
  create_time         TIMESTAMP,
  update_time         TIMESTAMP,
  creator             VARCHAR(64),
  updater             VARCHAR(64),
  deleted             int2 NOT NULL DEFAULT 0
);

CREATE INDEX IF NOT EXISTS idx_report_fill_info_source_by_industry_record_id
  ON report_fill_info_source_by_industry (record_id);

COMMENT ON TABLE report_fill_info_source_by_industry IS '报表填报-信息来源情况（按行业分类，1:N）';
COMMENT ON COLUMN report_fill_info_source_by_industry.record_id IS '主记录id';

ALTER TABLE report_fill_info_source_by_industry ADD CONSTRAINT pk_report_fill_info_source_by_industry PRIMARY KEY (id);

-- ============================
-- report_fill_complaint_security_stat（异议/投诉/信息安全情况 1:1）
-- ============================
DROP SEQUENCE IF EXISTS report_fill_complaint_security_stat_seq;
CREATE SEQUENCE report_fill_complaint_security_stat_seq START 1;

CREATE TABLE IF NOT EXISTS report_fill_complaint_security_stat (
  id                        BIGINT NOT NULL DEFAULT nextval('report_fill_complaint_security_stat_seq'),
  complaint_handle_count    NUMERIC(18, 0),
  dispute_handle_count      NUMERIC(18, 0),
  dispute_confirmed_count   NUMERIC(18, 0),
  dispute_corrected_count   NUMERIC(18, 0),
  dispute_unconfirmed_count NUMERIC(18, 0),
  security_incident_count   NUMERIC(18, 0),
  leak_incident_count       NUMERIC(18, 0),
  leak_enterprise_count     NUMERIC(18, 0),
  leak_person_count         NUMERIC(18, 0),
  create_time               TIMESTAMP,
  update_time               TIMESTAMP,
  creator                   VARCHAR(64),
  updater                   VARCHAR(64),
  deleted                   int2 NOT NULL DEFAULT 0
);

COMMENT ON TABLE report_fill_complaint_security_stat IS '报表填报-异议投诉处理与信息安全情况（1:1）';
COMMENT ON COLUMN report_fill_complaint_security_stat.id IS '主键（= report_fill_basic_info.id）';

ALTER TABLE report_fill_complaint_security_stat ADD CONSTRAINT pk_report_fill_complaint_security_stat PRIMARY KEY (id);

-- ============================
-- report_fill_info_collect_stat（信息采集情况 1:1）
-- 说明：此块字段较多，建议按你们最终口径清单再扩展列；这里先给骨架 + 常见项示例
-- ============================
DROP SEQUENCE IF EXISTS report_fill_info_collect_stat_seq;
CREATE SEQUENCE report_fill_info_collect_stat_seq START 1;

CREATE TABLE IF NOT EXISTS report_fill_info_collect_stat (
  id                      BIGINT NOT NULL DEFAULT nextval('report_fill_info_collect_stat_seq'),
  collect_enterprise_total NUMERIC(18, 0),
  collect_borrow_total     NUMERIC(18, 0),
  collect_trade_total      NUMERIC(18, 0),
  collect_finance_total    NUMERIC(18, 0),
  collect_tax_total        NUMERIC(18, 0),
  collect_custom_total     NUMERIC(18, 0),
  collect_public_utility_total NUMERIC(18, 0),
  collect_field_sme_total  NUMERIC(18, 0),
  collect_micro_sme_total  NUMERIC(18, 0),
  collect_individual_total NUMERIC(18, 0),
  collect_natural_person_shareholder_total NUMERIC(18, 0),
  collect_key_person_total NUMERIC(18, 0),
  crawler_network_channel_total NUMERIC(18, 0),
  create_time             TIMESTAMP,
  update_time             TIMESTAMP,
  creator                 VARCHAR(64),
  updater                 VARCHAR(64),
  deleted                 int2 NOT NULL DEFAULT 0
);

COMMENT ON TABLE report_fill_info_collect_stat IS '报表填报-信息采集情况（1:1，字段可扩展）';
COMMENT ON COLUMN report_fill_info_collect_stat.id IS '主键（= report_fill_basic_info.id）';

ALTER TABLE report_fill_info_collect_stat ADD CONSTRAINT pk_report_fill_info_collect_stat PRIMARY KEY (id);
