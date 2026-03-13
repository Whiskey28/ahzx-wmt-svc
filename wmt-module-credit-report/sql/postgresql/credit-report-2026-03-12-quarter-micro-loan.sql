-- 信用报送 - 季报（小微贷款）落库 - PostgreSQL v15 - 2026-03-12
-- 说明：
-- 1) 仍以 report_fill_basic_info 作为填报锚点（period_id 可使用 2026Q1 这种格式）
-- 2) 本表为 1:1 宽表，使用 parent_id 关联主记录 id
-- 3) 比率字段以“百分数”存储（例如 3.25 表示 3.25%）

CREATE EXTENSION IF NOT EXISTS pgcrypto;

DROP TABLE IF EXISTS report_fill_quarter_micro_loan;
CREATE TABLE report_fill_quarter_micro_loan (
  id                  VARCHAR(32) NOT NULL DEFAULT replace(gen_random_uuid()::text, '-', ''),
  parent_id           VARCHAR(32) NOT NULL,

  apply_households    NUMERIC(18, 6),
  grant_households    NUMERIC(18, 6),
  grant_amount        NUMERIC(18, 6),
  credit_grant_amount NUMERIC(18, 6),

  avg_annual_rate_pct NUMERIC(18, 6),
  npl_rate_pct        NUMERIC(18, 6),

  create_time         TIMESTAMP,
  update_time         TIMESTAMP,
  creator             VARCHAR(64),
  updater             VARCHAR(64),
  deleted             int2 NOT NULL DEFAULT 0
);

ALTER TABLE report_fill_quarter_micro_loan
  ADD CONSTRAINT pk_report_fill_quarter_micro_loan PRIMARY KEY (id);

CREATE UNIQUE INDEX uk_report_fill_quarter_micro_loan_parent_id
  ON report_fill_quarter_micro_loan (parent_id, deleted);

CREATE INDEX idx_report_fill_quarter_micro_loan_parent_id
  ON report_fill_quarter_micro_loan (parent_id);

COMMENT ON TABLE report_fill_quarter_micro_loan IS '报表填报-季报（小微贷款，1:1，UUID主键）';
COMMENT ON COLUMN report_fill_quarter_micro_loan.parent_id IS '父记录id（= report_fill_basic_info.id）';

COMMENT ON COLUMN report_fill_quarter_micro_loan.apply_households IS '小微企业申请贷款户数（当季发生额）';
COMMENT ON COLUMN report_fill_quarter_micro_loan.grant_households IS '小微企业获得贷款户数（当季发生额）';
COMMENT ON COLUMN report_fill_quarter_micro_loan.grant_amount IS '小微企业获得贷款金额（当季发生额）';
COMMENT ON COLUMN report_fill_quarter_micro_loan.credit_grant_amount IS '其中：信用贷款金额（当季发生额）';
COMMENT ON COLUMN report_fill_quarter_micro_loan.avg_annual_rate_pct IS '平均年利率（按金额加权，百分数）';
COMMENT ON COLUMN report_fill_quarter_micro_loan.npl_rate_pct IS '不良率（按金额加权，百分数）';

