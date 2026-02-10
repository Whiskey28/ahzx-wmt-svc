# 征信报送模块（credit-report）测试规划

## 1. 测试目标

- 验证 **各部门填报角色** 在积木表单侧完成填报后，管理端可正确查询填报锚点记录，并能生成正确的积木编辑/查看 URL。
- 验证 **信息使用者机构基础库** 的 CRUD、排序、按行业统计以及下拉树数据集输出正确。
- 验证 **积木报表数据集接口** 的口径、聚合范围（按周期/报表/角色过滤）、异常分支与边界条件。
- 验证固定业务规则：**汇总后 + 3000 × 月数** 的计算结果正确。

## 2. 测试范围

### 2.1 后端接口范围（本模块）

- 报表填报管理：`/credit/report-fill/**`
- 信息使用者机构：`/credit/report-info-user/**`
- 积木数据集接口：`/credit/jmreport/data/**`

### 2.2 角色覆盖范围

填报人角色（`system_role.id`）：

- 200 综合管理部填报人
- 201 财务部填报人
- 202 党委工作部填报人
- 203 创新研发中心填报人
- 204 市场部填报人
- 205 信息技术中心填报人
- 206 企业信用部填报人
- 207 普惠信用部填报人
- 208 数据管理中心填报人

> 重点口径涉及角色筛选/聚合的接口，至少覆盖 203/206/207/208，并补充全角色的“通用能力冒烟”。

### 2.3 非范围（明确不测或不在本模块实现）

- 审批/提交/驳回流程（业务已确认：**无流程**）
- `projectId` 维度统计与隔离（业务已确认：**暂不涉及**）
- 积木表单组件本身渲染/校验（属于积木产品能力，本文只关注接口与数据）

## 3. 测试环境与数据准备

### 3.1 periodId 约束

- 仅支持 `YYYY-MM`，测试周期建议使用：`2026-01`、`2026-02`。

### 3.2 角色与账号

可使用初始化 SQL `sql/postgresql/credit-report-role-insert.sql` 导入：

- 角色 200-208
- 示例用户 1001-1004 及其 `system_user_role` 绑定

### 3.3 字典数据（关键依赖）

下列字典在数据集口径中是强依赖（缺失将返回 NOT_FOUND）：

- `industry_code`（产品与服务提供情况行业字典）
- `info_source_status`（信息来源情况字典）
- `credit_product_type`（征信产品类别字典）
- `credit_pro`（提供的信用产品字典）

> 测试前需确认数据库中对应 dictId 已配置并“启用”；否则应覆盖“缺失字典时报错”的异常用例。

### 3.4 信息使用者机构基础数据

准备数据建议：

- 非政府（org_item）每个行业至少 2 条，包含：
  - 相同 `orgName` 不同记录（用于验证“去重计数”）
  - `isCurrentService=1/0` 混合
  - `sortNo` 乱序（用于验证下拉树排序）
- 政府（gov_item）至少 3 条，包含同名去重与 `isCurrentService` 混合

### 3.5 填报锚点记录与明细表数据

准备至少两个月份数据（`2026-01`、`2026-02`），并覆盖：

- 同一 `periodId + reportId` 下多角色（203/206/207/208）各自一条 record
- 同一 `periodId + reportId + roleId` 下多条 record（用于验证“取最新”逻辑）

## 4. 测试策略与分层

- **接口层（API）**：以 Postman/Apifox 或自动化测试直接调用接口，验证输入输出与异常。
- **口径层（统计校验）**：对聚合结果做可追溯校验（从明细表推导期望值）。
- **回归层（冒烟）**：覆盖最常用链路：分类/模板 → 记录分页 → edit-url → 数据集拉取。

## 5. 测试用例清单（高优先级）

### 5.1 报表填报管理（/credit/report-fill）

#### A. 分类与模板

- **A1 分类列表**：`GET /credit/report-fill/category/list` 返回非空（或符合环境初始化数据），字段完整。
- **A2 模板列表默认 submitForm**：不传 `submitForm` 时，默认只查 `submitForm=1` 的在线填报表单。
- **A3 模板列表 submitForm=0**：传 `submitForm=0` 时能查到数据报表模板（若环境有）。
- **A4 模板列表非法分类**：`categoryId` 不存在时返回空列表（或符合实现行为）。

#### B. 填报记录分页

- **B1 基础分页**：`GET /credit/report-fill/record/page` 按 `periodId` 查询返回预期数量。
- **B2 角色筛选**：传 `roleId=208` 仅返回数据管理中心记录。
- **B3 分类筛选**：传 `categoryId` 只返回该分类下报表的记录（验证分类→报表 id 过滤链路）。
- **B4 模板名称模糊查询**：`reportName` like 查询能命中。
- **B5 组合筛选交集为空**：分类 + 名称 + reportId 交集为空时返回空页（实现中应直接 empty）。

#### C. 编辑 URL 获取

- **C1 正常返回**：`GET /credit/report-fill/record/{id}/edit-url` 返回：
  - `editUrl=/jmreport/view/{reportId}/edit/{recordId}`
  - `viewUrl=/jmreport/view/{reportId}`
- **C2 记录不存在**：recordId 不存在返回 NOT_FOUND。
- **C3 record 未关联 reportId**：构造脏数据时应返回 BAD_REQUEST（无法生成编辑 URL）。

#### D. 删除记录

- **D1 正常删除**：`POST /credit/report-fill/record/delete/{id}` 返回 true，且再次查询不到。
- **D2 删除不存在**：返回 NOT_FOUND。

### 5.2 信息使用者机构（/credit/report-info-user）

#### E. 非政府机构明细 CRUD

- **E1 分页查询**：`GET /org/page` 支持按条件筛选（若有）且排序符合“行业字典顺序 + sortNo”（实现为 JOIN 排序）。
- **E2 新增**：`POST /org/create` 返回 id，分页能查到。
- **E3 更新不存在**：更新 id 不存在返回 NOT_FOUND。
- **E4 删除单条**：删除后不可查。
- **E5 批量删除 ids 为空**：返回 true（实现中直接 true）。

#### F. 政府机构明细 CRUD

- **F1 分页查询 sortNo 升序**：验证排序符合实现。
- **F2 新增/更新/删除/批量删除**：同 E 组用例覆盖。

#### G. 按行业统计（全表）

- **G1 行业顺序固定**：返回 items 顺序与 `industry_code` 字典一致。
- **G2 去重计数**：相同机构名称仅计 1。
- **G3 current 计数**：`isCurrentService=1` 去重计数正确。
- **G4 政府行业口径**：`industryCode=government` 仅统计 gov_item。

### 5.3 积木数据集接口（/credit/jmreport/data）

#### H. 信息来源情况（按行业）`/info-source/status`

- **H1 字典缺失**：移除/禁用字典时返回 NOT_FOUND。
- **H2 指定 recordId 正常**：当 recordId 的 periodId/reportId/roleId=208 都匹配时返回 items。
- **H3 recordId 不存在**：返回 NOT_FOUND。
- **H4 recordId 周期不匹配**：返回 BAD_REQUEST。
- **H5 recordId 模板不匹配**：返回 BAD_REQUEST。
- **H6 recordId 角色不为 208**：返回 BAD_REQUEST（只统计数据管理中心）。
- **H7 未指定 recordId 取最新**：
  - 同 period+report+role=208 存在多条 record，取 create_time 最新一条。

#### I. 产品与服务提供情况（按行业）`/service-by-industry`

- **I1 行业字典缺失**：返回 NOT_FOUND。
- **I2 机构数来自全表**：不随 periodId 变化（当前实现为全表统计），验证与基础库一致。
- **I3 当年提供次数聚合范围**：只聚合指定报表模板集合 + 指定角色集合（验证排除非指定角色/模板的 record）。
- **I4 government 行在最后**：返回 items 中 government 行应被放到末尾（实现有专门逻辑）。

#### J. 提供的征信产品(服务)数据集 `/-/product-service`

- **J1 字典缺失**：产品类别或产品名称字典缺失返回 NOT_FOUND。
- **J2 固定字段聚合**：三列汇总正确（reportYearTotal/creditYearTotal/antiYearTotal）。
- **J3 otherServiceCount 计算**：`totalYearServiceCount - totalProductCount` 正确。

#### K. 三项合计（固定规则）`/product-service/total`

- **K1 periodId 为空**：返回 BAD_REQUEST。
- **K2 月份解析**：`2026-01` 月份=1；`2026-12` 月份=12。
- **K3 固定规则校验**：期望值：
  - `reportYearTotal = sum(report_year_count) + 3000 × month`
  - `creditYearTotal = sum(credit_year_count) + 3000 × month`
  - `antiYearTotal = sum(anti_year_count) + 3000 × month`

#### L. 信息使用者下拉树（特殊结构）`/info-user/tree`

- **L1 直接 VO 返回**：响应体顶层应为 VO 字段（不含 `code/msg/data`）。
- **L2 根节点固定**：存在“产品与服务提供情况”根节点。
- **L3 二级行业节点齐全**：与行业字典一致。
- **L4 三级机构节点排序**：按 `sortNo` 字典序升序（缺失 sortNo 的放后）。
- **L5 government 行业使用 gov_item**：节点名称来自 `govOrgName`。

## 6. 自动化建议

### 6.1 接口自动化（建议）

- 用例分组：
  - `report-fill` 冒烟
  - `report-info-user` CRUD
  - `jmreport-data` 口径与异常
- 断言重点：
  - 响应码与错误码（NOT_FOUND/BAD_REQUEST）
  - items 顺序、去重计数、聚合和总计字段

### 6.2 口径校验表（建议输出物）

建议在测试数据准备阶段建立一份“期望值计算表”（Excel/Markdown），列出：

- 输入：periodId、reportId、roleId、recordId
- 明细表原始数据
- 期望输出：items 明细、总计字段、特殊规则（+3000×month）最终值

## 7. 回归清单（上线前）

- 分类列表、模板列表正常
- record 分页查询 + edit-url 能跳转
- 信息使用者基础库：新增/更新/删除/统计/下拉树均可用
- 关键数据集接口可被积木报表 HTTP 数据集稳定拉取（适配器正常工作）

