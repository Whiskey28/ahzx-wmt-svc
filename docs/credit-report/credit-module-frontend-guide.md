# Credit模块前端开发指南

## 一、前端架构设计原则

### 1.1 核心原则：**逻辑全在后端，前端只负责展示和交互**

**✅ 正确的做法**：
- 前端只负责：表单展示、数据录入、调用后端API、展示结果
- 所有业务逻辑（计算、校验、权限判断等）都在后端实现
- 前端通过RESTful API与后端交互

**❌ 错误的做法**：
- 在前端实现计算逻辑
- 在前端实现复杂的校验逻辑
- 在前端判断权限（前端权限只是UI展示控制，真正的权限在后端）

---

## 二、前端需要实现的功能

### 2.1 字段配置管理（管理员功能）

**功能描述**：管理员可以配置各部门的表单字段

**API调用**：
```
GET    /credit/field-config/page          # 分页查询字段配置
GET    /credit/field-config/{id}          # 获取字段配置详情
POST   /credit/field-config/create        # 创建字段配置
PUT    /credit/field-config/update        # 更新字段配置
DELETE /credit/field-config/delete        # 删除字段配置
```

**前端实现**：
- 列表页面：展示字段配置列表，支持分页、搜索、筛选
- 表单页面：动态表单（根据字段类型渲染不同的输入组件）
- 操作：增删改查，调用后端API即可

---

### 2.2 表单数据录入（部门专员功能）

**功能描述**：部门专员填写本部门的表单数据

**API调用**：
```
GET    /credit/form-data/page             # 分页查询表单数据
GET    /credit/form-data/{id}             # 获取表单数据详情
POST   /credit/form-data/create           # 创建表单数据（草稿）
PUT    /credit/form-data/update           # 更新表单数据
POST   /credit/form-data/submit            # 提交表单数据
DELETE /credit/form-data/delete           # 删除表单数据（仅草稿状态）
```

**前端实现**：
- **动态表单渲染**（关键）：
  - 调用 `GET /credit/field-config/list?deptId={deptId}&reportType={reportType}` 获取字段配置
  - 根据字段配置动态生成表单（使用 FormRender 或 FormBuilder）
  - 表单数据以JSON格式提交给后端

**示例代码结构**：
```javascript
// 1. 获取字段配置
const fieldConfigs = await api.get('/credit/field-config/list', {
  params: { deptId: currentDeptId, reportType: 'MONTHLY' }
});

// 2. 动态生成表单（使用 FormRender）
<FormRender
  schema={buildFormSchema(fieldConfigs)}  // 根据字段配置构建表单Schema
  formData={formData}
  onFormDataChange={(data) => setFormData(data)}
/>

// 3. 提交数据
const submitForm = async () => {
  await api.post('/credit/form-data/create', {
    deptId: currentDeptId,
    reportPeriod: '2025-01',
    reportType: 'MONTHLY',
    formData: formData  // JSON格式
  });
};
```

---

### 2.3 报表查看（所有用户）

**功能描述**：查看月报和季报

**API调用**：
```
GET    /credit/summary-report/page        # 分页查询报表
GET    /credit/summary-report/{id}        # 获取报表详情
GET    /credit/summary-report/export     # 导出Excel
POST   /credit/summary-report/generate    # 手动触发报表生成（管理员）
```

**前端实现**：
- 列表页面：展示报表列表，支持按周期、类型筛选
- 详情页面：展示报表数据（JSON格式，需要格式化展示）
- 导出功能：调用导出API，下载Excel文件

---

### 2.4 计算规则配置（管理员功能）

**功能描述**：管理员配置计算规则

**API调用**：
```
GET    /credit/calculation-rule/page      # 分页查询计算规则
GET    /credit/calculation-rule/{id}     # 获取计算规则详情
POST   /credit/calculation-rule/create    # 创建计算规则
PUT    /credit/calculation-rule/update   # 更新计算规则
DELETE /credit/calculation-rule/delete    # 删除计算规则
```

**前端实现**：
- 列表页面：展示计算规则列表
- 表单页面：配置计算规则（目标字段、规则类型、表达式等）
- **注意**：表达式编辑可以使用代码编辑器组件（Monaco Editor）

---

### 2.5 校验规则配置（管理员功能）

**功能描述**：管理员配置字段校验规则

**API调用**：
```
GET    /credit/validation-rule/page        # 分页查询校验规则
GET    /credit/validation-rule/{id}       # 获取校验规则详情
POST   /credit/validation-rule/create     # 创建校验规则
PUT    /credit/validation-rule/update    # 更新校验规则
DELETE /credit/validation-rule/delete    # 删除校验规则
```

**前端实现**：
- 列表页面：展示校验规则列表
- 表单页面：配置校验规则（字段、规则类型、表达式、错误提示）

---

### 2.6 审计日志查看（管理员功能）

**功能描述**：查看数据变更审计日志

**API调用**：
```
GET    /credit/audit-log/page             # 分页查询审计日志
GET    /credit/audit-log/{id}             # 获取审计日志详情
```

**前端实现**：
- 列表页面：展示审计日志列表，支持按业务类型、操作类型、时间范围筛选
- 详情页面：展示变更前后数据对比（JSON格式化展示）

---

## 三、前端技术栈建议

### 3.1 动态表单组件推荐

**方案一：FormRender（推荐）**
- GitHub: https://github.com/alibaba/form-render
- 优点：配置化程度高，支持JSON Schema
- 缺点：需要学习JSON Schema格式

**方案二：FormBuilder**
- GitHub: https://github.com/Kua-Fu/form-builder
- 优点：可视化拖拽，用户体验好
- 缺点：配置化程度相对较低

**方案三：自研动态表单组件**
- 根据字段配置动态渲染
- 完全可控，但开发成本较高

### 3.2 JSON数据展示组件

**推荐使用**：
- `react-json-view`（React）
- `vue-json-viewer`（Vue）
- 或者使用代码高亮组件（如 `highlight.js`）

---

## 四、前端开发注意事项

### 4.1 权限控制

**前端权限只是UI展示控制，真正的权限在后端**：

```javascript
// ✅ 正确：前端只控制UI展示
if (hasPermission('credit:form-data:create')) {
  // 显示"创建"按钮
}

// ❌ 错误：不要在前端判断业务权限
if (user.deptId === formData.deptId) {
  // 允许编辑（这个判断应该在后端）
}
```

**后端会进行权限校验**：
- 部门专员只能操作本部门的数据
- 高级角色只能查看
- 如果前端调用API时没有权限，后端会返回403错误

### 4.2 数据校验

**前端校验只是用户体验优化，真正的校验在后端**：

```javascript
// ✅ 前端校验（提升用户体验）
if (!formData.assets || formData.assets <= 0) {
  message.error('资产必须大于0');
  return;
}

// 后端也会进行校验（保证数据安全）
// 即使前端校验通过，后端校验失败也会返回错误
```

### 4.3 表单数据格式

**表单数据以JSON格式提交**：

```javascript
// 表单数据结构示例
const formData = {
  assets: 639979606,           // 资产
  liabilities: 162832194,      // 负债
  totalIncome: 206095138,      // 总收入
  // ... 其他字段
};

// 提交给后端
await api.post('/credit/form-data/create', {
  deptId: 1,
  reportPeriod: '2025-01',
  reportType: 'MONTHLY',
  formData: formData  // JSON格式
});
```

### 4.4 错误处理

**统一错误处理**：

```javascript
try {
  await api.post('/credit/form-data/create', data);
  message.success('创建成功');
} catch (error) {
  // 后端返回的错误信息
  if (error.response?.data?.code === 'CREDIT_FORM_DATA_DEPT_MISMATCH') {
    message.error('您只能操作本部门的数据');
  } else {
    message.error(error.response?.data?.msg || '操作失败');
  }
}
```

---

## 五、前端开发流程

### 5.1 开发步骤

1. **创建页面路由和菜单**
   - 在路由配置中添加credit模块的路由
   - 在菜单配置中添加credit模块的菜单项

2. **实现字段配置管理页面**
   - 列表页面：调用 `GET /credit/field-config/page`
   - 表单页面：调用 `POST /credit/field-config/create` 等

3. **实现表单数据录入页面**（核心功能）
   - 动态获取字段配置
   - 动态渲染表单
   - 提交表单数据

4. **实现报表查看页面**
   - 列表页面：调用 `GET /credit/summary-report/page`
   - 详情页面：格式化展示JSON数据
   - 导出功能：调用 `GET /credit/summary-report/export`

5. **实现其他管理页面**
   - 计算规则配置
   - 校验规则配置
   - 审计日志查看

### 5.2 测试要点

1. **权限测试**：
   - 部门专员只能看到本部门的数据
   - 高级角色只能查看，不能编辑

2. **数据校验测试**：
   - 必填字段校验
   - 数据类型校验
   - 自定义校验规则

3. **动态表单测试**：
   - 不同部门显示不同的字段
   - 字段类型正确渲染（数字、文本、日期等）

---

## 六、总结

### 6.1 核心原则

**✅ 前端只负责展示和交互，逻辑全在后端**

- 前端：表单展示、数据录入、调用API、展示结果
- 后端：业务逻辑、数据校验、权限控制、计算引擎

### 6.2 前端开发重点

1. **动态表单渲染**：根据字段配置动态生成表单
2. **JSON数据处理**：表单数据以JSON格式提交和展示
3. **权限UI控制**：前端只控制UI展示，真正的权限在后端
4. **错误处理**：统一处理后端返回的错误信息

### 6.3 开发建议

1. **先实现MVP**：先实现核心功能（表单录入、报表查看），再完善其他功能
2. **使用成熟的组件库**：推荐使用FormRender或FormBuilder实现动态表单
3. **保持代码简洁**：前端代码应该简洁，复杂的逻辑都在后端
4. **充分测试**：特别是权限和数据校验的测试

---

**如有疑问，请参考后端API文档或联系后端开发人员。**
