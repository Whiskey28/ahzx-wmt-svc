# PFB-API（`/pfb-api`）与 `UserTypeEnum.PFB`（C2）双仓集成说明

面向内蒙普惠金融小程序等「独立 C 端 API 前缀 + 独立 userType」场景，与现有 `/admin-api` + `ADMIN`、`/app-api` + `MEMBER` 并行，互不混用 Token。

## 1. 背景与取舍（C1 / C2）

| 方案 | 含义 | 取舍 |
|------|------|------|
| C1 | 复用 `/app-api` 与 `MEMBER` | 与现有会员体系、路由、Swagger 分组耦合，难以单独演进 |
| C2 | 新增 `/pfb-api` 与 `UserTypeEnum.PFB(3)` | 路径与 Token 类型一一对应，`TokenAuthenticationFilter` 可严格校验 |

本仓库采用 **C2**：内蒙小程序仅走 `/pfb-api`，发 Token 时 `userType = PFB`，`userId` 仍为 **`system_users.id`**（与后台用户同表，靠 `userType` 区分语义）。

## 2. 双仓分支与版本对齐

1. **组件库**（`wmt-framework`，子工程 `wmt-framework-jdk17`）：从约定基分支拉出 `feat/pfb-api-c2`，改完后在组件库根执行 `mvn clean install -DskipTests`，使本地/私服能解析到含 `PFB` 的 `wmt-common` 与各 starter。
2. **业务仓**（本仓库 `ahzx-wmt-svc`）：从 `dev` 拉 `feat/pfb-api-c2`。
3. **版本**：根 `pom.xml` 的 `revision`（如 `2025.12-jdk17-SNAPSHOT`）须与 BOM `wmt-dependencies-jdk17` 及已 install 的组件库 **一致**；否则编译期找不到新枚举或新 `WebProperties` 字段。

```text
wmt-framework (mvn install) → wmt-dependencies-jdk17 → ahzx-wmt-svc import
```

## 3. `userType` 与 URL 前缀推导关系

| 前缀 | 典型 Controller 包 | `WebFrameworkUtils.getLoginUserType` | Token 内 `userType` |
|------|---------------------|--------------------------------------|---------------------|
| `/admin-api` | `**.controller.admin.**` | `ADMIN` (2) | 须为 `ADMIN` |
| `/app-api` | `**.controller.app.**` | `MEMBER` (1) | 须为 `MEMBER` |
| `/pfb-api` | `**.controller.pfb.**` | `PFB` (3) | 须为 `PFB` |

完整 URL 形态示例：`/pfb-api` + 业务段 `/pfb/auth/login` → `/pfb-api/pfb/auth/login`。类上 `@RequestMapping("/pfb/auth")` 等仍只写业务段。

## 4. 组件库（`wmt-framework-jdk17`）改动清单（摘要）

- `UserTypeEnum`：新增 `PFB(3, "普惠金融小程序")`。
- `WebProperties`：`pfbApi`（默认 `prefix=/pfb-api`，`controller=**.controller.pfb.**`）。
- `WmtWebAutoConfiguration`：第三段 `putPathPrefix`；路径映射表容量与 admin/app 一致扩展为 3。
- `WebFrameworkUtils` / `ApiRequestFilter`：识别 `/pfb-api` 前缀。
- `WmtSwaggerAutoConfiguration`：`pathsToMatch` 增加 `/pfb-api/**` 相关 pattern。
- `AuthorizeRequestsCustomizer`：新增 `buildPfbApi(String url)`，供各模块 `SecurityConfiguration` 链式 `requestMatchers`。
- `TokenAuthenticationFilter`：注释说明带前缀请求须与 Token 中 `userType` 一致。

各业务模块若有 `switch (userType)`，需补 `PFB` 或 `default`，避免遗漏。

## 5. 业务仓（`ahzx-wmt-svc`）改动清单

### 5.1 `OAuth2TokenServiceImpl.buildUserInfo`

对 `UserTypeEnum.PFB`：按 `userId` 调用 `AdminUserService.getUser`（`system_users`），组装 `LoginUser` 所需 `userInfo`。用户不存在时返回 **空 Map**，避免落入「未知用户类型」。普惠金融场景下可不写入后台部门字段（如仅 `nickname`），与 ADMIN 分支区分产品语义。

### 5.2 `application-nmlocal.yaml`

- `wmt.web.pfb-api`：可按 profile 覆盖默认前缀与 controller 包 pattern。
- `wmt.security.permit-all_urls`：**注意** 在 Spring Boot 中，profile 文档里若单独声明该列表，会 **覆盖** 主 `application.yaml` 中同名列表，因此须 **同时保留** 原需匿名的路径（如 `/admin-api/mp/open/**`）并追加 PFB 匿名登录路径（如 `/pfb-api/pfb/auth/**`，以实际 `@PermitAll` 接口为准）。

### 5.3 `SecurityConfiguration`（按需）

若匿名接口无法仅靠 YAML 表达（例如按 HTTP 方法细分），可在各模块 `SecurityConfiguration` 中使用 `buildPfbApi("/pfb/...")` 与 `buildAdminApi` / `buildAppApi` 并列配置。

## 6. 构建与验证清单

**构建（已通过示例）：**

```bash
mvn -pl wmt-server -am package -DskipTests
```

**建议手工验证：**

1. `nmlocal`（或等价 profile）启动后，Swagger/Knife4j 能出现 `/pfb-api/**` 分组（若已启用 springdoc）。
2. 通过 `OAuth2TokenApi` 或登录接口签发 **`userType=PFB`** 的 Token，请求 `/pfb-api/...` 受保护接口，应 **200**。
3. 同一 Token 请求 `/admin-api/...`，`TokenAuthenticationFilter` 应报 **类型不匹配**（预期失败）。
4. 回归：`/admin-api/system/auth/**`、既有 `/app-api` 行为不变。

**常见故障：**

| 现象 | 可能原因 |
|------|----------|
| 编译失败，找不到 `UserTypeEnum.PFB` | 组件库未 `install` 或业务仓 `revision` 与 BOM 不一致 |
| 401 / 用户类型错误 | Token 的 `userType` 与请求前缀推导不一致；或接口未加入 `permit-all_urls` |
| 404 或路由未挂 `/pfb-api` | Controller 未落在 `**.controller.pfb.**` 包下 |

## 7. 可选：字典 `user_type`

管理端若用数据字典展示用户类型，可在 SQL 中为 `user_type` 增加 value `3`、label「普惠金融」等；**非** Token 校验硬依赖。

## 8. 参考代码位置（业务仓）

- `wmt-module-system/.../OAuth2TokenServiceImpl.java`：`buildUserInfo`
- `wmt-server/src/main/resources/application-nmlocal.yaml`：`wmt.web.pfb-api`、`wmt.security.permit-all_urls`

组件库路径以本机克隆为准，例如 `wmt-framework/wmt-framework-jdk17`。
