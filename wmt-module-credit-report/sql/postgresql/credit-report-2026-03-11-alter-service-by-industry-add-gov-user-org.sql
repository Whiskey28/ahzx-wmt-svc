-- 信用报送（增量变更汇总）- PostgreSQL - 2026-03-11
-- 说明：本文件用于汇总本次升级涉及的所有表结构增量变更（可在已有库上重复执行）。

-- ============================
-- 1) report_fill_service_by_industry：增加政府机构统计字段
-- ============================

ALTER TABLE report_fill_service_by_industry
  ADD COLUMN IF NOT EXISTS user_org_total_government NUMERIC(18, 0);

ALTER TABLE report_fill_service_by_industry
  ADD COLUMN IF NOT EXISTS user_org_current_government NUMERIC(18, 0);

COMMENT ON COLUMN report_fill_service_by_industry.user_org_total_government
  IS '政府-信息使用者机构总累计数';

COMMENT ON COLUMN report_fill_service_by_industry.user_org_current_government
  IS '政府-当前使用服务的信息使用者机构数';


-- ============================
-- 2) report_fill_info_user_org_item / report_fill_info_user_gov_item：字段约束调整（NOT NULL）
-- ============================
-- 注意：如果历史数据存在 NULL，这里先做数据兜底，再设置 NOT NULL。
-- 可按实际业务需要调整兜底值：
-- - industry_code: NULL -> 'other'
-- - sort_no: NULL -> '0'

UPDATE report_fill_info_user_org_item
SET industry_code = COALESCE(industry_code, 'other'),
    sort_no = COALESCE(sort_no, '0')
WHERE industry_code IS NULL OR sort_no IS NULL;

ALTER TABLE report_fill_info_user_org_item
  ALTER COLUMN industry_code SET NOT NULL;

ALTER TABLE report_fill_info_user_org_item
  ALTER COLUMN sort_no SET NOT NULL;

UPDATE report_fill_info_user_gov_item
SET sort_no = COALESCE(sort_no, '0')
WHERE sort_no IS NULL;

ALTER TABLE report_fill_info_user_gov_item
  ALTER COLUMN sort_no SET NOT NULL;


-- ============================
-- 3) report_fill_biz_stat_credit_build：补充信息使用者机构统计字段（按行业展开）
-- ============================
ALTER TABLE report_fill_biz_stat_credit_build
  ADD COLUMN IF NOT EXISTS user_org_total_bank NUMERIC(18, 0),
  ADD COLUMN IF NOT EXISTS user_org_current_bank NUMERIC(18, 0),
  ADD COLUMN IF NOT EXISTS user_org_total_securities NUMERIC(18, 0),
  ADD COLUMN IF NOT EXISTS user_org_current_securities NUMERIC(18, 0),
  ADD COLUMN IF NOT EXISTS user_org_total_insurance NUMERIC(18, 0),
  ADD COLUMN IF NOT EXISTS user_org_current_insurance NUMERIC(18, 0),
  ADD COLUMN IF NOT EXISTS user_org_total_trust NUMERIC(18, 0),
  ADD COLUMN IF NOT EXISTS user_org_current_trust NUMERIC(18, 0),
  ADD COLUMN IF NOT EXISTS user_org_total_p2p_lending_intermediary NUMERIC(18, 0),
  ADD COLUMN IF NOT EXISTS user_org_current_p2p_lending_intermediary NUMERIC(18, 0),
  ADD COLUMN IF NOT EXISTS user_org_total_payment_institution NUMERIC(18, 0),
  ADD COLUMN IF NOT EXISTS user_org_current_payment_institution NUMERIC(18, 0),
  ADD COLUMN IF NOT EXISTS user_org_total_financing_leasing_guarantee NUMERIC(18, 0),
  ADD COLUMN IF NOT EXISTS user_org_current_financing_leasing_guarantee NUMERIC(18, 0),
  ADD COLUMN IF NOT EXISTS user_org_total_micro_loan_company NUMERIC(18, 0),
  ADD COLUMN IF NOT EXISTS user_org_current_micro_loan_company NUMERIC(18, 0),
  ADD COLUMN IF NOT EXISTS user_org_total_consumer_finance_company NUMERIC(18, 0),
  ADD COLUMN IF NOT EXISTS user_org_current_consumer_finance_company NUMERIC(18, 0),
  ADD COLUMN IF NOT EXISTS user_org_total_asset_management_company NUMERIC(18, 0),
  ADD COLUMN IF NOT EXISTS user_org_current_asset_management_company NUMERIC(18, 0),
  ADD COLUMN IF NOT EXISTS user_org_total_auto_finance_company NUMERIC(18, 0),
  ADD COLUMN IF NOT EXISTS user_org_current_auto_finance_company NUMERIC(18, 0),
  ADD COLUMN IF NOT EXISTS user_org_total_commercial_factoring_company NUMERIC(18, 0),
  ADD COLUMN IF NOT EXISTS user_org_current_commercial_factoring_company NUMERIC(18, 0),
  ADD COLUMN IF NOT EXISTS user_org_total_government NUMERIC(18, 0),
  ADD COLUMN IF NOT EXISTS user_org_current_government NUMERIC(18, 0),
  ADD COLUMN IF NOT EXISTS user_org_total_public_utility NUMERIC(18, 0),
  ADD COLUMN IF NOT EXISTS user_org_current_public_utility NUMERIC(18, 0),
  ADD COLUMN IF NOT EXISTS user_org_total_industry_association NUMERIC(18, 0),
  ADD COLUMN IF NOT EXISTS user_org_current_industry_association NUMERIC(18, 0),
  ADD COLUMN IF NOT EXISTS user_org_total_court NUMERIC(18, 0),
  ADD COLUMN IF NOT EXISTS user_org_current_court NUMERIC(18, 0),
  ADD COLUMN IF NOT EXISTS user_org_total_ecommerce_platform NUMERIC(18, 0),
  ADD COLUMN IF NOT EXISTS user_org_current_ecommerce_platform NUMERIC(18, 0),
  ADD COLUMN IF NOT EXISTS user_org_total_agriculture_related_enterprise NUMERIC(18, 0),
  ADD COLUMN IF NOT EXISTS user_org_current_agriculture_related_enterprise NUMERIC(18, 0),
  ADD COLUMN IF NOT EXISTS user_org_total_other_credit_reporting_agency NUMERIC(18, 0),
  ADD COLUMN IF NOT EXISTS user_org_current_other_credit_reporting_agency NUMERIC(18, 0),
  ADD COLUMN IF NOT EXISTS user_org_total_data_service_provider NUMERIC(18, 0),
  ADD COLUMN IF NOT EXISTS user_org_current_data_service_provider NUMERIC(18, 0),
  ADD COLUMN IF NOT EXISTS user_org_total_trading_counterparty NUMERIC(18, 0),
  ADD COLUMN IF NOT EXISTS user_org_current_trading_counterparty NUMERIC(18, 0),
  ADD COLUMN IF NOT EXISTS user_org_total_information_subject_self NUMERIC(18, 0),
  ADD COLUMN IF NOT EXISTS user_org_current_information_subject_self NUMERIC(18, 0),
  ADD COLUMN IF NOT EXISTS user_org_total_other NUMERIC(18, 0),
  ADD COLUMN IF NOT EXISTS user_org_current_other NUMERIC(18, 0);

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

