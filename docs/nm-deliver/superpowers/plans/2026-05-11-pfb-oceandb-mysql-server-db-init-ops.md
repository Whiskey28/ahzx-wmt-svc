# PFB 一期数据库初始化 — OceanBase（MySQL 兼容模式）操作手册（含租户隔离说明）

> **与 Task 2 对齐**：脚本顺序与 [`2026-05-11-pfb-phase1-backend-tasks.md`](./2026-05-11-pfb-phase1-backend-tasks.md) 一致。  
> **凭据**：业务侧示例见 [`application-nmlocal.yaml`](../../../../wmt-server/src/main/resources/application-nmlocal.yaml)。本文 **不写任何口令**；一律 `-p` 交互或密钥柜。

---

## 0. 必须先读：OceanBase 的「租户隔离」与为什么你会「授权了却看不到库」

OceanBase 是 **多租户** 架构，与单机 MySQL 不同：

| 概念 | 说明 |
|------|------|
| **系统租户 `sys`** | 账号形态常为 **`root@sys`**，持有集群/系统租户视角的管理能力，适合 **运维级建库、导数、排障**。 |
| **业务租户**（如 `credit_tenant`） | 业务用户常为 **`业务用户@租户名`**（JDBC 里常写成 `credit_user@credit_tenant`）。**元数据（有哪些 database）按租户隔离**：在某个租户会话里创建的 `wmt_pfb`，**不一定会出现在另一租户或错误连接方式下的 `SHOW DATABASES` 里**。 |
| **现网联调结论** | 多次尝试后：**用带租户的普通业务账号做大脚本或排障，容易被租户边界挡住**；**建库与导入最终以 `root@sys` + 系统管理员密码** 执行才稳定；业务账号能否 `USE wmt_pfb` / `SHOW TABLES`，取决于 **库是否建在业务租户可见的空间内** + **`GRANT` 是否在正确租户上下文中执行** + **OBProxy/直连是否把连接路由到该租户**（具体以行方 DBA 说明为准）。 |

**Spring / JDBC 用户名写 `credit_user@credit_tenant` 还是只写 `credit_user`？**  
取决于 **OBProxy 配置**：有的环境 Proxy 已绑定租户，客户端只传 `credit_user`；有的必须传 `用户@租户`。**以能 `SHOW DATABASES` 列出 `wmt_pfb` 且能 `USE wmt_pfb` 的实测为准**，再固化到 `application-*.yaml`。

---

## 1. 连接与脚本路径（占位）

| 项 | 现网联调示例 | 说明 |
|----|-------------|------|
| 主机 / 端口 | `172.30.243.122` / `2881` | 以实际 OBProxy 或直连为准 |
| 普惠金融库名 | **`wmt_pfb`** | 与一期 Task 2、`application-pfb-local.yaml` 一致；应用 JDBC 库名需与此一致 |
| 运维账号 | **`root@sys`** | 建库 + 导入 SQL + 跨租户排障 |
| 业务账号 | **`credit_user`**（及 host，如 `@'%'`） | 应用与 `GRANT` 对象；是否与 `@credit_tenant` 同现于命令行以实测为准 |

脚本（在仓库根 `ahzx-wmt-svc`）：

1. `sql/mysql/ruoyi-vue-pro.sql` — 基线，**必须先执行**  
2. `docs/nm-deliver/sql/pfb-core-schema-v0.sql`（与 `sql/mysql/pfb-core-schema-v0.sql` 保持同源）— PFB 增量，**第二执行**

上传到执行机（示例 `/tmp`）：

```bash
scp sql/mysql/ruoyi-vue-pro.sql user@jump:/tmp/ruoyi-vue-pro.sql
scp docs/nm-deliver/sql/pfb-core-schema-v0.sql user@jump:/tmp/pfb-core-schema-v0.sql
```

---

## 2. 推荐主流程：仅用 `root@sys` 建库 + 导入（与 2026-05-14 现网一致）

**不要用业务账号执行大脚本**（权限、字符集、租户可见性均易踩坑）。

### 2.1 建库

```bash
mysql -h 172.30.243.122 -P 2881 -uroot@sys -p \
  -e "CREATE DATABASE IF NOT EXISTS \`wmt_pfb\` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;"
```

### 2.2 导入基线（指定客户端字符集，避免乱码）

```bash
mysql -h 172.30.243.122 -P 2881 -uroot@sys -p \
  --default-character-set=utf8mb4 \
  wmt_pfb < /tmp/ruoyi-vue-pro.sql
```

### 2.3 导入 PFB DDL

```bash
mysql -h 172.30.243.122 -P 2881 -uroot@sys -p \
  --default-character-set=utf8mb4 \
  wmt_pfb < /tmp/pfb-core-schema-v0.sql
```

未拷贝到 `/tmp` 时，将路径改为仓库内相对路径即可。

---

## 3. 对业务用户授权 `wmt_pfb`（GRANT）

在 **`root@sys`**（或行方指定的、能对目标库 `GRANT` 的管理会话）中执行；**`TO` 必须与 `CREATE USER` 时的用户名、host 完全一致**（常见为 `'credit_user'@'%'`）。

```sql
CREATE DATABASE IF NOT EXISTS `wmt_pfb`
  DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

GRANT ALL PRIVILEGES ON `wmt_pfb`.* TO 'credit_user'@'%';
FLUSH PRIVILEGES;

SHOW GRANTS FOR 'credit_user'@'%';
```

> 生产可按最小权限收紧 `ALL PRIVILEGES`，仅保留应用所需 DML/DDL。

---

## 4. 业务账号验证（两种用户名格式都试）

**现象**：`GRANT` 已成功，但 **`credit_user@credit_tenant` 下 `SHOW DATABASES` 没有 `wmt_pfb`** 或 `Unknown database` — 多为 **租户隔离 / 连接未进到存放该库的租户**。

依次验证（每次 `-p` 输入 **业务用户** 口令，口令来源以 nmlocal / 密钥柜为准，**勿写进仓库**）：

```bash
# 方式 A：无租户后缀（适用于 OBProxy 已绑定租户、或行方要求该写法）
mysql -h 172.30.243.122 -P 2881 -u credit_user -p \
  -e "SELECT USER(), CURRENT_USER(); SHOW DATABASES; USE \`wmt_pfb\`; SHOW TABLES LIKE 'pfb_%';"

# 方式 B：显式租户后缀（适用于直连租户、或 JDBC 同形态）
mysql -h 172.30.243.122 -P 2881 -u'credit_user@credit_tenant' -p \
  -e "SELECT USER(), CURRENT_USER(); SHOW DATABASES; USE \`wmt_pfb\`; SHOW TABLES LIKE 'pfb_%';"
```

**哪一种能列出 `wmt_pfb` 并能 `USE`**，Spring 里 `username` 就应与之一致（是否带 `@credit_tenant` 以该结果为准）。

---

## 5. 租户隔离排障清单（`root@sys` 侧）

以下均在 **`root@sys` + 管理员密码** 下执行（`-p` 交互）。

### 5.1 库是否存在（全局先看一眼）

```bash
mysql -h 172.30.243.122 -P 2881 -uroot@sys -p -e "SHOW DATABASES LIKE 'wmt%';"
```

### 5.2 库结构

```bash
mysql -h 172.30.243.122 -P 2881 -uroot@sys -p -e "SHOW CREATE DATABASE wmt_pfb;"
```

### 5.3 业务用户权限

```bash
mysql -h 172.30.243.122 -P 2881 -uroot@sys -p -e "SHOW GRANTS FOR 'credit_user'@'%';"
```

### 5.4 租户列表（版本/权限差异大，以 OceanBase 官方文档为准）

若行方开放系统视图，可在 **`root@sys`** 下由 DBA 执行类似查询，确认 **`wmt_pfb` 所在租户** 与业务连接是否一致（具体视图名、列名以当前集群版本为准）：

```sql
-- 示例：需高权限；若报错则改用语义等价的 DBA 视图或 OCP 控制台
SELECT * FROM oceanbase.DBA_OB_TENANTS;
```

**结论动作**：若库建在 **系统租户** 而业务连接在 **`credit_tenant`**，则业务侧永远「看不到」——需要 DBA 在 **正确租户内** 建库/迁移，或调整应用接入租户；**这不是单纯 `GRANT` 能解决的**。

---

## 6. 一键排障脚本模板（复制后自行改主机/用户）

**注意**：脚本内多次 `-p`，执行时会多次提示输入密码；可改为运维私有 runbook 使用 `MYSQL_PWD`（仍勿提交到 Git）。

```bash
#!/usr/bin/env bash
set -euo pipefail
H=172.30.243.122
P=2881

echo "=== 1. root@sys 身份与库 ==="
mysql -h"$H" -P"$P" -uroot@sys -p -e "SELECT USER(), DATABASE(); SHOW DATABASES LIKE 'wmt%';"

echo "=== 2. wmt_pfb 定义 ==="
mysql -h"$H" -P"$P" -uroot@sys -p -e "SHOW CREATE DATABASE wmt_pfb;" || true

echo "=== 3. credit_user 授权 ==="
mysql -h"$H" -P"$P" -uroot@sys -p -e "SHOW GRANTS FOR 'credit_user'@'%';" || true

echo "=== 4. 业务用户视角（无 @ 租户）==="
mysql -h"$H" -P"$P" -u credit_user -p -e "SELECT USER(), CURRENT_USER(); SHOW DATABASES;" || true

echo "=== 5. 业务用户视角（带 @ 租户，若适用）==="
mysql -h"$H" -P"$P" -u'credit_user@credit_tenant' -p -e "SELECT USER(), CURRENT_USER(); SHOW DATABASES;" || true
```

---

## 7. 若业务账号短期仍无法导数：维持 `root@sys` 运维导入

业务权限问题未理清前，**数据初始化一律用 §2 的 `root@sys`**；应用只读/读写权限理清后再切回业务账号（并收紧 `GRANT`）。

可在授权后再次用 `root@sys` 核对：

```bash
mysql -h 172.30.243.122 -P 2881 -uroot@sys -p \
  -e "GRANT ALL PRIVILEGES ON wmt_pfb.* TO 'credit_user'@'%'; FLUSH PRIVILEGES;"
```

---

## 8. 验收（Task 2.4）

```sql
USE `wmt_pfb`;
SHOW TABLES LIKE 'pfb_%';
SELECT COUNT(*) FROM system_users;
DESC pfb_loan_application;
```

---

## 9. 安全

- 凡在即时通讯中出现过的 **root / 业务口令**，均应按 **泄露** 处理：**轮换** 并更新密钥柜与 `application-nmlocal.yaml`（勿将新口令写入本仓库）。  
- 本文件仅保留 **命令骨架与租户隔离说明**。

---

## 10. 相关索引

| 文档 | 路径 |
|------|------|
| 一期 Task 2 | [`2026-05-11-pfb-phase1-backend-tasks.md`](./2026-05-11-pfb-phase1-backend-tasks.md) |
| PFB v0 DDL | [`../../sql/pfb-core-schema-v0.sql`](../../sql/pfb-core-schema-v0.sql) |
| nmlocal | [`../../../../wmt-server/src/main/resources/application-nmlocal.yaml`](../../../../wmt-server/src/main/resources/application-nmlocal.yaml) |
