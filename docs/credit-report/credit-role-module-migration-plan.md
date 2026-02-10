# 征信模块基于角色的改造方案（实施规划）

## 一、项目概述

### 1.1 目标
将 `wmt-module-credit` 模块复制为新模块 `wmt-module-credit-report`，并将区分维度从 `dept_id`（部门）改为 `role_id`（角色）。

### 1.2 新模块命名
- **模块名**：`wmt-module-credit-report`
- **包名**：`com.wmt.module.credit.role`（或 `com.wmt.module.creditrole`）
- **表名**：保持原表名，或添加 `_role` 后缀（如 `credit_field_config_role`）

### 1.3 核心改动
- **字段配置表**：`dept_id` → `role_id`
- **表单数据表**：`dept_id` → `role_id`（同时保留 `dept_id` 作为辅助字段）
- **计算规则表达式**：`#dept{id}` → `#role{id}`
- **所有相关代码**：涉及 `deptId` 的地方改为 `roleId`

---

## 二、数据库改动清单

### 2.1 表结构变更

#### 2.1.1 credit_field_config 表

**当前结构：**
```sql
CREATE TABLE credit_field_config (
    id BIGINT PRIMARY KEY,
    dept_id BIGINT,  -- 部门编号（0表示通用字段）
    field_code VARCHAR(100),
    field_name VARCHAR(200),
    ...
);
```

**新结构：**
```sql
CREATE TABLE credit_field_config_role (
    id BIGINT PRIMARY KEY,
    role_id BIGINT NOT NULL DEFAULT 0,  -- 角色编号（0表示通用字段）
    dept_id BIGINT,  -- 部门编号（辅助字段，可选，用于查询和权限控制）
    field_code VARCHAR(100),
    field_name VARCHAR(200),
    ...
    UNIQUE KEY uk_role_field (role_id, field_code, deleted)
);
```

**改动说明：**
- 表名：`credit_field_config` → `credit_field_config_role`（可选，或保持原表名）
- 主字段：`dept_id` → `role_id`
- 唯一约束：从 `(dept_id, field_code)` 改为 `(role_id, field_code)`
- 保留 `dept_id` 作为辅助字段（可选）

#### 2.1.2 credit_form_data 表

**当前结构：**
```sql
CREATE TABLE credit_form_data (
    id BIGINT PRIMARY KEY,
    dept_id BIGINT,  -- 部门编号
    report_period VARCHAR(20),
    report_type VARCHAR(20),
    form_data JSONB,
    ...
    UNIQUE KEY uk_dept_period_type (dept_id, report_period, report_type, deleted)
);
```

**新结构：**
```sql
CREATE TABLE credit_form_data_role (
    id BIGINT PRIMARY KEY,
    role_id BIGINT NOT NULL,  -- 角色编号（主要区分字段）
    dept_id BIGINT,  -- 部门编号（辅助字段，记录填写人所属部门）
    report_period VARCHAR(20),
    report_type VARCHAR(20),
    form_data JSONB,
    ...
    UNIQUE KEY uk_role_period_type (role_id, report_period, report_type, deleted)
);
```

**改动说明：**
- 表名：`credit_form_data` → `credit_form_data_role`（可选）
- 主字段：`dept_id` → `role_id`（主要区分字段）
- 保留 `dept_id` 作为辅助字段（记录填写人所属部门）
- 唯一约束：从 `(dept_id, report_period, report_type)` 改为 `(role_id, report_period, report_type)`

#### 2.1.3 credit_calculation_rule 表

**当前结构：**
```sql
CREATE TABLE credit_calculation_rule (
    id BIGINT PRIMARY KEY,
    rule_expression TEXT,  -- 例如："#sum({#dept115[...], #dept116[...]})"
    ...
);
```

**新结构：**
```sql
CREATE TABLE credit_calculation_rule_role (
    id BIGINT PRIMARY KEY,
    rule_expression TEXT,  -- 例如："#sum({#role201[...], #role202[...]})"
    ...
);
```

**改动说明：**
- 表名：`credit_calculation_rule` → `credit_calculation_rule_role`（可选）
- 表达式格式：`#dept{id}` → `#role{id}`

#### 2.1.4 其他表

以下表可能也需要调整，但主要改动在字段配置和表单数据表：
- `credit_summary_report`：可能需要调整，但主要是数据内容
- `credit_audit_log`：可能需要记录 `role_id`
- `credit_validation_rule`：可能需要按角色区分

---

## 三、代码改动清单

### 3.1 包名和类名改动

#### 3.1.1 包名变更
```
com.wmt.module.credit.*
  ↓
com.wmt.module.credit.role.*
```

#### 3.1.2 类名变更（可选）
如果保持原类名，只需修改包名。如果需要区分，可以添加后缀：
- `CreditFieldConfigDO` → `CreditRoleFieldConfigDO`（可选）
- `CreditFormDataDO` → `CreditRoleFormDataDO`（可选）

**建议**：保持原类名，只修改包名，减少改动量。

### 3.2 数据对象（DO）改动

#### 3.2.1 CreditFieldConfigDO

**文件路径：**
```
wmt-module-credit-report/src/main/java/com/wmt/module/credit/role/dal/dataobject/field/CreditFieldConfigDO.java
```

**改动内容：**
```java
// 原代码
private Long deptId;  // 部门编号（0表示通用字段）

// 新代码
private Long roleId;  // 角色编号（0表示通用字段）
private Long deptId;  // 部门编号（辅助字段，可选）
```

**完整改动：**
```java
@TableName(value = "credit_field_config_role", autoResultMap = true)
@KeySequence("credit_field_config_role_seq")
public class CreditFieldConfigDO extends BaseDO {
    @TableId
    private Long id;
    
    /**
     * 角色编号（0表示通用字段）
     * 关联 {@link com.wmt.module.system.dal.dataobject.permission.RoleDO#getId()}
     */
    private Long roleId;
    
    /**
     * 部门编号（辅助字段，用于查询和权限控制）
     * 关联 {@link com.wmt.module.system.dal.dataobject.dept.DeptDO#getId()}
     */
    private Long deptId;
    
    // ... 其他字段保持不变
}
```

#### 3.2.2 CreditFormDataDO

**文件路径：**
```
wmt-module-credit-report/src/main/java/com/wmt/module/credit/role/dal/dataobject/form/CreditFormDataDO.java
```

**改动内容：**
```java
@TableName(value = "credit_form_data_role", autoResultMap = true)
@KeySequence("credit_form_data_role_seq")
public class CreditFormDataDO extends BaseDO {
    @TableId
    private Long id;
    
    /**
     * 角色编号（主要区分字段）
     * 关联 {@link com.wmt.module.system.dal.dataobject.permission.RoleDO#getId()}
     */
    private Long roleId;
    
    /**
     * 部门编号（辅助字段，记录填写人所属部门）
     * 关联 {@link com.wmt.module.system.dal.dataobject.dept.DeptDO#getId()}
     */
    private Long deptId;
    
    // ... 其他字段保持不变
}
```

### 3.3 VO类改动

#### 3.3.1 CreditFieldConfigSaveReqVO

**改动内容：**
```java
// 原代码
@Schema(description = "部门编号（0表示通用字段）")
@NotNull(message = "部门编号不能为空")
private Long deptId;

// 新代码
@Schema(description = "角色编号（0表示通用字段）")
@NotNull(message = "角色编号不能为空")
private Long roleId;

@Schema(description = "部门编号（辅助字段，可选）")
private Long deptId;
```

#### 3.3.2 CreditFieldConfigRespVO

**改动内容：**
```java
// 原代码
@Schema(description = "部门编号（0表示通用字段）")
private Long deptId;

// 新代码
@Schema(description = "角色编号（0表示通用字段）")
private Long roleId;

@Schema(description = "部门编号（辅助字段）")
private Long deptId;
```

#### 3.3.3 CreditFormDataSaveReqVO

**改动内容：**
```java
// 原代码
@Schema(description = "部门编号")
@NotNull(message = "部门编号不能为空")
private Long deptId;

// 新代码
@Schema(description = "角色编号")
@NotNull(message = "角色编号不能为空")
private Long roleId;

@Schema(description = "部门编号（辅助字段，自动填充）")
private Long deptId;
```

#### 3.3.4 CreditFormDataRespVO

**改动内容：**
```java
// 原代码
@Schema(description = "部门编号")
private Long deptId;

@Schema(description = "部门名称")
private String deptName;

// 新代码
@Schema(description = "角色编号")
private Long roleId;

@Schema(description = "角色名称")
private String roleName;

@Schema(description = "部门编号（辅助字段）")
private Long deptId;

@Schema(description = "部门名称")
private String deptName;
```

#### 3.3.5 CreditFormDataPageReqVO

**改动内容：**
```java
// 原代码
@Schema(description = "部门编号")
private Long deptId;

// 新代码
@Schema(description = "角色编号")
private Long roleId;

@Schema(description = "部门编号（辅助查询字段）")
private Long deptId;
```

#### 3.3.6 CreditFieldConfigPageReqVO

**改动内容：**
```java
// 原代码
@Schema(description = "部门编号")
private Long deptId;

// 新代码
@Schema(description = "角色编号")
private Long roleId;

@Schema(description = "部门编号（辅助查询字段）")
private Long deptId;
```

### 3.4 Mapper接口改动

#### 3.4.1 CreditFieldConfigMapper

**文件路径：**
```
wmt-module-credit-report/src/main/java/com/wmt/module/credit/role/dal/mysql/field/CreditFieldConfigMapper.java
```

**改动内容：**
```java
// 原方法
CreditFieldConfigDO selectByDeptIdAndFieldCode(Long deptId, String fieldCode);

// 新方法
CreditFieldConfigDO selectByRoleIdAndFieldCode(Long roleId, String fieldCode);
List<CreditFieldConfigDO> selectListByRoleId(Long roleId);
List<CreditFieldConfigDO> selectListByDeptId(Long deptId);  // 辅助查询方法
```

#### 3.4.2 CreditFormDataMapper

**文件路径：**
```
wmt-module-credit-report/src/main/java/com/wmt/module/credit/role/dal/mysql/form/CreditFormDataMapper.java
```

**改动内容：**
```java
// 原方法
CreditFormDataDO selectByDeptIdAndPeriod(Long deptId, String reportPeriod, String reportType);
List<CreditFormDataDO> selectListByDeptId(Long deptId);

// 新方法
CreditFormDataDO selectByRoleIdAndPeriod(Long roleId, String reportPeriod, String reportType);
List<CreditFormDataDO> selectListByRoleId(Long roleId);
List<CreditFormDataDO> selectListByDeptId(Long deptId);  // 辅助查询方法

// 新增：按角色获取表单数据Map（用于计算）
Map<Long, Map<String, Object>> selectRoleFormDataMap(String reportPeriod, String reportType);
```

**XML映射文件改动：**
```xml
<!-- 原查询 -->
<select id="selectByDeptIdAndPeriod">
    SELECT * FROM credit_form_data
    WHERE dept_id = #{deptId}
      AND report_period = #{reportPeriod}
      AND report_type = #{reportType}
      AND deleted = 0
</select>

<!-- 新查询 -->
<select id="selectByRoleIdAndPeriod">
    SELECT * FROM credit_form_data_role
    WHERE role_id = #{roleId}
      AND report_period = #{reportPeriod}
      AND report_type = #{reportType}
      AND deleted = 0
</select>

<!-- 新增：按角色获取表单数据Map -->
<select id="selectRoleFormDataMap" resultType="java.util.Map">
    SELECT 
        role_id,
        form_data
    FROM credit_form_data_role
    WHERE report_period = #{reportPeriod}
      AND report_type = #{reportType}
      AND status = 1
      AND deleted = 0
</select>
```

### 3.5 Service层改动

#### 3.5.1 CreditFieldConfigService

**改动内容：**
```java
// 原方法
List<CreditFieldConfigDO> getFieldConfigListByDeptId(Long deptId);

// 新方法
List<CreditFieldConfigDO> getFieldConfigListByRoleId(Long roleId);
List<CreditFieldConfigDO> getFieldConfigListByDeptId(Long deptId);  // 辅助方法
```

#### 3.5.2 CreditFormDataService

**改动内容：**
```java
// 原方法
Map<Long, Map<String, Object>> getDeptFormDataMap(String reportPeriod, String reportType);
CreditFormDataDO getByDeptIdAndPeriod(Long deptId, String reportPeriod, String reportType);

// 新方法
Map<Long, Map<String, Object>> getRoleFormDataMap(String reportPeriod, String reportType);
CreditFormDataDO getByRoleIdAndPeriod(Long roleId, String reportPeriod, String reportType);
Map<Long, Map<String, Object>> getDeptFormDataMap(String reportPeriod, String reportType);  // 辅助方法（兼容）
```

#### 3.5.3 CreditFormDataServiceImpl

**关键改动点：**
```java
@Override
public Long createFormData(CreditFormDataSaveReqVO createReqVO, Long userId) {
    // 1. 如果请求中没有指定deptId，从用户信息中获取
    if (createReqVO.getDeptId() == null) {
        AdminUserRespDTO user = adminUserApi.getUser(userId);
        if (user != null && user.getDeptId() != null) {
            createReqVO.setDeptId(user.getDeptId());
        }
    }
    
    // 2. 验证roleId是否存在
    if (createReqVO.getRoleId() == null) {
        throw new ServiceException("角色编号不能为空");
    }
    
    // 3. 验证用户是否拥有该角色
    List<Long> userRoleIds = permissionApi.getUserRoleIds(userId);
    if (!userRoleIds.contains(createReqVO.getRoleId())) {
        throw new ServiceException("用户不拥有该角色");
    }
    
    // ... 其他逻辑
}

@Override
public Map<Long, Map<String, Object>> getRoleFormDataMap(String reportPeriod, String reportType) {
    List<CreditFormDataDO> formDataList = formDataMapper.selectListForCalculation(reportPeriod, reportType);
    Map<Long, Map<String, Object>> roleFormDataMap = new HashMap<>();
    for (CreditFormDataDO formData : formDataList) {
        roleFormDataMap.put(formData.getRoleId(), formData.getFormData());
    }
    return roleFormDataMap;
}
```

### 3.6 Controller层改动

#### 3.6.1 CreditFieldConfigController

**改动内容：**
- 所有涉及 `deptId` 的参数和响应改为 `roleId`
- 查询条件从按部门改为按角色
- 权限控制改为基于角色

#### 3.6.2 CreditFormDataController

**改动内容：**
```java
@GetMapping("/page")
public CommonResult<PageResult<CreditFormDataRespVO>> getFormDataPage(@Valid CreditFormDataPageReqVO pageReqVO) {
    // 数据权限过滤：非超级管理员只能查看自己拥有的角色对应的表单
    if (pageReqVO.getRoleId() == null) {
        Long userId = getLoginUserId();
        List<Long> userRoleIds = permissionApi.getUserRoleIds(userId);
        if (!userRoleIds.isEmpty()) {
            // 如果用户有角色，默认只显示第一个角色的表单
            // 或者显示所有角色的表单（根据业务需求）
            pageReqVO.setRoleIds(userRoleIds);  // 需要修改PageReqVO支持多个角色
        }
    }
    
    PageResult<CreditFormDataRespVO> pageResult = formDataService.getFormDataPage(pageReqVO, getLoginUserId());
    // 填充角色名称和部门名称
    fillRoleAndDeptNames(pageResult.getList());
    return success(pageResult);
}

private void fillRoleAndDeptNames(List<CreditFormDataRespVO> list) {
    // 获取所有角色ID和部门ID
    List<Long> roleIds = list.stream()
            .map(CreditFormDataRespVO::getRoleId)
            .filter(Objects::nonNull)
            .distinct()
            .toList();
    List<Long> deptIds = list.stream()
            .map(CreditFormDataRespVO::getDeptId)
            .filter(Objects::nonNull)
            .distinct()
            .toList();
    
    // 批量查询角色和部门信息
    Map<Long, RoleRespDTO> roleMap = permissionApi.getRoleMap(roleIds);
    Map<Long, DeptRespDTO> deptMap = deptApi.getDeptMap(deptIds);
    
    // 填充角色名称和部门名称
    list.forEach(respVO -> {
        if (respVO.getRoleId() != null) {
            RoleRespDTO role = roleMap.get(respVO.getRoleId());
            if (role != null) {
                respVO.setRoleName(role.getName());
            }
        }
        if (respVO.getDeptId() != null) {
            DeptRespDTO dept = deptMap.get(respVO.getDeptId());
            if (dept != null) {
                respVO.setDeptName(dept.getName());
            }
        }
    });
}
```

### 3.7 计算引擎改动

#### 3.7.1 CreditCalculationContextBuilder

**文件路径：**
```
wmt-module-credit-report/src/main/java/com/wmt/module/credit/role/framework/calculator/CreditCalculationContextBuilder.java
```

**改动内容：**
```java
public EvaluationContext buildContext(String reportPeriod, String reportType) {
    StandardEvaluationContext context = new StandardEvaluationContext();
    
    // 注册自定义函数
    registerCustomFunctions(context);
    
    // 变更：加载各角色表单数据（而不是各部门）
    Map<Long, Map<String, Object>> roleFormDataMap = 
        formDataService.getRoleFormDataMap(reportPeriod, reportType);
    context.setVariable("roleFormData", roleFormDataMap);
    
    // 为了方便访问，设置每个角色的数据
    roleFormDataMap.forEach((roleId, formData) -> {
        context.setVariable("role" + roleId, formData);
    });
    
    // 可选：保留部门数据访问（用于兼容或辅助查询）
    Map<Long, Map<String, Object>> deptFormDataMap = 
        formDataService.getDeptFormDataMap(reportPeriod, reportType);
    context.setVariable("deptFormData", deptFormDataMap);
    deptFormDataMap.forEach((deptId, formData) -> {
        context.setVariable("dept" + deptId, formData);
    });
    
    // ... 其他逻辑保持不变
    return context;
}
```

#### 3.7.2 计算规则表达式示例

**原表达式：**
```spel
#sum({#dept115['small_micro_enterprises_count'], 
      #dept116['small_micro_enterprises_count'], 
      #dept117['small_micro_enterprises_count']})
```

**新表达式：**
```spel
#sum({#role201['small_micro_enterprises_count'], 
      #role202['small_micro_enterprises_count'], 
      #role203['small_micro_enterprises_count']})
```

**或者使用更灵活的表达式：**
```spel
#sum({#roleFormData.values().![#safeGet(#this, 'small_micro_enterprises_count')]})
```

### 3.8 Convert转换器改动

#### 3.8.1 CreditFieldConfigConvert

**改动内容：**
- 所有涉及 `deptId` 的映射改为 `roleId`
- 同时保留 `deptId` 的映射

#### 3.8.2 CreditFormDataConvert

**改动内容：**
- 所有涉及 `deptId` 的映射改为 `roleId`
- 同时保留 `deptId` 的映射

### 3.9 其他改动

#### 3.9.1 RedisKeyConstants

**改动内容：**
```java
// 原代码
String CREDIT_FIELD_CONFIG_DEPT = "credit:field:config:dept:{}";

// 新代码
String CREDIT_FIELD_CONFIG_ROLE = "credit:field:config:role:{}";
String CREDIT_FIELD_CONFIG_DEPT = "credit:field:config:dept:{}";  // 保留，用于辅助查询
```

#### 3.9.2 枚举类

**CreditOperationTypeEnum**：可能需要添加角色相关的操作类型

#### 3.9.3 审计日志

**CreditAuditLog**：可能需要记录 `roleId`

---

## 四、实施步骤

### 4.1 阶段1：创建新模块（1-2天）

1. **复制模块结构**
   ```bash
   # 复制整个模块目录
   cp -r wmt-module-credit wmt-module-credit-report
   ```

2. **修改pom.xml**
   - 修改 `artifactId` 为 `wmt-module-credit-report`
   - 修改 `description` 为基于角色的征信模块

3. **修改包名**
   - 全局替换：`com.wmt.module.credit` → `com.wmt.module.credit.role`
   - 或者：`com.wmt.module.credit` → `com.wmt.module.creditrole`

4. **修改主pom.xml**
   - 在根 `pom.xml` 中添加新模块：
   ```xml
   <module>wmt-module-credit-report</module>
   ```

### 4.2 阶段2：数据库改动（1天）

1. **创建数据库迁移脚本**
   - 创建新表（或修改表名）
   - 创建序列（PostgreSQL）
   - 创建索引和约束

2. **执行数据库变更**
   - 在测试环境执行
   - 验证表结构

### 4.3 阶段3：DO和VO改动（1-2天）

1. **修改DO类**
   - `CreditFieldConfigDO`
   - `CreditFormDataDO`
   - 其他相关DO

2. **修改VO类**
   - 所有涉及 `deptId` 的VO
   - 添加 `roleId` 字段
   - 添加 `roleName` 字段

### 4.4 阶段4：Mapper和Service改动（2-3天）

1. **修改Mapper接口**
   - 修改方法签名
   - 修改XML映射文件

2. **修改Service接口和实现**
   - 修改方法签名
   - 修改业务逻辑
   - 添加权限验证

### 4.5 阶段5：Controller和计算引擎改动（2-3天）

1. **修改Controller**
   - 修改参数和响应
   - 修改权限控制逻辑
   - 修改数据填充逻辑

2. **修改计算引擎**
   - 修改上下文构建器
   - 修改表达式解析
   - 测试计算规则

### 4.6 阶段6：测试和验证（2-3天）

1. **单元测试**
   - 修改现有测试用例
   - 添加新测试用例

2. **集成测试**
   - 测试完整流程
   - 测试计算规则

3. **性能测试**
   - 测试查询性能
   - 测试计算性能

---

## 五、关键注意事项

### 5.1 命名规范

- **表名**：建议添加 `_role` 后缀，如 `credit_field_config_role`
- **序列名**：对应表名，如 `credit_field_config_role_seq`
- **包名**：`com.wmt.module.credit.role` 或 `com.wmt.module.creditrole`

### 5.2 兼容性考虑

- **保留deptId**：作为辅助字段，用于查询和权限控制
- **兼容模式**：计算引擎可以同时支持角色和部门的表达式
- **数据迁移**：如果需要从原模块迁移数据，需要建立部门到角色的映射

### 5.3 权限控制

- **角色验证**：用户只能填写自己拥有的角色对应的表单
- **部门权限**：可以结合部门权限，实现更细粒度的控制
- **数据权限**：非超级管理员只能查看自己角色的表单

### 5.4 前端改动

- **角色选择器**：用户填写表单前需要选择角色
- **字段加载**：根据选择的角色加载对应的字段配置
- **列表展示**：显示角色名称和部门名称

---

## 六、改动检查清单

### 6.1 数据库
- [ ] 创建新表或修改表结构
- [ ] 创建序列
- [ ] 创建索引和约束
- [ ] 验证表结构

### 6.2 DO类
- [ ] CreditFieldConfigDO
- [ ] CreditFormDataDO
- [ ] 其他相关DO

### 6.3 VO类
- [ ] CreditFieldConfigSaveReqVO
- [ ] CreditFieldConfigRespVO
- [ ] CreditFieldConfigPageReqVO
- [ ] CreditFormDataSaveReqVO
- [ ] CreditFormDataRespVO
- [ ] CreditFormDataPageReqVO

### 6.4 Mapper
- [ ] CreditFieldConfigMapper接口
- [ ] CreditFieldConfigMapper.xml
- [ ] CreditFormDataMapper接口
- [ ] CreditFormDataMapper.xml

### 6.5 Service
- [ ] CreditFieldConfigService接口
- [ ] CreditFieldConfigServiceImpl实现
- [ ] CreditFormDataService接口
- [ ] CreditFormDataServiceImpl实现

### 6.6 Controller
- [ ] CreditFieldConfigController
- [ ] CreditFormDataController

### 6.7 计算引擎
- [ ] CreditCalculationContextBuilder
- [ ] 计算规则表达式

### 6.8 其他
- [ ] Convert转换器
- [ ] RedisKeyConstants
- [ ] 枚举类
- [ ] 审计日志

### 6.9 测试
- [ ] 单元测试
- [ ] 集成测试
- [ ] 性能测试

---

## 七、后续工作

### 7.1 数据迁移（如果需要）

如果要从原 `credit` 模块迁移数据到新模块，需要：
1. 建立部门到角色的映射关系
2. 执行数据迁移脚本
3. 验证数据完整性

### 7.2 前端开发

1. 角色选择组件
2. 表单填写页面调整
3. 列表页面调整
4. 权限控制调整

### 7.3 文档更新

1. API文档
2. 用户手册
3. 开发文档

---

**文档版本**：v1.0  
**创建时间**：2025-01-XX  
**最后更新**：2025-01-XX
