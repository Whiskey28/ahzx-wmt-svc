-- 长三角征信链（数据部）- PostgreSQL v15 - 2026-02-28
-- 说明：
-- 1) 固定字段、1:1：每条填报主记录对应一行，7 个数据项各一列
-- 2) 与 report_fill_basic_info 为 1:1，record_id 唯一
-- 3) 图中 7 项：工商数据(爬虫/商采)、税务数据(平台)、司法数据(商采)、税务违法数据(爬虫)、双公示数据(爬虫)、全国风险库(商采)

CREATE EXTENSION IF NOT EXISTS pgcrypto;

-- ============================
-- report_fill_yangtze_credit_chain（长三角征信链，固定字段 1:1）
-- ============================
-- 注意：测试/生产增量升级场景禁止 DROP；本脚本需可重复执行
CREATE TABLE IF NOT EXISTS public.report_fill_yangtze_credit_chain (
  id                                    VARCHAR(32) NOT NULL DEFAULT replace(gen_random_uuid()::text, '-', ''),
  record_id                             VARCHAR(32) NOT NULL,

  -- 7 项固定字段（每项一列，可存数量或标识）
  yangtze_industry_crawler               NUMERIC(18, 0),
  yangtze_industry_purchase              NUMERIC(18, 0),
  yangtze_tax_platform                   NUMERIC(18, 0),
  yangtze_judicial_purchase              NUMERIC(18, 0),
  yangtze_tax_violation_crawler          NUMERIC(18, 0),
  yangtze_double_publicity_crawler       NUMERIC(18, 0),
  yangtze_national_risk_purchase         NUMERIC(18, 0),

  create_time                            TIMESTAMP,
  update_time                            TIMESTAMP,
  creator                                VARCHAR(64),
  updater                                VARCHAR(64),
  deleted                                int2 NOT NULL DEFAULT 0
);

DO $$
BEGIN
  IF NOT EXISTS (
    SELECT 1
    FROM pg_constraint
    WHERE conrelid = 'public.report_fill_yangtze_credit_chain'::regclass
      AND contype = 'p'
  ) THEN
    ALTER TABLE public.report_fill_yangtze_credit_chain
      ADD CONSTRAINT pk_report_fill_yangtze_credit_chain PRIMARY KEY (id);
  END IF;
END $$;

CREATE UNIQUE INDEX IF NOT EXISTS uk_report_fill_yangtze_credit_chain_record_id
  ON public.report_fill_yangtze_credit_chain (record_id, deleted);
CREATE INDEX IF NOT EXISTS idx_report_fill_yangtze_credit_chain_record_id
  ON public.report_fill_yangtze_credit_chain (record_id);

COMMENT ON TABLE public.report_fill_yangtze_credit_chain IS '报表填报-长三角征信链（数据部，固定字段 1:1）';
COMMENT ON COLUMN public.report_fill_yangtze_credit_chain.id IS '主键（32位UUID，无连字符）';
COMMENT ON COLUMN public.report_fill_yangtze_credit_chain.record_id IS '主记录id（= report_fill_basic_info.id）';
COMMENT ON COLUMN public.report_fill_yangtze_credit_chain.yangtze_industry_crawler IS '工商数据-爬虫';
COMMENT ON COLUMN public.report_fill_yangtze_credit_chain.yangtze_industry_purchase IS '工商数据-商采';
COMMENT ON COLUMN public.report_fill_yangtze_credit_chain.yangtze_tax_platform IS '税务数据-平台';
COMMENT ON COLUMN public.report_fill_yangtze_credit_chain.yangtze_judicial_purchase IS '司法数据-商采';
COMMENT ON COLUMN public.report_fill_yangtze_credit_chain.yangtze_tax_violation_crawler IS '税务违法数据-爬虫';
COMMENT ON COLUMN public.report_fill_yangtze_credit_chain.yangtze_double_publicity_crawler IS '双公示数据-爬虫';
COMMENT ON COLUMN public.report_fill_yangtze_credit_chain.yangtze_national_risk_purchase IS '全国风险库-商采';
