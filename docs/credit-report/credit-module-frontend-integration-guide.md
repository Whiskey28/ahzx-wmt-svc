# 征信模块前端接入指南

## 目录
1. [概述](#概述)
2. [接口清单](#接口清单)
3. [数据结构说明](#数据结构说明)
4. [动态表单实现指南](#动态表单实现指南)
5. [表单校验实现指南](#表单校验实现指南)
6. [汇总报表查看指南](#汇总报表查看指南)
7. [完整示例代码](#完整示例代码)
8. [常见问题](#常见问题)

---

## 概述

征信模块提供了完整的动态表单填报、校验和汇总功能。前端需要：

1. **动态表单渲染**：根据字段配置动态生成表单
2. **表单校验**：实现前端校验规则
3. **数据提交**：保存草稿和提交表单
4. **汇总查看**：查看汇总报表数据

### 核心流程

```
1. 获取字段配置 → 2. 动态渲染表单 → 3. 前端校验 → 4. 保存/提交 → 5. 查看汇总
```

---

## 接口清单

### 1. 字段配置接口

#### 1.1 获取字段配置列表（用于动态表单）
```http
GET /credit/field-config/list?deptId={deptId}&reportType={reportType}
```

**参数说明：**
- `deptId`（可选）：部门ID，0表示通用字段，不传则返回所有字段
- `reportType`（可选）：报表类型，`MONTHLY`（月报）或 `QUARTERLY`（季报）

**响应示例：**
```json
{
  "code": 0,
  "data": [
    {
      "id": 1,
      "deptId": 0,
      "fieldCode": "assets",
      "fieldName": "资产总额",
      "fieldType": "DECIMAL",
      "required": true,
      "defaultValue": "0",
      "validationRule": {
        "min": 0,
        "max": 999999999.99,
        "precision": 2
      },
      "displayOrder": 1,
      "status": 1
    }
  ],
  "msg": "操作成功"
}
```

### 2. 表单数据接口

#### 2.1 创建/更新表单数据（保存草稿）
```http
POST /credit/form-data/create
POST /credit/form-data/update
```

**请求体：**
```json
{
  "id": 1,                    // 更新时必填，创建时不填
  "deptId": 1,                // 必填：部门ID
  "reportPeriod": "2024-01", // 必填：报送周期，格式 YYYY-MM（月报）或 YYYY-Q1/Q2/Q3/Q4（季报）
  "reportType": "MONTHLY",    // 必填：MONTHLY（月报）或 QUARTERLY（季报）
  "formData": {               // 表单数据，key为fieldCode，value为字段值
    "assets": "1000000.00",
    "liabilities": "500000.00"
  }
}
```

#### 2.2 提交表单数据
```http
POST /credit/form-data/submit
```

**请求体：**
```json
{
  "id": 1  // 表单ID，必填
}
```

**注意：** 提交前会进行后端校验，如果校验失败会返回错误信息。

#### 2.3 获取表单数据详情
```http
GET /credit/form-data/get?id={id}
```

#### 2.4 获取表单数据分页列表
```http
GET /credit/form-data/page?pageNo=1&pageSize=10&deptId=1&reportPeriod=2024-01&reportType=MONTHLY
```

### 3. 汇总报表接口

#### 3.1 生成汇总报表
```http
POST /credit/summary-report/generate
```

**请求体：**
```json
{
  "reportPeriod": "2024-01",  // 必填：报送周期
  "reportType": "MONTHLY"     // 必填：MONTHLY（月报）或 QUARTERLY（季报）
}
```

**响应：**
```json
{
  "code": 0,
  "data": 123,  // 报表ID
  "msg": "操作成功"
}
```

**注意：** 生成报表是异步操作，可能需要一定时间。建议前端轮询查询报表状态。

#### 3.2 获取汇总报表详情
```http
GET /credit/summary-report/get?id={id}
```

**响应示例：**
```json
{
  "code": 0,
  "data": {
    "id": 123,
    "reportPeriod": "2024-01",
    "reportType": "MONTHLY",
    "reportData": {
      "totalAssets": "5000000.00",
      "totalLiabilities": "2000000.00"
    },
    "status": 1,  // 0-计算中，1-已完成，2-已报送
    "generateTime": "2024-01-15T10:30:00"
  },
  "msg": "操作成功"
}
```

#### 3.3 获取汇总报表分页列表
```http
GET /credit/summary-report/page?pageNo=1&pageSize=10&reportPeriod=2024-01&reportType=MONTHLY
```

#### 3.4 导出汇总报表Excel
```http
GET /credit/summary-report/export-excel?reportPeriod=2024-01&reportType=MONTHLY
```

---

## 数据结构说明

### 字段配置结构（CreditFieldConfigRespVO）

| 字段 | 类型 | 说明 | 示例 |
|------|------|------|------|
| `id` | Long | 字段编号 | 1 |
| `deptId` | Long | 部门ID（0表示通用字段） | 0 |
| `fieldCode` | String | 字段编码（唯一标识，用于formData的key） | "assets" |
| `fieldName` | String | 字段名称（显示标签） | "资产总额" |
| `fieldType` | String | 字段类型 | "DECIMAL" |
| `required` | Boolean | 是否必填 | true |
| `defaultValue` | String | 默认值 | "0" |
| `validationRule` | Map<String, Object> | 校验规则（JSON对象） | 见下方说明 |
| `displayOrder` | Integer | 显示顺序 | 1 |
| `status` | Integer | 状态（0-禁用，1-启用） | 1 |

### 字段类型枚举（CreditFieldTypeEnum）

| 值 | 说明 | 前端组件建议 |
|------|------|------------|
| `NUMBER` | 整数 | `<input type="number" step="1">` |
| `DECIMAL` | 小数 | `<input type="number" step="0.01">` |
| `TEXT` | 文本 | `<input type="text">` 或 `<textarea>` |
| `DATE` | 日期 | `<input type="date">` 或日期选择器 |
| `DATETIME` | 日期时间 | 日期时间选择器 |
| `BOOLEAN` | 布尔值 | `<input type="checkbox">` 或开关组件 |

### 校验规则结构（validationRule）

`validationRule` 是一个 JSON 对象，可能包含以下字段：

```typescript
interface ValidationRule {
  // 数值类型（NUMBER/DECIMAL）的校验规则
  min?: number;           // 最小值
  max?: number;           // 最大值
  precision?: number;      // 小数位数（DECIMAL类型）
  
  // 文本类型（TEXT）的校验规则
  minLength?: number;      // 最小长度
  maxLength?: number;      // 最大长度
  pattern?: string;       // 正则表达式
  
  // 日期类型（DATE/DATETIME）的校验规则
  minDate?: string;       // 最小日期（ISO格式）
  maxDate?: string;       // 最大日期（ISO格式）
  
  // 自定义校验规则
  customRule?: string;    // 自定义规则标识
  errorMessage?: string;  // 自定义错误提示
}
```

**示例：**
```json
{
  "min": 0,
  "max": 999999999.99,
  "precision": 2,
  "errorMessage": "资产总额必须在0到999999999.99之间，保留2位小数"
}
```

### 表单数据提交结构（CreditFormDataSaveReqVO）

```typescript
interface CreditFormDataSaveReqVO {
  id?: number;                    // 更新时必填
  deptId: number;                 // 必填：部门ID
  reportPeriod: string;           // 必填：报送周期
  reportType: "MONTHLY" | "QUARTERLY";  // 必填：报表类型
  formData: Record<string, any>;  // 表单数据，key为fieldCode
}
```

### 报表类型枚举（CreditReportTypeEnum）

| 值 | 说明 |
|------|------|
| `MONTHLY` | 月报 |
| `QUARTERLY` | 季报 |

### 报表状态枚举（CreditReportStatusEnum）

| 值 | 说明 |
|------|------|
| `0` | 计算中 |
| `1` | 已完成 |
| `2` | 已报送 |

---

## 动态表单实现指南

### 步骤1：获取字段配置

```typescript
// 1. 调用接口获取字段配置
const fetchFieldConfigs = async (deptId: number, reportType: string) => {
  const response = await fetch(
    `/credit/field-config/list?deptId=${deptId}&reportType=${reportType}`
  );
  const result = await response.json();
  if (result.code === 0) {
    // 过滤掉禁用的字段，并按displayOrder排序
    return result.data
      .filter((field: any) => field.status === 1)
      .sort((a: any, b: any) => a.displayOrder - b.displayOrder);
  }
  return [];
};
```

### 步骤2：动态渲染表单

#### React 示例

```tsx
import React, { useState, useEffect } from 'react';

interface FieldConfig {
  id: number;
  fieldCode: string;
  fieldName: string;
  fieldType: string;
  required: boolean;
  defaultValue: string;
  validationRule?: Record<string, any>;
  displayOrder: number;
}

const DynamicForm: React.FC<{
  deptId: number;
  reportType: 'MONTHLY' | 'QUARTERLY';
  reportPeriod: string;
}> = ({ deptId, reportType, reportPeriod }) => {
  const [fields, setFields] = useState<FieldConfig[]>([]);
  const [formData, setFormData] = useState<Record<string, any>>({});
  const [errors, setErrors] = useState<Record<string, string>>({});

  // 1. 加载字段配置
  useEffect(() => {
    const loadFields = async () => {
      const configs = await fetchFieldConfigs(deptId, reportType);
      setFields(configs);
      
      // 初始化表单数据（使用默认值）
      const initialData: Record<string, any> = {};
      configs.forEach((field: FieldConfig) => {
        initialData[field.fieldCode] = field.defaultValue || '';
      });
      setFormData(initialData);
    };
    loadFields();
  }, [deptId, reportType]);

  // 2. 渲染字段组件
  const renderField = (field: FieldConfig) => {
    const value = formData[field.fieldCode] || '';
    const error = errors[field.fieldCode];

    switch (field.fieldType) {
      case 'NUMBER':
        return (
          <div key={field.id} className="form-item">
            <label>
              {field.fieldName}
              {field.required && <span className="required">*</span>}
            </label>
            <input
              type="number"
              step="1"
              value={value}
              onChange={(e) => handleFieldChange(field.fieldCode, e.target.value)}
              onBlur={() => validateField(field)}
              className={error ? 'error' : ''}
            />
            {error && <span className="error-message">{error}</span>}
          </div>
        );

      case 'DECIMAL':
        const precision = field.validationRule?.precision || 2;
        return (
          <div key={field.id} className="form-item">
            <label>
              {field.fieldName}
              {field.required && <span className="required">*</span>}
            </label>
            <input
              type="number"
              step={Math.pow(10, -precision)}
              value={value}
              onChange={(e) => handleFieldChange(field.fieldCode, e.target.value)}
              onBlur={() => validateField(field)}
              className={error ? 'error' : ''}
            />
            {error && <span className="error-message">{error}</span>}
          </div>
        );

      case 'TEXT':
        return (
          <div key={field.id} className="form-item">
            <label>
              {field.fieldName}
              {field.required && <span className="required">*</span>}
            </label>
            <input
              type="text"
              value={value}
              onChange={(e) => handleFieldChange(field.fieldCode, e.target.value)}
              onBlur={() => validateField(field)}
              className={error ? 'error' : ''}
            />
            {error && <span className="error-message">{error}</span>}
          </div>
        );

      case 'DATE':
        return (
          <div key={field.id} className="form-item">
            <label>
              {field.fieldName}
              {field.required && <span className="required">*</span>}
            </label>
            <input
              type="date"
              value={value}
              onChange={(e) => handleFieldChange(field.fieldCode, e.target.value)}
              onBlur={() => validateField(field)}
              className={error ? 'error' : ''}
            />
            {error && <span className="error-message">{error}</span>}
          </div>
        );

      case 'BOOLEAN':
        return (
          <div key={field.id} className="form-item">
            <label>
              <input
                type="checkbox"
                checked={value === true || value === 'true'}
                onChange={(e) => handleFieldChange(field.fieldCode, e.target.checked)}
              />
              {field.fieldName}
              {field.required && <span className="required">*</span>}
            </label>
            {error && <span className="error-message">{error}</span>}
          </div>
        );

      default:
        return null;
    }
  };

  // 3. 处理字段值变化
  const handleFieldChange = (fieldCode: string, value: any) => {
    setFormData((prev) => ({
      ...prev,
      [fieldCode]: value,
    }));
    // 清除该字段的错误
    if (errors[fieldCode]) {
      setErrors((prev) => {
        const newErrors = { ...prev };
        delete newErrors[fieldCode];
        return newErrors;
      });
    }
  };

  return (
    <form className="dynamic-form">
      {fields.map((field) => renderField(field))}
      <div className="form-actions">
        <button type="button" onClick={handleSave}>保存草稿</button>
        <button type="button" onClick={handleSubmit}>提交</button>
      </div>
    </form>
  );
};
```

### 步骤3：保存和提交表单

```typescript
// 保存草稿
const handleSave = async () => {
  // 先进行前端校验
  const validationErrors = validateAllFields();
  if (Object.keys(validationErrors).length > 0) {
    setErrors(validationErrors);
    return;
  }

  const requestBody = {
    id: formId, // 如果有ID则是更新，否则是创建
    deptId,
    reportPeriod,
    reportType,
    formData,
  };

  const response = await fetch('/credit/form-data/create', {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify(requestBody),
  });

  const result = await response.json();
  if (result.code === 0) {
    if (!formId) {
      formId = result.data; // 保存返回的ID
    }
    alert('保存成功');
  } else {
    alert('保存失败：' + result.msg);
  }
};

// 提交表单
const handleSubmit = async () => {
  // 先进行前端校验
  const validationErrors = validateAllFields();
  if (Object.keys(validationErrors).length > 0) {
    setErrors(validationErrors);
    return;
  }

  // 如果没有保存过，先保存
  if (!formId) {
    await handleSave();
  }

  const response = await fetch('/credit/form-data/submit', {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify({ id: formId }),
  });

  const result = await response.json();
  if (result.code === 0) {
    alert('提交成功');
  } else {
    alert('提交失败：' + result.msg);
  }
};
```

---

## 表单校验实现指南

### 前端校验规则实现

```typescript
// 校验单个字段
const validateField = (field: FieldConfig): string | null => {
  const value = formData[field.fieldCode];
  const rule = field.validationRule || {};

  // 1. 必填校验
  if (field.required) {
    if (value === null || value === undefined || value === '') {
      return `${field.fieldName}不能为空`;
    }
  }

  // 如果值为空且不是必填，跳过其他校验
  if (!value && !field.required) {
    return null;
  }

  // 2. 根据字段类型进行校验
  switch (field.fieldType) {
    case 'NUMBER':
      const numValue = Number(value);
      if (isNaN(numValue)) {
        return `${field.fieldName}必须是整数`;
      }
      if (rule.min !== undefined && numValue < rule.min) {
        return rule.errorMessage || `${field.fieldName}不能小于${rule.min}`;
      }
      if (rule.max !== undefined && numValue > rule.max) {
        return rule.errorMessage || `${field.fieldName}不能大于${rule.max}`;
      }
      break;

    case 'DECIMAL':
      const decValue = Number(value);
      if (isNaN(decValue)) {
        return `${field.fieldName}必须是数字`;
      }
      // 校验小数位数
      if (rule.precision !== undefined) {
        const parts = String(value).split('.');
        if (parts[1] && parts[1].length > rule.precision) {
          return `${field.fieldName}最多保留${rule.precision}位小数`;
        }
      }
      if (rule.min !== undefined && decValue < rule.min) {
        return rule.errorMessage || `${field.fieldName}不能小于${rule.min}`;
      }
      if (rule.max !== undefined && decValue > rule.max) {
        return rule.errorMessage || `${field.fieldName}不能大于${rule.max}`;
      }
      break;

    case 'TEXT':
      const textValue = String(value);
      if (rule.minLength !== undefined && textValue.length < rule.minLength) {
        return `${field.fieldName}长度不能少于${rule.minLength}个字符`;
      }
      if (rule.maxLength !== undefined && textValue.length > rule.maxLength) {
        return `${field.fieldName}长度不能超过${rule.maxLength}个字符`;
      }
      if (rule.pattern) {
        const regex = new RegExp(rule.pattern);
        if (!regex.test(textValue)) {
          return rule.errorMessage || `${field.fieldName}格式不正确`;
        }
      }
      break;

    case 'DATE':
    case 'DATETIME':
      const dateValue = new Date(value);
      if (isNaN(dateValue.getTime())) {
        return `${field.fieldName}日期格式不正确`;
      }
      if (rule.minDate) {
        const minDate = new Date(rule.minDate);
        if (dateValue < minDate) {
          return `${field.fieldName}不能早于${rule.minDate}`;
        }
      }
      if (rule.maxDate) {
        const maxDate = new Date(rule.maxDate);
        if (dateValue > maxDate) {
          return `${field.fieldName}不能晚于${rule.maxDate}`;
        }
      }
      break;
  }

  return null;
};

// 校验所有字段
const validateAllFields = (): Record<string, string> => {
  const errors: Record<string, string> = {};
  fields.forEach((field) => {
    const error = validateField(field);
    if (error) {
      errors[field.fieldCode] = error;
    }
  });
  return errors;
};
```

### 实时校验（输入时校验）

```typescript
// 在输入框的 onChange 事件中，可以添加防抖校验
import { debounce } from 'lodash';

const debouncedValidate = debounce((field: FieldConfig) => {
  const error = validateField(field);
  if (error) {
    setErrors((prev) => ({
      ...prev,
      [field.fieldCode]: error,
    }));
  } else {
    setErrors((prev) => {
      const newErrors = { ...prev };
      delete newErrors[field.fieldCode];
      return newErrors;
    });
  }
}, 300);

// 在 handleFieldChange 中调用
const handleFieldChange = (fieldCode: string, value: any) => {
  setFormData((prev) => ({
    ...prev,
    [fieldCode]: value,
  }));
  
  const field = fields.find((f) => f.fieldCode === fieldCode);
  if (field) {
    debouncedValidate(field);
  }
};
```

---

## 汇总报表查看指南

### 1. 生成汇总报表

```typescript
const generateReport = async (reportPeriod: string, reportType: string) => {
  const response = await fetch('/credit/summary-report/generate', {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify({ reportPeriod, reportType }),
  });

  const result = await response.json();
  if (result.code === 0) {
    const reportId = result.data;
    // 开始轮询查询报表状态
    pollReportStatus(reportId);
  } else {
    alert('生成报表失败：' + result.msg);
  }
};
```

### 2. 轮询查询报表状态

```typescript
const pollReportStatus = async (reportId: number) => {
  const maxAttempts = 30; // 最多轮询30次
  let attempts = 0;

  const poll = async () => {
    attempts++;
    const response = await fetch(`/credit/summary-report/get?id=${reportId}`);
    const result = await response.json();

    if (result.code === 0) {
      const report = result.data;
      
      if (report.status === 1) {
        // 已完成，显示报表数据
        displayReport(report);
      } else if (report.status === 0) {
        // 计算中，继续轮询
        if (attempts < maxAttempts) {
          setTimeout(poll, 2000); // 2秒后再次查询
        } else {
          alert('报表生成超时，请稍后手动刷新');
        }
      }
    }
  };

  poll();
};
```

### 3. 显示汇总报表

```tsx
const ReportView: React.FC<{ reportId: number }> = ({ reportId }) => {
  const [report, setReport] = useState<any>(null);
  const [fields, setFields] = useState<FieldConfig[]>([]);

  useEffect(() => {
    // 加载报表数据
    const loadReport = async () => {
      const response = await fetch(`/credit/summary-report/get?id=${reportId}`);
      const result = await response.json();
      if (result.code === 0) {
        setReport(result.data);
        
        // 加载字段配置（用于显示字段名称）
        const configs = await fetchFieldConfigs(0, result.data.reportType);
        setFields(configs);
      }
    };
    loadReport();
  }, [reportId]);

  if (!report) {
    return <div>加载中...</div>;
  }

  if (report.status === 0) {
    return <div>报表计算中，请稍候...</div>;
  }

  return (
    <div className="report-view">
      <h2>
        {report.reportType === 'MONTHLY' ? '月报' : '季报'} - {report.reportPeriod}
      </h2>
      <table>
        <thead>
          <tr>
            <th>字段名称</th>
            <th>字段值</th>
          </tr>
        </thead>
        <tbody>
          {Object.entries(report.reportData || {}).map(([fieldCode, value]) => {
            const field = fields.find((f) => f.fieldCode === fieldCode);
            return (
              <tr key={fieldCode}>
                <td>{field?.fieldName || fieldCode}</td>
                <td>{value}</td>
              </tr>
            );
          })}
        </tbody>
      </table>
      <button onClick={() => exportExcel(report.reportPeriod, report.reportType)}>
        导出Excel
      </button>
    </div>
  );
};
```

### 4. 导出Excel

```typescript
const exportExcel = (reportPeriod: string, reportType: string) => {
  const url = `/credit/summary-report/export-excel?reportPeriod=${reportPeriod}&reportType=${reportType}`;
  window.open(url, '_blank');
};
```

---

## 完整示例代码

### React + TypeScript 完整示例

```tsx
import React, { useState, useEffect } from 'react';

// 类型定义
interface FieldConfig {
  id: number;
  deptId: number;
  fieldCode: string;
  fieldName: string;
  fieldType: string;
  required: boolean;
  defaultValue: string;
  validationRule?: Record<string, any>;
  displayOrder: number;
  status: number;
}

interface FormData {
  id?: number;
  deptId: number;
  reportPeriod: string;
  reportType: 'MONTHLY' | 'QUARTERLY';
  formData: Record<string, any>;
}

// API 调用
const api = {
  getFieldConfigs: async (deptId: number, reportType: string): Promise<FieldConfig[]> => {
    const response = await fetch(
      `/credit/field-config/list?deptId=${deptId}&reportType=${reportType}`
    );
    const result = await response.json();
    if (result.code === 0) {
      return result.data
        .filter((field: FieldConfig) => field.status === 1)
        .sort((a: FieldConfig, b: FieldConfig) => a.displayOrder - b.displayOrder);
    }
    return [];
  },

  saveFormData: async (data: FormData): Promise<number> => {
    const url = data.id ? '/credit/form-data/update' : '/credit/form-data/create';
    const response = await fetch(url, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(data),
    });
    const result = await response.json();
    if (result.code === 0) {
      return result.data;
    }
    throw new Error(result.msg || '保存失败');
  },

  submitFormData: async (id: number): Promise<void> => {
    const response = await fetch('/credit/form-data/submit', {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ id }),
    });
    const result = await response.json();
    if (result.code !== 0) {
      throw new Error(result.msg || '提交失败');
    }
  },
};

// 主组件
const CreditFormPage: React.FC = () => {
  const [deptId] = useState(1); // 从用户信息获取
  const [reportType] = useState<'MONTHLY' | 'QUARTERLY'>('MONTHLY');
  const [reportPeriod] = useState('2024-01');
  
  const [fields, setFields] = useState<FieldConfig[]>([]);
  const [formData, setFormData] = useState<Record<string, any>>({});
  const [formId, setFormId] = useState<number | undefined>();
  const [errors, setErrors] = useState<Record<string, string>>({});
  const [loading, setLoading] = useState(false);

  // 加载字段配置
  useEffect(() => {
    const loadFields = async () => {
      const configs = await api.getFieldConfigs(deptId, reportType);
      setFields(configs);
      
      const initialData: Record<string, any> = {};
      configs.forEach((field) => {
        initialData[field.fieldCode] = field.defaultValue || '';
      });
      setFormData(initialData);
    };
    loadFields();
  }, [deptId, reportType]);

  // 字段值变化处理
  const handleFieldChange = (fieldCode: string, value: any) => {
    setFormData((prev) => ({
      ...prev,
      [fieldCode]: value,
    }));
    // 清除错误
    if (errors[fieldCode]) {
      setErrors((prev) => {
        const newErrors = { ...prev };
        delete newErrors[fieldCode];
        return newErrors;
      });
    }
  };

  // 字段校验
  const validateField = (field: FieldConfig): string | null => {
    const value = formData[field.fieldCode];
    const rule = field.validationRule || {};

    if (field.required && (!value || value === '')) {
      return `${field.fieldName}不能为空`;
    }

    if (!value && !field.required) {
      return null;
    }

    switch (field.fieldType) {
      case 'NUMBER':
        const numValue = Number(value);
        if (isNaN(numValue)) return `${field.fieldName}必须是整数`;
        if (rule.min !== undefined && numValue < rule.min) {
          return rule.errorMessage || `${field.fieldName}不能小于${rule.min}`;
        }
        if (rule.max !== undefined && numValue > rule.max) {
          return rule.errorMessage || `${field.fieldName}不能大于${rule.max}`;
        }
        break;

      case 'DECIMAL':
        const decValue = Number(value);
        if (isNaN(decValue)) return `${field.fieldName}必须是数字`;
        if (rule.precision !== undefined) {
          const parts = String(value).split('.');
          if (parts[1] && parts[1].length > rule.precision) {
            return `${field.fieldName}最多保留${rule.precision}位小数`;
          }
        }
        if (rule.min !== undefined && decValue < rule.min) {
          return rule.errorMessage || `${field.fieldName}不能小于${rule.min}`;
        }
        if (rule.max !== undefined && decValue > rule.max) {
          return rule.errorMessage || `${field.fieldName}不能大于${rule.max}`;
        }
        break;

      case 'TEXT':
        const textValue = String(value);
        if (rule.minLength !== undefined && textValue.length < rule.minLength) {
          return `${field.fieldName}长度不能少于${rule.minLength}个字符`;
        }
        if (rule.maxLength !== undefined && textValue.length > rule.maxLength) {
          return `${field.fieldName}长度不能超过${rule.maxLength}个字符`;
        }
        if (rule.pattern) {
          const regex = new RegExp(rule.pattern);
          if (!regex.test(textValue)) {
            return rule.errorMessage || `${field.fieldName}格式不正确`;
          }
        }
        break;
    }

    return null;
  };

  // 校验所有字段
  const validateAllFields = (): Record<string, string> => {
    const validationErrors: Record<string, string> = {};
    fields.forEach((field) => {
      const error = validateField(field);
      if (error) {
        validationErrors[field.fieldCode] = error;
      }
    });
    return validationErrors;
  };

  // 保存草稿
  const handleSave = async () => {
    const validationErrors = validateAllFields();
    if (Object.keys(validationErrors).length > 0) {
      setErrors(validationErrors);
      return;
    }

    setLoading(true);
    try {
      const data: FormData = {
        id: formId,
        deptId,
        reportPeriod,
        reportType,
        formData,
      };
      const id = await api.saveFormData(data);
      setFormId(id);
      alert('保存成功');
    } catch (error: any) {
      alert('保存失败：' + error.message);
    } finally {
      setLoading(false);
    }
  };

  // 提交表单
  const handleSubmit = async () => {
    const validationErrors = validateAllFields();
    if (Object.keys(validationErrors).length > 0) {
      setErrors(validationErrors);
      return;
    }

    if (!formId) {
      await handleSave();
    }

    setLoading(true);
    try {
      await api.submitFormData(formId!);
      alert('提交成功');
    } catch (error: any) {
      alert('提交失败：' + error.message);
    } finally {
      setLoading(false);
    }
  };

  // 渲染字段
  const renderField = (field: FieldConfig) => {
    const value = formData[field.fieldCode] || '';
    const error = errors[field.fieldCode];

    const commonProps = {
      key: field.id,
      value: value,
      onChange: (e: any) => handleFieldChange(field.fieldCode, e.target.value),
      onBlur: () => {
        const error = validateField(field);
        if (error) {
          setErrors((prev) => ({ ...prev, [field.fieldCode]: error }));
        } else {
          setErrors((prev) => {
            const newErrors = { ...prev };
            delete newErrors[field.fieldCode];
            return newErrors;
          });
        }
      },
      className: error ? 'error' : '',
    };

    return (
      <div className="form-item" key={field.id}>
        <label>
          {field.fieldName}
          {field.required && <span className="required">*</span>}
        </label>
        {field.fieldType === 'NUMBER' && (
          <input type="number" step="1" {...commonProps} />
        )}
        {field.fieldType === 'DECIMAL' && (
          <input
            type="number"
            step={Math.pow(10, -(field.validationRule?.precision || 2))}
            {...commonProps}
          />
        )}
        {field.fieldType === 'TEXT' && (
          <input type="text" {...commonProps} />
        )}
        {field.fieldType === 'DATE' && (
          <input type="date" {...commonProps} />
        )}
        {field.fieldType === 'BOOLEAN' && (
          <input
            type="checkbox"
            checked={value === true || value === 'true'}
            onChange={(e) => handleFieldChange(field.fieldCode, e.target.checked)}
          />
        )}
        {error && <span className="error-message">{error}</span>}
      </div>
    );
  };

  return (
    <div className="credit-form-page">
      <h1>征信表单填报</h1>
      <form className="dynamic-form">
        {fields.map((field) => renderField(field))}
        <div className="form-actions">
          <button type="button" onClick={handleSave} disabled={loading}>
            保存草稿
          </button>
          <button type="button" onClick={handleSubmit} disabled={loading}>
            提交
          </button>
        </div>
      </form>
    </div>
  );
};

export default CreditFormPage;
```

---

## 常见问题

### Q1: 如何获取当前用户的部门ID？

**A:** 前端应该从用户信息接口或登录后的用户信息中获取 `deptId`。如果后端接口支持，也可以从请求头或Token中解析。

### Q2: 报送周期格式是什么？

**A:** 
- 月报：`YYYY-MM`，例如 `2024-01`
- 季报：`YYYY-Q1/Q2/Q3/Q4`，例如 `2024-Q1`

### Q3: 表单数据中的字段值类型是什么？

**A:** 
- `NUMBER` 类型：数字字符串或数字，例如 `"100"` 或 `100`
- `DECIMAL` 类型：数字字符串，例如 `"100.50"`
- `TEXT` 类型：字符串
- `DATE` 类型：ISO日期字符串，例如 `"2024-01-15"`
- `DATETIME` 类型：ISO日期时间字符串，例如 `"2024-01-15T10:30:00"`
- `BOOLEAN` 类型：布尔值 `true/false` 或字符串 `"true"/"false"`

### Q4: 如何实现字段联动？

**A:** 字段联动逻辑可以在前端实现。当某个字段值变化时，根据业务规则更新其他字段的值。例如：

```typescript
const handleFieldChange = (fieldCode: string, value: any) => {
  setFormData((prev) => {
    const newData = { ...prev, [fieldCode]: value };
    
    // 字段联动示例：如果资产变化，自动计算净资产
    if (fieldCode === 'assets' || fieldCode === 'liabilities') {
      const assets = Number(newData.assets || 0);
      const liabilities = Number(newData.liabilities || 0);
      newData.netAssets = String(assets - liabilities);
    }
    
    return newData;
  });
};
```

### Q5: 如何加载已保存的表单数据？

**A:** 在组件加载时，可以调用 `/credit/form-data/get` 接口获取已保存的数据：

```typescript
useEffect(() => {
  const loadSavedData = async () => {
    // 先查询是否存在该周期的表单数据
    const response = await fetch(
      `/credit/form-data/page?deptId=${deptId}&reportPeriod=${reportPeriod}&reportType=${reportType}&pageSize=1`
    );
    const result = await response.json();
    if (result.code === 0 && result.data.list.length > 0) {
      const savedForm = result.data.list[0];
      setFormId(savedForm.id);
      setFormData(savedForm.formData || {});
    }
  };
  loadSavedData();
}, [deptId, reportPeriod, reportType]);
```

### Q6: 如何实现字段分组显示？

**A:** 可以在字段配置中添加 `group` 字段（需要后端支持），或者前端根据字段名称或编码进行分组：

```typescript
// 假设字段编码有前缀，例如 "asset_xxx", "liability_xxx"
const groupedFields = fields.reduce((acc, field) => {
  const group = field.fieldCode.split('_')[0] || 'other';
  if (!acc[group]) acc[group] = [];
  acc[group].push(field);
  return acc;
}, {} as Record<string, FieldConfig[]>);

// 渲染分组
Object.entries(groupedFields).map(([groupName, groupFields]) => (
  <div key={groupName} className="field-group">
    <h3>{groupName}</h3>
    {groupFields.map((field) => renderField(field))}
  </div>
));
```

### Q7: 如何实现字段的只读模式？

**A:** 可以根据表单状态判断字段是否只读：

```typescript
const isReadOnly = formStatus === 1 || formStatus === 2; // 已提交或已报送

<input
  type="number"
  value={value}
  onChange={handleFieldChange}
  readOnly={isReadOnly}
  disabled={isReadOnly}
/>
```

### Q8: 如何实现字段的格式化显示？

**A:** 对于数值类型，可以使用 `toLocaleString` 格式化：

```typescript
const formatNumber = (value: any, fieldType: string) => {
  if (fieldType === 'DECIMAL' || fieldType === 'NUMBER') {
    const num = Number(value);
    if (!isNaN(num)) {
      return num.toLocaleString('zh-CN', {
        minimumFractionDigits: 0,
        maximumFractionDigits: 2,
      });
    }
  }
  return value;
};
```

---

## 总结

本指南提供了征信模块前端接入的完整方案，包括：

1. ✅ **接口清单**：所有需要调用的后端接口
2. ✅ **数据结构说明**：字段配置、表单数据、校验规则等
3. ✅ **动态表单实现**：根据字段配置动态渲染表单
4. ✅ **表单校验**：前端校验规则实现
5. ✅ **汇总报表**：报表生成和查看
6. ✅ **完整示例代码**：可直接使用的React组件

**下一步建议：**

1. 根据你的前端框架（React/Vue/Angular等）调整示例代码
2. 根据UI设计规范调整样式
3. 添加加载状态、错误处理等用户体验优化
4. 根据实际业务需求调整字段联动逻辑

如有问题，请参考后端接口文档或联系后端开发人员。
