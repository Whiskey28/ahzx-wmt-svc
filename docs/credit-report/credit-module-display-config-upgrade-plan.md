# 征信模块展示配置字段升级方案

## 📋 升级概述

**数据库：** PostgreSQL v15  
**升级目标：** 为计算规则表和字段配置表增加展示配置字段，支持前端动态展示

**升级内容：**
1. `credit_calculation_rule` 表：增加 `target_field_name` 和 `display_config` 字段
2. `credit_field_config` 表：增加 `display_config` 字段

---

## 🎯 升级方案设计

### 一、数据库表结构升级

#### 1.1 计算规则表升级

**表名：** `credit_calculation_rule`

**新增字段：**
- `target_field_name` VARCHAR(128) - 目标字段名称（用于前端显示）
- `display_config` JSONB - 前端展示配置（JSON格式）

**升级SQL：**

```sql
-- ============================================
-- 征信模块展示配置字段升级脚本
-- 数据库：PostgreSQL v15
-- 日期：2025-01-XX
-- ============================================

-- ----------------------------
-- 1. 计算规则表升级
-- ----------------------------
-- 1.1 增加目标字段名称字段
ALTER TABLE credit_calculation_rule 
ADD COLUMN IF NOT EXISTS target_field_name VARCHAR(128) NULL;

COMMENT ON COLUMN credit_calculation_rule.target_field_name IS '目标字段名称（用于前端显示）';

-- 1.2 增加前端展示配置字段（JSONB类型）
ALTER TABLE credit_calculation_rule 
ADD COLUMN IF NOT EXISTS display_config JSONB NULL;

COMMENT ON COLUMN credit_calculation_rule.display_config IS '前端展示配置（JSONB格式，存储字段分组、布局、样式等展示信息）';

-- 1.3 为 display_config 创建 GIN 索引（可选，如果需要对JSON字段进行查询）
CREATE INDEX IF NOT EXISTS idx_credit_calculation_rule_display_config_gin 
ON credit_calculation_rule USING GIN (display_config) 
WHERE deleted = 0 AND status = 1;
```

---

#### 1.2 字段配置表升级

**表名：** `credit_field_config`

**新增字段：**
- `display_config` JSONB - 前端展示配置（JSON格式）

**升级SQL：**

```sql
-- ----------------------------
-- 2. 字段配置表升级
-- ----------------------------
-- 2.1 增加前端展示配置字段（JSONB类型）
ALTER TABLE credit_field_config 
ADD COLUMN IF NOT EXISTS display_config JSONB NULL;

COMMENT ON COLUMN credit_field_config.display_config IS '前端展示配置（JSONB格式，存储字段分组、布局、样式等展示信息）';

-- 2.2 为 display_config 创建 GIN 索引（可选，如果需要对JSON字段进行查询）
CREATE INDEX IF NOT EXISTS idx_credit_field_config_display_config_gin 
ON credit_field_config USING GIN (display_config) 
WHERE deleted = 0 AND status = 1;
```

---

### 二、JSON配置结构设计

#### 2.1 计算规则展示配置（`display_config`）

**JSON结构：**

```json
{
  "groupName": "金融、类金融机构",
  "groupOrder": 1,
  "columnType": "total_cumulative",
  "layout": {
    "type": "row",
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
  },
  "custom": {
    // 未来扩展的配置项
  }
}
```

**简化版（推荐初始使用）：**

```json
{
  "groupName": "金融、类金融机构",
  "groupOrder": 1,
  "columnType": "total_cumulative"
}
```

**字段说明：**

| 字段 | 类型 | 说明 | 示例 |
|------|------|------|------|
| `groupName` | string | 字段分组名称 | "金融、类金融机构" |
| `groupOrder` | number | 分组内显示顺序 | 1 |
| `columnType` | string | 列类型（用于多列展示） | "total_cumulative" |
| `layout` | object | 布局配置 | 见下方 |
| `style` | object | 样式配置 | 见下方 |
| `format` | object | 格式化配置 | 见下方 |
| `ui` | object | UI组件配置 | 见下方 |
| `custom` | object | 自定义配置（未来扩展） | - |

**layout 对象：**

```json
{
  "type": "row",      // "row" | "column"
  "span": 12,         // 占位（1-24）
  "position": "left"  // "left" | "right" | "center"
}
```

**format 对象：**

```json
{
  "type": "number",              // "number" | "currency" | "percent" | "date"
  "precision": 0,                 // 小数位数
  "thousandSeparator": true,     // 千分位分隔符
  "currency": "CNY"              // 货币符号（currency类型时使用）
}
```

---

#### 2.2 字段配置展示配置（`display_config`）

**JSON结构：**

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
    "suffix": "个"
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

**简化版（推荐初始使用）：**

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

**字段说明：**

| 字段 | 类型 | 说明 | 示例 |
|------|------|------|------|
| `groupName` | string | 字段分组名称 | "市场部填写" |
| `groupOrder` | number | 分组内显示顺序 | 1 |
| `layout` | object | 布局配置 | 见上方 |
| `ui` | object | UI组件配置 | 见下方 |
| `style` | object | 样式配置 | 见下方 |
| `custom` | object | 自定义配置（未来扩展） | - |

**ui 对象：**

```json
{
  "component": "input",        // "input" | "textarea" | "select" | "date" | "number"
  "placeholder": "请输入",
  "prefix": "¥",              // 前缀（如"¥"）
  "suffix": "元",             // 后缀（如"元"）
  "rows": 3,                  // textarea行数
  "options": [                // select选项
    {"label": "选项1", "value": "1"},
    {"label": "选项2", "value": "2"}
  ]
}
```

---

### 三、后端代码升级

#### 3.1 修改 DO 类

**`CreditCalculationRuleDO`：**

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

**`CreditFieldConfigDO`：**

```java
/**
 * 前端展示配置（JSONB格式，存储字段分组、布局、样式等展示信息）
 * 例如：{"groupName": "市场部填写", "groupOrder": 1, "layout": {...}, ...}
 */
@TableField(typeHandler = JacksonTypeHandler.class)
private Map<String, Object> displayConfig;  // ✅ 新增
```

---

#### 3.2 修改 VO 类

**`CreditCalculationRuleSaveReqVO`：**

```java
@Schema(description = "目标字段名称（用于前端显示）")
private String targetFieldName;  // ✅ 新增

@Schema(description = "前端展示配置（JSON格式）")
private Map<String, Object> displayConfig;  // ✅ 新增
```

**`CreditCalculationRuleRespVO`：**

```java
@Schema(description = "目标字段名称（用于前端显示）")
private String targetFieldName;  // ✅ 新增

@Schema(description = "前端展示配置（JSON格式）")
private Map<String, Object> displayConfig;  // ✅ 新增
```

**`CreditFieldConfigSaveReqVO`：**

```java
@Schema(description = "前端展示配置（JSON格式）")
private Map<String, Object> displayConfig;  // ✅ 新增
```

**`CreditFieldConfigRespVO`：**

```java
@Schema(description = "前端展示配置（JSON格式）")
private Map<String, Object> displayConfig;  // ✅ 新增
```

---

#### 3.3 修改 Service 层

**`CreditSummaryReportService`：**

在查询汇总报表时，返回字段元数据（包含展示配置）：

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

#### 3.4 新增 FieldMetadataVO

**创建 `FieldMetadataVO`：**

```java
package com.wmt.module.credit.controller.admin.report.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Map;

/**
 * 字段元数据 VO（用于前端展示）
 */
@Schema(description = "字段元数据 VO")
@Data
public class FieldMetadataVO {

    @Schema(description = "字段编码", requiredMode = Schema.RequiredMode.REQUIRED)
    private String fieldCode;

    @Schema(description = "字段名称", requiredMode = Schema.RequiredMode.REQUIRED)
    private String fieldName;

    @Schema(description = "前端展示配置（JSON格式）")
    private Map<String, Object> displayConfig;

}
```

---

### 四、升级脚本完整版

**文件：** `sql/postgresql/credit-module-upgrade-v1.1.sql`

```sql
-- ============================================
-- 征信模块展示配置字段升级脚本
-- 版本：v1.1
-- 数据库：PostgreSQL v15
-- 日期：2025-01-XX
-- 说明：为计算规则表和字段配置表增加展示配置字段
-- ============================================

BEGIN;

-- ----------------------------
-- 1. 计算规则表升级
-- ----------------------------

-- 1.1 增加目标字段名称字段
ALTER TABLE credit_calculation_rule 
ADD COLUMN IF NOT EXISTS target_field_name VARCHAR(128) NULL;

COMMENT ON COLUMN credit_calculation_rule.target_field_name IS '目标字段名称（用于前端显示）';

-- 1.2 增加前端展示配置字段（JSONB类型）
ALTER TABLE credit_calculation_rule 
ADD COLUMN IF NOT EXISTS display_config JSONB NULL;

COMMENT ON COLUMN credit_calculation_rule.display_config IS '前端展示配置（JSONB格式，存储字段分组、布局、样式等展示信息）';

-- 1.3 为 display_config 创建 GIN 索引（可选，如果需要对JSON字段进行查询）
CREATE INDEX IF NOT EXISTS idx_credit_calculation_rule_display_config_gin 
ON credit_calculation_rule USING GIN (display_config) 
WHERE deleted = 0 AND status = 1;

-- ----------------------------
-- 2. 字段配置表升级
-- ----------------------------

-- 2.1 增加前端展示配置字段（JSONB类型）
ALTER TABLE credit_field_config 
ADD COLUMN IF NOT EXISTS display_config JSONB NULL;

COMMENT ON COLUMN credit_field_config.display_config IS '前端展示配置（JSONB格式，存储字段分组、布局、样式等展示信息）';

-- 2.2 为 display_config 创建 GIN 索引（可选，如果需要对JSON字段进行查询）
CREATE INDEX IF NOT EXISTS idx_credit_field_config_display_config_gin 
ON credit_field_config USING GIN (display_config) 
WHERE deleted = 0 AND status = 1;

COMMIT;

-- ============================================
-- 升级完成
-- ============================================
```

---

### 五、回滚脚本（可选）

**文件：** `sql/postgresql/credit-module-rollback-v1.1.sql`

```sql
-- ============================================
-- 征信模块展示配置字段回滚脚本
-- 版本：v1.1
-- 数据库：PostgreSQL v15
-- 日期：2025-01-XX
-- 说明：回滚展示配置字段升级
-- ============================================

BEGIN;

-- ----------------------------
-- 1. 计算规则表回滚
-- ----------------------------

-- 1.1 删除索引
DROP INDEX IF EXISTS idx_credit_calculation_rule_display_config_gin;

-- 1.2 删除字段
ALTER TABLE credit_calculation_rule 
DROP COLUMN IF EXISTS display_config;

ALTER TABLE credit_calculation_rule 
DROP COLUMN IF EXISTS target_field_name;

-- ----------------------------
-- 2. 字段配置表回滚
-- ----------------------------

-- 2.1 删除索引
DROP INDEX IF EXISTS idx_credit_field_config_display_config_gin;

-- 2.2 删除字段
ALTER TABLE credit_field_config 
DROP COLUMN IF EXISTS display_config;

COMMIT;

-- ============================================
-- 回滚完成
-- ============================================
```

---

### 六、升级检查清单

#### 6.1 升级前检查

- [ ] 备份数据库
- [ ] 确认数据库版本为 PostgreSQL v15
- [ ] 确认表 `credit_calculation_rule` 和 `credit_field_config` 存在
- [ ] 确认没有未提交的事务

#### 6.2 升级执行

- [ ] 执行升级SQL脚本
- [ ] 检查字段是否成功添加
- [ ] 检查索引是否成功创建
- [ ] 验证现有数据完整性

#### 6.3 升级后验证

- [ ] 验证字段可以正常插入数据
- [ ] 验证JSON字段可以正常存储和查询
- [ ] 验证后端代码可以正常读取JSON字段
- [ ] 验证前端可以正常接收和解析JSON配置

---

### 七、数据迁移建议

#### 7.1 现有数据迁移

**如果现有计算规则需要设置字段名称：**

```sql
-- 示例：为现有计算规则设置字段名称（根据实际业务调整）
UPDATE credit_calculation_rule 
SET target_field_name = '总资产'
WHERE target_field_code = 'total_assets_sum' 
  AND deleted = 0;
```

**如果现有字段配置需要设置展示配置：**

```sql
-- 示例：为现有字段配置设置展示配置（根据实际业务调整）
UPDATE credit_field_config 
SET display_config = '{"groupName": "通用字段", "groupOrder": 1}'::jsonb
WHERE field_code = 'total_assets' 
  AND deleted = 0;
```

---

### 八、PostgreSQL JSONB 特性说明

#### 8.1 JSONB vs JSON

**PostgreSQL 支持两种JSON类型：**
- `JSON`：存储原始JSON文本，保留空格和键顺序
- `JSONB`：存储二进制格式，去除空格，键顺序不固定，查询性能更好

**推荐使用 JSONB：**
- ✅ 查询性能更好
- ✅ 支持GIN索引
- ✅ 支持更多操作符

#### 8.2 JSONB 查询示例

```sql
-- 查询 display_config 中的 groupName
SELECT target_field_code, display_config->>'groupName' as group_name
FROM credit_calculation_rule
WHERE deleted = 0;

-- 查询 display_config 中的 groupOrder（数字类型）
SELECT target_field_code, (display_config->>'groupOrder')::int as group_order
FROM credit_calculation_rule
WHERE deleted = 0;

-- 使用 JSONB 操作符查询
SELECT * FROM credit_calculation_rule
WHERE display_config @> '{"groupName": "金融、类金融机构"}'::jsonb
  AND deleted = 0;
```

#### 8.3 GIN 索引使用

**GIN索引支持的操作符：**
- `@>`：包含
- `?`：键是否存在
- `?&`：所有键都存在
- `?|`：任意键存在

**示例：**

```sql
-- 使用 GIN 索引查询（性能更好）
SELECT * FROM credit_calculation_rule
WHERE display_config @> '{"groupName": "金融、类金融机构"}'::jsonb
  AND deleted = 0;
```

---

## 📝 实施步骤

### Step 1: 数据库升级

1. **备份数据库**
   ```bash
   pg_dump -U username -d database_name > backup_before_upgrade.sql
   ```

2. **执行升级脚本**
   ```bash
   psql -U username -d database_name -f sql/postgresql/credit-module-upgrade-v1.1.sql
   ```

3. **验证升级结果**
   ```sql
   -- 检查字段是否添加成功
   SELECT column_name, data_type 
   FROM information_schema.columns 
   WHERE table_name = 'credit_calculation_rule' 
     AND column_name IN ('target_field_name', 'display_config');
   
   SELECT column_name, data_type 
   FROM information_schema.columns 
   WHERE table_name = 'credit_field_config' 
     AND column_name = 'display_config';
   ```

---

### Step 2: 后端代码修改（已完成）

✅ **已完成的修改：**

1. ✅ 修改 `CreditCalculationRuleDO` - 增加 `targetFieldName` 和 `displayConfig`
2. ✅ 修改 `CreditFieldConfigDO` - 增加 `displayConfig`
3. ✅ 修改 `CreditCalculationRuleSaveReqVO` - 增加字段
4. ✅ 修改 `CreditCalculationRuleRespVO` - 增加字段
5. ✅ 修改 `CreditFieldConfigSaveReqVO` - 增加字段
6. ✅ 修改 `CreditFieldConfigRespVO` - 增加字段
7. ✅ 修改 `CreditSummaryReportRespVO` - 增加 `fieldMetadata`
8. ✅ 创建 `FieldMetadataVO` - 新增字段元数据VO
9. ✅ 修改 `CreditSummaryReportController` - 查询时返回字段元数据

---

### Step 3: 测试验证

1. **单元测试**
   - 测试字段插入和查询
   - 测试JSON字段的序列化和反序列化

2. **集成测试**
   - 测试计算规则配置的创建和更新
   - 测试字段配置的创建和更新
   - 测试汇总报表查询时返回字段元数据

3. **功能测试**
   - 测试前端可以正常接收和解析JSON配置
   - 测试展示效果符合预期

---

## ✅ 升级方案总结

### 升级内容

| 表名 | 新增字段 | 字段类型 | 说明 |
|------|---------|---------|------|
| `credit_calculation_rule` | `target_field_name` | VARCHAR(128) | 目标字段名称 |
| `credit_calculation_rule` | `display_config` | JSONB | 前端展示配置 |
| `credit_field_config` | `display_config` | JSONB | 前端展示配置 |

### 优势

1. ✅ **扩展性强**：JSON字段可以存储任意配置，无需修改表结构
2. ✅ **性能好**：PostgreSQL JSONB + GIN索引性能优秀
3. ✅ **灵活度高**：支持复杂的展示需求，类似低代码平台
4. ✅ **易于迁移**：未来迁移到低代码平台时，JSON配置可以直接使用

---

**文档版本：** v1.0  
**最后更新：** 2025-01-XX

如有问题，请联系技术支持。
