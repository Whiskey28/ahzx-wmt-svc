-- ============================================
-- 征信模块数据库备份脚本
-- 数据库：PostgreSQL v15
-- 日期：2025-01-XX
-- 说明：备份测试前的数据，用于测试后回退
-- ============================================

-- 创建备份表（如果不存在）
CREATE TABLE IF NOT EXISTS credit_field_config_backup AS 
SELECT * FROM credit_field_config WHERE 1=0;

CREATE TABLE IF NOT EXISTS credit_form_data_backup AS 
SELECT * FROM credit_form_data WHERE 1=0;

CREATE TABLE IF NOT EXISTS credit_calculation_rule_backup AS 
SELECT * FROM credit_calculation_rule WHERE 1=0;

CREATE TABLE IF NOT EXISTS credit_summary_report_backup AS 
SELECT * FROM credit_summary_report WHERE 1=0;

-- 备份数据
TRUNCATE TABLE credit_field_config_backup;
INSERT INTO credit_field_config_backup SELECT * FROM credit_field_config;

TRUNCATE TABLE credit_form_data_backup;
INSERT INTO credit_form_data_backup SELECT * FROM credit_form_data;

TRUNCATE TABLE credit_calculation_rule_backup;
INSERT INTO credit_calculation_rule_backup SELECT * FROM credit_calculation_rule;

TRUNCATE TABLE credit_summary_report_backup;
INSERT INTO credit_summary_report_backup SELECT * FROM credit_summary_report;

-- 备份序列值（记录当前序列值）
CREATE TABLE IF NOT EXISTS credit_sequences_backup (
    sequence_name VARCHAR(128) PRIMARY KEY,
    last_value BIGINT,
    backup_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

DELETE FROM credit_sequences_backup WHERE sequence_name LIKE 'credit_%_seq';

INSERT INTO credit_sequences_backup (sequence_name, last_value)
SELECT 
    sequence_name::VARCHAR,
    last_value
FROM (
    SELECT 'credit_field_config_seq'::TEXT as sequence_name, last_value FROM credit_field_config_seq
    UNION ALL
    SELECT 'credit_form_data_seq'::TEXT, last_value FROM credit_form_data_seq
    UNION ALL
    SELECT 'credit_calculation_rule_seq'::TEXT, last_value FROM credit_calculation_rule_seq
    UNION ALL
    SELECT 'credit_summary_report_seq'::TEXT, last_value FROM credit_summary_report_seq
) seq_data;

-- 输出备份信息
SELECT 
    'credit_field_config_backup' as table_name,
    COUNT(*) as record_count
FROM credit_field_config_backup
UNION ALL
SELECT 
    'credit_form_data_backup',
    COUNT(*)
FROM credit_form_data_backup
UNION ALL
SELECT 
    'credit_calculation_rule_backup',
    COUNT(*)
FROM credit_calculation_rule_backup
UNION ALL
SELECT 
    'credit_summary_report_backup',
    COUNT(*)
FROM credit_summary_report_backup;

SELECT '备份完成！' as status;
