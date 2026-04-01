-- ------------------------------
-- report_fill_info_user_org_item：信息使用者机构明细（当前使用服务=1）
-- 来源：wmt-module-credit-report/docs/数据销售客户2026.3.2.xlsx（sheet：当前）
-- 生成时间：2026-03-30 16:30:12
-- ------------------------------

-- 银行 (bank)
UPDATE report_fill_info_user_org_item t
SET is_current_service = 1,
    sort_no = v.sort_no::varchar(32),
    updater = '1',
    update_time = CURRENT_TIMESTAMP,
    deleted = 0
FROM (
  VALUES
    ('合肥科农行', 1),
    ('浙江网商银行', 2),
    ('工商银行', 3),
    ('上海银行', 4),
    ('微众银行', 5),
    ('农业银行', 6),
    ('嘉兴银行', 7),
    ('邮储银行', 8),
    ('徽商银行', 9),
    ('中国银行', 10),
    ('新安', 11),
    ('九江银行合肥分行', 12),
    ('民生银行', 13),
    ('浙江泰隆商业银行股份有限公司', 14),
    ('恒丰银行', 15),
    ('无为徽银村镇银行', 16),
    ('交通银行', 17),
    ('浙商银行', 18),
    ('辽宁振兴银行', 19),
    ('广发银行', 20),
    ('四川新网银行股份有限公司', 21),
    ('武汉众邦银行股份有限公司', 22),
    ('平安银行', 23),
    ('珠海华润银行股份有限公司', 24),
    ('昆仑银行股份有限公司', 25)
) AS v(org_name, sort_no)
WHERE t.org_name = v.org_name
  AND t.industry_code = 'bank'
  AND t.deleted = 0;

INSERT INTO report_fill_info_user_org_item
  (org_name, industry_code, is_current_service, sort_no, create_time, update_time, creator, updater, deleted)
SELECT
  v.org_name,
  'bank' AS industry_code,
  1 AS is_current_service,
  v.sort_no::varchar(32) AS sort_no,
  CURRENT_TIMESTAMP AS create_time,
  CURRENT_TIMESTAMP AS update_time,
  '1' AS creator,
  '1' AS updater,
  0 AS deleted
FROM (
  VALUES
    ('合肥科农行', 1),
    ('浙江网商银行', 2),
    ('工商银行', 3),
    ('上海银行', 4),
    ('微众银行', 5),
    ('农业银行', 6),
    ('嘉兴银行', 7),
    ('邮储银行', 8),
    ('徽商银行', 9),
    ('中国银行', 10),
    ('新安', 11),
    ('九江银行合肥分行', 12),
    ('民生银行', 13),
    ('浙江泰隆商业银行股份有限公司', 14),
    ('恒丰银行', 15),
    ('无为徽银村镇银行', 16),
    ('交通银行', 17),
    ('浙商银行', 18),
    ('辽宁振兴银行', 19),
    ('广发银行', 20),
    ('四川新网银行股份有限公司', 21),
    ('武汉众邦银行股份有限公司', 22),
    ('平安银行', 23),
    ('珠海华润银行股份有限公司', 24),
    ('昆仑银行股份有限公司', 25)
) AS v(org_name, sort_no)
WHERE NOT EXISTS (SELECT 1 FROM report_fill_info_user_org_item t
                  WHERE t.org_name = v.org_name
                    AND t.industry_code = 'bank'
                    AND t.deleted = 0);

-- 融资租赁及担保类公司 (financial_leasing_guarantee)
UPDATE report_fill_info_user_org_item t
SET is_current_service = 1,
    sort_no = v.sort_no::varchar(32),
    updater = '1',
    update_time = CURRENT_TIMESTAMP,
    deleted = 0
FROM (
  VALUES
    ('福州奇富融担', 1),
    ('安徽中源融资担保', 2),
    ('永赢金租', 3),
    ('安徽省信用融资担保集团有限公司', 4),
    ('合肥高新融资担保', 5),
    ('维仕融资担保有限公司', 6)
) AS v(org_name, sort_no)
WHERE t.org_name = v.org_name
  AND t.industry_code = 'financial_leasing_guarantee'
  AND t.deleted = 0;

INSERT INTO report_fill_info_user_org_item
  (org_name, industry_code, is_current_service, sort_no, create_time, update_time, creator, updater, deleted)
SELECT
  v.org_name,
  'financial_leasing_guarantee' AS industry_code,
  1 AS is_current_service,
  v.sort_no::varchar(32) AS sort_no,
  CURRENT_TIMESTAMP AS create_time,
  CURRENT_TIMESTAMP AS update_time,
  '1' AS creator,
  '1' AS updater,
  0 AS deleted
FROM (
  VALUES
    ('福州奇富融担', 1),
    ('安徽中源融资担保', 2),
    ('永赢金租', 3),
    ('安徽省信用融资担保集团有限公司', 4),
    ('合肥高新融资担保', 5),
    ('维仕融资担保有限公司', 6)
) AS v(org_name, sort_no)
WHERE NOT EXISTS (SELECT 1 FROM report_fill_info_user_org_item t
                  WHERE t.org_name = v.org_name
                    AND t.industry_code = 'financial_leasing_guarantee'
                    AND t.deleted = 0);

-- 其他征信机构 (other_credit_agency)
UPDATE report_fill_info_user_org_item t
SET is_current_service = 1,
    sort_no = v.sort_no::varchar(32),
    updater = '1',
    update_time = CURRENT_TIMESTAMP,
    deleted = 0
FROM (
  VALUES
    ('江苏联合征信', 1),
    ('上海生腾数据科技有限公司', 2),
    ('北京华道征信有限公司', 3),
    ('上海恒先君展企业信用征信服务有限公司', 4),
    ('海南省征信有限公司', 5),
    ('新疆征信有限责任公司', 6),
    ('中青信用管理有限公司', 7),
    ('中博信征信有限公司', 8)
) AS v(org_name, sort_no)
WHERE t.org_name = v.org_name
  AND t.industry_code = 'other_credit_agency'
  AND t.deleted = 0;

INSERT INTO report_fill_info_user_org_item
  (org_name, industry_code, is_current_service, sort_no, create_time, update_time, creator, updater, deleted)
SELECT
  v.org_name,
  'other_credit_agency' AS industry_code,
  1 AS is_current_service,
  v.sort_no::varchar(32) AS sort_no,
  CURRENT_TIMESTAMP AS create_time,
  CURRENT_TIMESTAMP AS update_time,
  '1' AS creator,
  '1' AS updater,
  0 AS deleted
FROM (
  VALUES
    ('江苏联合征信', 1),
    ('上海生腾数据科技有限公司', 2),
    ('北京华道征信有限公司', 3),
    ('上海恒先君展企业信用征信服务有限公司', 4),
    ('海南省征信有限公司', 5),
    ('新疆征信有限责任公司', 6),
    ('中青信用管理有限公司', 7),
    ('中博信征信有限公司', 8)
) AS v(org_name, sort_no)
WHERE NOT EXISTS (SELECT 1 FROM report_fill_info_user_org_item t
                  WHERE t.org_name = v.org_name
                    AND t.industry_code = 'other_credit_agency'
                    AND t.deleted = 0);

-- 保险 (insurance)
UPDATE report_fill_info_user_org_item t
SET is_current_service = 1,
    sort_no = v.sort_no::varchar(32),
    updater = '1',
    update_time = CURRENT_TIMESTAMP,
    deleted = 0
FROM (
  VALUES
    ('国元农业保险股份有限公司', 1)
) AS v(org_name, sort_no)
WHERE t.org_name = v.org_name
  AND t.industry_code = 'insurance'
  AND t.deleted = 0;

INSERT INTO report_fill_info_user_org_item
  (org_name, industry_code, is_current_service, sort_no, create_time, update_time, creator, updater, deleted)
SELECT
  v.org_name,
  'insurance' AS industry_code,
  1 AS is_current_service,
  v.sort_no::varchar(32) AS sort_no,
  CURRENT_TIMESTAMP AS create_time,
  CURRENT_TIMESTAMP AS update_time,
  '1' AS creator,
  '1' AS updater,
  0 AS deleted
FROM (
  VALUES
    ('国元农业保险股份有限公司', 1)
) AS v(org_name, sort_no)
WHERE NOT EXISTS (SELECT 1 FROM report_fill_info_user_org_item t
                  WHERE t.org_name = v.org_name
                    AND t.industry_code = 'insurance'
                    AND t.deleted = 0);

-- 数据服务商 (data_service_provider)
UPDATE report_fill_info_user_org_item t
SET is_current_service = 1,
    sort_no = v.sort_no::varchar(32),
    updater = '1',
    update_time = CURRENT_TIMESTAMP,
    deleted = 0
FROM (
  VALUES
    ('智慧芽', 1),
    ('证通股份有限公司', 2),
    ('贵州数据宝网络科技有限公司', 3)
) AS v(org_name, sort_no)
WHERE t.org_name = v.org_name
  AND t.industry_code = 'data_service_provider'
  AND t.deleted = 0;

INSERT INTO report_fill_info_user_org_item
  (org_name, industry_code, is_current_service, sort_no, create_time, update_time, creator, updater, deleted)
SELECT
  v.org_name,
  'data_service_provider' AS industry_code,
  1 AS is_current_service,
  v.sort_no::varchar(32) AS sort_no,
  CURRENT_TIMESTAMP AS create_time,
  CURRENT_TIMESTAMP AS update_time,
  '1' AS creator,
  '1' AS updater,
  0 AS deleted
FROM (
  VALUES
    ('智慧芽', 1),
    ('证通股份有限公司', 2),
    ('贵州数据宝网络科技有限公司', 3)
) AS v(org_name, sort_no)
WHERE NOT EXISTS (SELECT 1 FROM report_fill_info_user_org_item t
                  WHERE t.org_name = v.org_name
                    AND t.industry_code = 'data_service_provider'
                    AND t.deleted = 0);

-- 电子商务平台 (ecommerce_platform)
UPDATE report_fill_info_user_org_item t
SET is_current_service = 1,
    sort_no = v.sort_no::varchar(32),
    updater = '1',
    update_time = CURRENT_TIMESTAMP,
    deleted = 0
FROM (
  VALUES
    ('安徽安天利信工程管理股份有限公司', 1)
) AS v(org_name, sort_no)
WHERE t.org_name = v.org_name
  AND t.industry_code = 'ecommerce_platform'
  AND t.deleted = 0;

INSERT INTO report_fill_info_user_org_item
  (org_name, industry_code, is_current_service, sort_no, create_time, update_time, creator, updater, deleted)
SELECT
  v.org_name,
  'ecommerce_platform' AS industry_code,
  1 AS is_current_service,
  v.sort_no::varchar(32) AS sort_no,
  CURRENT_TIMESTAMP AS create_time,
  CURRENT_TIMESTAMP AS update_time,
  '1' AS creator,
  '1' AS updater,
  0 AS deleted
FROM (
  VALUES
    ('安徽安天利信工程管理股份有限公司', 1)
) AS v(org_name, sort_no)
WHERE NOT EXISTS (SELECT 1 FROM report_fill_info_user_org_item t
                  WHERE t.org_name = v.org_name
                    AND t.industry_code = 'ecommerce_platform'
                    AND t.deleted = 0);

