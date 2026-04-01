# 仓库与目录布局（repo-layout.md）

## 当前状态

- 后端：**本仓库** `ahzx-wmt-svc`，Maven 多模块，`wmt-server` 为启动入口。
- 既有前端：常见为 **兄弟目录** `wmt-ui`（Vue2/Vue3 Admin、UniApp 等），不在本仓内。

## 推荐目标布局（两选一，需团队裁决）

### 方案 A：契约多仓（改动最小，贴近现状）

```
ahzx-wmt-svc/          # 后端 + OpenAPI 发布产物
wmt-ui-admin-vue3/     # 管理端 Web（现有）
wechat-miniprogram/    # 小程序（独立仓或子模块）
```

- **OpenAPI**：由后端生成/维护，前端用代码生成客户端或通过 Apifox 同步。
- **优点**：与现有 Vue 生态一致；小程序独立发布节奏。
- **缺点**：流水线 `check-gates.ps1 -Stage frontend` 需指向各仓或本仓仅 e2e。

### 方案 B：单仓 Monorepo（贴合 skill 默认）

```
ahzx-wmt-svc/
  wmt-server/
  wmt-module-fin-*/
  frontend/
    admin/           # Vite + React（新管理端）或壳工程聚合旧 UI
  apps/
    miniprogram/     # 可选：小程序源码（若团队接受同仓）
  docs/delivery-lending-platform/
  .delivery-pipeline/
```

- **优点**：一次 MR 可改 API + UI；门禁脚本易找 `package.json`。
- **缺点**：仓库体积与 CI 时间上升；与已有 Vue Admin 并存需规范。

## 本项目的默认推荐

在 **未迁移前端入仓** 前采用 **方案 A**；同时在 `docs/delivery-lending-platform/04-data-contracts/` 维护 **OpenAPI 真相源**，以便两栖发展。

若你希望 **严格遵循** `wmt-fullstack-pipeline-orchestrator-springboot` 的 frontend 门禁，则在 M1 末尾创建 `frontend/admin` 最小壳（健康检查页 + 登录回调 Mock），使 `check-gates -Stage frontend` 可运行。

## Maven 调整备忘

- 根 `pom.xml` `<modules>`：移除或注释 `wmt-module-credit-report`、`wmt-module-report`；新增 `wmt-module-fin-*`。
- `wmt-server/pom.xml`：同步替换依赖；保证无残留 Bean 扫描冲突。

## 敏感数据工程实践

- **密钥**：不入库不入 Git；Spring 配置走环境变量或密钥管理系统。
- **日志**：禁止打印完整身份证、手机号、银行卡；使用掩码工具类。
