# 开发自测标准与清单 — 普惠金融 PFB（一期）

> **适用阶段**：开发完成后的 **自测**（非正式测试团队用例集）。  
> **目标**：在合并/提测前，用可重复步骤验证本迭代接口 **可用、可回归、可说明**，并留下最小 **自测证据**。  
> **接口前缀**：`http://{host}:{port}/pfb-api`（路径形如 `/pfb-api/pfb/...`）。  
> **真源**：Controller 见 `wmt-module-pfb`；匿名白名单见 `wmt-server` 下 `application-pfb-local.yaml` / `application-nmlocal.yaml` 的 `wmt.security.permit-all_urls`。  
> **Postman**：可导入 [`postman/WMT-PFB-api.postman_collection.json`](./postman/WMT-PFB-api.postman_collection.json)，说明见 [`postman/README.md`](./postman/README.md)。

---

## 一、开发自测标准（本仓库约定）

### 1.1 自测准入（何时开始做）

- 本地或联调环境 **可启动** `wmt-server`（当前 profile 能连上配置库 **Redis + 数据源**）。
- 本迭代涉及库表时：**Task 2 顺序**已执行（基线 `ruoyi-vue-pro.sql` + `pfb-core-schema-v0.sql`），且库名与 JDBC 一致（如 `wmt_pfb`）。
- 依赖 **短信 / 三方** 时：已明确开发环境行为（固定码、Mock、或真实通道），避免「环境未配却判为 Bug」。

### 1.2 自测通过最低标准

| 维度 | 要求 |
|------|------|
| **功能** | 本迭代新增/修改的 **每个对外 HTTP 接口** 至少 1 条 **正向主路径**；关键业务规则各 1 条 **负向或边界**（见第三节）。 |
| **鉴权** | 所有「需登录」接口：**无 Token** 应失败；**PFB 用户 Token** 应成功（与 `userType` 一致）。 |
| **匿名** | `permit-all_urls` 中接口：**无 Token** 可访问且业务语义正确。 |
| **响应** | 统一校验 `CommonResult`：`code` 成功、`data`/`msg` 与 Swagger 描述一致；禁止仅「200 无报错」。 |
| **数据** | 主路径产生 **可核对** 的数据库变化（或明确为只读无落库），自测记录中写 **库表/关键字段**。 |
| **回归** | 与本次改动 **同模块** 的相邻接口各抽 1 条冒烟（防止改 A 坏 B）。 |

### 1.3 自测不做（交给测试/联调）

- 全量兼容性矩阵、长稳压测、安全渗透。  
- 未在需求/接口契约中约定的「体验优化」类主观项（可记为改进建议，不阻塞自测通过）。

### 1.4 自测产出物（合并前应具备）

1. **本文第四节「自测记录表」**：每条用例 **执行结果打勾 + 备注**（失败写现象与日志关键字）。  
2. **缺陷**：阻塞合并的问题记 Issue/飞书 + 复现步骤；非阻塞记「已知问题」并关联排期。  
3. **配置说明**：若依赖特殊 profile、hosts、OB 租户账号格式等，在 MR 描述或本文「环境」节补一行。

### 1.5 工具建议（开发自测）

- **Swagger / Knife4j**：字段与枚举对齐（PFB Controller/VO 已补 `@Tag`/`@Operation`/`@Parameter`/`@Schema`）。  
- **curl / Bruno / Postman**：保存环境变量 `BASE`、`TOKEN`。  
- **日志**：`wmt-server` 控制台 + SQL 慢日志（如有）；错误需能对应到 **业务错误码**（`PfbErrorCodeConstants` 等）。

---

## 二、环境与测试数据

### 2.1 勾选

- [ ] `wmt-server` 启动 profile：`____________`（如 `pfb-local` / `nmlocal`）  
- [ ] `export BASE=http://127.0.0.1:48080/pfb-api`（按实际主机/端口改）  
- [ ] 数据源库名：`wmt_pfb`  
- [ ] Redis 可用  
- [ ] 短信：`wmt.captcha.enable` = `____`；若为 `true`，登录/注册等需在 JSON 中补 **图形验证码** 字段（见 `CaptchaVerificationReqVO` / Swagger）  
- [ ] 测试手机号 `MOBILE`、短信码（开发固定码或真实下发）已确认  

### 2.2 公共 shell 变量（以下 curl 均假设已执行）

```bash
export BASE=http://127.0.0.1:48080/pfb-api
export TOKEN='替换为登录接口返回的 accessToken'
export MOBILE=19000000001
export SMS_CODE='替换为实际短信验证码或环境固定码'
export REFRESH_TOKEN='替换为 refreshToken'
```

### 2.3 测试数据导入 / 清除（仅种子 UUID，不写 system_users）

在仓库根目录执行（库名按环境修改）：

```bash
mysql -h127.0.0.1 -P3306 -uroot -p wmt_pfb < docs/nm-deliver/test/sql/pfb-self-test-data-seed.sql
mysql -h127.0.0.1 -P3306 -uroot -p wmt_pfb < docs/nm-deliver/test/sql/pfb-self-test-data-cleanup.sql
```

**种子主键约定**（与 curl 示例一致）：

| 类型 | UUID |
|------|------|
| 上架 + 热门产品 | `11111111-1111-1111-1111-111111111101` |
| 上架 + 非热门产品 | `11111111-1111-1111-1111-111111111102` |
| 下架产品（get 负例） | `11111111-1111-1111-1111-111111111103` |
| 上架 Banner | `22222222-2222-2222-2222-222222222201` |

---

## 三、用例与 curl（按编号自测并打勾）

> 短信 `scene`：`LOGIN` | `REGISTER` | `RESET_PASSWORD`。  
> 需登录接口：增加请求头 `-H "Authorization: Bearer $TOKEN"`。  
> **未**开启图形验证码时，下列登录/注册 body 不含 captcha 字段；若 `wmt.captcha.enable=true`，请按 Swagger 自行追加。

### 3.1 匿名可读

- [ ] **A1** `GET /pfb/banner/list`

```bash
curl -sS -X GET "$BASE/pfb/banner/list" -H "Content-Type: application/json"
```

- [ ] **A2** `GET /pfb/loan-product/list`

```bash
curl -sS -X GET "$BASE/pfb/loan-product/list" -H "Content-Type: application/json"
```

- [ ] **A3** `GET /pfb/loan-product/hot-list`（默认 + limit）

```bash
curl -sS -X GET "$BASE/pfb/loan-product/hot-list" -H "Content-Type: application/json"
curl -sS -X GET "$BASE/pfb/loan-product/hot-list?limit=1" -H "Content-Type: application/json"
curl -sS -X GET "$BASE/pfb/loan-product/hot-list?limit=500" -H "Content-Type: application/json"
```

- [ ] **A4** `GET /pfb/loan-product/get`（上架 id + 下架 id）

```bash
curl -sS -X GET "$BASE/pfb/loan-product/get?id=11111111-1111-1111-1111-111111111101" -H "Content-Type: application/json"
curl -sS -X GET "$BASE/pfb/loan-product/get?id=11111111-1111-1111-1111-111111111103" -H "Content-Type: application/json"
```

### 3.2 认证（多数无 Token）

- [ ] **B1** 发送短信 — 非法 scene

```bash
curl -sS -X POST "$BASE/pfb/auth/send-sms-code" -H "Content-Type: application/json" \
  -d "{\"mobile\":\"$MOBILE\",\"scene\":\"INVALID\"}"
```

- [ ] **B1b** 发送短信 — LOGIN（需手机号已注册）

```bash
curl -sS -X POST "$BASE/pfb/auth/send-sms-code" -H "Content-Type: application/json" \
  -d "{\"mobile\":\"$MOBILE\",\"scene\":\"LOGIN\"}"
```

- [ ] **B2** 校验短信（不消费）

```bash
curl -sS -X POST "$BASE/pfb/auth/validate-sms-code" -H "Content-Type: application/json" \
  -d "{\"mobile\":\"$MOBILE\",\"scene\":\"LOGIN\",\"code\":\"$SMS_CODE\"}"
```

- [ ] **B3** 注册（需未占用手机号 + REGISTER 短信）

```bash
curl -sS -X POST "$BASE/pfb/auth/register" -H "Content-Type: application/json" \
  -d "{\"mobile\":\"19000000002\",\"nickname\":\"自测\",\"password\":\"a1234567\",\"smsCode\":\"$SMS_CODE\"}"
```

- [ ] **B4** 登录（密码 + LOGIN 短信）

```bash
curl -sS -X POST "$BASE/pfb/auth/login" -H "Content-Type: application/json" \
  -d "{\"mobile\":\"$MOBILE\",\"password\":\"你的密码\",\"smsCode\":\"$SMS_CODE\"}"
```

- [ ] **B5** 短信登录

```bash
curl -sS -X POST "$BASE/pfb/auth/sms-login" -H "Content-Type: application/json" \
  -d "{\"mobile\":\"$MOBILE\",\"code\":\"$SMS_CODE\"}"
```

- [ ] **B6** 重置密码

```bash
curl -sS -X POST "$BASE/pfb/auth/reset-password" -H "Content-Type: application/json" \
  -d "{\"mobile\":\"$MOBILE\",\"password\":\"a1234567\",\"code\":\"$SMS_CODE\"}"
```

- [ ] **B7** 刷新令牌

```bash
curl -sS -X POST "$BASE/pfb/auth/refresh-token?refreshToken=$REFRESH_TOKEN" -H "Content-Type: application/json"
```

- [ ] **B8** 登出（可带 Token）

```bash
curl -sS -X POST "$BASE/pfb/auth/logout" -H "Content-Type: application/json" -H "Authorization: Bearer $TOKEN"
```

- [ ] **B9** 注销（占位）

```bash
curl -sS -X POST "$BASE/pfb/auth/account-cancel" -H "Content-Type: application/json" -H "Authorization: Bearer $TOKEN"
```

### 3.3 自然人（需 Token）

- [ ] **C1** 状态

```bash
curl -sS -X GET "$BASE/pfb/individual/status" -H "Authorization: Bearer $TOKEN"
curl -sS -X GET "$BASE/pfb/individual/status"
```

- [ ] **C2** 提交/更新自然人

```bash
curl -sS -X POST "$BASE/pfb/individual/submit" -H "Content-Type: application/json" -H "Authorization: Bearer $TOKEN" \
  -d "{\"realName\":\"张三\",\"idCardNo\":\"110101199001011234\",\"address\":\"北京市朝阳区\"}"
```

- [ ] **C3** L2 回调占位

```bash
curl -sS -X POST "$BASE/pfb/individual/l2/callback" -H "Content-Type: application/json" -H "Authorization: Bearer $TOKEN"
```

### 3.4 企业（需 Token；`entId` 用创建接口返回值替换）

- [ ] **D1** 列表

```bash
curl -sS -X GET "$BASE/pfb/enterprise/list" -H "Authorization: Bearer $TOKEN"
```

- [ ] **D2** 新增企业（统一社会信用代码 18 位，勿与种子冲突）

```bash
curl -sS -X POST "$BASE/pfb/enterprise" -H "Content-Type: application/json" -H "Authorization: Bearer $TOKEN" \
  -d "{\"enterpriseName\":\"自测企业有限公司\",\"companyCreditCode\":\"91110000123456789X\",\"coreContact\":\"13800000000\"}"
```

- [ ] **D3** 编辑（`ENT_ID` 替换为 D2 返回的 id）

```bash
export ENT_ID='33333333-3333-3333-3333-333333333301'
curl -sS -X PUT "$BASE/pfb/enterprise/$ENT_ID" -H "Content-Type: application/json" -H "Authorization: Bearer $TOKEN" \
  -d "{\"enterpriseName\":\"自测企业有限公司（改）\",\"englishName\":\"SelfTest Co Ltd\",\"coreContact\":\"13800000001\"}"
```

- [ ] **D4** 默认企业

```bash
curl -sS -X POST "$BASE/pfb/enterprise/$ENT_ID/set-default" -H "Content-Type: application/json" -H "Authorization: Bearer $TOKEN"
```

- [ ] **D5** 法人核验占位

```bash
curl -sS -X POST "$BASE/pfb/enterprise/$ENT_ID/legal-verify" -H "Content-Type: application/json" -H "Authorization: Bearer $TOKEN"
```

### 3.5 融资申请（需 Token；先完成 C2、D2）

- [ ] **E1–E3** 门禁/征信/金额（按需改 body 触发失败）

```bash
curl -sS -X POST "$BASE/pfb/loan-application/submit" -H "Content-Type: application/json" -H "Authorization: Bearer $TOKEN" \
  -d "{\"productId\":\"11111111-1111-1111-1111-111111111101\",\"entId\":\"$ENT_ID\",\"applyAmountCent\":100000001,\"termMonths\":12,\"repayMethod\":\"EQUAL_PRINCIPAL_INTEREST\",\"creditAuthAccepted\":0}"
```

- [ ] **E4** 成功提交（金额须为万元整数倍，如 100 万元 = 100000000 分）

```bash
curl -sS -X POST "$BASE/pfb/loan-application/submit" -H "Content-Type: application/json" -H "Authorization: Bearer $TOKEN" \
  -d "{\"productId\":\"11111111-1111-1111-1111-111111111101\",\"entId\":\"$ENT_ID\",\"applyAmountCent\":100000000,\"termMonths\":12,\"repayMethod\":\"EQUAL_PRINCIPAL_INTEREST\",\"creditAuthAccepted\":1}"
```

- [ ] **E5** 我的申请列表

```bash
curl -sS -X GET "$BASE/pfb/loan-application/list" -H "Authorization: Bearer $TOKEN"
```

### 3.6 回归冒烟（可选）

- [ ] **R1** 其他模块健康检查（按你们环境替换主机/端口/路径）

```bash
export ADMIN_BASE=http://127.0.0.1:48080/admin-api
curl -sS -o /dev/null -w "%{http_code}" -H "Content-Type: application/json" "$ADMIN_BASE/system/auth/get-permission-info" || true
```

- [ ] **R2** 刷新令牌后旧 Token 行为（手动比对两次请求）

```bash
# 见 B7
curl -sS -X GET "$BASE/pfb/individual/status" -H "Authorization: Bearer $OLD_TOKEN"
```

---

## 四、自测记录表（合并前填写）

**开发者**：________ **分支/Commit**：________ **日期**：________

| 用例编号 | 结果（通过/失败/跳过） | 证据（日志关键字 / 截图路径 / HTTP trace） | 备注 |
|----------|------------------------|-----------------------------------------------|------|
| A1–A4 | | | |
| B1–B9 | | | |
| C1–C3 | | | |
| D1–D5 | | | |
| E1–E5 | | | |
| R1–R2 | | | |

**结论**： [ ] 达到第一节自测通过最低标准，可进入 Code Review / 提测  
**遗留已知问题**（非阻塞）：  

---

## 五、参考索引

| 内容 | 路径 |
|------|------|
| 种子 SQL | [`sql/pfb-self-test-data-seed.sql`](./sql/pfb-self-test-data-seed.sql) |
| 清除 SQL | [`sql/pfb-self-test-data-cleanup.sql`](./sql/pfb-self-test-data-cleanup.sql) |
| 一期任务与 Task 2 | [`../superpowers/plans/2026-05-11-pfb-phase1-backend-tasks.md`](../superpowers/plans/2026-05-11-pfb-phase1-backend-tasks.md) |
| OceanDB 初始化运维 | [`../superpowers/plans/2026-05-11-pfb-oceandb-mysql-server-db-init-ops.md`](../superpowers/plans/2026-05-11-pfb-oceandb-mysql-server-db-init-ops.md) |
| Postman 集合 | [`postman/WMT-PFB-api.postman_collection.json`](./postman/WMT-PFB-api.postman_collection.json) |
