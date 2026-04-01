-- 工单模块（wmt-module-ticket）第一版表结构

CREATE TABLE IF NOT EXISTS `ticket_category` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '分类编号',
  `name` varchar(64) NOT NULL COMMENT '分类名称',
  `sort` int NOT NULL DEFAULT 0 COMMENT '显示排序',
  `status` tinyint NOT NULL DEFAULT 0 COMMENT '状态(0-启用,1-停用)',
  `creator` varchar(64) DEFAULT '' COMMENT '创建者',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updater` varchar(64) DEFAULT '' COMMENT '更新者',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否删除',
  PRIMARY KEY (`id`),
  KEY `idx_status_sort` (`status`, `sort`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='工单分类表';

CREATE TABLE IF NOT EXISTS `ticket` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '工单编号',
  `ticket_no` varchar(32) NOT NULL COMMENT '工单号',
  `user_id` bigint NOT NULL COMMENT '提交用户编号',
  `category_id` bigint NOT NULL COMMENT '分类编号',
  `title` varchar(128) NOT NULL COMMENT '标题',
  `content` text NOT NULL COMMENT '问题描述',
  `status` tinyint NOT NULL COMMENT '状态(10-已创建,20-处理中,40-已关闭)',
  `priority` tinyint NOT NULL COMMENT '优先级(1-低,2-中,3-高)',
  `source` tinyint NOT NULL COMMENT '来源(1-App,2-Admin)',
  `assigned_admin_user_id` bigint DEFAULT NULL COMMENT '指派管理员编号',
  `last_reply_time` datetime DEFAULT NULL COMMENT '最后回复时间',
  `closed_time` datetime DEFAULT NULL COMMENT '关闭时间',
  `creator` varchar(64) DEFAULT '' COMMENT '创建者',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updater` varchar(64) DEFAULT '' COMMENT '更新者',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否删除',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_ticket_no` (`ticket_no`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_status` (`status`),
  KEY `idx_assigned_admin_user_id` (`assigned_admin_user_id`),
  KEY `idx_category_id` (`category_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='工单主表';

CREATE TABLE IF NOT EXISTS `ticket_reply` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '回复编号',
  `ticket_id` bigint NOT NULL COMMENT '工单编号',
  `from_type` tinyint NOT NULL COMMENT '回复方类型(1-用户,2-管理员)',
  `user_id` bigint DEFAULT NULL COMMENT '回复用户编号',
  `admin_user_id` bigint DEFAULT NULL COMMENT '回复管理员编号',
  `content` text NOT NULL COMMENT '回复内容',
  `creator` varchar(64) DEFAULT '' COMMENT '创建者',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updater` varchar(64) DEFAULT '' COMMENT '更新者',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否删除',
  PRIMARY KEY (`id`),
  KEY `idx_ticket_id` (`ticket_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='工单回复表';

INSERT INTO `ticket_category` (`name`, `sort`, `status`, `creator`, `updater`, `deleted`)
SELECT '账号问题', 10, 0, 'system', 'system', b'0'
WHERE NOT EXISTS (SELECT 1 FROM `ticket_category` WHERE `name` = '账号问题' AND `deleted` = b'0');
