# WMT 技术组件库

> WMT技术组件库 - 提供企业级Spring Boot技术组件封装

## 📖 项目介绍

WMT是一个基于Spring Boot的企业级技术组件库，提供了一系列开箱即用的技术组件，帮助开发者快速构建企业级应用。所有组件都经过精心设计和封装，遵循Spring Boot Starter规范，可以通过Maven依赖方式快速集成。

### 特性

- ✨ **开箱即用**：遵循Spring Boot自动配置规范，零配置或少量配置即可使用
- 🔒 **源码保护**：支持代码混淆，保护核心技术不被轻易反编译[4.0.2版本sql](..%2F..%2F..%2F..%2Fdesktop%2FAHC%2F0.%E5%B7%A5%E4%BD%9C%E4%BB%BB%E5%8A%A1%2F0%E8%BF%9B%E8%A1%8C%E6%97%B6%2F202403DevOps%E5%B9%B3%E5%8F%B0%2F2.%E9%A1%B9%E7%9B%AE%E8%BF%90%E7%BB%B4%2F%E6%95%B0%E6%8D%AE%E5%BA%93%E8%A1%A8%2F4.0.2%2F4.0.2%E7%89%88%E6%9C%ACsql)
- 📦 **模块化设计**：各组件独立打包，按需引入，不引入无关依赖
- 🎯 **企业级实践**：基于实际项目经验提炼，满足企业级应用需求
- 📚 **完善文档**：提供详细的使用文档和示例代码

wmt-common (基础层)
    ↓
框架技术组件 (中间层)
    ↓  
业务组件 (应用层)

## 🚀 快速开始

### 环境要求

**JDK 8 版本：**
- JDK 1.8+
- Maven 3.6+
- Spring Boot 2.7.x

**JDK 17 版本：**
- JDK 17+
- Maven 3.6+
- Spring Boot 3.5.x

### 方式一：使用依赖管理（推荐）

在你的Spring Boot项目的`pom.xml`中添加依赖管理：

**JDK 8 版本：**
```xml
<dependencyManagement>
    <dependencies>
        <!-- 引入WMT依赖管理（JDK8版本） -->
        <dependency>
            <groupId>com.wmt</groupId>
            <artifactId>wmt-dependencies</artifactId>
            <version>2025.12-jdk8-SNAPSHOT</version>
            <type>pom</type>
            <scope>import</scope>
        </dependency>
    </dependencies>
</dependencyManagement>
```

**JDK 17 版本：**
```xml
<dependencyManagement>
    <dependencies>
        <!-- 引入WMT依赖管理（JDK17版本） -->
        <dependency>
            <groupId>com.wmt</groupId>
            <artifactId>wmt-dependencies-jdk17</artifactId>
            <version>2025.12-jdk17-SNAPSHOT</version>
            <type>pom</type>
            <scope>import</scope>
        </dependency>
    </dependencies>
</dependencyManagement>
```

然后根据需要引入具体的组件（不需要指定版本）：

```xml
<dependencies>
    <!-- Web组件 -->
    <dependency>
        <groupId>com.wmt</groupId>
        <artifactId>wmt-spring-boot-starter-web</artifactId>
    </dependency>
    
    <!-- MyBatis组件 -->
    <dependency>
        <groupId>com.wmt</groupId>
        <artifactId>wmt-spring-boot-starter-mybatis</artifactId>
    </dependency>
    
    <!-- Redis组件 -->
    <dependency>
        <groupId>com.wmt</groupId>
        <artifactId>wmt-spring-boot-starter-cache</artifactId>
    </dependency>
</dependencies>
```

### 方式二：直接依赖组件

如果您不想使用依赖管理，也可以直接依赖具体组件：

**JDK 8 版本：**
```xml
<dependencies>
    <!-- Web组件 -->
    <dependency>
        <groupId>com.wmt</groupId>
        <artifactId>wmt-spring-boot-starter-web</artifactId>
        <version>2025.12-jdk8-SNAPSHOT</version>
    </dependency>
    
    <!-- MyBatis组件 -->
    <dependency>
        <groupId>com.wmt</groupId>
        <artifactId>wmt-spring-boot-starter-mybatis</artifactId>
        <version>2025.12-jdk8-SNAPSHOT</version>
    </dependency>
</dependencies>
```

**JDK 17 版本：**
```xml
<dependencies>
    <!-- Web组件 -->
    <dependency>
        <groupId>com.wmt</groupId>
        <artifactId>wmt-spring-boot-starter-web</artifactId>
        <version>2025.12-jdk17-SNAPSHOT</version>
    </dependency>
    
    <!-- MyBatis组件 -->
    <dependency>
        <groupId>com.wmt</groupId>
        <artifactId>wmt-spring-boot-starter-mybatis</artifactId>
        <version>2025.12-jdk17-SNAPSHOT</version>
    </dependency>
</dependencies>
```

**推荐使用方式一**，因为：
- 统一管理所有组件版本，避免版本冲突
- 简化pom配置，不需要重复指定版本号
- 便于批量升级组件版本

### 配置示例

在`application.yml`中添加相应的配置：

```yaml
spring:
  application:
    name: your-application
    
# 根据引入的组件添加对应的配置
```

## 📦 组件清单

### 基础组件

| 组件名称 | 说明 | Maven坐标 |
|---------|------|----------|
| wmt-common | 公共工具类和基础组件（POJO、异常、枚举、工具类等） | `com.wmt:wmt-common` |

### 框架技术组件

| 组件名称 | 说明 | Maven坐标 |
|---------|------|----------|
| wmt-spring-boot-starter-web | Web增强组件（统一异常处理、参数校验、API日志等） | `com.wmt:wmt-spring-boot-starter-web` |
| wmt-spring-boot-starter-mybatis | MyBatis增强组件（分页、数据源等） | `com.wmt:wmt-spring-boot-starter-mybatis` |
| wmt-spring-boot-starter-cache | Redis组件（缓存、分布式锁等） | `com.wmt:wmt-spring-boot-starter-cache` |
| wmt-spring-boot-starter-redis | Redis组件（兼容旧版本） | `com.wmt:wmt-spring-boot-starter-redis` |
| wmt-spring-boot-starter-security | 安全组件（认证、授权等） | `com.wmt:wmt-spring-boot-starter-security` |
| wmt-spring-boot-starter-websocket | WebSocket组件 | `com.wmt:wmt-spring-boot-starter-websocket` |
| wmt-spring-boot-starter-monitor | 监控组件（链路追踪等） | `com.wmt:wmt-spring-boot-starter-monitor` |
| wmt-spring-boot-starter-prometheus-grafana | Prometheus和Grafana监控组件 | `com.wmt:wmt-spring-boot-starter-prometheus-grafana` |
| wmt-spring-boot-starter-protection | 服务保护组件（限流、熔断、幂等等） | `com.wmt:wmt-spring-boot-starter-protection` |
| wmt-spring-boot-starter-job | 定时任务组件（基于Quartz） | `com.wmt:wmt-spring-boot-starter-job` |
| wmt-spring-boot-starter-xxljob | XXL-JOB集成组件 | `com.wmt:wmt-spring-boot-starter-xxljob` |
| wmt-spring-boot-starter-mq | 消息队列组件 | `com.wmt:wmt-spring-boot-starter-mq` |
| wmt-spring-boot-starter-excel | Excel导入导出组件 | `com.wmt:wmt-spring-boot-starter-excel` |
| wmt-spring-boot-starter-test | 测试组件 | `com.wmt:wmt-spring-boot-starter-test` |
| wmt-spring-boot-starter-elk-logging | ELK日志组件 | `com.wmt:wmt-spring-boot-starter-elk-logging` |
| wmt-spring-boot-starter-sjb | SJB组件 | `com.wmt:wmt-spring-boot-starter-sjb` |

### 业务技术组件

| 组件名称 | 说明 | Maven坐标 |
|---------|------|----------|
| wmt-spring-boot-starter-biz-tenant | 多租户组件 | `com.wmt:wmt-spring-boot-starter-biz-tenant` |
| wmt-spring-boot-starter-biz-data-permission | 数据权限组件 | `com.wmt:wmt-spring-boot-starter-biz-data-permission` |
| wmt-spring-boot-starter-biz-ip | IP地理位置组件 | `com.wmt:wmt-spring-boot-starter-biz-ip` |

## 💡 使用示例

### Web组件使用

引入Web组件后，自动获得以下功能：

1. **统一异常处理**：自动处理全局异常并返回统一的错误响应
2. **API日志记录**：自动记录所有API请求和响应
3. **参数校验增强**：基于JSR-303的参数校验
4. **跨域配置**：支持跨域请求配置

```java
@RestController
@RequestMapping("/api/user")
public class UserController {
    
    @GetMapping("/{id}")
    public CommonResult<UserVO> getUser(@PathVariable Long id) {
        // 业务逻辑
        return CommonResult.success(userVO);
    }
    
    @PostMapping
    public CommonResult<Long> createUser(@Valid @RequestBody UserCreateDTO dto) {
        // 参数自动校验
        return CommonResult.success(userId);
    }
}
```

### Redis组件使用

```java
@Service
public class UserService {
    
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;
    
    public void cacheUser(Long userId, User user) {
        redisTemplate.opsForValue().set("user:" + userId, user, 1, TimeUnit.HOURS);
    }
    
    public User getUser(Long userId) {
        return (User) redisTemplate.opsForValue().get("user:" + userId);
    }
}
```

### 数据权限组件使用

```java
@Service
public class UserService {
    
    @DataPermission(enable = true) // 启用数据权限
    public List<User> getUserList() {
        // 自动根据当前用户的数据权限过滤数据
        return userMapper.selectList();
    }
}
```

### 多租户组件使用

```yaml
# application.yml
wmt:
  tenant:
    enable: true
    ignore-tables:
      - sys_config
      - sys_dict
```

```java
@Service
public class OrderService {
    
    public List<Order> getOrderList() {
        // 自动根据当前租户ID过滤数据
        return orderMapper.selectList();
    }
    
    @TenantIgnore // 忽略租户隔离
    public List<Order> getAllOrders() {
        return orderMapper.selectList();
    }
}
```

## 🔧 本地安装与部署

### 本地安装到Maven仓库

```bash
# 克隆代码
git clone https://github.com/Wmt/wmt-framework.git
cd wmt-framework

# 安装到本地仓库
mvn clean install -DskipTests
```

### 部署到私有Maven仓库

1. 配置Maven仓库地址（在`pom.xml`的`distributionManagement`节点）

2. 配置认证信息（在`~/.m2/settings.xml`中）

```bash
# 参考 settings.xml.example 文件配置你的settings.xml
cp settings.xml.example ~/.m2/settings.xml
# 编辑 ~/.m2/settings.xml，填入实际的仓库地址和认证信息
```

3. 执行部署

```bash
# Linux/Mac
./deploy.sh

# Windows
deploy.bat

# 使用代码混淆方式部署
./deploy.sh obfuscate
```

### 发布到Maven中央仓库

```bash
# 使用release profile（包含GPG签名）
./deploy.sh release
```
## 支持版本


### 中间件版本范围清单

| 组件名称                                                    | BOM 中使用的客户端/框架版本 | 建议服务端中间件版本范围                                                                          | 说明 / 依据                                                                                                                    |
| ------------------------------------------------------- | ---------------- | ------------------------------------------------------------------------------------- | -------------------------------------------------------------------------------------------------------------------------- |
| Redis 客户端（Redisson v3.51.0）                             | 3.51.0           | **Redis 服务端 ≥ 3.0**（推荐 5.x、6.x、7.x）                                                   | 官方说明：“Redis compatible – from 3.0 up to the latest version”([Redisson][1])。因此你可以放心使用 Redis 3.0 以上版本。建议在生产中使用主流的 6.x 或 7.x。 |
| MyBatis-Plus（v3.5.14）                                   | 3.5.14           | 关系型数据库（MySQL、PostgreSQL、Oracle、SQL Server 等）主流版本（如 MySQL 5.7/8.0、PostgreSQL 12/13/14） | 虽然 MyBatis-Plus 本身并不限制数据库版本，但因其基于 MyBatis + JDBC 驱动，建议使用较新数据库版本以避免老版本驱动或兼容问题。 MyBatis 官方支持 JDK8+、现代数据库。([mybatis.org][2])  |
| Flowable 工作流引擎 v6.8.0                                   | 6.8.0            | 支持 Spring Boot 2.7.x，对应关系型数据库主流版本                                                     | 官方 Release 中指出 6.8.0 是基于 Spring Boot 2.7.6([GitHub][3])。建议选用兼容 Spring Boot 2.7.x 的中间件栈。                                    |
| Druid 数据库连接池 v1.2.27                                    | 1.2.27           | 支持 JDBC 驱动所对应的数据库版本                                                                   | Druid 是连接池，并不强限定数据库版本，但需确保 JDBC 驱动与数据库匹配。                                                                                  |
| Dynamic‑Datasource‑Spring‑Boot‑Starter v4.3.1           | 4.3.1            | 多数据源支持任意主流数据库版本                                                                       | 该组件支持多数据源切换，适用范围较宽，但建议数据库版本与主 JDBC 驱动版本兼容。                                                                                 |
| XXL‑Job v2.4.0                                          | 2.4.0            | 任意可运行 Java 8 的环境 + 支持数据库（如 MySQL）                                                     | XXL-Job 本身对数据库版本要求较低，但要确保 JDBC 驱动兼容。                                                                                       |
| Apache RocketMQ Spring Boot‑Starter v2.3.4              | 2.3.4            | 建议使用 RocketMQ 服务端相匹配的 4.x/5.x 版本                                                      | 虽然你使用的是 Spring Boot Starter 客户端版本，但服务端版本建议与客户端成熟配套。需查阅 RocketMQ 官方兼容矩阵。                                                    |
| 日志/监控/追踪相关（如 SkyWalking v8.12.0）                        | 8.12.0           | SkyWalking 服务端建议使用对应 8.x 系列版本                                                         | 建议客户端和服务端版本在同一大版本系列。                                                                                                       |
| 其他辅助工具（如 JSoup 1.21.2、Guava 33.4.8-jre、Hutool 5.8.40 等） | —                | 辅助工具库，无服务端中间件版本需求                                                                     | 这些属于纯 Java 库，不涉及服务端中间件版本兼容问题。                                                                                              |

---

### 关于 Java / Spring Boot 版本范围（你提到还需确定）

* BOM 中指定：`spring.boot.version = 2.7.18`，`spring.framework.version = 5.3.39`。
  因此建议使用 **Spring Boot 2.7.x 系列（2.7.18 为基准）**，对应 Spring Framework 5.3.x 系列。
* Java 建议使用 JDK 8 （你已指定 JDK 8）。在 JDK 8 环境下，以上版本组合是可行的。
* 注意：某些中间件或库可能未来要求 JDK 11+，但当前你环境为 JDK8，应重点选择支持 JDK 8 的版本。

---


## 🔐 源码保护

为了保护技术组件的源代码不被轻易反编译，项目提供了代码混淆功能：

### 启用代码混淆

```bash
# 使用混淆方式打包
mvn clean package -Pobfuscate -DskipTests=true

# 使用混淆方式部署
mvn deploy -Pobfuscate -DskipTests=true
```

### 混淆配置

混淆配置文件位于项目根目录的`proguard.conf`，你可以根据需要调整混淆规则。

**注意**：
- 混淆后的jar包依然可以正常使用，不影响功能
- 保留了所有公共API，确保外部调用不受影响
- 保留了Spring相关注解和配置，确保自动配置正常工作
- 反编译后的代码可读性大大降低，有效保护核心逻辑

## 📋 版本规范

**JDK 8 版本：**
- `x.x-jdk8-SNAPSHOT`：快照版本，用于开发和测试
- `x.x-jdk8`：正式版本，用于生产环境
- 当前版本：`2025.12-jdk8-SNAPSHOT`

**JDK 17 版本：**
- `x.x-jdk17-SNAPSHOT`：快照版本，用于开发和测试
- `x.x-jdk17`：正式版本，用于生产环境
- 当前版本：`2025.12-jdk17-SNAPSHOT`

**注意：** JDK 8 和 JDK 17 版本使用不同的根POM文件：
- JDK 8 版本：使用 `pom.xml`
- JDK 17 版本：使用 `pom-jdk17.xml`

## 🔨 组件开发指南

如果您需要为WMT框架开发新组件，请参阅 [组件开发指南](docs/wmt/COMPONENT_DEVELOPMENT.md)

## 🤝 贡献指南

欢迎提交Issue和Pull Request！

## 📄 许可证

本项目采用 [LICENSE](LICENSE) 许可证。

## 📞 联系方式

如有问题或建议，请通过以下方式联系：

- 提交Issue：https://github.com/Wmt/wmt-framework/issues
- 邮箱：wangmingteng@mail.ustc.edu.cn

## 🙏 致谢

感谢所有为本项目做出贡献的开发者！
