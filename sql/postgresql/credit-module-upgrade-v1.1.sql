-- ============================================
-- 征信模块展示配置字段升级脚本
-- 版本：v1.1
-- 数据库：PostgreSQL v15
-- 日期：2025-01-XX
-- 说明：为计算规则表和字段配置表增加展示配置字段
-- ============================================

BEGIN;

-- ----------------------------
-- 1. 计算规则表升级
-- ----------------------------

-- 1.1 增加目标字段名称字段
ALTER TABLE credit_calculation_rule 
ADD COLUMN IF NOT EXISTS target_field_name VARCHAR(128) NULL;

COMMENT ON COLUMN credit_calculation_rule.target_field_name IS '目标字段名称（用于前端显示）';

-- 1.2 增加前端展示配置字段（JSONB类型）
ALTER TABLE credit_calculation_rule 
ADD COLUMN IF NOT EXISTS display_config JSONB NULL;

COMMENT ON COLUMN credit_calculation_rule.display_config IS '前端展示配置（JSONB格式，存储字段分组、布局、样式等展示信息）';

-- 1.3 为 display_config 创建 GIN 索引（可选，如果需要对JSON字段进行查询）
CREATE INDEX IF NOT EXISTS idx_credit_calculation_rule_display_config_gin 
ON credit_calculation_rule USING GIN (display_config) 
WHERE deleted = 0 AND status = 1;

-- ----------------------------
-- 2. 字段配置表升级
-- ----------------------------

-- 2.1 增加前端展示配置字段（JSONB类型）
ALTER TABLE credit_field_config 
ADD COLUMN IF NOT EXISTS display_config JSONB NULL;

COMMENT ON COLUMN credit_field_config.display_config IS '前端展示配置（JSONB格式，存储字段分组、布局、样式等展示信息）';

-- 2.2 为 display_config 创建 GIN 索引（可选，如果需要对JSON字段进行查询）
CREATE INDEX IF NOT EXISTS idx_credit_field_config_display_config_gin 
ON credit_field_config USING GIN (display_config) 
WHERE deleted = 0 AND status = 1;

COMMIT;

-- ============================================
-- 升级完成
-- ============================================

-- 验证升级结果
SELECT 
    'credit_calculation_rule' as table_name,
    column_name,
    data_type,
    is_nullable
FROM information_schema.columns 
WHERE table_name = 'credit_calculation_rule' 
  AND column_name IN ('target_field_name', 'display_config')
UNION ALL
SELECT 
    'credit_field_config' as table_name,
    column_name,
    data_type,
    is_nullable
FROM information_schema.columns 
WHERE table_name = 'credit_field_config' 
  AND column_name = 'display_config'
ORDER BY table_name, column_name;
