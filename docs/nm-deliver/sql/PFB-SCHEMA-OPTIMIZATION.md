# 普惠金融核心表（`pfb_*`）— MySQL 优化与演进建议

> 基准脚本：`pfb-core-schema-v0.sql`  
> 角色：面向后续数据量增长、报表与监管查询的表结构/索引/类型演进说明，供 DBA 与后端评审。

---

## 0. 与 wmt `BaseDO` 的列对齐（实施必读）

物理表字段与 `com.wmt.framework.mybatis.core.dataobject.BaseDO` 一致：

| Java（BaseDO） | MySQL 列 | 说明 |
|----------------|----------|------|
| `creator` | `creator` VARCHAR(64) | 创建者，框架为 **String**（SysUser id 等） |
| `createTime` | `create_time` DATETIME | `FieldFill.INSERT` |
| `updater` | `updater` VARCHAR(64) | 更新者 |
| `updateTime` | `update_time` DATETIME | `FieldFill.INSERT_UPDATE` |
| `deleted` | `deleted` **TINYINT(1)** | `@TableLogic`，**0 未删 / 1 已删**（与脚本 v0 一致；勿再使用 BIT） |

**不包含** `tenant_id`：本域表暂不支持多租户；DO 继承 `BaseDO` 时若工程启用了租户拦截，实体上增加 **`@TenantIgnore`**（与 `DictDataDO` 等同款处理）。

**布尔与状态标志**：与 `deleted` 同型，统一 **`TINYINT(1)`**，避免 JDBC 对 `BIT` 映射差异。

---

## 0.1 命名与域模型（与 v0 DDL 一致）

| 原命名（已废弃） | 现命名 | 说明 |
|------------------|--------|------|
| `pfb_mobile_user` | **`pfb_individual_customer`** | 自然人进件客户主数据；**不等于**「手机号登录用户」（手机号在 `system_users`） |
| `pfb_system_user_mobile_user` | **`pfb_system_user_individual_customer`** | 后台账号与自然人客户多对多（含软删历史） |
| `current_mobile_user_id` | **`current_individual_customer_id`** | `pfb_system_user_ext` 快照列 |

关联表为 **信息扩展**，**不修改** `system_users` 核心表结构，与框架设计边界一致。

---

## 0.2 决策记录：敏感个人信息存储（PII）

| 日期 | 决策 | 说明 |
|------|------|------|
| 2026-05-12 | **本期不在库表实施列级加密/透明加密** | 身份证号、姓名等仍以明文落库；依赖 OS/磁盘、网络与访问控制等基线安全。 |
| 待办 | 与行方安全/等保评审对齐后 | 可选：应用层信封加密、MySQL TDE、脱敏展示规范、审计日志字段、密钥管理（KMS）等，**单独立项**，再出迁移脚本。 |

---

## 0.3 待决策：申请提交幂等与防重复（§3.2-6）

以下方案尚未选定，**不在 v0 DDL 体现**：

- **客户端幂等键**：`Idempotency-Key` / `client_request_id` 唯一（时间窗内）。  
- **业务防重**：短时间同 `(sys_user_id, ent_id, product_id, apply_amount_cent, 日)` 拒绝或合并。  
- **纯应用层**：仅依赖前端防抖 + 后端事务，无 DB 约束。

待产品确认「是否允许同一企业同日多次同额申请」后再定索引或唯一约束。

---

## 1. 当前脚本已落地的「小步优化」

| 项 | 说明 |
|----|------|
| **产品列表索引** | `pfb_loan_product`：`idx_pfb_loan_product_list (deleted, product_status, sort_order, id)`。 |
| **Banner 列表** | `pfb_banner`：`idx_pfb_banner_list (deleted, banner_status, sort_order, id)`；列表需叠加 `effective_begin/end` 条件（应用或生成列）。 |
| **避免改 `system_users`** | `pfb_system_user_ext` + `pfb_system_user_individual_customer` / `pfb_system_user_ent_info`。 |
| **申请主体与 PRD** | `pfb_loan_application` 含 **`ent_id`** + 企业名称/统一社会信用代码 **快照**；索引 **`(sys_user_id, ent_id, create_time)`** 支撑「本企业申请」时间序。 |
| **企业主数据** | **`pfb_ent_info.company_credit_code` NOT NULL**：企业建档即须具备有效统一社会信用代码，避免申请快照与授权书出现空码。 |
| **CHAR(36) UUID** | 可读性优先；高量后可评估 `BINARY(16)`（见 §2.2）。 |

---

## 2. 类型与建模（建议评审后择机升级）

### 2.1 标志位与逻辑删除

- **已定稿**：统一 **`TINYINT(1)`**，与 §0 一致。

### 2.2 UUID 存储：`CHAR(36)` → `BINARY(16)`（可选）

- **收益**：主键/外键索引长度约 **36B → 16B**。  
- **代价**：排障可读性下降；应用需编解码。  
- **建议**：千万级前维持 `CHAR(36)`。

### 2.3 金额与利率

- **产品表**：`max_amount_limit` 已用 **`DECIMAL(18,2)`**（元）；`min_rate_limit` 为 **`DECIMAL(10,6)`**（比例），与常见展示一致。  
- **申请金额**：`apply_amount_cent` 保持 **BIGINT 分**。

### 2.4 `pfb_ent_info` 宽表

- **现状**：已按字段语义 **收紧 VARCHAR 长度**、经营范围等用 **2000** 级，避免全盘 255。**`company_credit_code` 为 NOT NULL**：建档即须合法码，与申请快照、授权书变量一致。  
- **演进**：仍偏大时再做垂直拆分；**本期不采用 JSON 列**（已确认）。

---

## 3. 索引与查询模式

### 3.1 关联表唯一约束与软删

- **现状**：`UNIQUE (sys_user_id, individual_customer_id, deleted)` 等，与「扩展表不改变框架核心设计」一致，允许软删换绑历史。  
- **可选**：若要求「同一 `sys_user_id` 仅一个有效自然人客户」，用应用层强校验或 MySQL 8 **部分唯一索引**（`deleted=0`）。

### 3.2 `pfb_loan_application`

- **已建**：`(sys_user_id, ent_id, create_time)`、`ent_id`、`product_id`。  
- **可选**：运营统计 `(product_id, status)` 视查询再建。

### 3.3 `pfb_banner`

- 列表：`deleted=0 AND banner_status=1 AND (effective_begin IS NULL OR effective_begin<=NOW()) AND (effective_end IS NULL OR effective_end>=NOW()) ORDER BY sort_order, id`。

### 3.4 外键

- 生产可不建 FK，由应用保证；测试库可加 FK 发现脏数据。

---

## 4. 字符集、排序规则与校对

- 已使用 **`utf8mb4` + `utf8mb4_unicode_ci`**。

---

## 5. 运维与容量

| 项 | 建议 |
|----|------|
| **备份与归档** | `pfb_loan_application` 后续可增加信贷流水号、对账状态等字段。 |
| **监控** | 关注 `idx_pfb_loan_product_list`、`idx_pfb_banner_list`、`idx_pfb_loan_app_user_ent_time` 的慢查询。 |

---

## 6. 与实现计划（代码侧）的衔接

- 登录态：优先读 **`pfb_system_user_ext`**（`current_individual_customer_id`、`default_ent_id`），必要时回退关联表并回填快照。  
- 创建申请：必须写入 **`ent_id`** 与 **企业名称/统一社会信用代码快照**（与 PRD 授权书变量一致）；`individual_customer_id` 建议一并写入。  
- 产品列表：`WHERE deleted=0 AND product_status=1 ORDER BY sort_order ASC, id ASC`。

---

*文档版本：与 `pfb-core-schema-v0.sql` 同次修订对齐（含自然人客户重命名、Banner、TINYINT、申请企业快照）。*
