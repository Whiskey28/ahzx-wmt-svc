# PFB 一期数据库初始化 — 服务器直装 OceanBase（MySQL 兼容模式）操作说明

> **与 Task 2 对齐**：执行顺序、脚本路径与 [`2026-05-11-pfb-phase1-backend-tasks.md`](./2026-05-11-pfb-phase1-backend-tasks.md) 中「Task 2 — 数据库初始化」一致；本文件仅补充 **在已安装 OceanBase / OceanDB 且开启 MySQL 兼容协议** 场景下的连接方式与命令示例。  
> **凭据来源**：仓库内 [`wmt-server/src/main/resources/application-nmlocal.yaml`](../../../../wmt-server/src/main/resources/application-nmlocal.yaml) 中 `spring.datasource.dynamic.datasource.master`（及 `slave` 同构项）。以下 **主机、端口、用户名** 为该文件当前示例值；**所有口令一律用 `-p` 交互输入或运维密钥系统**，**禁止**将 `root@sys` 或应用账号密码写入本仓库或即时通讯长期留存。

---

## 1. 从 `application-nmlocal.yaml` 读取的示例连接参数

| 项 | 当前仓库示例值（`master`） | 说明 |
|----|---------------------------|------|
| JDBC 主机 | `172.30.243.122` | 替换为你的 OceanDB 对外 MySQL 监听地址 |
| JDBC 端口 | `2881` | OceanBase 集群 MySQL 模式常用监听端口之一（以实际 `obproxy` / 直连配置为准） |
| JDBC URL 中的库名 | `credit_project_db` | **注意**：一期 Task 2 普惠金融库名为 **`wmt_pfb`**（见 `application-pfb-local.yaml`）。现网联调可在 OceanDB 上 **单独建 `wmt_pfb`** 专用于 PFB；应用连接该库时请将对应 profile 的 JDBC URL **库名改为 `wmt_pfb`**。若行方坚持统一库名，则把下文所有 `wmt_pfb` 替换为实际库名。 |
| 应用用户名 | `credit_user@credit_tenant` | OceanBase 租户用户常见形态：`用户@租户`；密码见 nmlocal，勿入库。 |

应用侧 JDBC 示例（摘自 nmlocal，便于对照；**库名需与实际上线库一致**）：

```text
jdbc:mysql://172.30.243.122:2881/credit_project_db?useSSL=true&allowPublicKeyRetrieval=true&useUnicode=true&characterEncoding=UTF-8&serverTimezone=Asia/Shanghai&rewriteBatchedStatements=true
```

---

## 2. 与 Task 2 相同的逻辑前提

1. **先建库**（字符集与 Task 2 一致）。  
2. **执行顺序固定**：  
   - 先 **`sql/mysql/ruoyi-vue-pro.sql`**（RuoYi 基线）；  
   - 再 **`docs/nm-deliver/sql/pfb-core-schema-v0.sql`**（或仓库副本 **`sql/mysql/pfb-core-schema-v0.sql`**，内容应对齐交付 DDL）（`pfb_*` 增量）。  
3. **禁止**颠倒顺序（先 PFB 再基线可能导致基线脚本覆盖/破坏对象）。

脚本在代码仓中的路径（在 **应用服务器** 或 **有 mysql 客户端的跳板机** 上，先 `cd` 到仓库根目录 `ahzx-wmt-svc`）：

- `sql/mysql/ruoyi-vue-pro.sql`  
- `docs/nm-deliver/sql/pfb-core-schema-v0.sql`（与 `sql/mysql/pfb-core-schema-v0.sql` 建议保持同源）

**上传到服务器 `/tmp`（与现网联调习惯一致）**：

```bash
scp sql/mysql/ruoyi-vue-pro.sql user@your-jump:/tmp/ruoyi-vue-pro.sql
scp docs/nm-deliver/sql/pfb-core-schema-v0.sql user@your-jump:/tmp/pfb-core-schema-v0.sql
# 再在目标机将文件放到执行 mysql 的那台机器的 /tmp/
```

---

## 3. 现网联调实录：`root@sys` 建库 + `/tmp` 导入（高权限）

以下与 **2026-05-14 现网执行方式** 一致：**使用系统租户管理员 `root@sys`** 完成建库与两次导入；`-p` 后 **回车再输入口令**，口令不落盘、不写进命令行历史（避免 `mysql -p密码` 形式）。

### 3.1 建库 `wmt_pfb`

```bash
mysql -h 172.30.243.122 -P 2881 -uroot@sys \
  -p \
  -e "CREATE DATABASE IF NOT EXISTS \`wmt_pfb\` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;"
```

### 3.2 导入 RuoYi 基线

```bash
mysql -h 172.30.243.122 -P 2881 -uroot@sys -p \
  wmt_pfb < /tmp/ruoyi-vue-pro.sql
```

### 3.3 导入 PFB v0 增量 DDL

```bash
mysql -h 172.30.243.122 -P 2881 -uroot@sys -p \
  wmt_pfb < /tmp/pfb-core-schema-v0.sql
```

**说明**：若直接在仓库根目录执行且未拷贝到 `/tmp`，可将 `< /tmp/...` 改为相对路径，例如 `< sql/mysql/ruoyi-vue-pro.sql`。

---

## 4. 应用用户对 `wmt_pfb` 的授权（GRANT）

**背景**：使用 `CREATE USER`（或控制台）创建的 **应用租户用户**（如 JDBC 使用的 **`credit_user@credit_tenant`**）通常 **不会自动获得新建库 `wmt_pfb` 的权限**；若仅把 `credit_project_db` 授过权，连接 `wmt_pfb` 会出现 **Access denied / 无权限使用该库**。需在 **具备管理权限的账号**（如 `root@sys`，或租户内管理员）下对 **`wmt_pfb` 整库** 补授权。

### 4.1 在 `mysql` 会话中执行（示例，按你们 OceanBase 版本与租户模型微调）

以 **`root@sys`** 登录后（或进入承载 `wmt_pfb` 的租户会话后），对应用库授权给 **租户内的业务用户 `credit_user`**（主机名常用 `%`）：

```sql
-- 库级授权（应用日常读写 + 结构变更视策略收紧）
GRANT ALL PRIVILEGES ON `wmt_pfb`.* TO `credit_user`@`%`;
-- 若 OceanBase 要求显式列出权限，可改为 SELECT,INSERT,UPDATE,DELETE,CREATE,DROP,ALTER,INDEX 等按需组合

-- MySQL 兼容习惯（OceanBase 部分版本可省略）
FLUSH PRIVILEGES;
```

若用户创建时带 **其他 host**（如 `'credit_user'@'172.%'`），请将 `TO` 子句与创建用户时 **完全一致**。

### 4.2 验证应用账号能否进库

使用 **与 Spring 相同的用户**（不要用 `root@sys`）：

```bash
mysql -h 172.30.243.122 -P 2881 -u'credit_user@credit_tenant' -p \
  -e "USE \`wmt_pfb\`; SHOW TABLES LIKE 'pfb_%';"
```

能列出 `pfb_*` 表即表示 **库级授权已生效**；若仍失败，请 DBA 核对：用户是否属于 **存有 `wmt_pfb` 的同一租户**、OBProxy 是否指向正确租户、以及是否还需 **全局级** 权限（少见）。

---

## 5. 使用应用账号自行建库/导入（仅当该账号已有足够 DDL 权限时）

若 **`credit_user@credit_tenant`** 已被授予可建库、可执行大脚本的全集权限（一般 **不推荐**），则可与 Task 2 一样用应用账号执行第 2 节顺序；否则请 **始终用 3 节 `root@sys` 完成 DDL 导入，再用 4 节 GRANT 放开应用访问**。

---

## 6. 使用标准 `mysql` 客户端（环境变量方式，应用账号）

在仓库根目录执行（**库名 `wmt_pfb`**）：

```bash
export OB_MYSQL_HOST=172.30.243.122
export OB_MYSQL_PORT=2881
export OB_MYSQL_USER='credit_user@credit_tenant'
export OB_MYSQL_DB=wmt_pfb
read -s OB_MYSQL_PWD && export MYSQL_PWD="$OB_MYSQL_PWD"
```

导入（**仅当该用户已有权限**）：

```bash
mysql -h"$OB_MYSQL_HOST" -P"$OB_MYSQL_PORT" -u"$OB_MYSQL_USER" \
  "$OB_MYSQL_DB" < sql/mysql/ruoyi-vue-pro.sql

mysql -h"$OB_MYSQL_HOST" -P"$OB_MYSQL_PORT" -u"$OB_MYSQL_USER" \
  "$OB_MYSQL_DB" < docs/nm-deliver/sql/pfb-core-schema-v0.sql
```

（是否加 `--ssl-mode=REQUIRED` 以行方 TLS 策略为准。）

---

## 7. 验收 SQL（与 Task 2.4 一致）

```sql
USE `wmt_pfb`;
SHOW TABLES LIKE 'pfb_%';
SELECT COUNT(*) FROM system_users;
DESC pfb_loan_application;
```

---

## 8. 安全与文档维护

- **任何** `root@sys`、应用账号密码若曾在聊天、邮件中明文出现，应视为 **已泄露**，请尽快在 OceanBase **轮换口令** 并更新 `application-nmlocal.yaml` 等配置（勿将新口令回写进本 Git 文档）。  
- 仓库内只保留 **步骤、顺序与 GRANT 模板**；真实口令仅存密钥柜或 CI 秘密变量。

---

## 9. 相关索引

| 文档 | 路径 |
|------|------|
| 一期 Task 2（Docker/本机 MySQL） | [`2026-05-11-pfb-phase1-backend-tasks.md`](./2026-05-11-pfb-phase1-backend-tasks.md) |
| PFB v0 DDL | [`../../sql/pfb-core-schema-v0.sql`](../../sql/pfb-core-schema-v0.sql) |
| nmlocal 数据源 | [`../../../../wmt-server/src/main/resources/application-nmlocal.yaml`](../../../../wmt-server/src/main/resources/application-nmlocal.yaml) |
