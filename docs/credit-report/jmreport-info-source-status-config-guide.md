# 积木报表配置指南：信息来源情况报表（基于 `report_fill_info_source_by_industry`）

> 版本：JimuReport 2.3.0  
> 数据库：PostgreSQL v15  
> 相关建表 SQL：`sql/postgresql/credit-report-2026-02-02-uuid.sql`  
> 字典配置 SQL：`sql/postgresql/jimureport-custom-2026-02-03.sql`

---

## 一、报表需求和数据来源梳理

### 1.1 报表场景

- **报表名称**：企业征信业务监测指标 - (四) 信息来源情况
- **报表类型**：数据报表（按月统计）
- **填报部门**：数据管理部
- **展示内容**：
  - 21种信息来源类型（银行、证券、保险、信托等）
  - 每种类型显示：
    - **累计签约的信息提供机构总数**（`provider_org_total`）
    - **当前提供服务的机构数**（`provider_org_current`）
  - 合计行（所有类型的汇总）
  - 网络爬取渠道数（单独显示）

### 1.2 数据来源表结构

#### 主数据表：`report_fill_info_source_by_industry`

```sql
CREATE TABLE report_fill_info_source_by_industry (
  id                   VARCHAR(32) NOT NULL,
  record_id            VARCHAR(32) NOT NULL,  -- 关联 report_fill_basic_info.id
  industry_code        VARCHAR(64),            -- 行业代码（对应字典 item_value）
  provider_org_total   NUMERIC(18, 0),        -- 累计签约的信息提供机构总数
  provider_org_current NUMERIC(18, 0),        -- 当前提供服务的机构数
  deleted              int2 NOT NULL DEFAULT 0
);
```

- `record_id`：关联主表 `report_fill_basic_info.id`
- `industry_code`：对应字典 `info_source_status` 的 `item_value`（如：`bank`、`security`、`insurance` 等）
- `provider_org_total`：累计签约总数
- `provider_org_current`：当前服务数

#### 辅助数据：`report_fill_info_collect_stat`

```sql
-- 网络爬取渠道数
crawler_network_channel_total NUMERIC(18, 0)
```

### 1.3 字典配置

- **字典类型**：`info_source_status`（ID: `1178553700000000001`）
- **字典项数量**：21个
- **字典项示例**：
  - `bank` - 银行
  - `security` - 证券
  - `insurance` - 保险
  - `trust` - 信托
  - `government` - 政府
  - `public_utilities` - 公用事业单位
  - ...（共21项）

---

## 二、整体设计思路

### 2.1 设计原则

1. **SQL 负责数据查询和聚合**：从 `report_fill_info_source_by_industry` 按 `industry_code` 分组汇总
2. **字典表 LEFT JOIN**：确保所有21种信息来源类型都显示（即使没有数据也显示0）
3. **合计行在报表中计算**：使用单元格公式 `SUM()` 计算合计
4. **网络爬取渠道数单独获取**：通过子查询从 `report_fill_info_collect_stat` 获取

### 2.2 数据流程

```
字典表 (jimu_dict_item) 
  ↓ LEFT JOIN
主表 (report_fill_basic_info) 
  ↓ LEFT JOIN
数据表 (report_fill_info_source_by_industry)
  ↓ GROUP BY + SUM
21行结果（每行一种信息来源类型）
  +
子查询获取网络爬取渠道数
```

---

## 三、SQL 数据集设计

### 3.1 完整 SQL 查询语句

```sql
SELECT
    d.item_text AS source_type_name,
    d.item_value AS industry_code,
    d.sort_order,
    COALESCE(SUM(s.provider_org_total), 0) AS provider_org_total,
    COALESCE(SUM(s.provider_org_current), 0) AS provider_org_current,
    (SELECT COALESCE(c.crawler_network_channel_total, 0)
     FROM report_fill_basic_info r2
     JOIN report_fill_info_collect_stat c 
       ON c.parent_id = r2.id 
      AND c.deleted = 0
     WHERE r2.deleted = 0
       AND r2.period_id = '${periodId}'
       AND r2.report_id = '<<信息来源情况报表模板ID>>'
     ORDER BY r2.create_time DESC
     LIMIT 1) AS crawler_network_channel_total
FROM jimu_dict_item d
LEFT JOIN report_fill_basic_info r
    ON r.deleted = 0
    AND r.period_id = '${periodId}'
    AND r.report_id = '<<信息来源情况报表模板ID>>'
LEFT JOIN report_fill_info_source_by_industry s
    ON s.record_id = r.id
    AND s.industry_code = d.item_value
    AND s.deleted = 0
WHERE d.dict_id = '1178553700000000001'  -- info_source_status 字典ID
  AND d.status = 1
GROUP BY d.item_text, d.item_value, d.sort_order
ORDER BY d.sort_order;
```

### 3.2 SQL 参数说明

- **`${periodId}`**：填报周期参数（例如 `2025-12`）
  - 参数名：`periodId`
  - 类型：文本
  - 必填：是
  - 默认值：可选当前月份或留空

- **`<<信息来源情况报表模板ID>>`**：请替换为你在积木里配置"信息来源情况在线填报模板"的 `jimu_report.id`

### 3.3 SQL 输出字段说明

| 字段名 | 说明 | 示例值 |
|--------|------|--------|
| `source_type_name` | 信息来源类型名称 | "银行"、"证券"、"保险" |
| `industry_code` | 行业代码 | "bank"、"security"、"insurance" |
| `sort_order` | 排序顺序 | 1、2、3... |
| `provider_org_total` | 累计签约的信息提供机构总数 | 2、0、0 |
| `provider_org_current` | 当前提供服务的机构数 | 0、0、0 |
| `crawler_network_channel_total` | 网络爬取渠道数 | 9 |

---

## 四、在积木报表中手动配置的具体步骤

### 步骤 1：创建数据报表

1. 登录积木报表后台 → **报表管理** → 选择"数据报表"
2. 点击"新建报表"，类型选择"数据报表"
3. 报表名称：`企业征信业务监测指标-信息来源情况`

### 步骤 2：配置 SQL 数据集

1. 在设计界面左侧"数据集"面板点击 `+` → 选择"SQL 数据集"
2. 数据集名称：`信息来源情况数据`
3. 数据源：选择你的业务库数据源
4. 将第 3.1 节的 SQL 复制进去，替换 `<<信息来源情况报表模板ID>>` 为实际 `report_id`
5. 切换到"参数"页签，添加参数：
   - 参数名：`periodId`
   - 类型：文本
   - 默认值：如 `2025-12`
   - 必填：勾选
6. 点击"测试"，确认能返回21行数据，每行包含：
   - `source_type_name`（信息来源类型名称）
   - `provider_org_total`（累计总数）
   - `provider_org_current`（当前服务数）
   - `crawler_network_channel_total`（网络爬取渠道数，每行值相同）

### 步骤 3：设计表格布局

根据你的 Excel 模板，设计表格结构：

1. **表头行**：
   - A1：`信息来源情况`
   - B1：`累计签约的信息提供机构总数`
   - C1：`当前提供服务的机构数`

2. **数据行**（21行，对应21种信息来源类型）：
   - A2～A22：信息来源类型名称（绑定 `source_type_name`）
   - B2～B22：累计总数（绑定 `provider_org_total`）
   - C2～C22：当前服务数（绑定 `provider_org_current`）

3. **合计行**（第23行）：
   - A23：`合计`
   - B23：公式 `=SUM(B2:B22)`
   - C23：公式 `=SUM(C2:C22)`

4. **网络爬取渠道数**（单独显示）：
   - 可在表格下方或单独区域显示
   - 绑定字段：`crawler_network_channel_total`（取第一行的值即可）

### 步骤 4：字段绑定

#### 4.1 数据行绑定（使用数据集循环）

1. 在表格中选中数据行区域（A2:C22）
2. 右键 → 选择"数据集循环"或"列表循环"
3. 绑定数据集：`信息来源情况数据`
4. 字段绑定：
   - A列（信息来源类型）：绑定 `source_type_name`
   - B列（累计总数）：绑定 `provider_org_total`
   - C列（当前服务数）：绑定 `provider_org_current`

#### 4.2 合计行公式

- B23 单元格：`=SUM(B2:B22)`
- C23 单元格：`=SUM(C2:C22)`

#### 4.3 网络爬取渠道数显示

- 方式1：在表格下方添加一行，绑定 `crawler_network_channel_total`（取第一行数据）
- 方式2：在单独文本框中显示，绑定 `crawler_network_channel_total`

### 步骤 5：配置查询栏参数

1. 右侧面板 → **查询设置**：
   - 开启 **默认查询**
   - 开启 **展开查询**
2. 添加查询控件：
   - 控件类型：文本输入框
   - 控件名称：`periodId`
   - 绑定参数：`periodId`
   - 默认值：如 `2025-12`
3. 预览页中，在查询栏输入：`periodId = 2025-12`
4. 点击"查询"按钮，触发数据集重新取数

> **注意**：查询栏里的参数名必须是 `periodId`，与 SQL 里的 `${periodId}` 完全一致（区分大小写）

---

## 五、报表布局示例（参考）

```
┌─────────────────────────────────────────────────────────────┐
│  企业征信业务监测指标 - (四) 信息来源情况                    │
│  填报周期：${periodId}                                       │
├─────────────────────────────────────────────────────────────┤
│  信息来源情况  │ 累计签约的信息提供机构总数 │ 当前提供服务的机构数 │
├─────────────────────────────────────────────────────────────┤
│  银行         │  2                        │  0                │
│  证券         │  0                        │  0                │
│  保险         │  0                        │  0                │
│  ...          │  ...                      │  ...              │
│  政府         │  45                       │  45               │
│  公用事业单位  │  1                        │  1                │
│  ...          │  ...                      │  ...              │
│  其他         │  1                        │  0                │
├─────────────────────────────────────────────────────────────┤
│  合计         │  106                      │  90               │
├─────────────────────────────────────────────────────────────┤
│  网络爬取渠道数：9                                           │
└─────────────────────────────────────────────────────────────┘
```

---

## 六、扩展与校验建议

### 6.1 数据校验

可在报表中增加辅助行/备注，用于校验：

- 检查合计行是否等于所有明细行的汇总
- 检查 `provider_org_current` 是否 ≤ `provider_org_total`
- 必要时添加"数据校验提示"文本框，使用条件格式标红异常

### 6.2 扩展更多来源分类

如果后续监管模板新增来源类别：

1. 在字典 `info_source_status` 中补充对应字典项
2. SQL 会自动包含新字典项（因为使用字典表 LEFT JOIN）
3. 在报表中新增一行，对应绑定新字段即可

---

## 七、落地检查清单

- [ ] SQL 数据集能在积木中"测试通过"，返回21行数据
- [ ] 字段 `source_type_name`、`provider_org_total`、`provider_org_current` 正确显示
- [ ] 网络爬取渠道数 `crawler_network_channel_total` 正确显示
- [ ] 表格布局已搭建完毕，与监管模板结构一致
- [ ] 数据行已正确绑定字段（使用数据集循环）
- [ ] 合计行公式已正确设置（`SUM()`）
- [ ] 查询栏参数 `periodId` 工作正常，切换周期数据随之变化
- [ ] 报表已保存、设置权限并成功发布

---

## 八、常见问题

### Q1: SQL 返回的数据行数不是21行？

**A**: 检查：
- 字典表 `jimu_dict_item` 中 `dict_id = '1178553700000000001'` 的字典项是否正好21个
- `d.status = 1` 条件是否正确（确保字典项是启用状态）

### Q2: 网络爬取渠道数显示为0或空？

**A**: 检查：
- `report_fill_info_collect_stat` 表中是否有对应周期的数据
- 子查询中的 `report_id` 是否正确
- 确认 `crawler_network_channel_total` 字段有值

### Q3: 某些信息来源类型显示为0，但实际有数据？

**A**: 检查：
- `report_fill_info_source_by_industry` 表中的 `industry_code` 是否与字典的 `item_value` 完全匹配（区分大小写）
- 确认 `period_id` 和 `report_id` 过滤条件正确

---

## 九、SQL 优化建议（可选）

如果数据量较大，可以考虑以下优化：

1. **索引优化**：
   ```sql
   -- 确保以下字段有索引
   CREATE INDEX idx_report_fill_basic_info_period_report 
     ON report_fill_basic_info(period_id, report_id, deleted);
   
   CREATE INDEX idx_report_fill_info_source_by_industry_record_code 
     ON report_fill_info_source_by_industry(record_id, industry_code, deleted);
   ```

2. **子查询优化**：如果网络爬取渠道数在同一周期内不会变化，可以考虑将子查询改为 JOIN，减少查询次数。

---

**文档版本**：v1.0  
**最后更新**：2026-02-03  
**维护人员**：数据管理部
