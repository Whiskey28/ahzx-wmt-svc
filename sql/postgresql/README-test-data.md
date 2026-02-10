# 征信模块测试数据使用说明

## 📋 脚本说明

### 1. 备份脚本：`credit-module-backup.sql`

**用途：** 在测试前备份现有数据，用于测试后回退

**执行方式：**
```bash
psql -U username -d database_name -f sql/postgresql/credit-module-backup.sql
```

**功能：**
- 创建备份表（`*_backup`）
- 备份字段配置、表单数据、计算规则、汇总报表
- 备份序列值

---

### 2. 测试数据脚本：`credit-module-test-data.sql`

**用途：** 准备测试数据，支持月报和季报生成测试

**执行方式：**
```bash
psql -U username -d database_name -f sql/postgresql/credit-module-test-data.sql
```

**数据内容：**
- **字段配置**：5个通用字段（对应5个项目）+ 3个部门的字段配置
- **表单数据**：3个部门 × 6个月 = 18条记录
  - 2025-01、2025-02、2025-03（当前季度）
  - 2024-10、2024-11、2024-12（历史数据，用于同期对比）
- **计算规则**：
  - 月报规则：10条（5个项目 × 2列：当期发生额、累计发生额）
  - 季报规则：8条（2个项目 × 4列：当期发生额、累计发生额、同期增长量、同期增长率）

**数据值特点：**
- 数据值较大（符合业务场景）
- 涉及乘法和除法运算
- 支持跨期数据引用（2025S3、2024S4）

---

### 3. 恢复脚本：`credit-module-restore.sql`

**用途：** 从备份表恢复数据，用于测试后回退

**执行方式：**
```bash
psql -U username -d database_name -f sql/postgresql/credit-module-restore.sql
```

**功能：**
- 删除测试数据
- 从备份表恢复原始数据
- 恢复序列值

---

## 🚀 测试流程

### Step 1: 备份数据

```bash
psql -U username -d database_name -f sql/postgresql/credit-module-backup.sql
```

---

### Step 2: 准备测试数据

```bash
psql -U username -d database_name -f sql/postgresql/credit-module-test-data.sql
```

**验证数据：**
```sql
-- 检查数据完整性
SELECT '字段配置数量' as check_item, COUNT(*) as count FROM credit_field_config WHERE deleted = 0
UNION ALL
SELECT '表单数据数量', COUNT(*) FROM credit_form_data WHERE deleted = 0 AND status = 1
UNION ALL
SELECT '月报计算规则数量', COUNT(*) FROM credit_calculation_rule WHERE deleted = 0 AND status = 1 AND report_type = 'MONTHLY'
UNION ALL
SELECT '季报计算规则数量', COUNT(*) FROM credit_calculation_rule WHERE deleted = 0 AND status = 1 AND report_type = 'QUARTERLY';
```

---

### Step 3: 生成月报

**生成2025-01月报：**
```bash
curl -X POST http://localhost:8080/credit/summary-report/generate \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer {token}" \
  -d '{
    "reportPeriod": "2025-01",
    "reportType": "MONTHLY"
  }'
```

**生成2025-02月报：**
```bash
curl -X POST http://localhost:8080/credit/summary-report/generate \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer {token}" \
  -d '{
    "reportPeriod": "2025-02",
    "reportType": "MONTHLY"
  }'
```

**生成2025-03月报：**
```bash
curl -X POST http://localhost:8080/credit/summary-report/generate \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer {token}" \
  -d '{
    "reportPeriod": "2025-03",
    "reportType": "MONTHLY"
  }'
```

---

### Step 4: 生成历史季度报表（用于同期对比）

**生成2024-Q4季报（需要先有2024-10、2024-11、2024-12的月报）：**

```bash
# 先生成3个月的月报
curl -X POST http://localhost:8080/credit/summary-report/generate \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer {token}" \
  -d '{"reportPeriod": "2024-10", "reportType": "MONTHLY"}'

curl -X POST http://localhost:8080/credit/summary-report/generate \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer {token}" \
  -d '{"reportPeriod": "2024-11", "reportType": "MONTHLY"}'

curl -X POST http://localhost:8080/credit/summary-report/generate \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer {token}" \
  -d '{"reportPeriod": "2024-12", "reportType": "MONTHLY"}'

# 生成2024-Q4季报
curl -X POST http://localhost:8080/credit/summary-report/generate \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer {token}" \
  -d '{
    "reportPeriod": "2024-Q4",
    "reportType": "QUARTERLY"
  }'
```

**注意：** 2024-Q4季报将作为 `previousQuarterReport` 用于2025-Q1的同期对比计算。

---

### Step 5: 生成2025-Q1季报

```bash
curl -X POST http://localhost:8080/credit/summary-report/generate \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer {token}" \
  -d '{
    "reportPeriod": "2025-Q1",
    "reportType": "QUARTERLY"
  }'
```

---

### Step 6: 查询报表详情（验证字段元数据）

```bash
curl -X GET "http://localhost:8080/credit/summary-report/get?id={reportId}" \
  -H "Authorization: Bearer {token}"
```

**预期返回：**
```json
{
  "code": 0,
  "data": {
    "id": 1,
    "reportPeriod": "2025-Q1",
    "reportType": "QUARTERLY",
    "reportData": {
      "small_micro_enterprises_q1_current": 12345,
      "small_micro_enterprises_q1_cumulative": 405000,
      "small_micro_enterprises_q1_growth": 1234,
      "small_micro_enterprises_q1_growth_rate": 0.1234,
      ...
    },
    "fieldMetadata": [
      {
        "fieldCode": "small_micro_enterprises_q1_current",
        "fieldName": "数据库收录小微企业户数-当期发生额",
        "displayConfig": {
          "groupName": "数据库收录小微企业户数",
          "groupOrder": 1,
          "columnType": "current_period"
        }
      },
      ...
    ]
  }
}
```

---

### Step 7: 导出Excel

```bash
curl -X GET "http://localhost:8080/credit/summary-report/export-excel?reportPeriod=2025-Q1&reportType=QUARTERLY" \
  -H "Authorization: Bearer {token}" \
  -o report-q1.xls
```

---

### Step 8: 恢复数据（测试完成后）

```bash
psql -U username -d database_name -f sql/postgresql/credit-module-restore.sql
```

---

## 📊 测试数据说明

### 字段配置

**5个通用字段（对应5个项目）：**
1. `small_micro_enterprises_count`：小微企业户数
2. `credit_service_calls`：征信服务调用总次数
3. `loan_applications_count`：申请进件企业数
4. `loan_approval_rate`：企业贷款申请通过率（小数，如0.75表示75%）
5. `avg_loan_amount`：已通过的每一户的平均放款（金额）

**部门字段：** 每个部门（115、116、117）都有相同的5个字段

---

### 表单数据值

**2025-01月数据（示例）：**
- 财务部115：小微企业户数125000，调用次数850000，申请数3500，通过率0.75，平均放款150000
- 市场部116：小微企业户数185000，调用次数1200000，申请数5200，通过率0.78，平均放款180000
- 技术部117：小微企业户数95000，调用次数650000，申请数2800，通过率0.72，平均放款135000

**数据值特点：**
- 数值较大（符合业务场景）
- 市场部数据最大，财务部中等，技术部最小
- 每月数据略有增长（模拟业务增长）

---

### 计算规则说明

**月报规则（10条）：**
- 5个项目 × 2列（当期发生额、累计发生额）
- 使用 `#sum()` 函数汇总3个部门的数据
- 项目4和项目5涉及乘法运算（申请数 × 通过率 × 平均放款）

**季报规则（8条）：**
- 2个项目 × 4列（当期发生额、累计发生额、同期增长量、同期增长率）
- 使用 `#safeGet()` 函数访问历史月报数据
- 使用 `previousQuarterReport` 访问上一季度数据
- 同期增长率涉及除法运算（增长量 / 去年同期值）

---

## ⚠️ 注意事项

1. **序列值管理**：PostgreSQL使用序列，脚本会自动处理序列值
2. **租户ID**：业务表没有 `tenant_id` 字段，脚本中不包含该字段
3. **提交人ID**：所有表单数据的 `submit_user_id` 设置为 1
4. **历史数据**：需要先生成2024-Q4的季报，才能正确计算2025-Q1的同期增长率
5. **数据清理**：如果需要重新测试，可以取消注释脚本开头的DELETE语句

---

## 🔍 验证检查清单

- [ ] 备份脚本执行成功
- [ ] 测试数据脚本执行成功
- [ ] 字段配置数量正确（至少20条：5个通用 + 15个部门字段）
- [ ] 表单数据数量正确（18条：3个部门 × 6个月）
- [ ] 计算规则数量正确（10条月报 + 8条季报）
- [ ] 月报生成成功
- [ ] 季报生成成功
- [ ] 字段元数据正确返回
- [ ] Excel导出成功
- [ ] 恢复脚本执行成功

---

**文档版本：** v1.0  
**最后更新：** 2025-01-XX

如有问题，请联系技术支持。
