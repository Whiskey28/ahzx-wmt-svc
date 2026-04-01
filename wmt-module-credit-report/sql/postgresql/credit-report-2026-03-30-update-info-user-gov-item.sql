-- ============================
-- 征信报送（积木报表）信息使用者-政府机构明细：数据种子/修复
-- 创建时间：2026-03-30
-- 说明：
-- 1) 对应表：report_fill_info_user_gov_item
-- 2) gov_org_name：政府机构名称
-- 3) is_current_service：
--    - 1：当前使用服务的政府机构
--    - 0：仅参与“总累计”的政府机构（当前使用服务为空/未统计完整）
-- 4) sort_no：排序号（用于分页/导出/树组件显示顺序）
-- ============================

-- 1、安徽省综合金融服务平台（当前使用服务：是）
UPDATE report_fill_info_user_gov_item
SET is_current_service = 1,
    sort_no = '1',
    updater = '1',
    update_time = CURRENT_TIMESTAMP,
    deleted = 0
WHERE gov_org_name = '安徽省综合金融服务平台'
  AND deleted = 0;
INSERT INTO report_fill_info_user_gov_item
  (gov_org_name, is_current_service, sort_no, create_time, update_time, creator, updater, deleted)
SELECT
  '安徽省综合金融服务平台', 1, '1', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '1', '1', 0
WHERE NOT EXISTS (
  SELECT 1 FROM report_fill_info_user_gov_item
  WHERE gov_org_name = '安徽省综合金融服务平台' AND deleted = 0
);

-- 2、安徽省金融大脑（当前使用服务：是）
UPDATE report_fill_info_user_gov_item
SET is_current_service = 1,
    sort_no = '2',
    updater = '1',
    update_time = CURRENT_TIMESTAMP,
    deleted = 0
WHERE gov_org_name = '安徽省金融大脑'
  AND deleted = 0;
INSERT INTO report_fill_info_user_gov_item
  (gov_org_name, is_current_service, sort_no, create_time, update_time, creator, updater, deleted)
SELECT
  '安徽省金融大脑', 1, '2', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '1', '1', 0
WHERE NOT EXISTS (
  SELECT 1 FROM report_fill_info_user_gov_item
  WHERE gov_org_name = '安徽省金融大脑' AND deleted = 0
);

-- 3、黄山市综合金融服务平台（当前使用服务：是）
UPDATE report_fill_info_user_gov_item
SET is_current_service = 1,
    sort_no = '3',
    updater = '1',
    update_time = CURRENT_TIMESTAMP,
    deleted = 0
WHERE gov_org_name = '黄山市综合金融服务平台'
  AND deleted = 0;
INSERT INTO report_fill_info_user_gov_item
  (gov_org_name, is_current_service, sort_no, create_time, update_time, creator, updater, deleted)
SELECT
  '黄山市综合金融服务平台', 1, '3', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '1', '1', 0
WHERE NOT EXISTS (
  SELECT 1 FROM report_fill_info_user_gov_item
  WHERE gov_org_name = '黄山市综合金融服务平台' AND deleted = 0
);

-- 4、合肥海关（当前使用服务：否，仅累计：数据后期补全）
UPDATE report_fill_info_user_gov_item
SET is_current_service = 0,
    sort_no = '4',
    updater = '1',
    update_time = CURRENT_TIMESTAMP,
    deleted = 0
WHERE gov_org_name = '合肥海关'
  AND deleted = 0;
INSERT INTO report_fill_info_user_gov_item
  (gov_org_name, is_current_service, sort_no, create_time, update_time, creator, updater, deleted)
SELECT
  '合肥海关', 0, '4', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '1', '1', 0
WHERE NOT EXISTS (
  SELECT 1 FROM report_fill_info_user_gov_item
  WHERE gov_org_name = '合肥海关' AND deleted = 0
);

-- 5、淮北发改委（当前使用服务：否，仅累计：数据后期补全）
UPDATE report_fill_info_user_gov_item
SET is_current_service = 0,
    sort_no = '5',
    updater = '1',
    update_time = CURRENT_TIMESTAMP,
    deleted = 0
WHERE gov_org_name = '淮北发改委'
  AND deleted = 0;
INSERT INTO report_fill_info_user_gov_item
  (gov_org_name, is_current_service, sort_no, create_time, update_time, creator, updater, deleted)
SELECT
  '淮北发改委', 0, '5', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '1', '1', 0
WHERE NOT EXISTS (
  SELECT 1 FROM report_fill_info_user_gov_item
  WHERE gov_org_name = '淮北发改委' AND deleted = 0
);

