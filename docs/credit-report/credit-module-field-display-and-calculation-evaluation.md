# 征信模块字段展示与计算规则评估报告

## 📋 问题概述

基于您提供的图片和现有系统设计，本文档评估两个关键问题：

1. **问题1**：同一部门对同一字段的两种情况进行填写时的前端展示问题
2. **问题2**：月报和季报复杂计算规则的实现可行性

---

## 问题1：同一部门对同一字段的两种情况进行填写

### 1.1 问题描述

根据图1和图2的描述，存在以下场景：

- **场景**：市场部需要填写两个不同的字段值
  - 字段A：`信息使用者机构总累计数`
  - 字段B：`当前使用服务的信息使用者机构总数`
- **特点**：
  - 两个字段都是市场部填写（同一个部门）
  - 两个字段在业务上属于同一类别（都是"信息使用者机构"相关）
  - 在UI上可能需要并排显示或分组显示

### 1.2 现有设计分析

**当前字段配置结构（CreditFieldConfigDO）：**

```java
- fieldCode: 字段编码（唯一标识）
- fieldName: 字段名称（显示标签）
- deptId: 部门ID（0表示通用字段）
- fieldType: 字段类型
- displayOrder: 显示顺序
- validationRule: 校验规则
```

**现有设计的局限性：**

1. ❌ **缺少字段分组信息**：无法标识哪些字段应该分组显示
2. ❌ **缺少UI布局提示**：无法告诉前端字段应该如何布局（并排、分组、表格等）
3. ❌ **缺少字段关联信息**：无法标识字段之间的业务关联关系
4. ✅ **支持多字段配置**：可以通过配置多个字段（不同的fieldCode）来实现

### 1.3 解决方案

#### 方案一：扩展字段配置表（推荐）

**在 `CreditFieldConfigDO` 中增加以下字段：**

```java
/**
 * 字段分组名称（用于前端分组显示）
 * 例如："市场部填写"、"数据部填写"等
 */
private String groupName;

/**
 * 分组内显示顺序
 * 同一分组内的字段按此顺序排序
 */
private Integer groupOrder;

/**
 * UI布局提示（JSON格式）
 * 例如：{"layout": "row", "span": 6} 表示并排显示，占6列
 */
@TableField(typeHandler = JacksonTypeHandler.class)
private Map<String, Object> layoutHint;
```

**数据库表结构修改：**

```sql
ALTER TABLE credit_field_config 
ADD COLUMN group_name VARCHAR(128) COMMENT '字段分组名称',
ADD COLUMN group_order INT DEFAULT 0 COMMENT '分组内显示顺序',
ADD COLUMN layout_hint JSON COMMENT 'UI布局提示（JSON格式）';
```

**配置示例：**

```json
// 字段1：信息使用者机构总累计数
{
  "fieldCode": "total_users_cumulative",
  "fieldName": "信息使用者机构总累计数",
  "deptId": 2,  // 市场部ID
  "groupName": "市场部填写",
  "groupOrder": 1,
  "layoutHint": {
    "layout": "row",
    "span": 12,
    "position": "left"
  }
}

// 字段2：当前使用服务的信息使用者机构总数
{
  "fieldCode": "current_users_total",
  "fieldName": "当前使用服务的信息使用者机构总数",
  "deptId": 2,  // 市场部ID
  "groupName": "市场部填写",
  "groupOrder": 2,
  "layoutHint": {
    "layout": "row",
    "span": 12,
    "position": "right"
  }
}
```

**前端渲染逻辑：**

```typescript
// 1. 按 groupName 分组
const groupedFields = fields.reduce((acc, field) => {
  const group = field.groupName || 'default';
  if (!acc[group]) acc[group] = [];
  acc[group].push(field);
  return acc;
}, {});

// 2. 每个分组内按 groupOrder 排序
Object.values(groupedFields).forEach(group => {
  group.sort((a, b) => (a.groupOrder || 0) - (b.groupOrder || 0));
});

// 3. 根据 layoutHint 渲染
Object.entries(groupedFields).map(([groupName, groupFields]) => (
  <div key={groupName} className="field-group">
    <h3>{groupName}</h3>
    <div className="field-row">
      {groupFields.map(field => (
        <div 
          key={field.fieldCode} 
          className={`field-item span-${field.layoutHint?.span || 12}`}
        >
          <label>{field.fieldName}</label>
          <input ... />
        </div>
      ))}
    </div>
  </div>
));
```

**优点：**
- ✅ 灵活，支持各种布局需求
- ✅ 向后兼容（新增字段，不影响现有功能）
- ✅ 配置清晰，易于维护

**缺点：**
- ⚠️ 需要修改数据库表结构
- ⚠️ 需要修改后端代码和前端代码

---

#### 方案二：通过字段名称约定（临时方案）

**不修改数据库，通过字段名称约定来实现：**

**配置示例：**

```json
// 字段1
{
  "fieldCode": "market_dept_total_users_cumulative",
  "fieldName": "市场部填写-信息使用者机构总累计数",
  "displayOrder": 10
}

// 字段2
{
  "fieldCode": "market_dept_current_users_total",
  "fieldName": "市场部填写-当前使用服务的信息使用者机构总数",
  "displayOrder": 11
}
```

**前端渲染逻辑：**

```typescript
// 根据字段名称前缀分组
const groupedFields = fields.reduce((acc, field) => {
  // 提取分组名称（例如："市场部填写"）
  const match = field.fieldName.match(/^([^-]+)-/);
  const groupName = match ? match[1] : '其他';
  
  if (!acc[groupName]) acc[groupName] = [];
  acc[groupName].push({
    ...field,
    displayName: field.fieldName.replace(/^[^-]+-/, '') // 去掉前缀
  });
  return acc;
}, {});
```

**优点：**
- ✅ 不需要修改数据库
- ✅ 实现简单快速

**缺点：**
- ❌ 不够灵活，依赖命名约定
- ❌ 字段名称冗余（包含分组信息）
- ❌ 维护成本高

---

#### 方案三：使用字段编码约定（不推荐）

**通过字段编码的命名约定来实现分组：**

```json
// 字段编码格式：{group}_{field}
{
  "fieldCode": "market_dept_total_users_cumulative",
  "fieldName": "信息使用者机构总累计数"
}
```

**缺点：**
- ❌ 字段编码包含业务信息，不够清晰
- ❌ 前端需要解析编码，逻辑复杂
- ❌ 不利于国际化

---

### 1.4 推荐方案

**推荐使用方案一（扩展字段配置表）**，原因：

1. **长期可维护性**：配置清晰，易于理解和维护
2. **灵活性**：支持各种UI布局需求
3. **扩展性**：未来可以支持更复杂的布局需求
4. **向后兼容**：新增字段不影响现有功能

**实施步骤：**

1. **阶段1**：修改数据库表结构，增加 `group_name`、`group_order`、`layout_hint` 字段
2. **阶段2**：修改后端代码，更新 `CreditFieldConfigDO` 和相关VO
3. **阶段3**：修改前端代码，实现分组渲染逻辑
4. **阶段4**：迁移现有字段配置，添加分组信息

---

## 问题2：月报和季报复杂计算规则的实现

### 2.1 问题描述

根据图3和图4的描述，需要实现以下复杂计算：

**月报计算示例：**
- `=sum(数据部+各项目经理)`：从数据部和各项目经理的表单数据中求和

**季报计算示例：**
- **当期发生额**：`= 当季最新月报表3小微企业数 - 2025S3 小微企业户数累计发生额`
- **累计发生额**：`=本季度申请进件企业数总和 + 2025S3 申请贷款户数累计发生额`
- **同期增长量**：`=(当季最新月报表3小微企业数-2025S3小微企业户数累计发生额) - 2024S4 小微企业户数当期发生额`
- **同期增长率**：`= ((当季最新月报表3小微企业数 - 2025S3 小微企业户数累计发生额) - 2024S4 小微企业户数当期发生额) / 2024S4 小微企业户数当期发生额`

### 2.2 现有设计分析

**系统已具备的能力：**

✅ **SpEL表达式引擎**：支持复杂表达式计算  
✅ **计算上下文构建器**：可以加载各部门表单数据、历史月报、历史季报  
✅ **自定义函数**：支持 sum、avg、max、min、safeGet 等函数  
✅ **计算顺序控制**：通过 `calculationOrder` 控制计算顺序  
✅ **结果引用**：可以在表达式中引用已计算的字段（通过 `result` 变量）

**现有上下文变量：**

```java
// 各部门表单数据
deptFormData: Map<Long, Map<String, Object>>
dept{id}: Map<String, Object>  // 例如：dept1, dept2

// 季报计算时的月报数据
monthlyReports: Map<String, Map<String, Object>>  // key为月份周期

// 季报计算时的上季度数据
previousQuarterReport: Map<String, Object>
```

### 2.3 实现方案

#### 2.3.1 月报计算：`=sum(数据部+各项目经理)`

**前提条件：**
- 数据部（假设部门ID=1）填写字段：`service_times_data_dept`
- 各项目经理（多个部门）填写字段：`service_times_pm`

**计算规则配置：**

```json
{
  "targetFieldCode": "total_service_times",
  "reportType": "MONTHLY",
  "ruleType": "FORMULA",
  "ruleExpression": "#safeGet(#dept1, 'service_times_data_dept') + #deptFormData.values().![#safeGet(#this, 'service_times_pm')].sum()",
  "calculationOrder": 1,
  "description": "服务次数总和 = 数据部服务次数 + 各项目经理服务次数之和"
}
```

**SpEL表达式说明：**
- `#safeGet(#dept1, 'service_times_data_dept')`：获取数据部的服务次数
- `#deptFormData.values().![#safeGet(#this, 'service_times_pm')]`：提取所有部门的项目经理服务次数
- `.sum()`：求和

---

#### 2.3.2 季报计算：当期发生额

**计算规则：** `= 当季最新月报表3小微企业数 - 2025S3 小微企业户数累计发生额`

**计算规则配置：**

```json
{
  "targetFieldCode": "current_period_sme_count",
  "reportType": "QUARTERLY",
  "ruleType": "FORMULA",
  "ruleExpression": "#safeGet(#monthlyReports['2024-03'], 'sme_count') - #safeGet(#previousQuarterReport, 'sme_cumulative_count')",
  "calculationOrder": 1,
  "description": "当期发生额 = 季度最后一个月报的小微企业数 - 上季度累计发生额"
}
```

**注意：** 需要扩展上下文构建器，支持获取"当季最新月报"：

```java
// 在 CreditCalculationContextBuilder 中增加方法
private void loadLatestMonthlyReport(StandardEvaluationContext context, String reportPeriod) {
    List<String> monthlyPeriods = parseQuarterToMonths(reportPeriod);
    if (!monthlyPeriods.isEmpty()) {
        String latestMonth = monthlyPeriods.get(monthlyPeriods.size() - 1); // 最后一个月份
        CreditSummaryReportDO latestReport = summaryReportService.getByPeriodAndType(latestMonth, "MONTHLY");
        if (latestReport != null && latestReport.getReportData() != null) {
            context.setVariable("latestMonthlyReport", latestReport.getReportData());
        }
    }
}
```

---

#### 2.3.3 季报计算：累计发生额

**计算规则：** `=本季度申请进件企业数总和 + 2025S3 申请贷款户数累计发生额`

**计算规则配置：**

```json
{
  "targetFieldCode": "cumulative_loan_applications",
  "reportType": "QUARTERLY",
  "ruleType": "FORMULA",
  "ruleExpression": "#deptFormData.values().![#safeGet(#this, 'quarter_loan_applications')].sum() + #safeGet(#previousQuarterReport, 'cumulative_loan_applications')",
  "calculationOrder": 2,
  "description": "累计发生额 = 本季度申请进件企业数总和 + 上季度累计发生额"
}
```

---

#### 2.3.4 季报计算：同期增长量

**计算规则：** `=(当季最新月报表3小微企业数-2025S3小微企业户数累计发生额) - 2024S4 小微企业户数当期发生额`

**需要扩展上下文构建器，支持获取去年同期数据：**

```java
// 在 CreditCalculationContextBuilder 中增加方法
private void loadSamePeriodLastYear(StandardEvaluationContext context, String reportPeriod) {
    // 计算去年同期季度
    String[] parts = reportPeriod.split("-Q");
    if (parts.length == 2) {
        int year = Integer.parseInt(parts[0]);
        int quarter = Integer.parseInt(parts[1]);
        String lastYearQuarter = String.format("%d-Q%d", year - 1, quarter);
        
        CreditSummaryReportDO lastYearReport = summaryReportService.getByPeriodAndType(lastYearQuarter, "QUARTERLY");
        if (lastYearReport != null && lastYearReport.getReportData() != null) {
            context.setVariable("samePeriodLastYear", lastYearReport.getReportData());
        }
    }
}
```

**计算规则配置：**

```json
{
  "targetFieldCode": "yoy_growth_amount",
  "reportType": "QUARTERLY",
  "ruleType": "FORMULA",
  "ruleExpression": "(#safeGet(#latestMonthlyReport, 'sme_count') - #safeGet(#previousQuarterReport, 'sme_cumulative_count')) - #safeGet(#samePeriodLastYear, 'current_period_sme_count')",
  "calculationOrder": 3,
  "description": "同期增长量 = (当期发生额) - 去年同期当期发生额"
}
```

---

#### 2.3.5 季报计算：同期增长率

**计算规则：** `= ((当季最新月报表3小微企业数 - 2025S3 小微企业户数累计发生额) - 2024S4 小微企业户数当期发生额) / 2024S4 小微企业户数当期发生额`

**计算规则配置：**

```json
{
  "targetFieldCode": "yoy_growth_rate",
  "reportType": "QUARTERLY",
  "ruleType": "FORMULA",
  "ruleExpression": "#safeGet(#samePeriodLastYear, 'current_period_sme_count') != 0 ? ((#safeGet(#latestMonthlyReport, 'sme_count') - #safeGet(#previousQuarterReport, 'sme_cumulative_count')) - #safeGet(#samePeriodLastYear, 'current_period_sme_count')) / #safeGet(#samePeriodLastYear, 'current_period_sme_count') : 0",
  "calculationOrder": 4,
  "description": "同期增长率 = 同期增长量 / 去年同期当期发生额"
}
```

**注意：** 需要处理除零情况，使用三元运算符判断。

---

### 2.4 需要扩展的功能

#### 2.4.1 扩展上下文构建器

**在 `CreditCalculationContextBuilder` 中增加以下方法：**

```java
/**
 * 加载当季最新月报数据
 */
private void loadLatestMonthlyReport(StandardEvaluationContext context, String reportPeriod) {
    List<String> monthlyPeriods = parseQuarterToMonths(reportPeriod);
    if (!monthlyPeriods.isEmpty()) {
        String latestMonth = monthlyPeriods.get(monthlyPeriods.size() - 1);
        CreditSummaryReportDO latestReport = summaryReportService.getByPeriodAndType(latestMonth, "MONTHLY");
        if (latestReport != null && latestReport.getReportData() != null) {
            context.setVariable("latestMonthlyReport", latestReport.getReportData());
        }
    }
}

/**
 * 加载去年同期数据
 */
private void loadSamePeriodLastYear(StandardEvaluationContext context, String reportPeriod) {
    String[] parts = reportPeriod.split("-Q");
    if (parts.length == 2) {
        int year = Integer.parseInt(parts[0]);
        int quarter = Integer.parseInt(parts[1]);
        String lastYearQuarter = String.format("%d-Q%d", year - 1, quarter);
        
        CreditSummaryReportDO lastYearReport = summaryReportService.getByPeriodAndType(lastYearQuarter, "QUARTERLY");
        if (lastYearReport != null && lastYearReport.getReportData() != null) {
            context.setVariable("samePeriodLastYear", lastYearReport.getReportData());
        }
    }
}

/**
 * 加载指定周期的报表数据（通用方法）
 */
private void loadReportByPeriod(StandardEvaluationContext context, String reportPeriod, String reportType, String variableName) {
    CreditSummaryReportDO report = summaryReportService.getByPeriodAndType(reportPeriod, reportType);
    if (report != null && report.getReportData() != null) {
        context.setVariable(variableName, report.getReportData());
    }
}
```

**修改 `buildContext` 方法：**

```java
public EvaluationContext buildContext(String reportPeriod, String reportType) {
    StandardEvaluationContext context = new StandardEvaluationContext();
    
    // 1. 注册自定义函数
    registerCustomFunctions(context);
    
    // 2. 加载各部门表单数据
    Map<Long, Map<String, Object>> deptFormDataMap = formDataService.getDeptFormDataMap(reportPeriod, reportType);
    context.setVariable("deptFormData", deptFormDataMap);
    deptFormDataMap.forEach((deptId, formData) -> {
        context.setVariable("dept" + deptId, formData);
    });
    
    // 3. 如果是季报，加载相关数据
    if ("QUARTERLY".equals(reportType)) {
        loadMonthlyReports(context, reportPeriod);
        loadPreviousQuarterReport(context, reportPeriod);
        loadLatestMonthlyReport(context, reportPeriod);
        loadSamePeriodLastYear(context, reportPeriod);
    }
    
    return context;
}
```

---

#### 2.4.2 扩展自定义函数

**在 `CreditCalculationFunctions` 中增加除法函数（处理除零）：**

```java
/**
 * 安全除法函数（避免除零）
 *
 * @param dividend 被除数
 * @param divisor  除数
 * @return 除法结果，如果除数为0则返回0
 */
public static BigDecimal safeDivide(Number dividend, Number divisor) {
    if (divisor == null || divisor.doubleValue() == 0) {
        return BigDecimal.ZERO;
    }
    BigDecimal dividendBD = dividend == null ? BigDecimal.ZERO : new BigDecimal(dividend.toString());
    BigDecimal divisorBD = new BigDecimal(divisor.toString());
    return dividendBD.divide(divisorBD, 4, RoundingMode.HALF_UP);
}
```

**注册函数：**

```java
context.registerFunction("safeDivide",
    CreditCalculationFunctions.class.getDeclaredMethod("safeDivide", Number.class, Number.class));
```

---

### 2.5 实现可行性评估

**✅ 完全可行**

**理由：**

1. **SpEL表达式支持**：SpEL支持复杂的数学运算、条件判断、集合操作等
2. **上下文数据完整**：可以加载各部门表单数据、历史月报、历史季报、去年同期数据
3. **计算顺序控制**：通过 `calculationOrder` 可以控制计算顺序，支持依赖关系
4. **结果引用**：可以在表达式中引用已计算的字段（通过 `result` 变量）

**实施建议：**

1. **扩展上下文构建器**：增加加载"当季最新月报"和"去年同期数据"的方法
2. **扩展自定义函数**：增加 `safeDivide` 等安全计算函数
3. **配置计算规则**：按照上述示例配置计算规则
4. **测试验证**：使用实际数据测试计算结果的正确性

---

## 总结

### 问题1：字段展示问题

**现状：** 现有设计不支持字段分组和UI布局提示  
**解决方案：** 推荐扩展字段配置表，增加 `groupName`、`groupOrder`、`layoutHint` 字段  
**实施难度：** 中等（需要修改数据库、后端、前端）  
**优先级：** 高（影响用户体验）

### 问题2：复杂计算规则

**现状：** 系统已具备实现复杂计算的能力  
**解决方案：** 扩展上下文构建器，增加加载历史数据的方法，配置SpEL表达式  
**实施难度：** 低（主要是配置工作）  
**优先级：** 高（核心业务需求）

---

**文档版本：** v1.0  
**最后更新：** 2024-01-XX

如有问题，请联系技术支持。
