## CentOS 7 内网离线安装 Docker 26.1.4 + docker-compose v2.18.0

> 适用：无外网的内网机器（CentOS 7）。  
> 方式：在外网机准备安装包（U 盘带入），内网机离线安装。

---

### 一、外网准备：需要带入内网的物料清单（放到 U 盘）

#### 1）Docker Engine 26.1.4（建议使用官方静态包）

- `docker-26.1.4.tgz`（官方 Linux x86_64 静态发行包）

#### 2）docker-compose v2.18.0（二选一）

- **推荐（插件方式）**：`docker-compose-linux-x86_64`（v2.18.0）
- 或 **备用（单文件）**：同名二进制也可直接放到 `/usr/local/bin/docker-compose`

#### 3）辅助工具（可选但建议）

- `jq`（排查 json 输出更方便，可选）
- `telnet` / `nc`（联通性排查用）

> 注：如果你们机器不是 x86_64（极少见），需要换对应架构的包。

---

### 二、内网安装 Docker（静态包方式）

> 以下步骤在每台需要运行 Docker 的机器上执行（例如后端机 `192.168.18.111`）。

#### 1）拷贝安装包到服务器

示例路径：

- `/opt/offline/docker-26.1.4.tgz`
- `/opt/offline/docker-compose-linux-x86_64`（v2.18.0）

#### 2）安装 Docker 二进制

```bash
mkdir -p /opt/offline
cd /opt/offline

# 解压 docker 静态包（示例文件名以你实际为准）
tar -xzf docker-26.1.4.tgz

# 将 docker/* 拷贝到 /usr/bin
cp -f docker/* /usr/bin/
chmod +x /usr/bin/docker /usr/bin/dockerd
```

#### 3）创建 systemd 服务并设置开机自启

创建 `/etc/systemd/system/docker.service`：

```bash
cat > /etc/systemd/system/docker.service <<'EOF'
[Unit]
Description=Docker Application Container Engine
Documentation=https://docs.docker.com
After=network-online.target
Wants=network-online.target

[Service]
Type=notify
ExecStart=/usr/bin/dockerd
ExecReload=/bin/kill -s HUP $MAINPID
LimitNOFILE=infinity
LimitNPROC=infinity
TimeoutStartSec=0
Delegate=yes
KillMode=process
Restart=always
RestartSec=2

[Install]
WantedBy=multi-user.target
EOF
```

启动并验证：

```bash
systemctl daemon-reload
systemctl enable docker
systemctl start docker

docker version
docker info
```

---

### 三、内网安装 docker-compose v2.18.0

#### 方案 A（推荐）：安装为 Docker 插件（`docker compose`）

```bash
mkdir -p /usr/libexec/docker/cli-plugins
cp -f /opt/offline/docker-compose-linux-x86_64 /usr/libexec/docker/cli-plugins/docker-compose
chmod +x /usr/libexec/docker/cli-plugins/docker-compose

docker compose version
```

#### 方案 B（备用）：安装为独立命令（`docker-compose`）

```bash
cp -f /opt/offline/docker-compose-linux-x86_64 /usr/local/bin/docker-compose
chmod +x /usr/local/bin/docker-compose

docker-compose version
```

---

### 四、常见问题排查

#### 1）启动 docker 失败

```bash
systemctl status docker -l
journalctl -u docker --no-pager | tail -200
```

#### 2）提示缺少内核能力 / cgroup 相关

CentOS7 建议保持默认内核能力；若遇到 cgroup/overlay2 兼容问题，通常需要运维配合调整内核/存储驱动。

#### 3）确认版本

```bash
docker version
docker compose version
```

