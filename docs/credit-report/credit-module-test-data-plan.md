# 征信模块测试数据方案评估

## 📋 测试目标

**目标：** 通过SQL脚本准备测试数据，然后调用接口验证系统能力

**验证场景：**
1. ✅ 月报生成：生成2025-01月的月报
2. ✅ 季报生成：生成2025-Q1的季报（需要先有3个月的月报）
3. ✅ 报表查询：查询报表详情，验证字段元数据返回
4. ✅ Excel导出：导出报表Excel

---

## 🎯 测试数据设计方案

### 一、测试场景设计

#### 场景1：月报测试

**测试周期：** 2025-01（2025年1月）

**数据准备：**
- 3个部门的字段配置（市场部、技术部、财务部）
- 3个部门2025-01月的表单数据（已提交状态）
- 月报计算规则（包含展示配置）

**验证点：**
- 可以生成月报
- 月报数据正确计算
- 字段元数据正确返回

---

#### 场景2：季报测试

**测试周期：** 2025-Q1（2025年第一季度）

**数据准备：**
- 3个部门2025-01、2025-02、2025-03三个月的表单数据
- 3个月的月报数据（已生成）
- 季报计算规则（包含展示配置）

**验证点：**
- 可以生成季报
- 季报数据正确计算（基于月报数据）
- 字段元数据正确返回

---

### 二、数据结构设计

#### 2.1 部门设计

**假设部门：**
- 部门1（dept_id = 1）：市场部
- 部门2（dept_id = 2）：技术部
- 部门3（dept_id = 3）：财务部

---

#### 2.2 字段配置设计

**通用字段（dept_id = 0）：**
- `total_users_cumulative`：信息使用者机构总累计数（NUMBER）
- `current_users`：当前使用服务的信息使用者机构总数（NUMBER）
- `products_provided`：当年提供产品(服务)次数（NUMBER）

**部门字段（每个部门都有）：**
- `bank_total_cumulative`：银行总累计数（NUMBER）
- `securities_total_cumulative`：证券总累计数（NUMBER）
- `insurance_total_cumulative`：保险总累计数（NUMBER）

**字段展示配置（display_config）：**
- 通用字段：`{"groupName": "通用信息", "groupOrder": 1}`
- 部门字段：`{"groupName": "金融、类金融机构", "groupOrder": 1}`

---

#### 2.3 表单数据设计

**表单数据字段（form_data JSON）：**
```json
{
  "total_users_cumulative": 100,
  "current_users": 80,
  "products_provided": 50,
  "bank_total_cumulative": 30,
  "securities_total_cumulative": 10,
  "insurance_total_cumulative": 5
}
```

**不同部门的数据值：**
- 市场部：数值较大（模拟主要业务部门）
- 技术部：数值中等
- 财务部：数值较小

---

#### 2.4 计算规则设计

**月报计算规则：**

| targetFieldCode | targetFieldName | 计算表达式 | groupName | columnType |
|----------------|----------------|-----------|-----------|------------|
| `bank_total_sum` | 银行合计 | `#sum([#dept1['bank_total_cumulative'], #dept2['bank_total_cumulative'], #dept3['bank_total_cumulative']])` | 金融、类金融机构 | total_cumulative |
| `securities_total_sum` | 证券合计 | `#sum([#dept1['securities_total_cumulative'], #dept2['securities_total_cumulative'], #dept3['securities_total_cumulative']])` | 金融、类金融机构 | total_cumulative |
| `insurance_total_sum` | 保险合计 | `#sum([#dept1['insurance_total_cumulative'], #dept2['insurance_total_cumulative'], #dept3['insurance_total_cumulative']])` | 金融、类金融机构 | total_cumulative |
| `total_users_sum` | 信息使用者机构总累计数 | `#sum([#dept1['total_users_cumulative'], #dept2['total_users_cumulative'], #dept3['total_users_cumulative']])` | 通用信息 | total_cumulative |
| `current_users_sum` | 当前使用服务的总数 | `#sum([#dept1['current_users'], #dept2['current_users'], #dept3['current_users']])` | 通用信息 | current_users |
| `products_provided_sum` | 当年提供产品次数合计 | `#sum([#dept1['products_provided'], #dept2['products_provided'], #dept3['products_provided']])` | 通用信息 | products_provided |

**季报计算规则：**

| targetFieldCode | targetFieldName | 计算表达式 | groupName | columnType |
|----------------|----------------|-----------|-----------|------------|
| `bank_total_q1` | 银行Q1合计 | `#sum([#safeGet(monthlyReports['2025-01'], 'bank_total_sum'), #safeGet(monthlyReports['2025-02'], 'bank_total_sum'), #safeGet(monthlyReports['2025-03'], 'bank_total_sum')])` | 金融、类金融机构 | total_cumulative |
| `total_users_q1` | 信息使用者机构Q1合计 | `#sum([#safeGet(monthlyReports['2025-01'], 'total_users_sum'), #safeGet(monthlyReports['2025-02'], 'total_users_sum'), #safeGet(monthlyReports['2025-03'], 'total_users_sum')])` | 通用信息 | total_cumulative |

---

### 三、SQL脚本结构设计

#### 3.1 脚本结构

```sql
-- ============================================
-- 征信模块测试数据脚本
-- 数据库：PostgreSQL v15
-- 日期：2025-01-XX
-- ============================================

BEGIN;

-- 1. 清理旧数据（可选）
-- DELETE FROM credit_summary_report WHERE report_period IN ('2025-01', '2025-02', '2025-03', '2025-Q1');
-- DELETE FROM credit_form_data WHERE report_period IN ('2025-01', '2025-02', '2025-03');
-- DELETE FROM credit_calculation_rule WHERE report_type IN ('MONTHLY', 'QUARTERLY');
-- DELETE FROM credit_field_config;

-- 2. 插入字段配置
-- 2.1 通用字段配置
-- 2.2 部门字段配置

-- 3. 插入表单数据
-- 3.1 2025-01月表单数据（3个部门）
-- 3.2 2025-02月表单数据（3个部门）
-- 3.3 2025-03月表单数据（3个部门）

-- 4. 插入计算规则
-- 4.1 月报计算规则
-- 4.2 季报计算规则

COMMIT;

-- 5. 验证数据
-- SELECT 语句验证数据完整性
```

---

#### 3.2 数据值设计

**部门1（市场部）数据：**
- `total_users_cumulative`: 50
- `current_users`: 40
- `products_provided`: 30
- `bank_total_cumulative`: 20
- `securities_total_cumulative`: 8
- `insurance_total_cumulative`: 3

**部门2（技术部）数据：**
- `total_users_cumulative`: 30
- `current_users`: 25
- `products_provided`: 15
- `bank_total_cumulative`: 10
- `securities_total_cumulative`: 5
- `insurance_total_cumulative`: 2

**部门3（财务部）数据：**
- `total_users_cumulative`: 20
- `current_users`: 15
- `products_provided`: 5
- `bank_total_cumulative`: 5
- `securities_total_cumulative`: 2
- `insurance_total_cumulative`: 1

**预期计算结果（月报）：**
- `bank_total_sum`: 20 + 10 + 5 = 35
- `securities_total_sum`: 8 + 5 + 2 = 15
- `insurance_total_sum`: 3 + 2 + 1 = 6
- `total_users_sum`: 50 + 30 + 20 = 100
- `current_users_sum`: 40 + 25 + 15 = 80
- `products_provided_sum`: 30 + 15 + 5 = 50

---

### 四、接口调用流程

#### 4.1 月报测试流程

```bash
# 1. 执行SQL脚本准备数据
psql -U username -d database_name -f sql/postgresql/credit-module-test-data.sql

# 2. 生成月报
curl -X POST http://localhost:8080/credit/summary-report/generate \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer {token}" \
  -d '{
    "reportPeriod": "2025-01",
    "reportType": "MONTHLY"
  }'

# 3. 查询月报详情（验证字段元数据）
curl -X GET "http://localhost:8080/credit/summary-report/get?id={reportId}" \
  -H "Authorization: Bearer {token}"

# 4. 导出Excel
curl -X GET "http://localhost:8080/credit/summary-report/export-excel?reportPeriod=2025-01&reportType=MONTHLY" \
  -H "Authorization: Bearer {token}" \
  -o report.xls
```

---

#### 4.2 季报测试流程

```bash
# 1. 先生成3个月的月报
curl -X POST http://localhost:8080/credit/summary-report/generate \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer {token}" \
  -d '{"reportPeriod": "2025-01", "reportType": "MONTHLY"}'

curl -X POST http://localhost:8080/credit/summary-report/generate \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer {token}" \
  -d '{"reportPeriod": "2025-02", "reportType": "MONTHLY"}'

curl -X POST http://localhost:8080/credit/summary-report/generate \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer {token}" \
  -d '{"reportPeriod": "2025-03", "reportType": "MONTHLY"}'

# 2. 生成季报
curl -X POST http://localhost:8080/credit/summary-report/generate \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer {token}" \
  -d '{
    "reportPeriod": "2025-Q1",
    "reportType": "QUARTERLY"
  }'

# 3. 查询季报详情
curl -X GET "http://localhost:8080/credit/summary-report/get?id={reportId}" \
  -H "Authorization: Bearer {token}"

# 4. 导出Excel
curl -X GET "http://localhost:8080/credit/summary-report/export-excel?reportPeriod=2025-Q1&reportType=QUARTERLY" \
  -H "Authorization: Bearer {token}" \
  -o report-q1.xls
```

---

### 五、验证检查清单

#### 5.1 数据完整性检查

**SQL验证：**
```sql
-- 检查字段配置数量
SELECT COUNT(*) FROM credit_field_config WHERE deleted = 0;
-- 预期：至少6条（3个通用字段 + 3个部门字段）

-- 检查表单数据数量
SELECT COUNT(*) FROM credit_form_data WHERE deleted = 0 AND status = 1;
-- 预期：9条（3个部门 × 3个月）

-- 检查计算规则数量
SELECT COUNT(*) FROM credit_calculation_rule WHERE deleted = 0 AND status = 1;
-- 预期：至少6条（月报规则）+ 2条（季报规则）
```

---

#### 5.2 接口返回验证

**月报查询返回验证：**
```json
{
  "code": 0,
  "data": {
    "id": 1,
    "reportPeriod": "2025-01",
    "reportType": "MONTHLY",
    "reportData": {
      "bank_total_sum": 35,
      "securities_total_sum": 15,
      "insurance_total_sum": 6,
      "total_users_sum": 100,
      "current_users_sum": 80,
      "products_provided_sum": 50
    },
    "fieldMetadata": [
      {
        "fieldCode": "bank_total_sum",
        "fieldName": "银行合计",
        "displayConfig": {
          "groupName": "金融、类金融机构",
          "groupOrder": 1,
          "columnType": "total_cumulative"
        }
      },
      ...
    ]
  }
}
```

---

## ✅ 方案评估

### 优点

1. ✅ **数据完整**：覆盖字段配置、表单数据、计算规则
2. ✅ **场景完整**：支持月报和季报测试
3. ✅ **易于验证**：数据值设计简单，便于人工验证计算结果
4. ✅ **可重复执行**：SQL脚本可以多次执行（使用DELETE清理旧数据）

### 注意事项

1. ⚠️ **序列号管理**：PostgreSQL使用序列，需要确保序列值正确
2. ⚠️ **时间戳**：需要设置合理的创建时间和更新时间
3. ⚠️ **租户ID**：需要根据实际系统设置租户ID（默认0）
4. ⚠️ **用户ID**：提交人ID需要根据实际系统设置

---

## 📝 实施建议

### 阶段1：基础数据准备

1. 插入字段配置（通用字段 + 部门字段）
2. 插入表单数据（3个部门 × 3个月）
3. 插入计算规则（月报 + 季报）

### 阶段2：测试执行

1. 执行SQL脚本
2. 调用月报生成接口
3. 验证月报数据
4. 调用季报生成接口
5. 验证季报数据

### 阶段3：结果验证

1. 验证报表数据正确性
2. 验证字段元数据返回
3. 验证Excel导出功能

---

## 🎯 待确认事项

1. **部门ID**：实际系统中的部门ID是多少？是否需要创建部门数据？
2. **用户ID**：提交人ID（submit_user_id）如何设置？
3. **租户ID**：租户ID（tenant_id）如何设置？
4. **数据值**：测试数据值是否符合业务场景？
5. **计算规则**：计算表达式是否符合实际业务需求？

---

**文档版本：** v1.0  
**最后更新：** 2025-01-XX

请确认方案后，我将生成完整的SQL脚本。
