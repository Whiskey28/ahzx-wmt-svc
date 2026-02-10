-- 检查触发器是否已创建
-- 执行此 SQL 可以查看所有 report_fill_* 表的触发器状态

-- 查看所有 report_fill_* 表的触发器
SELECT 
    tgname AS trigger_name,
    tgrelid::regclass AS table_name,
    tgenabled AS enabled,
    pg_get_triggerdef(oid) AS trigger_definition
FROM pg_trigger
WHERE tgrelid::regclass::text LIKE 'report_fill_%'
  AND tgname NOT LIKE 'RI_%'  -- 排除外键约束触发器
ORDER BY tgrelid::regclass::text, tgname;

-- 查看触发器函数是否存在
SELECT 
    proname AS function_name,
    prosrc AS function_source
FROM pg_proc
WHERE proname = 'fill_audit_time_fields';

-- 测试触发器是否工作（可选，用于调试）
-- 注意：执行前请先备份数据
/*
INSERT INTO report_fill_enterprise_basic (id, enterprise_name, create_time, update_time)
VALUES (999999, '测试企业', NULL, NULL);

-- 然后查询，看时间是否自动填充
SELECT id, enterprise_name, create_time, update_time 
FROM report_fill_enterprise_basic 
WHERE id = 999999;

-- 清理测试数据
DELETE FROM report_fill_enterprise_basic WHERE id = 999999;
*/
