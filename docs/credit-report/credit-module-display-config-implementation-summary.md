# 征信模块展示配置字段实施总结

## 📋 实施概述

**实施日期：** 2025-01-XX  
**数据库：** PostgreSQL v15  
**升级版本：** v1.1

**实施内容：**
1. 为 `credit_calculation_rule` 表增加 `target_field_name` 和 `display_config` 字段
2. 为 `credit_field_config` 表增加 `display_config` 字段
3. 修改后端代码支持新字段

---

## ✅ 已完成的修改

### 一、数据库升级脚本

**文件：** `sql/postgresql/credit-module-upgrade-v1.1.sql`

**修改内容：**
- ✅ 计算规则表：增加 `target_field_name` (VARCHAR(128))
- ✅ 计算规则表：增加 `display_config` (JSONB)
- ✅ 字段配置表：增加 `display_config` (JSONB)
- ✅ 为 `display_config` 字段创建 GIN 索引

---

### 二、后端代码修改

#### 2.1 DO 类修改

**`CreditCalculationRuleDO`：**
- ✅ 增加 `targetFieldName` 字段
- ✅ 增加 `displayConfig` 字段（使用 `JacksonTypeHandler`）

**`CreditFieldConfigDO`：**
- ✅ 增加 `displayConfig` 字段（使用 `JacksonTypeHandler`）

---

#### 2.2 VO 类修改

**`CreditCalculationRuleSaveReqVO`：**
- ✅ 增加 `targetFieldName` 字段
- ✅ 增加 `displayConfig` 字段

**`CreditCalculationRuleRespVO`：**
- ✅ 增加 `targetFieldName` 字段
- ✅ 增加 `displayConfig` 字段

**`CreditFieldConfigSaveReqVO`：**
- ✅ 增加 `displayConfig` 字段

**`CreditFieldConfigRespVO`：**
- ✅ 增加 `displayConfig` 字段

**`CreditSummaryReportRespVO`：**
- ✅ 增加 `fieldMetadata` 字段（`List<FieldMetadataVO>`）

---

#### 2.3 新增 VO 类

**`FieldMetadataVO`：**
- ✅ 新增字段元数据VO，包含：
  - `fieldCode`：字段编码
  - `fieldName`：字段名称
  - `displayConfig`：展示配置

---

#### 2.4 Controller 修改

**`CreditSummaryReportController`：**
- ✅ 在 `getSummaryReport` 方法中查询字段元数据
- ✅ 将字段元数据设置到响应VO中

---

### 三、JSON配置结构

#### 3.1 计算规则展示配置

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
```

#### 3.2 字段配置展示配置

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
    "suffix": "个"
  }
}
```

---

## 📝 实施步骤

### Step 1: 执行数据库升级脚本

```bash
# 连接PostgreSQL数据库
psql -U username -d database_name

# 执行升级脚本
\i sql/postgresql/credit-module-upgrade-v1.1.sql

# 验证升级结果
SELECT column_name, data_type 
FROM information_schema.columns 
WHERE table_name = 'credit_calculation_rule' 
  AND column_name IN ('target_field_name', 'display_config');
```

---

### Step 2: 验证代码修改

1. **编译项目**
   ```bash
   mvn clean compile
   ```

2. **运行单元测试**
   ```bash
   mvn test
   ```

---

### Step 3: 测试功能

1. **测试计算规则配置**
   - 创建计算规则，设置 `targetFieldName` 和 `displayConfig`
   - 查询计算规则，验证字段是否正确返回

2. **测试字段配置**
   - 创建字段配置，设置 `displayConfig`
   - 查询字段配置，验证字段是否正确返回

3. **测试汇总报表查询**
   - 生成汇总报表
   - 查询汇总报表，验证 `fieldMetadata` 是否正确返回

---

## 🔍 接口返回示例

### 汇总报表查询接口返回

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
    "fieldMetadata": [
      {
        "fieldCode": "bank_total_cumulative",
        "fieldName": "银行",
        "displayConfig": {
          "groupName": "金融、类金融机构",
          "groupOrder": 1,
          "columnType": "total_cumulative"
        }
      },
      {
        "fieldCode": "securities_total_cumulative",
        "fieldName": "证券",
        "displayConfig": {
          "groupName": "金融、类金融机构",
          "groupOrder": 2,
          "columnType": "total_cumulative"
        }
      }
    ],
    "status": 1,
    "generateTime": "2025-12-01T10:30:00"
  },
  "msg": "操作成功"
}
```

---

## ✅ 验收标准

### 数据库验收

- [ ] `credit_calculation_rule` 表存在 `target_field_name` 字段
- [ ] `credit_calculation_rule` 表存在 `display_config` 字段（JSONB类型）
- [ ] `credit_field_config` 表存在 `display_config` 字段（JSONB类型）
- [ ] GIN索引创建成功

### 功能验收

- [ ] 可以创建计算规则，设置 `targetFieldName` 和 `displayConfig`
- [ ] 可以创建字段配置，设置 `displayConfig`
- [ ] 查询汇总报表时，`fieldMetadata` 正确返回
- [ ] JSON字段可以正常存储和读取

---

## 📊 修改文件清单

### 数据库脚本

1. ✅ `sql/postgresql/credit-module-upgrade-v1.1.sql` - 升级脚本

### Java 代码

#### DO 类（2个）

1. ✅ `CreditCalculationRuleDO.java` - 增加 `targetFieldName` 和 `displayConfig`
2. ✅ `CreditFieldConfigDO.java` - 增加 `displayConfig`

#### VO 类（6个）

1. ✅ `CreditCalculationRuleSaveReqVO.java` - 增加字段
2. ✅ `CreditCalculationRuleRespVO.java` - 增加字段
3. ✅ `CreditFieldConfigSaveReqVO.java` - 增加字段
4. ✅ `CreditFieldConfigRespVO.java` - 增加字段
5. ✅ `CreditSummaryReportRespVO.java` - 增加 `fieldMetadata`
6. ✅ `FieldMetadataVO.java` - 新增

#### Controller（1个）

1. ✅ `CreditSummaryReportController.java` - 修改查询方法

---

## 🎯 后续工作

### 前端对接

1. **接收字段元数据**
   - 前端调用 `/credit/summary-report/get` 接口
   - 解析 `fieldMetadata` 数组

2. **解析展示配置**
   - 从 `displayConfig` JSON中提取配置信息
   - 根据配置渲染组件

3. **实现分组展示**
   - 根据 `displayConfig.groupName` 分组
   - 根据 `displayConfig.groupOrder` 排序

---

## 📝 注意事项

1. **PostgreSQL JSONB 特性**
   - 使用 `JSONB` 类型（不是 `JSON`），性能更好
   - 支持 GIN 索引，查询性能优秀
   - 使用 `JacksonTypeHandler` 自动处理序列化和反序列化

2. **字段名称获取**
   - 计算规则的字段名称从 `targetFieldName` 独立字段获取
   - 展示配置从 `displayConfig` JSON字段获取

3. **向后兼容**
   - 新增字段都是可空的（NULL），不影响现有数据
   - 现有功能不受影响

---

**文档版本：** v1.0  
**最后更新：** 2025-01-XX

如有问题，请联系技术支持。
