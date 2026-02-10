# Credit模块后端实施计划

## 一、项目概述

**模块名称**：`wmt-module-credit`  
**模块描述**：征信报送模块，实现各部门表单填报、数据汇总计算、月报季报生成等功能  
**技术栈**：Spring Boot 3.5.9 + JDK 17 + MyBatis Plus + PostgreSQL 15 + SpEL

---

## 二、任务规划总览

### 2.1 开发阶段划分

| 阶段 | 阶段名称 | 预计时间 | 优先级 |
|------|---------|---------|--------|
| 阶段1 | 模块基础搭建 | 1天 | P0 |
| 阶段2 | 数据访问层 | 2天 | P0 |
| 阶段3 | 枚举和常量 | 1天 | P0 |
| 阶段4 | 字段配置管理 | 2天 | P0 |
| 阶段5 | 表单数据管理 | 3天 | P0 |
| 阶段6 | 计算引擎核心 | 3天 | P0 |
| 阶段7 | 报表生成服务 | 2天 | P0 |
| 阶段8 | 校验规则服务 | 1天 | P1 |
| 阶段9 | 审计日志服务 | 1天 | P1 |
| 阶段10 | 定时任务 | 2天 | P0 |
| 阶段11 | Excel导出 | 1天 | P1 |
| 阶段12 | 邮件通知 | 1天 | P1 |
| 阶段13 | 单元测试 | 2天 | P1 |
| **总计** | | **22天** | |

---

## 三、详细任务清单

### 阶段1：模块基础搭建（1天）

#### 任务1.1：创建Maven模块
**文件**：`wmt-module-credit/pom.xml`

**任务内容**：
- [ ] 创建 `wmt-module-credit` 模块目录结构
- [ ] 配置 `pom.xml`，参考 `wmt-module-crm/pom.xml`
- [ ] 添加依赖：
  - `wmt-module-system`
  - `wmt-module-infra`
  - `wmt-spring-boot-starter-web`
  - `wmt-spring-boot-starter-security`
  - `wmt-spring-boot-starter-mybatis`
  - `wmt-spring-boot-starter-excel`
  - `wmt-spring-boot-starter-test`
  - `wmt-spring-boot-starter-redis`（用于缓存）

**验收标准**：
- [ ] 模块可以正常编译
- [ ] 依赖关系正确

---

#### 任务1.2：创建包结构
**目录结构**：
```
wmt-module-credit/
├── src/main/java/com/wmt/module/credit/
│   ├── controller/admin/
│   │   ├── field/
│   │   ├── form/
│   │   ├── report/
│   │   ├── calculation/
│   │   ├── validation/
│   │   └── audit/
│   ├── service/
│   │   ├── field/
│   │   ├── form/
│   │   ├── report/
│   │   ├── calculation/
│   │   ├── validation/
│   │   ├── audit/
│   │   └── notification/
│   ├── dal/
│   │   ├── dataobject/
│   │   │   ├── field/
│   │   │   ├── form/
│   │   │   ├── report/
│   │   │   ├── calculation/
│   │   │   ├── validation/
│   │   │   └── audit/
│   │   ├── mysql/
│   │   │   ├── field/
│   │   │   ├── form/
│   │   │   ├── report/
│   │   │   ├── calculation/
│   │   │   ├── validation/
│   │   │   └── audit/
│   │   └── redis/
│   ├── convert/
│   ├── enums/
│   ├── framework/
│   │   ├── calculator/
│   │   └── validator/
│   └── job/
└── src/main/resources/
    └── mapper/
```

**任务内容**：
- [ ] 创建所有包目录
- [ ] 创建 `package-info.java` 文件（参考其他模块）

**验收标准**：
- [ ] 包结构完整
- [ ] 所有包都有 `package-info.java`

---

### 阶段2：数据访问层（2天）

#### 任务2.1：字段配置表（DO + Mapper）
**文件**：
- `dal/dataobject/field/CreditFieldConfigDO.java`
- `dal/mysql/field/CreditFieldConfigMapper.java`
- `resources/mapper/field/CreditFieldConfigMapper.xml`

**任务内容**：
- [ ] 创建 `CreditFieldConfigDO`，字段：
  - `id`, `deptId`, `fieldCode`, `fieldName`, `fieldType`
  - `required`, `defaultValue`, `validationRule` (JSONB)
  - `displayOrder`, `status`
  - 继承 `BaseDO`
- [ ] 创建 `CreditFieldConfigMapper`，方法：
  - `selectPage()` - 分页查询
  - `selectListByDeptId()` - 根据部门ID查询
  - `selectListByReportType()` - 根据报表类型查询
  - `selectByDeptIdAndFieldCode()` - 根据部门和字段编码查询
- [ ] 创建 MyBatis XML映射文件

**验收标准**：
- [ ] DO类字段完整，使用 `@TableName("credit_field_config")`
- [ ] Mapper方法完整，支持分页和条件查询
- [ ] XML映射文件正确

---

#### 任务2.2：表单数据表（DO + Mapper）
**文件**：
- `dal/dataobject/form/CreditFormDataDO.java`
- `dal/mysql/form/CreditFormDataMapper.java`
- `resources/mapper/form/CreditFormDataMapper.xml`

**任务内容**：
- [ ] 创建 `CreditFormDataDO`，字段：
  - `id`, `deptId`, `reportPeriod`, `reportType`
  - `formData` (JSONB，使用 `JacksonTypeHandler`)
  - `status`, `submitUserId`, `submitTime`
  - 继承 `BaseDO`
- [ ] 创建 `CreditFormDataMapper`，方法：
  - `selectPage()` - 分页查询
  - `selectByDeptIdAndPeriod()` - 根据部门和周期查询
  - `selectListByPeriodAndType()` - 根据周期和类型查询
  - `selectDeptFormDataMap()` - 查询各部门表单数据Map（用于计算）
- [ ] 创建 MyBatis XML映射文件
- [ ] **重要**：JSONB字段查询使用PostgreSQL的JSON操作符

**验收标准**：
- [ ] DO类字段完整，JSONB字段使用正确的TypeHandler
- [ ] Mapper方法完整，支持JSONB字段查询
- [ ] XML映射文件正确，包含JSONB查询示例

---

#### 任务2.3：计算规则表（DO + Mapper）
**文件**：
- `dal/dataobject/calculation/CreditCalculationRuleDO.java`
- `dal/mysql/calculation/CreditCalculationRuleMapper.java`
- `resources/mapper/calculation/CreditCalculationRuleMapper.xml`

**任务内容**：
- [ ] 创建 `CreditCalculationRuleDO`，字段：
  - `id`, `targetFieldCode`, `reportType`, `ruleType`
  - `ruleExpression`, `dataSource` (JSONB)
  - `calculationOrder`, `description`, `status`
  - 继承 `BaseDO`
- [ ] 创建 `CreditCalculationRuleMapper`，方法：
  - `selectPage()` - 分页查询
  - `selectListByReportType()` - 根据报表类型查询（按calculationOrder排序）
  - `selectByTargetFieldCode()` - 根据目标字段查询
- [ ] 创建 MyBatis XML映射文件

**验收标准**：
- [ ] DO类字段完整
- [ ] Mapper方法完整，支持按计算顺序排序
- [ ] XML映射文件正确

---

#### 任务2.4：汇总报表表（DO + Mapper）
**文件**：
- `dal/dataobject/report/CreditSummaryReportDO.java`
- `dal/mysql/report/CreditSummaryReportMapper.java`
- `resources/mapper/report/CreditSummaryReportMapper.xml`

**任务内容**：
- [ ] 创建 `CreditSummaryReportDO`，字段：
  - `id`, `reportPeriod`, `reportType`
  - `reportData` (JSONB，使用 `JacksonTypeHandler`)
  - `calculationLog`, `status`, `generateTime`
  - 继承 `BaseDO`
- [ ] 创建 `CreditSummaryReportMapper`，方法：
  - `selectPage()` - 分页查询
  - `selectByPeriodAndType()` - 根据周期和类型查询
  - `selectListByPeriodRange()` - 根据周期范围查询（用于季报计算）
- [ ] 创建 MyBatis XML映射文件

**验收标准**：
- [ ] DO类字段完整
- [ ] Mapper方法完整
- [ ] XML映射文件正确

---

#### 任务2.5：审计日志表（DO + Mapper）
**文件**：
- `dal/dataobject/audit/CreditAuditLogDO.java`
- `dal/mysql/audit/CreditAuditLogMapper.java`
- `resources/mapper/audit/CreditAuditLogMapper.xml`

**任务内容**：
- [ ] 创建 `CreditAuditLogDO`，字段：
  - `id`, `bizType`, `bizId`, `operationType`
  - `operationUserId`, `operationDesc`
  - `beforeData` (JSONB), `afterData` (JSONB)
  - `changeFields`, `ipAddress`, `userAgent`
  - `createTime`（不继承BaseDO，审计日志不删除）
- [ ] 创建 `CreditAuditLogMapper`，方法：
  - `selectPage()` - 分页查询
  - `selectListByBizTypeAndBizId()` - 根据业务类型和ID查询
- [ ] 创建 MyBatis XML映射文件

**验收标准**：
- [ ] DO类字段完整
- [ ] Mapper方法完整
- [ ] XML映射文件正确

---

#### 任务2.6：校验规则表（DO + Mapper）
**文件**：
- `dal/dataobject/validation/CreditValidationRuleDO.java`
- `dal/mysql/validation/CreditValidationRuleMapper.java`
- `resources/mapper/validation/CreditValidationRuleMapper.xml`

**任务内容**：
- [ ] 创建 `CreditValidationRuleDO`，字段：
  - `id`, `fieldCode`, `ruleType`, `ruleExpression`
  - `errorMessage`, `priority`, `status`
  - 继承 `BaseDO`
- [ ] 创建 `CreditValidationRuleMapper`，方法：
  - `selectPage()` - 分页查询
  - `selectListByFieldCode()` - 根据字段编码查询（按priority排序）
- [ ] 创建 MyBatis XML映射文件

**验收标准**：
- [ ] DO类字段完整
- [ ] Mapper方法完整
- [ ] XML映射文件正确

---

#### 任务2.7：Redis常量定义
**文件**：`dal/redis/RedisKeyConstants.java`

**任务内容**：
- [ ] 定义Redis Key常量：
  - `CREDIT_FIELD_CONFIG` - 字段配置缓存
  - `CREDIT_FORM_DATA` - 表单数据缓存
  - `CREDIT_CALCULATION_RULE` - 计算规则缓存
  - `CREDIT_SUMMARY_REPORT` - 汇总报表缓存

**验收标准**：
- [ ] 所有Redis Key常量定义完整
- [ ] Key格式统一：`credit:{module}:{id}`

---

### 阶段3：枚举和常量（1天）

#### 任务3.1：错误码常量
**文件**：`enums/ErrorCodeConstants.java`

**任务内容**：
- [ ] 定义错误码（使用 1-050-000-000 段）：
  - 字段配置相关：`FIELD_CONFIG_NOT_EXISTS`, `FIELD_CONFIG_CODE_DUPLICATE` 等
  - 表单数据相关：`FORM_DATA_NOT_EXISTS`, `FORM_DATA_DEPT_MISMATCH`, `FORM_DATA_STATUS_ERROR` 等
  - 计算规则相关：`CALCULATION_RULE_NOT_EXISTS`, `CALCULATION_EXECUTE_FAIL` 等
  - 报表相关：`SUMMARY_REPORT_NOT_EXISTS`, `SUMMARY_REPORT_GENERATE_FAIL` 等
  - 校验相关：`VALIDATION_FAIL`, `VALIDATION_RULE_NOT_EXISTS` 等

**验收标准**：
- [ ] 错误码定义完整，覆盖所有业务场景
- [ ] 错误码格式统一：`1_050_XXX_XXX`

---

#### 任务3.2：业务枚举类
**文件**：
- `enums/CreditFormStatusEnum.java` - 表单状态枚举
- `enums/CreditReportTypeEnum.java` - 报表类型枚举
- `enums/CreditReportStatusEnum.java` - 报表状态枚举
- `enums/CreditFieldTypeEnum.java` - 字段类型枚举
- `enums/CreditRuleTypeEnum.java` - 规则类型枚举
- `enums/CreditOperationTypeEnum.java` - 操作类型枚举
- `enums/CreditBizTypeEnum.java` - 业务类型枚举

**任务内容**：
- [ ] 创建所有枚举类，参考 `CrmAuditStatusEnum.java`
- [ ] 每个枚举实现 `ArrayValuable<Integer>` 接口
- [ ] 定义枚举值和名称

**验收标准**：
- [ ] 所有枚举类定义完整
- [ ] 枚举值符合业务需求

---

#### 任务3.3：字典类型常量
**文件**：`enums/DictTypeConstants.java`

**任务内容**：
- [ ] 定义字典类型常量：
  - `CREDIT_FIELD_TYPE` - 字段类型
  - `CREDIT_REPORT_TYPE` - 报表类型
  - `CREDIT_FORM_STATUS` - 表单状态
  - `CREDIT_RULE_TYPE` - 规则类型

**验收标准**：
- [ ] 字典类型常量定义完整

---

### 阶段4：字段配置管理（2天）

#### 任务4.1：VO类定义
**文件**：
- `controller/admin/field/vo/CreditFieldConfigPageReqVO.java`
- `controller/admin/field/vo/CreditFieldConfigRespVO.java`
- `controller/admin/field/vo/CreditFieldConfigSaveReqVO.java`

**任务内容**：
- [ ] 创建所有VO类
- [ ] 添加Swagger注解
- [ ] 添加校验注解（`@NotNull`, `@NotBlank` 等）

**验收标准**：
- [ ] VO类字段完整
- [ ] 校验注解正确

---

#### 任务4.2：Convert转换类
**文件**：`convert/CreditFieldConfigConvert.java`

**任务内容**：
- [ ] 使用MapStruct创建转换接口
- [ ] 定义DO到RespVO的转换方法
- [ ] 定义SaveReqVO到DO的转换方法

**验收标准**：
- [ ] 转换方法完整
- [ ] 使用 `@Mapper` 注解

---

#### 任务4.3：Service接口和实现
**文件**：
- `service/field/CreditFieldConfigService.java`
- `service/field/CreditFieldConfigServiceImpl.java`

**任务内容**：
- [ ] 定义Service接口方法：
  - `createFieldConfig()` - 创建字段配置
  - `updateFieldConfig()` - 更新字段配置
  - `deleteFieldConfig()` - 删除字段配置
  - `getFieldConfig()` - 获取字段配置
  - `getFieldConfigPage()` - 分页查询
  - `getFieldConfigList()` - 列表查询（根据部门ID和报表类型）
- [ ] 实现Service方法：
  - 校验字段编码唯一性
  - 校验部门ID存在性（调用 `AdminDeptApi`）
  - 实现CRUD操作
  - 实现缓存（可选）

**验收标准**：
- [ ] Service方法完整
- [ ] 业务逻辑正确
- [ ] 异常处理完善

---

#### 任务4.4：Controller控制器
**文件**：`controller/admin/field/CreditFieldConfigController.java`

**任务内容**：
- [ ] 创建Controller，使用 `@RestController`
- [ ] 定义API接口：
  - `POST /credit/field-config/create` - 创建
  - `PUT /credit/field-config/update` - 更新
  - `DELETE /credit/field-config/delete` - 删除
  - `GET /credit/field-config/{id}` - 获取详情
  - `GET /credit/field-config/page` - 分页查询
  - `GET /credit/field-config/list` - 列表查询
- [ ] 添加权限注解：`@PreAuthorize`
- [ ] 添加操作日志注解：`@ApiAccessLog`

**验收标准**：
- [ ] API接口完整
- [ ] 权限控制正确
- [ ] Swagger文档完整

---

### 阶段5：表单数据管理（3天）

#### 任务5.1：VO类定义
**文件**：
- `controller/admin/form/vo/CreditFormDataPageReqVO.java`
- `controller/admin/form/vo/CreditFormDataRespVO.java`
- `controller/admin/form/vo/CreditFormDataSaveReqVO.java`
- `controller/admin/form/vo/CreditFormDataSubmitReqVO.java`

**任务内容**：
- [ ] 创建所有VO类
- [ ] `formData` 字段使用 `Map<String, Object>` 类型
- [ ] 添加Swagger注解和校验注解

**验收标准**：
- [ ] VO类字段完整
- [ ] JSON字段类型正确

---

#### 任务5.2：Convert转换类
**文件**：`convert/CreditFormDataConvert.java`

**任务内容**：
- [ ] 创建转换接口
- [ ] 处理JSON字段的转换

**验收标准**：
- [ ] 转换方法完整

---

#### 任务5.3：Service接口和实现
**文件**：
- `service/form/CreditFormDataService.java`
- `service/form/CreditFormDataServiceImpl.java`

**任务内容**：
- [ ] 定义Service接口方法：
  - `createFormData()` - 创建表单数据（草稿状态）
  - `updateFormData()` - 更新表单数据（仅草稿状态）
  - `submitFormData()` - 提交表单数据
  - `deleteFormData()` - 删除表单数据（仅草稿状态）
  - `getFormData()` - 获取表单数据
  - `getFormDataPage()` - 分页查询
  - `getDeptFormDataMap()` - 获取各部门表单数据Map（用于计算）
- [ ] 实现Service方法：
  - **权限校验**：部门专员只能操作本部门数据
  - **状态校验**：更新和删除只能操作草稿状态
  - **数据校验**：调用校验服务
  - **审计日志**：记录所有操作
- [ ] 实现数据权限过滤（使用 `@DataPermission`）

**验收标准**：
- [ ] Service方法完整
- [ ] 权限控制正确
- [ ] 状态校验正确
- [ ] 审计日志记录完整

---

#### 任务5.4：Controller控制器
**文件**：`controller/admin/form/CreditFormDataController.java`

**任务内容**：
- [ ] 创建Controller
- [ ] 定义API接口：
  - `POST /credit/form-data/create` - 创建
  - `PUT /credit/form-data/update` - 更新
  - `POST /credit/form-data/submit` - 提交
  - `DELETE /credit/form-data/delete` - 删除
  - `GET /credit/form-data/{id}` - 获取详情
  - `GET /credit/form-data/page` - 分页查询
- [ ] 添加权限注解和操作日志注解

**验收标准**：
- [ ] API接口完整
- [ ] 权限控制正确

---

### 阶段6：计算引擎核心（3天）

#### 任务6.1：计算上下文构建器
**文件**：`framework/calculator/CreditCalculationContextBuilder.java`

**任务内容**：
- [ ] 创建上下文构建器类
- [ ] 实现方法：
  - `buildContext()` - 构建SpEL执行上下文
  - `loadDeptFormData()` - 加载各部门表单数据
  - `loadMonthlyReports()` - 加载月报数据（用于季报计算）
  - `loadPreviousQuarterReport()` - 加载之前季报数据
  - `registerCustomFunctions()` - 注册自定义函数（sum、avg等）
- [ ] 实现缓存数据加载（从Redis加载）

**验收标准**：
- [ ] 上下文构建正确
- [ ] 数据加载完整
- [ ] 自定义函数注册成功

---

#### 任务6.2：计算引擎核心类
**文件**：`framework/calculator/CreditCalculationEngine.java`

**任务内容**：
- [ ] 创建计算引擎类，使用 `@Service`
- [ ] 注入依赖：
  - `SpelExpressionParser`
  - `CreditCalculationRuleService`
  - `CreditFormDataService`
  - `CreditSummaryReportService`
  - `CreditCalculationContextBuilder`
- [ ] 实现方法：
  - `calculateSummaryReport()` - 计算汇总报表（核心方法）
  - `executeCalculationRule()` - 执行单个计算规则
  - `validateCalculationResult()` - 校验计算结果
- [ ] 实现计算日志记录
- [ ] 实现异常处理和回滚机制

**验收标准**：
- [ ] 计算引擎可以正确执行SpEL表达式
- [ ] 计算日志记录完整
- [ ] 异常处理完善

---

#### 任务6.3：自定义计算函数
**文件**：`framework/calculator/CreditCalculationFunctions.java`

**任务内容**：
- [ ] 创建自定义函数类
- [ ] 实现函数：
  - `sum()` - 求和
  - `avg()` - 平均值
  - `max()` - 最大值
  - `min()` - 最小值
  - `getCachedField()` - 从缓存获取字段值（可选）

**验收标准**：
- [ ] 自定义函数可以正确注册到SpEL上下文
- [ ] 函数计算结果正确

---

#### 任务6.4：计算规则Service
**文件**：
- `service/calculation/CreditCalculationRuleService.java`
- `service/calculation/CreditCalculationRuleServiceImpl.java`

**任务内容**：
- [ ] 定义Service接口方法：
  - `createCalculationRule()` - 创建计算规则
  - `updateCalculationRule()` - 更新计算规则
  - `deleteCalculationRule()` - 删除计算规则
  - `getCalculationRule()` - 获取计算规则
  - `getCalculationRulePage()` - 分页查询
  - `getRulesByReportType()` - 根据报表类型查询（按计算顺序排序）
- [ ] 实现Service方法：
  - 校验SpEL表达式语法（可选）
  - 实现CRUD操作

**验收标准**：
- [ ] Service方法完整
- [ ] 规则查询按计算顺序排序

---

#### 任务6.5：计算规则Controller
**文件**：`controller/admin/calculation/CreditCalculationRuleController.java`

**任务内容**：
- [ ] 创建Controller
- [ ] 定义API接口（CRUD）
- [ ] 添加权限注解

**验收标准**：
- [ ] API接口完整

---

### 阶段7：报表生成服务（2天）

#### 任务7.1：报表Service接口和实现
**文件**：
- `service/report/CreditSummaryReportService.java`
- `service/report/CreditSummaryReportServiceImpl.java`

**任务内容**：
- [ ] 定义Service接口方法：
  - `generateMonthlyReport()` - 生成月报
  - `generateQuarterlyReport()` - 生成季报
  - `getSummaryReport()` - 获取报表
  - `getSummaryReportPage()` - 分页查询
  - `getByPeriodAndType()` - 根据周期和类型查询
- [ ] 实现Service方法：
  - 调用计算引擎生成报表
  - 校验各部门表单是否全部提交（月报）
  - 校验相关月报是否全部生成（季报）
  - 保存报表数据
  - 实现缓存（可选）

**验收标准**：
- [ ] Service方法完整
- [ ] 报表生成逻辑正确
- [ ] 校验逻辑完善

---

#### 任务7.2：报表Controller
**文件**：`controller/admin/report/CreditSummaryReportController.java`

**任务内容**：
- [ ] 创建Controller
- [ ] 定义API接口：
  - `POST /credit/summary-report/generate` - 手动触发生成
  - `GET /credit/summary-report/{id}` - 获取详情
  - `GET /credit/summary-report/page` - 分页查询
  - `GET /credit/summary-report/export` - 导出Excel
- [ ] 添加权限注解

**验收标准**：
- [ ] API接口完整

---

#### 任务7.3：Excel导出功能
**文件**：
- `controller/admin/report/vo/CreditSummaryReportExcelVO.java`
- `framework/excel/CreditReportExcelUtils.java`（可选）

**任务内容**：
- [ ] 创建Excel导出VO类
- [ ] 实现导出方法：
  - 获取字段配置（用于表头）
  - 构建导出数据
  - 使用EasyExcel导出
- [ ] 支持动态表头（根据字段配置）

**验收标准**：
- [ ] Excel导出功能正常
- [ ] 表头动态生成

---

### 阶段8：校验规则服务（1天）

#### 任务8.1：校验器框架
**文件**：`framework/validator/CreditValidator.java`

**任务内容**：
- [ ] 创建校验器接口
- [ ] 实现校验器：
  - `RequiredValidator` - 必填校验
  - `RangeValidator` - 范围校验
  - `RegexValidator` - 正则校验
  - `CustomValidator` - 自定义校验（SpEL表达式）

**验收标准**：
- [ ] 校验器可以正确执行校验
- [ ] 错误信息返回正确

---

#### 任务8.2：校验Service
**文件**：
- `service/validation/CreditValidationService.java`
- `service/validation/CreditValidationServiceImpl.java`

**任务内容**：
- [ ] 定义Service接口方法：
  - `validateFormData()` - 校验表单数据
  - `validateField()` - 校验单个字段
- [ ] 实现Service方法：
  - 加载字段的校验规则
  - 按优先级执行校验
  - 返回校验结果

**验收标准**：
- [ ] 校验逻辑正确
- [ ] 错误信息完整

---

#### 任务8.3：校验规则管理
**文件**：
- `service/validation/CreditValidationRuleService.java`
- `service/validation/CreditValidationRuleServiceImpl.java`
- `controller/admin/validation/CreditValidationRuleController.java`

**任务内容**：
- [ ] 实现校验规则的CRUD操作
- [ ] 创建Controller API

**验收标准**：
- [ ] CRUD功能完整

---

### 阶段9：审计日志服务（1天）

#### 任务9.1：审计日志AOP切面
**文件**：`framework/audit/CreditAuditLogAspect.java`

**任务内容**：
- [ ] 创建AOP切面类
- [ ] 定义切点：拦截Service层的修改方法
- [ ] 实现环绕通知：
  - 记录变更前数据
  - 执行方法
  - 记录变更后数据
  - 保存审计日志
- [ ] 创建自定义注解：`@CreditAuditLog`

**验收标准**：
- [ ] 审计日志可以正确记录
- [ ] 变更前后数据对比正确

---

#### 任务9.2：审计日志Service和Controller
**文件**：
- `service/audit/CreditAuditLogService.java`
- `service/audit/CreditAuditLogServiceImpl.java`
- `controller/admin/audit/CreditAuditLogController.java`

**任务内容**：
- [ ] 实现审计日志查询功能
- [ ] 创建Controller API

**验收标准**：
- [ ] 查询功能完整

---

### 阶段10：定时任务（2天）

#### 任务10.1：月报生成任务
**文件**：`job/CreditMonthlyReportGenerateJob.java`

**任务内容**：
- [ ] 创建定时任务类，实现 `JobHandler`
- [ ] 实现 `execute()` 方法：
  - 获取上个月的报送周期
  - 检查各部门表单是否全部提交
  - 调用报表生成服务
  - 发送邮件通知
- [ ] 配置Cron表达式：每月1号凌晨2点执行
- [ ] 添加 `@TenantJob` 注解（如果支持多租户）

**验收标准**：
- [ ] 定时任务可以正确执行
- [ ] 异常处理完善

---

#### 任务10.2：季报生成任务
**文件**：`job/CreditQuarterlyReportGenerateJob.java`

**任务内容**：
- [ ] 创建定时任务类
- [ ] 实现 `execute()` 方法：
  - 获取上个季度的报送周期
  - 检查相关月报是否全部生成
  - 调用报表生成服务
  - 发送邮件通知
- [ ] 配置Cron表达式：每季度第1个月1号凌晨3点执行
- [ ] 实现季度周期计算逻辑（Q1:1-3月，Q2:4-6月等）

**验收标准**：
- [ ] 定时任务可以正确执行
- [ ] 季度周期计算正确

---

#### 任务10.3：邮件提醒任务
**文件**：`job/CreditNotificationJob.java`

**任务内容**：
- [ ] 创建定时任务类
- [ ] 实现 `execute()` 方法：
  - 查询即将到期的填报任务
  - 发送邮件提醒给部门专员
- [ ] 配置Cron表达式：每天上午9点执行
- [ ] 集成邮件服务（`wmt-module-system` 的 `MailSendService`）

**验收标准**：
- [ ] 邮件提醒功能正常
- [ ] 邮件内容正确

---

### 阶段11：Excel导出（1天）

#### 任务11.1：完善Excel导出功能
**文件**：`controller/admin/report/CreditSummaryReportController.java`

**任务内容**：
- [ ] 完善导出方法：
  - 支持月报导出
  - 支持季报导出
  - 支持自定义字段导出
- [ ] 实现Excel模板（可选）

**验收标准**：
- [ ] Excel导出功能完整
- [ ] 导出数据正确

---

### 阶段12：邮件通知（1天）

#### 任务12.1：邮件通知Service
**文件**：
- `service/notification/CreditNotificationService.java`
- `service/notification/CreditNotificationServiceImpl.java`

**任务内容**：
- [ ] 创建邮件通知Service
- [ ] 实现方法：
  - `sendFormDataReminder()` - 发送表单填报提醒
  - `sendReportGenerateNotification()` - 发送报表生成通知
- [ ] 集成邮件服务
- [ ] 创建邮件模板（HTML格式）

**验收标准**：
- [ ] 邮件发送功能正常
- [ ] 邮件内容格式正确

---

### 阶段13：单元测试（2天）

#### 任务13.1：Mapper测试
**文件**：`dal/mysql/**/*MapperTest.java`

**任务内容**：
- [ ] 为所有Mapper创建单元测试
- [ ] 测试CRUD操作
- [ ] 测试JSONB字段查询

**验收标准**：
- [ ] 测试覆盖率 > 80%

---

#### 任务13.2：Service测试
**文件**：`service/**/*ServiceTest.java`

**任务内容**：
- [ ] 为所有Service创建单元测试
- [ ] 测试业务逻辑
- [ ] 测试异常场景

**验收标准**：
- [ ] 测试覆盖率 > 70%

---

#### 任务13.3：计算引擎测试
**文件**：`framework/calculator/CreditCalculationEngineTest.java`

**任务内容**：
- [ ] 创建计算引擎单元测试
- [ ] 测试简单计算规则
- [ ] 测试复杂计算规则（季报计算）
- [ ] 测试异常场景

**验收标准**：
- [ ] 计算引擎测试完整
- [ ] 所有计算规则都能正确执行

---

## 四、开发规范和注意事项

### 4.1 代码规范
- [ ] 遵循阿里巴巴Java开发手册
- [ ] 使用Lombok简化代码
- [ ] 使用MapStruct进行对象转换
- [ ] 统一异常处理（使用 `GlobalExceptionHandler`）

### 4.2 数据库规范
- [ ] 所有表使用 `credit_` 前缀
- [ ] 所有字段使用下划线命名
- [ ] JSONB字段使用 `JacksonTypeHandler`
- [ ] 索引命名规范：`idx_表名_字段名` 或 `uk_表名_字段名`

### 4.3 API规范
- [ ] Controller URL以 `/credit/` 开头
- [ ] 统一使用RESTful风格
- [ ] 统一返回格式：`CommonResult<T>`
- [ ] 统一异常返回格式

### 4.4 权限规范
- [ ] 使用 `@PreAuthorize` 控制接口权限
- [ ] 使用 `@DataPermission` 控制数据权限
- [ ] 部门专员只能操作本部门数据

### 4.5 日志规范
- [ ] 使用 `@ApiAccessLog` 记录API访问日志
- [ ] 使用审计日志记录数据变更
- [ ] 计算过程记录计算日志

---

## 五、测试计划

### 5.1 单元测试
- [ ] Mapper层测试
- [ ] Service层测试
- [ ] 计算引擎测试

### 5.2 集成测试
- [ ] API接口测试
- [ ] 计算流程测试
- [ ] 权限控制测试

### 5.3 性能测试
- [ ] 计算性能测试（单次计算 < 30秒）
- [ ] 并发测试
- [ ] 大数据量测试

---

## 六、部署清单

### 6.1 数据库脚本
- [ ] 执行 `sql/postgresql/credit-module.sql`
- [ ] 验证表结构正确
- [ ] 验证索引创建成功

### 6.2 配置检查
- [ ] 检查 `application.yaml` 配置
- [ ] 检查数据库连接配置
- [ ] 检查Redis连接配置
- [ ] 检查邮件服务配置

### 6.3 定时任务配置
- [ ] 在Quartz中配置定时任务
- [ ] 验证定时任务可以正常执行

---

## 七、验收标准

### 7.1 功能验收
- [ ] 字段配置管理功能正常
- [ ] 表单数据录入功能正常
- [ ] 月报生成功能正常
- [ ] 季报生成功能正常
- [ ] Excel导出功能正常
- [ ] 邮件提醒功能正常
- [ ] 权限控制正确

### 7.2 性能验收
- [ ] 单次计算时间 < 30秒
- [ ] API响应时间 < 2秒
- [ ] 支持并发访问

### 7.3 代码质量验收
- [ ] 代码审查通过
- [ ] 单元测试覆盖率 > 70%
- [ ] 无严重Bug

---

## 八、风险控制

### 8.1 技术风险
- **SpEL表达式复杂度**：如果SpEL无法满足需求，保留硬编码接口作为兜底
- **计算性能问题**：如果性能不足，后续优化（缓存、异步计算）

### 8.2 业务风险
- **计算规则变更**：提供规则版本管理
- **数据准确性**：增加计算日志，支持人工复核

---

## 九、后续优化计划

### 9.1 性能优化（按需）
- [ ] 添加Redis缓存
- [ ] 实现异步计算
- [ ] 优化数据库查询

### 9.2 功能扩展（按需）
- [ ] 接入BPM工作流
- [ ] 实现版本管理
- [ ] 实现多租户支持

---

**计划制定日期**：2025-01-XX  
**预计完成时间**：22个工作日  
**负责人**：待定
