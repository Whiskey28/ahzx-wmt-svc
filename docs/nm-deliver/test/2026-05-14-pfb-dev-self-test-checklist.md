# 开发自测标准与清单 — 普惠金融 PFB（一期）

> **适用阶段**：开发完成后的 **自测**（非正式测试团队用例集）。  
> **目标**：在合并/提测前，用可重复步骤验证本迭代接口 **可用、可回归、可说明**，并留下最小 **自测证据**。  
> **接口前缀**：`http://{host}:{port}/pfb-api`（路径形如 `/pfb-api/pfb/...`）。  
> **真源**：Controller 见 `wmt-module-pfb`；匿名白名单见 `wmt-server` 下 `application-pfb-local.yaml` / `application-nmlocal.yaml` 的 `wmt.security.permit-all_urls`。

---

## 一、开发自测标准（本仓库约定）

### 1.1 自测准入（何时开始做）

- 本地或联调环境 **可启动** `wmt-server`（当前 profile 能连上配置库 **Redis + 数据源**）。
- 本迭代涉及库表时：**Task 2 顺序**已执行（基线 `ruoyi-vue-pro.sql` + `pfb-core-schema-v0.sql`），且库名与 JDBC 一致（如 `wmt_pfb`）。
- 依赖 **短信 / 三方** 时：已明确开发环境行为（固定码、Mock、或真实通道），避免「环境未配却判为 Bug」。

### 1.2 自测通过最低标准

| 维度 | 要求 |
|------|------|
| **功能** | 本迭代新增/修改的 **每个对外 HTTP 接口** 至少 1 条 **正向主路径**；关键业务规则各 1 条 **负向或边界**（见第三节清单）。 |
| **鉴权** | 所有「需登录」接口：**无 Token** 应失败；**PFB 用户 Token** 应成功（与 `userType` 一致）。 |
| **匿名** | `permit-all_urls` 中接口：**无 Token** 可访问且业务语义正确。 |
| **响应** | 统一校验 `CommonResult`：`code` 成功、`data`/`msg` 与 Swagger 描述一致；禁止仅「200 无报错」。 |
| **数据** | 主路径产生 **可核对** 的数据库变化（或明确为只读无落库），自测记录中写 **库表/关键字段**。 |
| **回归** | 与本次改动 **同模块** 的相邻接口各抽 1 条冒烟（防止改 A 坏 B）。 |

### 1.3 自测不做（交给测试/联调）

- 全量兼容性矩阵、长稳压测、安全渗透。  
- 未在需求/接口契约中约定的「体验优化」类主观项（可记为改进建议，不阻塞自测通过）。

### 1.4 自测产出物（合并前应具备）

1. **本文档第四节「自测记录表」**：每条用例 **执行结果打勾 + 备注**（失败写现象与日志关键字）。  
2. **缺陷**：阻塞合并的问题记 Issue/飞书 + 复现步骤；非阻塞记「已知问题」并关联排期。  
3. **配置说明**：若依赖特殊 profile、hosts、OB 租户账号格式等，在 MR 描述或本文档「环境」节补一行。

### 1.5 工具建议（开发自测）

- **Swagger / Knife4j**：字段与枚举对齐。  
- **curl / Bruno / Postman**：保存环境变量 `baseUrl`、`accessToken`。  
- **日志**：`wmt-server` 控制台 + SQL 慢日志（如有）；错误需能对应到 **业务错误码**（`PfbErrorCodeConstants` 等）。

---

## 二、环境与数据准备（自测前勾选）

- [ ] `wmt-server` 启动 profile：`____________`（如 `pfb-local` / `nmlocal`）  
- [ ] Base URL：`http://________:____/pfb-api`  
- [ ] 数据源库名与表：`wmt_pfb` 中 `pfb_*`、`system_users` 可读  
- [ ] Redis 可用  
- [ ] 短信：`wmt.captcha.enable` = `____`；若开启图形验证码，登录/注册请求已按 VO 补齐  
- [ ] 测试手机号：`____________`（建议专用号段，避免污染生产用户）

---

## 三、PFB 一期接口自测清单（按执行顺序）

> 短信 `scene` 仅：`LOGIN` | `REGISTER` | `RESET_PASSWORD`。  
> 需登录接口：请求头 `Authorization: Bearer {accessToken}`（若项目改为其他 header，以 `SecurityProperties` 为准）。

### 3.1 匿名可读（无 Token）

| # | 方法 | 路径 | 自测要点 | 通过 |
|---|------|------|----------|------|
| A1 | GET | `/pfb-api/pfb/banner/list` | 返回列表；状态/时间窗符合「仅上架生效」 | [ ] |
| A2 | GET | `/pfb-api/pfb/loan-product/list` | 仅上架产品；字段非空合理 | [ ] |
| A3 | GET | `/pfb-api/pfb/loan-product/hot-list` | 默认条数；`?limit=1` 与较大 limit | [ ] |
| A4 | GET | `/pfb-api/pfb/loan-product/get?id={uuid}` | 上架产品 id 成功；胡编 id/下架 id 预期失败 | [ ] |

### 3.2 认证（无 Token，除注销外多为 PermitAll）

| # | 方法 | 路径 | 自测要点 | 通过 |
|---|------|------|----------|------|
| B1 | POST | `/pfb-api/pfb/auth/send-sms-code` | `scene` 非法 → 业务错误；合法 scene 能发（或开发环境等价成功） | [ ] |
| B2 | POST | `/pfb-api/pfb/auth/validate-sms-code` | 错码/过期；对码成功 | [ ] |
| B3 | POST | `/pfb-api/pfb/auth/register` | 新号注册成功，返回 token 结构；重复手机号失败 | [ ] |
| B4 | POST | `/pfb-api/pfb/auth/login` | 密码错；短信错；**正确链路**拿 `accessToken` | [ ] |
| B5 | POST | `/pfb-api/pfb/auth/sms-login` | 主路径一条 | [ ] |
| B6 | POST | `/pfb-api/pfb/auth/reset-password` | 主路径一条（注意 scene） | [ ] |
| B7 | POST | `/pfb-api/pfb/auth/refresh-token` | 合法 refresh 换新 token | [ ] |
| B8 | POST | `/pfb-api/pfb/auth/logout` | 带 token 调用后，原 token 访问需登录接口应失败（按实现验证） | [ ] |
| B9 | POST | `/pfb-api/pfb/auth/account-cancel` | 一期占位：登录态下调用，记录 **实际 code/msg**（可能为 TODO 业务码） | [ ] |

### 3.3 自然人（需 PFB Token）

| # | 方法 | 路径 | 自测要点 | 通过 |
|---|------|------|----------|------|
| C1 | GET | `/pfb-api/pfb/individual/status` | 无 Token 401/403；有 Token 返回结构合理 | [ ] |
| C2 | POST | `/pfb-api/pfb/individual/submit` | 合法提交成功；**身份证已被他人绑定** 负例（换号或造数据） | [ ] |
| C3 | POST | `/pfb-api/pfb/individual/l2/callback` | 占位接口成功即可 | [ ] |

### 3.4 企业（需 PFB Token）

| # | 方法 | 路径 | 自测要点 | 通过 |
|---|------|------|----------|------|
| D1 | GET | `/pfb-api/pfb/enterprise/list` | 无 Token 拒绝；有 Token 列表结构 | [ ] |
| D2 | POST | `/pfb-api/pfb/enterprise` | 合法统一社会信用代码建档+绑定；**格式非法**、**重复绑定** 负例 | [ ] |
| D3 | PUT | `/pfb-api/pfb/enterprise/{id}` | 本人企业成功；他人 `id` 无权限 | [ ] |
| D4 | POST | `/pfb-api/pfb/enterprise/{id}/set-default` | 主路径 | [ ] |
| D5 | POST | `/pfb-api/pfb/enterprise/{id}/legal-verify` | 占位：记录返回是否与「未开通」类说明一致 | [ ] |

### 3.5 融资申请（需 PFB Token）

| # | 方法 | 路径 | 自测要点 | 通过 |
|---|------|------|----------|------|
| E1 | POST | `/pfb-api/pfb/loan-application/submit` | **未实名/未绑企业** 应被门禁拦截 | [ ] |
| E2 | 同上 | 同上 | **未勾选征信授权** 负例 | [ ] |
| E3 | 同上 | 同上 | **申请金额非万元整数倍**（`applyAmountCent`）负例 | [ ] |
| E4 | 同上 | 同上 | 完整数据：**productId + entId** 合法，提交成功 | [ ] |
| E5 | GET | `/pfb-api/pfb/loan-application/list` | 能看到刚提交的记录（或一期 stub 行为与实现一致） | [ ] |

### 3.6 回归冒烟（可选但推荐）

| # | 说明 | 通过 |
|---|------|------|
| R1 | 改代码模块相关的 **Admin/Member 无关接口** 未被误伤：抽 1 条原系统登录或健康检查（若有） | [ ] |
| R2 | 切换 `refresh-token` 后旧 token 行为符合预期 | [ ] |

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
| 一期任务与 Task 2 | `docs/nm-deliver/superpowers/plans/2026-05-11-pfb-phase1-backend-tasks.md` |
| OceanDB 初始化运维 | `docs/nm-deliver/superpowers/plans/2026-05-11-pfb-oceandb-mysql-server-db-init-ops.md` |
| nm-deliver 总索引 | `docs/nm-deliver/README.md` |
