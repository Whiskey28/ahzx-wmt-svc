# 普惠金融小程序进件端 — 后端交付计划（索引）

> **拆分说明**：原「单文件 MVP 任务清单」已拆为 **一期（与 v0 DDL 对齐的必交付）** 与 **二期（扩展能力）**，避免一期范围膨胀。  
> **DDL 真源**：`docs/nm-deliver/sql/pfb-core-schema-v0.sql`  
> **产品说明**：`docs/nm-deliver/PRD-普惠金融数字化系统-小程序-需求加固-V0.1.md`  
> **列级与决策记录**：`docs/nm-deliver/sql/PFB-SCHEMA-OPTIMIZATION.md`

---

## PFB-API（C2）与模块边界（执行前必读）

内蒙小程序进件能力统一落在 **`wmt-module-pfb`**，对外 HTTP 前缀为 **`/pfb-api`**，Controller 必须落在框架约定的 **`**.controller.pfb.**`** 包下（例如 `com.wmt.module.pfb.controller.pfb`），以便 `WebProperties` / `TokenAuthenticationFilter` 将请求推导为 **`UserTypeEnum.PFB(3)`**，且 Token 内 `userType` 与路径前缀一致。

| 项 | 约定 |
|----|------|
| 集成真源 | [docs/learning-roadmap/pfb-api-c2-framework-and-app-integration.md](../../../learning-roadmap/pfb-api-c2-framework-and-app-integration.md) |
| 完整 URL | `/pfb-api` + 业务段；类上 `@RequestMapping("/pfb/...")` 时，实际为 `/pfb-api/pfb/...` |
| 登录主体 | `userId` = `system_users.id`，`userType` = **PFB**；业务仓 `OAuth2TokenServiceImpl.buildUserInfo` 已扩展 PFB 分支 |
| 本地配置 | **基线** [`application-nmlocal.yaml`](../../../../wmt-server/src/main/resources/application-nmlocal.yaml) **不改**；[`application-pfb-local.yaml`](../../../../wmt-server/src/main/resources/application-pfb-local.yaml) 与 nmlocal **全文对齐**，仅 **JDBC 本机 + `wmt_pfb`**、**Redis host 本机**；启动 **`spring.profiles.active=nmlocal,pfb-local`** |
| 匿名白名单 | `wmt.security.permit-all_urls` 在 profile 中为 **覆盖语义**；若在 `pfb-local` 中声明该列表须 **整段合并** nmlocal 已有项后再追加（详见一期 Task 9 与集成文档 §5.2） |
| 安全扩展 | 各模块 `SecurityConfiguration` 可使用 **`buildPfbApi("/pfb/...")`** 与 `buildAdminApi` / `buildAppApi` 并列 |

**一期范围（本索引与一期任务书一致）**：数据库初始化（库名 **`wmt_pfb`**）；**PFB 小程序认证**（PRD US-02～US-04 + 登出/刷新/注销/验证码等，见一期 Task 3）；**贷款产品只读 + 详情 + 热门**（US-01/US-07/US-08）；**首页 Banner 只读**（US-07，`pfb_banner`）；**自然人 / 企业**（PRD **US-05 / US-06** 对应绑定与扩展表）；**融资申请提交**（US-09）；**安全与联调 + `application-pfb-local.yaml`**（一期 Task 9）。

---

## 该看哪份任务文档？

| 文档 | 用途 |
|------|------|
| [**一期后端任务**](./2026-05-11-pfb-phase1-backend-tasks.md) | Maven 模块 **`wmt-module-pfb`**、**数据库（`wmt_pfb`）**、**认证 / 产品 / Banner / 自然人 / 企业 / 申请** 接口细化、**`application-pfb-local.yaml`**、安全联调。与 **当前 `pfb_*` 表结构** 一一对应。 |
| [**二期后端任务**](./2026-05-11-pfb-phase2-backend-tasks.md) | US-08 高级检索、US-12 进度、幂等、**Banner/产品运营 CRUD**、腾讯人脸等深度集成、信贷对接等。 |

---

## 数据库初始化速览（详见一期 Task 2）

- **库名**：**`wmt_pfb`**（普惠金融开发库；与 [`application-pfb-local.yaml`](../../../../wmt-server/src/main/resources/application-pfb-local.yaml) 中 JDBC 一致）。  
- **顺序**：先 `sql/mysql/ruoyi-vue-pro.sql`，再 `docs/nm-deliver/sql/pfb-core-schema-v0.sql`（均在 **`wmt_pfb`** 库执行）。  
- **连接**：本地 Docker 示例 **`127.0.0.1:3306`**，`root` / `wmt123456`；无本机 `mysql` 客户端时用 `docker exec -i <容器名> mysql ...`。  
- **应用启动**：`--spring.profiles.active=nmlocal,pfb-local`，由 **`pfb-local`** 覆盖 **`master`/`slave` JDBC** 与 **Redis host** 指向本地 **`wmt_pfb`** / 本机 Redis（见一期 Task 9）。

---

*历史长文任务清单已收敛至一期/二期两份；请以一期、二期文件为执行真源。*
