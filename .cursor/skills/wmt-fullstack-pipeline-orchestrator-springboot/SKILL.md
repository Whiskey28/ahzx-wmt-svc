---
name: wmt-fullstack-pipeline-orchestrator-springboot
description: WMT-owned fullstack pipeline orchestrator with Spring Boot (Maven) + React defaults, monorepo layout, and Spring-specific quality gates. Use when the backend is Java Spring Boot and the user wants the same staged delivery as wmt-fullstack-pipeline-orchestrator with Maven test and multi-module path resolution.
---

# WMT Fullstack Pipeline — Spring Boot Profile

## When To Use

- Backend: **Spring Boot**, **Maven** (`pom.xml`), optional `mvnw`
- Frontend: **React** with `package.json`
- Repo: **单仓**；推荐目录：`backend/`（或 `server/`、`api/`）+ `frontend/`（或 `client/`、`web/`）

## Inherits

阶段定义、产物路径、门禁脚本以 **`templates/wmt-fullstack-skill-pipeline/pipeline.yaml`** 为准。  
本 profile **在每个节点下方列出须按序调用的外部 skills**（非 WMT 前缀）；执行时先读入参、再产出、最后跑对应门禁。

## 每阶段须调用的 Skills（按序）

下列为 **Spring Boot + React 单仓** 的默认编排；若某 skill 未安装，选最近似能力并说明替换。

| 阶段 | 主要输入 | 主要输出 | 按序调用的 Skills |
| ---- | -------- | -------- | ----------------- |
| **1 discovery** | `00-intake/problem.md`, `constraints.md` | `01-discovery/*.md`（3 个） | ① `superpowers/brainstorming` → ② `superpowers/writing-plans` |
| **2 planning** | discovery 三文件 | `02-plan/backlog.json`, `milestones.md`, `risks.md` | ① `superpowers/writing-plans` → ② `superpowers/executing-plans` |
| **3 architecture** | `backlog.json`, `milestones.md` | `03-architecture/*.md`（3 个） | ① `docker-expert`（运行时/端口/Compose 约束）→ ② `multi-stage-dockerfile`（Spring 分层 JAR 镜像策略）→ ③ `code-refactoring`（模块边界与包结构） |
| **4 data_contracts** | `architecture.md` | `erd.md`, `schema.sql`, `openapi.yaml` | ① **PostgreSQL**：`supabase-postgres-best-practices`；**MySQL**：无专用 skill 时用架构文档自建索引/命名规范，并对照 `schema.sql` 自检 |
| **5 backend** | `openapi.yaml`, `schema.sql` | `05-backend/implementation-notes.md` + 代码 + `artifacts/backend-test-report.txt`（门禁生成） | ① `superpowers/test-driven-development` → ② `backend-testing`（JUnit/MockMvc/集成测试策略）→ ③ `code-refactoring`（分层与可测性）→ ④ `superpowers/verification-before-completion`（跑通 `profiles/springboot/check-gates.ps1 -Stage backend`） |
| **6 frontend** | `openapi.yaml`, `implementation-notes.md` | `06-frontend/ui-spec.md` + 代码 + `artifacts/frontend-test-report.txt` | ① `frontend-design` → ② `ui-ux-pro-max` → ③ `superpowers/verification-before-completion`（`-Stage frontend`） |
| **7 qa** | 后端说明 + `ui-spec.md` | `test-plan.md`, `test-cases.md`, `artifacts/e2e-report.txt` | ① `playwright-best-practices` → ② `backend-testing`（契约/回归用例对齐 OpenAPI）→ ③ `superpowers/verification-before-completion`（`-Stage qa`） |
| **8 deploy** | `artifacts/e2e-report.txt` | `08-deploy/*`（Dockerfile、compose、runbook、rollback） | ① `docker-expert` → ② `multi-stage-dockerfile` → ③ `superpowers/verification-before-completion`（`-Stage deploy`） |
| **9 ops** | `runbook.md` | `09-ops/*.md`（监控、告警、SLO、事故模板） | ① `work-retrospectives`（运维节奏与复盘）→ ② `superpowers/systematic-debugging`（排障流程） |

**本阶段 WMT skill**：始终显式启用 **`wmt-fullstack-pipeline-orchestrator-springboot`**，按上表驱动其它 skills，不得跳阶段。

## Default Repo Layout (architecture / repo-layout.md)

Document this in `03-architecture/repo-layout.md` unless user overrides:

```
/05-backend code → backend/   (Spring Boot, Maven)
/06-frontend code → frontend/ (React, Vite or CRA)
04-data-contracts/ → OpenAPI + schema at repo root (delivery metadata)
08-deploy/ → Dockerfile(s) targeting backend + static frontend or compose
```

## Backend Implementation Hints

- Align REST with `04-data-contracts/openapi.yaml` (generate or hand-map DTOs).
- typical: Spring Web, Validation, Spring Data JPA, Flyway/Liquibase optional.
- Record API quirks in `05-backend/implementation-notes.md`.

## Quality Gates (WMT)

Run from **repository root** after the corresponding stage:

```powershell
pwsh .\.delivery-pipeline\profiles\springboot\check-gates.ps1 -Stage backend
pwsh .\.delivery-pipeline\profiles\springboot\check-gates.ps1 -Stage frontend
pwsh .\.delivery-pipeline\profiles\springboot\check-gates.ps1 -Stage qa
pwsh .\.delivery-pipeline\profiles\springboot\check-gates.ps1 -Stage deploy
```

Path if template not renamed: `templates/wmt-fullstack-skill-pipeline/profiles/springboot/check-gates.ps1`

### What The Script Does

- **backend**: finds first `pom.xml` in `backend/`, `server/`, `api/`, `services/api`, `apps/api`, or root; runs `mvn -B test` or `mvnw`
- **frontend**: finds `package.json` under `frontend/`, `client/`, `web/`, `apps/web/`, or root; runs `lint` / `test` / `build` if scripts exist
- **qa**: runs `test:e2e` if present; optional coverage threshold on `coverage/coverage-summary.json`
- **deploy**: `docker build -f 08-deploy/Dockerfile`

## Response Contract

Same as base orchestrator; always state **profile: springboot** and evidence from `check-gates.ps1`.

## Checklist

See [stage-checklist.md](stage-checklist.md).
