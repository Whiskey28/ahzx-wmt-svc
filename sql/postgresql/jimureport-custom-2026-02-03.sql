-- ============================
-- 积木报表数据库表定制化配置SQL (PostgreSQL v15)
-- 创建时间: 2026-02-03
-- 说明:
--   1. 信息主体类型字典配置（info_source_type）：23个字典项
--   2. 信息来源情况字典配置（info_source_status）：21个字典项
-- ============================

-- ----------------------------
-- 1. 创建字典类型：信息主体类型
-- ----------------------------
-- 如果字典类型已存在，先删除（可选，根据实际情况决定）
-- DELETE FROM jimu_dict WHERE dict_code = 'info_source_type';

-- 插入字典类型（使用 ON CONFLICT 处理重复插入）
INSERT INTO jimu_dict (id, dict_name, dict_code, description, del_flag, create_by, create_time, update_by, update_time, type, tenant_id)
VALUES ('1178553094177271808', '产品与服务提供情况', 'industry_code', '产品与服务提供情况', 0, '1', CURRENT_TIMESTAMP, NULL, NULL, 0, NULL)
ON CONFLICT (dict_code) DO UPDATE
SET
  dict_name = EXCLUDED.dict_name,
  description = EXCLUDED.description,
  update_time = CURRENT_TIMESTAMP;

-- ----------------------------
-- 2. 插入字典项：信息主体类型字典项
-- ----------------------------
-- 如果字典项已存在，先删除（可选，根据实际情况决定）
-- DELETE FROM jimu_dict_item WHERE dict_id = '1178553094177271808';

-- 插入字典项（使用 ON CONFLICT 处理重复插入）
INSERT INTO jimu_dict_item (id, dict_id, item_text, item_value, description, sort_order, status, create_by, create_time, update_by, update_time) VALUES
('1178553176083640320', '1178553094177271808', '银行', 'bank', NULL, 1, 1, '1', CURRENT_TIMESTAMP, NULL, NULL),
('1178553529353089024', '1178553094177271808', '证券', 'security', NULL, 2, 1, '1', CURRENT_TIMESTAMP, NULL, NULL),
('1178553600000000001', '1178553094177271808', '保险', 'insurance', NULL, 3, 1, '1', CURRENT_TIMESTAMP, NULL, NULL),
('1178553600000000002', '1178553094177271808', '信托', 'trust', NULL, 4, 1, '1', CURRENT_TIMESTAMP, NULL, NULL),
('1178553600000000003', '1178553094177271808', 'P2P网络借贷中介', 'p2p_lending', NULL, 5, 1, '1', CURRENT_TIMESTAMP, NULL, NULL),
('1178553600000000004', '1178553094177271808', '支付机构', 'payment_institution', NULL, 6, 1, '1', CURRENT_TIMESTAMP, NULL, NULL),
('1178553600000000005', '1178553094177271808', '融资租赁及担保类公司', 'financial_leasing_guarantee', NULL, 7, 1, '1', CURRENT_TIMESTAMP, NULL, NULL),
('1178553600000000006', '1178553094177271808', '小额贷款公司', 'micro_loan_company', NULL, 8, 1, '1', CURRENT_TIMESTAMP, NULL, NULL),
('1178553600000000007', '1178553094177271808', '消费金融公司', 'consumer_finance_company', NULL, 9, 1, '1', CURRENT_TIMESTAMP, NULL, NULL),
('1178553600000000008', '1178553094177271808', '资产管理公司', 'asset_management_company', NULL, 10, 1, '1', CURRENT_TIMESTAMP, NULL, NULL),
('1178553600000000009', '1178553094177271808', '汽车金融公司', 'auto_finance_company', NULL, 11, 1, '1', CURRENT_TIMESTAMP, NULL, NULL),
('1178553600000000010', '1178553094177271808', '商业保理公司', 'commercial_factoring_company', NULL, 12, 1, '1', CURRENT_TIMESTAMP, NULL, NULL),
('1178553600000000011', '1178553094177271808', '政府', 'government', NULL, 13, 1, '1', CURRENT_TIMESTAMP, NULL, NULL),
('1178553600000000012', '1178553094177271808', '公用事业单位(水、电、气、通讯等)', 'public_utilities', NULL, 14, 1, '1', CURRENT_TIMESTAMP, NULL, NULL),
('1178553600000000013', '1178553094177271808', '行业协会', 'industry_association', NULL, 15, 1, '1', CURRENT_TIMESTAMP, NULL, NULL),
('1178553600000000014', '1178553094177271808', '法院', 'court', NULL, 16, 1, '1', CURRENT_TIMESTAMP, NULL, NULL),
('1178553600000000015', '1178553094177271808', '电子商务平台', 'ecommerce_platform', NULL, 17, 1, '1', CURRENT_TIMESTAMP, NULL, NULL),
('1178553600000000016', '1178553094177271808', '涉农企业', 'agricultural_enterprise', NULL, 18, 1, '1', CURRENT_TIMESTAMP, NULL, NULL),
('1178553600000000017', '1178553094177271808', '其他征信机构', 'other_credit_agency', NULL, 19, 1, '1', CURRENT_TIMESTAMP, NULL, NULL),
('1178553600000000018', '1178553094177271808', '数据服务商', 'data_service_provider', NULL, 20, 1, '1', CURRENT_TIMESTAMP, NULL, NULL),
('1178553600000000019', '1178553094177271808', '交易对手方', 'counterparty', NULL, 21, 1, '1', CURRENT_TIMESTAMP, NULL, NULL),
('1178553600000000020', '1178553094177271808', '信息主体自身', 'information_subject_itself', NULL, 22, 1, '1', CURRENT_TIMESTAMP, NULL, NULL),
('1178553600000000021', '1178553094177271808', '其他(数量多请附说明)', 'other', NULL, 23, 1, '1', CURRENT_TIMESTAMP, NULL, NULL)
ON CONFLICT (id) DO UPDATE
SET
  item_text = EXCLUDED.item_text,
  item_value = EXCLUDED.item_value,
  sort_order = EXCLUDED.sort_order,
  status = EXCLUDED.status,
  update_time = CURRENT_TIMESTAMP;

-- ----------------------------
-- 3. 创建字典类型：信息来源情况
-- ----------------------------
-- 如果字典类型已存在，先删除（可选，根据实际情况决定）
-- DELETE FROM jimu_dict WHERE dict_code = 'info_source_status';

-- 插入字典类型（使用 ON CONFLICT 处理重复插入）
INSERT INTO jimu_dict (id, dict_name, dict_code, description, del_flag, create_by, create_time, update_by, update_time, type, tenant_id)
VALUES ('1178553700000000001', '信息来源情况', 'info_source_status', '信息来源情况字典，用于标识信息来源的组织类型和情况', 0, '1', CURRENT_TIMESTAMP, NULL, NULL, 0, NULL)
ON CONFLICT (dict_code) DO UPDATE
SET
  dict_name = EXCLUDED.dict_name,
  description = EXCLUDED.description,
  update_time = CURRENT_TIMESTAMP;

-- ----------------------------
-- 4. 插入字典项：信息来源情况字典项
-- ----------------------------
-- 如果字典项已存在，先删除（可选，根据实际情况决定）
-- DELETE FROM jimu_dict_item WHERE dict_id = '1178553700000000001';

-- 插入字典项（共21项，不包括"金融、类金融机构"分类标题和"备注"）
INSERT INTO jimu_dict_item (id, dict_id, item_text, item_value, description, sort_order, status, create_by, create_time, update_by, update_time) VALUES
('1178553700000000101', '1178553700000000001', '银行', 'bank', NULL, 1, 1, '1', CURRENT_TIMESTAMP, NULL, NULL),
('1178553700000000102', '1178553700000000001', '证券', 'security', NULL, 2, 1, '1', CURRENT_TIMESTAMP, NULL, NULL),
('1178553700000000103', '1178553700000000001', '保险', 'insurance', NULL, 3, 1, '1', CURRENT_TIMESTAMP, NULL, NULL),
('1178553700000000104', '1178553700000000001', '信托', 'trust', NULL, 4, 1, '1', CURRENT_TIMESTAMP, NULL, NULL),
('1178553700000000105', '1178553700000000001', 'P2P网络借贷中介', 'p2p_lending', NULL, 5, 1, '1', CURRENT_TIMESTAMP, NULL, NULL),
('1178553700000000106', '1178553700000000001', '支付机构', 'payment_institution', NULL, 6, 1, '1', CURRENT_TIMESTAMP, NULL, NULL),
('1178553700000000107', '1178553700000000001', '融资租赁及担保类公司', 'financial_leasing_guarantee', NULL, 7, 1, '1', CURRENT_TIMESTAMP, NULL, NULL),
('1178553700000000108', '1178553700000000001', '小额贷款公司', 'micro_loan_company', NULL, 8, 1, '1', CURRENT_TIMESTAMP, NULL, NULL),
('1178553700000000109', '1178553700000000001', '消费金融公司', 'consumer_finance_company', NULL, 9, 1, '1', CURRENT_TIMESTAMP, NULL, NULL),
('1178553700000000110', '1178553700000000001', '资产管理公司', 'asset_management_company', NULL, 10, 1, '1', CURRENT_TIMESTAMP, NULL, NULL),
('1178553700000000111', '1178553700000000001', '汽车金融公司', 'auto_finance_company', NULL, 11, 1, '1', CURRENT_TIMESTAMP, NULL, NULL),
('1178553700000000112', '1178553700000000001', '商业保理公司', 'commercial_factoring_company', NULL, 12, 1, '1', CURRENT_TIMESTAMP, NULL, NULL),
('1178553700000000113', '1178553700000000001', '政府', 'government', NULL, 13, 1, '1', CURRENT_TIMESTAMP, NULL, NULL),
('1178553700000000114', '1178553700000000001', '公用事业单位(水、电、气、通讯等)', 'public_utilities', NULL, 14, 1, '1', CURRENT_TIMESTAMP, NULL, NULL),
('1178553700000000115', '1178553700000000001', '行业协会', 'industry_association', NULL, 15, 1, '1', CURRENT_TIMESTAMP, NULL, NULL),
('1178553700000000116', '1178553700000000001', '法院', 'court', NULL, 16, 1, '1', CURRENT_TIMESTAMP, NULL, NULL),
('1178553700000000117', '1178553700000000001', '电子商务平台', 'ecommerce_platform', NULL, 17, 1, '1', CURRENT_TIMESTAMP, NULL, NULL),
('1178553700000000118', '1178553700000000001', '其他征信机构', 'other_credit_agency', NULL, 18, 1, '1', CURRENT_TIMESTAMP, NULL, NULL),
('1178553700000000119', '1178553700000000001', '数据服务商', 'data_service_provider', NULL, 19, 1, '1', CURRENT_TIMESTAMP, NULL, NULL),
('1178553700000000120', '1178553700000000001', '交易对手方', 'counterparty', NULL, 20, 1, '1', CURRENT_TIMESTAMP, NULL, NULL),
('1178553700000000121', '1178553700000000001', '其他(不含信息主体自身提供,数量多请附说明)', 'other', NULL, 21, 1, '1', CURRENT_TIMESTAMP, NULL, NULL)
ON CONFLICT (id) DO UPDATE
SET
  item_text = EXCLUDED.item_text,
  item_value = EXCLUDED.item_value,
  sort_order = EXCLUDED.sort_order,
  status = EXCLUDED.status,
  update_time = CURRENT_TIMESTAMP;

-- ============================
-- SQL执行完成
-- 说明：
-- 1. 信息主体类型字典（info_source_type）：包含23个字典项，包括"信息主体自身"
-- 2. 信息来源情况字典（info_source_status）：包含21个字典项，不包括"信息主体自身"，"其他"的描述也不同
-- ============================
