# 积木报表配置指南：经营情况数据报表

## 一、报表需求说明

### 1.1 报表信息
- **报表名称**：企业征信业务监测指标 - (二) 经营情况
- **报表类型**：数据报表（按月统计）
- **数据来源**：
  - `report_fill_biz_stat_finance`（财务数据）
  - `report_fill_biz_stat_hr`（人员数据）
  - `report_fill_biz_stat_credit_build`（信用体系建设数据）

### 1.2 报表字段映射

| 报表字段 | 数据来源表 | 数据来源字段 | 单位 |
|---------|-----------|------------|------|
| 资产 | report_fill_biz_stat_finance | asset_amount | 人民币元 |
| 负债 | report_fill_biz_stat_finance | liability_amount | 人民币元 |
| 本年度总收入 | report_fill_biz_stat_finance | year_income_amount | 人民币元 |
| 本年度企业征信业务收入 | report_fill_biz_stat_finance | year_credit_income_amount | 人民币元 |
| 本年度净利润 | report_fill_biz_stat_finance | year_net_profit_amount | 人民币元 |
| 员工总人数 | report_fill_biz_stat_hr | employee_total | 人 |
| 从事征信业务员工数 | report_fill_biz_stat_hr | employee_credit_biz | 人 |
| 企业参与行业、地方信用体系建设项目数 | report_fill_biz_stat_credit_build | project_count | 个 |
| 企业参与行业、地方信用体系建设收入 | report_fill_biz_stat_credit_build | income_amount | 人民币元 |
| 银行-信息使用者机构总累计数 | report_fill_biz_stat_credit_build | user_org_total_bank | 家 |
| 银行-当前使用服务的信息使用者机构数 | report_fill_biz_stat_credit_build | user_org_current_bank | 家 |
| 证券-信息使用者机构总累计数 | report_fill_biz_stat_credit_build | user_org_total_securities | 家 |
| 证券-当前使用服务的信息使用者机构数 | report_fill_biz_stat_credit_build | user_org_current_securities | 家 |
| 保险-信息使用者机构总累计数 | report_fill_biz_stat_credit_build | user_org_total_insurance | 家 |
| 保险-当前使用服务的信息使用者机构数 | report_fill_biz_stat_credit_build | user_org_current_insurance | 家 |
| 信托-信息使用者机构总累计数 | report_fill_biz_stat_credit_build | user_org_total_trust | 家 |
| 信托-当前使用服务的信息使用者机构数 | report_fill_biz_stat_credit_build | user_org_current_trust | 家 |
| P2P网络借贷中介-信息使用者机构总累计数 | report_fill_biz_stat_credit_build | user_org_total_p2p_lending_intermediary | 家 |
| P2P网络借贷中介-当前使用服务的信息使用者机构数 | report_fill_biz_stat_credit_build | user_org_current_p2p_lending_intermediary | 家 |
| 支付机构-信息使用者机构总累计数 | report_fill_biz_stat_credit_build | user_org_total_payment_institution | 家 |
| 支付机构-当前使用服务的信息使用者机构数 | report_fill_biz_stat_credit_build | user_org_current_payment_institution | 家 |
| 融资租赁及担保类公司-信息使用者机构总累计数 | report_fill_biz_stat_credit_build | user_org_total_financing_leasing_guarantee | 家 |
| 融资租赁及担保类公司-当前使用服务的信息使用者机构数 | report_fill_biz_stat_credit_build | user_org_current_financing_leasing_guarantee | 家 |
| 小额贷款公司-信息使用者机构总累计数 | report_fill_biz_stat_credit_build | user_org_total_micro_loan_company | 家 |
| 小额贷款公司-当前使用服务的信息使用者机构数 | report_fill_biz_stat_credit_build | user_org_current_micro_loan_company | 家 |
| 消费金融公司-信息使用者机构总累计数 | report_fill_biz_stat_credit_build | user_org_total_consumer_finance_company | 家 |
| 消费金融公司-当前使用服务的信息使用者机构数 | report_fill_biz_stat_credit_build | user_org_current_consumer_finance_company | 家 |
| 资产管理公司-信息使用者机构总累计数 | report_fill_biz_stat_credit_build | user_org_total_asset_management_company | 家 |
| 资产管理公司-当前使用服务的信息使用者机构数 | report_fill_biz_stat_credit_build | user_org_current_asset_management_company | 家 |
| 汽车金融公司-信息使用者机构总累计数 | report_fill_biz_stat_credit_build | user_org_total_auto_finance_company | 家 |
| 汽车金融公司-当前使用服务的信息使用者机构数 | report_fill_biz_stat_credit_build | user_org_current_auto_finance_company | 家 |
| 商业保理公司-信息使用者机构总累计数 | report_fill_biz_stat_credit_build | user_org_total_commercial_factoring_company | 家 |
| 商业保理公司-当前使用服务的信息使用者机构数 | report_fill_biz_stat_credit_build | user_org_current_commercial_factoring_company | 家 |
| 政府-信息使用者机构总累计数 | report_fill_biz_stat_credit_build | user_org_total_government | 家 |
| 政府-当前使用服务的信息使用者机构数 | report_fill_biz_stat_credit_build | user_org_current_government | 家 |
| 公用事业单位（水、电、气、通讯等）-信息使用者机构总累计数 | report_fill_biz_stat_credit_build | user_org_total_public_utility | 家 |
| 公用事业单位（水、电、气、通讯等）-当前使用服务的信息使用者机构数 | report_fill_biz_stat_credit_build | user_org_current_public_utility | 家 |
| 行业协会-信息使用者机构总累计数 | report_fill_biz_stat_credit_build | user_org_total_industry_association | 家 |
| 行业协会-当前使用服务的信息使用者机构数 | report_fill_biz_stat_credit_build | user_org_current_industry_association | 家 |
| 法院-信息使用者机构总累计数 | report_fill_biz_stat_credit_build | user_org_total_court | 家 |
| 法院-当前使用服务的信息使用者机构数 | report_fill_biz_stat_credit_build | user_org_current_court | 家 |
| 电子商务平台-信息使用者机构总累计数 | report_fill_biz_stat_credit_build | user_org_total_ecommerce_platform | 家 |
| 电子商务平台-当前使用服务的信息使用者机构数 | report_fill_biz_stat_credit_build | user_org_current_ecommerce_platform | 家 |
| 涉农企业-信息使用者机构总累计数 | report_fill_biz_stat_credit_build | user_org_total_agriculture_related_enterprise | 家 |
| 涉农企业-当前使用服务的信息使用者机构数 | report_fill_biz_stat_credit_build | user_org_current_agriculture_related_enterprise | 家 |
| 其他征信机构-信息使用者机构总累计数 | report_fill_biz_stat_credit_build | user_org_total_other_credit_reporting_agency | 家 |
| 其他征信机构-当前使用服务的信息使用者机构数 | report_fill_biz_stat_credit_build | user_org_current_other_credit_reporting_agency | 家 |
| 数据服务商-信息使用者机构总累计数 | report_fill_biz_stat_credit_build | user_org_total_data_service_provider | 家 |
| 数据服务商-当前使用服务的信息使用者机构数 | report_fill_biz_stat_credit_build | user_org_current_data_service_provider | 家 |
| 交易对手方-信息使用者机构总累计数 | report_fill_biz_stat_credit_build | user_org_total_trading_counterparty | 家 |
| 交易对手方-当前使用服务的信息使用者机构数 | report_fill_biz_stat_credit_build | user_org_current_trading_counterparty | 家 |
| 信息主体自身-信息使用者机构总累计数 | report_fill_biz_stat_credit_build | user_org_total_information_subject_self | 家 |
| 信息主体自身-当前使用服务的信息使用者机构数 | report_fill_biz_stat_credit_build | user_org_current_information_subject_self | 家 |
| 其他-信息使用者机构总累计数 | report_fill_biz_stat_credit_build | user_org_total_other | 家 |
| 其他-当前使用服务的信息使用者机构数 | report_fill_biz_stat_credit_build | user_org_current_other | 家 |

## 二、SQL数据集配置

### 2.1 SQL查询语句（积木报表兼容版本，不使用 WITH/UNION）

```sql
SELECT
    '${periodId}' AS period_id,
    COALESCE(f.asset_amount, 0) AS asset_amount,
    COALESCE(f.liability_amount, 0) AS liability_amount,
    COALESCE(f.year_income_amount, 0) AS year_income_amount,
    COALESCE(f.year_credit_income_amount, 0) AS year_credit_income_amount,
    COALESCE(f.year_net_profit_amount, 0) AS year_net_profit_amount,
    COALESCE(h.employee_total, 0) AS employee_total,
    COALESCE(h.employee_credit_biz, 0) AS employee_credit_biz,
    COALESCE(c.project_count, 0) AS project_count,
    COALESCE(c.income_amount, 0) AS income_amount,
    COALESCE(c.user_org_total_bank, 0) AS user_org_total_bank,
    COALESCE(c.user_org_current_bank, 0) AS user_org_current_bank,
    COALESCE(c.user_org_total_securities, 0) AS user_org_total_securities,
    COALESCE(c.user_org_current_securities, 0) AS user_org_current_securities,
    COALESCE(c.user_org_total_insurance, 0) AS user_org_total_insurance,
    COALESCE(c.user_org_current_insurance, 0) AS user_org_current_insurance,
    COALESCE(c.user_org_total_trust, 0) AS user_org_total_trust,
    COALESCE(c.user_org_current_trust, 0) AS user_org_current_trust,
    COALESCE(c.user_org_total_p2p_lending_intermediary, 0) AS user_org_total_p2p_lending_intermediary,
    COALESCE(c.user_org_current_p2p_lending_intermediary, 0) AS user_org_current_p2p_lending_intermediary,
    COALESCE(c.user_org_total_payment_institution, 0) AS user_org_total_payment_institution,
    COALESCE(c.user_org_current_payment_institution, 0) AS user_org_current_payment_institution,
    COALESCE(c.user_org_total_financing_leasing_guarantee, 0) AS user_org_total_financing_leasing_guarantee,
    COALESCE(c.user_org_current_financing_leasing_guarantee, 0) AS user_org_current_financing_leasing_guarantee,
    COALESCE(c.user_org_total_micro_loan_company, 0) AS user_org_total_micro_loan_company,
    COALESCE(c.user_org_current_micro_loan_company, 0) AS user_org_current_micro_loan_company,
    COALESCE(c.user_org_total_consumer_finance_company, 0) AS user_org_total_consumer_finance_company,
    COALESCE(c.user_org_current_consumer_finance_company, 0) AS user_org_current_consumer_finance_company,
    COALESCE(c.user_org_total_asset_management_company, 0) AS user_org_total_asset_management_company,
    COALESCE(c.user_org_current_asset_management_company, 0) AS user_org_current_asset_management_company,
    COALESCE(c.user_org_total_auto_finance_company, 0) AS user_org_total_auto_finance_company,
    COALESCE(c.user_org_current_auto_finance_company, 0) AS user_org_current_auto_finance_company,
    COALESCE(c.user_org_total_commercial_factoring_company, 0) AS user_org_total_commercial_factoring_company,
    COALESCE(c.user_org_current_commercial_factoring_company, 0) AS user_org_current_commercial_factoring_company,
    COALESCE(c.user_org_total_government, 0) AS user_org_total_government,
    COALESCE(c.user_org_current_government, 0) AS user_org_current_government,
    COALESCE(c.user_org_total_public_utility, 0) AS user_org_total_public_utility,
    COALESCE(c.user_org_current_public_utility, 0) AS user_org_current_public_utility,
    COALESCE(c.user_org_total_industry_association, 0) AS user_org_total_industry_association,
    COALESCE(c.user_org_current_industry_association, 0) AS user_org_current_industry_association,
    COALESCE(c.user_org_total_court, 0) AS user_org_total_court,
    COALESCE(c.user_org_current_court, 0) AS user_org_current_court,
    COALESCE(c.user_org_total_ecommerce_platform, 0) AS user_org_total_ecommerce_platform,
    COALESCE(c.user_org_current_ecommerce_platform, 0) AS user_org_current_ecommerce_platform,
    COALESCE(c.user_org_total_agriculture_related_enterprise, 0) AS user_org_total_agriculture_related_enterprise,
    COALESCE(c.user_org_current_agriculture_related_enterprise, 0) AS user_org_current_agriculture_related_enterprise,
    COALESCE(c.user_org_total_other_credit_reporting_agency, 0) AS user_org_total_other_credit_reporting_agency,
    COALESCE(c.user_org_current_other_credit_reporting_agency, 0) AS user_org_current_other_credit_reporting_agency,
    COALESCE(c.user_org_total_data_service_provider, 0) AS user_org_total_data_service_provider,
    COALESCE(c.user_org_current_data_service_provider, 0) AS user_org_current_data_service_provider,
    COALESCE(c.user_org_total_trading_counterparty, 0) AS user_org_total_trading_counterparty,
    COALESCE(c.user_org_current_trading_counterparty, 0) AS user_org_current_trading_counterparty,
    COALESCE(c.user_org_total_information_subject_self, 0) AS user_org_total_information_subject_self,
    COALESCE(c.user_org_current_information_subject_self, 0) AS user_org_current_information_subject_self,
    COALESCE(c.user_org_total_other, 0) AS user_org_total_other,
    COALESCE(c.user_org_current_other, 0) AS user_org_current_other
FROM (
    SELECT r.period_id,
           f.asset_amount,
           f.liability_amount,
           f.year_income_amount,
           f.year_credit_income_amount,
           f.year_net_profit_amount
    FROM report_fill_basic_info r
    JOIN report_fill_biz_stat_finance f ON f.parent_id = r.id AND f.deleted = 0
    WHERE r.deleted = 0
      AND r.period_id = '${periodId}'
      AND r.report_id = '1177121021981863936'
    ORDER BY r.create_time DESC
    LIMIT 1
) f
LEFT JOIN (
    SELECT r.period_id,
           h.employee_total,
           h.employee_credit_biz
    FROM report_fill_basic_info r
    JOIN report_fill_biz_stat_hr h ON h.parent_id = r.id AND h.deleted = 0
    WHERE r.deleted = 0
      AND r.period_id = '${periodId}'
      AND r.report_id = '1177127729483460608'
    ORDER BY r.create_time DESC
    LIMIT 1
) h ON h.period_id = f.period_id
LEFT JOIN (
    SELECT r.period_id,
           c.project_count,
           c.income_amount,
           c.user_org_total_bank,
           c.user_org_current_bank,
           c.user_org_total_securities,
           c.user_org_current_securities,
           c.user_org_total_insurance,
           c.user_org_current_insurance,
           c.user_org_total_trust,
           c.user_org_current_trust,
           c.user_org_total_p2p_lending_intermediary,
           c.user_org_current_p2p_lending_intermediary,
           c.user_org_total_payment_institution,
           c.user_org_current_payment_institution,
           c.user_org_total_financing_leasing_guarantee,
           c.user_org_current_financing_leasing_guarantee,
           c.user_org_total_micro_loan_company,
           c.user_org_current_micro_loan_company,
           c.user_org_total_consumer_finance_company,
           c.user_org_current_consumer_finance_company,
           c.user_org_total_asset_management_company,
           c.user_org_current_asset_management_company,
           c.user_org_total_auto_finance_company,
           c.user_org_current_auto_finance_company,
           c.user_org_total_commercial_factoring_company,
           c.user_org_current_commercial_factoring_company,
           c.user_org_total_government,
           c.user_org_current_government,
           c.user_org_total_public_utility,
           c.user_org_current_public_utility,
           c.user_org_total_industry_association,
           c.user_org_current_industry_association,
           c.user_org_total_court,
           c.user_org_current_court,
           c.user_org_total_ecommerce_platform,
           c.user_org_current_ecommerce_platform,
           c.user_org_total_agriculture_related_enterprise,
           c.user_org_current_agriculture_related_enterprise,
           c.user_org_total_other_credit_reporting_agency,
           c.user_org_current_other_credit_reporting_agency,
           c.user_org_total_data_service_provider,
           c.user_org_current_data_service_provider,
           c.user_org_total_trading_counterparty,
           c.user_org_current_trading_counterparty,
           c.user_org_total_information_subject_self,
           c.user_org_current_information_subject_self,
           c.user_org_total_other,
           c.user_org_current_other
    FROM report_fill_basic_info r
    JOIN report_fill_biz_stat_credit_build c ON c.parent_id = r.id AND c.deleted = 0
    WHERE r.deleted = 0
      AND r.period_id = '${periodId}'
      AND r.report_id = '1178489730218569728'
    ORDER BY r.create_time DESC
    LIMIT 1
) c ON c.period_id = f.period_id;
```

### 2.2 SQL参数说明
- **`${periodId}`**：填报周期参数（如：`2025-12`）
  - 参数类型：文本
  - 必填：是
  - 默认值：可设置为当前月份

### 2.3 SQL查询逻辑说明
1. **主表**：`report_fill_basic_info`（填报主记录）
2. **关联表**：通过 `parent_id` 关联三张业务表
3. **过滤条件**：
   - `r.deleted = 0`：只查询未删除的主记录
   - `f.deleted = 0`、`h.deleted = 0`、`c.deleted = 0`：只关联未删除的业务数据
   - `r.period_id = '${param.periodId}'`：按周期过滤
4. **NULL值处理**：使用 `COALESCE` 函数，将NULL值转换为0

## 三、积木报表配置步骤

### 步骤1：创建数据报表

1. **登录积木报表管理后台**
   - 访问积木报表管理地址
   - 使用管理员账号登录

2. **进入报表管理**
   - 点击左侧菜单"报表管理"
   - 选择"数据报表"

3. **新建报表**
   - 点击"新建报表"按钮
   - 选择"数据报表"类型
   - 输入报表名称：`企业征信业务监测指标-经营情况`
   - 点击"确定"

### 步骤2：配置数据集

1. **添加数据集**
   - 在报表设计界面，点击左侧"数据集"面板
   - 点击"+"按钮，选择"SQL数据集"

2. **配置SQL查询**
   - 数据集名称：`经营情况数据`
   - 数据源：选择系统数据库连接
   - SQL语句：复制本节 2.1 的 CTE 拼装版 SQL
   - 点击"测试"按钮，验证SQL是否正确

3. **配置查询参数**
   - 点击"参数"标签页
   - 添加参数：
     - **参数名**：`periodId`（与 SQL 中 `${periodId}` 保持一致，使用驼峰）
     - **参数类型**：文本
     - **默认值**：可设置为空或当前月份（如：`2025-12`）
     - **是否必填**：是
   - 点击"确定"保存参数配置

### 步骤3：设计报表布局

#### 3.1 创建表格

1. **插入表格**
   - 在报表设计区域，点击"插入" → "表格"
   - 选择"2列N行"表格（根据实际需求调整行数）

2. **设置表格样式**
   - 第一列：指标名称列（宽度约40%）
   - 第二列：数值列（宽度约60%）
   - 设置表头样式（背景色、字体加粗等）

#### 3.2 填充表格内容

**第一行（表头）**：
- 单元格A1：`指标`
- 单元格B1：`数值`

**第二行**：
- 单元格A2：`资产 (单位: 人民币元)`
- 单元格B2：绑定字段 `asset_amount`，或输入公式 `=asset_amount`

**第三行**：
- 单元格A3：`负债 (单位: 人民币元)`
- 单元格B3：绑定字段 `liability_amount`，或输入公式 `=liability_amount`

**第四行**：
- 单元格A4：`本年度总收入 (单位: 人民币元)`
- 单元格B4：绑定字段 `year_income_amount`，或输入公式 `=year_income_amount`

**第五行**：
- 单元格A5：`本年度企业征信业务收入 (单位: 人民币元)`
- 单元格B5：绑定字段 `year_credit_income_amount`，或输入公式 `=year_credit_income_amount`

**第六行**：
- 单元格A6：`本年度净利润 (单位: 人民币元)`
- 单元格B6：绑定字段 `year_net_profit_amount`，或输入公式 `=year_net_profit_amount`

**第七行**：
- 单元格A7：`员工总人数`
- 单元格B7：绑定字段 `employee_total`，或输入公式 `=employee_total`

**第八行**：
- 单元格A8：`从事征信业务员工数`
- 单元格B8：绑定字段 `employee_credit_biz`，或输入公式 `=employee_credit_biz`

**第九行**：
- 单元格A9：`企业参与行业、地方信用体系建设项目数`
- 单元格B9：绑定字段 `project_count`，或输入公式 `=project_count`

**第十行**：
- 单元格A10：`企业参与行业、地方信用体系建设收入 (单位: 人民币元)`
- 单元格B10：绑定字段 `income_amount`，或输入公式 `=income_amount`

#### 3.3 设置数值格式

1. **金额字段格式化**
   - 选中金额字段单元格（B2、B3、B4、B5、B6、B10）
   - 右键 → "单元格格式" → "数字"
   - 选择"数值"格式
   - 设置小数位数：2位
   - 设置千分位分隔符：是
   - 点击"确定"

2. **人数字段格式化**
   - 选中人数字段单元格（B7、B8、B9）
   - 右键 → "单元格格式" → "数字"
   - 选择"数值"格式
   - 设置小数位数：0位
   - 点击"确定"

### 步骤4：配置报表参数（仅用查询栏，不用 URL 传参）

积木报表 2.3.0 的“数据报表”设计器里**一般不会出现“插入 → 参数面板”**。这里只使用右侧的**查询栏（查询设置）**来输入 `periodId`：

1. 在右侧面板找到 **查询设置**：
   - 打开 **默认查询**
   - 打开 **展开查询**（这样预览页会出现查询栏）
2. 在预览页的查询栏里输入 `periodId = 2025-12`（字段名要与 SQL 中的 `${periodId}` 完全一致，驼峰）
3. 点击“查询/刷新”触发数据集重新取数

> 提醒：参数名保持驼峰 `periodId`，不要用下划线 `period_id`，否则取不到值。

### 步骤5：添加报表标题

1. **插入标题**
   - 在报表顶部，点击"插入" → "文本"
   - 输入标题：`企业征信业务监测指标`
   - 设置字体大小、加粗等样式

2. **插入副标题**
   - 在标题下方，插入文本
   - 输入：`(二) 经营情况`
   - 设置字体样式

3. **插入周期显示**
   - 在副标题下方，插入文本
   - 输入：`${param.periodId}`（例如 `2025-12`）

### 步骤6：预览和测试

1. **预览报表**
   - 点击工具栏"预览"按钮
   - 输入测试参数：`periodId = 2025-12`（或通过 URL 传参）
   - 查看报表显示效果

2. **验证数据**
   - 检查数据是否正确显示
   - 检查数值格式是否正确
   - 检查是否有NULL值显示异常

3. **调整优化**
   - 根据预览效果调整表格样式
   - 调整列宽、行高
   - 优化字体大小和颜色

### 步骤7：保存和发布

1. **保存报表**
   - 点击"保存"按钮
   - 输入报表编码：`operating_conditions_report`
   - 输入报表名称：`企业征信业务监测指标-经营情况`
   - 选择报表分类
   - 点击"确定"

2. **设置权限**
   - 在报表设置中，配置查看权限
   - 设置哪些角色可以查看此报表

3. **发布报表**
   - 确认报表无误后，点击"发布"
   - 报表即可在系统中使用

## 四、常见问题处理

### 4.1 数据为空或NULL
**问题**：报表显示为空或显示NULL
**解决方案**：
- 检查SQL查询中的 `COALESCE` 函数是否正确使用
- 检查 `period_id` 参数是否正确传入
- 检查数据库中是否有对应周期的数据
- 检查表关联条件是否正确（`parent_id` 关联）

### 4.2 数据显示不正确
**问题**：显示的数值与预期不符
**解决方案**：
- 检查SQL查询逻辑是否正确
- 检查字段绑定是否正确
- 检查是否有多个记录（如果有多条记录，可能需要使用聚合函数）
- 检查 `deleted` 字段过滤是否正确

### 4.3 数值格式问题
**问题**：金额显示没有千分位分隔符或小数位数不对
**解决方案**：
- 检查单元格格式设置
- 确认数值格式为"数值"类型
- 设置正确的小数位数和千分位分隔符

### 4.4 参数不生效
**问题**：修改参数后数据不更新/查询不到数据
**解决方案**：
- 检查参数名称是否与SQL中的 `${param.periodId}` 一致（特别注意驼峰 vs 下划线）
- 检查参数控件是否正确绑定参数
- 检查参数是否必填且已正确传入

## 五、SQL查询优化建议

### 5.1 如果有多条记录需要汇总

如果同一个周期有多条填报记录（不同角色），需要汇总所有记录的数据，可以使用以下SQL：

```sql
SELECT 
    '${param.periodId}' AS period_id,
    -- 财务数据汇总
    SUM(COALESCE(f.asset_amount, 0)) AS asset_amount,
    SUM(COALESCE(f.liability_amount, 0)) AS liability_amount,
    SUM(COALESCE(f.year_income_amount, 0)) AS year_income_amount,
    SUM(COALESCE(f.year_credit_income_amount, 0)) AS year_credit_income_amount,
    SUM(COALESCE(f.year_net_profit_amount, 0)) AS year_net_profit_amount,
    -- 人员数据汇总（取最大值，因为人员数不应该累加）
    MAX(COALESCE(h.employee_total, 0)) AS employee_total,
    MAX(COALESCE(h.employee_credit_biz, 0)) AS employee_credit_biz,
    -- 信用体系建设数据汇总
    SUM(COALESCE(c.project_count, 0)) AS project_count,
    SUM(COALESCE(c.income_amount, 0)) AS income_amount
FROM report_fill_basic_info r
LEFT JOIN report_fill_biz_stat_finance f ON f.parent_id = r.id AND f.deleted = 0
LEFT JOIN report_fill_biz_stat_hr h ON h.parent_id = r.id AND h.deleted = 0
LEFT JOIN report_fill_biz_stat_credit_build c ON c.parent_id = r.id AND c.deleted = 0
WHERE r.deleted = 0
  AND r.period_id = '${param.periodId}'
GROUP BY r.period_id
```

**注意**：
- 财务数据使用 `SUM` 汇总（累加）
- 人员数据使用 `MAX` 取最大值（因为人员数不应该累加，应该取当前值）
- 信用体系建设数据使用 `SUM` 汇总（累加）

### 5.2 如果只需要单条记录

如果每个周期只有一条记录，使用第一个SQL即可（不需要GROUP BY和聚合函数）。

## 六、后续扩展

### 6.1 添加数据校验
- 可以在报表中添加数据校验规则
- 例如：检查资产是否大于负债等

### 6.2 添加趋势分析
- 可以添加图表展示数据趋势
- 例如：折线图展示月度收入变化

### 6.3 添加导出功能
- 配置报表导出为Excel、PDF格式
- 保持数值格式和样式

## 七、配置检查清单

- [ ] SQL查询语句正确，能正常执行
- [ ] 参数配置正确，参数名与SQL中的参数名一致
- [ ] 表格布局设计完成，所有字段都已绑定
- [ ] 数值格式设置正确（金额2位小数，人数0位小数）
- [ ] 报表标题和副标题已添加
- [ ] 参数控件已添加并正确绑定
- [ ] 预览测试通过，数据显示正确
- [ ] 报表已保存并设置权限
- [ ] 报表已发布，可以在系统中访问
