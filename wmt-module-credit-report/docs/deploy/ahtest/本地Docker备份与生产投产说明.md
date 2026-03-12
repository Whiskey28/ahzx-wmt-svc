# 本地 Docker PG 备份与生产投产说明

## 一、backup.sql 与 sql/postgresql 脚本的对应关系

`backup.sql` 是测试环境的**完整库 pg_dump**（schema + data），不是“增量升级脚本”。当前测试环境的结构大致由以下仓库内脚本叠加形成：


| 用途             | 脚本路径                                                               | 说明                                                                        |
| -------------- | ------------------------------------------------------------------ | ------------------------------------------------------------------------- |
| 信用报送主表（UUID 版） | `sql/postgresql/credit-report-2026-02-02-uuid.sql`                 | 所有 `report_fill`_* 主表结构（填报锚点、企业基础、经营情况、产品统计等）                             |
| 固定字段升级         | `sql/postgresql/v2/credit-report-2026-02-06-fixed-fields.sql`      | 将 `report_fill_info_source_by_industry` 等改为固定字段 1:1 宽表，与当前 backup 中该表结构一致 |
| 审计触发器          | `sql/postgresql/credit-report-2026-02-02-uuid-triggers.sql`        | 为 report_fill_* 表自动填充 create_time/update_time                             |
| 长三角征信链（新增）     | `sql/postgresql/credit-report-2026-02-28-yangtze-credit-chain.sql` | 数据部「长三角征信链」明细表（数据类型 + 来源）                                                 |
| 角色/字典等         | `sql/postgresql/credit-report-role-insert.sql`、其他业务 SQL            | 按需执行                                                                      |


**投产时不要用 backup.sql 直接覆盖生产库**：backup 含全库对象和数据，仅适合做测试环境还原或参考。生产应使用**按顺序执行的建表/升级脚本**。

---

## 二、本地 Docker PostgreSQL 如何备份

### 2.1 假设

- 容器名或服务名：例如 `pg15` 或由 `docker-compose-pg15.yml` 启动的 Postgres 服务名  
- 数据库名：如 `tdc-credit`（与 `application*.yml` 中一致）  
- 本地端口：如 `5432`（或 compose 里映射的宿主机端口）

### 2.2 仅备份 schema（推荐用于版本管理 / 生产对照）

```bash
docker exec <容器名> pg_dump -U <用户名> -d <数据库名> --schema-only --no-owner --no-privileges -f /tmp/schema.sql
docker cp <容器名>:/tmp/schema.sql ./backup-schema-$(date +%Y%m%d).sql
```

示例（用户 `ahzx`，库 `tdc-credit`）：

```bash
docker exec pg15 pg_dump -U ahzx -d tdc-credit --schema-only --no-owner --no-privileges -f /tmp/schema.sql
docker cp pg15:/tmp/schema.sql ./backup-schema-20260228.sql
```

### 2.3 全库备份（schema + data，用于测试环境还原）

```bash
docker exec <容器名> pg_dump -U <用户名> -d <数据库名> -F c -f /tmp/full.dump
docker cp <容器名>:/tmp/full.dump ./backup-full-$(date +%Y%m%d).dump
```

或生成 SQL 格式（便于查看/部分恢复）：

```bash
docker exec <容器名> pg_dump -U <用户名> -d <数据库名> --no-owner --no-privileges > backup-full-$(date +%Y%m%d).sql
```

### 2.4 只备份信用报送相关表（便于小范围迁移）

```bash
docker exec <容器名> pg_dump -U <用户名> -d <数据库名> --no-owner --no-privileges \
  -t report_fill_basic_info \
  -t report_fill_enterprise_basic \
  -t report_fill_info_source_by_industry \
  -t report_fill_yangtze_credit_chain \
  -t 'report_fill_*' \
  > backup-credit-report-tables-$(date +%Y%m%d).sql
```

（根据实际表名增减 `-t` 参数。）

---

## 三、生产投产应使用的脚本顺序（建议）

生产环境**首次部署**或**从零建库**时，建议按以下顺序执行（在已有库、用户的前提下）：

1. **信用报送主表（UUID 版）**
  - `sql/postgresql/credit-report-2026-02-02-uuid.sql`
2. **固定字段升级（与当前测试环境一致）**
  - `sql/postgresql/v2/credit-report-2026-02-06-fixed-fields.sql`
3. **长三角征信链表（数据部）**
  - `sql/postgresql/credit-report-2026-02-28-yangtze-credit-chain.sql`
4. **审计触发器**
  - `sql/postgresql/credit-report-2026-02-02-uuid-triggers.sql`  
   （若在 3 中已包含新表触发器，则无需再改；否则需保证新表也有触发器。）
5. **角色、字典、初始数据（按需）**
  - `sql/postgresql/credit-report-role-insert.sql` 等

**已有生产库的增量升级**：只执行新增或变更的脚本（例如本次新增的 `credit-report-2026-02-28-yangtze-credit-chain.sql` 以及触发器补充），不要重复执行会 DROP 表的脚本。

---

## 四、测试环境增量升级（已有大量数据，禁止全量覆盖）

当前测试环境已经存在大量业务数据时，**严禁**使用 `backup.sql` 做“全库还原/覆盖”，也**不要**执行任何包含 `DROP TABLE` 的“从零建库脚本”（例如 `credit-report-2026-02-02-uuid.sql` 这类脚本）。

本次需求仅包含两类变更：

- **新增本次表**：执行 `sql/postgresql/credit-report-2026-02-28-yangtze-credit-chain.sql`，新增 `report_fill_yangtze_credit_chain`
- **更新所有 Jimu 报表配置数据**：将 `jimu_`* 配置表的**数据**同步到测试环境（不影响 `report_fill_`* 等业务数据表）

### 4.1 新增长三角征信链表（不覆盖现有数据）

该脚本已按“增量可重复执行”方式编写（不会 DROP 表）。在测试环境库执行：

```bash
psql -h <host> -p <port> -U <user> -d <db> -f wmt-module-credit-report/sql/postgresql/credit-report-2026-02-28-yangtze-credit-chain.sql
```

建议执行后简单校验：

```sql
\d+ public.report_fill_yangtze_credit_chain
```

### 4.2 更新所有 Jimu 报表配置数据（仅覆盖 jimu_* 配置表）

Jimu 报表“配置数据”通常集中在如下表（以测试环境 `backup.sql` 为准）：

- `jimu_dict`、`jimu_dict_item`
- `jimu_report`、`jimu_report_category`
- `jimu_report_data_source`、`jimu_report_db`、`jimu_report_db_field`、`jimu_report_db_param`
- `jimu_report_icon_lib`、`jimu_report_link`、`jimu_report_map`、`jimu_report_share`
（`jimu_report_export_job`、`jimu_report_export_log` 属于任务/日志表，一般不建议迁移）

推荐做法是：*从“源库”导出上述表数据（data-only）→ 在测试环境仅清空这些 jimu_ 表 → 导入数据。这样不会触碰 `report_fill_`* 等业务数据表。

#### 4.2.1（可选）先备份测试环境现有 Jimu 配置，便于回滚

```bash
pg_dump -h <host> -p <port> -U <user> -d <db> --data-only --no-owner --no-privileges \
  -t public.jimu_dict -t public.jimu_dict_item \
  -t public.jimu_report -t public.jimu_report_category \
  -t public.jimu_report_data_source -t public.jimu_report_db -t public.jimu_report_db_field -t public.jimu_report_db_param \
  -t public.jimu_report_icon_lib -t public.jimu_report_link -t public.jimu_report_map -t public.jimu_report_share \
  > backup-jimu-config-$(date +%Y%m%d).sql
```

#### 4.2.2 从“源库”导出最新 Jimu 配置数据（data-only）

源库可以是：本地 Docker、开发库、或其它已配置好最新 Jimu 报表的环境。

```bash
pg_dump -h <src_host> -p <src_port> -U <src_user> -d <src_db> --data-only --no-owner --no-privileges \
  -t public.jimu_dict -t public.jimu_dict_item \
  -t public.jimu_report -t public.jimu_report_category \
  -t public.jimu_report_data_source -t public.jimu_report_db -t public.jimu_report_db_field -t public.jimu_report_db_param \
  -t public.jimu_report_icon_lib -t public.jimu_report_link -t public.jimu_report_map -t public.jimu_report_share \
  > jimu-config-latest.sql
```

#### 4.2.3 在测试环境仅清空 Jimu 配置表并导入

先清空（只针对 `jimu_*` 配置表）：

```sql
BEGIN;
TRUNCATE TABLE
  public.jimu_report_share,
  public.jimu_report_map,
  public.jimu_report_link,
  public.jimu_report_icon_lib,
  public.jimu_report_db_param,
  public.jimu_report_db_field,
  public.jimu_report_db,
  public.jimu_report_data_source,
  public.jimu_report,
  public.jimu_report_category,
  public.jimu_dict_item,
  public.jimu_dict
RESTART IDENTITY;
COMMIT;
```

再导入：

```bash
psql -h <host> -p <port> -U <user> -d <db> -f jimu-config-latest.sql
```

如果你只希望“追加/更新字典”，也可以仅执行仓库内的：

- `sql/postgresql/jimureport-custom-2026-02-03.sql`（字典与字典项，带冲突更新逻辑）

---

## 五、小结

- **backup.sql**：测试环境全库导出，用于还原测试或对照，不直接用于生产覆盖。  
- **生产投产**：使用仓库内 `sql/postgresql` 下的**建表/升级脚本**，按上述顺序执行。  
- **本地 Docker 备份**：用 `pg_dump` 做 schema 备份或全库备份，按需拷贝到宿主机保存。



