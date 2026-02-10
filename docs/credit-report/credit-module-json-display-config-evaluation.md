# JSON展示配置字段方案评估

## 📋 需求概述

**用户需求：**
- 为计算规则和字段配置都增加一个JSON字段
- 存储前端展示所需的完整配置信息（类似低代码平台的配置JSON）
- 即使前端暂时无法达到低代码平台的水平，也可以先在后端存储这个JSON配置
- **数据库：PostgreSQL v15**

**最终方案：**
1. `CreditCalculationRuleDO`：增加 `targetFieldName`（VARCHAR）和 `displayConfig`（JSONB）
2. `CreditFieldConfigDO`：增加 `displayConfig`（JSONB）

---

## ✅ 技术可行性

### 现有技术基础

**系统已支持JSON字段：**

```java
// CreditCalculationRuleDO 中已有JSON字段
@TableField(typeHandler = JacksonTypeHandler.class)
private Map<String, Object> dataSource;  // ✅ 已在使用（JSONB类型）

// CreditFieldConfigDO 中已有JSON字段
@TableField(typeHandler = JacksonTypeHandler.class)
private Map<String, Object> validationRule;  // ✅ 已在使用（JSONB类型）
```

**PostgreSQL v15 JSONB支持：**
- ✅ 原生支持JSONB类型
- ✅ 支持GIN索引（高性能查询）
- ✅ MyBatis Plus的JacksonTypeHandler已支持JSONB

**结论：** ✅ 技术完全可行，系统已具备JSON字段处理能力

---

## 💡 JSON字段方案设计

### 方案1：计算规则表增加 `displayConfig` JSON字段

#### 1.1 数据库表结构修改

```sql
ALTER TABLE credit_calculation_rule 
ADD COLUMN display_config JSON COMMENT '前端展示配置（JSON格式，存储字段名称、分组、排序等展示信息）' AFTER description;
```

#### 1.2 JSON结构设计

**计算规则的展示配置JSON结构：**

```json
{
  "fieldName": "银行",
  "groupName": "金融、类金融机构",
  "groupOrder": 1,
  "columnType": "total_cumulative",
  "displayOrder": 10,
  "layout": {
    "span": 12,
    "position": "left"
  },
  "style": {
    "fontSize": "14px",
    "fontWeight": "normal",
    "textAlign": "left"
  },
  "format": {
    "type": "number",
    "precision": 0,
    "thousandSeparator": true
  },
  "ui": {
    "component": "text",
    "readonly": true,
    "visible": true
  }
}
```

**简化版（推荐）：**

```json
{
  "fieldName": "银行",
  "groupName": "金融、类金融机构",
  "groupOrder": 1,
  "columnType": "total_cumulative"
}
```

---

### 方案2：字段配置表增加 `displayConfig` JSON字段

#### 2.1 数据库表结构修改

```sql
ALTER TABLE credit_field_config 
ADD COLUMN display_config JSON COMMENT '前端展示配置（JSON格式，存储分组、布局等展示信息）' AFTER validation_rule;
```

#### 2.2 JSON结构设计

**字段配置的展示配置JSON结构：**

```json
{
  "groupName": "市场部填写",
  "groupOrder": 1,
  "layout": {
    "type": "row",
    "span": 12,
    "position": "left"
  },
  "ui": {
    "component": "input",
    "placeholder": "请输入",
    "prefix": "",
    "suffix": ""
  },
  "style": {
    "width": "100%",
    "marginBottom": "16px"
  }
}
```

**简化版（推荐）：**

```json
{
  "groupName": "市场部填写",
  "groupOrder": 1,
  "layout": {
    "type": "row",
    "span": 12
  }
}
```

---

## 📊 方案对比

### 方案A：单独字段方案

**字段列表：**
- `targetFieldName` (VARCHAR)
- `groupName` (VARCHAR)
- `groupOrder` (INT)
- `columnType` (VARCHAR)

**优点：**
- ✅ 查询性能好（可以直接在SQL中筛选、排序）
- ✅ 数据结构清晰，易于理解
- ✅ 支持数据库索引（如果需要）
- ✅ 类型安全（Java中可以直接访问字段）

**缺点：**
- ❌ 扩展性差（新增展示需求需要修改表结构）
- ❌ 字段数量多（每个需求一个字段）
- ❌ 维护成本高（需要频繁修改表结构）

---

### 方案B：JSON字段方案（推荐）

**字段列表：**
- `displayConfig` (JSON)

**优点：**
- ✅ **扩展性强**：新增展示需求只需修改JSON结构，无需修改表结构
- ✅ **灵活度高**：可以存储任意复杂的配置信息
- ✅ **字段数量少**：只需一个JSON字段
- ✅ **易于迁移**：未来迁移到低代码平台时，可以直接使用这个JSON
- ✅ **配置集中**：所有展示配置集中在一个字段，易于管理

**缺点：**
- ⚠️ 查询性能略差（JSON字段查询需要函数调用）
- ⚠️ 类型安全较弱（需要手动解析JSON）
- ⚠️ 索引支持有限（MySQL 8.0+支持JSON索引，但不如普通字段方便）

---

## 🎯 最终方案：简化版

### 计算规则表：核心字段 + JSON配置

**设计思路：**
- **核心字段**：`targetFieldName`（字段名称，便于查询和展示）
- **JSON配置**：`displayConfig`（存储所有展示相关配置，便于扩展）

#### 计算规则表设计

```java
/**
 * 目标字段编码（计算结果字段）
 */
private String targetFieldCode;

/**
 * 目标字段名称（用于前端显示）
 */
private String targetFieldName;  // ✅ 新增独立字段

/**
 * 前端展示配置（JSONB格式，存储字段分组、布局、样式等展示信息）
 */
@TableField(typeHandler = JacksonTypeHandler.class)
private Map<String, Object> displayConfig;  // ✅ 新增JSON字段
```

**`displayConfig` JSON结构：**

```json
{
  "columnType": "total_cumulative",
  "layout": {
    "type": "row",
    "span": 12,
    "position": "left"
  },
  "style": {
    "fontSize": "14px",
    "fontWeight": "normal"
  },
  "format": {
    "type": "number",
    "precision": 0,
    "thousandSeparator": true
  },
  "ui": {
    "component": "text",
    "readonly": true,
    "visible": true
  },
  "custom": {
    // 未来扩展的配置项
  }
}
```

#### 字段配置表设计

```java
/**
 * 字段编码（唯一标识）
 */
private String fieldCode;

/**
 * 字段名称
 */
private String fieldName;

/**
 * 显示顺序
 */
private Integer displayOrder;

/**
 * 前端展示配置（JSON格式，存储分组、布局等展示信息）
 */
@TableField(typeHandler = JacksonTypeHandler.class)
private Map<String, Object> displayConfig;  // ✅ 新增JSON字段
```

**`displayConfig` JSON结构：**

```json
{
  "groupName": "市场部填写",
  "groupOrder": 1,
  "layout": {
    "type": "row",
    "span": 12,
    "position": "left"
  },
  "ui": {
    "component": "input",
    "placeholder": "请输入",
    "prefix": "",
    "suffix": ""
  },
  "style": {
    "width": "100%",
    "marginBottom": "16px"
  },
  "custom": {
    // 未来扩展的配置项
  }
}
```

---

## 📝 实施建议

### 阶段1：数据库表结构修改（PostgreSQL v15）

```sql
-- ============================================
-- 征信模块展示配置字段升级脚本
-- 数据库：PostgreSQL v15
-- ============================================

-- 1. 计算规则表升级
ALTER TABLE credit_calculation_rule 
ADD COLUMN IF NOT EXISTS target_field_name VARCHAR(128) NULL;

COMMENT ON COLUMN credit_calculation_rule.target_field_name IS '目标字段名称（用于前端显示）';

ALTER TABLE credit_calculation_rule 
ADD COLUMN IF NOT EXISTS display_config JSONB NULL;

COMMENT ON COLUMN credit_calculation_rule.display_config IS '前端展示配置（JSONB格式，存储字段分组、布局、样式等展示信息）';

-- 为 display_config 创建 GIN 索引（可选）
CREATE INDEX IF NOT EXISTS idx_credit_calculation_rule_display_config_gin 
ON credit_calculation_rule USING GIN (display_config) 
WHERE deleted = 0 AND status = 1;

-- 2. 字段配置表升级
ALTER TABLE credit_field_config 
ADD COLUMN IF NOT EXISTS display_config JSONB NULL;

COMMENT ON COLUMN credit_field_config.display_config IS '前端展示配置（JSONB格式，存储字段分组、布局、样式等展示信息）';

-- 为 display_config 创建 GIN 索引（可选）
CREATE INDEX IF NOT EXISTS idx_credit_field_config_display_config_gin 
ON credit_field_config USING GIN (display_config) 
WHERE deleted = 0 AND status = 1;
```

---

### 阶段2：后端代码修改

#### 2.1 修改 `CreditCalculationRuleDO`

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
 * 前端展示配置（JSONB格式，存储字段分组、布局、样式等展示信息）
 * 例如：{"groupName": "金融、类金融机构", "groupOrder": 1, "columnType": "total_cumulative", ...}
 */
@TableField(typeHandler = JacksonTypeHandler.class)
private Map<String, Object> displayConfig;  // ✅ 新增
```

#### 2.2 修改 `CreditFieldConfigDO`

```java
/**
 * 前端展示配置（JSON格式，存储分组、布局等展示信息）
 * 例如：{"groupName": "市场部填写", "groupOrder": 1, "layout": {...}}
 */
@TableField(typeHandler = JacksonTypeHandler.class)
private Map<String, Object> displayConfig;  // ✅ 新增
```

#### 2.3 修改VO类

**`CreditCalculationRuleSaveReqVO`：**

```java
@Schema(description = "目标字段名称（用于前端显示）")
private String targetFieldName;  // ✅ 新增

@Schema(description = "前端展示配置（JSON格式）")
private Map<String, Object> displayConfig;  // ✅ 新增
```

**`CreditFieldConfigSaveReqVO`：**

```java
@TableField(typeHandler = JacksonTypeHandler.class)
private Map<String, Object> displayConfig;
```

#### 2.4 修改Service层

**在查询汇总报表时，返回字段元数据：**

```java
@Override
public CreditSummaryReportRespVO getSummaryReport(Long id) {
    CreditSummaryReportDO report = getSummaryReport(id);
    CreditSummaryReportRespVO respVO = CreditSummaryReportConvert.INSTANCE.convert(report);
    
    // 查询字段元数据（包含展示配置）
    List<CreditCalculationRuleDO> rules = calculationRuleService
        .getRulesByReportType(report.getReportType());
    
    List<FieldMetadataVO> fieldMetadata = rules.stream()
        .filter(rule -> rule.getStatus() != null && rule.getStatus() == 1)  // 只返回启用的规则
        .map(rule -> {
            FieldMetadataVO vo = new FieldMetadataVO();
            vo.setFieldCode(rule.getTargetFieldCode());
            vo.setFieldName(rule.getTargetFieldName());  // 从独立字段获取
            vo.setDisplayConfig(rule.getDisplayConfig());  // 从JSON字段获取
            return vo;
        })
        .collect(Collectors.toList());
    
    respVO.setFieldMetadata(fieldMetadata);
    return respVO;
}
```

---

### 阶段3：JSON配置结构定义

#### 3.1 计算规则展示配置JSON Schema

```typescript
interface CalculationRuleDisplayConfig {
  // 列类型（用于多列展示）
  columnType?: string;  // "total_cumulative" | "current_users" | "products_provided"
  
  // 布局配置
  layout?: {
    type?: string;      // "row" | "column"
    span?: number;      // 占位（1-24）
    position?: string;  // "left" | "right" | "center"
  };
  
  // 样式配置
  style?: {
    fontSize?: string;
    fontWeight?: string;
    textAlign?: string;
    color?: string;
  };
  
  // 格式化配置
  format?: {
    type?: string;              // "number" | "currency" | "percent" | "date"
    precision?: number;          // 小数位数
    thousandSeparator?: boolean; // 千分位分隔符
    currency?: string;          // 货币符号
  };
  
  // UI组件配置
  ui?: {
    component?: string;  // "text" | "input" | "select" | "date"
    readonly?: boolean;
    visible?: boolean;
    disabled?: boolean;
  };
  
  // 自定义配置（未来扩展）
  custom?: Record<string, any>;
}
```

#### 3.2 字段配置展示配置JSON Schema

```typescript
interface FieldConfigDisplayConfig {
  // 分组配置
  groupName?: string;
  groupOrder?: number;
  
  // 布局配置
  layout?: {
    type?: string;      // "row" | "column"
    span?: number;      // 占位（1-24）
    position?: string;  // "left" | "right" | "center"
  };
  
  // UI组件配置
  ui?: {
    component?: string;     // "input" | "textarea" | "select" | "date" | "number"
    placeholder?: string;
    prefix?: string;        // 前缀（如"¥"）
    suffix?: string;        // 后缀（如"元"）
    rows?: number;          // textarea行数
    options?: Array<{       // select选项
      label: string;
      value: any;
    }>;
  };
  
  // 样式配置
  style?: {
    width?: string;
    marginBottom?: string;
    padding?: string;
  };
  
  // 自定义配置（未来扩展）
  custom?: Record<string, any>;
}
```

---

## 🔍 使用场景示例

### 场景1：计算规则配置

**配置示例：**

```json
{
  "targetFieldCode": "bank_total_cumulative",
  "targetFieldName": "银行",
  "groupName": "金融、类金融机构",
  "groupOrder": 1,
  "displayConfig": {
    "columnType": "total_cumulative",
    "format": {
      "type": "number",
      "precision": 0,
      "thousandSeparator": true
    },
    "ui": {
      "component": "text",
      "readonly": true
    }
  }
}
```

---

### 场景2：字段配置

**配置示例：**

```json
{
  "fieldCode": "market_dept_total_users_cumulative",
  "fieldName": "信息使用者机构总累计数",
  "displayConfig": {
    "groupName": "市场部填写",
    "groupOrder": 1,
    "layout": {
      "type": "row",
      "span": 12,
      "position": "left"
    },
    "ui": {
      "component": "input",
      "placeholder": "请输入总累计数",
      "suffix": "个"
    }
  }
}
```

---

## ✅ 方案评估总结

### 推荐方案：混合方案（核心字段 + JSON配置）

| 评估项 | 评分 | 说明 |
|--------|------|------|
| **扩展性** | ⭐⭐⭐⭐⭐ | JSON字段可以存储任意配置，无需修改表结构 |
| **灵活性** | ⭐⭐⭐⭐⭐ | 可以支持复杂的展示需求，类似低代码平台 |
| **性能** | ⭐⭐⭐⭐ | 核心字段保留为独立字段，查询性能好 |
| **可维护性** | ⭐⭐⭐⭐ | 配置集中，易于管理和迁移 |
| **类型安全** | ⭐⭐⭐ | 核心字段有类型安全，JSON字段需要手动解析 |

### 最终方案

**简化版方案（已确定）：**

1. **计算规则表**：
   - `targetFieldName`（VARCHAR）：字段名称（独立字段，便于查询和展示）
   - `displayConfig`（JSONB）：所有展示配置（JSON字段，便于扩展）

2. **字段配置表**：
   - `displayConfig`（JSONB）：所有展示配置（JSON字段，便于扩展）

**理由：**
- ✅ 简化设计，减少字段数量
- ✅ JSON字段存储所有展示配置，扩展性强
- ✅ PostgreSQL JSONB性能优秀，支持GIN索引
- ✅ 未来迁移到低代码平台时，JSON配置可以直接使用

---

## 📝 实施步骤

### Step 1: 数据库表结构修改

```sql
-- 计算规则表
ALTER TABLE credit_calculation_rule 
ADD COLUMN target_field_name VARCHAR(128) COMMENT '目标字段名称（用于前端显示）' AFTER target_field_code,
ADD COLUMN group_name VARCHAR(128) COMMENT '字段分组名称（用于前端分组展示）' AFTER target_field_name,
ADD COLUMN group_order INT DEFAULT 0 COMMENT '分组内显示顺序' AFTER group_name,
ADD COLUMN display_config JSON COMMENT '前端展示配置（JSON格式）' AFTER description;

-- 字段配置表
ALTER TABLE credit_field_config 
ADD COLUMN display_config JSON COMMENT '前端展示配置（JSON格式）' AFTER validation_rule;
```

### Step 2: 后端代码修改

1. 修改DO类（增加字段）
2. 修改VO类（增加字段）
3. 修改Service层（返回展示配置）
4. 修改Controller层（接收展示配置）

### Step 3: 前端对接

1. 前端接收 `displayConfig` JSON
2. 解析JSON配置
3. 根据配置渲染组件

---

**文档版本：** v1.0  
**最后更新：** 2024-01-XX

如有问题，请联系技术支持。
