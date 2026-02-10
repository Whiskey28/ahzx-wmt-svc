# 计算规则展示增强需求评估

## 📋 需求概述

**用户需求：**
1. 计算规则 = 汇总报表中每个字段的来源与计算方案 ✅（理解正确）
2. 需要增加字段用于前端展示中文名
3. 需要增加组名，支持按图1的方式展示（分组、中文名称、层次结构）

**目标展示效果（图1）：**
- 有分组标题（如"金融、类金融机构"、"提供的征信产品(服务)的名称及次数"）
- 所有标签都是中文名称
- 有层次结构（分组下有多行数据）
- 有合并单元格效果（分组名称跨多行）
- 有多个数据列（"总累计数"、"当前使用服务的"、"当年提供产品(服务)次数"）

---

## ✅ 需求理解确认

### 1. 计算规则的作用

**您的理解完全正确！**

```
计算规则配置 → 汇总报表字段
  ├─ targetFieldCode（字段编码）
  ├─ ruleExpression（计算表达式）
  └─ 计算结果 → reportData[targetFieldCode] = 计算值
```

**证据：**
- `CreditCalculationEngine.calculateSummaryReport()` 中，每个计算规则生成一个汇总报表字段
- `result.put(rule.getTargetFieldCode(), value)` - 使用 `targetFieldCode` 作为报表数据的 key

---

## 🔍 图1展示需求分析

### 图1的关键特征

1. **分组结构**
   - 分组1："金融、类金融机构"（包含12个子项：银行、证券、保险等）
   - 分组2："提供的征信产品(服务)的名称及次数"（包含4个子项：信用报告、信用分、反欺诈、其他）

2. **中文名称**
   - 所有行标签都是中文（如"银行"、"证券"、"企业信用报告"）
   - 分组名称也是中文

3. **多列数据**
   - 列1："总累计数"
   - 列2："当前使用服务的"
   - 列3："当年提供产品(服务)次数"

4. **层次结构**
   - 分组名称（跨多行）
   - 分组下的具体字段（多行）

---

## 💡 需要增加的字段评估

### 方案：在计算规则表中增加以下字段

#### 1. `targetFieldName`（目标字段名称）- ✅ 必需

**用途：** 前端展示时的中文名称

**示例：**
- `targetFieldCode`: `bank_total_cumulative`
- `targetFieldName`: `银行`

**数据库字段：**
```sql
ALTER TABLE credit_calculation_rule 
ADD COLUMN target_field_name VARCHAR(128) COMMENT '目标字段名称（用于前端显示）' AFTER target_field_code;
```

---

#### 2. `groupName`（组名）- ✅ 必需

**用途：** 字段分组，支持分组展示

**示例：**
- `targetFieldCode`: `bank_total_cumulative`
- `targetFieldName`: `银行`
- `groupName`: `金融、类金融机构`

**数据库字段：**
```sql
ALTER TABLE credit_calculation_rule 
ADD COLUMN group_name VARCHAR(128) COMMENT '字段分组名称（用于前端分组展示）' AFTER target_field_name;
```

---

#### 3. `groupOrder`（分组内显示顺序）- ✅ 推荐

**用途：** 同一分组内的字段排序

**示例：**
- 分组"金融、类金融机构"内：
  - `银行`: `groupOrder = 1`
  - `证券`: `groupOrder = 2`
  - `保险`: `groupOrder = 3`

**数据库字段：**
```sql
ALTER TABLE credit_calculation_rule 
ADD COLUMN group_order INT DEFAULT 0 COMMENT '分组内显示顺序' AFTER group_name;
```

**注意：** 如果只有 `calculationOrder`（全局计算顺序），可能无法满足分组内的排序需求。建议同时保留 `calculationOrder`（用于计算顺序）和 `groupOrder`（用于展示顺序）。

---

#### 4. `displayOrder`（整体显示顺序）- ⚠️ 可选

**用途：** 跨分组的整体排序（如果分组也需要排序）

**说明：**
- 如果分组也需要排序（如"金融、类金融机构"在"提供的征信产品"之前），可能需要额外的分组排序字段
- 或者可以通过 `groupName` + `groupOrder` 的组合来实现

**建议：** 如果分组数量不多，可以通过 `groupName` 的字母顺序或配置顺序来实现，不一定需要单独的字段。

---

## 📊 字段设计方案

### 完整的字段设计

**修改 `CreditCalculationRuleDO`：**

```java
/**
 * 目标字段编码（计算结果字段）
 */
private String targetFieldCode;

/**
 * 目标字段名称（用于前端显示）
 */
private String targetFieldName;  // ✅ 新增

/**
 * 字段分组名称（用于前端分组展示）
 */
private String groupName;  // ✅ 新增

/**
 * 分组内显示顺序
 */
private Integer groupOrder;  // ✅ 新增（推荐）

/**
 * 计算顺序（保留，用于控制计算执行顺序）
 */
private Integer calculationOrder;  // 保留原有字段
```

**数据库表结构修改：**

```sql
ALTER TABLE credit_calculation_rule 
ADD COLUMN target_field_name VARCHAR(128) COMMENT '目标字段名称（用于前端显示）' AFTER target_field_code,
ADD COLUMN group_name VARCHAR(128) COMMENT '字段分组名称（用于前端分组展示）' AFTER target_field_name,
ADD COLUMN group_order INT DEFAULT 0 COMMENT '分组内显示顺序' AFTER group_name;
```

---

## 🎯 前端展示实现方案

### 数据获取

**后端接口返回：**

```json
{
  "code": 0,
  "data": {
    "id": 123,
    "reportPeriod": "2025-12",
    "reportType": "MONTHLY",
    "reportData": {
      "bank_total_cumulative": 34,
      "securities_total_cumulative": 1,
      "insurance_total_cumulative": 3
    },
    "fieldMetadata": [  // ✅ 新增：字段元数据
      {
        "fieldCode": "bank_total_cumulative",
        "fieldName": "银行",
        "groupName": "金融、类金融机构",
        "groupOrder": 1
      },
      {
        "fieldCode": "securities_total_cumulative",
        "fieldName": "证券",
        "groupName": "金融、类金融机构",
        "groupOrder": 2
      }
    ]
  }
}
```

**或者更简洁的方式：**

```json
{
  "code": 0,
  "data": {
    "id": 123,
    "reportPeriod": "2025-12",
    "reportType": "MONTHLY",
    "reportData": {
      "bank_total_cumulative": 34,
      "securities_total_cumulative": 1
    },
    "fieldNameMap": {  // 字段编码 -> 字段名称
      "bank_total_cumulative": "银行",
      "securities_total_cumulative": "证券"
    },
    "fieldGroupMap": {  // 字段编码 -> 分组信息
      "bank_total_cumulative": {
        "groupName": "金融、类金融机构",
        "groupOrder": 1
      }
    }
  }
}
```

---

### 前端渲染逻辑

**React 示例：**

```typescript
interface FieldMetadata {
  fieldCode: string;
  fieldName: string;
  groupName: string;
  groupOrder: number;
}

// 1. 获取汇总报表数据
const report = await fetch(`/credit/summary-report/get?id=${id}`);
const reportData = report.data.reportData;
const fieldMetadata = report.data.fieldMetadata;  // 字段元数据

// 2. 按分组组织数据
const groupedData = fieldMetadata.reduce((acc, field) => {
  const group = field.groupName || '其他';
  if (!acc[group]) {
    acc[group] = [];
  }
  acc[group].push({
    ...field,
    value: reportData[field.fieldCode]
  });
  return acc;
}, {} as Record<string, Array<FieldMetadata & { value: any }>>);

// 3. 每个分组内按 groupOrder 排序
Object.values(groupedData).forEach(group => {
  group.sort((a, b) => a.groupOrder - b.groupOrder);
});

// 4. 渲染表格
return (
  <table>
    <thead>
      <tr>
        <th rowSpan={2}>项目</th>
        <th colSpan={2}>信息使用者机构</th>
        <th>当年提供产品(服务)次数</th>
      </tr>
      <tr>
        <th>总累计数</th>
        <th>当前使用服务的</th>
      </tr>
    </thead>
    <tbody>
      {Object.entries(groupedData).map(([groupName, fields]) => (
        <>
          {/* 分组标题行（合并单元格） */}
          <tr key={`group-${groupName}`}>
            <td rowSpan={fields.length} className="group-header">
              {groupName}
            </td>
            <td>{fields[0].fieldName}</td>
            <td>{fields[0].value}</td>
            <td>{/* 其他列的值 */}</td>
          </tr>
          {/* 分组内的其他行 */}
          {fields.slice(1).map((field, index) => (
            <tr key={field.fieldCode}>
              <td>{field.fieldName}</td>
              <td>{field.value}</td>
              <td>{/* 其他列的值 */}</td>
            </tr>
          ))}
        </>
      ))}
    </tbody>
  </table>
);
```

---

## ✅ 可行性评估

### 核心需求满足度

| 需求项 | 是否满足 | 说明 |
|--------|---------|------|
| **中文名称展示** | ✅ 完全满足 | `targetFieldName` 提供中文名称 |
| **分组展示** | ✅ 完全满足 | `groupName` 支持分组 |
| **分组内排序** | ✅ 完全满足 | `groupOrder` 支持分组内排序 |
| **层次结构** | ✅ 完全满足 | 前端通过 `groupName` 组织层次结构 |
| **合并单元格** | ✅ 完全满足 | 前端通过 `rowSpan` 实现合并单元格 |

### 多列数据支持

**图1中有多列数据：**
- "总累计数"
- "当前使用服务的"
- "当年提供产品(服务)次数"

**解决方案：**

这些多列数据实际上对应**多个计算规则**，每个计算规则生成一列数据：

```json
// 规则1：银行总累计数
{
  "targetFieldCode": "bank_total_cumulative",
  "targetFieldName": "银行",
  "groupName": "金融、类金融机构",
  "groupOrder": 1
}

// 规则2：银行当前使用服务的
{
  "targetFieldCode": "bank_current_users",
  "targetFieldName": "银行",
  "groupName": "金融、类金融机构",
  "groupOrder": 1
}

// 规则3：银行当年提供产品次数
{
  "targetFieldCode": "bank_products_provided",
  "targetFieldName": "银行",
  "groupName": "金融、类金融机构",
  "groupOrder": 1
}
```

**前端展示逻辑：**

```typescript
// 按 groupName + groupOrder 分组，然后按列类型组织
const organizedData = fields.reduce((acc, field) => {
  const key = `${field.groupName}_${field.groupOrder}`;
  if (!acc[key]) {
    acc[key] = {
      groupName: field.groupName,
      groupOrder: field.groupOrder,
      fieldName: field.fieldName,
      columns: {}
    };
  }
  // 根据字段编码的后缀判断列类型
  if (field.fieldCode.endsWith('_total_cumulative')) {
    acc[key].columns.totalCumulative = field.value;
  } else if (field.fieldCode.endsWith('_current_users')) {
    acc[key].columns.currentUsers = field.value;
  } else if (field.fieldCode.endsWith('_products_provided')) {
    acc[key].columns.productsProvided = field.value;
  }
  return acc;
}, {});
```

**或者更灵活的方式：**

在计算规则中增加 `columnType` 字段，明确标识该字段属于哪一列：

```java
/**
 * 列类型（用于多列展示）
 * 例如：total_cumulative（总累计数）、current_users（当前使用服务的）、products_provided（当年提供产品次数）
 */
private String columnType;
```

---

## 🎯 最终推荐方案

### 必需字段（3个）

1. ✅ **`targetFieldName`**：目标字段名称（中文）
2. ✅ **`groupName`**：字段分组名称
3. ✅ **`groupOrder`**：分组内显示顺序

### 可选字段（1个）

4. ⚠️ **`columnType`**：列类型（如果有多列数据需要区分）

**说明：** 如果汇总报表只有单列数据，不需要 `columnType`。如果像图1那样有多列数据，建议增加 `columnType` 字段。

---

## 📝 实施建议

### 阶段1：数据库表结构修改

```sql
-- 为计算规则表增加展示相关字段
ALTER TABLE credit_calculation_rule 
ADD COLUMN target_field_name VARCHAR(128) COMMENT '目标字段名称（用于前端显示）' AFTER target_field_code,
ADD COLUMN group_name VARCHAR(128) COMMENT '字段分组名称（用于前端分组展示）' AFTER target_field_name,
ADD COLUMN group_order INT DEFAULT 0 COMMENT '分组内显示顺序' AFTER group_name,
ADD COLUMN column_type VARCHAR(64) COMMENT '列类型（用于多列展示，如：total_cumulative、current_users、products_provided）' AFTER group_order;
```

### 阶段2：后端代码修改

1. **修改 `CreditCalculationRuleDO`：**
   ```java
   private String targetFieldName;
   private String groupName;
   private Integer groupOrder;
   private String columnType;  // 可选
   ```

2. **修改 `CreditCalculationRuleSaveReqVO`：**
   ```java
   private String targetFieldName;
   private String groupName;
   private Integer groupOrder;
   private String columnType;  // 可选
   ```

3. **修改 `CreditSummaryReportRespVO`：**
   ```java
   @Schema(description = "报表数据（JSON格式）")
   private Map<String, Object> reportData;
   
   @Schema(description = "字段元数据列表（用于前端展示）")
   private List<FieldMetadataVO> fieldMetadata;  // 新增
   ```

4. **修改 `CreditSummaryReportService`：**
   ```java
   // 在查询报表时，从计算规则表中查询字段元数据
   public CreditSummaryReportRespVO getSummaryReport(Long id) {
       CreditSummaryReportDO report = getSummaryReport(id);
       // 查询字段元数据
       List<CreditCalculationRuleDO> rules = calculationRuleService
           .getRulesByReportType(report.getReportType());
       List<FieldMetadataVO> fieldMetadata = rules.stream()
           .map(rule -> {
               FieldMetadataVO vo = new FieldMetadataVO();
               vo.setFieldCode(rule.getTargetFieldCode());
               vo.setFieldName(rule.getTargetFieldName());
               vo.setGroupName(rule.getGroupName());
               vo.setGroupOrder(rule.getGroupOrder());
               vo.setColumnType(rule.getColumnType());
               return vo;
           })
           .collect(Collectors.toList());
       // 设置到响应VO中
   }
   ```

### 阶段3：前端代码修改

```typescript
// 前端根据 fieldMetadata 组织数据并渲染
const renderReport = (report: SummaryReport) => {
  // 1. 按分组组织数据
  const grouped = organizeByGroup(report.fieldMetadata, report.reportData);
  
  // 2. 渲染表格（支持分组、合并单元格、多列）
  return renderGroupedTable(grouped);
};
```

---

## ✅ 总结

### 您的理解完全正确！

1. ✅ **计算规则 = 汇总报表字段的来源与计算方案** - 完全正确
2. ✅ **需要增加 `targetFieldName`（中文名）** - 必需
3. ✅ **需要增加 `groupName`（组名）** - 必需
4. ✅ **可以满足图1的展示需求** - 完全满足

### 建议增加的字段

| 字段 | 是否必需 | 用途 |
|------|---------|------|
| `targetFieldName` | ✅ 必需 | 前端展示中文名称 |
| `groupName` | ✅ 必需 | 字段分组 |
| `groupOrder` | ✅ 推荐 | 分组内排序 |
| `columnType` | ⚠️ 可选 | 多列数据区分（如果有多列） |

### 实施优先级

1. **P0（必须）**：`targetFieldName`、`groupName`
2. **P1（推荐）**：`groupOrder`
3. **P2（可选）**：`columnType`（如果有多列数据需求）

---

**文档版本：** v1.0  
**最后更新：** 2024-01-XX

如有问题，请联系技术支持。
