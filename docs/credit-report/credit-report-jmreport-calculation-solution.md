# 积木报表数据报表字段计算解决方案

## 一、需求分析

### 1.1 业务背景
- 已使用积木报表2.3.0的在线填报功能，数据已成功收集到数据库
- 需要在积木报表中创建**数据报表**，实现多字段计算
- 计算逻辑包括：字段引用、加减乘除、SUM聚合、跨记录汇总等

### 1.2 核心需求（基于图片示例）

#### 需求1：层级计算字段
- **收录企业总户数（A）**：数据部填写（红色）
- **收录境内企业户数**：`=A`（引用A字段）
- **仅收录基本信息的企业户数**：`=A-B`（A减去B）
- **收录基本信息以外信息的企业户数**：`=SUM(B+67604+C+6901+21266+483367+0)*99.4%`
  - 包含多个子项：
    - 收录借贷信息的企业户数（B，红色，数据部填写）
    - 收录商业交易信息的企业户数（67604，固定值）
    - 收录财务信息的企业户数（C，红色，数据部填写）
    - 收录税务信息的企业户数（6901，固定值）
    - 收录进出口信息的企业户数（21266，固定值）
    - 收录水电气等公用事业的企业户数（483367，固定值）

#### 需求2：跨部门/跨角色汇总
- **服务次数总和**：`=sum(数据部+各项目经理)`
  - 数据部填写：`service_times_data_dept`
  - 各项目经理填写：`service_times_pm`（多个记录）
  - 需要汇总多个角色的数据

#### 需求3：总计行计算
- **信息使用者机构总累计数**：`=SUM(本列)`
- **当前使用服务的信息使用者机构总数**：`=SUM(本列)`
- **当年提供产品(服务)总次数**：`=SUM(本列)`

## 二、积木报表数据报表功能说明

### 2.1 数据报表 vs 在线填报
- **在线填报**：用于数据录入，数据保存到数据库表
- **数据报表**：用于数据展示和计算，基于数据库表数据，通过SQL查询和公式计算生成报表

### 2.2 积木报表2.3.0支持的计算方式

#### 2.2.1 单元格公式计算
- 支持Excel风格的公式：`=A1+B1`、`=SUM(A1:A10)`等
- 支持字段引用：`=字段名`、`=表名.字段名`
- 支持函数：SUM、AVG、MAX、MIN、COUNT等

#### 2.2.2 SQL数据源计算
- 在SQL查询中使用聚合函数：`SELECT SUM(amount) FROM table`
- 支持JOIN关联多表数据
- 支持GROUP BY分组汇总

#### 2.2.3 数据集计算字段
- 在数据集配置中添加计算字段
- 使用SQL表达式：`(field1 + field2) AS total`
- 支持子查询和窗口函数

## 三、技术方案设计

### 3.1 方案一：使用积木报表数据报表 + SQL计算（推荐）

#### 3.1.1 设计思路
1. **创建数据报表模板**：在积木报表中创建数据报表，关联填报数据表
2. **SQL数据源**：编写SQL查询，使用聚合函数计算汇总值
3. **单元格公式**：在报表单元格中使用公式引用SQL查询结果
4. **跨记录汇总**：通过SQL的GROUP BY和聚合函数实现

#### 3.1.2 实现步骤

**步骤1：创建数据报表模板**
- 在积木报表管理后台创建新的数据报表
- 选择数据源类型：SQL数据集
- 配置数据源连接（使用系统数据库）

**步骤2：编写SQL查询（示例）**

```sql
-- 示例1：计算收录企业相关数据
SELECT 
    r.id,
    r.period_id,
    r.role_id,
    r.report_id,
    -- 基础数据（来自填报表）
    e.collect_enterprise_total AS A,  -- 收录企业总户数
    i.collect_borrow_total AS B,      -- 收录借贷信息的企业户数
    i.collect_finance_total AS C,     -- 收录财务信息的企业户数
    -- 计算字段
    e.collect_enterprise_total AS domestic_enterprise_count,  -- 收录境内企业户数 = A
    (e.collect_enterprise_total - i.collect_borrow_total) AS basic_info_only_count,  -- 仅收录基本信息 = A-B
    -- 收录基本信息以外信息 = SUM(B+67604+C+6901+21266+483367+0)*99.4%
    (
        COALESCE(i.collect_borrow_total, 0) + 
        67604 + 
        COALESCE(i.collect_finance_total, 0) + 
        6901 + 
        21266 + 
        483367 + 
        0
    ) * 0.994 AS other_info_count,
    -- 其他字段
    e.collect_micro_sme_total AS D,   -- 收录小微企业总户数
    e.collect_individual_business_total AS E,  -- 收录个体工商户总户数
    e.collect_personnel_total AS F   -- 收录自然人股东等人员人数
FROM report_fill_basic_info r
LEFT JOIN report_fill_enterprise_basic e ON e.parent_id = r.id AND e.deleted = 0
LEFT JOIN report_fill_info_collect_stat i ON i.parent_id = r.id AND i.deleted = 0
WHERE r.deleted = 0
  AND r.period_id = '${period_id}'  -- 参数化查询
  AND r.role_id = '${role_id}'      -- 参数化查询
```

```sql
-- 示例2：跨角色汇总服务次数
SELECT 
    'total' AS summary_type,
    SUM(
        COALESCE(
            CASE WHEN r.role_id = 'data_dept' THEN e.service_times_data_dept ELSE 0 END, 
            0
        ) + 
        COALESCE(
            CASE WHEN r.role_id != 'data_dept' THEN e.service_times_pm ELSE 0 END, 
            0
        )
    ) AS total_service_times
FROM report_fill_basic_info r
LEFT JOIN report_fill_service_stat e ON e.parent_id = r.id AND e.deleted = 0
WHERE r.deleted = 0
  AND r.period_id = '${period_id}'
GROUP BY 'total'
```

**步骤3：在报表单元格中配置公式**
- 在报表设计器中，将SQL查询结果的字段拖拽到对应单元格
- 对于需要进一步计算的单元格，使用公式：
  - `=A1`（引用A1单元格）
  - `=A1-B1`（A1减去B1）
  - `=SUM(A1:A10)`（A1到A10求和）

**步骤4：配置参数**
- 在数据报表中配置查询参数：`period_id`、`role_id`等
- 支持从URL参数或表单控件获取参数值

### 3.2 方案二：使用积木报表数据集计算字段

#### 3.2.1 设计思路
在数据集配置中直接添加计算字段，使用SQL表达式计算

#### 3.2.2 实现示例

```sql
SELECT 
    r.id,
    r.period_id,
    -- 基础字段
    e.collect_enterprise_total,
    i.collect_borrow_total,
    i.collect_finance_total,
    -- 计算字段（在SQL中直接计算）
    e.collect_enterprise_total AS domestic_enterprise_count,
    (e.collect_enterprise_total - COALESCE(i.collect_borrow_total, 0)) AS basic_info_only_count,
    (
        COALESCE(i.collect_borrow_total, 0) + 67604 + 
        COALESCE(i.collect_finance_total, 0) + 6901 + 
        21266 + 483367 + 0
    ) * 0.994 AS other_info_count
FROM report_fill_basic_info r
LEFT JOIN report_fill_enterprise_basic e ON e.parent_id = r.id AND e.deleted = 0
LEFT JOIN report_fill_info_collect_stat i ON i.parent_id = r.id AND i.deleted = 0
WHERE r.deleted = 0
```

### 3.3 方案三：混合方案（SQL + 单元格公式）

#### 3.3.1 设计思路
- **复杂计算**：在SQL中完成（如跨表关联、聚合函数）
- **简单计算**：在报表单元格中使用公式（如字段引用、加减）

#### 3.3.2 实现示例

**SQL查询（基础数据 + 部分计算）**：
```sql
SELECT 
    r.id,
    r.period_id,
    r.role_id,
    -- 基础数据
    e.collect_enterprise_total AS A,
    i.collect_borrow_total AS B,
    i.collect_finance_total AS C,
    -- SQL中计算复杂公式
    (
        COALESCE(i.collect_borrow_total, 0) + 67604 + 
        COALESCE(i.collect_finance_total, 0) + 6901 + 
        21266 + 483367 + 0
    ) * 0.994 AS other_info_calculation_base
FROM report_fill_basic_info r
LEFT JOIN report_fill_enterprise_basic e ON e.parent_id = r.id AND e.deleted = 0
LEFT JOIN report_fill_info_collect_stat i ON i.parent_id = r.id AND i.deleted = 0
WHERE r.deleted = 0
```

**报表单元格公式**：
- 单元格A1：`=A`（显示收录企业总户数）
- 单元格A2：`=A1`（收录境内企业户数 = A）
- 单元格A3：`=A1-B1`（仅收录基本信息 = A-B）
- 单元格A4：`=other_info_calculation_base`（直接使用SQL计算结果）

## 四、具体实现指南

### 4.1 场景1：层级计算字段实现

#### 4.1.1 数据准备
确保以下表有数据：
- `report_fill_basic_info`：主记录
- `report_fill_enterprise_basic`：企业基础信息（字段：`collect_enterprise_total`等）
- `report_fill_info_collect_stat`：信息采集统计（字段：`collect_borrow_total`、`collect_finance_total`等）

#### 4.1.2 SQL数据集配置

```sql
SELECT 
    r.id AS record_id,
    r.period_id,
    r.role_id,
    r.report_id,
    -- 红色字段（数据部填写）
    e.collect_enterprise_total AS field_A,
    i.collect_borrow_total AS field_B,
    i.collect_finance_total AS field_C,
    e.collect_micro_sme_total AS field_D,
    e.collect_individual_business_total AS field_E,
    e.collect_personnel_total AS field_F,
    -- 固定值
    67604 AS fixed_value_1,
    6901 AS fixed_value_2,
    21266 AS fixed_value_3,
    483367 AS fixed_value_4,
    -- 计算字段
    e.collect_enterprise_total AS domestic_enterprise_count,  -- =A
    (e.collect_enterprise_total - COALESCE(i.collect_borrow_total, 0)) AS basic_info_only_count,  -- =A-B
    -- 收录基本信息以外信息 = SUM(B+67604+C+6901+21266+483367+0)*99.4%
    (
        COALESCE(i.collect_borrow_total, 0) + 
        67604 + 
        COALESCE(i.collect_finance_total, 0) + 
        6901 + 
        21266 + 
        483367 + 
        0
    ) * 0.994 AS other_info_count
FROM report_fill_basic_info r
LEFT JOIN report_fill_enterprise_basic e ON e.parent_id = r.id AND e.deleted = 0
LEFT JOIN report_fill_info_collect_stat i ON i.parent_id = r.id AND i.deleted = 0
WHERE r.deleted = 0
  AND r.period_id = '${period_id}'
ORDER BY r.create_time DESC
```

#### 4.1.3 报表设计
1. 创建报表模板，使用上述SQL作为数据源
2. 在报表中设计表格布局：
   ```
   | 字段名称                    | 数值/公式           |
   |----------------------------|-------------------|
   | 收录企业总户数              | =field_A          |
   | 收录境内企业户数            | =domestic_enterprise_count |
   | 仅收录基本信息的企业户数    | =basic_info_only_count |
   | 收录基本信息以外信息的企业户数 | =other_info_count |
   |   ├ 收录借贷信息的企业户数  | =field_B          |
   |   ├ 收录商业交易信息的企业户数 | =fixed_value_1   |
   |   ├ 收录财务信息的企业户数  | =field_C          |
   |   └ ...                    | ...              |
   | 收录小微企业总户数          | =field_D          |
   | 收录个体工商户总户数        | =field_E          |
   | 收录自然人股东等人员人数    | =field_F          |
   ```

### 4.2 场景2：跨角色汇总实现

#### 4.2.1 SQL数据集配置

```sql
-- 跨角色汇总服务次数
SELECT 
    r.period_id,
    -- 数据部的服务次数
    SUM(
        CASE 
            WHEN r.role_id = 'data_dept' 
            THEN COALESCE(s.service_times_data_dept, 0) 
            ELSE 0 
        END
    ) AS data_dept_total,
    -- 各项目经理的服务次数
    SUM(
        CASE 
            WHEN r.role_id != 'data_dept' 
            THEN COALESCE(s.service_times_pm, 0) 
            ELSE 0 
        END
    ) AS pm_total,
    -- 总计
    SUM(
        COALESCE(s.service_times_data_dept, 0) + 
        COALESCE(s.service_times_pm, 0)
    ) AS total_service_times
FROM report_fill_basic_info r
LEFT JOIN report_fill_service_stat s ON s.parent_id = r.id AND s.deleted = 0
WHERE r.deleted = 0
  AND r.period_id = '${period_id}'
GROUP BY r.period_id
```

#### 4.2.2 报表单元格公式
- 总计单元格：`=data_dept_total + pm_total` 或直接使用 `=total_service_times`

### 4.3 场景3：总计行计算实现

#### 4.3.1 SQL数据集配置

```sql
SELECT 
    source_type,
    org_count,
    current_service_count,
    service_times
FROM (
    -- 明细数据
    SELECT 
        'detail' AS row_type,
        i.source_type,
        i.org_count,
        i.current_service_count,
        i.service_times
    FROM report_fill_info_source_by_industry i
    INNER JOIN report_fill_basic_info r ON r.id = i.record_id AND r.deleted = 0
    WHERE i.deleted = 0
      AND r.period_id = '${period_id}'
    
    UNION ALL
    
    -- 总计行
    SELECT 
        'total' AS row_type,
        '总计' AS source_type,
        SUM(i.org_count) AS org_count,
        SUM(i.current_service_count) AS current_service_count,
        SUM(i.service_times) AS service_times
    FROM report_fill_info_source_by_industry i
    INNER JOIN report_fill_basic_info r ON r.id = i.record_id AND r.deleted = 0
    WHERE i.deleted = 0
      AND r.period_id = '${period_id}'
    GROUP BY 'total'
) t
ORDER BY row_type DESC, source_type
```

## 五、积木报表配置步骤

### 5.1 创建数据报表
1. 登录积木报表管理后台
2. 进入"报表管理" → "数据报表"
3. 点击"新建报表"
4. 选择"数据报表"类型

### 5.2 配置数据源
1. 在报表设计中，点击"数据集"
2. 选择"SQL数据集"
3. 输入SQL查询语句（参考上述示例）
4. 配置查询参数（如`period_id`、`role_id`）

### 5.3 设计报表布局
1. 拖拽字段到报表单元格
2. 对于计算字段，在单元格中输入公式：
   - 引用字段：`=字段名`
   - 单元格引用：`=A1`、`=A1-B1`
   - 函数计算：`=SUM(A1:A10)`

### 5.4 配置参数
1. 在报表参数配置中添加参数
2. 参数类型：文本、数字、日期等
3. 参数来源：URL参数、表单控件、固定值等

## 六、注意事项

### 6.1 数据准确性
- 确保SQL查询正确关联所有需要的表
- 注意LEFT JOIN和INNER JOIN的使用场景
- 处理NULL值：使用`COALESCE(field, 0)`

### 6.2 性能优化
- 大数据量时，在SQL中使用索引字段过滤
- 避免在单元格公式中进行大量计算，尽量在SQL中完成
- 使用参数化查询，避免SQL注入

### 6.3 公式语法
- 积木报表支持Excel风格的公式语法
- 字段引用：使用字段名或单元格坐标
- 函数支持：SUM、AVG、MAX、MIN、COUNT、IF等

### 6.4 数据权限
- 根据角色控制可查看的数据范围
- 在SQL中添加权限过滤条件

## 七、扩展建议

### 7.1 动态参数
- 支持从URL参数获取`period_id`、`role_id`等
- 支持从当前登录用户信息获取角色

### 7.2 数据校验
- 在报表中添加数据校验规则
- 显示数据异常提示

### 7.3 报表导出
- 支持导出Excel、PDF格式
- 保持计算公式和格式

### 7.4 报表缓存
- 对于计算复杂的报表，考虑缓存计算结果
- 设置缓存过期时间

## 八、实施步骤

### 8.1 第一阶段：基础数据报表
1. 创建基础数据报表模板
2. 配置SQL数据集，查询填报数据
3. 设计报表布局，展示基础字段

### 8.2 第二阶段：计算字段实现
1. 在SQL中添加计算字段
2. 在报表单元格中配置公式
3. 测试计算结果的准确性

### 8.3 第三阶段：跨记录汇总
1. 实现跨角色/跨周期的汇总SQL
2. 配置汇总报表模板
3. 测试汇总逻辑

### 8.4 第四阶段：优化和扩展
1. 性能优化
2. 添加数据校验
3. 支持报表导出
4. 添加权限控制
