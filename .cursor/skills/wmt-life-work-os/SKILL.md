---
name: wmt-life-work-os
description: WMT 个人工作生活总控技能。基于单一 profile 文件统一完成季度/月度规划、日排班、周复盘、任务准入评估与12周学习路线。用于生活工作规划、时间管理、周复盘、学习路线和机会决策等场景。
---

# WMT Life Work OS

## 目标

用一个技能统一管理你的全流程规划，减少多技能切换成本。

## 你关心的说明（关于 skill-creator）

本技能的结构与规范参考了 `@skill-creator` 的要求（YAML frontmatter、清晰触发描述、单技能文件、流程化模板、避免过长/过散）。你反馈的关键问题是 **总控技能缺少子流程的完整“定义与处理流程”**，本次更新已将 5 个子场景的完整步骤与模板全部内嵌在本文件中。

## 必须遵守

1. 先读取 `.cursor/planning/wmt-profile.yaml`。
2. `wmt-profile.yaml` 只读，不覆盖。
3. 所有输出均为 YAML，并给出简短中文说明。
4. 仅写入派生文件，不修改其它业务文件。
5. 若输入缺失，先提示补齐最少字段再生成。

## 场景入口

通过 `scenario` 选择要执行的子流程：

- `master_plan`：生成季度/月度主规划
- `daily_plan`：生成当日日程
- `weekly_review`：生成周复盘和下周计划
- `decision_gate`：评估新任务是否准入
- `skill_roadmap`：生成12周学习-实践-资产化路线
- `full_cycle`：一次性串行执行（推荐周日使用）

## 输入协议

```yaml
request:
  scenario: "master_plan|daily_plan|weekly_review|decision_gate|skill_roadmap|full_cycle"
  date: "YYYY-MM-DD"
  week: "YYYY-Www"
  extra_context: {}
```

`extra_context` 按场景使用：

- `master_plan`: `new_goals`, `special_constraints`
- `daily_plan`: `top3`, `extra_unavailable_blocks`, `energy_level`
- `weekly_review`: `wins`, `misses`, `time_deviation`, `issues`
- `decision_gate`: `task_name`, `expected_outcome`, `estimated_hours`, `deadline`, `reusability`
- `skill_roadmap`: `current_skills`, `target_skills`, `weekly_hours`, `business_scenarios`

## 输出文件映射

- `master_plan`
  - `.cursor/planning/quarter-plan.yaml`
  - `.cursor/planning/month-plan.yaml`
- `daily_plan`
  - `.cursor/planning/daily/{date}.yaml`
- `weekly_review`
  - `.cursor/planning/reviews/{year}-W{week}-review.yaml`
  - `.cursor/planning/weekly/{year}-W{week}.yaml`
- `decision_gate`
  - `.cursor/planning/decisions/{date}-{task_slug}.yaml`
- `skill_roadmap`
  - `.cursor/planning/12-week-roadmap.yaml`
- `full_cycle`
  - 依次生成以上全部目标文件（按可用输入）

## 执行规则

1. **对齐主线**：所有输出对齐 `north_star.quarter_goal`。
2. **时间约束优先**：必须避开 `constraints.fixed_unavailable_blocks`。
3. **决策门生效**：插队任务先走 `decision_gate`。
4. **反停滞检查**：输出中必须体现以下检查之一：
   - 新闻摄入是否产出结论
   - 工具行为是否产生业务结果
   - 学习是否转化为实践或收益
5. **最小可执行**：给出“今天就能执行”的最小动作。

## 子场景定义与处理流程（全集版）

以下 5 个子场景是本技能的“内置流程”，不需要依赖其它分拆技能文件。

### A. `master_plan`（季度/月度主规划）

#### 输入最少字段

- profile：`north_star`、`constraints`、`allocation`、`decision_gate`
- 可选：`extra_context.new_goals`、`extra_context.special_constraints`

#### 处理步骤

1. 从 `north_star.quarter_goal` 提炼季度主题与主目标。
2. 定义 2-3 个 Key Results（每个 KR 必须满足：metric/target/deadline）。
3. 将季度主线压缩为“本月推进节奏”：W1 打底、W2 修正、W3 放大、W4 收敛复盘。
4. 写入 `quarter-plan.yaml` 与 `month-plan.yaml`。
5. 给出简短中文说明（包含：本季度不做什么/决策门提醒/最小执行动作）。

#### 输出：`.cursor/planning/quarter-plan.yaml` 模板

```yaml
meta:
  based_on_profile: ".cursor/planning/wmt-profile.yaml"
  generated_at: "YYYY-MM-DD"
  planning_window: "YYYY-Qn"

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

weekly_focus_framework:
  fixed_top3_domains:
    - ""
  time_budget_ratio:
    main_job_dev: 35
    side_business_paper: 25
    ai_learning_build: 25
    mba_study: 10
    planning_review_system: 5
```

#### 输出：`.cursor/planning/month-plan.yaml` 模板

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
  risk_controls:
    - ""
```

### B. `daily_plan`（当日日程）

#### B1. 输入最少字段

- profile：`constraints.fixed_unavailable_blocks`、`constraints.preferred_deep_work_blocks`
- `extra_context.top3`（3个任务）
- 可选：`extra_context.extra_unavailable_blocks`、`extra_context.energy_level`

#### B2. 处理步骤

1. 校验 `top3` 非空且为 3 项；若不足则提示补齐。
2. 以 `preferred_deep_work_blocks` 为骨架排入 Top3（高认知任务放最早可用块）。
3. 自动避开 `fixed_unavailable_blocks` 与 `extra_unavailable_blocks`。
4. 生成 done_criteria / risks / fallback_plan（必须包含 profile 的兜底顺序）。
5. 写入 `.cursor/planning/daily/{date}.yaml`。

#### B3. 输出模板：`.cursor/planning/daily/{date}.yaml`

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
    - "深度块至少完成1段（优先早晨）"
  risks:
    - ""
  fallback_plan:
    - ""
```

### C. `weekly_review`（周复盘 + 下周计划）

#### C1. 输入最少字段

- profile：`north_star.success_criteria`、`review_rhythm.weekly_review`
- 可选：`extra_context.wins`、`extra_context.misses`、`extra_context.time_deviation`、`extra_context.issues`

#### C2. 处理步骤

1. 汇总 wins/misses（若为空则输出空模板并给“填报提示”）。
2. 从 misses 推导 root_causes（至少 3 条，指向流程问题而非情绪）。
3. 输出四象限动作：delete/simplify/delegate/automate（每类至少 1 条，允许为空但需解释原因）。
4. 生成下周 next_week_top3（对齐 `north_star.success_criteria` 与 `allocation`）。
5. 写入两个文件：review 与 next week plan。

#### C3. 输出模板：review

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

#### C4. 输出模板：next week

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

### D. `decision_gate`（新任务准入评估）

#### D1. 输入最少字段

- profile：`decision_gate.mandatory_checks`、`decision_gate.default_rule`
- `extra_context.task_name`
- 可选：`expected_outcome`、`estimated_hours`、`deadline`、`reusability`

#### D2. 处理步骤

1. 将候选任务映射到 4 个 mandatory_checks（逐条给出 true/false 与一句理由）。
2. 计算 pass_count，并按 default_rule 输出 result（accept/defer/reject）。
3. 输出 next_action（accept：排入时间块；defer：补信息/缩范围；reject：写入backlog建议）。
4. 写入 `.cursor/planning/decisions/{date}-{task_slug}.yaml`。

#### D3. 输出模板

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

### E. `skill_roadmap`（12周学习-实践-资产化路线）

#### E1. 输入最少字段

- profile：`identity`、`anti_stagnation`、`allocation`
- 可选：`extra_context.current_skills`、`target_skills`、`weekly_hours`、`business_scenarios`

#### E2. 处理步骤

1. 按 1-4 / 5-8 / 9-12 三阶段输出：每周都有 deliverable（学习/实践/资产）。
2. 每两周至少一个可复用资产（模板/SOP/脚本/skill片段）。
3. 必须包含 value_validation：business/learning/reuse 三类指标。
4. 写入 `.cursor/planning/12-week-roadmap.yaml`。

#### E3. 输出模板

```yaml
meta:
  based_on_profile: ".cursor/planning/wmt-profile.yaml"
  generated_at: "YYYY-MM-DD"
  duration_weeks: 12

roadmap:
  phase_1_weeks_1_4:
    objective: ""
    weekly_deliverables:
      - week: 1
        learning_focus: ""
        practice_task: ""
        asset_output: ""
      - week: 2
        learning_focus: ""
        practice_task: ""
        asset_output: ""
      - week: 3
        learning_focus: ""
        practice_task: ""
        asset_output: ""
      - week: 4
        learning_focus: ""
        practice_task: ""
        asset_output: ""
  phase_2_weeks_5_8:
    objective: ""
    weekly_deliverables: []
  phase_3_weeks_9_12:
    objective: ""
    weekly_deliverables: []

value_validation:
  business_metrics:
    - ""
  learning_metrics:
    - ""
  reuse_metrics:
    - ""
```

## 建议默认用法

- 每日：`scenario: daily_plan`
- 每周日：`scenario: weekly_review` 后接 `scenario: master_plan`
- 新机会出现时：`scenario: decision_gate`
- 每月第一周：`scenario: skill_roadmap`
