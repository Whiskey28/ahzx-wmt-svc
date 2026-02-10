# 征信报送模块（credit-report）操作手册

## 1. 文档目的与适用范围

本手册用于指导各角色在系统中完成 **征信报送（月度）** 相关的：

- **积木表单填报/编辑**（由积木报表 JimuReport 承载表单页面与保存）
- **填报记录查询**（管理后台接口）
- **信息使用者机构基础数据维护**（管理后台接口）
- **报表展示取数**（积木报表通过后端数据集接口拉取聚合数据）

说明（已确认的业务约束）：

- **无审批/审核流程**：填报完成后表单数据保存在积木侧（后端提供数据集接口用于报表统计/展示）。
- **填报周期 `periodId` 仅支持 `YYYY-MM`**（例：`2026-01`）。
- **`projectId` 暂不涉及**（不参与当前业务使用）。
- **存在固定口径规则**：部分统计存在“汇总后 + 3000 × 月数”的业务规则（详见第 5 章）。

## 2. 角色与账号准备

### 2.1 填报人角色（system_role.id）

征信报表填报角色（`sql/postgresql/credit-report-role-insert.sql`）：

- **200**：综合管理部填报人（`credit_report_admin_dept`）
- **201**：财务部填报人（`credit_report_finance_dept`）
- **202**：党委工作部填报人（`credit_report_party_dept`）
- **203**：创新研发中心填报人（`credit_report_rd_center`）
- **204**：市场部填报人（`credit_report_market_dept`）
- **205**：信息技术中心填报人（`credit_report_it_center`）
- **206**：企业信用部填报人（`credit_report_enterprise_credit`）
- **207**：普惠信用部填报人（`credit_report_inclusive_credit`）
- **208**：数据管理中心填报人（`credit_report_data_center`）

> 注意：后端统计口径中会按 `roleId` 精确筛选/聚合（例如“信息来源情况”只统计 208）。

### 2.2 示例用户（可用于联调/演示）

`sql/postgresql/credit-report-role-insert.sql` 中提供了演示用户与角色绑定（可按需导入）：

- 用户 **1001**：`chenjunya`（角色：200 + 206）
- 用户 **1002**：`shenyingying`（角色：203）
- 用户 **1003**：`wangyujia`（角色：208）
- 用户 **1004**：`liyiming`（角色：204）

> 密码为加密串（BCrypt），通常配合项目默认明文密码使用；以你们环境的实际登录策略为准。

## 3. 系统入口与路径约定

### 3.1 后端接口前缀（管理端）

本模块管理端接口主要包含三组（均在 `wmt-module-credit-report`）：

- **报表填报管理**：`/credit/report-fill/**`
- **信息使用者机构管理**：`/credit/report-info-user/**`
- **积木报表专用数据集接口**：`/credit/jmreport/data/**`

积木报表 HTTP 数据集配置说明（见适配器源码注释）：

- URL 形如：`/admin-api/credit/jmreport/data/...`
- 部分数据集需要填写“适配器 Bean 名称”（如 `infoSourceStatusParser`），用于把 `CommonResult` 包装结构转换成积木可识别的扁平数组数据集。

### 3.2 积木报表页面路径（编辑/查看）

系统通过填报记录可生成积木页面 URL（后端拼接）：

- **编辑页**：`/jmreport/view/{reportId}/edit/{recordId}`
- **查看页**：`/jmreport/view/{reportId}`

## 4. 管理后台：报表填报管理（ReportFill）

### 4.1 查询报表分类列表

- **用途**：给前端/页面展示“报表分类”树/列表，用于筛选模板。
- **接口**：`GET /credit/report-fill/category/list`
- **返回**：分类列表（仅 `id/name/parentId` 等基础字段）

操作步骤：

1. 进入“报表填报管理”页面
2. 加载分类列表
3. 选择分类进入模板列表

### 4.2 查询分类下的报表模板列表

- **用途**：列出某分类（`categoryId`）下可填报的模板。
- **接口**：`GET /credit/report-fill/template/list`
- **关键参数**：
  - `categoryId`：分类 id
  - `submitForm`：是否填报表单（默认查询 `submitForm=1` 在线填报表单）

操作步骤：

1. 选择分类
2. 查看模板列表（一般只展示在线填报表单）
3. 点击模板进入积木表单填报/查看

### 4.3 分页查询填报记录

- **用途**：按周期、角色、分类/模板筛选查看历史填报锚点记录。
- **接口**：`GET /credit/report-fill/record/page`
- **常用筛选条件**（以页面字段为准）：
  - `periodId`：如 `2026-01`
  - `roleId`：200-208
  - `categoryId` / `reportId` / `reportName`：模板筛选

操作步骤：

1. 设置周期（必须符合 `YYYY-MM`）
2. 选择角色（通常为当前登录用户所属填报角色）
3. 查询得到记录列表
4. 从记录进入编辑页/查看页（见 4.4）

### 4.4 获取记录的编辑 URL / 查看 URL

- **用途**：从“填报记录”跳转到积木表单编辑页。
- **接口**：`GET /credit/report-fill/record/{id}/edit-url`
- **返回**：`editUrl`、`viewUrl`、`recordId`、`reportId`

操作步骤：

1. 在填报记录列表中点击“编辑”
2. 调接口获取 `editUrl`
3. 跳转到积木页面进行填报/修改并保存

### 4.5 删除填报记录（逻辑删除）

- **用途**：删除某条填报锚点记录（逻辑删除）。
- **接口**：`POST /credit/report-fill/record/delete/{id}`

注意：

- 删除后可能影响统计口径（某些数据集按 record 聚合/取最新）。
- 由于无审批流程，删除通常仅用于清理误填/测试数据。

## 5. 管理后台：信息使用者机构（ReportInfoUser）

该部分维护的是**全局基础信息**（当前实现按“全表数据”统计与下拉树展示，不再按 recordId 区分）。

### 5.1 机构明细（非政府）维护

接口清单：

- `GET /credit/report-info-user/org/page`：分页查询
- `POST /credit/report-info-user/org/create`：新增
- `POST /credit/report-info-user/org/update`：更新
- `POST /credit/report-info-user/org/delete?id=...`：删除
- `POST /credit/report-info-user/org/delete-batch`：批量删除（body 为 id 列表）

字段含义（核心）：

- `orgName`：机构名称（统计时按名称去重）
- `industryCode`：行业字典编码（用于按行业分组）
- `isCurrentService`：是否当前提供服务（0/1）
- `sortNo`：排序号（用于下拉树节点排序）

### 5.2 政府机构明细维护

接口清单：

- `GET /credit/report-info-user/gov/page`
- `POST /credit/report-info-user/gov/create`
- `POST /credit/report-info-user/gov/update`
- `POST /credit/report-info-user/gov/delete?id=...`
- `POST /credit/report-info-user/gov/delete-batch`

字段含义（核心）：

- `govOrgName`：政府机构名称（统计时按名称去重）
- `isCurrentService`：是否当前提供服务（0/1）
- `sortNo`：排序号

### 5.3 按行业统计（全表口径）

- **接口**：`GET /credit/report-info-user/stat/by-industry`
- **口径**：
  - 行业顺序来自字典 `industry_code`
  - 非政府行业：从 `org_item` 按 `industryCode` 分组，按 `orgName` 去重计数
  - 政府行业：从 `gov_item` 按 `govOrgName` 去重计数
  - `isCurrentService=1` 作为“当前提供服务数”

## 6. 积木报表：数据集接口使用说明（JimuReportData）

### 6.1 信息来源情况（按行业）数据集

- **接口**：`GET /credit/jmreport/data/info-source/status`
- **关键参数**：`periodId`、`reportId`、可选 `recordId`
- **口径**：
  - **只统计角色 208（数据管理中心填报人）**
  - 若指定 `recordId`：校验周期、模板、角色匹配；不匹配则报错
  - 未指定 `recordId`：按 `periodId + reportId + roleId=208` 取最新记录

积木数据集适配器：

- Bean：`infoSourceStatusParser`
- 作用：把 `CommonResult.data.items` 转成扁平数组作为数据集行。

### 6.2 产品与服务提供情况（按行业）数据集

- **接口**：`GET /credit/jmreport/data/service-by-industry`
- **关键参数**：`periodId`、`reportId`（可选 `recordId` 在当前实现中用于聚合时一般不使用）
- **口径**：
  - “信息使用者机构数”来自信息使用者基础库（全表）
  - “当年提供产品（服务）次数”来自填报表 `report_fill_service_by_industry`，并按：**周期 + 指定报表模板集合 + 指定角色集合** 聚合

积木数据集适配器：

- Bean：`serviceByIndustryParser`

### 6.3 产品与服务提供情况（按行业）总计

- **接口**：`GET /credit/jmreport/data/service-by-industry/total`
- **用途**：返回上接口中的总计字段，供报表页眉/汇总区展示

积木数据集适配器：

- Bean：`serviceByIndustryTotalParser`

### 6.4 提供的征信产品(服务)数据集

- **接口**：`GET /credit/jmreport/data/product-service`
- **关键参数**：`periodId`、`reportId`、可选 `recordId`
- **口径**：
  - “征信产品（服务）次数”来自 `report_fill_product_stat` 的固定字段（当前实现聚合三个字段：企业信用报告/招投标报告、信用分、反欺诈）
  - “总计次数”来自 `report_fill_service_by_industry` 的聚合总和
  - 计算 “其他征信服务产品次数 = 总计次数 - 已列示产品次数之和”

积木数据集适配器：

- Bean：`productServiceParser`

### 6.5 信息使用者机构下拉树（特殊返回结构）

- **接口**：`GET /credit/jmreport/data/info-user/tree`
- **返回结构**：直接返回 `JmReportInfoUserTreeRespVO`（**不使用 CommonResult 包装**）
- **用途**：给积木报表的“下拉树组件”使用，节点结构：
  - 一级：固定“产品与服务提供情况”
  - 二级：行业字典项
  - 三级：机构名称（按 `sortNo` 排序）

## 7. 常见问题（FAQ）

### 7.1 为什么有的数据集接口返回 CommonResult，有的直接返回 VO？

- 大多数数据集接口用于表格/图表数据，积木侧适配器按 `CommonResult.data.items` 解析并转换为扁平数组。
- 下拉树组件需要特殊结构（`JmReportInfoUserTreeRespVO`），因此接口直接返回 VO，避免被 `CommonResult` 包一层导致积木组件取值路径不兼容。

### 7.2 periodId 填什么？

- **仅支持 `YYYY-MM`**，例如：`2026-01`。
