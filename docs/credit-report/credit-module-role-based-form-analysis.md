# 征信模块基于角色的表单区分设计方案分析报告

## 一、需求理解与确认

### 1.1 核心需求

根据您的描述，核心需求如下：

1. **多表单场景**
   - ✅ 同一部门在同一周期内需要填写多个表单
   - ✅ 这些表单可能需要汇总到一起，也可能不需要汇总（多个独立的表）
   - ✅ 同一部门有不同角色来填写不同的表单

2. **设计思路转变**
   - ✅ 从"按部门区分"改为"按角色区分"
   - ✅ 角色与部门没有直接关系，都是与用户多对一的关系（一个用户可以有多个角色）
   - ✅ 计算汇总改为按角色进行，而不是按部门

3. **扩展性需求**
   - ✅ 角色可以代表项目、地区、业务线等不同维度
   - ✅ 每次填写时，用户选择自己所对应的其中一个角色
   - ✅ 根据选择的角色获取对应的字段配置来填写

### 1.2 业务场景示例

**场景1：同一部门多个角色填写不同表单**
```
财务部（dept_id=115）
├── 角色1：财务经理（role_id=201）→ 填写"财务汇总表"
├── 角色2：成本会计（role_id=202）→ 填写"成本明细表"
└── 角色3：出纳（role_id=203）→ 填写"资金流水表"
```

**场景2：角色代表不同维度**
```
角色维度示例：
├── 项目维度：项目A负责人（role_id=301）、项目B负责人（role_id=302）
├── 地区维度：华东区经理（role_id=401）、华南区经理（role_id=402）
└── 业务线：信贷业务（role_id=501）、投资业务（role_id=502）
```

**场景3：表单汇总场景**
```
需要汇总的表单：
- 角色201（财务经理）的表单 + 角色202（成本会计）的表单 → 汇总成"财务总表"

不需要汇总的表单：
- 角色301（项目A负责人）的表单 → 独立的"项目A报表"
- 角色401（华东区经理）的表单 → 独立的"华东区报表"
```

---

## 二、方案对比分析

### 2.1 两种可行方案

根据您的洞察，实际上有两种方案都可以满足需求：

#### 方案A：重新定义"部门"概念（最小改动）

**核心思路**：不把"部门"看成公司内的组织部门，而是看成"表单分类/模板"

**实现方式**：
- 在 `system_dept` 表中创建虚拟部门，如：
  - "财务汇总表部门"（dept_id=201）
  - "成本明细表部门"（dept_id=202）
  - "项目A部门"（dept_id=301）
  - "华东区部门"（dept_id=401）
- 字段配置和表单数据仍然使用 `dept_id` 区分
- 用户填写时，选择对应的"部门"（实际上是表单类型）

**优点**：
- ✅ **改动最小**：几乎不需要修改代码和数据库结构
- ✅ **快速实施**：可以立即使用，无需数据迁移
- ✅ **向后兼容**：完全兼容现有设计

**缺点**：
- ❌ **概念混淆**：部门在系统中已有明确语义（组织架构），重新定义会造成理解困难
- ❌ **语义不一致**：与系统其他模块的部门概念不一致
  - `system_users.dept_id` 表示用户所属的组织部门
  - `credit_form_data.dept_id` 表示表单分类（概念不同）
- ❌ **权限控制混乱**：用户的实际部门和表单"部门"混在一起
- ❌ **维护困难**：需要在组织部门之外维护一套"虚拟部门"
- ❌ **扩展性差**：如果未来需要真正的组织部门功能，会冲突

#### 方案B：使用角色作为区分维度（推荐）

**核心思路**：使用系统现有的角色体系，角色可以灵活代表职责、项目、地区等

**实现方式**：
- 字段配置和表单数据使用 `role_id` 区分
- 角色可以灵活创建，如：
  - "财务经理"（role_id=201）
  - "成本会计"（role_id=202）
  - "项目A负责人"（role_id=301）
  - "华东区经理"（role_id=401）
- 用户填写时，选择自己拥有的角色

**优点**：
- ✅ **语义清晰**：角色本身就是灵活的，可以代表职责、项目、地区等
- ✅ **概念一致**：与系统现有角色体系完全一致
- ✅ **权限精确**：基于角色的权限控制更精确
- ✅ **扩展性强**：可以轻松增加新角色和新维度
- ✅ **维护简单**：利用现有的角色管理体系

**缺点**：
- ❌ **改动较大**：需要修改数据库、代码、前端
- ❌ **需要迁移**：需要处理历史数据和计算规则

### 2.2 方案对比表

| 对比项 | 方案A：重新定义部门 | 方案B：使用角色 |
|--------|-------------------|----------------|
| **改动量** | ⭐⭐⭐⭐⭐ 最小 | ⭐⭐ 较大 |
| **实施速度** | ⭐⭐⭐⭐⭐ 最快 | ⭐⭐⭐ 需要迁移 |
| **概念清晰度** | ⭐⭐ 混淆 | ⭐⭐⭐⭐⭐ 清晰 |
| **语义一致性** | ⭐⭐ 不一致 | ⭐⭐⭐⭐⭐ 一致 |
| **权限控制** | ⭐⭐ 混乱 | ⭐⭐⭐⭐⭐ 精确 |
| **扩展性** | ⭐⭐ 差 | ⭐⭐⭐⭐⭐ 强 |
| **维护成本** | ⭐⭐ 高 | ⭐⭐⭐⭐ 低 |
| **长期适用性** | ⭐⭐ 可能冲突 | ⭐⭐⭐⭐⭐ 长期适用 |

### 2.3 关键决策点

**如果选择方案A（重新定义部门）：**
- ✅ 适合：快速上线，临时方案，短期使用
- ❌ 不适合：长期维护，需要清晰的权限控制，需要扩展

**如果选择方案B（使用角色）：**
- ✅ 适合：长期使用，需要清晰的权限控制，需要扩展
- ❌ 不适合：需要立即上线，无法接受改动

### 2.4 推荐结论

**强烈推荐方案B（使用角色）**，原因：

1. **符合公司场景**：正如您所说，角色更符合公司实际场景
2. **概念清晰**：不会造成理解上的混淆
3. **长期价值**：虽然改动较大，但长期来看更易维护和扩展
4. **避免技术债**：方案A虽然改动小，但会留下技术债，未来可能需要重构

**如果确实需要快速上线**，可以考虑：
- 短期使用方案A快速上线
- 同时规划方案B的实施
- 在合适的时间点迁移到方案B

---

## 三、当前架构分析

### 3.1 当前数据模型

```
字段配置表 (credit_field_config)
├── dept_id: 部门ID（0表示通用字段）
├── field_code: 字段编码
├── field_name: 字段名称
└── ...

表单数据表 (credit_form_data)
├── dept_id: 部门ID
├── report_period: 报送周期
├── report_type: 报表类型（MONTHLY/QUARTERLY）
├── form_data: JSONB格式的字段值
└── 唯一约束：uk_dept_period_type (dept_id, report_period, report_type)

计算规则表 (credit_calculation_rule)
├── rule_expression: "#sum({#dept115[...], #dept116[...], #dept117[...]})"
└── ...
```

### 3.2 系统用户-角色-部门关系

```
用户表 (system_users)
├── id: 用户ID
├── dept_id: 部门ID（用户所属部门）
└── ...

用户角色关联表 (system_user_role)
├── user_id: 用户ID
├── role_id: 角色ID
└── 关系：多对多（一个用户可以有多个角色）

角色表 (system_role)
├── id: 角色ID
├── name: 角色名称
├── code: 角色编码
└── 注意：角色与部门没有直接关系
```

### 3.3 当前设计的局限性

❌ **维度单一**：只能按部门区分，无法支持多维度  
❌ **灵活性不足**：一个部门在一个周期内只能有一个表单  
❌ **扩展性差**：无法支持项目、地区、业务线等维度  
❌ **角色支持缺失**：无法利用现有的角色体系

---

## 四、设计方案（基于角色的方案）

### 4.1 核心设计思路

**将区分维度从"部门"改为"角色"，同时保留部门信息用于辅助查询和权限控制。**

### 4.2 数据库设计变更

#### 4.2.1 字段配置表变更

```sql
-- 方案A：完全替换（推荐用于新系统）
ALTER TABLE credit_field_config 
DROP COLUMN dept_id,
ADD COLUMN role_id BIGINT NOT NULL DEFAULT 0 COMMENT '角色编号（0表示通用字段）';

-- 方案B：兼容过渡（推荐用于已有数据的系统）
ALTER TABLE credit_field_config 
ADD COLUMN role_id BIGINT COMMENT '角色编号（0表示通用字段）',
ADD COLUMN dept_id_backup BIGINT COMMENT '原部门ID（用于数据迁移）';

-- 更新唯一约束
ALTER TABLE credit_field_config 
DROP CONSTRAINT IF EXISTS uk_dept_field,
ADD CONSTRAINT uk_role_field UNIQUE (role_id, field_code, deleted);
```

**字段说明：**
- `role_id = 0`：表示通用字段，所有角色都可以使用
- `role_id > 0`：表示该角色专属的字段配置
- 保留 `dept_id`（可选）：用于辅助查询和权限控制，但不作为区分维度

#### 4.2.2 表单数据表变更

```sql
-- 方案A：完全替换
ALTER TABLE credit_form_data 
DROP COLUMN dept_id,
ADD COLUMN role_id BIGINT NOT NULL COMMENT '角色编号',
ADD COLUMN dept_id BIGINT COMMENT '部门编号（辅助字段，用于权限控制和查询）';

-- 方案B：兼容过渡
ALTER TABLE credit_form_data 
ADD COLUMN role_id BIGINT COMMENT '角色编号',
ADD COLUMN dept_id_backup BIGINT COMMENT '原部门ID（用于数据迁移）';

-- 修改唯一约束
ALTER TABLE credit_form_data 
DROP CONSTRAINT IF EXISTS uk_dept_period_type,
ADD CONSTRAINT uk_role_period_type UNIQUE (role_id, report_period, report_type, deleted);
```

**字段说明：**
- `role_id`：**主要区分字段**，标识该表单属于哪个角色
- `dept_id`：**辅助字段**，记录填写人所属部门，用于：
  - 权限控制（用户只能查看本部门的表单）
  - 数据查询（按部门筛选）
  - 报表展示（显示部门信息）

#### 4.2.3 计算规则表达式变更

**当前表达式：**
```spel
#sum({#dept115['small_micro_enterprises_count'], 
      #dept116['small_micro_enterprises_count'], 
      #dept117['small_micro_enterprises_count']})
```

**新表达式（按角色）：**
```spel
#sum({#role201['small_micro_enterprises_count'], 
      #role202['small_micro_enterprises_count'], 
      #role203['small_micro_enterprises_count']})
```

**或者支持更灵活的表达式：**
```spel
#sum({#roleFormData.values().![#safeGet(#this, 'small_micro_enterprises_count')]})
```

### 4.3 代码变更

#### 4.3.1 实体类变更

```java
// CreditFieldConfigDO.java
public class CreditFieldConfigDO extends BaseDO {
    private Long id;
    // 变更：deptId -> roleId
    private Long roleId;  // 角色编号（0表示通用字段）
    private String fieldCode;
    private String fieldName;
    // 可选：保留deptId作为辅助字段
    private Long deptId;  // 部门编号（辅助字段，用于查询和权限控制）
    // ...
}

// CreditFormDataDO.java
public class CreditFormDataDO extends BaseDO {
    private Long id;
    // 变更：deptId -> roleId（主要区分字段）
    private Long roleId;  // 角色编号
    // 新增：deptId作为辅助字段
    private Long deptId;  // 部门编号（辅助字段）
    private String reportPeriod;
    private String reportType;
    private Map<String, Object> formData;
    // ...
}
```

#### 4.3.2 服务层变更

```java
// CreditFormDataService.java
public interface CreditFormDataService {
    // 变更：按角色获取表单数据
    Map<Long, Map<String, Object>> getRoleFormDataMap(String reportPeriod, String reportType);
    
    // 新增：按角色和周期查询表单
    CreditFormDataDO getByRoleIdAndPeriod(Long roleId, String reportPeriod, String reportType);
    
    // 保留：按部门查询（用于权限控制）
    List<CreditFormDataDO> getByDeptIdAndPeriod(Long deptId, String reportPeriod, String reportType);
}
```

#### 4.3.3 计算上下文构建器变更

```java
// CreditCalculationContextBuilder.java
public EvaluationContext buildContext(String reportPeriod, String reportType) {
    StandardEvaluationContext context = new StandardEvaluationContext();
    
    // 变更：加载各角色表单数据
    Map<Long, Map<String, Object>> roleFormDataMap = 
        formDataService.getRoleFormDataMap(reportPeriod, reportType);
    context.setVariable("roleFormData", roleFormDataMap);
    
    // 为了方便访问，设置每个角色的数据
    roleFormDataMap.forEach((roleId, formData) -> {
        context.setVariable("role" + roleId, formData);
    });
    
    // 可选：保留部门数据访问（用于兼容旧的计算规则）
    Map<Long, Map<String, Object>> deptFormDataMap = 
        formDataService.getDeptFormDataMap(reportPeriod, reportType);
    context.setVariable("deptFormData", deptFormDataMap);
    deptFormDataMap.forEach((deptId, formData) -> {
        context.setVariable("dept" + deptId, formData);
    });
    
    // ... 其他逻辑
    return context;
}
```

#### 4.3.4 控制器变更

```java
// CreditFormDataController.java
@PostMapping("/create")
public CommonResult<Long> createFormData(@Valid @RequestBody CreditFormDataSaveReqVO createReqVO) {
    // 变更：从请求中获取roleId，而不是deptId
    // 同时从当前用户信息中获取deptId作为辅助字段
    Long userId = getLoginUserId();
    AdminUserRespDTO user = adminUserApi.getUser(userId);
    
    // 如果请求中没有指定deptId，使用用户所属部门
    if (createReqVO.getDeptId() == null && user != null) {
        createReqVO.setDeptId(user.getDeptId());
    }
    
    return success(formDataService.createFormData(createReqVO, userId));
}
```

### 4.4 前端交互变更

#### 4.4.1 表单填写流程

```
1. 用户登录系统
2. 系统获取用户的所有角色列表
3. 用户选择要填写的角色（下拉选择）
4. 根据选择的角色，加载对应的字段配置
5. 用户填写表单
6. 提交时，同时保存 roleId 和 deptId（用户所属部门）
```

#### 4.4.2 角色选择界面

```typescript
// 角色选择组件
interface RoleSelectorProps {
  userId: number;
  onRoleSelect: (roleId: number) => void;
}

// 表单填写页面
const FormFillPage = () => {
  const [selectedRoleId, setSelectedRoleId] = useState<number | null>(null);
  const [fieldConfigs, setFieldConfigs] = useState<FieldConfig[]>([]);
  
  // 加载用户角色列表
  useEffect(() => {
    loadUserRoles().then(roles => {
      // 显示角色选择器
    });
  }, []);
  
  // 选择角色后，加载字段配置
  useEffect(() => {
    if (selectedRoleId) {
      loadFieldConfigsByRole(selectedRoleId).then(configs => {
        setFieldConfigs(configs);
      });
    }
  }, [selectedRoleId]);
  
  return (
    <div>
      <RoleSelector onRoleSelect={setSelectedRoleId} />
      {selectedRoleId && <FormFields configs={fieldConfigs} />}
    </div>
  );
};
```

---

## 五、数据迁移方案

### 5.1 迁移策略

由于现有数据都是按部门组织的，需要进行数据迁移：

#### 5.1.1 部门到角色的映射策略

**策略1：一对一映射（简单场景）**
```sql
-- 假设：每个部门对应一个默认角色
-- 创建部门默认角色
INSERT INTO system_role (name, code, ...) 
SELECT CONCAT(dept.name, '默认角色'), CONCAT('dept_', dept.id, '_default'), ...
FROM system_dept dept;

-- 为部门用户分配默认角色
INSERT INTO system_user_role (user_id, role_id, ...)
SELECT u.id, r.id, ...
FROM system_users u
JOIN system_role r ON r.code = CONCAT('dept_', u.dept_id, '_default')
WHERE u.dept_id IS NOT NULL;
```

**策略2：手动配置映射（复杂场景）**
```sql
-- 创建映射表
CREATE TABLE credit_dept_role_mapping (
    id BIGINT PRIMARY KEY,
    dept_id BIGINT NOT NULL,
    role_id BIGINT NOT NULL,
    mapping_type VARCHAR(50), -- 'DEFAULT', 'MANUAL'
    ...
);

-- 管理员手动配置部门到角色的映射关系
```

#### 5.1.2 数据迁移脚本

```sql
-- 步骤1：备份原数据
CREATE TABLE credit_field_config_backup AS 
SELECT * FROM credit_field_config;

CREATE TABLE credit_form_data_backup AS 
SELECT * FROM credit_form_data;

-- 步骤2：创建部门到角色的映射（假设使用策略1）
-- 这里需要根据实际情况创建角色和映射关系

-- 步骤3：迁移字段配置
-- 方案A：如果有明确的部门-角色映射
UPDATE credit_field_config cfc
SET role_id = (
    SELECT role_id 
    FROM credit_dept_role_mapping 
    WHERE dept_id = cfc.dept_id 
    LIMIT 1
)
WHERE dept_id > 0;

-- 方案B：如果没有映射，先设置为0（通用字段），后续手动配置
UPDATE credit_field_config 
SET role_id = 0 
WHERE dept_id > 0;

-- 步骤4：迁移表单数据
UPDATE credit_form_data cfd
SET role_id = (
    SELECT role_id 
    FROM credit_dept_role_mapping 
    WHERE dept_id = cfd.dept_id 
    LIMIT 1
),
dept_id = cfd.dept_id  -- 保留原部门ID作为辅助字段
WHERE dept_id IS NOT NULL;

-- 步骤5：验证数据完整性
SELECT 
    '字段配置迁移' as item,
    COUNT(*) as total_count,
    COUNT(CASE WHEN role_id IS NOT NULL THEN 1 END) as migrated_count
FROM credit_field_config
UNION ALL
SELECT 
    '表单数据迁移',
    COUNT(*),
    COUNT(CASE WHEN role_id IS NOT NULL THEN 1 END)
FROM credit_form_data;
```

### 5.2 迁移风险与应对

| 风险 | 影响 | 应对措施 |
|------|------|---------|
| 数据丢失 | 高 | 完整备份，分阶段迁移，支持回滚 |
| 计算规则失效 | 高 | 同时支持新旧表达式，逐步迁移规则 |
| 权限控制混乱 | 中 | 保留部门信息，双重权限检查 |
| 用户使用习惯改变 | 中 | 提供角色选择引导，用户培训 |

---

## 六、权限控制方案

### 6.1 权限控制策略

#### 6.1.1 双重权限控制

```java
// 权限控制逻辑
public boolean canAccessForm(Long userId, Long formId) {
    // 1. 检查用户是否有该表单对应的角色
    CreditFormDataDO formData = formDataService.getFormData(formId);
    if (formData == null) {
        return false;
    }
    
    // 检查用户是否拥有该角色
    List<Long> userRoleIds = userRoleService.getUserRoleIds(userId);
    if (!userRoleIds.contains(formData.getRoleId())) {
        return false;
    }
    
    // 2. 检查部门权限（可选，用于更严格的权限控制）
    AdminUserRespDTO user = adminUserApi.getUser(userId);
    if (user != null && user.getDeptId() != null) {
        // 如果配置了部门权限，则检查部门是否匹配
        if (formData.getDeptId() != null && !formData.getDeptId().equals(user.getDeptId())) {
            // 可以根据业务需求决定是否允许跨部门查看
            // return false;  // 严格模式：不允许跨部门
        }
    }
    
    return true;
}
```

#### 6.1.2 角色权限配置

```java
// 角色权限配置表（可选）
CREATE TABLE credit_role_form_permission (
    id BIGINT PRIMARY KEY,
    role_id BIGINT NOT NULL,
    form_type VARCHAR(50),  -- 表单类型
    can_view BOOLEAN DEFAULT TRUE,
    can_edit BOOLEAN DEFAULT TRUE,
    can_submit BOOLEAN DEFAULT TRUE,
    can_approve BOOLEAN DEFAULT FALSE,
    ...
);
```

---

## 七、计算规则迁移方案

### 7.1 计算规则表达式转换

#### 7.1.1 自动转换工具

```java
// 计算规则表达式转换器
public class CreditCalculationRuleConverter {
    
    /**
     * 将基于部门的表达式转换为基于角色的表达式
     * 
     * @param oldExpression 旧表达式（基于部门）
     * @param deptRoleMapping 部门到角色的映射
     * @return 新表达式（基于角色）
     */
    public String convertDeptToRole(String oldExpression, 
                                     Map<Long, Long> deptRoleMapping) {
        // 示例转换：
        // #dept115[...] -> #role201[...]
        // #dept116[...] -> #role202[...]
        
        String newExpression = oldExpression;
        for (Map.Entry<Long, Long> entry : deptRoleMapping.entrySet()) {
            Long deptId = entry.getKey();
            Long roleId = entry.getValue();
            newExpression = newExpression.replaceAll(
                "#dept" + deptId, 
                "#role" + roleId
            );
        }
        return newExpression;
    }
    
    /**
     * 批量转换计算规则
     */
    public void batchConvertRules(Map<Long, Long> deptRoleMapping) {
        List<CreditCalculationRuleDO> rules = calculationRuleMapper.selectList(null);
        for (CreditCalculationRuleDO rule : rules) {
            String newExpression = convertDeptToRole(
                rule.getRuleExpression(), 
                deptRoleMapping
            );
            rule.setRuleExpression(newExpression);
            calculationRuleMapper.updateById(rule);
        }
    }
}
```

#### 7.1.2 兼容模式

```java
// 计算上下文构建器支持兼容模式
public EvaluationContext buildContext(String reportPeriod, String reportType) {
    StandardEvaluationContext context = new StandardEvaluationContext();
    
    // 同时加载角色数据和部门数据（兼容旧规则）
    Map<Long, Map<String, Object>> roleFormDataMap = 
        formDataService.getRoleFormDataMap(reportPeriod, reportType);
    Map<Long, Map<String, Object>> deptFormDataMap = 
        formDataService.getDeptFormDataMap(reportPeriod, reportType);
    
    // 设置角色数据
    context.setVariable("roleFormData", roleFormDataMap);
    roleFormDataMap.forEach((roleId, formData) -> {
        context.setVariable("role" + roleId, formData);
    });
    
    // 设置部门数据（兼容旧规则）
    context.setVariable("deptFormData", deptFormDataMap);
    deptFormDataMap.forEach((deptId, formData) -> {
        context.setVariable("dept" + deptId, formData);
    });
    
    return context;
}
```

---

## 八、实施计划

### 8.1 分阶段实施

#### 阶段1：准备阶段（1-2周）
- [ ] 需求确认和方案评审
- [ ] 创建部门到角色的映射关系
- [ ] 准备数据迁移脚本
- [ ] 准备回滚方案

#### 阶段2：数据库变更（1周）
- [ ] 备份现有数据
- [ ] 执行数据库结构变更
- [ ] 执行数据迁移
- [ ] 验证数据完整性

#### 阶段3：代码变更（2-3周）
- [ ] 修改实体类（DO、VO）
- [ ] 修改Mapper和Service层
- [ ] 修改Controller层
- [ ] 修改计算规则相关代码
- [ ] 单元测试和集成测试

#### 阶段4：前端变更（1-2周）
- [ ] 修改角色选择组件
- [ ] 修改表单填写页面
- [ ] 修改表单列表页面
- [ ] 修改权限控制逻辑

#### 阶段5：测试与上线（1-2周）
- [ ] 功能测试
- [ ] 性能测试
- [ ] 用户验收测试
- [ ] 生产环境部署
- [ ] 数据迁移验证

#### 阶段6：后续优化（持续）
- [ ] 监控系统运行情况
- [ ] 收集用户反馈
- [ ] 优化性能
- [ ] 逐步移除兼容代码

### 8.2 风险控制

1. **数据安全**
   - 完整备份所有数据
   - 在测试环境充分验证
   - 准备回滚脚本

2. **业务连续性**
   - 支持新旧系统并行运行
   - 逐步切换，不一次性全部切换
   - 保留兼容模式

3. **用户体验**
   - 提供角色选择引导
   - 用户培训和支持
   - 收集反馈并快速响应

---

## 九、方案优势与劣势

### 9.1 优势

✅ **灵活性高**：支持多维度（项目、地区、业务线等）  
✅ **扩展性强**：可以轻松增加新的角色和表单类型  
✅ **权限精确**：基于角色的权限控制更精确  
✅ **复用现有体系**：充分利用系统现有的角色体系  
✅ **支持多表单**：同一部门可以填写多个表单  
✅ **支持多角色用户**：一个用户可以拥有多个角色，填写多个表单

### 9.2 劣势

❌ **改动较大**：需要修改数据库、代码、前端  
❌ **迁移复杂**：需要处理历史数据和计算规则  
❌ **学习成本**：用户需要理解角色概念  
❌ **权限复杂**：权限控制逻辑更复杂

### 9.3 适用场景

✅ **强烈推荐**：需要支持多维度、多表单的场景  
✅ **推荐**：角色体系已经建立，需要充分利用  
✅ **推荐**：需要灵活的权限控制  
❌ **不推荐**：简单的单表单、单维度场景

---

## 十、总结与建议

### 10.1 核心结论

基于您的需求，**强烈推荐采用"基于角色的表单区分"方案**，原因如下：

1. **完全满足需求**：支持多表单、多维度、按角色汇总
2. **充分利用现有体系**：系统已有完整的角色体系
3. **扩展性强**：未来可以轻松支持新的维度
4. **权限精确**：基于角色的权限控制更符合业务需求

### 10.2 关键建议

1. **保留部门信息**：虽然主要区分字段改为角色，但建议保留 `dept_id` 作为辅助字段，用于：
   - 权限控制（部门级别的权限）
   - 数据查询（按部门筛选）
   - 报表展示（显示部门信息）

2. **分阶段实施**：不要一次性全部切换，建议：
   - 先支持角色和部门并行
   - 逐步迁移数据
   - 逐步切换计算规则
   - 最后移除部门相关代码

3. **数据迁移策略**：建议使用"手动配置映射"策略，因为：
   - 部门到角色的映射关系可能不是一对一的
   - 需要业务人员确认映射关系
   - 可以更精确地控制迁移过程

4. **兼容性考虑**：在过渡期间，建议同时支持：
   - 基于角色的表达式：`#role201[...]`
   - 基于部门的表达式：`#dept115[...]`（兼容旧规则）

### 10.3 下一步行动

1. **确认方案**：评审本方案，确认是否符合需求
2. **制定详细计划**：根据实际情况制定详细的实施计划
3. **准备映射关系**：确定部门到角色的映射关系
4. **准备测试环境**：搭建测试环境，验证方案可行性
5. **开始实施**：按照分阶段计划开始实施

---

## 十一、附录

### 11.1 数据库变更脚本示例

```sql
-- 完整的数据库变更脚本（PostgreSQL）
BEGIN;

-- 1. 备份表
CREATE TABLE credit_field_config_backup AS SELECT * FROM credit_field_config;
CREATE TABLE credit_form_data_backup AS SELECT * FROM credit_form_data;

-- 2. 字段配置表变更
ALTER TABLE credit_field_config 
ADD COLUMN role_id BIGINT,
ADD COLUMN dept_id_backup BIGINT;

-- 备份原dept_id
UPDATE credit_field_config SET dept_id_backup = dept_id;

-- 3. 表单数据表变更
ALTER TABLE credit_form_data 
ADD COLUMN role_id BIGINT,
ADD COLUMN dept_id_backup BIGINT;

-- 备份原dept_id
UPDATE credit_form_data SET dept_id_backup = dept_id;

-- 4. 创建部门到角色映射表（需要根据实际情况填充）
CREATE TABLE credit_dept_role_mapping (
    id BIGINT PRIMARY KEY,
    dept_id BIGINT NOT NULL,
    role_id BIGINT NOT NULL,
    mapping_type VARCHAR(50) DEFAULT 'MANUAL',
    creator VARCHAR(64),
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    UNIQUE(dept_id, role_id)
);

-- 5. 执行数据迁移（需要先填充映射表）
-- UPDATE credit_field_config cfc
-- SET role_id = (
--     SELECT role_id 
--     FROM credit_dept_role_mapping 
--     WHERE dept_id = cfc.dept_id_backup 
--     LIMIT 1
-- )
-- WHERE dept_id_backup > 0;

-- UPDATE credit_form_data cfd
-- SET role_id = (
--     SELECT role_id 
--     FROM credit_dept_role_mapping 
--     WHERE dept_id = cfd.dept_id_backup 
--     LIMIT 1
-- )
-- WHERE dept_id_backup IS NOT NULL;

-- 6. 修改唯一约束
-- ALTER TABLE credit_field_config 
-- DROP CONSTRAINT IF EXISTS uk_dept_field,
-- ADD CONSTRAINT uk_role_field UNIQUE (role_id, field_code, deleted);

-- ALTER TABLE credit_form_data 
-- DROP CONSTRAINT IF EXISTS uk_dept_period_type,
-- ADD CONSTRAINT uk_role_period_type UNIQUE (role_id, report_period, report_type, deleted);

COMMIT;
```

### 11.2 代码变更清单

- [ ] `CreditFieldConfigDO.java` - 增加 roleId 字段
- [ ] `CreditFormDataDO.java` - 增加 roleId 字段，保留 deptId
- [ ] `CreditFieldConfigMapper.java` - 修改查询方法
- [ ] `CreditFormDataMapper.java` - 修改查询方法，增加按角色查询
- [ ] `CreditFormDataService.java` - 修改服务方法
- [ ] `CreditFormDataServiceImpl.java` - 实现按角色的业务逻辑
- [ ] `CreditCalculationContextBuilder.java` - 修改上下文构建逻辑
- [ ] `CreditFormDataController.java` - 修改控制器，支持角色选择
- [ ] 所有相关的 VO 类 - 增加 roleId 字段
- [ ] 计算规则转换工具类 - 新增

### 11.3 测试用例

- [ ] 角色选择功能测试
- [ ] 按角色加载字段配置测试
- [ ] 表单创建和提交测试
- [ ] 按角色查询表单测试
- [ ] 计算规则表达式测试（新旧表达式）
- [ ] 权限控制测试
- [ ] 数据迁移验证测试
- [ ] 性能测试

---

**报告完成时间**：2025-01-XX  
**报告版本**：v1.0  
**下一步**：等待方案确认后，制定详细实施计划
