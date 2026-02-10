# 积木报表配置指南：信息采集/来源情况数据报表（基于 `report_fill_info_collect_stat`）

> 版本：JimuReport 2.3.0  
> 数据库：PostgreSQL v15  
> 相关建表 SQL：`sql/postgresql/credit-report-2026-02-02-uuid.sql`

---

## 一、报表需求和数据来源梳理

### 1.1 报表场景（对应你截图中的 A、B、C…）

- 报表名称示例：**企业征信业务监测指标 - (三) 信息来源情况**
- 报表类型：**数据报表（按月统计）**
- 展示内容（典型结构，与你 Excel 截图一致）：
  - 1 收录企业（含个体工商户、小微企业）总户数 A
  - 1.1 收录境内企业（含个体工商户、小微企业）户数 A  
  - 1.1.1 仅收录基本信息的企业（含个体工商户、小微企业）户数 A-B  
  - 1.1.2 收录基本信息以外信息的企业户数  
    - 1.1.2.1 收录借贷信息的企业户数 B  
    - 1.1.2.2 收录商业交易/支付信息的企业户数 C  
    - 1.1.2.3 收录债券信息的企业户数 D  
    - 1.1.2.4 收录税务信息的企业户数 E  
    - 1.1.2.5 收录进出口信息的企业户数 F  
    - 1.1.2.6 收录电商等信息的企业户数 G  
  - 2 收录小微企业总户数 D  
  - 3 收录个体工商户总户数 E  
  - 4 收录自然人股东、公司董事、监事、高管等人员人数 F  

> **关键点**：A/B/C/D/E/F/G 等是**可计算字段**，基础数据全部来自表  
> `report_fill_info_collect_stat`，再通过报表单元格公式做组合和比例计算。

### 1.2 数据来源表结构回顾

根据 `credit-report-2026-02-02-uuid.sql`，信息采集情况表结构为：

```sql
CREATE TABLE report_fill_info_collect_stat (
  id                                   VARCHAR(32) NOT NULL DEFAULT replace(gen_random_uuid()::text, '-', ''),
  parent_id                            VARCHAR(32) NOT NULL,
  collect_enterprise_total             NUMERIC(18, 0),
  collect_borrow_total                 NUMERIC(18, 0),
  collect_trade_total                  NUMERIC(18, 0),
  collect_finance_total                NUMERIC(18, 0),
  collect_tax_total                    NUMERIC(18, 0),
  collect_custom_total                 NUMERIC(18, 0),
  collect_public_utility_total         NUMERIC(18, 0),
  collect_field_sme_total              NUMERIC(18, 0),
  collect_micro_sme_total              NUMERIC(18, 0),
  collect_individual_total             NUMERIC(18, 0),
  collect_natural_person_shareholder_total NUMERIC(18, 0),
  crawler_network_channel_total        NUMERIC(18, 0),
  create_time                          TIMESTAMP,
  update_time                          TIMESTAMP,
  creator                              VARCHAR(64),
  updater                              VARCHAR(64),
  deleted                              int2 NOT NULL DEFAULT 0
);
```

- `parent_id`：对应主表 `report_fill_basic_info.id`
- 其余字段是不同维度的“收录/采集户数/人数”，可直接作为监管表中 A/B/C/D/E/F 等基础数字。

---

## 二、整体设计思路（保证“能落地”）

### 2.1 设计原则

1. **SQL 只负责把基础数查出来并“行转列”**，例如：从 `industry_code` 不同值汇总成 B、C、D、E、F、G 等列。
2. **所有复杂公式（A、A-B、SUM(B+常数+…)*99.4%）都在报表单元格中完成**，完全照你 Excel 截图抄公式。
3. **按周期取数**：通过 `periodId` 参数从主表 `report_fill_basic_info` 过滤到当月数据，再关联到 `report_fill_info_source_by_industry`。
4. **只取一行结果**：SQL 返回 1 行（该周期的综合情况），表格每一行绑定这一行里的不同字段或用公式计算。

### 2.2 关键字段映射（示例）

> 下面是一个**建议映射**，你可以根据监管口径和实际含义微调，只要保持“每个监管行从哪个字段取数”这件事是明确的即可。

| 业务含义（与你截图对应）                                   | 来源字段                                | SQL 输出字段名          |
|----------------------------------------------------------|-----------------------------------------|-------------------------|
| 收录企业（含个体工商户、小微企业）总户数（A）            | `collect_enterprise_total`              | `A_enterprise_total`    |
| 收录借贷信息的企业户数（B）                              | `collect_borrow_total`                  | `B_borrow_total`        |
| 收录商业交易/支付信息的企业户数（C）                     | `collect_trade_total`                   | `C_trade_total`         |
| 收录债券信息的企业户数（D，若有）                        | `collect_finance_total`                 | `D_finance_total`       |
| 收录税务信息的企业户数（E）                              | `collect_tax_total`                     | `E_tax_total`           |
| 收录进出口信息的企业户数（F）                            | `collect_custom_total`                  | `F_custom_total`        |
| 收录公共事业信息的企业户数（如有，G）                    | `collect_public_utility_total`          | `G_public_utility_total`|
| 收录小微企业总户数（D 行，可选）                         | `collect_micro_sme_total`               | `D_micro_sme_total`     |
| 收录个体工商户总户数（E 行，可选）                       | `collect_individual_total`              | `E_individual_total`    |
| 收录自然人股东、公司董监高等人员人数（F 行）             | `collect_natural_person_shareholder_total` | `F_shareholder_total` |

> 建议：你可以把监管表每一行和上表做一次人工确认，必要时对“来源字段 → 输出字段名”做个自己的对照表，后续维护就会很清晰。

---

## 三、SQL 数据集设计（直接取 `report_fill_info_collect_stat` 的字段）

### 3.1 SQL 查询语句（示例，可直接在积木中创建数据集）

> 假设：  
> - 一个周期内，只取某一个“信息采集情况在线填报模板”的记录，例如 `report_id = 'xxxx'`。  
> - 如你需要从 URL 或外层系统传 `reportId`，可以把 `report_id` 写成参数；这里先写成固定值，方便手动配置。

```sql
SELECT
  '${periodId}' AS period_id,
  -- 直接取信息采集情况表中的各类统计字段
  COALESCE(s.collect_enterprise_total,              0) AS A_enterprise_total,
  COALESCE(s.collect_borrow_total,                  0) AS B_borrow_total,
  COALESCE(s.collect_trade_total,                   0) AS C_trade_total,
  COALESCE(s.collect_finance_total,                 0) AS D_finance_total,
  COALESCE(s.collect_tax_total,                     0) AS E_tax_total,
  COALESCE(s.collect_custom_total,                  0) AS F_custom_total,
  COALESCE(s.collect_public_utility_total,          0) AS G_public_utility_total,
  COALESCE(s.collect_field_sme_total,               0) AS field_sme_total,
  COALESCE(s.collect_micro_sme_total,               0) AS micro_sme_total,
  COALESCE(s.collect_individual_total,              0) AS individual_total,
  COALESCE(s.collect_natural_person_shareholder_total, 0) AS F_shareholder_total,
  COALESCE(s.crawler_network_channel_total,         0) AS crawler_network_channel_total
FROM report_fill_basic_info r
JOIN report_fill_info_collect_stat s
  ON s.parent_id = r.id
 AND s.deleted   = 0
WHERE r.deleted   = 0
  AND r.period_id = '${periodId}'
  AND r.report_id = '<<信息采集情况报表模板ID>>'
ORDER BY r.create_time DESC
LIMIT 1;
```

- **`'<<信息来源情况报表模板ID>>'`**：请替换为你在积木里配置“信息来源情况在线填报模板”的 `jimu_report.id`。
- 如你希望同一周期内可能有多条记录（不同角色）**汇总**，上面的 SQL 已经用 `SUM` 汇总了多行。

### 3.2 SQL 参数说明

- **`${periodId}`**：填报周期参数（例如 `2025-12`）
  - 参数名：`periodId`
  - 类型：文本
  - 必填：是
  - 默认值：可选当前月份或留空

---

## 四、在积木报表中手动配置的具体步骤

### 步骤 1：创建数据报表

1. 登录积木报表后台 → **报表管理** → 选择“数据报表”。
2. 点击“新建报表”，类型选择“数据报表”。
3. 报表名称示例：`企业征信业务监测指标-信息来源情况`。

### 步骤 2：配置 SQL 数据集

1. 在设计界面左侧“数据集”面板点击 `+` → 选择“SQL 数据集”。
2. 数据集名称：`信息来源情况数据`。
3. 数据源：选择你的业务库数据源。
4. 将第 3.1 节的 SQL 复制进去，替换 `<<信息来源情况报表模板ID>>` 为实际 `report_id`。
5. 切换到“参数”页签，添加参数：
   - 参数名：`periodId`
   - 类型：文本
   - 默认值：如 `2025-12`
   - 必填：勾选。
6. 点击“测试”，确认能返回一行数据，列中有 `A_enterprise_total、B_borrow_total、C_trade_total、E_tax_total、F_custom_total、F_shareholder_total` 等字段。

### 步骤 3：设计表格布局（按监管模板/Excel 样式）

1. 在画布上插入一个 2 列 N 行的表格（N 覆盖你所有指标行数）。
2. 第一列填写指标名称（完全照监管模板/你截图中的文字即可）。
3. 第二列留作数值列，用于绑定字段或写公式。

示例（假设数值列为 B 列）：

- A2：`1 收录企业（含个体工商户、小微企业）总户数`
- A3：`1.1 收录境内企业（含个体工商户、小微企业）户数`
- A4：`1.1.1 仅收录基本信息的企业（含个体工商户、小微企业）户数`
- A5：`1.1.2 收录基本信息以外信息的企业户数`
- A6～A11：不同来源类型的明细项，对应 B/C/D/E/F/G。

### 步骤 4：字段绑定与单元格公式（核心）

#### 4.1 绑定基础字段（A/B/C/D/E/F 等）

假设：

- 第二列 B2 对应“1 收录企业（含个体工商户、小微企业）总户数 A”
- 第二列 B6 对应“收录借贷信息的企业户数（B）”
- 第二列 B7 对应“收录商业交易/支付信息的企业户数（C）”
- 第二列 B8 对应“收录财务/融资信息的企业户数（D）”
- 第二列 B9 对应“收录税务信息的企业户数（E）”
- 第二列 B10 对应“收录进出口信息的企业户数（F）”
- 第二列 B11 对应“收录电商等信息的企业户数（G）”

则：

- B2：绑定字段 `A_enterprise_total` 或公式 `=A_enterprise_total`
- B6：绑定字段 `B_borrow_total` 或公式 `=B_borrow_total`
- B7：绑定字段 `C_trade_total` 或公式 `=C_trade_total`
- B8：绑定字段 `D_finance_total` 或公式 `=D_finance_total`
- B9：绑定字段 `E_tax_total` 或公式 `=E_tax_total`
- B10：绑定字段 `F_custom_total` 或公式 `=F_custom_total`
- B11：绑定字段 `G_public_utility_total`（如你使用公共事业口径）或其他你确认好的字段。

#### 4.2 按 Excel 方式写计算公式（A、A-B、复杂 SUM）

以下行号仅为示例，请按你实际表格行号调整：

- **A 行（总户数）**：假设在 B2，需要等于“境内企业户数 A”，比如你决定它就是所有来源企业的某种汇总，可以在 SQL 中单独输出一个字段 `A_total`，这里不展开。  
  - 表格中：B2 写 `=A_total`。

- **仅收录基本信息的企业户数（A-B）**：假设在 B4：
  - B4 公式：`=B2 - B5`（A - B）

- **“收录基本信息以外信息的企业户数”那行（你截图中的复杂公式）**：假设在 B5：

  你 Excel 中类似：
  \[
    \text{B5} = (B6 + 67604 + B7 + 6901 + 21266 + 483367 + 0) \times 99.4\%
  \]

  则在积木单元格中直接写：

  ```text
  =(B6 + 67604 + B7 + 6901 + 21266 + 483367 + 0) * 0.994
  ```

  - 其中 `B6、B7` 是明细行的单元格引用。
  - `67604、6901、21266、483367` 为你监管模板中给定的常数，直接写死在公式里即可。

- **小微企业总户数、个体工商户总户数、自然人股东人数等**：
  - 建议与 SQL 再增加几个字段，例如 `micro_total、individual_total、share_holder_person_total`，然后在对应行 B 列用简单公式 `=micro_total` 即可。

> **关键要点**：所有监管表格里的“A、A-B、SUM(B+常数+…)×99.4%”都不在 SQL 里实现，完全交给报表单元格公式，这样你可以和 Excel 保持一模一样的计算逻辑。

### 步骤 5：配置查询栏参数（仅用查询栏，不用 URL 传参）

1. 右侧面板 → **查询设置**：
   - 开启 **默认查询**
   - 开启 **展开查询**
2. 预览页中，在查询栏输入：`periodId = 2025-12`。
3. 点击“查询”按钮，触发数据集重新取数。

> 注意：查询栏里的参数名必须是 `periodId`，与 SQL 里的 `${periodId}` 完全一致（区分大小写）。

---

## 五、扩展与校验建议

### 5.1 扩展更多来源分类

如果后续监管模板新增来源类别（比如“公共事业单位”“政府来源”等），只需：

1. 在字典 `信息来源情况` 中补充对应 `dict_value`；
2. 在 SQL 中新增一条 `CASE WHEN s.industry_code = 'new_code' THEN ...`；
3. 在报表中新增一行，对应绑定这个新字段即可。

### 5.2 校验逻辑建议

可在报表中增加辅助行/备注，用于校验：

- 检查 `A` 是否 ≈ `仅基本信息企业数 + 基本信息以外信息企业数`；
- 检查行内合计是否等于 SQL 汇总值；
- 必要时添加“数据校验提示”文本框，使用条件格式标红异常。

---

## 六、落地检查清单

- [ ] SQL 数据集能在积木中“测试通过”，返回 1 行数据。
- [ ] 字段 `B_loan、C_trade、D_bond、E_tax、F_custom、G_ecom` 与字典配置一一对应。
- [ ] 表格布局已搭建完毕，与监管模板结构一致。
- [ ] B/C/D/E/F/G 明细行已正确绑定字段。
- [ ] A、A-B、复杂 SUM 等行的公式已经按 Excel 原公式搬运到单元格。
- [ ] 查询栏参数 `periodId` 工作正常，切换周期数据随之变化。
- [ ] 报表已保存、设置权限并成功发布。

如果你把“监管模板需求 + 实际字典的 industry_code + 你想要的常数和公式”整理成一张表，我也可以帮你把上面的 SQL 和单元格公式再精确到“每一行该写什么”，你只需要复制粘贴即可。  
