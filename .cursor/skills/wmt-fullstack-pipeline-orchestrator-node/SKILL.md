---
name: wmt-fullstack-pipeline-orchestrator-node
description: WMT-owned fullstack pipeline orchestrator with Node (Express/Nest/npm workspaces) + React defaults and Node-specific quality gates. Use when the backend and tooling are Node-based and the user wants staged delivery with npm scripts and optional monorepo client/server split.
---

# WMT Fullstack Pipeline — Node Profile

## When To Use

- Backend: **Node** (Express, Nest, Fastify, etc.) with `package.json`
- Frontend: **React** (separate package or workspace)
- Repo: **单仓**；推荐：`backend/` + `frontend/`，或单包内 `lint:client` / `build:server` 这类脚本

## Inherits

阶段定义、产物路径、门禁以 **`templates/wmt-fullstack-skill-pipeline/pipeline.yaml`** 为准。  
本 profile **在每个节点下方列出须按序调用的外部 skills**；执行时先读入参、再产出、最后跑 `profiles/node/check-gates.ps1`。

## 每阶段须调用的 Skills（按序）

下列为 **Node 后端 + React 前端**（Express/Nest/工作区）默认编排；若某 skill 未安装，选最近似能力并说明替换。

| 阶段 | 主要输入 | 主要输出 | 按序调用的 Skills |
| ---- | -------- | -------- | ----------------- |
| **1 discovery** | `00-intake/problem.md`, `constraints.md` | `01-discovery/*.md`（3 个） | ① `superpowers/brainstorming` → ② `superpowers/writing-plans` |
| **2 planning** | discovery 三文件 | `02-plan/backlog.json`, `milestones.md`, `risks.md` | ① `superpowers/writing-plans` → ② `superpowers/executing-plans` |
| **3 architecture** | `backlog.json`, `milestones.md` | `03-architecture/*.md`（3 个） | ① `docker-expert`（Node 进程、env、多阶段镜像）→ ② `multi-stage-dockerfile`（deps/build/run 分层）→ ③ `code-refactoring`（目录与边界：如 `backend/`、`frontend/`） |
| **4 data_contracts** | `architecture.md` | `erd.md`, `schema.sql`, `openapi.yaml` | ① **PostgreSQL**：`supabase-postgres-best-practices`；**MySQL**：在 `schema.sql` 与 ERD 中自建规范；② 若需对外工具协议：`mcp-builder`（可选，仅当项目含 MCP/工具服务） |
| **5 backend** | `openapi.yaml`, `schema.sql` | `05-backend/implementation-notes.md` + 代码 + `artifacts/backend-test-report.txt` | ① `superpowers/test-driven-development` → ② `backend-testing`（Jest/Vitest/Supertest 或 Nest 测试策略）→ ③ `code-refactoring` → ④ `superpowers/verification-before-completion`（`-Stage backend`） |
| **6 frontend** | `openapi.yaml`, `implementation-notes.md` | `06-frontend/ui-spec.md` + 代码 + `artifacts/frontend-test-report.txt` | ① `frontend-design` → ② `ui-ux-pro-max` → ③ `superpowers/verification-before-completion`（`-Stage frontend`） |
| **7 qa** | 后端说明 + `ui-spec.md` | `test-plan.md`, `test-cases.md`, `artifacts/e2e-report.txt` | ① `playwright-best-practices` → ② `backend-testing`（API/契约与 E2E 数据准备）→ ③ `superpowers/verification-before-completion`（`-Stage qa`） |
| **8 deploy** | `artifacts/e2e-report.txt` | `08-deploy/*` | ① `docker-expert` → ② `multi-stage-dockerfile` → ③ `superpowers/verification-before-completion`（`-Stage deploy`） |
| **9 ops** | `runbook.md` | `09-ops/*.md` | ① `work-retrospectives` → ② `superpowers/systematic-debugging` |

**本阶段 WMT skill**：始终显式启用 **`wmt-fullstack-pipeline-orchestrator-node`**，按上表驱动其它 skills，不得跳阶段。

## Default Repo Layout

Put in `03-architecture/repo-layout.md` unless overridden:

- **Split packages**: `backend/package.json` + `frontend/package.json`
- **Monolith npm**: single root `package.json` with `lint:client`, `test:server`, `build:client` (script names are optional; gate skips missing scripts)

## Quality Gates (WMT)

From **repository root**:

```powershell
pwsh .\.delivery-pipeline\profiles\node\check-gates.ps1 -Stage backend
pwsh .\.delivery-pipeline\profiles\node\check-gates.ps1 -Stage frontend
pwsh .\.delivery-pipeline\profiles\node\check-gates.ps1 -Stage qa
pwsh .\.delivery-pipeline\profiles\node\check-gates.ps1 -Stage deploy
```

Template path: `templates/wmt-fullstack-skill-pipeline/profiles/node/check-gates.ps1`

### Behavior Summary

- **backend**: prefers `backend/`, `server/`, `api/`, `apps/api/`, else root; runs `lint` / `test` / `build` when separated; if only root package, also tries `test:server`, `build:server`
- **frontend**: separate `frontend/` → `lint` / `test` / `build`; else root monolith tries `lint:client` / `test:client` / `build:client`
- **qa**: `test:e2e` on frontend package if present, else backend root; optional coverage gate
- **deploy**: same Docker build rule as Spring profile

## Response Contract

Same as base orchestrator; state **profile: node** and gate log path under `artifacts/`.

## Checklist

See [stage-checklist.md](stage-checklist.md).
