---
name: wmt-daily-scheduler
description: 基于用户主配置和当天上下文生成日排班YAML，自动避开不可用时段并优先深度工作。用于今日计划、日程排班、时间冲突重排。先读取 .cursor/planning/wmt-profile.yaml，再输出 .cursor/planning/daily/{date}.yaml。
---

# WMT Daily Scheduler

> 状态：兼容保留（建议改用 `wmt-life-work-os`）。  
> 该技能仅负责 `daily_plan` 子场景，完整流程请使用总控技能统一管理。

## 目标

把当天任务转成可执行时间块，确保深度工作与关键任务优先落地。

## 必须遵守

1. 先读取 `.cursor/planning/wmt-profile.yaml`。
2. `wmt-profile.yaml` 只读，不覆盖。
3. 输出路径：`.cursor/planning/daily/{date}.yaml`。
4. 避开 `constraints.fixed_unavailable_blocks`。
5. 至少安排一个深度块，默认优先：

   - `06:20-07:20`
   - `12:30-13:00`
   - `21:30-22:00`
6. 输出 YAML + 简短中文解释。

## 输入

- 主输入：`.cursor/planning/wmt-profile.yaml`
- 当天上下文（可由用户口述）：

  - date
  - top3 任务池
  - 额外不可用时段
  - 当日精力状态（high/medium/low）

## 输出模板（daily/{date}.yaml）

```yaml
meta:
  based_on_profile: ".cursor/planning/wmt-profile.yaml"
  generated_at: "YYYY-MM-DD"

daily_plan:
  date: "YYYY-MM-DD"
  top3:
    - ""
    - ""
    - ""
  schedule:
    - block: "06:20-07:20"
      task: ""
      type: "deep_work"
    - block: "12:30-13:00"
      task: ""
      type: "micro_deep_work"
    - block: "21:30-22:00"
      task: ""
      type: "close_loop"
  admin_tasks:
    - ""
  done_criteria:
    - "Top3至少完成2项"
    - "深度块至少完成1段"
  risks:
    - ""
  fallback_plan:
    - "保留早晨60分钟深度块"
    - "减少低优先级任务"
```

## 排班规则

- 先排 Top3，再排沟通与行政任务。
- 高认知任务放在最早可用深度块。
- 遇突发任务，先通过 `wmt-focus-decision-gate` 再插队。
- 当日精力 low 时，保证最小闭环：1段深度块 + 1项Top任务。
