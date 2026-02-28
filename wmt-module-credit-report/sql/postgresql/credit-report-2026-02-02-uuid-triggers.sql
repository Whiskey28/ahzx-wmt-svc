-- 信用报送表审计字段自动填充触发器（UUID 主键版）- PostgreSQL v15 - 2026-02-02
-- 说明：为 report_fill_* 表创建触发器，自动填充 create_time、update_time 字段
-- 注意：creator 和 updater 字段由表单隐藏字段传入，不在此处理

-- ============================
-- 函数：自动填充时间字段
-- ============================
CREATE OR REPLACE FUNCTION fill_audit_time_fields()
RETURNS TRIGGER AS $$
BEGIN
    -- 插入时：填充 create_time / update_time
    IF TG_OP = 'INSERT' THEN
        IF NEW.create_time IS NULL THEN
            NEW.create_time := CURRENT_TIMESTAMP;
        END IF;
        NEW.update_time := CURRENT_TIMESTAMP;
    END IF;

    -- 更新时：更新 update_time
    IF TG_OP = 'UPDATE' THEN
        NEW.update_time := CURRENT_TIMESTAMP;
    END IF;

    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

-- ============================
-- 为所有 report_fill_* 表创建触发器
-- ============================
DROP TRIGGER IF EXISTS trg_report_fill_basic_info_audit ON report_fill_basic_info;
CREATE TRIGGER trg_report_fill_basic_info_audit
    BEFORE INSERT OR UPDATE ON report_fill_basic_info
    FOR EACH ROW
    EXECUTE FUNCTION fill_audit_time_fields();

DROP TRIGGER IF EXISTS trg_report_fill_enterprise_basic_audit ON report_fill_enterprise_basic;
CREATE TRIGGER trg_report_fill_enterprise_basic_audit
    BEFORE INSERT OR UPDATE ON report_fill_enterprise_basic
    FOR EACH ROW
    EXECUTE FUNCTION fill_audit_time_fields();

DROP TRIGGER IF EXISTS trg_report_fill_info_user_org_item_audit ON report_fill_info_user_org_item;
CREATE TRIGGER trg_report_fill_info_user_org_item_audit
    BEFORE INSERT OR UPDATE ON report_fill_info_user_org_item
    FOR EACH ROW
    EXECUTE FUNCTION fill_audit_time_fields();

DROP TRIGGER IF EXISTS trg_report_fill_info_user_gov_item_audit ON report_fill_info_user_gov_item;
CREATE TRIGGER trg_report_fill_info_user_gov_item_audit
    BEFORE INSERT OR UPDATE ON report_fill_info_user_gov_item
    FOR EACH ROW
    EXECUTE FUNCTION fill_audit_time_fields();

DROP TRIGGER IF EXISTS trg_report_fill_biz_stat_finance_audit ON report_fill_biz_stat_finance;
CREATE TRIGGER trg_report_fill_biz_stat_finance_audit
    BEFORE INSERT OR UPDATE ON report_fill_biz_stat_finance
    FOR EACH ROW
    EXECUTE FUNCTION fill_audit_time_fields();

DROP TRIGGER IF EXISTS trg_report_fill_biz_stat_hr_audit ON report_fill_biz_stat_hr;
CREATE TRIGGER trg_report_fill_biz_stat_hr_audit
    BEFORE INSERT OR UPDATE ON report_fill_biz_stat_hr
    FOR EACH ROW
    EXECUTE FUNCTION fill_audit_time_fields();

DROP TRIGGER IF EXISTS trg_report_fill_biz_stat_credit_build_audit ON report_fill_biz_stat_credit_build;
CREATE TRIGGER trg_report_fill_biz_stat_credit_build_audit
    BEFORE INSERT OR UPDATE ON report_fill_biz_stat_credit_build
    FOR EACH ROW
    EXECUTE FUNCTION fill_audit_time_fields();

DROP TRIGGER IF EXISTS trg_report_fill_product_stat_audit ON report_fill_product_stat;
CREATE TRIGGER trg_report_fill_product_stat_audit
    BEFORE INSERT OR UPDATE ON report_fill_product_stat
    FOR EACH ROW
    EXECUTE FUNCTION fill_audit_time_fields();

DROP TRIGGER IF EXISTS trg_report_fill_service_by_industry_audit ON report_fill_service_by_industry;
CREATE TRIGGER trg_report_fill_service_by_industry_audit
    BEFORE INSERT OR UPDATE ON report_fill_service_by_industry
    FOR EACH ROW
    EXECUTE FUNCTION fill_audit_time_fields();

DROP TRIGGER IF EXISTS trg_report_fill_info_source_by_industry_audit ON report_fill_info_source_by_industry;
CREATE TRIGGER trg_report_fill_info_source_by_industry_audit
    BEFORE INSERT OR UPDATE ON report_fill_info_source_by_industry
    FOR EACH ROW
    EXECUTE FUNCTION fill_audit_time_fields();

DROP TRIGGER IF EXISTS trg_report_fill_complaint_security_stat_audit ON report_fill_complaint_security_stat;
CREATE TRIGGER trg_report_fill_complaint_security_stat_audit
    BEFORE INSERT OR UPDATE ON report_fill_complaint_security_stat
    FOR EACH ROW
    EXECUTE FUNCTION fill_audit_time_fields();

DROP TRIGGER IF EXISTS trg_report_fill_info_collect_stat_audit ON report_fill_info_collect_stat;
CREATE TRIGGER trg_report_fill_info_collect_stat_audit
    BEFORE INSERT OR UPDATE ON report_fill_info_collect_stat
    FOR EACH ROW
    EXECUTE FUNCTION fill_audit_time_fields();

DROP TRIGGER IF EXISTS trg_report_fill_yangtze_credit_chain_audit ON report_fill_yangtze_credit_chain;
CREATE TRIGGER trg_report_fill_yangtze_credit_chain_audit
    BEFORE INSERT OR UPDATE ON report_fill_yangtze_credit_chain
    FOR EACH ROW
    EXECUTE FUNCTION fill_audit_time_fields();

