---
name: wmt-weekly-review-coach
description: 生成每周复盘与下周计划YAML，输出 wins/misses/根因/改进动作，避免只忙不进步。用于周复盘、下周安排、执行复盘。先读取 .cursor/planning/wmt-profile.yaml，再输出 reviews 与 weekly 计划文件。
---

# WMT Weekly Review Coach

> 状态：兼容保留（建议改用 `wmt-life-work-os`）。  
> 该技能仅负责 `weekly_review` 子场景，完整流程请使用总控技能统一管理。

## 目标

形成稳定周复盘闭环：复盘事实、识别根因、给出下周可执行计划。

## 必须遵守

1. 先读取 `.cursor/planning/wmt-profile.yaml`。
2. `wmt-profile.yaml` 只读，不覆盖。
3. 输出：
   - `.cursor/planning/reviews/{year}-W{week}-review.yaml`
   - `.cursor/planning/weekly/{year}-W{week}.yaml`
4. 必须包含：

   - wins
   - misses
   - root_causes
   - delete/simplify/delegate/automate
   - next_week_top3
5. 输出 YAML + 简短中文解释。

## 输入

- 主输入：`.cursor/planning/wmt-profile.yaml`
- 周执行日志（用户提供）：

  - 已完成项
  - 未完成项
  - 时间分配偏差
  - 收益与问题

## review 文件模板

```yaml
meta:
  based_on_profile: ".cursor/planning/wmt-profile.yaml"
  generated_at: "YYYY-MM-DD"
  week: "YYYY-Www"

weekly_review:
  wins:
    - ""
  misses:
    - ""
  root_causes:
    - ""
  decisions:
    delete:
      - ""
    simplify:
      - ""
    delegate:
      - ""
    automate:
      - ""
  anti_stagnation_check:
    news_without_output: false
    tools_without_business_result: false
    learning_without_practice: false
```

## next week 文件模板

```yaml
meta:
  based_on_profile: ".cursor/planning/wmt-profile.yaml"
  generated_at: "YYYY-MM-DD"
  week: "YYYY-Www"

weekly_plan:
  next_week_top3:
    - ""
    - ""
    - ""
  must_deliver:
    - ""
  time_budget_adjustment:
    main_job_dev: 35
    side_business_paper: 25
    ai_learning_build: 25
    mba_study: 10
    planning_review_system: 5
  risk_controls:
    - ""
```

## 质量检查

- 是否对齐 `north_star.success_criteria`
- 是否执行了删除/简化/委托/自动化
- 下周 Top3 是否可验证、可交付
