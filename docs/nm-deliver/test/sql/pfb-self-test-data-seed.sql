-- =============================================================================
-- PFB 开发自测 — 测试数据导入（幂等：先删后插固定 UUID，仅影响下列主键）
-- 使用方式：mysql ... wmt_pfb < docs/nm-deliver/test/sql/pfb-self-test-data-seed.sql
-- 说明：不写入 system_users；认证/企业/进件仍走接口或另行造数。
-- =============================================================================

SET NAMES utf8mb4;

DELETE FROM `pfb_loan_product` WHERE `id` IN (
  '11111111-1111-1111-1111-111111111101',
  '11111111-1111-1111-1111-111111111102',
  '11111111-1111-1111-1111-111111111103'
);
DELETE FROM `pfb_banner` WHERE `id` IN (
  '22222222-2222-2222-2222-222222222201',
  '22222222-2222-2222-2222-222222222202'
);

-- 上架 + 热门（用于 list / hot-list / get）
INSERT INTO `pfb_loan_product` (
  `id`, `product_code`, `product_name`, `product_term`, `repayment_method`, `product_tags`,
  `is_hot`, `product_status`, `sort_order`, `max_amount_limit`, `min_rate_limit`,
  `product_effective_date`, `product_desc`, `creator`, `deleted`
) VALUES (
  '11111111-1111-1111-1111-111111111101',
  'PFB-SELF-001',
  '自测产品-热门上架',
  '12个月',
  '等额本息',
  '普惠,自测',
  1, 1, 10,
  5000000.00, 0.035000,
  NOW(), '自测种子数据：热门、上架',
  'dev-self-test', 0
);

-- 上架 + 非热门
INSERT INTO `pfb_loan_product` (
  `id`, `product_code`, `product_name`, `product_term`, `repayment_method`, `product_tags`,
  `is_hot`, `product_status`, `sort_order`, `max_amount_limit`, `min_rate_limit`,
  `product_effective_date`, `product_desc`, `creator`, `deleted`
) VALUES (
  '11111111-1111-1111-1111-111111111102',
  'PFB-SELF-002',
  '自测产品-上架非热门',
  '24个月',
  '先息后本',
  '自测',
  0, 1, 20,
  3000000.00, 0.040000,
  NOW(), '自测种子数据：上架、非热门',
  'dev-self-test', 0
);

-- 下架（用于 get 负例：未上架）
INSERT INTO `pfb_loan_product` (
  `id`, `product_code`, `product_name`, `product_term`, `repayment_method`, `product_tags`,
  `is_hot`, `product_status`, `sort_order`, `max_amount_limit`, `min_rate_limit`,
  `product_effective_date`, `product_desc`, `creator`, `deleted`
) VALUES (
  '11111111-1111-1111-1111-111111111103',
  'PFB-SELF-OFF',
  '自测产品-已下架',
  '6个月',
  '到期还本',
  NULL,
  0, 0, 99,
  1000000.00, 0.045000,
  NOW(), '自测种子数据：下架',
  'dev-self-test', 0
);

-- 上架 Banner（时间窗覆盖当前）
INSERT INTO `pfb_banner` (
  `id`, `title`, `image_url`, `link_url`, `sort_order`, `banner_status`,
  `effective_begin`, `effective_end`, `creator`, `deleted`
) VALUES (
  '22222222-2222-2222-2222-222222222201',
  '自测Banner-上架',
  'https://example.com/pfb-self-test-banner.png',
  'https://example.com/pfb',
  1, 1,
  DATE_SUB(NOW(), INTERVAL 1 DAY), DATE_ADD(NOW(), INTERVAL 365 DAY),
  'dev-self-test', 0
);

-- 下架 Banner（列表应不可见）
INSERT INTO `pfb_banner` (
  `id`, `title`, `image_url`, `link_url`, `sort_order`, `banner_status`,
  `effective_begin`, `effective_end`, `creator`, `deleted`
) VALUES (
  '22222222-2222-2222-2222-222222222202',
  '自测Banner-下架',
  'https://example.com/pfb-self-test-banner-off.png',
  NULL,
  2, 0,
  NULL, NULL,
  'dev-self-test', 0
);
