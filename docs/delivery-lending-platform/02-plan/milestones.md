# 里程碑规划（Planning）

> 与 `wmt-fullstack-pipeline-orchestrator-springboot` 阶段对齐：**4 data_contracts → 5 backend → 6 frontend → 7 qa → 8 deploy**。

## M0：立项与基线（当前文档包）

- 完成 intake / discovery / 本 backlog & 架构草案。
- 确认：数据库选型、小程序主体、是否多法人租户。

## M1：工程可运行 + 安全骨架（目标 2～3 周量级，视人力调整）

- POM 调整：`wmt-server` 去掉 `wmt-module-credit-report`、`wmt-module-report`；新增业务模块依赖。
- 定义 `**/admin/`** 与 `/app/**`（或等价）** 鉴权与 Swagger 分组。
- 复用 `wmt-module-system`：管理端登录、RBAC 最小闭环。
- 小程序登录技术 spike（微信 code → 绑定用户）。

**门禁**：`mvn -B test` 全绿（在接入 `.delivery-pipeline` 后映射为 `check-gates.ps1 -Stage backend`）。

## M2：数据契约与核心域模型（OpenAPI + DDL）

- 输出 `04-data-contracts/openapi.yaml`、`schema.sql`（可在仓库 `docs/delivery-lending-platform/04-data-contracts/` 落地后再升迁到标准路径）。
- 核心实体：**客户、申请/案件、授权与材料、借据、预警、工单**。
- MySQL 索引与审计字段规范评审。

## M3：C 端 MVP

- CAP-C-001～006 主路径 API + 集成测试。
- 文件上传与授权记录可读。

## M4：管理端 MVP + 贷后闭环 v1

- 进件列表/详情、内部状态、贷后监测+预警+催收工单最小闭环。
- 驾驶舱占位指标。

## M5：硬化与上线准备

- 脱敏、审计抽样、性能基线、部署 Runbook。
- E2E：登录 → 申请 → 内部查询 → 贷后工单（可用测试桩）。

## 并行前端策略

- **若 Vue Admin 在外部仓库**：M3 起并行，以 OpenAPI 为契约；本仓库可先只保证后端与 Mock。
- **若接受 skill 默认**：在单仓增加 `frontend/`（Vite React）专用于新管理端，与旧 admin 共存策略写入 `repo-layout.md`。

