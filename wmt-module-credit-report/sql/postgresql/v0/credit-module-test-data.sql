-- ============================================
-- 征信模块测试数据脚本
-- 数据库：PostgreSQL v15
-- 日期：2025-01-XX
-- 说明：准备测试数据，支持月报和季报生成测试
-- 部门：财务部115，市场部116，技术部117
-- 提交人：1
-- ============================================

BEGIN;

-- ============================================
-- 1. 清理旧测试数据（可选，取消注释以启用）
-- ============================================
-- DELETE FROM credit_summary_report WHERE report_period IN ('2025-01', '2025-02', '2025-03', '2025-Q1', '2024-Q4', '2024-10', '2024-11', '2024-12');
-- DELETE FROM credit_form_data WHERE report_period IN ('2025-01', '2025-02', '2025-03', '2024-10', '2024-11', '2024-12');
-- DELETE FROM credit_calculation_rule WHERE report_type IN ('MONTHLY', 'QUARTERLY');
-- DELETE FROM credit_field_config WHERE dept_id IN (0, 115, 116, 117);

-- ============================================
-- 2. 插入字段配置
-- ============================================

-- 2.1 通用字段配置（dept_id = 0）
INSERT INTO credit_field_config (id, dept_id, field_code, field_name, field_type, required, display_order, status, creator, create_time, updater, update_time, deleted, display_config)
VALUES
-- 项目1：数据库收录小微企业户数相关字段
(nextval('credit_field_config_seq'), 0, 'small_micro_enterprises_count', '小微企业户数', 'NUMBER', 1, 1, 1, 'system', CURRENT_TIMESTAMP, 'system', CURRENT_TIMESTAMP, 0, '{"groupName": "数据库收录小微企业户数", "groupOrder": 1}'::jsonb),
-- 项目2：为放贷机构提供小微企业征信服务次数相关字段
(nextval('credit_field_config_seq'), 0, 'credit_service_calls', '征信服务调用总次数', 'NUMBER', 1, 2, 1, 'system', CURRENT_TIMESTAMP, 'system', CURRENT_TIMESTAMP, 0, '{"groupName": "为放贷机构提供小微企业征信服务次数", "groupOrder": 2}'::jsonb),
-- 项目3：小微企业申请贷款户数相关字段
(nextval('credit_field_config_seq'), 0, 'loan_applications_count', '申请进件企业数', 'NUMBER', 1, 3, 1, 'system', CURRENT_TIMESTAMP, 'system', CURRENT_TIMESTAMP, 0, '{"groupName": "小微企业申请贷款户数", "groupOrder": 3}'::jsonb),
-- 项目4：小微企业获得贷款户数相关字段
(nextval('credit_field_config_seq'), 0, 'loan_approval_rate', '企业贷款申请通过率', 'DECIMAL', 1, 4, 1, 'system', CURRENT_TIMESTAMP, 'system', CURRENT_TIMESTAMP, 0, '{"groupName": "小微企业获得贷款户数", "groupOrder": 4}'::jsonb),
-- 项目5：小微企业获得贷款金额相关字段
(nextval('credit_field_config_seq'), 0, 'avg_loan_amount', '已通过的每一户的平均放款', 'DECIMAL', 1, 5, 1, 'system', CURRENT_TIMESTAMP, 'system', CURRENT_TIMESTAMP, 0, '{"groupName": "小微企业获得贷款金额", "groupOrder": 5}'::jsonb);

-- 2.2 部门字段配置（每个部门都有相同的字段）
-- 财务部115
INSERT INTO credit_field_config (id, dept_id, field_code, field_name, field_type, required, display_order, status, creator, create_time, updater, update_time, deleted, display_config)
SELECT 
    nextval('credit_field_config_seq'), 
    115, 
    field_code, 
    field_name, 
    field_type, 
    required, 
    display_order, 
    status, 
    creator, 
    create_time, 
    updater, 
    update_time, 
    deleted,
    display_config
FROM credit_field_config WHERE dept_id = 0;

-- 市场部116
INSERT INTO credit_field_config (id, dept_id, field_code, field_name, field_type, required, display_order, status, creator, create_time, updater, update_time, deleted, display_config)
SELECT 
    nextval('credit_field_config_seq'), 
    116, 
    field_code, 
    field_name, 
    field_type, 
    required, 
    display_order, 
    status, 
    creator, 
    create_time, 
    updater, 
    update_time, 
    deleted,
    display_config
FROM credit_field_config WHERE dept_id = 0;

-- 技术部117
INSERT INTO credit_field_config (id, dept_id, field_code, field_name, field_type, required, display_order, status, creator, create_time, updater, update_time, deleted, display_config)
SELECT 
    nextval('credit_field_config_seq'), 
    117, 
    field_code, 
    field_name, 
    field_type, 
    required, 
    display_order, 
    status, 
    creator, 
    create_time, 
    updater, 
    update_time, 
    deleted,
    display_config
FROM credit_field_config WHERE dept_id = 0;

-- ============================================
-- 3. 插入表单数据（3个部门 × 6个月 = 18条记录）
-- ============================================

-- 3.1 2025-01月表单数据
INSERT INTO credit_form_data (id, dept_id, report_period, report_type, form_data, status, submit_user_id, submit_time, creator, create_time, updater, update_time, deleted)
VALUES
-- 财务部115
(nextval('credit_form_data_seq'), 115, '2025-01', 'MONTHLY', 
 '{"small_micro_enterprises_count": 125000, "credit_service_calls": 850000, "loan_applications_count": 3500, "loan_approval_rate": 0.75, "avg_loan_amount": 150000}'::jsonb,
 1, 1, CURRENT_TIMESTAMP, 'system', CURRENT_TIMESTAMP, 'system', CURRENT_TIMESTAMP, 0),
-- 市场部116
(nextval('credit_form_data_seq'), 116, '2025-01', 'MONTHLY',
 '{"small_micro_enterprises_count": 185000, "credit_service_calls": 1200000, "loan_applications_count": 5200, "loan_approval_rate": 0.78, "avg_loan_amount": 180000}'::jsonb,
 1, 1, CURRENT_TIMESTAMP, 'system', CURRENT_TIMESTAMP, 'system', CURRENT_TIMESTAMP, 0),
-- 技术部117
(nextval('credit_form_data_seq'), 117, '2025-01', 'MONTHLY',
 '{"small_micro_enterprises_count": 95000, "credit_service_calls": 650000, "loan_applications_count": 2800, "loan_approval_rate": 0.72, "avg_loan_amount": 135000}'::jsonb,
 1, 1, CURRENT_TIMESTAMP, 'system', CURRENT_TIMESTAMP, 'system', CURRENT_TIMESTAMP, 0),

-- 3.2 2025-02月表单数据
-- 财务部115
(nextval('credit_form_data_seq'), 115, '2025-02', 'MONTHLY',
 '{"small_micro_enterprises_count": 128000, "credit_service_calls": 880000, "loan_applications_count": 3600, "loan_approval_rate": 0.76, "avg_loan_amount": 152000}'::jsonb,
 1, 1, CURRENT_TIMESTAMP, 'system', CURRENT_TIMESTAMP, 'system', CURRENT_TIMESTAMP, 0),
-- 市场部116
(nextval('credit_form_data_seq'), 116, '2025-02', 'MONTHLY',
 '{"small_micro_enterprises_count": 188000, "credit_service_calls": 1250000, "loan_applications_count": 5400, "loan_approval_rate": 0.79, "avg_loan_amount": 182000}'::jsonb,
 1, 1, CURRENT_TIMESTAMP, 'system', CURRENT_TIMESTAMP, 'system', CURRENT_TIMESTAMP, 0),
-- 技术部117
(nextval('credit_form_data_seq'), 117, '2025-02', 'MONTHLY',
 '{"small_micro_enterprises_count": 98000, "credit_service_calls": 680000, "loan_applications_count": 2900, "loan_approval_rate": 0.73, "avg_loan_amount": 137000}'::jsonb,
 1, 1, CURRENT_TIMESTAMP, 'system', CURRENT_TIMESTAMP, 'system', CURRENT_TIMESTAMP, 0),

-- 3.3 2025-03月表单数据
-- 财务部115
(nextval('credit_form_data_seq'), 115, '2025-03', 'MONTHLY',
 '{"small_micro_enterprises_count": 132000, "credit_service_calls": 920000, "loan_applications_count": 3800, "loan_approval_rate": 0.77, "avg_loan_amount": 155000}'::jsonb,
 1, 1, CURRENT_TIMESTAMP, 'system', CURRENT_TIMESTAMP, 'system', CURRENT_TIMESTAMP, 0),
-- 市场部116
(nextval('credit_form_data_seq'), 116, '2025-03', 'MONTHLY',
 '{"small_micro_enterprises_count": 192000, "credit_service_calls": 1300000, "loan_applications_count": 5600, "loan_approval_rate": 0.80, "avg_loan_amount": 185000}'::jsonb,
 1, 1, CURRENT_TIMESTAMP, 'system', CURRENT_TIMESTAMP, 'system', CURRENT_TIMESTAMP, 0),
-- 技术部117
(nextval('credit_form_data_seq'), 117, '2025-03', 'MONTHLY',
 '{"small_micro_enterprises_count": 102000, "credit_service_calls": 710000, "loan_applications_count": 3000, "loan_approval_rate": 0.74, "avg_loan_amount": 140000}'::jsonb,
 1, 1, CURRENT_TIMESTAMP, 'system', CURRENT_TIMESTAMP, 'system', CURRENT_TIMESTAMP, 0),

-- 3.4 2024-10月表单数据（历史数据，用于同期对比）
-- 财务部115
(nextval('credit_form_data_seq'), 115, '2024-10', 'MONTHLY',
 '{"small_micro_enterprises_count": 118000, "credit_service_calls": 780000, "loan_applications_count": 3200, "loan_approval_rate": 0.73, "avg_loan_amount": 145000}'::jsonb,
 1, 1, CURRENT_TIMESTAMP, 'system', CURRENT_TIMESTAMP, 'system', CURRENT_TIMESTAMP, 0),
-- 市场部116
(nextval('credit_form_data_seq'), 116, '2024-10', 'MONTHLY',
 '{"small_micro_enterprises_count": 172000, "credit_service_calls": 1100000, "loan_applications_count": 4800, "loan_approval_rate": 0.76, "avg_loan_amount": 170000}'::jsonb,
 1, 1, CURRENT_TIMESTAMP, 'system', CURRENT_TIMESTAMP, 'system', CURRENT_TIMESTAMP, 0),
-- 技术部117
(nextval('credit_form_data_seq'), 117, '2024-10', 'MONTHLY',
 '{"small_micro_enterprises_count": 88000, "credit_service_calls": 600000, "loan_applications_count": 2600, "loan_approval_rate": 0.70, "avg_loan_amount": 130000}'::jsonb,
 1, 1, CURRENT_TIMESTAMP, 'system', CURRENT_TIMESTAMP, 'system', CURRENT_TIMESTAMP, 0),

-- 3.5 2024-11月表单数据
-- 财务部115
(nextval('credit_form_data_seq'), 115, '2024-11', 'MONTHLY',
 '{"small_micro_enterprises_count": 120000, "credit_service_calls": 800000, "loan_applications_count": 3300, "loan_approval_rate": 0.74, "avg_loan_amount": 147000}'::jsonb,
 1, 1, CURRENT_TIMESTAMP, 'system', CURRENT_TIMESTAMP, 'system', CURRENT_TIMESTAMP, 0),
-- 市场部116
(nextval('credit_form_data_seq'), 116, '2024-11', 'MONTHLY',
 '{"small_micro_enterprises_count": 175000, "credit_service_calls": 1120000, "loan_applications_count": 4900, "loan_approval_rate": 0.77, "avg_loan_amount": 172000}'::jsonb,
 1, 1, CURRENT_TIMESTAMP, 'system', CURRENT_TIMESTAMP, 'system', CURRENT_TIMESTAMP, 0),
-- 技术部117
(nextval('credit_form_data_seq'), 117, '2024-11', 'MONTHLY',
 '{"small_micro_enterprises_count": 90000, "credit_service_calls": 620000, "loan_applications_count": 2700, "loan_approval_rate": 0.71, "avg_loan_amount": 132000}'::jsonb,
 1, 1, CURRENT_TIMESTAMP, 'system', CURRENT_TIMESTAMP, 'system', CURRENT_TIMESTAMP, 0),

-- 3.6 2024-12月表单数据
-- 财务部115
(nextval('credit_form_data_seq'), 115, '2024-12', 'MONTHLY',
 '{"small_micro_enterprises_count": 122000, "credit_service_calls": 820000, "loan_applications_count": 3400, "loan_approval_rate": 0.74, "avg_loan_amount": 148000}'::jsonb,
 1, 1, CURRENT_TIMESTAMP, 'system', CURRENT_TIMESTAMP, 'system', CURRENT_TIMESTAMP, 0),
-- 市场部116
(nextval('credit_form_data_seq'), 116, '2024-12', 'MONTHLY',
 '{"small_micro_enterprises_count": 178000, "credit_service_calls": 1150000, "loan_applications_count": 5000, "loan_approval_rate": 0.77, "avg_loan_amount": 173000}'::jsonb,
 1, 1, CURRENT_TIMESTAMP, 'system', CURRENT_TIMESTAMP, 'system', CURRENT_TIMESTAMP, 0),
-- 技术部117
(nextval('credit_form_data_seq'), 117, '2024-12', 'MONTHLY',
 '{"small_micro_enterprises_count": 92000, "credit_service_calls": 640000, "loan_applications_count": 2800, "loan_approval_rate": 0.71, "avg_loan_amount": 133000}'::jsonb,
 1, 1, CURRENT_TIMESTAMP, 'system', CURRENT_TIMESTAMP, 'system', CURRENT_TIMESTAMP, 0);

-- ============================================
-- 4. 插入计算规则
-- ============================================

-- 4.1 月报计算规则（当期发生额和累计发生额）
INSERT INTO credit_calculation_rule (id, target_field_code, target_field_name, report_type, rule_type, rule_expression, calculation_order, description, status, creator, create_time, updater, update_time, deleted, display_config)
VALUES
-- 项目1：数据库收录小微企业户数 - 当期发生额（当季最新月报表3小微企业数）
(nextval('credit_calculation_rule_seq'), 'small_micro_enterprises_current', '数据库收录小微企业户数-当期发生额', 'MONTHLY', 'FORMULA',
 '#sum({#dept115[''small_micro_enterprises_count''], #dept116[''small_micro_enterprises_count''], #dept117[''small_micro_enterprises_count'']})',
 1, '当季最新月报表小微企业数', 1, 'system', CURRENT_TIMESTAMP, 'system', CURRENT_TIMESTAMP, 0,
 '{"groupName": "数据库收录小微企业户数", "groupOrder": 1, "columnType": "current_period"}'::jsonb),

-- 项目1：数据库收录小微企业户数 - 累计发生额（当季最新月报表3小微企业数）
(nextval('credit_calculation_rule_seq'), 'small_micro_enterprises_cumulative', '数据库收录小微企业户数-累计发生额', 'MONTHLY', 'FORMULA',
 '#sum({#dept115[''small_micro_enterprises_count''], #dept116[''small_micro_enterprises_count''], #dept117[''small_micro_enterprises_count'']})',
 2, '当季最新月报表小微企业数', 1, 'system', CURRENT_TIMESTAMP, 'system', CURRENT_TIMESTAMP, 0,
 '{"groupName": "数据库收录小微企业户数", "groupOrder": 1, "columnType": "accumulated"}'::jsonb),

-- 项目2：为放贷机构提供小微企业征信服务次数 - 当期发生额
(nextval('credit_calculation_rule_seq'), 'credit_service_calls_current', '为放贷机构提供小微企业征信服务次数-当期发生额', 'MONTHLY', 'FORMULA',
 '#sum({#dept115[''credit_service_calls''], #dept116[''credit_service_calls''], #dept117[''credit_service_calls'']})',
 3, '当季最新月报表5调用总次数', 1, 'system', CURRENT_TIMESTAMP, 'system', CURRENT_TIMESTAMP, 0,
 '{"groupName": "为放贷机构提供小微企业征信服务次数", "groupOrder": 2, "columnType": "current_period"}'::jsonb),

-- 项目2：为放贷机构提供小微企业征信服务次数 - 累计发生额
(nextval('credit_calculation_rule_seq'), 'credit_service_calls_cumulative', '为放贷机构提供小微企业征信服务次数-累计发生额', 'MONTHLY', 'FORMULA',
 '#sum({#dept115[''credit_service_calls''], #dept116[''credit_service_calls''], #dept117[''credit_service_calls'']})',
 4, '当季最新月报表5调用总次数', 1, 'system', CURRENT_TIMESTAMP, 'system', CURRENT_TIMESTAMP, 0,
 '{"groupName": "为放贷机构提供小微企业征信服务次数", "groupOrder": 2, "columnType": "accumulated"}'::jsonb),

-- 项目3：小微企业申请贷款户数 - 当期发生额（本季度申请进件企业数总和）
(nextval('credit_calculation_rule_seq'), 'loan_applications_current', '小微企业申请贷款户数-当期发生额', 'MONTHLY', 'FORMULA',
 '#sum({#dept115[''loan_applications_count''], #dept116[''loan_applications_count''], #dept117[''loan_applications_count'']})',
 5, '本季度申请进件企业数总和', 1, 'system', CURRENT_TIMESTAMP, 'system', CURRENT_TIMESTAMP, 0,
 '{"groupName": "小微企业申请贷款户数", "groupOrder": 3, "columnType": "current_period"}'::jsonb),

-- 项目3：小微企业申请贷款户数 - 累计发生额
(nextval('credit_calculation_rule_seq'), 'loan_applications_cumulative', '小微企业申请贷款户数-累计发生额', 'MONTHLY', 'FORMULA',
 '#sum({#dept115[''loan_applications_count''], #dept116[''loan_applications_count''], #dept117[''loan_applications_count'']})',
 6, '本季度申请进件企业数总和', 1, 'system', CURRENT_TIMESTAMP, 'system', CURRENT_TIMESTAMP, 0,
 '{"groupName": "小微企业申请贷款户数", "groupOrder": 3, "columnType": "accumulated"}'::jsonb),

-- 项目4：小微企业获得贷款户数 - 当期发生额（申请数 * 通过率）
(nextval('credit_calculation_rule_seq'), 'loan_approved_current', '小微企业获得贷款户数-当期发生额', 'MONTHLY', 'FORMULA',
 '#sum({#dept115[''loan_applications_count''] * #dept115[''loan_approval_rate''], #dept116[''loan_applications_count''] * #dept116[''loan_approval_rate''], #dept117[''loan_applications_count''] * #dept117[''loan_approval_rate'']})',
 7, '本季度申请进件企业数总和 * 企业贷款申请通过率', 1, 'system', CURRENT_TIMESTAMP, 'system', CURRENT_TIMESTAMP, 0,
 '{"groupName": "小微企业获得贷款户数", "groupOrder": 4, "columnType": "current_period"}'::jsonb),

-- 项目4：小微企业获得贷款户数 - 累计发生额
(nextval('credit_calculation_rule_seq'), 'loan_approved_cumulative', '小微企业获得贷款户数-累计发生额', 'MONTHLY', 'FORMULA',
 '#sum({#dept115[''loan_applications_count''] * #dept115[''loan_approval_rate''], #dept116[''loan_applications_count''] * #dept116[''loan_approval_rate''], #dept117[''loan_applications_count''] * #dept117[''loan_approval_rate'']})',
 8, '本季度申请进件企业数总和 * 企业贷款申请通过率', 1, 'system', CURRENT_TIMESTAMP, 'system', CURRENT_TIMESTAMP, 0,
 '{"groupName": "小微企业获得贷款户数", "groupOrder": 4, "columnType": "accumulated"}'::jsonb),

-- 项目5：小微企业获得贷款金额 - 当期发生额（申请数 * 通过率 * 平均放款）
(nextval('credit_calculation_rule_seq'), 'loan_amount_current', '小微企业获得贷款金额-当期发生额', 'MONTHLY', 'FORMULA',
 '#sum({#dept115[''loan_applications_count''] * #dept115[''loan_approval_rate''] * #dept115[''avg_loan_amount''], #dept116[''loan_applications_count''] * #dept116[''loan_approval_rate''] * #dept116[''avg_loan_amount''], #dept117[''loan_applications_count''] * #dept117[''loan_approval_rate''] * #dept117[''avg_loan_amount'']})',
 9, '本季度申请进件企业数总和 * 企业贷款申请通过率 * 已通过的每一户的平均放款', 1, 'system', CURRENT_TIMESTAMP, 'system', CURRENT_TIMESTAMP, 0,
 '{"groupName": "小微企业获得贷款金额", "groupOrder": 5, "columnType": "current_period"}'::jsonb),

-- 项目5：小微企业获得贷款金额 - 累计发生额
(nextval('credit_calculation_rule_seq'), 'loan_amount_cumulative', '小微企业获得贷款金额-累计发生额', 'MONTHLY', 'FORMULA',
 '#sum({#dept115[''loan_applications_count''] * #dept115[''loan_approval_rate''] * #dept115[''avg_loan_amount''], #dept116[''loan_applications_count''] * #dept116[''loan_approval_rate''] * #dept116[''avg_loan_amount''], #dept117[''loan_applications_count''] * #dept117[''loan_approval_rate''] * #dept117[''avg_loan_amount'']})',
 10, '本季度申请进件企业数总和 * 企业贷款申请通过率 * 已通过的每一户的平均放款', 1, 'system', CURRENT_TIMESTAMP, 'system', CURRENT_TIMESTAMP, 0,
 '{"groupName": "小微企业获得贷款金额", "groupOrder": 5, "columnType": "accumulated"}'::jsonb);

-- 4.2 季报计算规则（包含同期增长量和同期增长率）
INSERT INTO credit_calculation_rule (id, target_field_code, target_field_name, report_type, rule_type, rule_expression, calculation_order, description, status, creator, create_time, updater, update_time, deleted, display_config)
VALUES
-- 项目1：数据库收录小微企业户数 - 当期发生额（当季最新月报表3小微企业数 - 上季度累计发生额）
(nextval('credit_calculation_rule_seq'), 'small_micro_enterprises_q1_current', '数据库收录小微企业户数-当期发生额', 'QUARTERLY', 'FORMULA',
 '#safeGet(#latestMonthlyReport, ''small_micro_enterprises_cumulative'') - #safeGet(#previousQuarterReport, ''small_micro_enterprises_cumulative'')',
 1, '当季最新月报表3小微企业数 - 上季度累计发生额', 1, 'system', CURRENT_TIMESTAMP, 'system', CURRENT_TIMESTAMP, 0,
 '{"groupName": "数据库收录小微企业户数", "groupOrder": 1, "columnType": "current_period"}'::jsonb),

-- 项目1：数据库收录小微企业户数 - 累计发生额
(nextval('credit_calculation_rule_seq'), 'small_micro_enterprises_q1_cumulative', '数据库收录小微企业户数-累计发生额', 'QUARTERLY', 'FORMULA',
 '#safeGet(#latestMonthlyReport, ''small_micro_enterprises_cumulative'')',
 2, '当季最新月报表3小微企业数', 1, 'system', CURRENT_TIMESTAMP, 'system', CURRENT_TIMESTAMP, 0,
 '{"groupName": "数据库收录小微企业户数", "groupOrder": 1, "columnType": "accumulated"}'::jsonb),

-- 项目1：数据库收录小微企业户数 - 同期增长量
(nextval('credit_calculation_rule_seq'), 'small_micro_enterprises_q1_growth', '数据库收录小微企业户数-同期增长量', 'QUARTERLY', 'FORMULA',
 '(#safeGet(#latestMonthlyReport, ''small_micro_enterprises_cumulative'') - #safeGet(#previousQuarterReport, ''small_micro_enterprises_cumulative'')) - #safeGet(#previousQuarterReport, ''small_micro_enterprises_current'')',
 3, '=(当季最新月报表3小微企业数 - 上季度累计发生额) - 去年同期当期发生额', 1, 'system', CURRENT_TIMESTAMP, 'system', CURRENT_TIMESTAMP, 0,
 '{"groupName": "数据库收录小微企业户数", "groupOrder": 1, "columnType": "period_growth"}'::jsonb),

-- 项目1：数据库收录小微企业户数 - 同期增长率
(nextval('credit_calculation_rule_seq'), 'small_micro_enterprises_q1_growth_rate', '数据库收录小微企业户数-同期增长率', 'QUARTERLY', 'FORMULA',
 '#safeDivide((#safeGet(#latestMonthlyReport, ''small_micro_enterprises_cumulative'') - #safeGet(#previousQuarterReport, ''small_micro_enterprises_cumulative'')) - #safeGet(#previousQuarterReport, ''small_micro_enterprises_current''), #safeGet(#previousQuarterReport, ''small_micro_enterprises_current''))',
 4, '=((当季最新月报表3小微企业数 - 上季度累计发生额) - 去年同期当期发生额) / 去年同期当期发生额', 1, 'system', CURRENT_TIMESTAMP, 'system', CURRENT_TIMESTAMP, 0,
 '{"groupName": "数据库收录小微企业户数", "groupOrder": 1, "columnType": "period_growth_rate"}'::jsonb),

-- 项目2：为放贷机构提供小微企业征信服务次数 - 当期发生额
(nextval('credit_calculation_rule_seq'), 'credit_service_calls_q1_current', '为放贷机构提供小微企业征信服务次数-当期发生额', 'QUARTERLY', 'FORMULA',
 '#safeGet(#latestMonthlyReport, ''credit_service_calls_current'')',
 5, '当季最新月报表5调用总次数', 1, 'system', CURRENT_TIMESTAMP, 'system', CURRENT_TIMESTAMP, 0,
 '{"groupName": "为放贷机构提供小微企业征信服务次数", "groupOrder": 2, "columnType": "current_period"}'::jsonb),

-- 项目2：为放贷机构提供小微企业征信服务次数 - 累计发生额
(nextval('credit_calculation_rule_seq'), 'credit_service_calls_q1_cumulative', '为放贷机构提供小微企业征信服务次数-累计发生额', 'QUARTERLY', 'FORMULA',
 '#safeGet(#latestMonthlyReport, ''credit_service_calls_cumulative'') + #safeGet(#previousQuarterReport, ''credit_service_calls_cumulative'')',
 6, '=当季最新月报表5调用总次数 + 上季度累计发生额', 1, 'system', CURRENT_TIMESTAMP, 'system', CURRENT_TIMESTAMP, 0,
 '{"groupName": "为放贷机构提供小微企业征信服务次数", "groupOrder": 2, "columnType": "accumulated"}'::jsonb),

-- 项目2：为放贷机构提供小微企业征信服务次数 - 同期增长量
(nextval('credit_calculation_rule_seq'), 'credit_service_calls_q1_growth', '为放贷机构提供小微企业征信服务次数-同期增长量', 'QUARTERLY', 'FORMULA',
 '#safeGet(#latestMonthlyReport, ''credit_service_calls_current'') - #safeGet(#previousQuarterReport, ''credit_service_calls_current'')',
 7, '=当季最新月报表5调用总次数 - 去年同期当期发生额', 1, 'system', CURRENT_TIMESTAMP, 'system', CURRENT_TIMESTAMP, 0,
 '{"groupName": "为放贷机构提供小微企业征信服务次数", "groupOrder": 2, "columnType": "period_growth"}'::jsonb),

-- 项目2：为放贷机构提供小微企业征信服务次数 - 同期增长率
(nextval('credit_calculation_rule_seq'), 'credit_service_calls_q1_growth_rate', '为放贷机构提供小微企业征信服务次数-同期增长率', 'QUARTERLY', 'FORMULA',
 '#safeDivide(#safeGet(#latestMonthlyReport, ''credit_service_calls_current'') - #safeGet(#previousQuarterReport, ''credit_service_calls_current''), #safeGet(#previousQuarterReport, ''credit_service_calls_current''))',
 8, '=(当季最新月报表5调用总次数 - 去年同期当期发生额) / 去年同期当期发生额', 1, 'system', CURRENT_TIMESTAMP, 'system', CURRENT_TIMESTAMP, 0,
 '{"groupName": "为放贷机构提供小微企业征信服务次数", "groupOrder": 2, "columnType": "period_growth_rate"}'::jsonb);

COMMIT;

-- ============================================
-- 5. 验证数据完整性
-- ============================================

SELECT '字段配置数量' as check_item, COUNT(*) as count FROM credit_field_config WHERE deleted = 0
UNION ALL
SELECT '表单数据数量', COUNT(*) FROM credit_form_data WHERE deleted = 0 AND status = 1
UNION ALL
SELECT '月报计算规则数量', COUNT(*) FROM credit_calculation_rule WHERE deleted = 0 AND status = 1 AND report_type = 'MONTHLY'
UNION ALL
SELECT '季报计算规则数量', COUNT(*) FROM credit_calculation_rule WHERE deleted = 0 AND status = 1 AND report_type = 'QUARTERLY';

-- 显示部分数据示例
SELECT '表单数据示例（2025-01）' as info;
SELECT dept_id, report_period, form_data FROM credit_form_data WHERE report_period = '2025-01' AND deleted = 0 LIMIT 3;

SELECT '计算规则示例（月报）' as info;
SELECT target_field_code, target_field_name, rule_expression FROM credit_calculation_rule WHERE report_type = 'MONTHLY' AND deleted = 0 LIMIT 3;

SELECT '数据准备完成！' as status;
