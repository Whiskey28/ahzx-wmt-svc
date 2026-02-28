-- ============================
-- 征信报表填报人角色导入 SQL
-- 创建时间: 2026-02-05
-- 说明: 为征信报表系统创建各部门填报人角色
-- ============================

-- ----------------------------
-- 插入填报人角色
-- ----------------------------
-- 使用 ON CONFLICT 处理重复插入（如果角色已存在则更新）
INSERT INTO system_role (id, name, code, sort, data_scope, data_scope_dept_ids, status, type, remark, creator, create_time, updater, update_time, deleted, tenant_id) VALUES
(200, '综合管理部填报人', 'credit_report_admin_dept', 10, 1, '', 0, 1, '综合管理部报表填报角色', '1', CURRENT_TIMESTAMP, '1', CURRENT_TIMESTAMP, 0, 1),
(201, '财务部填报人', 'credit_report_finance_dept', 11, 1, '', 0, 1, '财务部报表填报角色', '1', CURRENT_TIMESTAMP, '1', CURRENT_TIMESTAMP, 0, 1),
(202, '党委工作部填报人', 'credit_report_party_dept', 12, 1, '', 0, 1, '党委工作部报表填报角色', '1', CURRENT_TIMESTAMP, '1', CURRENT_TIMESTAMP, 0, 1),
(203, '创新研发中心填报人', 'credit_report_rd_center', 13, 1, '', 0, 1, '创新研发中心报表填报角色', '1', CURRENT_TIMESTAMP, '1', CURRENT_TIMESTAMP, 0, 1),
(204, '市场部填报人', 'credit_report_market_dept', 14, 1, '', 0, 1, '市场部报表填报角色', '1', CURRENT_TIMESTAMP, '1', CURRENT_TIMESTAMP, 0, 1),
(205, '信息技术中心填报人', 'credit_report_it_center', 15, 1, '', 0, 1, '信息技术中心报表填报角色', '1', CURRENT_TIMESTAMP, '1', CURRENT_TIMESTAMP, 0, 1),
(206, '企业信用部填报人', 'credit_report_enterprise_credit', 16, 1, '', 0, 1, '企业信用部报表填报角色', '1', CURRENT_TIMESTAMP, '1', CURRENT_TIMESTAMP, 0, 1),
(207, '普惠信用部填报人', 'credit_report_inclusive_credit', 17, 1, '', 0, 1, '普惠信用部报表填报角色', '1', CURRENT_TIMESTAMP, '1', CURRENT_TIMESTAMP, 0, 1),
(208, '数据管理中心填报人', 'credit_report_data_center', 18, 1, '', 0, 1, '数据管理中心报表填报角色', '1', CURRENT_TIMESTAMP, '1', CURRENT_TIMESTAMP, 0, 1)
ON CONFLICT (id) DO UPDATE
SET
  name = EXCLUDED.name,
  code = EXCLUDED.code,
  sort = EXCLUDED.sort,
  remark = EXCLUDED.remark,
  updater = EXCLUDED.updater,
  update_time = CURRENT_TIMESTAMP;

-- ============================
-- SQL执行完成
-- 说明：
-- 1. 角色ID范围：200-208（避免与现有角色冲突）
-- 2. 角色代码统一使用 credit_report_ 前缀
-- 3. 排序号从 10 开始递增
-- 4. 所有角色默认启用（status=0），数据权限为全部（data_scope=1）
-- 5. 租户ID默认为 1
-- ============================
-- ----------------------------
INSERT INTO system_users (id, username, password, nickname, remark, dept_id, post_ids, email, mobile, sex, avatar, status, login_ip, login_date, creator, create_time, updater, update_time, deleted, tenant_id) VALUES
(1001, 'chenjunya', '$2a$04$KljJDa/LK7QfDm0lF5OhuePhlPfjRH3tB2Wu351Uidz.oQGJXevPi', '陈俊雅', '征信报表填报人', NULL, '[]', '', '', 0, '', 0, '', NULL, '1', CURRENT_TIMESTAMP, '1', CURRENT_TIMESTAMP, 0, 1),
(1002, 'shenyingying', '$2a$04$KljJDa/LK7QfDm0lF5OhuePhlPfjRH3tB2Wu351Uidz.oQGJXevPi', '沈莹莹', '征信报表填报人', NULL, '[]', '', '', 0, '', 0, '', NULL, '1', CURRENT_TIMESTAMP, '1', CURRENT_TIMESTAMP, 0, 1),
(1003, 'wangyujia', '$2a$04$KljJDa/LK7QfDm0lF5OhuePhlPfjRH3tB2Wu351Uidz.oQGJXevPi', '汪雨佳', '征信报表填报人', NULL, '[]', '', '', 0, '', 0, '', NULL, '1', CURRENT_TIMESTAMP, '1', CURRENT_TIMESTAMP, 0, 1),
(1004, 'liyiming', '$2a$04$KljJDa/LK7QfDm0lF5OhuePhlPfjRH3tB2Wu351Uidz.oQGJXevPi', '李一鸣', '征信报表填报人', NULL, '[]', '', '', 0, '', 0, '', NULL, '1', CURRENT_TIMESTAMP, '1', CURRENT_TIMESTAMP, 0, 1)
ON CONFLICT (id) DO UPDATE
SET
  username = EXCLUDED.username,
  nickname = EXCLUDED.nickname,
  remark = EXCLUDED.remark,
  dept_id = EXCLUDED.dept_id,
  post_ids = EXCLUDED.post_ids,
  status = EXCLUDED.status,
  updater = EXCLUDED.updater,
  update_time = CURRENT_TIMESTAMP;

-- ----------------------------
-- 插入征信报表填报人用户-角色关联
-- 说明：
-- 1. 角色ID来自本文件前面的 system_role 插入：
--    - 200：综合管理部填报人（credit_report_admin_dept）
--    - 203：创新研发中心填报人（credit_report_rd_center）
--    - 204：市场部填报人（credit_report_market_dept）
--    - 206：企业信用部填报人（credit_report_enterprise_credit）
--    - 208：数据管理中心填报人（credit_report_data_center）
-- 2. 映射关系：
--    - 用户 1001（陈俊雅）：角色 200 + 206
--    - 用户 1002（沈莹莹）：角色 203
--    - 用户 1003（汪雨佳）：角色 208
--    - 用户 1004（李一鸣）：角色 204
-- 3. 用户-角色关联ID范围：2001-2005（避免与现有数据冲突）
-- ----------------------------
INSERT INTO system_user_role (id, user_id, role_id, creator, create_time, updater, update_time, deleted, tenant_id) VALUES
(2001, 1001, 200, '1', CURRENT_TIMESTAMP, '1', CURRENT_TIMESTAMP, 0, 1),
(2002, 1001, 206, '1', CURRENT_TIMESTAMP, '1', CURRENT_TIMESTAMP, 0, 1),
(2003, 1002, 203, '1', CURRENT_TIMESTAMP, '1', CURRENT_TIMESTAMP, 0, 1),
(2004, 1003, 208, '1', CURRENT_TIMESTAMP, '1', CURRENT_TIMESTAMP, 0, 1),
(2005, 1004, 204, '1', CURRENT_TIMESTAMP, '1', CURRENT_TIMESTAMP, 0, 1)
ON CONFLICT (id) DO UPDATE
SET
  user_id = EXCLUDED.user_id,
  role_id = EXCLUDED.role_id,
  updater = EXCLUDED.updater,
  update_time = CURRENT_TIMESTAMP;

-- ============================
-- SQL执行完成
-- 说明：
-- 1. 角色ID范围：200-208（避免与现有角色冲突）
-- 2. 角色代码统一使用 credit_report_ 前缀
-- 3. 排序号从 10 开始递增
-- 4. 所有角色默认启用（status=0），数据权限为全部（data_scope=1）
-- 5. 租户ID默认为 1
-- ============================
