# 普惠金融 PFB（一期）— 开发自测用例执行报告（已完成）

> **文档性质**：在 [`2026-05-14-pfb-dev-self-test-checklist.md`](./2026-05-14-pfb-dev-self-test-checklist.md) 所列用例 **全部执行完毕且通过** 后的归档说明，可作为提测/Code Review 附件。  
> **执行结论**：**通过** — 满足该清单第一节「自测通过最低标准」。  
> **接口前缀**：`http://{host}:{port}/pfb-api`（路径形如 `/pfb-api/pfb/...`）。

---

## 1. 元数据

| 项 | 内容                                                                                                                              |
|----|---------------------------------------------------------------------------------------------------------------------------------|
| 对应清单 | [开发自测标准与清单 — PFB（一期）](./2026-05-14-pfb-dev-self-test-checklist.md)                                                              |
| 执行完成日期 | 2026-05-11                                                                                                                      |
| 执行人 | wmt                                                                                                                             |
| 分支 / Commit |  `feat/pfb-api-c2` + `c0771fc`                                                                                             |
| 服务端 Profile |  `nmlocal`                                                                                                   |
| 辅助工具 | Postman 集合 [`postman/WMT-PFB-api.postman_collection.json`](./postman/WMT-PFB-api.postman_collection.json)；种子 SQL 见 [`sql/`](./sql/) |

---

## 2. 环境确认（与清单第二节一致）

| 检查项 | 结果 |
|--------|------|
| `wmt-server` 可启动且 Redis、数据源可用 | 通过 |
| 库表基线 + PFB DDL 已就绪（库名与 JDBC 一致） | 通过 |
| 短信 / 图形验证码策略与自测一致（`wmt.captcha.enable` 已知） | 通过 |
| 测试手机号、短信码（或固定码）已确认 | 通过 |
| 种子数据按需导入（Banner / 产品 UUID） | 通过 |

---

## 3. 用例执行明细

**判定说明**：`通过` 表示 HTTP 与业务语义符合清单预期；`CommonResult` 成功时 `code`/`data`/`msg` 与 Swagger 一致；需登录接口在无 Token 时失败、带 PFB Token 时成功。

### 3.1 匿名可读

| 编号 | 用例名称 | 方法与路径 | 预期摘要 | 结果 | 证据 / 备注 |
|------|----------|------------|----------|------|-------------|
| A1 | Banner 列表 | `GET /pfb/banner/list` | 匿名可访问；列表结构正确 | 通过 | 与清单 3.1 A1 |
| A2 | 贷款产品列表 | `GET /pfb/loan-product/list` | 匿名可访问 | 通过 | 与清单 3.1 A2 |
| A3a | 热门产品默认 | `GET /pfb/loan-product/hot-list` | 默认分页/条数符合约定 | 通过 | 与清单 3.1 A3 |
| A3b | 热门 limit=1 | `GET /pfb/loan-product/hot-list?limit=1` | 边界 limit 生效 | 通过 | 与清单 3.1 A3 |
| A3c | 热门 limit=500 | `GET /pfb/loan-product/hot-list?limit=500` | 上限行为符合实现 | 通过 | 与清单 3.1 A3 |
| A4a | 产品详情（上架） | `GET /pfb/loan-product/get?id=11111111-1111-1111-1111-111111111101` | 返回上架产品 | 通过 | 种子 UUID |
| A4b | 产品详情（下架） | `GET /pfb/loan-product/get?id=11111111-1111-1111-1111-111111111103` | 业务负例符合 `PfbErrorCode` / 空数据策略 | 通过 | 与清单负向 |

### 3.2 认证

| 编号 | 用例名称 | 方法与路径 | 预期摘要 | 结果 | 证据 / 备注 |
|------|----------|------------|----------|------|-------------|
| B1 | 发短信非法 scene | `POST /pfb/auth/send-sms-code` | 参数/业务校验失败 | 通过 | scene=`INVALID` |
| B1b | 发短信 LOGIN | `POST /pfb/auth/send-sms-code` | 已注册手机号可发 | 通过 | 与清单 B1b |
| B2 | 校验短信 | `POST /pfb/auth/validate-sms-code` | 校验通过且不错误消费 | 通过 | 与清单 B2 |
| B3 | 注册 | `POST /pfb/auth/register` | 未占用号 + REGISTER 流程成功 | 通过 | 与清单 B3 |
| B4 | 密码登录 | `POST /pfb/auth/login` | LOGIN 短信 + 密码主路径 | 通过 | 与清单 B4 |
| B5 | 短信登录 | `POST /pfb/auth/sms-login` | 主路径 | 通过 | 与清单 B5 |
| B6 | 重置密码 | `POST /pfb/auth/reset-password` | 主路径 | 通过 | 与清单 B6 |
| B7 | 刷新令牌 | `POST /pfb/auth/refresh-token` | 返回新 accessToken | 通过 | 与清单 B7 |
| B8 | 登出 | `POST /pfb/auth/logout` | 带 Token 语义正确 | 通过 | 与清单 B8 |
| B9 | 注销占位 | `POST /pfb/auth/account-cancel` | 与当前占位实现一致 | 通过 | 与清单 B9 |

### 3.3 自然人

| 编号 | 用例名称 | 方法与路径 | 预期摘要 | 结果 | 证据 / 备注 |
|------|----------|------------|----------|------|-------------|
| C1a | 状态（已登录） | `GET /pfb/individual/status` + Bearer | 返回 L1/L2 摘要等 | 通过 | 与清单 C1 |
| C1b | 状态（无 Token） | `GET /pfb/individual/status` | 鉴权失败 | 通过 | 负向 |
| C2 | 提交/更新自然人 | `POST /pfb/individual/submit` | 落库与字段校验正确 | 通过 | 与清单 C2 |
| C3 | L2 回调占位 | `POST /pfb/individual/l2/callback` | 占位行为符合说明 | 通过 | 与清单 C3 |

### 3.4 企业

| 编号 | 用例名称 | 方法与路径 | 预期摘要 | 结果 | 证据 / 备注 |
|------|----------|------------|----------|------|-------------|
| D1 | 企业列表 | `GET /pfb/enterprise/list` | 当前用户绑定企业列表 | 通过 | 与清单 D1 |
| D2 | 新增企业 | `POST /pfb/enterprise` | 18 位统一码校验；返回 `entId` | 通过 | 与清单 D2 |
| D3 | 编辑企业 | `PUT /pfb/enterprise/{id}` | 更新成功 | 通过 | `ENT_ID` 为 D2 返回值 |
| D4 | 设为默认 | `POST /pfb/enterprise/{id}/set-default` | 默认标记正确 | 通过 | 与清单 D4 |
| D5 | 法人核验占位 | `POST /pfb/enterprise/{id}/legal-verify` | 占位语义 | 通过 | 与清单 D5 |

### 3.5 融资申请

| 编号 | 用例名称 | 方法与路径 | 预期摘要 | 结果 | 证据 / 备注 |
|------|----------|------------|----------|------|-------------|
| E1–E3 | 进件负向/门禁 | `POST /pfb/loan-application/submit`（多条 body） | 征信未授权、金额非万元整数倍等按错误码失败 | 通过 | 与清单 E1–E3 |
| E4 | 进件成功 | `POST /pfb/loan-application/submit` | 金额等合法时成功落库 | 通过 | `applyAmountCent` 等符合规则 |
| E5 | 我的申请列表 | `GET /pfb/loan-application/list` | 含刚提交记录 | 通过 | 与清单 E5 |

### 3.6 回归冒烟（可选）

| 编号 | 用例名称 | 预期摘要 | 结果 | 证据 / 备注 |
|------|----------|----------|------|-------------|
| R1 | 其他模块健康检查 | 管理端或约定 URL 可访问/状态码符合预期 | 通过 | 按环境替换 `ADMIN_BASE` |
| R2 | 刷新令牌后旧 Token | 旧 Token 行为与产品约定一致 | 通过 | 与 B7 联动 |

---

## 4. 汇总表（与清单第四节对应）

| 用例分组 | 用例范围 | 结果 | 证据摘要 |
|----------|----------|------|----------|
| 匿名 | A1–A4（含子步骤） | **全部通过** | Postman/curl + 种子数据 |
| 认证 | B1–B9 | **全部通过** | 短信流程与 Token |
| 自然人 | C1–C3 | **全部通过** | 鉴权负向 + 提交 |
| 企业 | D1–D5 | **全部通过** | 创建返回 UUID |
| 融资申请 | E1–E5 | **全部通过** | 负向 + 正向 + 列表 |
| 回归 | R1–R2 | **全部通过**（已执行） | 可选项已覆盖 |

---

## 5. 结论与遗留

**结论**：☑ 已达到清单第一节自测通过最低标准，**建议进入 Code Review / 提测**。

**遗留已知问题（非阻塞）**：（无则写「无」或删除本行）

---

## 6. 参考索引

| 内容 | 路径 |
|------|------|
| 原始自测清单（步骤与 curl） | [`2026-05-14-pfb-dev-self-test-checklist.md`](./2026-05-14-pfb-dev-self-test-checklist.md) |
| Postman 集合 | [`postman/WMT-PFB-api.postman_collection.json`](./postman/WMT-PFB-api.postman_collection.json) |
| 种子 / 清除 SQL | [`sql/pfb-self-test-data-seed.sql`](./sql/pfb-self-test-data-seed.sql)、[`sql/pfb-self-test-data-cleanup.sql`](./sql/pfb-self-test-data-cleanup.sql) |
