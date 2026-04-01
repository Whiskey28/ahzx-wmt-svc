# 约束与假设（Constraints）

## 技术约束

- **后端**：继续使用本仓库 **Spring Boot 3.x + JDK 17 + Maven 多模块**；Controller 返回 `CommonResult`、分页 `PageParam` / `PageResult`、MyBatis-Plus / `BaseMapperX`（见项目 Cursor 规则）。
- **工程复用**：计划从 `wmt-server` / 根 `pom.xml` **移除** `wmt-module-credit-report`、`wmt-module-report`（及相关的 `wmt-module-credit` 若存在依赖链）等历史模块依赖，由 **新业务模块** 替代或逐步迁移。
- **前端**：流水线 skill 默认 **React（Vite/CRA）**；若公司现状为 **Vue3 Admin（如 `wmt-ui-admin-vue3`）+ 微信小程序**，需在 `03-architecture/repo-layout.md` 中显式定格：**仓库内**是否新增 `frontend/` 单仓，或 **多仓 + OpenAPI 契约** 驱动。本阶段允许保留两种选项，architecture 文档给出推荐默认。

## 安全与合规

- **敏感数据**：客户身份信息、征信授权轨迹、贷后风险信号、催收记录等均属高敏；需全链路 **TLS**、字段级 **脱敏/加密（视监管要求）**、**审计日志**（谁、何时、对何对象、何操作）。
- **权限**：公司级多用户；至少 **功能权限 + 数据权限**；必要时 **租户/机构隔离**（若一套系统服务多法人/多机构，需提前确认）。

## 组织与交付

- 责任组织：**科技交付中心**（与表格一致）；实施时需产品/风控/合规/运营等角色参与规则与验收。
- **协作**：需求以 **里程碑** 切片；接口以 **OpenAPI** 为单一事实来源（阶段 4：data_contracts）。

## 运行时与运维（初版假设）

- 部署形态待定：**K8s / VM + Jar**；需配套日志、指标、链路追踪（在阶段 8 deploy / 9 ops 展开）。
- 数据库：沿用现有栈（多为 **MySQL**）；大表与索引策略在 `04-data-contracts` 与 `supabase-postgres-best-practices` 不适用时，采用 **MySQL 命名与索引规范** 自检。

## 依赖外部系统（待确认清单）

- 微信开放平台（小程序登录、消息、合规组件）。
- 可能的征信/风控/支付/核心账务（一期可 Mock 或适配层隔离）。

