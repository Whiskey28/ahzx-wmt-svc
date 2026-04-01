---
name: wmt-focus-decision-gate
description: 对新任务做准入评估并输出 accept/defer/reject 的决策YAML，防止目标漂移和伪忙。用于机会评估、技术选型、临时任务插队前。先读取 .cursor/planning/wmt-profile.yaml，再输出 decisions 文件。
---

# WMT Focus Decision Gate

> 状态：兼容保留（建议改用 `wmt-life-work-os`）。  
> 该技能仅负责 `decision_gate` 子场景，完整流程请使用总控技能统一管理。

## 目标

在新任务进入前进行统一评估，保证时间投入到高杠杆事项。

## 必须遵守

1. 先读取 `.cursor/planning/wmt-profile.yaml`。
2. `wmt-profile.yaml` 只读，不覆盖。
3. 输出路径：`.cursor/planning/decisions/{date}-{task_slug}.yaml`。
4. 必须基于 `decision_gate.mandatory_checks` 评估。
5. 输出 YAML + 简短中文解释（含建议动作）。

## 输入

- 主输入：`.cursor/planning/wmt-profile.yaml`
- 候选任务上下文：

  - task_name
  - expected_outcome
  - estimated_hours
  - expected_deadline
  - potential_reusability

## 决策规则

- 4项检查通过 >= 3 项：`accept`
- 通过 2 项：`defer`（要求补齐信息或缩小范围）
- 通过 <= 1 项：`reject`

## 输出模板

```yaml
meta:
  based_on_profile: ".cursor/planning/wmt-profile.yaml"
  generated_at: "YYYY-MM-DD"

decision:
  candidate_task: ""
  checks:
    aligns_quarter_goal: false
    has_72h_visible_output: false
    can_be_reusable_asset: false
    must_be_done_by_self: false
  pass_count: 0
  result: "accept|defer|reject"
  reason: ""
  next_action:
    - ""
```

## 示例建议动作

- `accept`：进入本周 Top3 并锁定首个时间块
- `defer`：缩小任务范围，48小时后重评
- `reject`：记录到 backlog，不占用本周深度块
