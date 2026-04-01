---
name: wmt-life-work-master-planner
description: 生成生活与工作总规划（90天/30天/周主线）并输出结构化YAML。用于季度规划、月规划、目标重置、方向不清晰时。先读取 .cursor/planning/wmt-profile.yaml，再生成派生计划文件。
---

# WMT Life Work Master Planner

> 状态：兼容保留（建议改用 `wmt-life-work-os`）。  
> 该技能仅负责 `master_plan` 子场景，完整流程请使用总控技能统一管理。

## 目标

将长期目标转为可执行的季度、月度、周主线，并保证与用户约束一致。

## 必须遵守

1. 先读取 `.cursor/planning/wmt-profile.yaml`。
2. `wmt-profile.yaml` 只读，不覆盖。
3. 仅输出派生文件：
   - `.cursor/planning/quarter-plan.yaml`
   - `.cursor/planning/month-plan.yaml`
4. 输出必须为 YAML，字段命名稳定，不随意新增同义字段。
5. 输出后给出简短中文解释（3-8行）。

## 输入

- 主输入：`.cursor/planning/wmt-profile.yaml`
- 可选输入：本次新增目标、特殊时间约束、阶段性突发事项

## 处理流程

1. 读取 profile 的 `north_star`、`constraints`、`allocation`、`decision_gate`。
2. 生成季度目标拆解（1个主目标 + 2-3个KR）。
3. 生成月度推进节奏（M1打底、M2放大、M3收敛）。
4. 生成本周主线（Top3）与验收标准。
5. 写入 YAML 文件并输出简短说明。

## quarter-plan.yaml 模板

```yaml
meta:
  based_on_profile: ".cursor/planning/wmt-profile.yaml"
  generated_at: "YYYY-MM-DD"

quarter:
  theme: ""
  north_star_goal: ""
  key_results:
    - id: "KR1"
      metric: ""
      target: ""
      deadline: "YYYY-MM-DD"
    - id: "KR2"
      metric: ""
      target: ""
      deadline: "YYYY-MM-DD"
    - id: "KR3"
      metric: ""
      target: ""
      deadline: "YYYY-MM-DD"

execution_principles:
  must_keep:
    - ""
  must_avoid:
    - ""
  decision_gate_rule: ""
```

## month-plan.yaml 模板

```yaml
meta:
  based_on_profile: ".cursor/planning/wmt-profile.yaml"
  generated_at: "YYYY-MM-DD"

month_plan:
  month: "YYYY-MM"
  monthly_focus:
    - ""
    - ""
    - ""
  weekly_milestones:
    - week: "W1"
      deliverables:
        - ""
    - week: "W2"
      deliverables:
        - ""
    - week: "W3"
      deliverables:
        - ""
    - week: "W4"
      deliverables:
        - ""
```

## 质量检查

- 规划是否对齐 `north_star.quarter_goal`
- 是否考虑 `constraints.fixed_unavailable_blocks`
- 是否尊重 `allocation.weekly_ratio_percent`
- 是否包含可验证验收项（metric/target/deadline）
