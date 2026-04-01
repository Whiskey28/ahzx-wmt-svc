# 信贷数字化平台 — 交付流水线文档包

本目录按 **`wmt-fullstack-pipeline-orchestrator-springboot`**（**profile: springboot**）阶段 **1～3** 产出：

| 阶段 | 路径 | 说明 |
|------|------|------|
| intake | `00-intake/` | 问题与约束 |
| discovery | `01-discovery/` | 范围、角色旅程、能力拆解（对齐需求表） |
| planning | `02-plan/` | `backlog.json`、里程碑、风险 |
| architecture | `03-architecture/` | 总体架构、仓库布局、安全与数据治理 |

## 下一步（阶段 4+）

1. **`04-data-contracts/`**：`openapi.yaml`、`schema.sql`、`erd.md`（可从本架构包派生）。
2. **`05-backend` ~ `08-deploy`**：实现与门禁；在仓库根接入模板 `.delivery-pipeline` 后执行：

```powershell
pwsh .\.delivery-pipeline\profiles\springboot\check-gates.ps1 -Stage backend
```

（若模板路径不同，以 skill 内 `templates/wmt-fullstack-skill-pipeline/...` 为准。）

## 需求来源摘要

- 微信小程序：注册登录、授信/申贷、材料与授权、进度与额度查询等。
- 数字化管理平台：驾驶舱、可视化、流程自动化、营销与风控全周期、监管报送等。
- 贷后管理：监测、预警、催收与标准化运营（建议先作为管理端子域）。
