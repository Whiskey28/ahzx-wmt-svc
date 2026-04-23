# 阶段一：启动链路 + 征信报送「报表填报」示例业务链

面向本仓库（`ahzx-wmt-svc`）与前端 `CreditService_Report_Web`（部署名常为 `CreditServiceReport`）联调。

## 1. 应用如何启动

- **入口类**：[`WmtServerApplication`](../../wmt-server/src/main/java/com/wmt/server/WmtServerApplication.java)
  - `@SpringBootApplication(scanBasePackages = {"${wmt.info.base-package}.server", "${wmt.info.base-package}.module"})`：扫描 `com.wmt.server` 与所有 `com.wmt.module` 下的组件，使各 `wmt-module-*` jar 中的 `@Configuration`、`@RestController` 等生效。
  - `@EnableAsync`：异步任务支持。
- **聚合方式**：[`wmt-server/pom.xml`](../../wmt-server/pom.xml) 通过 Maven 依赖引入 `wmt-module-credit-report`、`wmt-module-system`、`wmt-module-infra`、`wmt-module-report` 等；**无业务代码的容器模块**，运行时 classpath 即包含各业务模块。
- **激活配置**：[`application.yaml`](../../wmt-server/src/main/resources/application.yaml) 中 `spring.profiles.active`（示例为 `creditlocal`），本地以实际 profile 为准。

## 2. 管理端 API 前缀为何是 `/admin-api`

- 配置类：框架 [`WebProperties`](../../wmt/wmt-framework-jdk17/wmt-spring-boot-starter-web/src/main/java/com/wmt/framework/web/config/WebProperties.java) 中 `wmt.web.admin-api.prefix` 默认为 `/admin-api`，且仅对包路径匹配 `**.controller.admin.**` 的 Controller 追加此前缀。
- **效果**：`ReportFillController` 映射为 `@RequestMapping("/credit/report-fill")`，对外完整路径为：

  `GET /admin-api/credit/report-fill/category/list`

- **安全与网关**：`/admin-api` 与 `/app-api` 分离，便于 Nginx 只反向代理 API 前缀，避免 Swagger、Actuator 等误暴露（见 `WebProperties` 注释）。

## 3. 示例业务链：查询积木报表分类列表

以下为「只读、无鉴权注释场景」的最短链路，适合画序列图与断点调试。

| 层级 | 类型 | 说明 |
|------|------|------|
| HTTP | `GET /admin-api/credit/report-fill/category/list` | 管理端前缀 + Controller 路径 |
| Controller | [`ReportFillController#getCategoryList`](../../wmt-module-credit-report/src/main/java/com/wmt/module/credit/report/controller/admin/report/ReportFillController.java) | 返回 `CommonResult<List<JimuReportCategoryRespVO>>` |
| Service | [`ReportFillServiceImpl#getCategoryList`](../../wmt-module-credit-report/src/main/java/com/wmt/module/credit/report/service/ReportFillServiceImpl.java) | `jimuReportCategoryMapper.selectList` + `BeanUtils.toBean` 转 VO |
| 持久层 | `JimuReportCategoryMapper`（MyBatis-Plus） | 条件：`del_flag = 0`，按 `sort_no` 排序 |

**异常与边界**：若数据库不可用，异常由全局异常处理器转为 `CommonResult` 错误响应（具体 handler 在 `wmt-framework` 中，调试时可从调用栈向上追）。

**同模块延伸路径**（可自行续画）：

- `GET .../template/list?categoryId=xxx` → `getTemplateListByCategoryId`
- `GET .../record/page` → 分页与多表关联填充（报表、分类名等）
- `GET .../record/{id}/edit-url` → 拼装积木编辑 URL

## 4. 前端联调笔记（与 Nginx 部署一致时）

以下与 [`wmt-module-credit-report/docs`](../../wmt-module-credit-report/docs) 中测试环境部署描述一致，本地开发可将前端 devServer 代理到后端。

| 项 | 说明 |
|----|------|
| 静态资源路径 | 生产常挂载为 `/CreditServiceReport/`（以实际 `nginx.conf` 为准） |
| 后端 API | 通常反代到同一主机端口的 `/admin-api/`，与浏览器中 Network 面板路径一致 |
| 鉴权 | 带登录态时关注 `Authorization` 等请求头；未配置 `@PreAuthorize` 的接口仍可能被全局安全过滤器约束，以运行环境为准 |
| 响应体 | 成功时 `code` 为成功码（测试中常见 `0`），业务数据在 `data`（见 `ReportFillControllerWebMvcTest`） |

**建议自测步骤**：登录管理端 → 打开报表填报相关页 → DevTools → 选中 `category/list` 或 `record/page` → 核对 URL、状态码、`CommonResult` 字段。

## 5. 阶段一自评（出口）

- [ ] 能口述从 `WmtServerApplication` 到 `getCategoryList` 的调用顺序与数据去向。
- [ ] 能说明为何 Controller 上写 `/credit/report-fill` 而浏览器里是 `/admin-api/credit/report-fill/...`。
- [ ] 本地或联调环境完成至少一条「登录 → 列表/分类」请求闭环，并记下与本文不一致的环境差异。
