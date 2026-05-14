# PFB 一期数据库初始化 — 服务器直装 OceanBase（MySQL 兼容模式）操作说明

> **与 Task 2 对齐**：执行顺序、脚本路径与 [`2026-05-11-pfb-phase1-backend-tasks.md`](./2026-05-11-pfb-phase1-backend-tasks.md) 中「Task 2 — 数据库初始化」一致；本文件仅补充 **在已安装 OceanBase / OceanDB 且开启 MySQL 兼容协议** 场景下的连接方式与命令示例。  
> **凭据来源**：仓库内 [`wmt-server/src/main/resources/application-nmlocal.yaml`](../../../../wmt-server/src/main/resources/application-nmlocal.yaml) 中 `spring.datasource.dynamic.datasource.master`（及 `slave` 同构项）。以下 **主机、端口、用户名、密码** 为该文件当前示例值；**上线环境请以实际部署为准**，勿将生产口令写入公开仓库。

---

## 1. 从 `application-nmlocal.yaml` 读取的示例连接参数

| 项 | 当前仓库示例值（`master`） | 说明 |
|----|---------------------------|------|
| JDBC 主机 | `172.30.243.122` | 替换为你的 OceanDB 对外 MySQL 监听地址 |
| JDBC 端口 | `2881` | OceanBase 集群 MySQL 模式常用监听端口之一（以实际 `obproxy` / 直连配置为准） |
| JDBC URL 中的库名 | `credit_project_db` | **注意**：一期 Task 2 文档中的普惠金融开发库名为 **`wmt_pfb`**（见 `application-pfb-local.yaml`）。若 OceanDB 上沿用 Task 2 规范，应 **在目标实例上创建并使用 `wmt_pfb`**，并在对应环境的 Spring 配置里把 URL 库名改为与之一致；若行方统一使用 `credit_project_db`，则以下命令中 **把库名全部替换为实际库名** 即可。 |
| 用户名 | `credit_user@credit_tenant` | OceanBase 租户用户常见形态：`用户@租户` |
| 密码 | `Credit@123456` | 生产请轮换并走密钥管理 |

应用侧 JDBC 示例（摘自 nmlocal，便于对照）：

```text
jdbc:mysql://172.30.243.122:2881/credit_project_db?useSSL=true&allowPublicKeyRetrieval=true&useUnicode=true&characterEncoding=UTF-8&serverTimezone=Asia/Shanghai&rewriteBatchedStatements=true
```

---

## 2. 与 Task 2 相同的逻辑前提

1. **先建库**（字符集与 Task 2 一致）。  
2. **执行顺序固定**：  
   - 先 **`sql/mysql/ruoyi-vue-pro.sql`**（RuoYi 基线）；  
   - 再 **`docs/nm-deliver/sql/pfb-core-schema-v0.sql`**（`pfb_*` 增量）。  
3. **禁止**颠倒顺序（先 PFB 再基线可能导致基线脚本覆盖/破坏对象）。

脚本在代码仓中的路径（在 **应用服务器** 或 **有 mysql 客户端的跳板机** 上，先 `cd` 到仓库根目录 `ahzx-wmt-svc`）：

- `sql/mysql/ruoyi-vue-pro.sql`  
- `docs/nm-deliver/sql/pfb-core-schema-v0.sql`

---

## 3. 使用标准 `mysql` 客户端连接 OceanDB（MySQL 模式）

OceanDB MySQL 兼容模式下，一般可使用 **MySQL 官方客户端**（`mysql` / 8.x 的 `mysql`）或 OceanBase 提供的 **`obclient`**（若已安装，语法与 mysql 类似）。

### 3.1 环境变量（推荐，避免在 shell 历史里明文密码）

在仓库根目录执行（**库名以 `wmt_pfb` 为例**；若使用 nmlocal 中的 `credit_project_db`，将 `DB_NAME` 改为该名）：

```bash
export OB_MYSQL_HOST=172.30.243.122
export OB_MYSQL_PORT=2881
export OB_MYSQL_USER='credit_user@credit_tenant'
export OB_MYSQL_DB=wmt_pfb
# 密码从 nmlocal 的 master.password 读取；勿将 export 行提交到 git
read -s OB_MYSQL_PWD && export MYSQL_PWD="$OB_MYSQL_PWD"
```

### 3.2 建库

```bash
mysql -h"$OB_MYSQL_HOST" -P"$OB_MYSQL_PORT" -u"$OB_MYSQL_USER" \
  --ssl-mode=REQUIRED \
  -e "CREATE DATABASE IF NOT EXISTS \`${OB_MYSQL_DB}\` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;"
```

若客户端较旧不支持 `--ssl-mode`，可改为 `--ssl` 或按 OceanDB 运维文档关闭 SSL（**仅内网且经安全评估**）。

### 3.3 导入脚本（顺序与 Task 2 一致）

```bash
mysql -h"$OB_MYSQL_HOST" -P"$OB_MYSQL_PORT" -u"$OB_MYSQL_USER" \
  --ssl-mode=REQUIRED \
  "$OB_MYSQL_DB" < sql/mysql/ruoyi-vue-pro.sql

mysql -h"$OB_MYSQL_HOST" -P"$OB_MYSQL_PORT" -u"$OB_MYSQL_USER" \
  --ssl-mode=REQUIRED \
  "$OB_MYSQL_DB" < docs/nm-deliver/sql/pfb-core-schema-v0.sql
```

### 3.4 一行式示例（便于复制；密码仍建议用 `MYSQL_PWD`）

将 `YOUR_DB` 替换为 `wmt_pfb` 或行方指定库名：

```bash
mysql -h172.30.243.122 -P2881 -u'credit_user@credit_tenant' -p'Credit@123456' \
  --ssl-mode=REQUIRED \
  -e "CREATE DATABASE IF NOT EXISTS \`YOUR_DB\` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;"

mysql -h172.30.243.122 -P2881 -u'credit_user@credit_tenant' -p'Credit@123456' \
  --ssl-mode=REQUIRED YOUR_DB < sql/mysql/ruoyi-vue-pro.sql

mysql -h172.30.243.122 -P2881 -u'credit_user@credit_tenant' -p'Credit@123456' \
  --ssl-mode=REQUIRED YOUR_DB < docs/nm-deliver/sql/pfb-core-schema-v0.sql
```

---

## 4. 权限与租户说明（OceanDB 特有）

- 用户 **`credit_user@credit_tenant`** 必须对目标库拥有 **CREATE / DROP / ALTER / INDEX / INSERT** 等执行 DDL+DML 的权限；若仅应用账号无建表权，需由 **DBA 使用高权限账号** 执行两段 SQL，再对应用账号 **GRANT 所需 DML/查询权限**。  
- 若连接串使用 **OBProxy** 或 **VIP**，将 `OB_MYSQL_HOST`/`OB_MYSQL_PORT` 改为运维提供的地址即可，**脚本与顺序不变**。

---

## 5. 验收 SQL（与 Task 2.4 一致）

```sql
USE `wmt_pfb`;   -- 或你的实际库名
SHOW TABLES LIKE 'pfb_%';
SELECT COUNT(*) FROM system_users;
DESC pfb_loan_application;
```

期望出现 `pfb_banner`、`pfb_loan_product`、`pfb_loan_application` 等表，且 `system_users` 可查询。

---

## 6. 安全与文档维护

- 本文档中的 **密码与内网 IP** 来自开发/联调配置样例，**不得**作为生产真值长期固化；更新 `application-nmlocal.yaml` 后，运维应同步修订本文件或改为引用「内部运维 wiki 链接」。  
- 建议在团队内用 **运维 runbook** 或密钥柜保存最终命令，仓库内仅保留 **步骤与顺序**。

---

## 7. 相关索引

| 文档 | 路径 |
|------|------|
| 一期 Task 2（Docker/本机 MySQL） | [`2026-05-11-pfb-phase1-backend-tasks.md`](./2026-05-11-pfb-phase1-backend-tasks.md) |
| PFB v0 DDL | [`../../sql/pfb-core-schema-v0.sql`](../../sql/pfb-core-schema-v0.sql) |
| nmlocal 数据源 | [`../../../../wmt-server/src/main/resources/application-nmlocal.yaml`](../../../../wmt-server/src/main/resources/application-nmlocal.yaml) |
