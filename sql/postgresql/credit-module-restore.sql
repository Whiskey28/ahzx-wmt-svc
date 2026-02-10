-- ============================================
-- 征信模块数据库恢复脚本
-- 数据库：PostgreSQL v15
-- 日期：2025-01-XX
-- 说明：从备份表恢复数据，用于测试后回退
-- ============================================

BEGIN;

-- 1. 删除测试数据
DELETE FROM credit_summary_report WHERE report_period IN ('2025-01', '2025-02', '2025-03', '2025-Q1', '2024-Q4', '2024-10', '2024-11', '2024-12');
DELETE FROM credit_form_data WHERE report_period IN ('2025-01', '2025-02', '2025-03', '2024-10', '2024-11', '2024-12');
DELETE FROM credit_calculation_rule WHERE report_type IN ('MONTHLY', 'QUARTERLY');
DELETE FROM credit_field_config;

-- 2. 恢复数据
INSERT INTO credit_field_config SELECT * FROM credit_field_config_backup;
INSERT INTO credit_form_data SELECT * FROM credit_form_data_backup;
INSERT INTO credit_calculation_rule SELECT * FROM credit_calculation_rule_backup;
INSERT INTO credit_summary_report SELECT * FROM credit_summary_report_backup;

-- 3. 恢复序列值
DO $$
DECLARE
    seq_record RECORD;
BEGIN
    FOR seq_record IN 
        SELECT sequence_name, last_value 
        FROM credit_sequences_backup 
        WHERE sequence_name LIKE 'credit_%_seq'
    LOOP
        EXECUTE format('SELECT setval(%L, %s)', seq_record.sequence_name, seq_record.last_value);
    END LOOP;
END $$;

COMMIT;

-- 验证恢复结果
SELECT 
    'credit_field_config' as table_name,
    COUNT(*) as record_count
FROM credit_field_config
UNION ALL
SELECT 
    'credit_form_data',
    COUNT(*)
FROM credit_form_data
UNION ALL
SELECT 
    'credit_calculation_rule',
    COUNT(*)
FROM credit_calculation_rule
UNION ALL
SELECT 
    'credit_summary_report',
    COUNT(*)
FROM credit_summary_report;

SELECT '恢复完成！' as status;
