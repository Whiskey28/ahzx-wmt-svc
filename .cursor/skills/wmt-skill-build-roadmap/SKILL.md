---
name: wmt-skill-build-roadmap
description: 将学习目标转换为12周可交付路线图（学习-实践-资产化），输出结构化YAML。用于学习路线设计、技能迭代、从学习到产出转化。先读取 .cursor/planning/wmt-profile.yaml，再输出 roadmap 文件。
---

# WMT Skill Build Roadmap

> 状态：兼容保留（建议改用 `wmt-life-work-os`）。  
> 该技能仅负责 `skill_roadmap` 子场景，完整流程请使用总控技能统一管理。

## 目标

把“学技术”转为“可复用资产”，避免仅信息摄入而无业务结果。

## 必须遵守

1. 先读取 `.cursor/planning/wmt-profile.yaml`。
2. `wmt-profile.yaml` 只读，不覆盖。
3. 输出路径：`.cursor/planning/12-week-roadmap.yaml`。
4. 路线图必须覆盖：

   - 学习目标
   - 实践项目
   - 可复用资产
   - 业务价值验证
5. 输出 YAML + 简短中文解释。

## 输入

- 主输入：`.cursor/planning/wmt-profile.yaml`
- 上下文输入：

  - 当前能力盘点
  - 目标能力
  - 每周可投入小时
  - 当前副业或主业可结合场景

## 输出模板

```yaml
meta:
  based_on_profile: ".cursor/planning/wmt-profile.yaml"
  generated_at: "YYYY-MM-DD"
  duration_weeks: 12

roadmap:
  phase_1_weeks_1_4:
    objective: "打基础并形成第一个可见产出"
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
    objective: "强化迁移并接入真实场景"
    weekly_deliverables: []
  phase_3_weeks_9_12:
    objective: "沉淀模板和复用体系"
    weekly_deliverables: []

value_validation:
  business_metrics:
    - ""
  learning_metrics:
    - ""
  reuse_metrics:
    - ""
```

## 质量检查

- 每周是否都有可交付物
- 是否至少每2周产生一个可复用资产
- 是否包含业务验证指标（而非仅学习进度）
