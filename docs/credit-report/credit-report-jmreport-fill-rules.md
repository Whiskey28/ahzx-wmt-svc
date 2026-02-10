## 信用报送积木报表集成与在线填报规则

### 一、总体设计

- **目标**：通过 JimuReport 在线填报，将各部门（准确说是各“角色”）按周期填报的数据统一落库到 PostgreSQL 的 `report_fill_*` 表中，并在业务系统中提供列表查看与编辑入口。
- **核心表结构**：
  - `report_fill_basic_info`：**主记录锚点表**，唯一约束 `role_id + period_id + report_id`。
  - 若干 1:1 **主数据块表**：如 `report_fill_enterprise_basic`、`report_fill_biz_stat_finance`、`report_fill_biz_stat_hr` 等。
  - 若干 1:N **明细子表**：如 `report_fill_product_stat`、`report_fill_service_by_industry`、`report_fill_info_source_by_industry` 等。
- **审计与逻辑删除**：
  - 审计字段：`create_time`、`update_time` 使用 PostgreSQL 触发器统一维护；`creator`、`updater` 从表单隐藏字段写入。
  - 逻辑删除字段：`deleted int2 NOT NULL DEFAULT 0`，与全局 MyBatis-Plus 逻辑删除配置保持一致。
- **数值字段类型策略**：
  - **需要计算/汇总/统计的字段统一使用 `NUMERIC`** 类型：
    - 金额：`NUMERIC(18, 2)`（如资产、收入、净利润等）。
    - 计数/户数/次数：`NUMERIC(18, 0)`。
    - 比例：`NUMERIC(5, 2)`（如股份占比）。
  - 仅展示、不参与计算的字段使用 `VARCHAR`。

---

### 二、数据库层规则（PostgreSQL）

#### 2.1 主记录锚点表 `report_fill_basic_info`

- **作用**：记录“哪个角色在某个周期，针对哪个报表模板产生了一次填报记录”，并承载通用审计字段。
- **关键字段**：
  - `id BIGINT`：主键，自增（`report_fill_basic_info_seq`），逻辑上作为 `record_id`。
  - `role_id VARCHAR(64)`：角色 ID（替代历史设计中的部门字段）。
  - `period_id VARCHAR(16)`：填报周期（如 `2025-01`、`2025-Q1`）。
  - `report_id VARCHAR(64)`：报表模板 ID（`jimu_report.id`）。
  - 审计字段：`create_time`、`update_time`、`creator`、`updater`、`deleted int2 NOT NULL DEFAULT 0`。
- **约束**：
  - 主键：`pk_report_fill_basic_info (id)`。
  - 唯一索引：`uk_report_fill_basic_info_role_period_report (role_id, period_id, report_id)`，保证同一角色同一周期同一模板只有一条主记录。

#### 2.2 主数据块表（1:1）

示例表：

- `report_fill_enterprise_basic`：企业基础信息。
  - 金额字段：`reg_cap_million`、`paid_in_cap_million` → `NUMERIC(18, 2)`。
  - 计数字段：`branch_count` → `NUMERIC(18, 0)`。
  - 比例字段：`controlling_shareholder_ratio` → `NUMERIC(5, 2)`。
  - 其他均为 `VARCHAR`（企业名称、信用代码、地址、联系人等）。

- `report_fill_biz_stat_finance`：经营情况-财务口径。
  - 金额字段均为 `NUMERIC(18, 2)`：
    - `asset_amount`、`liability_amount`、
      `year_income_amount`、`year_credit_income_amount`、`year_net_profit_amount`。

- `report_fill_biz_stat_hr`：经营情况-人员口径。
  - 计数字段为 `NUMERIC(18, 0)`：
    - `employee_total`、`employee_credit_biz`。

- `report_fill_biz_stat_credit_build`：信用体系建设口径。
  - `project_count NUMERIC(18, 0)`、`income_amount NUMERIC(18, 2)`。

- `report_fill_complaint_security_stat`：异议/投诉/信息安全。
  - 所有 `*_count` 字段为 `NUMERIC(18, 0)`。

- `report_fill_info_collect_stat`：信息采集情况。
  - 所有 `collect_*_total` 字段为 `NUMERIC(18, 0)`。

所有主数据块表均：

- 主键：`id BIGINT NOT NULL DEFAULT nextval('<table>_seq')`。
- 审计字段：`create_time`、`update_time`、`creator`、`updater`、`deleted int2 NOT NULL DEFAULT 0`。

#### 2.3 明细子表（1:N）

- `report_fill_product_stat`：
  - `record_id BIGINT NOT NULL`：关联主记录。
  - `product_type VARCHAR(64)`（字典 code）、`product_name VARCHAR(256)`。
  - `year_count NUMERIC(18, 0)`：当年提供次数。

- `report_fill_service_by_industry`：
  - `record_id BIGINT NOT NULL`。
  - `industry_code VARCHAR(64)`。
  - `year_service_count NUMERIC(18, 0)`：按行业分类统计的提供次数。

- `report_fill_info_source_by_industry`：
  - `record_id BIGINT NOT NULL`。
  - `industry_code VARCHAR(64)`。
  - `provider_org_total` / `provider_org_current`：`NUMERIC(18, 0)`。

各子表统一：

- 主键自增 + `record_id` 索引：`idx_<table>_record_id (record_id)`。
- 审计字段同上。

#### 2.4 审计触发器

- 触发器函数：`fill_audit_time_fields()`。
  - `INSERT`：
    - 若 `NEW.create_time IS NULL`，则填充 `CURRENT_TIMESTAMP`。
    - 无条件设置 `NEW.update_time = CURRENT_TIMESTAMP`。
  - `UPDATE`：
    - 无条件设置 `NEW.update_time = CURRENT_TIMESTAMP`。
- 为所有 `report_fill_*` 表创建：
  - `BEFORE INSERT OR UPDATE ON <table> FOR EACH ROW EXECUTE FUNCTION fill_audit_time_fields();`
- `creator`、`updater` 字段不由触发器处理，统一从表单隐藏参数写入。

---

### 三、Java DO 映射原则（wmt-module-credit-report）

- DO 类统一放在 `com.wmt.module.credit.report.dal.dataobject` 包下，并继承 `BaseDO`。
- 与 SQL 对应的数值字段统一使用 `java.math.BigDecimal`：
  - 金额/计数/比例字段：`BigDecimal`（而非 `String`）。
  - 示例：
    - `ReportFillBizStatFinanceDO.assetAmount` → `BigDecimal assetAmount`。
    - `ReportFillBizStatHrDO.employeeTotal` → `BigDecimal employeeTotal`。
    - `ReportFillInfoCollectStatDO.collectEnterpriseTotal` 等全部为 `BigDecimal`。
    - `ReportFillEnterpriseBasicDO.regCapMillion` / `paidInCapMillion` / `branchCount` / `controllingShareholderRatio` → `BigDecimal`。
- 主键和 `recordId`：
  - 主键统一为 `Long id`，并使用 `@KeySequence("<table>_seq") + @TableId`。
  - 明细表外键 `recordId` 为 `Long recordId`。
- 字符串类字段（如企业名称、地址、行业 code 等）继续使用 `String`。

> 说明：选择 `BigDecimal` 作为统一数值类型，便于后续在 Service/SpEL/SQL 中做精确计算和聚合，也避免字符串排序/比较带来的问题。

---

### 四、报表查看页规则（业务系统前端）

- 页面：**报表管理 → 报表查看**。
- 后端接口：`ReportFillController.getFillRecordPage`，底层通过 `ReportFillService.getFillRecordPage` 查询：
  - 主表：`report_fill_basic_info`。
  - 关联表：`jimu_report`（模板名称）、`report_fill_enterprise_basic`（企业名称）等。
- 列表字段：
  - 报表模板：`jimu_report.name`。
  - 企业名称：`report_fill_enterprise_basic.enterprise_name`。
  - 填报周期：`report_fill_basic_info.period_id`。
  - 角色（原“部门”列）：`report_fill_basic_info.role_id` 映射到角色名称。
  - 创建时间 / 更新时间：来自主记录审计字段。
- 查询条件（当前能力）：
  - 报表分类 / 模板：通过 `jimu_report.type` / `jimu_report.id`。
  - 企业名称：先在 `report_fill_enterprise_basic` 模糊查询 `enterprise_name`，再回到主表。
  - 填报周期：按 `period_id` 过滤。
- 操作：
  - **查看**：进入 JimuReport 查看模式。
  - **修改**：调用 `/credit/report-fill/record/{id}/edit-url`，跳转到积木填报编辑 URL。

---

### 五、在线填报表单配置流程（JimuReport）

#### 5.1 准备阶段

1. **设计数据库表结构**  
   - 按上文规则调整 `credit-report-2026-01-29.sql`，确保：
     - 主记录表 + 主数据块表 + 明细子表完整。
     - 主键序列、自增、触发器均已创建。
2. **导入 Excel 模板**  
   - 在本地 Excel 中先完成报表业务布局（行列、合并、文字等），无须严格的字段表头。
   - 在 JimuReport 中导入 Excel，生成报表模板。

#### 5.2 创建在线填报表单

1. 在 JimuReport 中选择对应模板，进入“**填报设计**”。
2. 新建在线填报表单，**命名以部门/角色为准**，例如：
   - `综合管理填报（党委工作部）`
   - `综合管理填报（风险管理部）`
3. 确定该表单归属哪个角色填写，后续 iframe URL 中需要携带该角色对应的 `roleId`。

#### 5.3 数据集配置

- **关系表数据集：`report_fill_basic_info`（必配）**
  - 作用：
    - 首次填报时插入主记录，生成 `id`（recordId）。
    - 编辑时根据 `recordId` 或 `role_id + period_id + report_id` 查询记录。
  - 关键字段绑定：
    - `role_id` ← `${param.roleId}`。
    - `period_id` ← 表单中对应控件或 `${param.period}`。
    - `report_id` ← 固定为当前模板 ID（可在数据集或初始化脚本中写死）。
    - `creator` / `updater` ← `${param.userId}`。

- **主数据块（1:1）数据集**
  - 如 `report_fill_enterprise_basic`、`report_fill_biz_stat_finance`、`report_fill_biz_stat_hr` 等。
  - 主键 `id` 与主记录的 `id` 一致（逻辑上为 1:1）。
  - 将表单中的业务单元格绑定到对应字段：
    - 例：`员工总人数` → `employee_total`（`NUMERIC`）。
    - 例：`本年度总收入` → `year_income_amount`（`NUMERIC`）。

- **明细子表（1:N）数据集**
  - 如 `report_fill_product_stat`、`report_fill_service_by_industry`、`report_fill_info_source_by_industry`。
  - 统一规则：
    - `record_id` ← 主记录 `id`。
    - 明细行采用“子表控件”或多行区域绑定。

#### 5.4 iframe URL 与隐藏字段

- **iframe URL 约定参数**
  - `roleId`：当前角色 ID。
  - `reportId`：报表模板 ID（`jimu_report.id`）。
  - `period`：填报周期（如 `2026-01`）。
  - `userId`：当前登录用户 ID。
  - （可选）`recordId`：已有记录的主键 ID，用于编辑模式。

- **表单隐藏字段配置（对应图示 3）**
  - 在表格右侧属性中，将某些单元格设置为：
    - 控件类型：单行文本。
    - 绑定字段：关系表或主数据表中的字段。
    - 默认值：`${param.xxx}`。
    - 勾选“只读”“隐藏”，防止用户修改。
  - 推荐映射：
    - `report_fill_basic_info.role_id` ← `${param.roleId}`。
    - `report_fill_basic_info.report_id` ← `${param.reportId}`。
    - `report_fill_basic_info.period_id` ← `${param.period}` 或用户输入。
    - `report_fill_basic_info.creator` ← `${param.userId}`（新建）。
    - `report_fill_basic_info.updater` ← `${param.userId}`（新建 & 修改）。
    - 如主数据块/子表中也有 `creator` / `updater` 字段，则同样从 `${param.userId}` 赋值。

- **主数据可见控件绑定**
  - 将需要用户填写的单元格绑定到业务字段，并选择合适组件：
    - 计数/金额类：数字组件（对应 DO 中 `BigDecimal`，SQL 中 `NUMERIC`）。
    - 文本类：单行文本/多行文本组件。

---

### 六、数值计算与统计使用方式

- **数据库侧计算**：
  - 由于上述字段使用 `NUMERIC`，可以直接在 SQL/MyBatis 中使用：
    - `SUM(asset_amount)`、`AVG(year_income_amount)`、`SUM(employee_total)` 等。
  - 无需显式类型转换，避免 `VARCHAR` → 数值的隐式转换错误。

- **SpEL / Java 侧计算**：
  - DO 中数值字段为 `BigDecimal`，便于在 Service 或规则引擎中做加减乘除：
    - 例如：`yearIncomeAmount.subtract(yearCreditIncomeAmount)`。
  - 在需要时，可将 `BigDecimal` 格式化为字符串返回给前端。

- **展示上的“所见即所得”**：
  - 若需要展示“暂无数据”等文案，建议在视图层（前端或报表聚合结果）处理，而不是直接写进数值字段。
  - 对于确实不参与任何计算的字段，仍可按照场景使用 `VARCHAR`。

---

### 七、使用与扩展建议

- 后续如需新增字段：
  - 先在 `credit-report-2026-01-29.sql` 中补充列，并按上述规则选定 `NUMERIC` 或 `VARCHAR`。
  - 同步更新对应 DO 类字段（`BigDecimal` 或 `String`），保持命名与数据库一致。
  - 在 JimuReport 填报设计中为新字段绑定单元格，并视情况决定是否参与统计。
- 若将来需要跨报表统计（多周期、多角色汇总）：
  - 优先在数据库层基于 `report_fill_basic_info.id` 关联各块表，使用 `NUMERIC` 字段直接做聚合查询。
  - 必要时可以在单独统计模块中新增视图或物化视图。

