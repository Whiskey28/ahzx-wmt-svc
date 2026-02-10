# 信用报送数据汇总计算解决方案

## 一、需求分析

### 1.1 业务背景
- 已实现积木报表在线填报功能，各角色按周期填报数据
- 需要将分散的填报数据按不同维度进行汇总计算
- 生成统一的数据报表，支持多颗粒度查询和分析

### 1.2 核心需求
1. **多维度汇总**：支持按周期、角色、报表类型、分类、项目等维度汇总
2. **灵活计算**：支持SUM、AVG、MAX、MIN、COUNT等聚合函数
3. **实时/定时汇总**：支持实时计算和定时批量汇总
4. **历史快照**：保留历史汇总结果，支持趋势分析
5. **查询接口**：提供灵活的汇总数据查询接口

## 二、技术方案设计

### 2.1 数据库设计

#### 2.1.1 汇总配置表（report_summary_config）
**作用**：定义汇总规则，配置哪些字段需要汇总、如何汇总、按什么维度汇总

**核心字段**：
- `config_code`：配置编码（唯一标识）
- `source_table`：源表名（如 `report_fill_biz_stat_finance`）
- `source_field`：源字段名（如 `asset_amount`）
- `target_field`：目标字段名（汇总结果表中的字段）
- `aggregate_type`：聚合类型（SUM/AVG/MAX/MIN/COUNT）
- `dimension_fields`：汇总维度字段（JSON数组，如 `["period_id", "role_id"]`）
- `filter_conditions`：过滤条件（JSON对象，如 `{"deleted": 0}`）

**设计要点**：
- 支持一个配置对应一个字段的汇总规则
- 通过配置表实现灵活的汇总规则管理
- 支持启用/禁用配置

#### 2.1.2 汇总结果表（report_summary_result）
**作用**：存储多维度汇总结果

**核心字段**：
- **维度字段**：
  - `period_id`：周期维度（如 2025-01）
  - `role_id`：角色维度
  - `report_id`：报表类型维度
  - `category_id`：报表分类维度
  - `project_id`：项目维度
  - `dimension_key`：维度组合键（用于快速查询，如 `period_id:role_id:report_id`）

- **汇总数值字段**（根据业务需求定义）：
  - 企业基础信息汇总：`enterprise_count`、`total_reg_cap`、`total_paid_in_cap`
  - 财务数据汇总：`total_asset_amount`、`total_liability_amount`、`total_year_income`、`total_credit_income`、`total_net_profit`、`avg_asset_amount`、`avg_year_income`
  - 人员数据汇总：`total_employee`、`total_credit_employee`、`avg_employee`
  - 信息采集汇总：`total_collect_enterprise`、`total_collect_borrow`、`total_collect_trade`、`total_collect_finance`
  - 产品服务汇总：`total_product_count`、`total_service_count`
  - 投诉安全汇总：`total_complaint_count`、`total_security_incident`

- **元数据字段**：
  - `summary_code`：汇总配置编码
  - `record_count`：参与汇总的记录数
  - `summary_time`：汇总时间
  - `summary_version`：汇总版本（用于增量更新）
  - `extra_data`：扩展数据（JSON格式，存储其他动态汇总数据）

**设计要点**：
- 使用唯一索引 `(summary_code, dimension_key, deleted)` 防止重复汇总
- 支持多维度组合查询
- 保留汇总时间和版本，支持增量更新

#### 2.1.3 汇总任务表（report_summary_task）
**作用**：记录汇总任务执行历史，用于监控和审计

**核心字段**：
- `task_name`：任务名称
- `task_type`：任务类型（FULL全量/INCREMENTAL增量/MANUAL手动）
- `summary_code`：汇总配置编码（为空表示全部配置）
- `period_id`：汇总周期（为空表示全部周期）
- `status`：任务状态（PENDING/RUNNING/SUCCESS/FAILED）
- `start_time`、`end_time`：执行时间
- `duration_seconds`：执行时长（秒）
- `record_count`：处理记录数
- `result_count`：生成结果数
- `error_message`：错误信息

### 2.2 汇总维度设计

#### 2.2.1 单维度汇总
- **按周期汇总**：`period_id`（如 2025-01、2025-Q1、2025）
- **按角色汇总**：`role_id`
- **按报表类型汇总**：`report_id`
- **按分类汇总**：`category_id`
- **按项目汇总**：`project_id`

#### 2.2.2 多维度组合汇总
- **周期+角色**：`period_id + role_id`
- **周期+报表类型**：`period_id + report_id`
- **周期+角色+报表类型**：`period_id + role_id + report_id`
- **全维度汇总**：`period_id + role_id + report_id + category_id + project_id`

#### 2.2.3 维度键生成规则
- 格式：`维度值1:维度值2:维度值3`
- 示例：`2025-01:role001:report001`
- 空值处理：使用 `ALL` 表示该维度不参与分组

### 2.3 汇总计算逻辑

#### 2.3.1 数据源表分类
1. **1:1关联表**（通过parent_id关联）：
   - `report_fill_enterprise_basic`：企业基础信息
   - `report_fill_biz_stat_finance`：财务数据
   - `report_fill_biz_stat_hr`：人员数据
   - `report_fill_biz_stat_credit_build`：信用体系建设数据
   - `report_fill_complaint_security_stat`：投诉安全数据
   - `report_fill_info_collect_stat`：信息采集数据

2. **1:N明细表**（通过record_id关联）：
   - `report_fill_product_stat`：产品服务统计
   - `report_fill_service_by_industry`：按行业分类服务
   - `report_fill_info_source_by_industry`：按行业分类信息来源
   - `report_fill_info_user_org_item`：信息使用者机构明细
   - `report_fill_info_user_gov_item`：信息使用者政府明细

#### 2.3.2 汇总计算流程
```
1. 读取汇总配置（report_summary_config）
   ↓
2. 根据配置的source_table和source_field，查询源数据
   ↓
3. 应用过滤条件（filter_conditions）
   ↓
4. 按维度字段分组（dimension_fields）
   ↓
5. 应用聚合函数（aggregate_type：SUM/AVG/MAX/MIN/COUNT）
   ↓
6. 生成维度键（dimension_key）
   ↓
7. 写入汇总结果表（report_summary_result）
   ↓
8. 记录任务执行历史（report_summary_task）
```

#### 2.3.3 聚合函数说明
- **SUM**：求和（适用于数值字段，如资产总额、收入总额）
- **AVG**：平均值（适用于数值字段，如平均资产、平均收入）
- **MAX**：最大值（适用于数值字段，如最大资产）
- **MIN**：最小值（适用于数值字段，如最小资产）
- **COUNT**：计数（适用于记录数统计，如企业数量）

### 2.4 服务层设计

#### 2.4.1 ReportSummaryConfigService（汇总配置服务）
**功能**：
- 配置管理：增删改查汇总配置
- 配置启用/禁用
- 配置验证

**接口设计**：
```java
// 创建汇总配置
Long createSummaryConfig(SummaryConfigCreateReqVO reqVO);

// 更新汇总配置
Boolean updateSummaryConfig(SummaryConfigUpdateReqVO reqVO);

// 删除汇总配置
Boolean deleteSummaryConfig(Long id);

// 查询汇总配置列表
List<SummaryConfigRespVO> getSummaryConfigList();

// 启用/禁用配置
Boolean enableSummaryConfig(Long id, Boolean enabled);
```

#### 2.4.2 ReportSummaryService（汇总计算服务）
**功能**：
- 执行汇总计算（实时/定时）
- 查询汇总结果
- 汇总任务管理

**接口设计**：
```java
// 执行汇总计算（全量）
SummaryTaskRespVO executeFullSummary(SummaryExecuteReqVO reqVO);

// 执行汇总计算（增量）
SummaryTaskRespVO executeIncrementalSummary(SummaryExecuteReqVO reqVO);

// 查询汇总结果（分页）
PageResult<SummaryResultRespVO> getSummaryResultPage(SummaryResultPageReqVO reqVO);

// 查询汇总结果（按维度）
List<SummaryResultRespVO> getSummaryResultByDimension(SummaryDimensionReqVO reqVO);

// 查询汇总任务列表
PageResult<SummaryTaskRespVO> getSummaryTaskPage(SummaryTaskPageReqVO reqVO);
```

#### 2.4.3 汇总计算实现要点
1. **事务处理**：汇总计算使用事务，确保数据一致性
2. **批量处理**：大数据量时采用分批处理，避免内存溢出
3. **并发控制**：同一配置的汇总任务不能并发执行
4. **错误处理**：汇总失败时记录错误信息，支持重试
5. **性能优化**：
   - 使用数据库聚合函数（SUM、AVG等）而非Java计算
   - 合理使用索引
   - 汇总结果缓存（可选）

### 2.5 控制器层设计

#### 2.5.1 ReportSummaryConfigController（汇总配置管理）
**接口**：
- `POST /admin-api/credit-report/summary-config/create`：创建配置
- `PUT /admin-api/credit-report/summary-config/update`：更新配置
- `DELETE /admin-api/credit-report/summary-config/delete`：删除配置
- `GET /admin-api/credit-report/summary-config/list`：查询配置列表
- `PUT /admin-api/credit-report/summary-config/enable`：启用/禁用配置

#### 2.5.2 ReportSummaryController（汇总计算和查询）
**接口**：
- `POST /admin-api/credit-report/summary/execute-full`：执行全量汇总
- `POST /admin-api/credit-report/summary/execute-incremental`：执行增量汇总
- `GET /admin-api/credit-report/summary/result/page`：分页查询汇总结果
- `GET /admin-api/credit-report/summary/result/dimension`：按维度查询汇总结果
- `GET /admin-api/credit-report/summary/task/page`：查询汇总任务列表

### 2.6 定时任务设计（可选）

#### 2.6.1 定时汇总任务
- **任务名称**：`CreditReportSummaryJob`
- **执行频率**：每日凌晨2点执行
- **执行逻辑**：
  1. 查询所有启用的汇总配置
  2. 按配置执行增量汇总（汇总前一天的数据）
  3. 记录任务执行历史
  4. 发送执行结果通知（可选）

#### 2.6.2 全量汇总任务
- **任务名称**：`CreditReportFullSummaryJob`
- **执行频率**：每月1号凌晨3点执行
- **执行逻辑**：
  1. 执行全量汇总（汇总所有历史数据）
  2. 生成月度汇总报表
  3. 清理过期汇总结果（可选）

## 三、实施步骤

### 3.1 第一阶段：基础框架搭建
1. 创建数据库表（report_summary_config、report_summary_result、report_summary_task）
2. 创建DO类、Mapper接口
3. 创建Service接口和实现类（基础CRUD）
4. 创建Controller接口（基础接口）

### 3.2 第二阶段：汇总计算核心功能
1. 实现汇总配置管理功能
2. 实现汇总计算核心逻辑
3. 实现汇总结果查询功能
4. 单元测试和集成测试

### 3.3 第三阶段：高级功能
1. 实现定时汇总任务
2. 实现汇总结果缓存（可选）
3. 实现汇总报表导出（可选）
4. 性能优化和监控

### 3.4 第四阶段：前端集成
1. 汇总配置管理页面
2. 汇总结果查询页面
3. 汇总报表展示页面
4. 汇总任务监控页面

## 四、技术要点

### 4.1 数据一致性
- 汇总计算使用数据库事务
- 汇总结果表使用唯一索引防止重复
- 支持增量更新，避免重复计算

### 4.2 性能优化
- 使用数据库聚合函数，减少Java计算
- 合理设计索引，提高查询性能
- 大数据量时采用分批处理
- 汇总结果可考虑缓存（Redis）

### 4.3 扩展性
- 汇总配置表支持动态配置，无需修改代码
- 汇总结果表预留扩展字段（extra_data）
- 支持自定义聚合函数（通过配置扩展）

### 4.4 可维护性
- 汇总规则通过配置表管理，便于调整
- 汇总任务记录执行历史，便于排查问题
- 提供汇总结果查询接口，便于数据分析

## 五、示例场景

### 5.1 场景1：按周期汇总财务数据
**需求**：统计2025年1月所有角色的资产总额、收入总额

**配置**：
- `source_table`: `report_fill_biz_stat_finance`
- `source_field`: `asset_amount`
- `target_field`: `total_asset_amount`
- `aggregate_type`: `SUM`
- `dimension_fields`: `["period_id"]`
- `filter_conditions`: `{"period_id": "2025-01", "deleted": 0}`

**结果**：
- `period_id`: `2025-01`
- `total_asset_amount`: `1000000000.00`
- `record_count`: `50`

### 5.2 场景2：按角色+周期汇总企业数量
**需求**：统计每个角色在每个周期的企业数量

**配置**：
- `source_table`: `report_fill_enterprise_basic`
- `source_field`: `id`
- `target_field`: `enterprise_count`
- `aggregate_type`: `COUNT`
- `dimension_fields`: `["period_id", "role_id"]`
- `filter_conditions`: `{"deleted": 0}`

**结果**：
- `period_id`: `2025-01`, `role_id`: `role001`, `enterprise_count`: `10`
- `period_id`: `2025-01`, `role_id`: `role002`, `enterprise_count`: `15`
- ...

### 5.3 场景3：按行业汇总产品服务次数
**需求**：统计每个行业的产品服务总次数

**配置**：
- `source_table`: `report_fill_service_by_industry`
- `source_field`: `year_service_count`
- `target_field`: `total_service_count`
- `aggregate_type`: `SUM`
- `dimension_fields`: `["industry_code"]`
- `filter_conditions`: `{"deleted": 0}`

**结果**：
- `industry_code`: `bank`, `total_service_count`: `1000`
- `industry_code`: `security`, `total_service_count`: `800`
- ...

## 六、注意事项

1. **数据准确性**：确保汇总计算逻辑正确，建议先在小数据集上验证
2. **性能考虑**：大数据量汇总可能耗时较长，建议异步执行或定时执行
3. **历史数据**：汇总结果表保留历史数据，支持趋势分析
4. **配置管理**：汇总配置变更后，需要重新执行汇总计算
5. **错误处理**：汇总失败时记录详细错误信息，便于排查问题

## 七、后续优化方向

1. **实时汇总**：支持数据变更时实时更新汇总结果
2. **汇总报表**：基于汇总结果生成可视化报表
3. **数据导出**：支持汇总结果导出Excel/PDF
4. **权限控制**：不同角色查看不同维度的汇总数据
5. **数据校验**：汇总结果与原始数据的一致性校验
