-- 信用报送（积木表单落库：UUID 主键版）- PostgreSQL v15 - 2026-02-02
-- 说明：
-- 1) 主键策略：使用 32 位 UUID 字符串（无连字符），适配积木报表常用 UUID 策略
-- 2) report_fill_basic_info 作为"填报主记录（record）锚点"，唯一约束：role_id + period_id + report_id
-- 3) 1:1 块表使用独立主键 id，并用 parent_id 关联主记录；1:N 明细表使用独立主键 id，并用 record_id 关联主记录
-- 4) 审计/逻辑删除字段：create_time/update_time/creator/updater/deleted
--
-- 依赖扩展：pgcrypto（用于 gen_random_uuid）
CREATE EXTENSION IF NOT EXISTS pgcrypto;

-- ============================
-- report_fill_basic_info（主记录锚点）
-- ============================
DROP TABLE IF EXISTS report_fill_basic_info;
CREATE TABLE report_fill_basic_info (
  id          VARCHAR(32) NOT NULL DEFAULT replace(gen_random_uuid()::text, '-', ''),
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

ALTER TABLE report_fill_basic_info ADD CONSTRAINT pk_report_fill_basic_info PRIMARY KEY (id);
CREATE UNIQUE INDEX uk_report_fill_basic_info_role_period_report
  ON report_fill_basic_info (role_id, period_id, report_id, deleted);

COMMENT ON TABLE report_fill_basic_info IS '报表填报主记录（锚点，UUID主键）';
COMMENT ON COLUMN report_fill_basic_info.id IS '主键（32位UUID，无连字符）';
COMMENT ON COLUMN report_fill_basic_info.period_id IS '填报周期（如 2025-01 / 2025-Q1）';
COMMENT ON COLUMN report_fill_basic_info.role_id IS '角色id';
COMMENT ON COLUMN report_fill_basic_info.project_id IS '项目id（可选，不参与唯一约束）';
COMMENT ON COLUMN report_fill_basic_info.report_id IS '报表模板id（jimu_report.id）';
COMMENT ON COLUMN report_fill_basic_info.deleted IS '是否删除';

-- ============================
-- report_fill_enterprise_basic（企业基础信息块 1:1）
-- ============================
DROP TABLE IF EXISTS report_fill_enterprise_basic;
CREATE TABLE report_fill_enterprise_basic (
  id                            VARCHAR(32) NOT NULL DEFAULT replace(gen_random_uuid()::text, '-', ''),
  parent_id                     VARCHAR(32) NOT NULL,
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

ALTER TABLE report_fill_enterprise_basic ADD CONSTRAINT pk_report_fill_enterprise_basic PRIMARY KEY (id);
CREATE UNIQUE INDEX uk_report_fill_enterprise_basic_parent_id ON report_fill_enterprise_basic (parent_id);
CREATE INDEX idx_report_fill_enterprise_basic_parent_id ON report_fill_enterprise_basic (parent_id);
COMMENT ON TABLE report_fill_enterprise_basic IS '报表填报-企业基础信息块（1:1）';
COMMENT ON COLUMN report_fill_enterprise_basic.id IS '主键（UUID）';
COMMENT ON COLUMN report_fill_enterprise_basic.parent_id IS '父记录id（= report_fill_basic_info.id）';

-- ============================
-- report_fill_info_user_org_item（信息使用者机构明细 1:N）
-- ============================
DROP TABLE IF EXISTS report_fill_info_user_org_item;
CREATE TABLE report_fill_info_user_org_item (
  id                 VARCHAR(32) NOT NULL DEFAULT replace(gen_random_uuid()::text, '-', ''),
  org_name           VARCHAR(256) NOT NULL,
  industry_code      VARCHAR(64) NOT NULL,
  is_current_service int2 NOT NULL DEFAULT 0,
  sort_no            VARCHAR(32) NOT NULL,
  create_time        TIMESTAMP,
  update_time        TIMESTAMP,
  creator            VARCHAR(64),
  updater            VARCHAR(64),
  deleted            int2 NOT NULL DEFAULT 0
);

ALTER TABLE report_fill_info_user_org_item ADD CONSTRAINT pk_report_fill_info_user_org_item PRIMARY KEY (id);
COMMENT ON TABLE report_fill_info_user_org_item IS '报表填报-信息使用者机构明细（UUID主键）';

-- ============================
-- report_fill_info_user_gov_item（信息使用者机构+政府 明细 1:N）
-- ============================
DROP TABLE IF EXISTS report_fill_info_user_gov_item;
CREATE TABLE report_fill_info_user_gov_item (
  id                 VARCHAR(32) NOT NULL DEFAULT replace(gen_random_uuid()::text, '-', ''),
  gov_org_name       VARCHAR(256) NOT NULL,
  is_current_service int2 NOT NULL DEFAULT 0,
  sort_no            VARCHAR(32) NOT NULL,
  create_time        TIMESTAMP,
  update_time        TIMESTAMP,
  creator            VARCHAR(64),
  updater            VARCHAR(64),
  deleted            int2 NOT NULL DEFAULT 0
);

ALTER TABLE report_fill_info_user_gov_item ADD CONSTRAINT pk_report_fill_info_user_gov_item PRIMARY KEY (id);
COMMENT ON TABLE report_fill_info_user_gov_item IS '报表填报-信息使用者机构+政府明细（UUID主键）';

-- ============================
-- report_fill_biz_stat_finance（经营情况-财务口径 1:1）
-- ============================
DROP TABLE IF EXISTS report_fill_biz_stat_finance;
CREATE TABLE report_fill_biz_stat_finance (
  id                         VARCHAR(32) NOT NULL DEFAULT replace(gen_random_uuid()::text, '-', ''),
  parent_id                  VARCHAR(32) NOT NULL,
  asset_amount               NUMERIC(18, 2),
  liability_amount           NUMERIC(18, 2),
  year_income_amount         NUMERIC(18, 2),
  year_credit_income_amount  NUMERIC(18, 2),
  year_net_profit_amount     NUMERIC(18, 2),
  create_time                TIMESTAMP,
  update_time                TIMESTAMP,
  creator                    VARCHAR(64),
  updater                    VARCHAR(64),
  deleted                    int2 NOT NULL DEFAULT 0
);

ALTER TABLE report_fill_biz_stat_finance ADD CONSTRAINT pk_report_fill_biz_stat_finance PRIMARY KEY (id);
CREATE UNIQUE INDEX uk_report_fill_biz_stat_finance_parent_id ON report_fill_biz_stat_finance (parent_id);
CREATE INDEX idx_report_fill_biz_stat_finance_parent_id ON report_fill_biz_stat_finance (parent_id);
COMMENT ON TABLE report_fill_biz_stat_finance IS '报表填报-经营情况（财务口径，1:1）';
COMMENT ON COLUMN report_fill_biz_stat_finance.id IS '主键（UUID）';
COMMENT ON COLUMN report_fill_biz_stat_finance.parent_id IS '父记录id（= report_fill_basic_info.id）';

-- ============================
-- report_fill_biz_stat_hr（经营情况-人员口径 1:1）
-- ============================
DROP TABLE IF EXISTS report_fill_biz_stat_hr;
CREATE TABLE report_fill_biz_stat_hr (
  id                  VARCHAR(32) NOT NULL DEFAULT replace(gen_random_uuid()::text, '-', ''),
  parent_id           VARCHAR(32) NOT NULL,
  employee_total      NUMERIC(18, 0),
  employee_credit_biz NUMERIC(18, 0),
  create_time         TIMESTAMP,
  update_time         TIMESTAMP,
  creator             VARCHAR(64),
  updater             VARCHAR(64),
  deleted             int2 NOT NULL DEFAULT 0
);

ALTER TABLE report_fill_biz_stat_hr ADD CONSTRAINT pk_report_fill_biz_stat_hr PRIMARY KEY (id);
CREATE UNIQUE INDEX uk_report_fill_biz_stat_hr_parent_id ON report_fill_biz_stat_hr (parent_id);
CREATE INDEX idx_report_fill_biz_stat_hr_parent_id ON report_fill_biz_stat_hr (parent_id);
COMMENT ON TABLE report_fill_biz_stat_hr IS '报表填报-经营情况（人员口径，1:1）';
COMMENT ON COLUMN report_fill_biz_stat_hr.id IS '主键（UUID）';
COMMENT ON COLUMN report_fill_biz_stat_hr.parent_id IS '父记录id（= report_fill_basic_info.id）';

-- ============================
-- report_fill_biz_stat_credit_build（经营情况-信用体系建设口径 1:1）
-- ============================
DROP TABLE IF EXISTS report_fill_biz_stat_credit_build;
CREATE TABLE report_fill_biz_stat_credit_build (
  id            VARCHAR(32) NOT NULL DEFAULT replace(gen_random_uuid()::text, '-', ''),
  parent_id     VARCHAR(32) NOT NULL,
  project_count NUMERIC(18, 0),
  income_amount NUMERIC(18, 2),
  -- 信息使用者机构（按行业分类）总累计数 / 当前使用机构数（见填报表单）
  user_org_total_bank NUMERIC(18, 0),
  user_org_current_bank NUMERIC(18, 0),
  user_org_total_securities NUMERIC(18, 0),
  user_org_current_securities NUMERIC(18, 0),
  user_org_total_insurance NUMERIC(18, 0),
  user_org_current_insurance NUMERIC(18, 0),
  user_org_total_trust NUMERIC(18, 0),
  user_org_current_trust NUMERIC(18, 0),
  user_org_total_p2p_lending_intermediary NUMERIC(18, 0),
  user_org_current_p2p_lending_intermediary NUMERIC(18, 0),
  user_org_total_payment_institution NUMERIC(18, 0),
  user_org_current_payment_institution NUMERIC(18, 0),
  user_org_total_financing_leasing_guarantee NUMERIC(18, 0),
  user_org_current_financing_leasing_guarantee NUMERIC(18, 0),
  user_org_total_micro_loan_company NUMERIC(18, 0),
  user_org_current_micro_loan_company NUMERIC(18, 0),
  user_org_total_consumer_finance_company NUMERIC(18, 0),
  user_org_current_consumer_finance_company NUMERIC(18, 0),
  user_org_total_asset_management_company NUMERIC(18, 0),
  user_org_current_asset_management_company NUMERIC(18, 0),
  user_org_total_auto_finance_company NUMERIC(18, 0),
  user_org_current_auto_finance_company NUMERIC(18, 0),
  user_org_total_commercial_factoring_company NUMERIC(18, 0),
  user_org_current_commercial_factoring_company NUMERIC(18, 0),
  user_org_total_government NUMERIC(18, 0),
  user_org_current_government NUMERIC(18, 0),
  user_org_total_public_utility NUMERIC(18, 0),
  user_org_current_public_utility NUMERIC(18, 0),
  user_org_total_industry_association NUMERIC(18, 0),
  user_org_current_industry_association NUMERIC(18, 0),
  user_org_total_court NUMERIC(18, 0),
  user_org_current_court NUMERIC(18, 0),
  user_org_total_ecommerce_platform NUMERIC(18, 0),
  user_org_current_ecommerce_platform NUMERIC(18, 0),
  user_org_total_agriculture_related_enterprise NUMERIC(18, 0),
  user_org_current_agriculture_related_enterprise NUMERIC(18, 0),
  user_org_total_other_credit_reporting_agency NUMERIC(18, 0),
  user_org_current_other_credit_reporting_agency NUMERIC(18, 0),
  user_org_total_data_service_provider NUMERIC(18, 0),
  user_org_current_data_service_provider NUMERIC(18, 0),
  user_org_total_trading_counterparty NUMERIC(18, 0),
  user_org_current_trading_counterparty NUMERIC(18, 0),
  user_org_total_information_subject_self NUMERIC(18, 0),
  user_org_current_information_subject_self NUMERIC(18, 0),
  user_org_total_other NUMERIC(18, 0),
  user_org_current_other NUMERIC(18, 0),
  create_time   TIMESTAMP,
  update_time   TIMESTAMP,
  creator       VARCHAR(64),
  updater       VARCHAR(64),
  deleted       int2 NOT NULL DEFAULT 0
);

ALTER TABLE report_fill_biz_stat_credit_build ADD CONSTRAINT pk_report_fill_biz_stat_credit_build PRIMARY KEY (id);
CREATE UNIQUE INDEX uk_report_fill_biz_stat_credit_build_parent_id ON report_fill_biz_stat_credit_build (parent_id);
CREATE INDEX idx_report_fill_biz_stat_credit_build_parent_id ON report_fill_biz_stat_credit_build (parent_id);
COMMENT ON TABLE report_fill_biz_stat_credit_build IS '报表填报-经营情况（信用体系建设，1:1）';
COMMENT ON COLUMN report_fill_biz_stat_credit_build.id IS '主键（UUID）';
COMMENT ON COLUMN report_fill_biz_stat_credit_build.parent_id IS '父记录id（= report_fill_basic_info.id）';
COMMENT ON COLUMN report_fill_biz_stat_credit_build.user_org_total_bank IS '银行-信息使用者机构总累计数';
COMMENT ON COLUMN report_fill_biz_stat_credit_build.user_org_current_bank IS '银行-当前使用服务的信息使用者机构数';
COMMENT ON COLUMN report_fill_biz_stat_credit_build.user_org_total_securities IS '证券-信息使用者机构总累计数';
COMMENT ON COLUMN report_fill_biz_stat_credit_build.user_org_current_securities IS '证券-当前使用服务的信息使用者机构数';
COMMENT ON COLUMN report_fill_biz_stat_credit_build.user_org_total_insurance IS '保险-信息使用者机构总累计数';
COMMENT ON COLUMN report_fill_biz_stat_credit_build.user_org_current_insurance IS '保险-当前使用服务的信息使用者机构数';
COMMENT ON COLUMN report_fill_biz_stat_credit_build.user_org_total_trust IS '信托-信息使用者机构总累计数';
COMMENT ON COLUMN report_fill_biz_stat_credit_build.user_org_current_trust IS '信托-当前使用服务的信息使用者机构数';
COMMENT ON COLUMN report_fill_biz_stat_credit_build.user_org_total_p2p_lending_intermediary IS 'P2P网络借贷中介-信息使用者机构总累计数';
COMMENT ON COLUMN report_fill_biz_stat_credit_build.user_org_current_p2p_lending_intermediary IS 'P2P网络借贷中介-当前使用服务的信息使用者机构数';
COMMENT ON COLUMN report_fill_biz_stat_credit_build.user_org_total_payment_institution IS '支付机构-信息使用者机构总累计数';
COMMENT ON COLUMN report_fill_biz_stat_credit_build.user_org_current_payment_institution IS '支付机构-当前使用服务的信息使用者机构数';
COMMENT ON COLUMN report_fill_biz_stat_credit_build.user_org_total_financing_leasing_guarantee IS '融资租赁及担保类公司-信息使用者机构总累计数';
COMMENT ON COLUMN report_fill_biz_stat_credit_build.user_org_current_financing_leasing_guarantee IS '融资租赁及担保类公司-当前使用服务的信息使用者机构数';
COMMENT ON COLUMN report_fill_biz_stat_credit_build.user_org_total_micro_loan_company IS '小额贷款公司-信息使用者机构总累计数';
COMMENT ON COLUMN report_fill_biz_stat_credit_build.user_org_current_micro_loan_company IS '小额贷款公司-当前使用服务的信息使用者机构数';
COMMENT ON COLUMN report_fill_biz_stat_credit_build.user_org_total_consumer_finance_company IS '消费金融公司-信息使用者机构总累计数';
COMMENT ON COLUMN report_fill_biz_stat_credit_build.user_org_current_consumer_finance_company IS '消费金融公司-当前使用服务的信息使用者机构数';
COMMENT ON COLUMN report_fill_biz_stat_credit_build.user_org_total_asset_management_company IS '资产管理公司-信息使用者机构总累计数';
COMMENT ON COLUMN report_fill_biz_stat_credit_build.user_org_current_asset_management_company IS '资产管理公司-当前使用服务的信息使用者机构数';
COMMENT ON COLUMN report_fill_biz_stat_credit_build.user_org_total_auto_finance_company IS '汽车金融公司-信息使用者机构总累计数';
COMMENT ON COLUMN report_fill_biz_stat_credit_build.user_org_current_auto_finance_company IS '汽车金融公司-当前使用服务的信息使用者机构数';
COMMENT ON COLUMN report_fill_biz_stat_credit_build.user_org_total_commercial_factoring_company IS '商业保理公司-信息使用者机构总累计数';
COMMENT ON COLUMN report_fill_biz_stat_credit_build.user_org_current_commercial_factoring_company IS '商业保理公司-当前使用服务的信息使用者机构数';
COMMENT ON COLUMN report_fill_biz_stat_credit_build.user_org_total_government IS '政府-信息使用者机构总累计数';
COMMENT ON COLUMN report_fill_biz_stat_credit_build.user_org_current_government IS '政府-当前使用服务的信息使用者机构数';
COMMENT ON COLUMN report_fill_biz_stat_credit_build.user_org_total_public_utility IS '公用事业单位（水、电、气、通讯等）-信息使用者机构总累计数';
COMMENT ON COLUMN report_fill_biz_stat_credit_build.user_org_current_public_utility IS '公用事业单位（水、电、气、通讯等）-当前使用服务的信息使用者机构数';
COMMENT ON COLUMN report_fill_biz_stat_credit_build.user_org_total_industry_association IS '行业协会-信息使用者机构总累计数';
COMMENT ON COLUMN report_fill_biz_stat_credit_build.user_org_current_industry_association IS '行业协会-当前使用服务的信息使用者机构数';
COMMENT ON COLUMN report_fill_biz_stat_credit_build.user_org_total_court IS '法院-信息使用者机构总累计数';
COMMENT ON COLUMN report_fill_biz_stat_credit_build.user_org_current_court IS '法院-当前使用服务的信息使用者机构数';
COMMENT ON COLUMN report_fill_biz_stat_credit_build.user_org_total_ecommerce_platform IS '电子商务平台-信息使用者机构总累计数';
COMMENT ON COLUMN report_fill_biz_stat_credit_build.user_org_current_ecommerce_platform IS '电子商务平台-当前使用服务的信息使用者机构数';
COMMENT ON COLUMN report_fill_biz_stat_credit_build.user_org_total_agriculture_related_enterprise IS '涉农企业-信息使用者机构总累计数';
COMMENT ON COLUMN report_fill_biz_stat_credit_build.user_org_current_agriculture_related_enterprise IS '涉农企业-当前使用服务的信息使用者机构数';
COMMENT ON COLUMN report_fill_biz_stat_credit_build.user_org_total_other_credit_reporting_agency IS '其他征信机构-信息使用者机构总累计数';
COMMENT ON COLUMN report_fill_biz_stat_credit_build.user_org_current_other_credit_reporting_agency IS '其他征信机构-当前使用服务的信息使用者机构数';
COMMENT ON COLUMN report_fill_biz_stat_credit_build.user_org_total_data_service_provider IS '数据服务商-信息使用者机构总累计数';
COMMENT ON COLUMN report_fill_biz_stat_credit_build.user_org_current_data_service_provider IS '数据服务商-当前使用服务的信息使用者机构数';
COMMENT ON COLUMN report_fill_biz_stat_credit_build.user_org_total_trading_counterparty IS '交易对手方-信息使用者机构总累计数';
COMMENT ON COLUMN report_fill_biz_stat_credit_build.user_org_current_trading_counterparty IS '交易对手方-当前使用服务的信息使用者机构数';
COMMENT ON COLUMN report_fill_biz_stat_credit_build.user_org_total_information_subject_self IS '信息主体自身-信息使用者机构总累计数';
COMMENT ON COLUMN report_fill_biz_stat_credit_build.user_org_current_information_subject_self IS '信息主体自身-当前使用服务的信息使用者机构数';
COMMENT ON COLUMN report_fill_biz_stat_credit_build.user_org_total_other IS '其他-信息使用者机构总累计数';
COMMENT ON COLUMN report_fill_biz_stat_credit_build.user_org_current_other IS '其他-当前使用服务的信息使用者机构数';

-- ============================
-- report_fill_product_stat（提供的征信产品/服务次数 1:N）
-- ============================
DROP TABLE IF EXISTS report_fill_product_stat;
CREATE TABLE report_fill_product_stat (
  id           VARCHAR(32) NOT NULL DEFAULT replace(gen_random_uuid()::text, '-', ''),
  record_id    VARCHAR(32) NOT NULL,
  product_type VARCHAR(64) NOT NULL,
  product_name VARCHAR(256),
  year_count   NUMERIC(18, 0),
  create_time  TIMESTAMP,
  update_time  TIMESTAMP,
  creator      VARCHAR(64),
  updater      VARCHAR(64),
  deleted      int2 NOT NULL DEFAULT 0
);

ALTER TABLE report_fill_product_stat ADD CONSTRAINT pk_report_fill_product_stat PRIMARY KEY (id);
CREATE INDEX idx_report_fill_product_stat_record_id
  ON report_fill_product_stat (record_id);
COMMENT ON TABLE report_fill_product_stat IS '报表填报-提供的征信产品/服务次数（1:N，UUID主键）';
COMMENT ON COLUMN report_fill_product_stat.record_id IS '主记录id（= report_fill_basic_info.id）';

-- ============================
-- report_fill_service_by_industry（按行业分类：产品与服务提供次数 1:N）
-- ============================
DROP TABLE IF EXISTS report_fill_service_by_industry;
CREATE TABLE report_fill_service_by_industry (
  id                 VARCHAR(32) NOT NULL DEFAULT replace(gen_random_uuid()::text, '-', ''),
  record_id          VARCHAR(32) NOT NULL,
  industry_code      VARCHAR(64),
  year_service_count NUMERIC(18, 0),
  create_time        TIMESTAMP,
  update_time        TIMESTAMP,
  creator            VARCHAR(64),
  updater            VARCHAR(64),
  deleted            int2 NOT NULL DEFAULT 0
);

ALTER TABLE report_fill_service_by_industry ADD CONSTRAINT pk_report_fill_service_by_industry PRIMARY KEY (id);
CREATE INDEX idx_report_fill_service_by_industry_record_id
  ON report_fill_service_by_industry (record_id);
COMMENT ON TABLE report_fill_service_by_industry IS '报表填报-产品与服务提供情况（按行业分类，1:N，UUID主键）';
COMMENT ON COLUMN report_fill_service_by_industry.record_id IS '主记录id（= report_fill_basic_info.id）';

-- ============================
-- report_fill_info_source_by_industry（信息来源情况：按行业分类 1:N）
-- ============================
DROP TABLE IF EXISTS report_fill_info_source_by_industry;
CREATE TABLE report_fill_info_source_by_industry (
  id                   VARCHAR(32) NOT NULL DEFAULT replace(gen_random_uuid()::text, '-', ''),
  record_id            VARCHAR(32) NOT NULL,
  industry_code        VARCHAR(64),
  provider_org_total   NUMERIC(18, 0),
  provider_org_current NUMERIC(18, 0),
  create_time          TIMESTAMP,
  update_time          TIMESTAMP,
  creator              VARCHAR(64),
  updater              VARCHAR(64),
  deleted              int2 NOT NULL DEFAULT 0
);

ALTER TABLE report_fill_info_source_by_industry ADD CONSTRAINT pk_report_fill_info_source_by_industry PRIMARY KEY (id);
CREATE INDEX idx_report_fill_info_source_by_industry_record_id
  ON report_fill_info_source_by_industry (record_id);
COMMENT ON TABLE report_fill_info_source_by_industry IS '报表填报-信息来源情况（按行业分类，1:N，UUID主键）';
COMMENT ON COLUMN report_fill_info_source_by_industry.record_id IS '主记录id（= report_fill_basic_info.id）';

-- ============================
-- report_fill_complaint_security_stat（异议/投诉/信息安全情况 1:1）
-- ============================
DROP TABLE IF EXISTS report_fill_complaint_security_stat;
CREATE TABLE report_fill_complaint_security_stat (
  id                        VARCHAR(32) NOT NULL DEFAULT replace(gen_random_uuid()::text, '-', ''),
  parent_id                 VARCHAR(32) NOT NULL,
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

ALTER TABLE report_fill_complaint_security_stat ADD CONSTRAINT pk_report_fill_complaint_security_stat PRIMARY KEY (id);
CREATE UNIQUE INDEX uk_report_fill_complaint_security_stat_parent_id ON report_fill_complaint_security_stat (parent_id);
CREATE INDEX idx_report_fill_complaint_security_stat_parent_id ON report_fill_complaint_security_stat (parent_id);
COMMENT ON TABLE report_fill_complaint_security_stat IS '报表填报-异议投诉处理与信息安全情况（1:1）';
COMMENT ON COLUMN report_fill_complaint_security_stat.id IS '主键（UUID）';
COMMENT ON COLUMN report_fill_complaint_security_stat.parent_id IS '父记录id（= report_fill_basic_info.id）';

-- ============================
-- report_fill_info_collect_stat（信息采集情况 1:1）
-- ============================
DROP TABLE IF EXISTS report_fill_info_collect_stat;
CREATE TABLE report_fill_info_collect_stat (
  id                                   VARCHAR(32) NOT NULL DEFAULT replace(gen_random_uuid()::text, '-', ''),
  parent_id                            VARCHAR(32) NOT NULL,
  collect_enterprise_total             NUMERIC(18, 0),
  collect_borrow_total                 NUMERIC(18, 0),
  collect_trade_total                  NUMERIC(18, 0),
  collect_finance_total                NUMERIC(18, 0),
  collect_tax_total                    NUMERIC(18, 0),
  collect_custom_total                 NUMERIC(18, 0),
  collect_public_utility_total         NUMERIC(18, 0),
  collect_field_sme_total              NUMERIC(18, 0),
  collect_micro_sme_total              NUMERIC(18, 0),
  collect_individual_total             NUMERIC(18, 0),
  collect_natural_person_shareholder_total NUMERIC(18, 0),
  crawler_network_channel_total        NUMERIC(18, 0),
  create_time                          TIMESTAMP,
  update_time                          TIMESTAMP,
  creator                              VARCHAR(64),
  updater                              VARCHAR(64),
  deleted                              int2 NOT NULL DEFAULT 0
);

ALTER TABLE report_fill_info_collect_stat ADD CONSTRAINT pk_report_fill_info_collect_stat PRIMARY KEY (id);
CREATE UNIQUE INDEX uk_report_fill_info_collect_stat_parent_id ON report_fill_info_collect_stat (parent_id);
CREATE INDEX idx_report_fill_info_collect_stat_parent_id ON report_fill_info_collect_stat (parent_id);
COMMENT ON TABLE report_fill_info_collect_stat IS '报表填报-信息采集情况（1:1）';
COMMENT ON COLUMN report_fill_info_collect_stat.id IS '主键（UUID）';
COMMENT ON COLUMN report_fill_info_collect_stat.parent_id IS '父记录id（= report_fill_basic_info.id）';

