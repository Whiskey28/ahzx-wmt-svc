# 个人技能自评与学习索引（固化版）

> **填写日期**：2026-04-20  
> **适用仓库**：`ahzx-wmt-svc`（Spring Boot 3.5 + JDK 17 + `wmt-framework-jdk17` 体系）  
> **说明**：本文档固化本人自评结论与官方/权威文档入口，便于按弱项检索；不展开原理讲义。

---

## 1. 五档自评（原始结论）


| 档位  | 描述                        | 自评                |
| --- | ------------------------- | ----------------- |
| 1   | 能照文档/示例写出可运行代码            | **完全可以**          |
| 2   | 能独立改配置并解释每个关键项在运行时的作用     | **可依文档改配置，但解释不全** |
| 3   | 能读线程栈 / SQL / 日志定位常见问题    | **能**             |
| 4   | 能说明与相邻层（事务、缓存、安全等）的交互与典型坑 | **不能**            |
| 5   | 能设计或评审方案（性能、一致性、安全）       | **不能**            |


### 1.1 快速情景题（结论：均不能）


| 情景                                                 | 结论     |
| -------------------------------------------------- | ------ |
| 说清一次 HTTP 从 Filter/Security → Controller → 返回的大致链路 | **不能** |
| 解释逻辑删除、分页、Wrapper、多租户插件对 SQL 的先后顺序与影响              | **不能** |
| 说明缓存与 DB 一致性在本项目中的策略与取舍                            | **不能** |
| 画出 MQ 生产者 → Broker → 消费者的失败重试与幂等落点                 | **不能** |
| 说明 Token/Session、权限注解、数据权限在封装中的配合关系                | **不能** |


---

## 2. 能力画像（摘要）

- **强项**：按文档落地代码；结合日志/SQL/栈做常见故障定位。  
- **中项**：改配置「能动手、解释不完整」——缺的是**运行时模型**与**术语对齐**。  
- **短板**：跨层交互、边界与坑、方案级设计/评审；**请求链路、数据访问链、消息与安全模型**尚未形成可复述的心智图。

**推论**：下一阶段不应再堆「更多 API 记忆」，而应优先补齐 **链路 + 分层边界 + 一致性/安全基础概念**（仍通过官方文档 + 本仓库代码对照，而非长文教程）。

---

## 3. 学习优先级（建议 6～10 周主线）

按弱项依赖顺序排列；每一项学完用「能用自己的话画一张简图 + 指出本仓库一个对应类/配置」作验收。


| 顺序  | 主题                                                   | 原因（与自评对齐）                |
| --- | ---------------------------------------------------- | ------------------------ |
| P0  | **Spring MVC + Servlet 过滤器链 + Spring Security 过滤器链** | 情景题「HTTP 链路」「Token/权限」不能 |
| P0  | **Spring 事务与数据源、与 MyBatis / MP 的关系**                 | 档位 4「与事务相邻层」不能           |
| P1  | **MyBatis-Plus 插件顺序、多租户、逻辑删除、分页**                    | 情景题「SQL 与插件」不能           |
| P1  | **Spring Cache + Redis/Redisson 与 DB 的常见模式**         | 情景题「缓存一致性」不能             |
| P2  | **MQ 语义（至少一种实际启用的实现）+ 幂等与重试**                        | 情景题「MQ」不能                |
| P2  | **可观测：日志、Trace 与排障在 Dev 环境的用法**                      | 为档位 4、5 打基础              |
| P3  | **方案与设计**：在 P0～P2 有简图后再读「生产检查清单」类官方章节**              | 档位 5                     |


---

## 4. 文档与索引链接表（按上表 P0→P3 分组）

### 4.1 P0：Web 与安全链路

- Spring Boot 总览：[https://docs.spring.io/spring-boot/reference/](https://docs.spring.io/spring-boot/reference/)  
- Spring MVC / Servlet：[https://docs.spring.io/spring-framework/reference/web/webmvc.html](https://docs.spring.io/spring-framework/reference/web/webmvc.html)  
- Spring Security：[https://docs.spring.io/spring-security/reference/](https://docs.spring.io/spring-security/reference/)  
- Servlet Security 过滤器链：[https://docs.spring.io/spring-security/reference/servlet/architecture.html](https://docs.spring.io/spring-security/reference/servlet/architecture.html)  
- Jakarta Bean Validation：[https://jakarta.ee/specifications/bean-validation/3.0/](https://jakarta.ee/specifications/bean-validation/3.0/)

**本仓库对照**：`wmt/wmt-framework-jdk17/wmt-spring-boot-starter-web`、`wmt-spring-boot-starter-security`；全局异常与统一返回约定见工作区规则 `.cursor/rules/java-springboot-mybatisplus-cursorrules.mdc`。

### 4.2 P0～P1：事务与数据访问

- Spring 事务：[https://docs.spring.io/spring-framework/reference/data-access/transaction.html](https://docs.spring.io/spring-framework/reference/data-access/transaction.html)  
- Spring Boot 数据源：[https://docs.spring.io/spring-boot/reference/data/sql.html](https://docs.spring.io/spring-boot/reference/data/sql.html)  
- MyBatis 3：[https://mybatis.org/mybatis-3/](https://mybatis.org/mybatis-3/)  
- MyBatis-Plus（中文）：[https://baomidou.com/](https://baomidou.com/)  
- MyBatis-Plus-Join：[https://github.com/yulichang/mybatis-plus-join](https://github.com/yulichang/mybatis-plus-join)  
- dynamic-datasource：[https://www.dynamic-datasource.com/](https://www.dynamic-datasource.com/)  
- Druid：[https://github.com/alibaba/druid/wiki](https://github.com/alibaba/druid/wiki)

### 4.3 P1：缓存与 Redis

- Spring Cache：[https://docs.spring.io/spring-boot/reference/io/caching.html](https://docs.spring.io/spring-boot/reference/io/caching.html)  
- Spring Data Redis：[https://docs.spring.io/spring-data/redis/reference/](https://docs.spring.io/spring-data/redis/reference/)  
- Redisson：[https://redisson.org/documentation/](https://redisson.org/documentation/)

### 4.4 P2：消息

- RocketMQ Spring：[https://github.com/apache/rocketmq-spring](https://github.com/apache/rocketmq-spring)  
- Spring for Apache Kafka：[https://docs.spring.io/spring-kafka/reference/](https://docs.spring.io/spring-kafka/reference/)  
- Spring AMQP：[https://docs.spring.io/spring-amqp/reference/](https://docs.spring.io/spring-amqp/reference/)

### 4.5 P2：任务与异步

- Quartz：[https://www.quartz-scheduler.org/documentation/](https://www.quartz-scheduler.org/documentation/)  
- Spring Scheduling / 异步：[https://docs.spring.io/spring-framework/reference/integration/scheduling.html](https://docs.spring.io/spring-framework/reference/integration/scheduling.html)

### 4.6 WebSocket

- Spring WebSocket：[https://docs.spring.io/spring-framework/reference/web/websocket.html](https://docs.spring.io/spring-framework/reference/web/websocket.html)

### 4.7 服务保障与锁

- Lock4j：[https://baomidou.com/pages/ec9d1a/](https://baomidou.com/pages/ec9d1a/)

### 4.8 可观测

- SkyWalking：[https://skywalking.apache.org/docs/](https://skywalking.apache.org/docs/)  
- Spring Boot Admin：[https://docs.spring-boot-admin.com/current/](https://docs.spring-boot-admin.com/current/)

### 4.9 API 文档（日常开发已用）

- SpringDoc：[https://springdoc.org/](https://springdoc.org/)  
- Knife4j：[https://doc.xiaominfo.com/](https://doc.xiaominfo.com/)

### 4.10 映射与构建

- MapStruct：[https://mapstruct.org/documentation/stable/reference/html/](https://mapstruct.org/documentation/stable/reference/html/)  
- Lombok：[https://projectlombok.org/features/](https://projectlombok.org/features/)  
- Maven Compiler 注解处理器：[https://maven.apache.org/plugins/maven-compiler-plugin/examples/set-compiler-annotation-processor-paths.html](https://maven.apache.org/plugins/maven-compiler-plugin/examples/set-compiler-annotation-processor-paths.html)

### 4.11 可选（模块启用时再挖）

- Flowable：[https://www.flowable.com/open-source/docs/](https://www.flowable.com/open-source/docs/)  
- JustAuth：[https://www.justauth.cn/](https://www.justauth.cn/)

### 4.12 英文实战补充（非官方）

- Baeldung Spring：[https://www.baeldung.com/spring-tutorial](https://www.baeldung.com/spring-tutorial)

---

## 5. 最小验收建议（与档位 4、5 对齐）

每完成一个 P0/P1 主题，在笔记或 PR 描述中回答三句话即可：

1. **谁**在**哪一层**调用了谁（只写类/接口名与包前缀即可）。
2. **失败时**最先看哪两类日志或哪条 SQL。
3. **不能做什么**（写一条边界，避免过度自信）。

---

## 6. 各主题迫切问题与简明答案

> 用法：每个小主题先自问「能否不看文档答出」；答不出则对照上节官方链接 + 本仓库路径加深。答案刻意写短，便于复述。

### 6.1 Spring Boot 核心（配置、启动、自动配置）

| 迫切问题 | 简明答案 |
|----------|----------|
| 多个 `application-*.yaml` 与 `spring.profiles.active` 是什么关系？谁覆盖谁？ | **主文件 + 激活的 profile 文件合并**成一套 `Environment`；**同 key 一般以后加载的 profile 为准**（具体优先级见 Boot 文档 *Externalized Configuration* 的完整顺序表）。本仓库：`application.yaml` 里 `spring.profiles.active: nmlocal` 会并入 `application-nmlocal.yaml`。 |
| `spring.main.allow-circular-references: true` 解决什么？关掉会怎样？ | Boot 2.6+ **默认禁止循环依赖**；设为 `true` **允许 A→B→A** 这类 Bean 注入。关掉后若存在循环依赖，**启动失败**并抛出循环依赖异常。本仓库注释写明因三层架构暂允许循环引用。 |
| `@SpringBootApplication` 一行里实际包含什么？ | 等价 **`@Configuration` + `@EnableAutoConfiguration` + `@ComponentScan`**（默认扫主类所在包及子包）。业务意义：**本模块的 Bean 定义入口 + 触发 classpath 上各 starter 的自动配置**。 |
| 自动配置「为什么在我机器上生效 / 不生效」？ | 生效条件通常是 **`@ConditionalOnClass` / `@ConditionalOnMissingBean` / `@ConditionalOnProperty`** 等：classpath 有某类、或尚未存在某 Bean、或某配置为 true。排查：看自动配置类上的条件注解 + `application*.yaml` 是否显式关闭（如 `wmt.tenant.enable=false`）。 |

### 6.2 P0：HTTP、Servlet Filter、Spring MVC、Spring Security

| 迫切问题 | 简明答案 |
|----------|----------|
| 一次请求进 Tomcat/Undertow 后，**Filter** 和 **DispatcherServlet** 谁先谁后？ | **Filter 链先执行**，再到 **DispatcherServlet**，再到 **Controller**。Filter 属于 Servlet 规范；DispatcherServlet 是 Spring MVC 前端控制器。 |
| **Spring Security** 的 Filter 和普通 `Filter` 放一起时顺序谁定？ | Security 通过 **`FilterChainProxy`** 注册一串 Filter，顺序由 Security 配置决定；还可与 Boot 的 `Ordered` / `FilterRegistrationBean#order` 等共同决定**相对其他 Filter**的前后。本仓库多租户等见 `WebFilterOrderEnum` 与各 `FilterRegistrationBean`。 |
| **HandlerInterceptor**（MVC 拦截器）和 **Filter** 差在哪？ | **Filter**：Servlet 层，**不知道** Spring MVC 的 Handler；**Interceptor**：Spring MVC 内部，在**已解析出 Handler** 前后调用，可拿到 `HandlerMethod`。认证结论常在 Filter 已写好；业务鉴权可在 Interceptor 或方法上。 |
| 为什么有时是 **401/403 在进 Controller 之前**就返回了？ | **Security Filter** 在链路前面：未认证/无权限时**直接短路响应**，请求**不会进入**你的 `@RestController`。若进了 Controller 再失败，多是**业务错误码**（如你们 `CommonResult`）由全局异常或业务代码返回。 |
| `@PreAuthorize` 这类注解依赖什么才能生效？ | 需在配置中**开启方法级安全**（如 `@EnableMethodSecurity`），且目标方法须走 **Spring 代理**（对外部调用生效）。与「仅 Filter 做路径级鉴权」是**不同层级**。 |

### 6.3 P0：数据源、Spring 事务、MyBatis / MyBatis-Plus

| 迫切问题 | 简明答案 |
|----------|----------|
| `@Transactional` 什么时候**不生效**？ | 常见：**同类内部 `this.xxx()` 调用**（无代理）、**非 public**（默认代理策略）、**异常被吞掉**、**数据源未走 Spring 管理**、**多数据源切错库**等。 |
| Spring 事务与 **MyBatis 执行的 SQL** 如何绑在同一连接上？ | Spring 把当前 **`DataSource` + `Connection`** 绑到 **`TransactionSynchronizationManager`**；`SqlSessionTemplate` 取连接执行 SQL，**提交/回滚由事务管理器统一做**。 |
| **readOnly=true** 有什么用？ | 提示容器/驱动**只读事务**，部分场景下可优化（如路由只读库）；**不自动禁止写**，写仍可能成功或依赖数据源行为，勿当安全锁。 |
| MyBatis-Plus 的 **BaseMapper** 方法与 XML 里语句冲突谁赢？ | 一般 **XML 中同 id 会覆盖/与注解映射合并规则以 MyBatis 解析结果为准**；实际以是否重复 `id`、是否 `namespace` 一致为准，**重复应主动改名避免歧义**。 |

### 6.4 P1：MyBatis-Plus 插件（租户、分页、逻辑删除、Wrapper）

| 迫切问题 | 简明答案 |
|----------|----------|
| **租户、分页、数据权限**等插件谁先改 SQL？ | MP 使用 **`MybatisPlusInterceptor` 内多 `InnerInterceptor`**，**`addInnerInterceptor` 顺序决定执行顺序**（链式改写 BoundSql）。**本仓库**：租户 `TenantLineInnerInterceptor` 通过 `MyBatisUtils.addInterceptor(interceptor, inner, **0**)` **插在索引 0**，注释写明 **必须在分页插件之前**（见 `WmtTenantAutoConfiguration` / `WmtMybatisAutoConfiguration`）。 |
| **逻辑删除**最终怎么体现在 SQL 上？ | 查询自动带 **`deleted=0`**（或你配置的值）；删除常变为 **UPDATE 置删除标记**。与租户条件一样是 **WHERE 改写**。 |
| **分页**插件做了什么？ | 对列表查询：额外发 **count**（或可优化为单次），再对原 SQL **包一层 limit**。若 SQL 极复杂，**count 可能不准或慢**，需单独优化或自定义 count。 |
| `LambdaQueryWrapper` 与 XML 混用注意什么？ | Wrapper 生成动态条件；XML 手写片段。**同一方法不要两套条件打架**；动态表名/租户字段若手写 XML 要**自己保证**与插件一致。 |

### 6.5 P1：Spring Cache、Redis、Redisson、与 DB 的一致性

| 迫切问题 | 简明答案 |
|----------|----------|
| `@Cacheable` 和直接用 **RedisTemplate** 差在哪？ | `@Cacheable` 走 **Spring Cache 抽象**，由 `CacheManager`（如 Redis）管理 key/value 序列化、过期；**RedisTemplate** 是**命令级 API**，更灵活但无统一缓存注解语义。 |
| **本仓库**里缓存与租户可能有什么关系？ | 多租户场景下，**缓存 key 常需带 tenantId**，否则出现**串租户数据**。本仓库租户 starter 含 **TenantRedisCacheManager** 等（见 `WmtTenantAutoConfiguration`），学习时对照「key 维度」。 |
| 缓存与 DB **强一致**能靠 `@Cacheable` 默认行为保证吗？ | **不能默认保证**。常见是 **Cache-Aside**：先更 DB 再删缓存 / 或先删缓存再更 DB，各有**竞态窗口**；要求高一致需**分布式锁、版本号、Canal 等变更订阅**等方案，按业务容忍度选。 |
| **穿透 / 击穿 / 雪崩**各一句话？ | **穿透**：大量查**不存在**的 key，缓存挡不住打穿 DB。**击穿**：**热点 key 过期**瞬间并发打 DB。**雪崩**：**大量 key 同时过期**或 Redis 宕机导致集体打 DB。 |

### 6.6 P2：消息队列（RocketMQ / RabbitMQ / Kafka，按项目实际启用为准）

| 迫切问题 | 简明答案 |
|----------|----------|
| **至少一次（at-least-once）** 对消费者意味着什么？ | 同一条消息**可能投递多次**；业务必须 **幂等**（去重键、状态机、唯一约束），不能只依赖「我以为只会来一次」。 |
| **重试**该放在 Broker、客户端还是业务里？ | **Broker/客户端重试**解决**抖动与临时失败**；**业务幂等**解决**重复投递语义**。二者**不能互相替代**。 |
| 消费失败一直重试会把系统拖死吗？ | 会。需 **最大重试次数、死信队列（DLQ）、告警、人工介入**；并避免在消费逻辑里做**无超时**的外部调用。 |
| 本仓库 **租户与 MQ** 可能怎么配合？ | 租户 starter 内含 **RocketMQ / RabbitMQ** 等初始化器（类名含 `TenantRocketMQInitializer`、`TenantRabbitMQInitializer`），目的常是**透传租户上下文**；学习时打开对应类看 **Message 头或 ThreadLocal** 如何设置。 |

### 6.7 P2：定时任务（Quartz）、异步（`@Async`）

| 迫切问题 | 简明答案 |
|----------|----------|
| Quartz Job 里打 **`@Transactional`** 要注意什么？ | Job 是否走 Spring 代理、**线程池线程**是否带事务上下文；异常是否被 Quartz **吞掉**导致你以为提交成功。 |
| `@Async` 默认在什么线程跑？与 HTTP 请求线程关系？ | 默认 **`SimpleAsyncTaskExecutor` 或自定义 Executor**；**与请求线程分离**，故 **Request 作用域 Bean / ThreadLocal** 默认**不会自动传递**，需 **TaskDecorator** 或显式传参。 |

### 6.8 WebSocket（本仓库 starter 与 MQ、Security 相关）

| 迫切问题 | 简明答案 |
|----------|----------|
| WebSocket 握手前的请求会过 **Security** 吗？ | **会**经过与 HTTP 相同的 Filter 链（握手是 HTTP Upgrade）；**握手后**帧走另一套协议栈，认证信息依赖**握手阶段建立的 Principal / Session** 或子协议约定。 |
| 为什么本仓库 **websocket starter 依赖 security**？ | `wmt-spring-boot-starter-websocket` 的 `pom` 注释说明：与 **当前登录用户** 绑定（如 `WebSocketSessionManagerImpl`），需在安全上下文明确后再建连。 |

### 6.9 服务保障（Lock4j + Redisson）

| 迫切问题 | 简明答案 |
|----------|----------|
| **分布式锁**能代替数据库事务吗？ | **不能**。锁解决 **互斥**；事务解决 **ACID 一组写**。常见模式：**短锁保护临界区**，事务尽量短。 |
| 锁应放在事务外还是事务内？ | **常见推荐**：先**短锁**再开事务，或锁粒度尽量小；锁在事务内易导致**锁持有时间过长**、死锁风险上升（依业务具体设计）。 |

### 6.10 P2：可观测（日志、链路、Admin）

| 迫切问题 | 简明答案 |
|----------|----------|
| **日志**和 **Trace（如 SkyWalking）** 分工？ | 日志回答「**某时刻某类里发生了什么**」；Trace 回答「**一次请求跨组件/跨服务的耗时与调用顺序**」。排障常**先看 Trace 定位慢 span，再下钻日志**。 |
| Actuator **health** 与业务接口健康有何不同？ | Actuator 汇报 **进程与依赖**（DB、磁盘等）是否存活；**不能替代**业务压测或业务指标监控。 |

### 6.11 P3：方案与设计（评审时「最低限度要问的」）

| 迫切问题 | 简明答案 |
|----------|----------|
| 评审新接口时最少确认哪三件事？ | **鉴权与数据权限**（谁能调、能看哪些行）、**失败与超时**（降级/重试/错误码）、**数据一致性**（事务边界、是否需幂等与对账）。 |
| 「能上线」与「能扛促销流量」差在哪？ | 前者是**功能与正确性**；后者要补 **容量、限流、缓存、异步化、压测指标、开关与回滚** 等非功能项。 |

### 6.12 映射与构建（MapStruct、Lombok、Maven 注解处理）

| 迫切问题 | 简明答案 |
|----------|----------|
| MapStruct 编译报错「找不到属性」常与什么有关？ | **Lombok 与 MapStruct 处理顺序**：需在 **`annotationProcessorPaths`** 中同时配置 **lombok、lombok-mapstruct-binding、mapstruct-processor**（本仓库根 `pom.xml` 已配置）。 |
| 运行时 MapStruct 有反射性能损耗吗？ | **无（默认）**：生成的是**普通 Java 实现类**，运行时直接调用，非反射映射。 |

---

## 7. 修订记录


| 日期         | 变更                               |
| ---------- | -------------------------------- |
| 2026-04-20 | 初版：固化自评与文档索引、学习优先级           |
| 2026-04-20 | 增补：§6 各主题迫切问题与简明答案（含本仓库对照） |


