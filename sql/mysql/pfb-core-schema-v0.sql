-- =============================================================================
-- 普惠金融（小程序进件）— 核心表结构 v0
-- 交付说明：
--   1. 业务表统一前缀 pfb_；
--   2. 主键一律 CHAR(36) 存 UUID（应用层生成，不用数据库自增整型）；
--   3. 与 system_users：不改核心表；用 pfb_system_user_ext（一对一快照）+ 关联表（多对多/扩展）。
--   4. 自然人客户主数据表名：pfb_individual_customer（非「手机号用户」语义；登录手机号仍在 system_users）。
--   5. 审计与逻辑删除列与 wmt BaseDO 对齐：creator、create_time、updater、update_time、deleted（无 tenant_id）。
--      deleted 与各布尔含义列统一 TINYINT(1) 0/1，便于 JDBC/MP；Java DO 继承 BaseDO，@TableLogic 映射 0/1。
--      若全局启用多租户拦截，实体类上加 @TenantIgnore。
-- =============================================================================

SET NAMES utf8mb4;

-- -----------------------------------------------------------------------------
-- 首页轮播图（Banner）
-- -----------------------------------------------------------------------------
DROP TABLE IF EXISTS `pfb_loan_application`;
DROP TABLE IF EXISTS `pfb_system_user_ext`;
DROP TABLE IF EXISTS `pfb_system_user_ent_info`;
DROP TABLE IF EXISTS `pfb_system_user_individual_customer`;
DROP TABLE IF EXISTS `pfb_banner`;
DROP TABLE IF EXISTS `pfb_loan_product`;
DROP TABLE IF EXISTS `pfb_ent_info`;
DROP TABLE IF EXISTS `pfb_individual_customer`;

CREATE TABLE `pfb_individual_customer` (
    `id` CHAR(36) NOT NULL COMMENT '自然人客户UUID主键（与登录手机号解耦；手机号在 system_users）',
    `real_name` VARCHAR(64) NOT NULL COMMENT '客户姓名',
    `id_card_no` VARCHAR(32) NOT NULL COMMENT '身份证号（18位或末位X）',
    `address` VARCHAR(512) DEFAULT NULL COMMENT '联系地址',
    `is_verified` TINYINT(1) NOT NULL DEFAULT 0 COMMENT '是否已实名认证(0否,1是)',
    `id_card_front_image` VARCHAR(512) DEFAULT NULL COMMENT '身份证正面图片URL',
    `id_card_back_image` VARCHAR(512) DEFAULT NULL COMMENT '身份证反面图片URL',
    `creator` VARCHAR(64) DEFAULT '' COMMENT '创建者（BaseDO：SysUser 编号字符串）',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updater` VARCHAR(64) DEFAULT '' COMMENT '更新者（BaseDO：SysUser 编号字符串）',
    `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `deleted` TINYINT(1) NOT NULL DEFAULT 0 COMMENT '是否删除(0否,1是)，对应 BaseDO @TableLogic',
    PRIMARY KEY (`id`),
    KEY `idx_pfb_icust_id_card` (`id_card_no`),
    KEY `idx_pfb_icust_deleted` (`deleted`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='自然人客户信息表（进件扩展身份，不等同于登录账号）';

-- -----------------------------------------------------------------------------
-- 企业基本信息（对应原 ent_info；布尔位与 deleted 为 TINYINT(1)；长度按字段语义收紧）
-- -----------------------------------------------------------------------------
CREATE TABLE `pfb_ent_info` (
    `id` CHAR(36) NOT NULL COMMENT '企业UUID主键',
    `enterprise_name` VARCHAR(256) DEFAULT NULL COMMENT '企业中文名称',
    `english_name` VARCHAR(256) DEFAULT NULL COMMENT '企业英文名称',
    `company_credit_code` VARCHAR(18) NOT NULL COMMENT '统一社会信用代码（18位）；企业主数据必填，与进件及授权书一致',
    `parent_company_name` VARCHAR(256) DEFAULT NULL COMMENT '上级企业名称',
    `parent_company_credit_code` VARCHAR(18) DEFAULT NULL COMMENT '上级统一社会信用代码',
    `industry_nature` VARCHAR(64) DEFAULT NULL COMMENT '行业性质（字典 industry_nature，存编码或短码）',
    `agri_production_category` VARCHAR(64) DEFAULT NULL COMMENT '农业生产分类（字典 agri_production_category）',
    `core_contact` VARCHAR(32) DEFAULT NULL COMMENT '核心联系方式（电话等）',
    `holding_type` VARCHAR(64) DEFAULT NULL COMMENT '控股类型（字典 holding_type）',
    `customer_type_sub` VARCHAR(64) DEFAULT NULL COMMENT '客户类型细分（字典 customer_type_sub）',
    `national_industry_category` VARCHAR(64) DEFAULT NULL COMMENT '国家行业分类（字典 national_industry_category）',
    `is_admin_law_enforcement` TINYINT(1) DEFAULT NULL COMMENT '是否行政执法类客户(0否,1是)',
    `register_address_detail` VARCHAR(512) DEFAULT NULL COMMENT '注册地址详细地址',
    `listed_company_type` VARCHAR(64) DEFAULT NULL COMMENT '上市公司类型（字典 listed_company_type）',
    `has_import_export_right` TINYINT(1) DEFAULT NULL COMMENT '有无进出口经营权(0无,1有)',
    `financial_statement_type` VARCHAR(64) DEFAULT NULL COMMENT '财务报表类型（字典 financial_statement_type）',
    `bank_credit_rating` VARCHAR(32) DEFAULT NULL COMMENT '本行即期信用等级（字典 bank_credit_rating）',
    `employee_count` INT DEFAULT NULL COMMENT '在职人数',
    `business_site_area` VARCHAR(64) DEFAULT NULL COMMENT '经营场地面积（可含单位，如「500㎡」）',
    `business_scope` VARCHAR(2000) DEFAULT NULL COMMENT '经营范围（可能较长）',
    `business_site_ownership` VARCHAR(64) DEFAULT NULL COMMENT '经营场地所有权（字典 business_site_ownership）',
    `qualification_management_level` VARCHAR(512) DEFAULT NULL COMMENT '历定资质、管理水平',
    `legal_operation_status` VARCHAR(512) DEFAULT NULL COMMENT '合法经营情况',
    `register_date` VARCHAR(32) DEFAULT NULL COMMENT '企业注册日期（原文形态；若需 DATE 可后续迁移）',
    `main_market_or_products` VARCHAR(2000) DEFAULT NULL COMMENT '主要市场或主营产品情况',
    `is_non_public_economy` TINYINT(1) DEFAULT NULL COMMENT '是否非公经济(0否,1是)',
    `operation_start_date` DATETIME DEFAULT NULL COMMENT '企业经营开始日期',
    `last_year_end_net_assets_income` VARCHAR(128) DEFAULT NULL COMMENT '上年末净资产收入（原文多为文本描述）',
    `is_county_economy` TINYINT(1) DEFAULT NULL COMMENT '是否县域经济(0否,1是)',
    `is_green` TINYINT(1) DEFAULT NULL COMMENT '是否绿色(0否,1是)',
    `tax_no` VARCHAR(64) DEFAULT NULL COMMENT '税务登记证号(国税)',
    `setup_date` DATETIME DEFAULT NULL COMMENT '企业成立日期',
    `org_type` VARCHAR(64) DEFAULT NULL COMMENT '企业类型（字典或短文本）',
    `scope` VARCHAR(64) DEFAULT NULL COMMENT '企业规模（字典）',
    `org_nature` VARCHAR(64) DEFAULT NULL COMMENT '机构类型（字典）',
    `enterprise_belong` VARCHAR(128) DEFAULT NULL COMMENT '机构隶属',
    `rc_currency` VARCHAR(16) DEFAULT NULL COMMENT '注册资本币种（ISO 4217 或行内码）',
    `register_capital` VARCHAR(64) DEFAULT NULL COMMENT '注册资本（可含单位/币种说明）',
    `pc_currency` VARCHAR(16) DEFAULT NULL COMMENT '实收资本币种',
    `paiclup_capital` VARCHAR(64) DEFAULT NULL COMMENT '实收资本（字段名保留业务历史拼写）',
    `country_code` VARCHAR(8) DEFAULT NULL COMMENT '注册国家/地区编码',
    `region_code` VARCHAR(64) DEFAULT NULL COMMENT '省市区编码或规范名称',
    `email_add` VARCHAR(128) DEFAULT NULL COMMENT '公司E-Mail',
    `web_add` VARCHAR(512) DEFAULT NULL COMMENT '公司网址',
    `contact_phone` VARCHAR(32) DEFAULT NULL COMMENT '联系电话',
    `contact_address` VARCHAR(512) DEFAULT NULL COMMENT '联系地址',
    `fictitious_person` VARCHAR(64) DEFAULT NULL COMMENT '法人代表姓名',
    `fictitious_person_id` VARCHAR(32) DEFAULT NULL COMMENT '法人代表身份证号',
    `creator` VARCHAR(64) DEFAULT '' COMMENT '创建者（BaseDO：SysUser 编号字符串）',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updater` VARCHAR(64) DEFAULT '' COMMENT '更新者（BaseDO：SysUser 编号字符串）',
    `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `deleted` TINYINT(1) NOT NULL DEFAULT 0 COMMENT '是否删除（0否,1是），对应 BaseDO @TableLogic',
    PRIMARY KEY (`id`),
    KEY `idx_pfb_ent_credit_code` (`company_credit_code`),
    KEY `idx_pfb_ent_deleted` (`deleted`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='企业基本信息';

-- -----------------------------------------------------------------------------
-- 贷款产品
-- -----------------------------------------------------------------------------
CREATE TABLE `pfb_loan_product` (
    `id` CHAR(36) NOT NULL COMMENT '产品UUID主键',
    `product_code` VARCHAR(64) DEFAULT NULL COMMENT '产品编号',
    `product_name` VARCHAR(256) DEFAULT NULL COMMENT '产品名称',
    `product_term` VARCHAR(128) DEFAULT NULL COMMENT '产品期限（展示文案或编码）',
    `repayment_method` VARCHAR(256) DEFAULT NULL COMMENT '还款方式（展示或编码列表）',
    `product_tags` VARCHAR(256) DEFAULT NULL COMMENT '产品标签',
    `is_hot` TINYINT(1) NOT NULL DEFAULT 0 COMMENT '是否热门产品(0否,1是)',
    `product_status` TINYINT(1) NOT NULL DEFAULT 1 COMMENT '产品状态(0下架,1上架)；含义以业务字典为准',
    `sort_order` INT NOT NULL DEFAULT 0 COMMENT '展示排序，数值越小越靠前；列表 ORDER BY sort_order, id',
    `max_amount_limit` DECIMAL(18,2) DEFAULT NULL COMMENT '额度上限（与行方展示口径一致，默认元）',
    `min_rate_limit` DECIMAL(10,6) DEFAULT NULL COMMENT '最低利率（比例小数，精度按行方）',
    `product_effective_date` DATETIME DEFAULT NULL COMMENT '产品生效日期',
    `product_desc` VARCHAR(1024) DEFAULT NULL COMMENT '产品简介',
    `creator` VARCHAR(64) DEFAULT '' COMMENT '创建者（BaseDO：SysUser 编号字符串）',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updater` VARCHAR(64) DEFAULT '' COMMENT '更新者（BaseDO：SysUser 编号字符串）',
    `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `deleted` TINYINT(1) NOT NULL DEFAULT 0 COMMENT '是否删除(0否,1是)，对应 BaseDO @TableLogic',
    PRIMARY KEY (`id`),
    KEY `idx_pfb_loan_product_code` (`product_code`),
    KEY `idx_pfb_loan_product_status_hot` (`product_status`, `is_hot`),
    KEY `idx_pfb_loan_product_list` (`deleted`, `product_status`, `sort_order`, `id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='贷款产品表';

-- -----------------------------------------------------------------------------
-- 首页 Banner 轮播图
-- -----------------------------------------------------------------------------
CREATE TABLE `pfb_banner` (
    `id` CHAR(36) NOT NULL COMMENT '轮播图UUID主键',
    `title` VARCHAR(128) DEFAULT NULL COMMENT '标题（可选，用于运营或无障碍）',
    `image_url` VARCHAR(768) NOT NULL COMMENT '图片地址（CDN/对象存储 URL）',
    `link_url` VARCHAR(768) DEFAULT NULL COMMENT '点击跳转链接（可选；无则仅展示）',
    `sort_order` INT NOT NULL DEFAULT 0 COMMENT '展示排序，越小越靠前',
    `banner_status` TINYINT(1) NOT NULL DEFAULT 1 COMMENT '0下架 1上架',
    `effective_begin` DATETIME DEFAULT NULL COMMENT '展示开始时间（空表示不限制）',
    `effective_end` DATETIME DEFAULT NULL COMMENT '展示结束时间（空表示不限制）',
    `creator` VARCHAR(64) DEFAULT '' COMMENT '创建者（BaseDO）',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updater` VARCHAR(64) DEFAULT '' COMMENT '更新者（BaseDO）',
    `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `deleted` TINYINT(1) NOT NULL DEFAULT 0 COMMENT '是否删除(0否,1是)，对应 BaseDO @TableLogic',
    PRIMARY KEY (`id`),
    KEY `idx_pfb_banner_list` (`deleted`, `banner_status`, `sort_order`, `id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='小程序首页轮播图';

-- -----------------------------------------------------------------------------
-- 关联表：后台 system_users ↔ 自然人客户 pfb_individual_customer
-- -----------------------------------------------------------------------------
CREATE TABLE `pfb_system_user_individual_customer` (
    `id` CHAR(36) NOT NULL COMMENT '关联记录UUID主键',
    `sys_user_id` BIGINT NOT NULL COMMENT '后台 system_users.id',
    `individual_customer_id` CHAR(36) NOT NULL COMMENT '自然人客户 pfb_individual_customer.id',
    `creator` VARCHAR(64) DEFAULT '' COMMENT '创建者（BaseDO）',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updater` VARCHAR(64) DEFAULT '' COMMENT '更新者（BaseDO）',
    `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `deleted` TINYINT(1) NOT NULL DEFAULT 0 COMMENT '是否删除（0否,1是），对应 BaseDO @TableLogic',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_pfb_sys_user_individual` (`sys_user_id`, `individual_customer_id`, `deleted`),
    KEY `idx_pfb_ssuic_individual` (`individual_customer_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='后台账号与自然人客户关联表（扩展信息，不替代框架用户表设计）';

-- -----------------------------------------------------------------------------
-- 关联表：后台 system_users ↔ 企业 pfb_ent_info（支持多企业）
-- -----------------------------------------------------------------------------
CREATE TABLE `pfb_system_user_ent_info` (
    `id` CHAR(36) NOT NULL COMMENT '关联记录UUID主键',
    `sys_user_id` BIGINT NOT NULL COMMENT '后台 system_users.id',
    `ent_id` CHAR(36) NOT NULL COMMENT '企业 pfb_ent_info.id',
    `is_default` TINYINT(1) NOT NULL DEFAULT 0 COMMENT '是否默认企业(0否,1是)',
    `creator` VARCHAR(64) DEFAULT '' COMMENT '创建者（BaseDO）',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updater` VARCHAR(64) DEFAULT '' COMMENT '更新者（BaseDO）',
    `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `deleted` TINYINT(1) NOT NULL DEFAULT 0 COMMENT '是否删除（0否,1是），对应 BaseDO @TableLogic',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_pfb_sys_user_ent` (`sys_user_id`, `ent_id`, `deleted`),
    KEY `idx_pfb_suei_ent` (`ent_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='后台账号与企业关联表';

-- -----------------------------------------------------------------------------
-- system_users 一对一扩展（不改核心表；快照与关联表并存）
-- -----------------------------------------------------------------------------
CREATE TABLE `pfb_system_user_ext` (
    `sys_user_id` BIGINT NOT NULL COMMENT '对应 system_users.id，一对一扩展主键',
    `current_individual_customer_id` CHAR(36) DEFAULT NULL COMMENT '当前绑定的自然人客户 pfb_individual_customer.id（快照）',
    `default_ent_id` CHAR(36) DEFAULT NULL COMMENT '当前默认企业 pfb_ent_info.id（快照）',
    `creator` VARCHAR(64) DEFAULT '' COMMENT '创建者（BaseDO）',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updater` VARCHAR(64) DEFAULT '' COMMENT '更新者（BaseDO）',
    `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `deleted` TINYINT(1) NOT NULL DEFAULT 0 COMMENT '扩展行逻辑删除(0否,1是)，对应 BaseDO @TableLogic',
    PRIMARY KEY (`sys_user_id`),
    KEY `idx_pfb_suext_individual` (`current_individual_customer_id`),
    KEY `idx_pfb_suext_ent` (`default_ent_id`),
    KEY `idx_pfb_suext_deleted` (`deleted`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='普惠金融：system_users 一对一扩展（替代 ALTER 核心表）';

-- -----------------------------------------------------------------------------
-- 融资申请（含申请企业主体与授权书页快照，对齐 PRD US-09/US-12）
-- -----------------------------------------------------------------------------
CREATE TABLE `pfb_loan_application` (
    `id` CHAR(36) NOT NULL COMMENT '申请UUID主键',
    `sys_user_id` BIGINT NOT NULL COMMENT '后台登录用户 system_users.id',
    `individual_customer_id` CHAR(36) DEFAULT NULL COMMENT '提交时关联的自然人客户 pfb_individual_customer.id',
    `ent_id` CHAR(36) NOT NULL COMMENT '申请主体企业 pfb_ent_info.id',
    `enterprise_name_snapshot` VARCHAR(256) NOT NULL COMMENT '提交时企业名称快照（授权书与审计；与 ent_id 同时写入）',
    `company_credit_code_snapshot` VARCHAR(18) NOT NULL COMMENT '提交时统一社会信用代码快照（缺失则不应允许提交申请）',
    `product_id` CHAR(36) NOT NULL COMMENT '贷款产品 pfb_loan_product.id',
    `apply_amount_cent` BIGINT NOT NULL COMMENT '申请金额（分），须为 10000 元整数倍',
    `term_months` INT NOT NULL COMMENT '申请期限（月），如 6 或 12',
    `repay_method` VARCHAR(32) NOT NULL COMMENT '还款方式编码，与 PRD 枚举一致',
    `credit_auth_accepted` TINYINT(1) NOT NULL DEFAULT 0 COMMENT '是否勾选征信查询报送授权(0否,1是)',
    `status` VARCHAR(32) NOT NULL DEFAULT 'SUBMITTED' COMMENT '受理状态',
    `creator` VARCHAR(64) DEFAULT '' COMMENT '创建者（BaseDO）',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updater` VARCHAR(64) DEFAULT '' COMMENT '更新者（BaseDO）',
    `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `deleted` TINYINT(1) NOT NULL DEFAULT 0 COMMENT '是否删除(0否,1是)，对应 BaseDO @TableLogic',
    PRIMARY KEY (`id`),
    KEY `idx_pfb_loan_app_sys_user` (`sys_user_id`),
    KEY `idx_pfb_loan_app_ent` (`ent_id`),
    KEY `idx_pfb_loan_app_user_ent_time` (`sys_user_id`, `ent_id`, `create_time`),
    KEY `idx_pfb_loan_app_product` (`product_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='融资申请';
