-- [注意] 本文件是 MySQL 方言示例，不适用于 PostgreSQL v15。
-- PostgreSQL 版本请使用：sql/postgresql/credit-report-2026-01-29.sql
--
-- 信用报送（积木表单落库：A 方案）- 2026-01-29
-- 说明：
-- 1) report_fill_basic_info 作为“填报主记录（record）锚点”，唯一约束：dept_id + period_id + report_id
-- 2) 企业基础信息等放入 1:1 块表 report_fill_enterprise_basic（主键与主记录 id 一致）
-- 3) 明细表统一继承 BaseDO 的审计/逻辑删除字段（create_time/update_time/creator/updater/deleted）

-- ----------------------------
-- Table structure for report_fill_basic_info（主记录锚点）
-- ----------------------------
-- 若线上已存在此表，请按实际情况执行 DROP/ALTER
CREATE TABLE IF NOT EXISTS `report_fill_basic_info` (
  `id` BIGINT NOT NULL COMMENT '主键（record_id）',
  `period_id` VARCHAR(16) NOT NULL COMMENT '填报周期（如 2025-01 / 2025-Q1）',
  `dept_id` VARCHAR(64) NOT NULL COMMENT '部门id',
  `project_id` VARCHAR(64) NULL DEFAULT NULL COMMENT '项目id（可选，不参与唯一约束）',
  `report_id` VARCHAR(64) NOT NULL COMMENT '报表模板id（jimu_report.id）',
  `create_time` DATETIME NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` DATETIME NULL DEFAULT NULL COMMENT '更新时间',
  `creator` VARCHAR(64) NULL DEFAULT NULL COMMENT '创建者',
  `updater` VARCHAR(64) NULL DEFAULT NULL COMMENT '更新者',
  `deleted` BIT(1) NULL DEFAULT b'0' COMMENT '是否删除',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_dept_period_report` (`dept_id`,`period_id`,`report_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='报表填报主记录（锚点）';

-- ----------------------------
-- Table structure for report_fill_enterprise_basic（企业基础信息块 1:1）
-- ----------------------------
CREATE TABLE IF NOT EXISTS `report_fill_enterprise_basic` (
  `id` BIGINT NOT NULL COMMENT '主键（= report_fill_basic_info.id）',
  `enterprise_name` VARCHAR(256) NULL DEFAULT NULL COMMENT '企业名称',
  `credit_code` VARCHAR(64) NULL DEFAULT NULL COMMENT '统一社会信用代码',
  `address` VARCHAR(512) NULL DEFAULT NULL COMMENT '注册地/地址',
  `legal_person` VARCHAR(128) NULL DEFAULT NULL COMMENT '法定代表人',
  `register_date` DATE NULL DEFAULT NULL COMMENT '注册时间',
  `biz_place` VARCHAR(512) NULL DEFAULT NULL COMMENT '经营场所',
  `record_no` VARCHAR(128) NULL DEFAULT NULL COMMENT '备案编号',
  `record_date` DATE NULL DEFAULT NULL COMMENT '备案时间',
  `record_branch` VARCHAR(256) NULL DEFAULT NULL COMMENT '备案分支行/机构',
  `reg_cap_million` DECIMAL(18,6) NULL DEFAULT NULL COMMENT '注册资本（万元）',
  `paid_in_cap_million` DECIMAL(18,6) NULL DEFAULT NULL COMMENT '实缴资本（万元）',
  `isms_level` VARCHAR(64) NULL DEFAULT NULL COMMENT '信息系统等级保护评定安全等级',
  `service_network_type` VARCHAR(64) NULL DEFAULT NULL COMMENT '服务网络形式',
  `branch_count` INT NULL DEFAULT NULL COMMENT '分支机构数量',
  `controlling_shareholder` VARCHAR(256) NULL DEFAULT NULL COMMENT '控股股东',
  `controlling_shareholder_ratio` DECIMAL(9,4) NULL DEFAULT NULL COMMENT '控股股东股份占比（%）',
  `controlling_shareholder_type` VARCHAR(64) NULL DEFAULT NULL COMMENT '控股股东类型',
  `contact_name` VARCHAR(128) NULL DEFAULT NULL COMMENT '联系人',
  `contact_phone` VARCHAR(64) NULL DEFAULT NULL COMMENT '联系电话',
  `contact_email` VARCHAR(128) NULL DEFAULT NULL COMMENT '联系邮箱',
  `create_time` DATETIME NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` DATETIME NULL DEFAULT NULL COMMENT '更新时间',
  `creator` VARCHAR(64) NULL DEFAULT NULL COMMENT '创建者',
  `updater` VARCHAR(64) NULL DEFAULT NULL COMMENT '更新者',
  `deleted` BIT(1) NULL DEFAULT b'0' COMMENT '是否删除',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='报表填报-企业基础信息块（1:1）';

-- ----------------------------
-- Table structure for report_fill_info_user_org_item（信息使用者机构明细 1:N）
-- ----------------------------
CREATE TABLE IF NOT EXISTS `report_fill_info_user_org_item` (
  `id` BIGINT NOT NULL COMMENT '主键',
  `record_id` BIGINT NOT NULL COMMENT '主记录id',
  `org_name` VARCHAR(256) NOT NULL COMMENT '机构名称',
  `industry_code` VARCHAR(64) NULL DEFAULT NULL COMMENT '行业分类（字典，可扩展）',
  `is_current_service` BIT(1) NULL DEFAULT b'0' COMMENT '是否当前使用服务',
  `sort_no` INT NULL DEFAULT NULL COMMENT '序号/排序',
  `create_time` DATETIME NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` DATETIME NULL DEFAULT NULL COMMENT '更新时间',
  `creator` VARCHAR(64) NULL DEFAULT NULL COMMENT '创建者',
  `updater` VARCHAR(64) NULL DEFAULT NULL COMMENT '更新者',
  `deleted` BIT(1) NULL DEFAULT b'0' COMMENT '是否删除',
  PRIMARY KEY (`id`),
  KEY `idx_record_id` (`record_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='报表填报-信息使用者机构明细';

-- ----------------------------
-- Table structure for report_fill_info_user_gov_item（信息使用者机构+政府 明细 1:N）
-- ----------------------------
CREATE TABLE IF NOT EXISTS `report_fill_info_user_gov_item` (
  `id` BIGINT NOT NULL COMMENT '主键',
  `record_id` BIGINT NOT NULL COMMENT '主记录id',
  `gov_org_name` VARCHAR(256) NOT NULL COMMENT '政府机构名称',
  `is_current_service` BIT(1) NULL DEFAULT b'0' COMMENT '是否当前使用服务',
  `sort_no` INT NULL DEFAULT NULL COMMENT '序号/排序',
  `create_time` DATETIME NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` DATETIME NULL DEFAULT NULL COMMENT '更新时间',
  `creator` VARCHAR(64) NULL DEFAULT NULL COMMENT '创建者',
  `updater` VARCHAR(64) NULL DEFAULT NULL COMMENT '更新者',
  `deleted` BIT(1) NULL DEFAULT b'0' COMMENT '是否删除',
  PRIMARY KEY (`id`),
  KEY `idx_record_id` (`record_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='报表填报-信息使用者机构+政府明细';

