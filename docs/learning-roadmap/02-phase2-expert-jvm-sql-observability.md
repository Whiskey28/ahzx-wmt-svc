# 阶段二：技术专家向 — JVM / SQL / 事务 / 可观测性 + 演练复盘

在本仓库栈（Java 17、Spring Boot 3、MyBatis-Plus、Redis 缓存等）上**结合真实模块**学习，避免孤立背概念。

## 1. JVM 与运行时（每周 2–4 小时理论 + 本仓库实践）

- **类加载与 Spring**：理解 `Fat Jar` 启动、`spring-boot-loader`；结合 `WmtServerApplication` 看组件扫描范围。
- **线程与请求**：Servlet 容器线程模型；`@Async` 与业务线程池（若有）边界——**事务只在调用线程传播**，跨线程需显式设计。
- **内存与 GC**：用 `-Xlog:gc*`（JDK 17）在本地跑一次；能读懂 Young GC / Full GC 是否与流量峰值相关。
- **Micrometer / Actuator**：查阅 `wmt-spring-boot-starter-monitor` 与 `application*.yaml` 是否开启 `management.endpoints`；能解释 **p99 延迟**与接口日志的对应关系。

**本仓库实践建议**：选一个 `wmt-module-credit-report` 或 `wmt-module-report` 的热点接口，用 JMeter 或简单并发脚本压测，对比开启/关闭某缓存或 SQL 索引前后的指标。

## 2. SQL 与数据访问

- **MyBatis-Plus**：`BaseMapperX`、`LambdaQueryWrapper`、分页 `pageSize=-1` 语义（见项目规则与框架文档）。
- **慢查询**：开启数据源 SQL 日志或慢查询日志；`EXPLAIN` 分析索引；**N+1** 与批量查询（对照 `ReportFillServiceImpl` 中批量查 report/category 的模式）。
- **迁移**：若使用 Flyway/Liquibase（以模块为准），理解版本号与回滚策略。

## 3. 事务与一致性

- **声明式事务**：`@Transactional` 默认传播行为、同类自调用陷阱、只读事务。
- **幂等与重试**：报表生成、报送类任务若存在「重复点击」或「消息重投」，如何设计幂等键。
- **失败可见性**：用户看到 `CommonResult` 错误码时，后台数据是否已部分写入——能口述补偿或人工处理路径。

## 4. 可观测性

- **日志**：链路 ID（若框架注入）、日志级别、敏感字段脱敏。
- **指标**：QPS、错误率、依赖（DB/Redis）耗时；告警阈值如何设避免「狼来了」。
- **分布式追踪**：若接入 SkyWalking/Zipkin，会看一次完整 trace。

---

## 附录：性能或故障演练 — 复盘模板（复制后填写）

```markdown
## 演练主题
- 日期：
- 环境：dev / test / 预发 / 生产-like
- 参与者：

## 现象
- 用户或监控表现：（例如：接口超时、错误率升高、CPU 飙高）

## 时间线
- T0：
- T+5min：

## 根因（5 Whys 或等价）
1.
2.

## 影响范围
- 受影响接口/租户/数据量：

## 临时措施
-

## 永久措施
- 代码/配置/容量：

## 预防与监控
- 新增告警/仪表盘/Runbook：

## 遗留项
-
```

**阶段二出口**：完成至少 **1 次** 填写完整的复盘（可在测试环境模拟慢 SQL 或线程打满），并能在组内 **15 分钟** 讲清根因与预防。
