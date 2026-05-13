# 普惠金融小程序进件端 — 一期后端任务（与 v0 DDL 对齐）

> **真源**：`docs/nm-deliver/sql/pfb-core-schema-v0.sql`（与 `sql/mysql/pfb-core-schema-v0.sql` 同步）。  
> **一期原则**：DDL 中出现的 `pfb_*` 表及与进件闭环直接相关的 **DO/Mapper/Service/Controller/校验/安全** 均在一期完成；高级检索、申请进度列表、幂等、**Banner/产品运营 CRUD**、腾讯人脸深度对接等见 [二期任务](./2026-05-11-pfb-phase2-backend-tasks.md)。  
> **框架基线**：`sql/mysql/ruoyi-vue-pro.sql`（无文件内 `CREATE DATABASE`/`USE`，须先建库再在目标库执行）。  
> **PFB-API（C2）**：路径前缀 **`/pfb-api`**、Controller 包 **`**.controller.pfb.**`**、`userType` = **PFB**。集成说明见 [pfb-api-c2-framework-and-app-integration.md](../../../learning-roadmap/pfb-api-c2-framework-and-app-integration.md)。  
> **PRD**：`docs/nm-deliver/PRD-普惠金融数字化系统-小程序-需求加固-V0.1.md`（下文 **US-xx** 均指该文档用户故事编号）。

---

## WMT 框架组件库（源码真源，与业务仓分离）

业务系统依赖的 **BOM / Starter** 由独立仓库 **`wmt-framework-jdk17`** 提供；编写 Controller、Service、DO、Mapper 时，应以组件库 **源码** 为准（字段含义、分页语义、租户拦截等），勿仅凭记忆臆造 API。

**仓库根目录（本机示例，与 `ahzx-wmt-svc` 并列时）**：`/Users/whiskey/Projects/Github/Whiskey1028/wmt-framework/wmt-framework-jdk17`

在 Cursor / VS Code 中可直接打开下列 `file://` 链接（若框架克隆在其他路径，请替换 URL 中的根路径段）。

| 能力 | 模块 | 源码（`file://`） |
|------|------|-------------------|
| `CommonResult` | `wmt-common` | [CommonResult.java](file:///Users/whiskey/Projects/Github/Whiskey1028/wmt-framework/wmt-framework-jdk17/wmt-common/src/main/java/com/wmt/framework/common/pojo/CommonResult.java) |
| `PageParam` | `wmt-common` | [PageParam.java](file:///Users/whiskey/Projects/Github/Whiskey1028/wmt-framework/wmt-framework-jdk17/wmt-common/src/main/java/com/wmt/framework/common/pojo/PageParam.java) |
| `SortablePageParam` / `SortingField` | `wmt-common` | [SortablePageParam.java](file:///Users/whiskey/Projects/Github/Whiskey1028/wmt-framework/wmt-framework-jdk17/wmt-common/src/main/java/com/wmt/framework/common/pojo/SortablePageParam.java) · [SortingField.java](file:///Users/whiskey/Projects/Github/Whiskey1028/wmt-framework/wmt-framework-jdk17/wmt-common/src/main/java/com/wmt/framework/common/pojo/SortingField.java) |
| `PageResult` | `wmt-common` | [PageResult.java](file:///Users/whiskey/Projects/Github/Whiskey1028/wmt-framework/wmt-framework-jdk17/wmt-common/src/main/java/com/wmt/framework/common/pojo/PageResult.java) |
| `PageUtils` | `wmt-common` | [PageUtils.java](file:///Users/whiskey/Projects/Github/Whiskey1028/wmt-framework/wmt-framework-jdk17/wmt-common/src/main/java/com/wmt/framework/common/util/object/PageUtils.java) |
| `ServiceException` / `ServiceExceptionUtil` | `wmt-common` | [ServiceException.java](file:///Users/whiskey/Projects/Github/Whiskey1028/wmt-framework/wmt-framework-jdk17/wmt-common/src/main/java/com/wmt/framework/common/exception/ServiceException.java) · [ServiceExceptionUtil.java](file:///Users/whiskey/Projects/Github/Whiskey1028/wmt-framework/wmt-framework-jdk17/wmt-common/src/main/java/com/wmt/framework/common/exception/util/ServiceExceptionUtil.java) |
| `GlobalErrorCodeConstants` | `wmt-common` | [GlobalErrorCodeConstants.java](file:///Users/whiskey/Projects/Github/Whiskey1028/wmt-framework/wmt-framework-jdk17/wmt-common/src/main/java/com/wmt/framework/common/exception/enums/GlobalErrorCodeConstants.java) |
| `BaseDO` | `wmt-spring-boot-starter-mybatis` | [BaseDO.java](file:///Users/whiskey/Projects/Github/Whiskey1028/wmt-framework/wmt-framework-jdk17/wmt-spring-boot-starter-mybatis/src/main/java/com/wmt/framework/mybatis/core/dataobject/BaseDO.java) |
| `BaseMapperX` | `wmt-spring-boot-starter-mybatis` | [BaseMapperX.java](file:///Users/whiskey/Projects/Github/Whiskey1028/wmt-framework/wmt-framework-jdk17/wmt-spring-boot-starter-mybatis/src/main/java/com/wmt/framework/mybatis/core/mapper/BaseMapperX.java) |
| `MyBatisUtils` | `wmt-spring-boot-starter-mybatis` | [MyBatisUtils.java](file:///Users/whiskey/Projects/Github/Whiskey1028/wmt-framework/wmt-framework-jdk17/wmt-spring-boot-starter-mybatis/src/main/java/com/wmt/framework/mybatis/core/util/MyBatisUtils.java) |
| `GlobalExceptionHandler` | `wmt-spring-boot-starter-web` | [GlobalExceptionHandler.java](file:///Users/whiskey/Projects/Github/Whiskey1028/wmt-framework/wmt-framework-jdk17/wmt-spring-boot-starter-web/src/main/java/com/wmt/framework/web/core/handler/GlobalExceptionHandler.java) |
| `GlobalResponseBodyHandler` | `wmt-spring-boot-starter-web` | [GlobalResponseBodyHandler.java](file:///Users/whiskey/Projects/Github/Whiskey1028/wmt-framework/wmt-framework-jdk17/wmt-spring-boot-starter-web/src/main/java/com/wmt/framework/web/core/handler/GlobalResponseBodyHandler.java) |
| `WebFrameworkUtils` | `wmt-spring-boot-starter-web` | [WebFrameworkUtils.java](file:///Users/whiskey/Projects/Github/Whiskey1028/wmt-framework/wmt-framework-jdk17/wmt-spring-boot-starter-web/src/main/java/com/wmt/framework/web/core/util/WebFrameworkUtils.java) |
| `@TenantIgnore` | `wmt-spring-boot-starter-biz-tenant` | [TenantIgnore.java](file:///Users/whiskey/Projects/Github/Whiskey1028/wmt-framework/wmt-framework-jdk17/wmt-spring-boot-starter-biz-tenant/src/main/java/com/wmt/framework/tenant/core/aop/TenantIgnore.java) |

**参考（会员端认证拆分风格）**：[AppAuthController.java](../../../../wmt-module-member/src/main/java/com/wmt/module/member/controller/app/auth/AppAuthController.java)

**IDE 快捷方式**：在 `ahzx-wmt-svc` 中 `import` 上述类后，使用「转到定义」若进入 `.class`/反编译视图，可将 **`wmt-framework-jdk17` 以多根工作区或附加源码路径** 打开，与组件库对照阅读。

---

## 全局数据与代码约定（一期所有 Task 遵守）

| 项 | 约定 |
|----|------|
| HTTP 前缀 | 对外完整路径为 **`/pfb-api` + 业务段**；禁止再以 **`/pfb/app/**`** 作为唯一约定（与 C2 框架推导冲突） |
| Controller 包 | 一期交付的进件接口一律在 **`com.wmt.module.pfb.controller.pfb`**（或子包，如 `...pfb.auth`、`...pfb.individual`），满足 `**.controller.pfb.**` |
| 主键 | 业务主表/关联表中带 `id CHAR(36)` 的，DO 使用 **`@TableId(type = IdType.INPUT)`** + 应用层 UUID；**`pfb_system_user_ext`** 主键为 **`sys_user_id`**（`BIGINT`），使用 **`@TableId("sys_user_id")`**（或等价映射）+ **`extends BaseDO`**，无单独 `id` 列 |
| 逻辑删除 | `deleted` 为 **`TINYINT(1)`**，与 `BaseDO` / `@TableLogic` 一致；勿使用 `BIT(1)` |
| 多租户 | v0 DDL **无 `tenant_id`**；相关 DO 类上 **`@TenantIgnore`**（若工程启用租户插件）。**禁止**在文档或 Wrapper 中假设 `pfb_*` 表存在 `tenant_id` |
| 企业统一社会信用代码 | 表 `company_credit_code` **NOT NULL**；创建/绑定 DTO **`@NotBlank`** + 格式校验（如 `RegexUtils.isCreditCode`） |
| 申请企业名称快照 | `enterprise_name_snapshot` **NOT NULL**；提交前从 `pfb_ent_info.enterprise_name` 取值，若库内为空则 **业务异常**，不插入 |
| 出参 | Controller 一律 **`CommonResult<T>`**；分页 **`PageParam` / `PageResult`** |

---

## Task 1 — Maven 模块骨架

- 新建 **`wmt-module-pfb`**（`packaging`=`jar`），`pom.xml` 依赖 `wmt-spring-boot-starter-web`、`mybatis`、`security`、`redis`、`tenant`（按需）等与 **`wmt-module-credit`** 同级模块对齐。
- **`wmt-server/pom.xml`** 增加对 **`wmt-module-pfb`** 的依赖；根 **`pom.xml`** `<modules>` 注册。
- 包根：`com.wmt.module.pfb`；可预留 `controller.admin`（管理端若未来挂 `admin-api`）；**一期进件接口仅实现 `controller.pfb`**。
- **验收**：`mvn -pl wmt-module-pfb,wmt-server -am package -DskipTests` 通过。

---

## Task 2 — 数据库初始化（Docker MySQL / 本机）

**开发库名**：**`wmt_pfb`**（普惠金融专用库名；与 [`application-pfb-local.yaml`](../../../../wmt-server/src/main/resources/application-pfb-local.yaml) 中 `master`/`slave` URL 一致）。

**环境假设**：MySQL 监听本机 **3306**。`application-pfb-local.yaml` 与 **`application-nmlocal.yaml` 对齐**（含 JDBC 用户名 `credit_user@credit_tenant` 等）；若本地仅有 `root`，请在 **`wmt_pfb`** 内创建等价租户用户，或在本文件中临时改写 `username`/`password`（会破坏「与 nmlocal 除库/Redis 外一致」的约定，仅作本机权宜）。容器名以本机 `docker ps` 为准（下文示例 **`mc-mysql`**）。

### 2.1 建库

```sql
CREATE DATABASE IF NOT EXISTS `wmt_pfb` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

### 2.2 执行顺序（均在库 **`wmt_pfb`** 下）

1. **`sql/mysql/ruoyi-vue-pro.sql`** — RuoYi 基线表（含 `system_users` 等）。  
2. **`docs/nm-deliver/sql/pfb-core-schema-v0.sql`** — 普惠金融 v0 增量（`pfb_*`）。

**说明**：若先执行 `pfb-core-schema-v0.sql` 再执行 `ruoyi-vue-pro.sql`，后者可能 **DROP/覆盖** 基线对象，导致顺序错误。**必须**先 RuoYi 基线，再 PFB 增量。

### 2.3 命令示例

**主机已安装 `mysql` 客户端**：

```bash
mysql -h127.0.0.1 -P3306 -uroot -pwmt123456 -e "CREATE DATABASE IF NOT EXISTS \`wmt_pfb\` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;"
mysql -h127.0.0.1 -P3306 -uroot -pwmt123456 wmt_pfb < sql/mysql/ruoyi-vue-pro.sql
mysql -h127.0.0.1 -P3306 -uroot -pwmt123456 wmt_pfb < docs/nm-deliver/sql/pfb-core-schema-v0.sql
```

**主机无 `mysql` CLI**（仅 Docker）：

```bash
docker exec -i mc-mysql mysql -uroot -pwmt123456 -e "CREATE DATABASE IF NOT EXISTS \`wmt_pfb\` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;"
docker exec -i mc-mysql mysql -uroot -pwmt123456 wmt_pfb < sql/mysql/ruoyi-vue-pro.sql
docker exec -i mc-mysql mysql -uroot -pwmt123456 wmt_pfb < docs/nm-deliver/sql/pfb-core-schema-v0.sql
```

### 2.4 验收 SQL

```sql
USE `wmt_pfb`;
SHOW TABLES LIKE 'pfb_%';
-- 期望包含：pfb_banner, pfb_ent_info, pfb_individual_customer, pfb_loan_application,
--   pfb_loan_product, pfb_system_user_ent_info, pfb_system_user_individual_customer,
--   pfb_system_user_ext
SELECT COUNT(*) FROM system_users;
DESC pfb_loan_application;
-- 确认 id 为 char(36)、deleted 为 tinyint(1)、enterprise_name_snapshot / company_credit_code_snapshot 等非空语义与 DDL 一致
```

---

## Task 3 — PFB 小程序认证（`/pfb-api/pfb/auth`，PRD US-02 / US-03 / US-04）

- **Controller**：`com.wmt.module.pfb.controller.pfb.auth`，类上 **`@RequestMapping("/pfb/auth")`**；完整 URL 前缀 **`/pfb-api`**。  
- **实现导向**：复用 `wmt-module-system` OAuth2 发 Token；**`userType` 固定为 `PFB`**；可新建 **`PfbAuthService`** 委托 `AdminUser` / 短信 / `OAuth2Token`（与 [AppAuthController](../../../../wmt-module-member/src/main/java/com/wmt/module/member/controller/app/auth/AppAuthController.java) 分组风格一致）。

### 3.1 接口契约（一期文档真源）

| 能力 | 方法 | 路径（业务段；完整前缀均为 `/pfb-api`） | PRD | 鉴权 |
|------|------|----------------------------------------|------|------|
| 发送短信验证码 | POST | `/pfb/auth/send-sms-code` | US-02 / US-03 / US-04 | `@PermitAll`；body：`mobile`、`scene`（`LOGIN` / `REGISTER` / `RESET_PASSWORD`） |
| 校验验证码（可选） | POST | `/pfb/auth/validate-sms-code` | 注册/重置流程拆分 | `@PermitAll`（或按安全设计收紧） |
| 手机号 + 密码 + 验证码登录 | POST | `/pfb/auth/login` | US-02 | `@PermitAll` |
| 手机号 + 验证码登录（可选） | POST | `/pfb/auth/sms-login` | SRS 若需双模式 | `@PermitAll` |
| 注册 | POST | `/pfb/auth/register` | US-03 | `@PermitAll` |
| 忘记密码 / 重置密码 | POST | `/pfb/auth/reset-password` | US-04 | `@PermitAll` |
| 刷新令牌 | POST | `/pfb/auth/refresh-token` | — | `@PermitAll` |
| 登出 | POST | `/pfb/auth/logout` | 小程序登出 | `@PermitAll`（携带 token 时吊销，与 Admin/Member 一致） |
| 注销账号 | POST | `/pfb/auth/account-cancel` | 小程序注销 | **需登录**；**策略须产品/安全定案**（物理删/软删 `system_users`、审计、行方合规）；一期允许 **接口占位 + 明确 TODO** |

- **`permit-all_urls`**：上述 `@PermitAll` 路径须出现在白名单（见 Task 9；**合并** nmlocal 已有项，禁止整段覆盖丢失 `/admin-api/mp/open/**` 等）。

---

## Task 4 — 贷款产品（只读，PRD US-01 / US-07 / US-08）

- **表**：`pfb_loan_product`（字段以 DDL 为准：`product_status`、`is_hot`、`sort_order`、`max_amount_limit`、`min_rate_limit` 等）。
- **DO**：主键 `String` + `@TableId(type = IdType.INPUT)`；`@TableName("pfb_loan_product")`；**`extends BaseDO`**；按需 **`@TenantIgnore`**。

### 4.1 接口契约

| 能力 | 方法 | 路径 | PRD | 说明 |
|------|------|------|------|------|
| 产品列表（全量上架） | GET | `/pfb-api/pfb/loan-product/list` | US-01 公开浏览、US-08 列表 | `deleted=0` 且 `product_status=1`；`ORDER BY sort_order ASC, id ASC` |
| 热门产品（首页） | GET | `/pfb-api/pfb/loan-product/hot-list` | US-07 | `deleted=0`、`product_status=1` 且 **`is_hot=1`**；同上排序；条数上限与 PRD/SRS 对齐（如 Top N） |
| 产品详情 | GET | `/pfb-api/pfb/loan-product/get` | US-08 | Query：`id`（UUID）；未上架或非删除过滤策略与 PRD「立即申请」门禁一致 |

- **匿名**：US-01 要求未登录可浏览公开内容；**`list` / `hot-list` / `get`** 若对访客开放，须加入 **`permit-all_urls`**（与 Task 9 合并规则一致）。

---

## Task 5 — Banner（只读，PRD US-01 / US-07）

- **表**：`pfb_banner`（`sort_order`、`banner_status`、`effective_begin` / `effective_end`、`deleted` 等，以 DDL 为准）。
- **接口**：`GET /pfb-api/pfb/banner/list` — `deleted=0` 且 `banner_status=1`；`sort_order` 升序；生效时间窗与 PRD US-07 一致。
- **匿名**：首页未登录展示时，将该 **GET** 加入 **`permit-all_urls`**（Task 9）。
- **验收**：与 PRD 首页 Banner 一致；**运营 CRUD** 见二期。

---

## Task 6 — 自然人客户与实名（PRD US-05）

**映射**：US-05（L1/L2 实名、个人信息）对应 **自然人主数据** + **`pfb_system_user_individual_customer`** + **`pfb_system_user_ext.current_individual_customer_id`** 快照；L2/OCR/公安/活体与腾讯对接可在 **二期** 做实装，一期接口可先占位或返回「未开通」。

### 6.1 数据与 DO

- **`pfb_individual_customer`**：`real_name`、`id_card_no` 等 NOT NULL 与 DDL 对齐；敏感字段日志脱敏。
- **`pfb_system_user_individual_customer`**：关联主键 `id` UUID；**`extends BaseDO`**；**`@TenantIgnore`**。
- **`pfb_system_user_ext`**：**`@TableId("sys_user_id")`** + **`extends BaseDO`**；**`@TenantIgnore`**。

### 6.2 接口契约

| 能力 | 方法 | 路径 | 说明 |
|------|------|------|------|
| 实名/绑定状态摘要 | GET | `/pfb-api/pfb/individual/status` | 返回 L1/L2 进度、当前 `individual_customer_id` 等（字段与 SRS 对齐后可迭代） |
| 提交/更新自然人并绑定 | POST | `/pfb-api/pfb/individual/submit` | 写入/更新 `pfb_individual_customer` + 关联表；更新 **`pfb_system_user_ext.current_individual_customer_id`**；**同一 `id_card_no` 禁止绑定多个不同 `sys_user_id`** |
| L2/OCR/比对回调（可选） | POST | `/pfb-api/pfb/individual/l2/callback` | 一期可为占位；二期接公安/腾讯 |

- **鉴权**：均需 **已登录**（`userType=PFB`），**不设** `@PermitAll`。

---

## Task 7 — 企业信息与绑定（PRD US-06）

**映射**：US-06（我的企业、添加企业、切换默认、法人短信/人脸）对应 **`pfb_ent_info`** + **`pfb_system_user_ent_info`** + **`pfb_system_user_ext.default_ent_id`**；**`company_credit_code` 必填**（DDL NOT NULL）。法人 **`legal-verify`** 一期可为 **壳接口**，二期接腾讯。

### 7.1 接口契约

| 能力 | 方法 | 路径 | 说明 |
|------|------|------|------|
| 我的企业列表 | GET | `/pfb-api/pfb/enterprise/list` | 含 `is_default`、企业摘要 |
| 添加企业并绑定 | POST | `/pfb-api/pfb/enterprise` | 写 `pfb_ent_info` + `pfb_system_user_ent_info`；信用代码格式校验 |
| 编辑企业 | PUT | `/pfb-api/pfb/enterprise/{id}` | `id` 为 `pfb_ent_info.id`（UUID） |
| 设为默认企业 | POST | `/pfb-api/pfb/enterprise/{id}/set-default` | 更新 `is_default` + **`pfb_system_user_ext.default_ent_id`** |
| 法人核验（短信/人脸） | POST | `/pfb-api/pfb/enterprise/{id}/legal-verify` | 一期占位或返回未实现；二期对接腾讯 |

- **Mapper**：`extends BaseMapperX<...>`；Wrapper 构建条件。

---

## Task 8 — 融资申请提交（PRD US-09）

- **表**：`pfb_loan_application`（`enterprise_name_snapshot`、`company_credit_code_snapshot`、`ent_id`、`product_id`、`apply_amount_cent`、`term_months`、`repay_method`、`credit_auth_accepted`、`status` 等，以 DDL 为准）。
- **门禁**：与 PRD 一致 — **已登录 → 完成 US-05 侧实名要求 → 已绑企业（US-06）**；否则业务异常或约定错误码。
- **校验**：产品 **`product_status=1`**；从 **`pfb_ent_info`** 取 `enterprise_name` 填 **`enterprise_name_snapshot`**；`enterprise_name` 为空则 **不允许提交**。
- **接口**：`POST /pfb-api/pfb/loan-application/submit`。
- **鉴权**：需登录；**不设** `@PermitAll`。

**US-12（申请进度）**：主列表/详情仍在 [二期](./2026-05-11-pfb-phase2-backend-tasks.md)；一期可选 stub：`GET /pfb-api/pfb/loan-application/list`（只读）— **若做则写清与二期的边界**，避免范围膨胀。

---

## Task 9 — 本地配置、安全与联调

### 9.1 `application-pfb-local.yaml`（不改 nmlocal）

- **基线**：[application-nmlocal.yaml](../../../../wmt-server/src/main/resources/application-nmlocal.yaml) **禁止修改既有内容**。  
- **PFB 本地**：[application-pfb-local.yaml](../../../../wmt-server/src/main/resources/application-pfb-local.yaml) 为 **nmlocal 的全文拷贝**，仅将 **`master`/`slave` JDBC** 改为本机 **`127.0.0.1:3306/wmt_pfb`**，**`spring.data.redis.host`** 改为 **`127.0.0.1`**（其余键与 nmlocal 一致，避免单独切换 profile 后缺配置起不来）。  
- **启动**：**`spring.profiles.active=nmlocal,pfb-local`**（推荐；后加载 profile 覆盖同名键）。**注意**：Redis **端口仍为 `48250`**（与 nmlocal 相同）；本机 Redis 若监听 `6379`，需在本文件中改 `port` 或在本机映射端口，否则连不上。

### 9.2 `permit-all_urls` 合并规则

- [application-nmlocal.yaml](../../../../wmt-server/src/main/resources/application-nmlocal.yaml) 已含示例：`/admin-api/mp/open/**`、`/pfb-api/pfb/auth/**`。  
- 若将 **Banner / 产品公开读** 设为匿名，须在 **最终生效的 profile** 中 **整段列出** 所需 URL（含上述保留项 + 例如 `/pfb-api/pfb/banner/list`、`/pfb-api/pfb/loan-product/list`、`/pfb-api/pfb/loan-product/hot-list`、`/pfb-api/pfb/loan-product/get` — 以实际 `@PermitAll` 为准）。**若在 `pfb-local` 中声明 `wmt.security.permit-all_urls`，须把 nmlocal 中需保留的条目一并抄入后再追加**，避免覆盖丢失（见 [集成文档 §5.2](../../../learning-roadmap/pfb-api-c2-framework-and-app-integration.md)）。

### 9.3 验收清单（对齐集成文档 §6）

1. **`userType=PFB`** 的 Token 访问 **`/pfb-api/pfb/...`** 受保护接口 **200**。  
2. 同一 Token 访问 **`/admin-api/...`**，**类型不匹配应失败**。  
3. 无 Token 访问 **绑定/提交/注销（若需登录）** 返回 **401**。  
4. 回归 **`/admin-api`**、既有 **`/app-api`** 行为不变。

---

## 一期完成定义（Definition of Done）

- [ ] Task 1–9 全部满足；`mvn package` 通过。  
- [ ] 数据库已按 Task 2 在 **`wmt_pfb`** 初始化且 `pfb_*` 表存在。  
- [ ] 本地可使用 **`nmlocal,pfb-local`** 启动并指向 **`wmt_pfb`**。  
- [ ] 二期范围未混入一期必交付（见二期文档）。

---

## 参考与索引

| 文档 | 路径 |
|------|------|
| 计划总索引 | [2026-05-11-pfb-miniprogram-backend-mvp.md](./2026-05-11-pfb-miniprogram-backend-mvp.md) |
| PFB-API C2 集成说明 | [pfb-api-c2-framework-and-app-integration.md](../../../learning-roadmap/pfb-api-c2-framework-and-app-integration.md) |
| PRD | `docs/nm-deliver/PRD-普惠金融数字化系统-小程序-需求加固-V0.1.md` |
| DDL 真源 | `docs/nm-deliver/sql/pfb-core-schema-v0.sql` |
| 列级说明 | `docs/nm-deliver/sql/PFB-SCHEMA-OPTIMIZATION.md` |
| `application-pfb-local` | `wmt-server/src/main/resources/application-pfb-local.yaml` |
| WMT 框架源码根 | `/Users/whiskey/Projects/Github/Whiskey1028/wmt-framework/wmt-framework-jdk17` |

---

*Self-Review：库名统一为 `wmt_pfb`；认证/产品/Banner/自然人/企业/申请接口与 PRD US 映射已表列；`application-pfb-local.yaml` 与 nmlocal 全文对齐（仅 JDBC/Redis host）且与 Task 9 已挂钩；`permit-all_urls` 合并风险已写明。*
