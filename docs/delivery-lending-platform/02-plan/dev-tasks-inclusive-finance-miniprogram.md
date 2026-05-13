# 普惠金融小程序需求 — 开发层任务拆解

> 对齐：`docs/delivery-lending-platform/03-architecture/architecture.md`、`04-data-contracts/openapi.yaml`、`.cursor/rules` 中 Java/Spring 约定（`CommonResult`、`PageResult`、`BaseMapperX`、`ServiceException`）。  
> 需求来源：内蒙古银行普惠小程序需求规格说明书 + `nm/普惠金融-小程序需求分析报告.md`（路径以你本机为准）。

---

## 0. 开工前必须澄清（否则实现会返工）

| 任务 ID | 内容 | 产出 |
|---------|------|------|
| CL-01 | 确认注册成功路径：自动登录进实名 **或** 回登录页 | 产品签字 + 用例表 |
| CL-02 | 统一口令策略：长度区间、是否允许连续相同字符 | `PasswordPolicy` 常量文档 |
| CL-03 | 合同签署「人脸→短信」与提款「短信→人脸」是否刻意区分 | 设计说明 1 页 |
| CL-04 | 「重复进件」判定数据来源：仅本地表 **或** 实时查信贷 | 接口契约字段 |
| CL-05 | C 端用户体系：复用 `wmt-module-member` **或** `fin-customer` 自建用户 | ADR / 模块边界 |

---

## 1. 工程与模块骨架（后端）

| 任务 ID | 描述 | 主要交付物 | 依赖 |
|---------|------|------------|------|
| BE-01 | 新建 Maven 模块 `wmt-module-fin-customer`（C 端 API）与 `wmt-module-fin-core`（领域/核算编排），或单模块 `wmt-module-fin-biz` 分包 | `pom.xml`、`package` 结构、`wmt-server` 依赖 | 无 |
| BE-02 | 定义 URL 前缀：`/app-api/fin/**`（MEMBER）、`/admin-api/fin/**`（ADMIN），与现有 `openapi` 路径**合并或迁移**决策 | `SecurityConfiguration` 扩展说明 + PR 说明 | BE-01 |
| BE-03 | Swagger Tag：`app-fin`、`admin-fin` | `application.yaml` / OpenAPI 分组 | BE-01 |
| BE-04 | 全局错误码段：`FIN_*` 与前端 Toast 映射表 | `ErrorCodeConstants` 类 + 文档 | BE-01 |

---

## 2. 身份、会话与短信（3.1）

| 任务 ID | 描述 | 主要交付物 | 依赖 |
|---------|------|------------|------|
| BE-10 | 短信发送统一服务：`send(scene, mobile)`，60s Redis 限流、模板 ID 配置化 | `FinSmsService` + `SmsSceneEnum` | BE-01, CL-02 |
| BE-11 | 登录：手机号+密码+验证码；失败次数与锁定（若需求未写，先占位配置） | `AppFinAuthController` + `*ReqVO` / `*RespVO` + Service | BE-10 |
| BE-12 | 注册：校验手机号唯一、协议勾选、入库用户；与 CL-01 一致的后置跳转逻辑 | `register` API + 集成测试 | BE-11, CL-01 |
| BE-13 | 忘记密码：验证码 + 新密码 | `resetPassword` API | BE-10, BE-11 |
| BE-14 | 微信 `code` 登录/绑定（若采用）：对接 WxJava，仅服务端换 `code`，日志脱敏 | `POST /app-api/auth/wechat/login` 实现与 openapi 对齐 | BE-01, CL-05 |

---

## 3. 实名与腾讯核验（3.2.1）

| 任务 ID | 描述 | 主要交付物 | 依赖 |
|---------|------|------------|------|
| BE-20 | 用户实名等级枚举：`L1`/`L2`；状态字段与表结构 | DO + Mapper + 迁移 SQL | BE-01 |
| BE-21 | L2：身份证 OCR + 联网核查结果落库（请求/响应摘要或第三方流水号） | `RealNameService` + 外部适配接口 `IdentityGateway` | BE-20 |
| BE-22 | 腾讯人脸/OCR SDK 封装：`TencentIdentityClient`（可 Mock） | `infra` 或 `fin-core` 下 `integration/tencent` | BE-21 |
| BE-23 | 个人信息查询/更新 API；未认证时脱敏展示规则 | `AppFinProfileController` + VO 无敏感字段泄露 | BE-20 |

---

## 4. 我的企业（3.2.4）

| 任务 ID | 描述 | 主要交付物 | 依赖 |
|---------|------|------------|------|
| BE-30 | 表：`fin_company`、`fin_user_company`（含 `default_flag`、绑定时间） | `schema.sql` 增量 + DO + Mapper | BE-01 |
| BE-31 | 企业列表、切换默认企业、排序规则 | `GET /companies`、`POST /companies/{id}/default` | BE-30 |
| BE-32 | 添加企业：名称带出统一社会信用代码/地址 — `EnterpriseInfoGateway` | 工商查询适配器 + DTO | BE-30 |
| BE-33 | 三要素、法人二要素校验编排；重复统一社会信用代码校验 | `CompanyBindService` + 单元测试 | BE-32, BE-10 |
| BE-34 | 绑企流程状态机：表单提交 → 法人短信 → 法人脸 → 成功；防跳过（`bindToken`） | `BindSession` Redis + Service | BE-33, BE-22 |

---

## 5. 首页与产品（3.2.6 / 3.2.7）

| 任务 ID | 描述 | 主要交付物 | 依赖 |
|---------|------|------------|------|
| BE-40 | Banner 配置 CRUD（管理端）+ C 端只读列表（排序、启用） | admin API + app API + DO | BE-01 |
| BE-41 | 产品主数据：列表、搜索、详情；字段与需求一致（额度、利率展示） | `AppFinProductController` + `FinProductDO` | BE-01 |
| BE-42 | 产品「立即申请」前置条件接口：`GET /fin/application/preflight` 返回缺项（未登录/未实名/未绑企） | Service + VO | BE-41, BE-20, BE-30 |

---

## 6. 融资申请与授权（3.2.7）

| 任务 ID | 描述 | 主要交付物 | 依赖 |
|---------|------|------------|------|
| BE-50 | 申请表 `fin_loan_application`：状态、金额、期限、还款方式、产品 ID、企业 ID、授权书版本 | DDL + DO + Mapper | BE-30, BE-41 |
| BE-51 | 创建草稿 / 更新 / 提交；金额万元整数倍、期限枚举与默认值 | `ApplicationService` + 校验器 | BE-50 |
| BE-52 | 授权书：模板变量渲染、阅读记录、勾选时间；未勾选禁止下一步（后端二次校验） | `AuthorizationRecordDO` + API | BE-50 |
| BE-53 | 重复进件规则实现（依赖 CL-04） | `DuplicateApplicationGuard` | BE-51, CL-04 |
| BE-54 | 与 openapi：`/app-api/loan/applications` 对齐或迁移路径 + 生成客户端 | `openapi.yaml` 更新 | BE-51 |

---

## 7. 额度、合同、提款（3.2.2）

| 任务 ID | 描述 | 主要交付物 | 依赖 |
|---------|------|------------|------|
| BE-60 | 额度只读：`fin_credit_limit` 或视图同步表；列表/详情/可用余额 | `AppFinLimitController` | BE-50 或信贷同步 |
| BE-61 | 合同状态：`PENDING_SIGN` / `SIGNED`；与按钮展示规则一致 | 枚举 + Service | BE-60 |
| BE-62 | 合同签署编排：人脸 → 短信 → 调签章（策略 `ContractSignStrategy`） | `ContractSignOrchestrator` + 集成 Mock | BE-22, BE-10 |
| BE-63 | 提款编排：短信 → 人脸 → 提交核算（策略 `DrawdownStrategy`） | `DrawdownOrchestrator` | BE-22, BE-10 |
| BE-64 | 影像合同查看：签名 URL、过期、审计 | `GET /contracts/{id}/pdf-url` | BE-62, `infra` OSS |

---

## 8. 借据、还款、试算（3.2.3）

| 任务 ID | 描述 | 主要交付物 | 依赖 |
|---------|------|------------|------|
| BE-70 | 借据表 `fin_loan_note` 或与核算共用视图；Tab 筛选 | Mapper `selectPage` + API | BE-01 |
| BE-71 | 还款计划：列表 + 按期展开本金/利息（若一条 SQL 复杂则分两次查询） | `RepaymentPlanService` | BE-70 |
| BE-72 | 还款申请 + 双验复用编排组件 | `RepaymentService` | BE-63 复用 |
| BE-73 | 还款试算：调用核算适配器 `AccountingClient.trialRepay` | 接口 + Mock + 超时处理 | BE-70 |

---

## 9. 申请进度（3.2.5）

| 任务 ID | 描述 | 主要交付物 | 依赖 |
|---------|------|------------|------|
| BE-80 | 进度列表/详情字段；与信贷同步任务或 API | `AppFinProgressController` + `CreditSyncJob` 占位 | BE-50 |

---

## 10. 外围集成（适配器层）

| 任务 ID | 描述 | 主要交付物 | 依赖 |
|---------|------|------------|------|
| BE-90 | `EsBClient` 骨架：统一 header、超时、重试、traceId | `integration/esb` 包 + 配置 | BE-01 |
| BE-91 | `CreditSystemClient`：额度、合同、客户 ID、审批结果（接口清单待行方） | 接口 + Mock | BE-90 |
| BE-92 | `CoreBankingClient`：开户校验、转账类（若本系统发起） | 接口 + Mock | BE-90 |
| BE-93 | `EsignClient`：发起签章、查询状态 | 接口 + Mock | BE-90 |
| BE-94 | `RegulatoryReportClient` / ODS 推送：异步任务 + 失败重试表 | Job + 表 | BE-90 |

---

## 11. 管理端（数字化管理 Web，独立仓时对齐契约）

| 任务 ID | 描述 | 主要交付物 | 依赖 |
|---------|------|------------|------|
| FE-ADM-01 | 进件列表页：分页、状态筛、跳转详情 | Vue 页 + API `admin-api` | BE-54 |
| FE-ADM-02 | 进件详情：展示授权书、材料、内部备注 | 页面 + API | FE-ADM-01 |
| FE-ADM-03 | 产品/Banner 配置页 | CRUD + 组件库表单 | BE-40, BE-41 |
| FE-ADM-04 | 合同启用、额度启用等（若需求在管理端） | 占位 + 调 `fin` 或信贷 API | BE-61 |

---

## 12. 小程序端（独立工程）

| 任务 ID | 描述 | 主要交付物 | 依赖 |
|---------|------|------------|------|
| FE-MP-01 | API 封装：`CommonResult` 解析、Token 注入、错误码 → Toast | `request.ts` + 拦截器 | openapi |
| FE-MP-02 | 登录/注册/忘记密码页面对接 BE-11～13 | 页面 + 表单校验对齐 CL-02 | FE-MP-01 |
| FE-MP-03 | 实名、绑企、首页、产品、融资申请流程页 | 页面 + 状态机对齐 BE-42, BE-51 | FE-MP-01 |
| FE-MP-04 | 额度/合同/提款/借据/还款/进度页 | 页面 | FE-MP-01 |

---

## 13. 测试与质量

| 任务 ID | 描述 | 主要交付物 | 依赖 |
|---------|------|------------|------|
| QA-01 | 契约测试：`openapi` 与 Controller 响应结构（可 Spring MVC 测试） | 测试类 | BE-54 |
| QA-02 | 核心规则单测：重复进件、万元整数倍、绑企重复统一社会信用代码 | JUnit | BE-53, BE-51 |
| QA-03 | 集成测试：Testcontainers + Redis Mock 短信 | `@SpringBootTest` 或切片测试 | BE-10 |
| QA-04 | E2E（可选）：登录 → 绑企 → 提交申请（桩信贷） | Playwright / 小程序自动化 | M3 后 |

---

## 14. 建议开发顺序（依赖简图）

```
CL-* → BE-01～04 → BE-10 → BE-11～14
              → BE-20～23
              → BE-30～34 → BE-40～42 → BE-50～54
              → BE-60～64 → BE-70～73 → BE-80
              → BE-90～94（可与 BE-60+ 并行，Mock 先行）
FE-MP-* / FE-ADM-* 与 BE-* 契约对齐后并行
```

**Sprint 建议**  
- **S1**：BE-01～04, BE-10～14, BE-20～23  
- **S2**：BE-30～34, BE-40～42, BE-50～54  
- **S3**：BE-60～64, BE-70～73, BE-80  
- **S4**：BE-90～94 替换 Mock + QA 全套  

---

## 15. 文档与追溯

| 任务 ID | 描述 |
|---------|------|
| DOC-01 | 每完成一个 BE-*，更新 `openapi.yaml` + `schema.sql` |
| DOC-02 | ADR：用户体系选型（CL-05）、路径迁移（BE-02） |

---

*本清单可直接复制到 Jira/禅道；任务 ID 可按团队规范重命名。*
