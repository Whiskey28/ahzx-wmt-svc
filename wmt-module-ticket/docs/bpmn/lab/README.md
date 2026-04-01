# 工单模块 Flowable 实验流程（BPMN 样例）

将同目录下 `*.bpmn20.xml` **导入**「流程管理 → 流程模型」或按你们环境支持的部署方式发布，使 **流程定义 Key** 与下表一致。

### 流程图不显示？

实例详情页「流程图」由前端 **bpmn-js** 根据 BPMN XML 渲染。若 XML **只有语义（process / sequenceFlow）而没有 BPMN DI**（`bpmndi:BPMNDiagram`、各节点的 `dc:Bounds`、连线的 `di:waypoint`），导入可能失败或画布空白；引擎仍可正常部署与执行。本目录样例已包含 **BPMNDI**。若你手写或从其它工具导出无 DI 的 XML，请用 **Camunda Modeler / Flowable Modeler / 系统内置 BPMN 设计器** 打开后重新保存导出，或自行补全 `http://www.omg.org/spec/BPMN/20100524/DI` 段。

| 文件 | 流程定义 Key（process id） | 说明 |
|------|---------------------------|------|
| ticket_lab_xor.bpmn20.xml | `ticket_lab_xor` | 排他网关，变量 `amount`，分支阈值 1000 |
| ticket_lab_select.bpmn20.xml | `ticket_lab_select` | 首节点 **Activity_select_approve** 为「发起人自选」(candidateStrategy=35) |
| ticket_lab_parallel.bpmn20.xml | `ticket_lab_parallel` | 并行网关，两路用户任务 |
| ticket_lab_receive.bpmn20.xml | `ticket_lab_receive` | 用户任务后接 Receive Task **Activity_receive_wait** |

## 业务表单（审批详情能否看到业务数据，必配）

仅导入 BPMN、不配置表单时，流程可跑通，但**审批中心 → 流程实例详情 → 审批详情**左侧会空白：该页只有在流程定义为 **业务表单** 时才会挂载自定义 Vue 组件，并用 **`businessKey`（= 工单实验表主键 id）** 拉取业务数据。

在「流程管理 → 流程模型」打开对应模型，在 **表单设计** 中设置：

1. **表单类型**：选 **业务表单**（与「流程表单」相对）。
2. **表单提交路由**：与 `src/views` 下发起页路径一致（供「发起流程」跳转；`registerComponent` 用子串匹配 `.vue` 文件）。
3. **表单查看地址**：填 **`ticket/biz/lab/detail`**（四个实验流程**共用**同一详情组件 `views/ticket/biz/lab/detail.vue`，不要写成 `ticket/biz/lab/xor/detail` 等不存在的路径）。

| 流程定义 Key | 表单提交路由（示例） | 表单查看地址（共用） |
|--------------|----------------------|------------------------|
| ticket_lab_xor | `ticket/biz/lab/xor/create` | `ticket/biz/lab/detail` |
| ticket_lab_select | `ticket/biz/lab/select/create` | `ticket/biz/lab/detail` |
| ticket_lab_parallel | `ticket/biz/lab/parallel/create` | `ticket/biz/lab/detail` |
| ticket_lab_receive | `ticket/biz/lab/receive/create` | `ticket/biz/lab/detail` |

保存并**重新发布**流程定义后，再发起的实例在审批详情中即可加载业务信息。后端发起时已设置 `businessKey` 为业务主键（见 `TicketBpmWorkflowLabServiceImpl`）。

**路径写法说明**：须能匹配到 `import.meta.glob('../views/**/*.vue')` 中的文件路径（例如 OA 请假为 `/bpm/oa/leave/detail`）。本项目工单实验详情请使用 **`ticket/biz/lab/detail`**，不要使用 `/ticket/biz/...` 前缀（否则匹配不到 `views/ticket/...`）。

### 审批人能否「改」表单？

默认业务详情页为**只读展示**。若需要审批人在此页修改字段，需二选一：**改用流程表单**并在模型里配置字段读写权限，或在 `detail.vue` 中根据 `taskId`/流程状态自行扩展可编辑区与保存接口。

## 数据库

执行仓库根目录 `sql/mysql/ticket_bpm_workflow_lab.sql` 创建表 `ticket_bpm_workflow_lab`。

## API

- `POST /ticket/bpm-lab/create`：创建并发起流程（body 含 `labType`：XOR / SELECT_USER / PARALLEL / RECEIVE）
- `GET /ticket/bpm-lab/get?id=`：详情
- `POST /ticket/bpm-lab/trigger-receive?id=`：仅 **RECEIVE** 实验，在发起人完成「发起人提交」任务后调用，用于推进 Receive Task

候选人策略数字与 `BpmTaskCandidateStrategyEnum` 一致：35=发起人自选，36=发起人自己。
