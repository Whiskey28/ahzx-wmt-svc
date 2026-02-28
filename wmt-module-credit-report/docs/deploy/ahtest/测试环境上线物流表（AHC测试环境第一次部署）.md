# AHC 测试环境第一次部署 - 上线物流表

> 目标：列清楚“从外网带什么进来、每台内网机器需要什么、部署后要检查什么”，避免上线当天缺物料。

---

## 一、外网阶段：需要准备并带出的物料

### 1. 镜像与部署包

- **镜像打包文件**
  - `ahc-images/ahc-images.tar`
    - 包含：
      - `ahzx-wmt-svc:1.0.0`
- **部署目录打包物**
  - `ahc-deploy/`（被整体打进 `ahc-deploy-bundle.tgz`）：
    - `docker-compose.ahtest.yml`（仅后端服务容器）
    - `nginx.conf`（Nginx 配置：7080 + `/CreditServiceReport/` + `/admin-api/` 反代）
    - `CreditServiceReport/`（前端打包产物）
- **最终外发包**
  - `ahc-deploy-bundle.tgz`（包含上述镜像与部署目录，需拷贝到 U 盘）

### 2. 代码与脚本

- **后端源码（可选，方便排查）**
  - 仓库：`ahzx-wmt-svc`（至少包含 `wmt-server` 模块）
- **部署脚本（已在仓库内，可打印/查看）**
  - `wmt-module-credit-report/docs/deploy/prepare_external.sh`
  - `wmt-module-credit-report/docs/deploy/deploy_db_internal.sh`
  - `wmt-module-credit-report/docs/deploy/deploy_app_internal.sh`
  - `wmt-module-credit-report/docs/deploy/deploy_front_internal.sh`

### 3. 初始化 SQL

- 建议在 U 盘单独建目录：`sql-init/`，包含：
  - `sql/postgresql/credit-report-2026-01-29.sql`
  - `sql/postgresql/credit-report-role-insert.sql`
  - 其他本次测试需要的初始化/字典/权限 SQL

---

## 二、内网 DB + Redis 服务器（DB 机）需要的物料与检查点

> 本次测试环境已存在 DB + Redis，不需要本项目提供任何 Redis 镜像/配置文件，也不使用 docker-compose 管理 DB/Redis。
>
> DB+Redis 地址：`192.168.18.112`

### 2.1 DB 机：需要拷入的物料

- `sql-init/` 目录（所有初始化 SQL）

### 2.2 DB 机：本机前置条件

- OS：CentOS 7
- 软件：
  - Docker 26.1.4（或兼容）
  - Docker Compose v2.18.0（或兼容）

### 2.3 DB 机：上线当天检查清单

- **网络连通**
  - 后端服务器（`192.168.18.111`）能 `ping` 到 `192.168.18.112`
  - 后端服务器能 `telnet 192.168.18.112 5432`、`telnet 192.168.18.112 6379`
- **数据库初始化**
  - 数据库：`tdc-credit` 已存在
  - 用户：`ahzx/ahzx123456` 可登录
  - 关键业务表与字典：按 `sql-init/` 中 SQL 验证（例如角色、报表表结构等）

---

## 三、内网后端服务器（应用机）需要的物料与检查点

> 后端服务器 IP：`192.168.18.111`

### 3.1 应用机：需要拷入的物料

- `ahc-deploy-bundle.tgz`（从 U 盘拷到 `/opt/ahc/`）

### 3.2 应用机：本机前置条件

- OS：CentOS 7
- 软件：
  - Docker / Docker Compose
- 磁盘目录：
  - `/data/ahc/logs`（挂载应用日志）

### 3.3 应用机：部署步骤对应的物料使用

- 使用 `deploy_app_internal.sh`（或手工执行等价命令）：
  - 解压 `ahc-deploy-bundle.tgz`
  - `docker load` 导入镜像
  - 创建 `/data/ahc/logs`
  - 使用 `docker-compose.ahtest.yml` 启动：
    - 容器 `ahzx-wmt-svc`
  - 容器通过 `ARGS` 指定 profile：
    - 后续你会提供：`application-ahtest` 专属配置（建议用 `--spring.profiles.active=ahtest`）
  - DB/Redis 地址固定为：`192.168.18.112`

### 3.4 应用机：上线当天检查清单

- **容器状态**
  - `ahzx-wmt-svc`：状态 `Up`，端口映射 `48080->48080`
- **网络连通**
  - 能 `ping` 到 `192.168.18.112`
  - 能 `telnet 192.168.18.112 5432`、`telnet 192.168.18.112 6379`
- **健康检查**
  - `curl http://127.0.0.1:48080/actuator/health` 返回 `UP`（或等价状态）
- **日志**
  - `/data/ahc/logs/` 目录下有最新日志文件，且无明显严重错误（连续报错、连不上 DB 等）

---

## 四、内网 Nginx + 前端服务器（Web 机）需要的物料与检查点

> Nginx 服务器 IP：`192.168.20.65`

### 4.1 Web 机：需要拷入的物料

- `ahc-deploy-bundle.tgz`（从 U 盘拷到 `/opt/ahc/`）

### 4.2 Web 机：本机前置条件

- 已安装 Nginx（二进制）
- 目录：
  - `/opt/nginx/html/CreditServiceReport/`（静态资源目录）
  - `/etc/nginx/conf.d/`（虚拟主机配置目录，或对应自定义路径）

### 4.3 Web 机：部署步骤对应的物料使用

- 使用 `deploy_front_internal.sh`：
  - 解压 `ahc-deploy-bundle.tgz`
  - 将 `CreditServiceReport/` 拷贝到 `/opt/nginx/html/CreditServiceReport/`
  - 将 `nginx.conf` 拷贝为 `/etc/nginx/conf.d/credit-report.conf`（或合入现有 Nginx 配置）
  - 确认反代后端地址为：`192.168.18.111:48080`
  - `nginx -t && nginx -s reload`

### 4.4 Web 机：上线当天检查清单

- **目录 & 文件**
  - `/opt/nginx/html/CreditServiceReport/` 下存在 `index.html` 等前端文件
  - `/etc/nginx/conf.d/credit-report.conf` 已生效
- **Nginx 状态**
  - 进程存在，`nginx -t` 无报错
  - `netstat -tnlp | grep 7080` 能看到监听端口
- **访问验证**
  - 浏览器访问：`http://192.168.20.65:7080/CreditServiceReport/` 页面能正常打开
  - 浏览器 Network 面板：
    - 静态文件（js/css）状态码 200
    - 接口请求 `http://192.168.20.65:7080/admin-api/...` 状态码 200/正常业务响应（不是 502/404）

---

## 五、账号与文档类物料

### 5.1 账号信息清单

- **Postgres**
  - IP：`192.168.18.112`
  - 端口：`5432`
  - 数据库：`tdc-credit`
  - 用户：`ahzx`
  - 密码：`ahzx123456`
- **Redis**
  - IP：`192.168.18.112`
  - 端口：`6379`
  - DB：`3`
  - 密码：`Ab123456`
- **应用登录账号**
  - 例如 `sql/postgresql/credit-report-role-insert.sql` 中导入的：
    - 用户 1001–1004 及其角色映射（实际登录用户名/初始密码需在表中确认）

### 5.2 文档与参考资料

- 部署脚本说明：
  - `wmt-module-credit-report/docs/deploy/prepare_external.sh`
  - `wmt-module-credit-report/docs/deploy/deploy_db_internal.sh`
  - `wmt-module-credit-report/docs/deploy/deploy_app_internal.sh`
  - `wmt-module-credit-report/docs/deploy/deploy_front_internal.sh`
- 业务相关文档：
  - `wmt-module-credit-report/docs/操作手册-征信报送模块（面向用户）.md`
  - `wmt-module-credit-report/docs/测试规划-征信报送模块（credit-report）.md`

---

## 六、上线前最后自查（Checklist 简版）

- **外网 U 盘内容**
  - `ahc-deploy-bundle.tgz`
  - `sql-init/`（所有需要执行的 SQL）
  - 上述脚本与账号/文档清单
- **DB 机**
  - `192.168.18.112` 上 DB/Redis 正常可用（端口可连通）
  - DB 初始化完成，`tdc-credit` 可用（手工执行 SQL）
- **应用机**
  - `ahzx-wmt-svc` 容器 `Up`，端口 48080 打开
  - 能连上 DB/Redis，健康检查正常
- **Web 机**
  - Nginx 监听 7080
  - `/CreditServiceReport/` 页面可访问
  - `/admin-api/`** 接口返回正常业务响应

