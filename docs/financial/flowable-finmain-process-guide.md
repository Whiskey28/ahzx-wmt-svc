# Flowable 落地指导：FinMain 业务主流程（本项目示例）

本文以本项目中 `FinMain`（表 `fin_main`）为样例，梳理一个“标志位工作流”业务流程，并提供一份**指导文档**，说明在一个“继承/集成了 Flowable 的系统”中，如何用 Flowable（建议升级到 Flowable 7.x 系列）实现该流程，同时兼容现有 `STATUS`/报表口径，做到最小单元可交付（MVP）。

---

## 1. 本项目现状：流程在哪里、长什么样？

### 1.1 现有“流程”载体

- **核心业务对象**：`com.finance.system.domain.FinMain`  
  - 关键字段：`STATUS`（业务状态）、`recordStatus`（备案状态）、`riskLeaderCode/Name`、`riskProjectCode/Name`、`submitTime` 等
- **状态机枚举**：`com.finance.system.enums.BusinessStatus`  
  - 定义了 0~15 的状态含义（例如 6=已受理待提交，7=待分配风险经理，9=待风险经理审核，11=审批拒绝，12=审批通过……）
- **服务层流转动作（示例）**：`com.finance.system.service.impl.FinMainServiceImpl`  
  - `submit()`：把状态推进到 `STATUS=7`，并初始化 `recordStatus`、风险负责人等
  - `assignRiskProject()`：分配风险经理后推进到 `STATUS=9`
  - `approve()`：审批通过/拒绝推进到 `STATUS=12`/`11`
- **查询/报表强依赖 `STATUS`**：大量 Mapper XML 使用 `STATUS` 条件、区间比较、统计口径（例如 `STATUS >= 12`）

### 1.2 结论

当前系统用 `STATUS` 等字段实现了“流程效果”（节点推进/待办口径/统计口径），但没有流程引擎的：任务表、候选人、历史轨迹、撤回/转办/加签等能力。

因此迁移时的合理策略是：

- **以 Flowable 为流程真相（source of truth）**
- **保留 `STATUS` 作为业务投影（projection）**：短期继续为列表/报表提供口径，后续再逐步弱化或替换。

---

## 2. 选定一个“最小单元（MVP）”流程

为了避免一次性改动全量状态机，建议先选取一个闭环子流程做 MVP。结合本项目现有代码，推荐从下面这段开始：

1. **提交到风险部门**：6 → 7（`submit()`）
2. **分配风险经理**：7/8 → 9（`assignRiskProject()`）
3. **风险经理审核 + 审批结果**：9/10 → 12（通过）或 11（拒绝）（`approve()`）

MVP 的验收标准：

- 新建业务单在该区间内的推进动作全部由 Flowable 驱动
- Flowable 里可查到：当前待办、办理人、审批意见、历史轨迹
- `fin_main.STATUS` 在关键节点保持与流程一致（用于列表与报表）

---

## 3. BPMN 设计（FinMain 风险审批子流程）

建议流程 Key：`finMainRiskApproval`

### 3.1 节点与职责

| 节点 ID（建议） | 类型 | 语义 | 现有 STATUS 投影 |
|---|---|---|---|
| `start` | startEvent | 流程开始 | - |
| `submit_to_risk` | userTask 或 serviceTask | 业务提交到风险部门 | 7 |
| `risk_assign` | userTask | 风险负责人分配风险经理 | 7 / 8（可选） |
| `risk_review` | userTask | 风险经理审核 | 9 |
| `approve_gateway` | exclusiveGateway | 通过/拒绝分支 | - |
| `approved` | endEvent | 审批通过 | 12 |
| `rejected` | endEvent | 审批拒绝 | 11 |

说明：

- `submit_to_risk` 是否做成 userTask 取决于你们 UI：  
  - 若“提交”按钮就是一个业务动作，可用 `serviceTask` + `delegateExpression` 做自动推进；  
  - 若需要形成待办，可用 `userTask`。
- `risk_assign` 与 `risk_review` 一定是 `userTask`，这样才能落到待办系统。

### 3.2 流程变量（建议最小集）

| 变量名 | 类型 | 用途 |
|---|---|---|
| `businessNo` | string | 业务主键，用于与 `fin_main` 关联 |
| `bizType` | string | 业务类型（FR/RD/…），用于动态指派 |
| `riskLeaderId` | string/long | 风险负责人（分配者） |
| `riskManagerId` | string/long | 风险经理（审核者） |
| `passed` | boolean | 审批结果 |
| `reason` | string | 拒绝原因/意见 |

---

## 4. 任务分配策略（如何对接你们现有用户体系）

本项目里已有类似能力：`userMapper.selectRiskLeaderByBusinessType(main.getType())`（按业务类型找风险负责人）。

在 Flowable 中建议这样落地：

### 4.1 风险分配任务（`risk_assign`）

- **候选人/候选组**：
  - 优先推荐“候选组”：例如 `ROLE_RISK_LEADER` + `bizType` 维度（按组织架构划分）
  - 或者直接用候选人：`candidateUsers = [riskLeaderId]`
- **实现方式**：
  - BPMN 里设置 `candidateUsers`/`candidateGroups` 表达式；或在 TaskListener（create）中动态设置候选人。

### 4.2 风险审核任务（`risk_review`）

- `assignee` 由 `risk_assign` 节点完成时确定：`riskManagerId`
- 实现方式：完成 `risk_assign` 任务时，写流程变量 `riskManagerId`，并在 `risk_review` 的 assignee 上写表达式 `${riskManagerId}`。

---

## 5. 与业务表的“投影回写”（STATUS 不消失，而是由引擎驱动）

### 5.1 为什么要回写？

因为当前系统大量列表/报表/统计依赖 `fin_main.STATUS`。MVP 阶段如果删掉 `STATUS`：

- 你需要立刻重写大量 Mapper SQL（风险极高）
- 报表口径会断裂（历史对不上）

因此 MVP 阶段建议：

- `STATUS` 继续存在
- 但**不再由业务代码随意 setSTATUS() 推进**，而改为：
  - **由 Flowable 节点进入/退出事件统一回写**

### 5.2 推荐回写机制

实现一个通用“流程投影服务”（伪接口）：

- `FinMainWorkflowProjectionService#onNodeEntered(businessNo, nodeId, variables)`
- `FinMainWorkflowProjectionService#onNodeCompleted(...)`

在 Flowable 中通过以下方式触发：

- TaskListener（create/complete）或 ExecutionListener（start/end）
- 每个关键节点触发一次回写

回写内容示例：

- 进入 `risk_assign`：`STATUS=7`，回写 `riskLeaderCode/Name`（来自变量/计算），`recordStatus=0`，`submitTime=now`
- 完成 `risk_assign` 且进入 `risk_review`：`STATUS=9`，回写 `riskProjectCode/Name`（若系统用这个字段表示风险经理）
- 流程结束（通过/拒绝）：`STATUS=12` 或 `STATUS=11`，回写 `refuseReason` 等

---

## 6. 接口改造：把“改标志位”改成“推 Flowable 任务”

以当前 `FinMainServiceImpl` 为映射，MVP 只改 3 类动作：

### 6.1 提交（原 `submit()`）

旧做法：查 `FinMain` → `setSTATUS("7")` → update

新做法（建议）：

1. 校验业务单存在、当前状态允许提交（例如必须为 6）
2. 计算风险负责人（复用现有 mapper/service）
3. **启动流程实例**（或推进到下一节点）：  
   - `runtimeService.startProcessInstanceByKey("finMainRiskApproval", businessNo, variables)`
4. 由监听器回写 `STATUS=7`、`recordStatus=0`、`riskLeader*`、`submitTime`

### 6.2 分配风险经理（原 `assignRiskProject()`）

旧做法：循环业务号 → `setSTATUS("9")` → update → 写日志

新做法（建议）：

1. 查到对应业务的当前待办任务（`risk_assign`），校验当前用户有权限办理
2. `taskService.complete(taskId, Map.of("riskManagerId", xxx))`
3. 监听器在进入 `risk_review` 时回写 `STATUS=9` 并同步字段

### 6.3 审批（原 `approve()`）

旧做法：passed→12 else→11 → update

新做法（建议）：

1. 查到当前业务在 `risk_review`（或后续审批节点）的待办任务
2. `taskService.complete(taskId, Map.of("passed", true/false, "reason", reason))`
3. 由网关走向 approved/rejected，并在 end listener 回写 `STATUS=12/11`

---

## 7. 待办/已办/轨迹：最小实现方式

### 7.1 待办列表（MVP）

做法：

1. `taskService.createTaskQuery().taskCandidateOrAssigned(userId).processDefinitionKey("finMainRiskApproval")...`
2. 从任务变量/业务 key 拿到 `businessNo`
3. 批量查询 `FinMain`（按 businessNo in (...)）
4. 返回给前端（包含 taskId、taskName、业务信息）

### 7.2 已办/历史

做法：

- `historyService.createHistoricTaskInstanceQuery().taskAssignee(userId)...`
- 或按 `businessNo` 查询 `HistoricProcessInstance`、`HistoricActivityInstance`

### 7.3 审批意见/批注

- `taskService.addComment(taskId, processInstanceId, comment)`  
  `reason` 也可作为 comment 或变量存储

---

## 8. 数据迁移策略（MVP 推荐：新单新流程）

为了最小风险：

- **只让新创建/新进入 6→7 的业务单走 Flowable**
- 存量单保持原逻辑直至完结（或后续补迁移）

技术上需要：

- 在 `fin_main` 增加（或复用）一个字段保存 `processInstanceId`（若已有类似字段可复用）  
  - 便于从业务单定位流程实例
- 或使用 Flowable 的 Business Key：`businessKey = businessNo`（推荐）  
  - 查询流程实例时用 businessKey 反查即可

---

## 9. 与本项目技术栈的注意事项（你们将遇到的坑）

1. **你们的 SQL/报表大量依赖 `STATUS`**：MVP 必须回写投影，否则页面/报表会崩。
2. **`STATUS` 是字符串但被当数字比较**：Mapper 中存在 `cast/convert(STATUS as unsigned/signed)`，后续若要替换字段类型或去掉字段，需要一次性清理这些比较口径。
3. **Flowable 版本升级**：当前项目依赖 Flowable 6.8.0。若升级到 7.x：\n+   - 需要评估 Spring Boot 版本兼容（你们当前是 Spring Boot 2.5.x）；\n+   - Flowable 7 对依赖栈与数据库脚本都有变化，建议先在分支/实验环境跑通。\n+4. **权限模型**：候选组/候选人必须与现有 SysUser/Role/Dept 打通，否则“待办查不到”会被误判为流程不工作。

---

## 10. MVP 交付清单（最小可用）

1. 新增 BPMN：`finMainRiskApproval.bpmn20.xml`
2. 启动与推进接口：\n+   - 提交：启动流程（businessKey=businessNo）\n+   - 分配风险经理：完成 `risk_assign`\n+   - 审批：完成 `risk_review`\n+3. 监听器：关键节点回写 `fin_main` 投影字段（至少 `STATUS`）\n+4. 待办接口：按用户查询 Flowable Task → 关联 `FinMain` 返回\n+5. 轨迹接口：按 `businessNo` 查询历史活动/评论\n+
做到以上五项，你就可以在不大改原有报表/列表 SQL 的前提下，把一个“标志位流程”升级为“真实工作流引擎驱动的流程”。后续再按同样模式逐段迁移 0~15 的其他状态链路。

---

## 11. 本仓库已提供的工作流设计产物

基于上述 MVP 设计，本仓库已落地以下内容，可直接接入或按需扩展。

### 11.1 BPMN 流程定义

| 文件 | 说明 |
|------|------|
| `docs/financial/bpmn/finMainRiskApproval.bpmn20.xml` | FinMain 风险审批子流程 BPMN 2.0 定义 |

- **流程 Key**：`finMainRiskApproval`
- **节点**：start → submit_to_risk(serviceTask) → risk_assign(userTask) → risk_review(userTask) → approve_gateway → approved/rejected(endEvent)
- **流程变量**：`businessNo`、`bizType`、`riskLeaderId`、`riskManagerId`、`passed`、`reason`

部署方式（二选一）：

1. **通过流程设计器**：将上述 BPMN 导入 BPM 模型设计器，保存并发布为流程定义。
2. **通过资源部署**：若希望从 classpath 自动部署，可将该文件放到 `wmt-module-bpm/src/main/resources/processes/` 并在 Flowable 配置中启用对应资源路径。

### 11.2 状态投影监听器与 API

| 类 | 说明 |
|------|------|
| `BpmFinMainRiskApprovalStatusListener` | 监听 `finMainRiskApproval` 流程结束，将 BPM 状态(2/3) 映射为 fin_main.STATUS(12/11) 并回写 |
| `FinMainWorkflowProjectionApi` | 投影 API：由拥有 `fin_main` 的模块实现，执行实际 DB 更新 |

- **监听器**：`com.wmt.module.bpm.service.finmain.listener.BpmFinMainRiskApprovalStatusListener`
- **流程 Key 常量**：`BpmFinMainRiskApprovalStatusListener.PROCESS_KEY = "finMainRiskApproval"`
- **映射规则**：审批通过(2) → STATUS=12，审批拒绝(3) → STATUS=11；拒绝时传入 `reason` 写入拒绝原因。

在拥有 `fin_main` 的模块中实现 `FinMainWorkflowProjectionApi` 并注册为 Spring Bean，即可在流程结束时自动回写业务表；未实现时流程照常结束，仅不回写。

### 11.3 后续可补充

- 启动与推进接口：在业务 Service 中调用 `runtimeService.startProcessInstanceByKey("finMainRiskApproval", businessNo, variables)` 及 `taskService.complete(taskId, vars)`。
- 待办/已办/轨迹：复用现有 BPM 待办、历史查询接口，按 `processDefinitionKey=finMainRiskApproval` 与 `businessKey=businessNo` 过滤即可。

