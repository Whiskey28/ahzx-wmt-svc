-- 信用报送表审计字段自动填充触发器（仅处理时间字段）
-- 说明：为 report_fill_* 表创建触发器，自动填充 create_time、update_time 字段
-- 注意：creator 和 updater 字段由表单隐藏字段传入，不在此处理

-- ============================
-- 函数：自动填充时间字段
-- ============================
CREATE OR REPLACE FUNCTION fill_audit_time_fields()
RETURNS TRIGGER AS $$
BEGIN
    -- 插入时：强制填充 create_time 和 update_time（即使显式传入 NULL 也会覆盖）
    IF TG_OP = 'INSERT' THEN
        -- 如果 create_time 为空或 NULL，设置为当前时间
        IF NEW.create_time IS NULL THEN
            NEW.create_time := CURRENT_TIMESTAMP;
        END IF;
        -- update_time 在插入时总是设置为当前时间
        NEW.update_time := CURRENT_TIMESTAMP;
    END IF;
    
    -- 更新时：自动更新 update_time
    IF TG_OP = 'UPDATE' THEN
        NEW.update_time := CURRENT_TIMESTAMP;
    END IF;
    
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

-- ============================
-- 为 report_fill_basic_info 创建触发器
-- ============================
DROP TRIGGER IF EXISTS trg_report_fill_basic_info_audit ON report_fill_basic_info;
CREATE TRIGGER trg_report_fill_basic_info_audit
    BEFORE INSERT OR UPDATE ON report_fill_basic_info
    FOR EACH ROW
    EXECUTE FUNCTION fill_audit_time_fields();

-- ============================
-- 为 report_fill_enterprise_basic 创建触发器
-- ============================
DROP TRIGGER IF EXISTS trg_report_fill_enterprise_basic_audit ON report_fill_enterprise_basic;
CREATE TRIGGER trg_report_fill_enterprise_basic_audit
    BEFORE INSERT OR UPDATE ON report_fill_enterprise_basic
    FOR EACH ROW
    EXECUTE FUNCTION fill_audit_time_fields();

-- ============================
-- 为 report_fill_info_user_org_item 创建触发器
-- ============================
DROP TRIGGER IF EXISTS trg_report_fill_info_user_org_item_audit ON report_fill_info_user_org_item;
CREATE TRIGGER trg_report_fill_info_user_org_item_audit
    BEFORE INSERT OR UPDATE ON report_fill_info_user_org_item
    FOR EACH ROW
    EXECUTE FUNCTION fill_audit_time_fields();

-- ============================
-- 为 report_fill_info_user_gov_item 创建触发器
-- ============================
DROP TRIGGER IF EXISTS trg_report_fill_info_user_gov_item_audit ON report_fill_info_user_gov_item;
CREATE TRIGGER trg_report_fill_info_user_gov_item_audit
    BEFORE INSERT OR UPDATE ON report_fill_info_user_gov_item
    FOR EACH ROW
    EXECUTE FUNCTION fill_audit_time_fields();

-- ============================
-- 为 report_fill_biz_stat_finance 创建触发器
-- ============================
DROP TRIGGER IF EXISTS trg_report_fill_biz_stat_finance_audit ON report_fill_biz_stat_finance;
CREATE TRIGGER trg_report_fill_biz_stat_finance_audit
    BEFORE INSERT OR UPDATE ON report_fill_biz_stat_finance
    FOR EACH ROW
    EXECUTE FUNCTION fill_audit_time_fields();

-- ============================
-- 为 report_fill_biz_stat_hr 创建触发器
-- ============================
DROP TRIGGER IF EXISTS trg_report_fill_biz_stat_hr_audit ON report_fill_biz_stat_hr;
CREATE TRIGGER trg_report_fill_biz_stat_hr_audit
    BEFORE INSERT OR UPDATE ON report_fill_biz_stat_hr
    FOR EACH ROW
    EXECUTE FUNCTION fill_audit_time_fields();

-- ============================
-- 为 report_fill_biz_stat_credit_build 创建触发器
-- ============================
DROP TRIGGER IF EXISTS trg_report_fill_biz_stat_credit_build_audit ON report_fill_biz_stat_credit_build;
CREATE TRIGGER trg_report_fill_biz_stat_credit_build_audit
    BEFORE INSERT OR UPDATE ON report_fill_biz_stat_credit_build
    FOR EACH ROW
    EXECUTE FUNCTION fill_audit_time_fields();

-- ============================
-- 为 report_fill_product_stat 创建触发器
-- ============================
DROP TRIGGER IF EXISTS trg_report_fill_product_stat_audit ON report_fill_product_stat;
CREATE TRIGGER trg_report_fill_product_stat_audit
    BEFORE INSERT OR UPDATE ON report_fill_product_stat
    FOR EACH ROW
    EXECUTE FUNCTION fill_audit_time_fields();

-- ============================
-- 为 report_fill_service_by_industry 创建触发器
-- ============================
DROP TRIGGER IF EXISTS trg_report_fill_service_by_industry_audit ON report_fill_service_by_industry;
CREATE TRIGGER trg_report_fill_service_by_industry_audit
    BEFORE INSERT OR UPDATE ON report_fill_service_by_industry
    FOR EACH ROW
    EXECUTE FUNCTION fill_audit_time_fields();

-- ============================
-- 为 report_fill_info_source_by_industry 创建触发器
-- ============================
DROP TRIGGER IF EXISTS trg_report_fill_info_source_by_industry_audit ON report_fill_info_source_by_industry;
CREATE TRIGGER trg_report_fill_info_source_by_industry_audit
    BEFORE INSERT OR UPDATE ON report_fill_info_source_by_industry
    FOR EACH ROW
    EXECUTE FUNCTION fill_audit_time_fields();

-- ============================
-- 为 report_fill_complaint_security_stat 创建触发器
-- ============================
DROP TRIGGER IF EXISTS trg_report_fill_complaint_security_stat_audit ON report_fill_complaint_security_stat;
CREATE TRIGGER trg_report_fill_complaint_security_stat_audit
    BEFORE INSERT OR UPDATE ON report_fill_complaint_security_stat
    FOR EACH ROW
    EXECUTE FUNCTION fill_audit_time_fields();

-- ============================
-- 为 report_fill_info_collect_stat 创建触发器
-- ============================
DROP TRIGGER IF EXISTS trg_report_fill_info_collect_stat_audit ON report_fill_info_collect_stat;
CREATE TRIGGER trg_report_fill_info_collect_stat_audit
    BEFORE INSERT OR UPDATE ON report_fill_info_collect_stat
    FOR EACH ROW
    EXECUTE FUNCTION fill_audit_time_fields();
