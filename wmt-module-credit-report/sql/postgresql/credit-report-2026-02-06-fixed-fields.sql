-- 信用报送（积木表单落库：固定字段版 v2）- PostgreSQL v15 - 2026-02-06
-- 说明：
-- 1) 本目录用于存放新版 SQL，与历史版本并行；请勿直接修改旧版脚本。
-- 2) 将以下 3 张表由“按 code 可扩展的 1:N”调整为“固定字段一一对应的 1:1（宽表）”：
--    - report_fill_info_source_by_industry（信息来源情况）
--    - report_fill_service_by_industry（产品与服务提供情况：当年提供次数）
--    - report_fill_product_stat（提供的征信产品/服务次数）
-- 3) 三张表的 record_id 均关联 report_fill_basic_info.id（同周期/角色/报表的填报锚点）。
-- 4) 仍保留审计/逻辑删除字段：create_time/update_time/creator/updater/deleted。

-- ============================
-- report_fill_info_source_by_industry（信息来源情况：固定字段，1:1）
-- 每个字段与字典 info_source_status（21项）一一对应：
-- - xxx_provider_org_total：累计签约的信息提供机构总数
-- - xxx_provider_org_current：当前提供服务的机构数
-- ============================
DROP TABLE IF EXISTS report_fill_info_source_by_industry;
CREATE TABLE report_fill_info_source_by_industry (
  id                                    VARCHAR(32) NOT NULL DEFAULT replace(gen_random_uuid()::text, '-', ''),
  record_id                              VARCHAR(32) NOT NULL,

  -- 金融、类金融机构
  bank_provider_org_total                NUMERIC(18, 0),
  bank_provider_org_current              NUMERIC(18, 0),
  security_provider_org_total            NUMERIC(18, 0),
  security_provider_org_current          NUMERIC(18, 0),
  insurance_provider_org_total           NUMERIC(18, 0),
  insurance_provider_org_current         NUMERIC(18, 0),
  trust_provider_org_total               NUMERIC(18, 0),
  trust_provider_org_current             NUMERIC(18, 0),
  p2p_lending_provider_org_total         NUMERIC(18, 0),
  p2p_lending_provider_org_current       NUMERIC(18, 0),
  payment_institution_provider_org_total  NUMERIC(18, 0),
  payment_institution_provider_org_current NUMERIC(18, 0),
  financial_leasing_guarantee_provider_org_total  NUMERIC(18, 0),
  financial_leasing_guarantee_provider_org_current NUMERIC(18, 0),
  micro_loan_company_provider_org_total  NUMERIC(18, 0),
  micro_loan_company_provider_org_current NUMERIC(18, 0),
  consumer_finance_company_provider_org_total  NUMERIC(18, 0),
  consumer_finance_company_provider_org_current NUMERIC(18, 0),
  asset_management_company_provider_org_total  NUMERIC(18, 0),
  asset_management_company_provider_org_current NUMERIC(18, 0),
  auto_finance_company_provider_org_total  NUMERIC(18, 0),
  auto_finance_company_provider_org_current NUMERIC(18, 0),
  commercial_factoring_company_provider_org_total  NUMERIC(18, 0),
  commercial_factoring_company_provider_org_current NUMERIC(18, 0),

  -- 其他信息来源机构
  government_provider_org_total          NUMERIC(18, 0),
  government_provider_org_current        NUMERIC(18, 0),
  public_utilities_provider_org_total    NUMERIC(18, 0),
  public_utilities_provider_org_current  NUMERIC(18, 0),
  industry_association_provider_org_total   NUMERIC(18, 0),
  industry_association_provider_org_current NUMERIC(18, 0),
  court_provider_org_total               NUMERIC(18, 0),
  court_provider_org_current             NUMERIC(18, 0),
  ecommerce_platform_provider_org_total  NUMERIC(18, 0),
  ecommerce_platform_provider_org_current NUMERIC(18, 0),
  other_credit_agency_provider_org_total NUMERIC(18, 0),
  other_credit_agency_provider_org_current NUMERIC(18, 0),
  data_service_provider_provider_org_total NUMERIC(18, 0),
  data_service_provider_provider_org_current NUMERIC(18, 0),
  counterparty_provider_org_total        NUMERIC(18, 0),
  counterparty_provider_org_current      NUMERIC(18, 0),
  other_provider_org_total               NUMERIC(18, 0),
  other_provider_org_current             NUMERIC(18, 0),

  create_time                            TIMESTAMP,
  update_time                            TIMESTAMP,
  creator                                VARCHAR(64),
  updater                                VARCHAR(64),
  deleted                                int2 NOT NULL DEFAULT 0
);

ALTER TABLE report_fill_info_source_by_industry
  ADD CONSTRAINT pk_report_fill_info_source_by_industry PRIMARY KEY (id);
CREATE UNIQUE INDEX uk_report_fill_info_source_by_industry_record_id
  ON report_fill_info_source_by_industry (record_id, deleted);
CREATE INDEX idx_report_fill_info_source_by_industry_record_id
  ON report_fill_info_source_by_industry (record_id);

COMMENT ON TABLE report_fill_info_source_by_industry IS '报表填报-信息来源情况（固定字段，1:1，UUID主键）';
COMMENT ON COLUMN report_fill_info_source_by_industry.record_id IS '主记录id（= report_fill_basic_info.id）';

COMMENT ON COLUMN report_fill_info_source_by_industry.bank_provider_org_total IS '银行：累计签约的信息提供机构总数（info_source_status.bank）';
COMMENT ON COLUMN report_fill_info_source_by_industry.bank_provider_org_current IS '银行：当前提供服务的机构数（info_source_status.bank）';
COMMENT ON COLUMN report_fill_info_source_by_industry.security_provider_org_total IS '证券：累计签约的信息提供机构总数（info_source_status.security）';
COMMENT ON COLUMN report_fill_info_source_by_industry.security_provider_org_current IS '证券：当前提供服务的机构数（info_source_status.security）';
COMMENT ON COLUMN report_fill_info_source_by_industry.insurance_provider_org_total IS '保险：累计签约的信息提供机构总数（info_source_status.insurance）';
COMMENT ON COLUMN report_fill_info_source_by_industry.insurance_provider_org_current IS '保险：当前提供服务的机构数（info_source_status.insurance）';
COMMENT ON COLUMN report_fill_info_source_by_industry.trust_provider_org_total IS '信托：累计签约的信息提供机构总数（info_source_status.trust）';
COMMENT ON COLUMN report_fill_info_source_by_industry.trust_provider_org_current IS '信托：当前提供服务的机构数（info_source_status.trust）';
COMMENT ON COLUMN report_fill_info_source_by_industry.p2p_lending_provider_org_total IS 'P2P网络借贷中介：累计签约总数（info_source_status.p2p_lending）';
COMMENT ON COLUMN report_fill_info_source_by_industry.p2p_lending_provider_org_current IS 'P2P网络借贷中介：当前服务数（info_source_status.p2p_lending）';
COMMENT ON COLUMN report_fill_info_source_by_industry.payment_institution_provider_org_total IS '支付机构：累计签约总数（info_source_status.payment_institution）';
COMMENT ON COLUMN report_fill_info_source_by_industry.payment_institution_provider_org_current IS '支付机构：当前服务数（info_source_status.payment_institution）';
COMMENT ON COLUMN report_fill_info_source_by_industry.financial_leasing_guarantee_provider_org_total IS '融资租赁及担保类公司：累计签约总数（info_source_status.financial_leasing_guarantee）';
COMMENT ON COLUMN report_fill_info_source_by_industry.financial_leasing_guarantee_provider_org_current IS '融资租赁及担保类公司：当前服务数（info_source_status.financial_leasing_guarantee）';
COMMENT ON COLUMN report_fill_info_source_by_industry.micro_loan_company_provider_org_total IS '小额贷款公司：累计签约总数（info_source_status.micro_loan_company）';
COMMENT ON COLUMN report_fill_info_source_by_industry.micro_loan_company_provider_org_current IS '小额贷款公司：当前服务数（info_source_status.micro_loan_company）';
COMMENT ON COLUMN report_fill_info_source_by_industry.consumer_finance_company_provider_org_total IS '消费金融公司：累计签约总数（info_source_status.consumer_finance_company）';
COMMENT ON COLUMN report_fill_info_source_by_industry.consumer_finance_company_provider_org_current IS '消费金融公司：当前服务数（info_source_status.consumer_finance_company）';
COMMENT ON COLUMN report_fill_info_source_by_industry.asset_management_company_provider_org_total IS '资产管理公司：累计签约总数（info_source_status.asset_management_company）';
COMMENT ON COLUMN report_fill_info_source_by_industry.asset_management_company_provider_org_current IS '资产管理公司：当前服务数（info_source_status.asset_management_company）';
COMMENT ON COLUMN report_fill_info_source_by_industry.auto_finance_company_provider_org_total IS '汽车金融公司：累计签约总数（info_source_status.auto_finance_company）';
COMMENT ON COLUMN report_fill_info_source_by_industry.auto_finance_company_provider_org_current IS '汽车金融公司：当前服务数（info_source_status.auto_finance_company）';
COMMENT ON COLUMN report_fill_info_source_by_industry.commercial_factoring_company_provider_org_total IS '商业保理公司：累计签约总数（info_source_status.commercial_factoring_company）';
COMMENT ON COLUMN report_fill_info_source_by_industry.commercial_factoring_company_provider_org_current IS '商业保理公司：当前服务数（info_source_status.commercial_factoring_company）';

COMMENT ON COLUMN report_fill_info_source_by_industry.government_provider_org_total IS '政府：累计签约总数（info_source_status.government）';
COMMENT ON COLUMN report_fill_info_source_by_industry.government_provider_org_current IS '政府：当前服务数（info_source_status.government）';
COMMENT ON COLUMN report_fill_info_source_by_industry.public_utilities_provider_org_total IS '公用事业单位：累计签约总数（info_source_status.public_utilities）';
COMMENT ON COLUMN report_fill_info_source_by_industry.public_utilities_provider_org_current IS '公用事业单位：当前服务数（info_source_status.public_utilities）';
COMMENT ON COLUMN report_fill_info_source_by_industry.industry_association_provider_org_total IS '行业协会：累计签约总数（info_source_status.industry_association）';
COMMENT ON COLUMN report_fill_info_source_by_industry.industry_association_provider_org_current IS '行业协会：当前服务数（info_source_status.industry_association）';
COMMENT ON COLUMN report_fill_info_source_by_industry.court_provider_org_total IS '法院：累计签约总数（info_source_status.court）';
COMMENT ON COLUMN report_fill_info_source_by_industry.court_provider_org_current IS '法院：当前服务数（info_source_status.court）';
COMMENT ON COLUMN report_fill_info_source_by_industry.ecommerce_platform_provider_org_total IS '电子商务平台：累计签约总数（info_source_status.ecommerce_platform）';
COMMENT ON COLUMN report_fill_info_source_by_industry.ecommerce_platform_provider_org_current IS '电子商务平台：当前服务数（info_source_status.ecommerce_platform）';
COMMENT ON COLUMN report_fill_info_source_by_industry.other_credit_agency_provider_org_total IS '其他征信机构：累计签约总数（info_source_status.other_credit_agency）';
COMMENT ON COLUMN report_fill_info_source_by_industry.other_credit_agency_provider_org_current IS '其他征信机构：当前服务数（info_source_status.other_credit_agency）';
COMMENT ON COLUMN report_fill_info_source_by_industry.data_service_provider_provider_org_total IS '数据服务商：累计签约总数（info_source_status.data_service_provider）';
COMMENT ON COLUMN report_fill_info_source_by_industry.data_service_provider_provider_org_current IS '数据服务商：当前服务数（info_source_status.data_service_provider）';
COMMENT ON COLUMN report_fill_info_source_by_industry.counterparty_provider_org_total IS '交易对手方：累计签约总数（info_source_status.counterparty）';
COMMENT ON COLUMN report_fill_info_source_by_industry.counterparty_provider_org_current IS '交易对手方：当前服务数（info_source_status.counterparty）';
COMMENT ON COLUMN report_fill_info_source_by_industry.other_provider_org_total IS '其他：累计签约总数（info_source_status.other，不含信息主体自身提供）';
COMMENT ON COLUMN report_fill_info_source_by_industry.other_provider_org_current IS '其他：当前服务数（info_source_status.other，不含信息主体自身提供）';

-- ============================
-- report_fill_service_by_industry（产品与服务提供情况：固定字段，1:1）
-- 每个字段与字典 industry_code（23项）一一对应：当年提供产品（服务）次数
-- ============================
DROP TABLE IF EXISTS report_fill_service_by_industry;
CREATE TABLE report_fill_service_by_industry (
  id                                    VARCHAR(32) NOT NULL DEFAULT replace(gen_random_uuid()::text, '-', ''),
  record_id                              VARCHAR(32) NOT NULL,

  bank_year_service_count                NUMERIC(18, 0),
  security_year_service_count            NUMERIC(18, 0),
  insurance_year_service_count           NUMERIC(18, 0),
  trust_year_service_count               NUMERIC(18, 0),
  p2p_lending_year_service_count         NUMERIC(18, 0),
  payment_institution_year_service_count NUMERIC(18, 0),
  financial_leasing_guarantee_year_service_count NUMERIC(18, 0),
  micro_loan_company_year_service_count  NUMERIC(18, 0),
  consumer_finance_company_year_service_count NUMERIC(18, 0),
  asset_management_company_year_service_count  NUMERIC(18, 0),
  auto_finance_company_year_service_count NUMERIC(18, 0),
  commercial_factoring_company_year_service_count NUMERIC(18, 0),
  government_year_service_count          NUMERIC(18, 0),
  public_utilities_year_service_count    NUMERIC(18, 0),
  industry_association_year_service_count NUMERIC(18, 0),
  court_year_service_count               NUMERIC(18, 0),
  ecommerce_platform_year_service_count  NUMERIC(18, 0),
  agricultural_enterprise_year_service_count NUMERIC(18, 0),
  other_credit_agency_year_service_count NUMERIC(18, 0),
  data_service_provider_year_service_count NUMERIC(18, 0),
  counterparty_year_service_count        NUMERIC(18, 0),
  information_subject_itself_year_service_count NUMERIC(18, 0),
  other_year_service_count               NUMERIC(18, 0),

  -- 政府（机构明细统计字段，供报表展示用）
  user_org_total_government              NUMERIC(18, 0),
  user_org_current_government            NUMERIC(18, 0),

  create_time                            TIMESTAMP,
  update_time                            TIMESTAMP,
  creator                                VARCHAR(64),
  updater                                VARCHAR(64),
  deleted                                int2 NOT NULL DEFAULT 0
);

ALTER TABLE report_fill_service_by_industry
  ADD CONSTRAINT pk_report_fill_service_by_industry PRIMARY KEY (id);
CREATE UNIQUE INDEX uk_report_fill_service_by_industry_record_id
  ON report_fill_service_by_industry (record_id, deleted);
CREATE INDEX idx_report_fill_service_by_industry_record_id
  ON report_fill_service_by_industry (record_id);

COMMENT ON TABLE report_fill_service_by_industry IS '报表填报-产品与服务提供情况（固定字段，1:1，UUID主键）';
COMMENT ON COLUMN report_fill_service_by_industry.record_id IS '主记录id（= report_fill_basic_info.id）';

COMMENT ON COLUMN report_fill_service_by_industry.bank_year_service_count IS '银行：当年提供产品（服务）次数（industry_code.bank）';
COMMENT ON COLUMN report_fill_service_by_industry.security_year_service_count IS '证券：当年提供产品（服务）次数（industry_code.security）';
COMMENT ON COLUMN report_fill_service_by_industry.insurance_year_service_count IS '保险：当年提供产品（服务）次数（industry_code.insurance）';
COMMENT ON COLUMN report_fill_service_by_industry.trust_year_service_count IS '信托：当年提供产品（服务）次数（industry_code.trust）';
COMMENT ON COLUMN report_fill_service_by_industry.p2p_lending_year_service_count IS 'P2P网络借贷中介：当年提供次数（industry_code.p2p_lending）';
COMMENT ON COLUMN report_fill_service_by_industry.payment_institution_year_service_count IS '支付机构：当年提供次数（industry_code.payment_institution）';
COMMENT ON COLUMN report_fill_service_by_industry.financial_leasing_guarantee_year_service_count IS '融资租赁及担保类公司：当年提供次数（industry_code.financial_leasing_guarantee）';
COMMENT ON COLUMN report_fill_service_by_industry.micro_loan_company_year_service_count IS '小额贷款公司：当年提供次数（industry_code.micro_loan_company）';
COMMENT ON COLUMN report_fill_service_by_industry.consumer_finance_company_year_service_count IS '消费金融公司：当年提供次数（industry_code.consumer_finance_company）';
COMMENT ON COLUMN report_fill_service_by_industry.asset_management_company_year_service_count IS '资产管理公司：当年提供次数（industry_code.asset_management_company）';
COMMENT ON COLUMN report_fill_service_by_industry.auto_finance_company_year_service_count IS '汽车金融公司：当年提供次数（industry_code.auto_finance_company）';
COMMENT ON COLUMN report_fill_service_by_industry.commercial_factoring_company_year_service_count IS '商业保理公司：当年提供次数（industry_code.commercial_factoring_company）';
COMMENT ON COLUMN report_fill_service_by_industry.government_year_service_count IS '政府：当年提供次数（industry_code.government）';
COMMENT ON COLUMN report_fill_service_by_industry.public_utilities_year_service_count IS '公用事业单位：当年提供次数（industry_code.public_utilities）';
COMMENT ON COLUMN report_fill_service_by_industry.industry_association_year_service_count IS '行业协会：当年提供次数（industry_code.industry_association）';
COMMENT ON COLUMN report_fill_service_by_industry.court_year_service_count IS '法院：当年提供次数（industry_code.court）';
COMMENT ON COLUMN report_fill_service_by_industry.ecommerce_platform_year_service_count IS '电子商务平台：当年提供次数（industry_code.ecommerce_platform）';
COMMENT ON COLUMN report_fill_service_by_industry.agricultural_enterprise_year_service_count IS '涉农企业：当年提供次数（industry_code.agricultural_enterprise）';
COMMENT ON COLUMN report_fill_service_by_industry.other_credit_agency_year_service_count IS '其他征信机构：当年提供次数（industry_code.other_credit_agency）';
COMMENT ON COLUMN report_fill_service_by_industry.data_service_provider_year_service_count IS '数据服务商：当年提供次数（industry_code.data_service_provider）';
COMMENT ON COLUMN report_fill_service_by_industry.counterparty_year_service_count IS '交易对手方：当年提供次数（industry_code.counterparty）';
COMMENT ON COLUMN report_fill_service_by_industry.information_subject_itself_year_service_count IS '信息主体自身：当年提供次数（industry_code.information_subject_itself）';
COMMENT ON COLUMN report_fill_service_by_industry.other_year_service_count IS '其他（数量多请附说明）：当年提供次数（industry_code.other）';

COMMENT ON COLUMN report_fill_service_by_industry.user_org_total_government IS '政府-信息使用者机构总累计数';
COMMENT ON COLUMN report_fill_service_by_industry.user_org_current_government IS '政府-当前使用服务的信息使用者机构数';

-- ============================
-- report_fill_product_stat（提供的征信产品/服务次数：固定字段，1:1）
-- 字段与字典一一对应（按 sort_order 绑定），不再存 product_type / product_name 的可扩展 code。
-- ============================
DROP TABLE IF EXISTS report_fill_product_stat;
CREATE TABLE report_fill_product_stat (
  id                      VARCHAR(32) NOT NULL DEFAULT replace(gen_random_uuid()::text, '-', ''),
  record_id                VARCHAR(32) NOT NULL,

  -- 信用报告产品名称（credit_product_type:report_type；credit_pro:report）
  report_year_count        NUMERIC(18, 0),
  -- 信用分产品名称（credit_product_type:credit_type；credit_pro:credit）
  credit_year_count        NUMERIC(18, 0),
  -- 反欺诈产品名称（credit_product_type:anti_type；credit_pro:anti）
  anti_year_count          NUMERIC(18, 0),

  create_time              TIMESTAMP,
  update_time              TIMESTAMP,
  creator                  VARCHAR(64),
  updater                  VARCHAR(64),
  deleted                  int2 NOT NULL DEFAULT 0
);

ALTER TABLE report_fill_product_stat
  ADD CONSTRAINT pk_report_fill_product_stat PRIMARY KEY (id);
CREATE UNIQUE INDEX uk_report_fill_product_stat_record_id
  ON report_fill_product_stat (record_id, deleted);
CREATE INDEX idx_report_fill_product_stat_record_id
  ON report_fill_product_stat (record_id);

COMMENT ON TABLE report_fill_product_stat IS '报表填报-提供的征信产品/服务次数（固定字段，1:1，UUID主键）';
COMMENT ON COLUMN report_fill_product_stat.record_id IS '主记录id（= report_fill_basic_info.id）';
COMMENT ON COLUMN report_fill_product_stat.report_year_count IS '企业信用报告、招投标报告：当年提供次数（字典一一对应：report_type/report）';
COMMENT ON COLUMN report_fill_product_stat.credit_year_count IS '信用分：当年提供次数（字典一一对应：credit_type/credit）';
COMMENT ON COLUMN report_fill_product_stat.anti_year_count IS '反欺诈：当年提供次数（字典一一对应：anti_type/anti）';

