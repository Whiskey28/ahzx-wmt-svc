# WMT XXL-Job 组件

## 概述

WMT XXL-Job 组件提供了基于 XXL-Job 的分布式任务调度能力，支持任务管理、监控和调度。本组件已与 Job 模块解耦，可独立使用。

## 功能特性

- ✅ **独立模块**：与 Job 模块解耦，可独立使用
- ✅ **自动配置**：基于 Spring Boot 的自动配置机制
- ✅ **参数支持**：支持简单参数和 JSON 复杂参数
- ✅ **日志记录**：集成 XXL-Job 日志系统
- ✅ **异常处理**：完善的异常处理机制
- ✅ **监控支持**：支持任务执行监控和告警
- ✅ **配置验证**：启动时自动验证必填配置项，缺失时记录警告日志
- ✅ **执行器工厂**：抽象执行器创建逻辑，支持执行器生命周期管理
- ✅ **健康检查**：集成 Spring Boot Actuator，提供执行器健康状态检查
- ✅ **扩展能力**：预留多执行器支持，便于后续扩展

## 快速开始

### 1. 添加依赖

```xml
<dependency>
    <groupId>com.wmt</groupId>
    <artifactId>wmt-spring-boot-starter-xxljob</artifactId>
    <version>${wmt.version}</version>
</dependency>
```

### 2. 配置 XXL-Job

```yaml
xxl:
  job:
    enabled: true
    access-token: your-access-token
    admin:
      addresses: http://your-xxljob-admin:8088/xxl-job-admin
    executor:
      appname: your-app-name
      port: 9999
      logpath: ./logs/xxl-job
      logretentiondays: 30
```

### 3. 创建任务

```java
@Component
@Slf4j
public class UserSyncJob {
    
    @XxlJob("userSyncJob")
    public void execute() {
        String param = XxlJobHelper.getJobParam();
        XxlJobHelper.log("任务开始执行，参数：{}", param);
        
        try {
            // 业务逻辑
            doBusinessLogic(param);
            XxlJobHelper.log("任务执行成功");
        } catch (Exception e) {
            XxlJobHelper.log("任务执行失败：{}", e.getMessage());
            throw e;
        }
    }
    
    private void doBusinessLogic(String param) {
        // 具体的业务逻辑
    }
}
```

### 4. 在 XXL-Job 管理台配置任务

1. 登录 XXL-Job 管理台
2. 进入"任务管理"
3. 添加任务：
   - 执行器：选择你的应用
   - 任务描述：用户数据同步
   - 路由策略：第一个
   - Cron：`0 0 2 * * ?`
   - 运行模式：BEAN
   - JobHandler：`userSyncJob`
   - 执行参数：`{"source": "external_system"}`

## 配置说明

### 基础配置

| 配置项 | 说明 | 默认值 | 必填 |
|--------|------|--------|------|
| `xxl.job.enabled` | 是否启用 XXL-Job | `false` | 是 |
| `xxl.job.access-token` | 访问令牌 | 无 | 否 |
| `xxl.job.admin.addresses` | 调度中心地址 | 无 | 是 |
| `xxl.job.executor.appname` | 执行器应用名 | 无 | 是 |
| `xxl.job.executor.port` | 执行器端口 | `9999` | 是 |
| `xxl.job.executor.logpath` | 日志路径 | `./logs/xxl-job` | 是 |
| `xxl.job.executor.logretentiondays` | 日志保留天数 | `30` | 否 |
| `xxl.job.executor.address` | 执行器注册地址（为空则自动获取） | 无 | 否 |
| `xxl.job.executor.ip` | 执行器IP（为空则自动获取） | 无 | 否 |

### 配置验证

组件在启动时会自动验证必填配置项。如果必填项未配置，会记录警告日志，但不会阻止应用启动。建议在生产环境中确保所有必填项都已正确配置。

**必填配置项**：
- `xxl.job.enabled`：必须为 `true` 才能启用组件
- `xxl.job.admin.addresses`：调度中心地址，未配置会导致执行器无法连接
- `xxl.job.executor.appname`：执行器应用名，未配置会导致执行器注册失败
- `xxl.job.executor.port`：执行器端口，未配置或无效会使用默认值 9999
- `xxl.job.executor.logpath`：日志路径，未配置会使用默认值 `./logs/xxl-job`

### 完整配置示例

```yaml
xxl:
  job:
    enabled: true
    access-token: default_token
    admin:
      addresses: http://127.0.0.1:8088/xxl-job-admin
    executor:
      appname: wmt-demo-executor
      address: 
      ip: 
      port: 9999
      logpath: ./logs/xxl-job
      logretentiondays: 30
```

## 任务开发规范

### 1. 基础模板

```java
@Component
@Slf4j
public class YourJob {
    
    @XxlJob("yourJobHandler")
    public void execute() {
        String param = XxlJobHelper.getJobParam();
        XxlJobHelper.log("任务开始执行，参数：{}", param);
        
        try {
            // 1. 参数验证
            validateParams(param);
            
            // 2. 业务逻辑
            doBusinessLogic(param);
            
            // 3. 成功日志
            XxlJobHelper.log("任务执行成功");
            
        } catch (Exception e) {
            // 4. 异常处理
            XxlJobHelper.log("任务执行失败：{}", e.getMessage());
            throw e;
        }
    }
}
```

### 2. 参数处理

#### 简单参数
```java
String param = XxlJobHelper.getJobParam();
```

#### JSON 参数
```java
String param = XxlJobHelper.getJobParam();
JSONObject params = JSON.parseObject(param);
String userId = params.getString("userId");
```

### 3. 日志记录

```java
// 任务开始
XxlJobHelper.log("任务开始执行");

// 进度日志
XxlJobHelper.log("处理进度：{}/{}", processed, total);

// 成功日志
XxlJobHelper.log("任务执行成功");

// 错误日志
XxlJobHelper.log("任务执行失败：{}", e.getMessage());
```

### 4. 异常处理

```java
try {
    doBusinessLogic();
} catch (IllegalArgumentException e) {
    // 参数错误，不重试
    XxlJobHelper.log("参数错误：{}", e.getMessage());
    throw e;
} catch (Exception e) {
    // 系统异常，可重试
    XxlJobHelper.log("系统异常：{}", e.getMessage());
    throw e;
}
```

## 从 Job 模块迁移

如果你之前使用 Job 模块，可以参考 [迁移指南](MIGRATION_GUIDE.md) 进行迁移。

### 主要变更

1. **依赖变更**：移除 `wmt-spring-boot-starter-job`，添加 `wmt-spring-boot-starter-xxljob`
2. **任务实现**：从 `JobHandler` 接口改为 `@XxlJob` 注解
3. **参数获取**：从方法参数改为 `XxlJobHelper.getJobParam()`
4. **日志记录**：从框架自动记录改为 `XxlJobHelper.log()`
5. **任务注册**：从代码注册改为管理台配置

## 健康检查

组件集成了 Spring Boot Actuator 的健康检查功能，可以通过 `/actuator/health` 端点查看执行器的健康状态。

### 启用健康检查

1. 添加 Spring Boot Actuator 依赖（如果项目中还没有）：

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-actuator</artifactId>
</dependency>
```

2. 配置 Actuator 端点：

```yaml
management:
  endpoints:
    web:
      exposure:
        include: health
  endpoint:
    health:
      show-details: when-authorized
```

### 健康检查信息

访问 `/actuator/health` 端点，可以看到以下信息：

```json
{
  "status": "UP",
  "components": {
    "xxlJobHealthIndicator": {
      "status": "UP",
      "details": {
        "initialized": true,
        "appname": "wmt-demo-executor",
        "port": 9999,
        "adminAddresses": "http://127.0.0.1:8088/xxl-job-admin",
        "logPath": "./logs/xxl-job",
        "connected": true
      }
    }
  }
}
```

**健康状态说明**：
- `UP`：执行器已初始化且连接正常
- `DOWN`：执行器未初始化或连接异常

**检查项**：
- `initialized`：执行器是否已初始化
- `appname`：执行器应用名
- `port`：执行器端口
- `adminAddresses`：调度中心地址
- `logPath`：日志路径
- `connected`：是否连接到调度中心

## 最佳实践

1. **任务命名**：使用驼峰命名法，如 `userSyncJob`
2. **参数验证**：始终验证输入参数的有效性
3. **异常处理**：合理处理异常，提供有意义的错误信息
4. **日志记录**：记录关键信息，便于问题排查
5. **性能监控**：监控执行时间和资源使用
6. **测试覆盖**：编写充分的单元测试
7. **配置验证**：确保所有必填配置项都已正确配置
8. **健康检查**：定期检查执行器健康状态，及时发现问题

## 示例项目

参考 `wmt-demo` 项目中的 `DemoJob` 类，了解完整的使用示例。

## 文档

- [迁移指南](MIGRATION_GUIDE.md) - 从 Job 模块迁移到 XXL-Job
- [开发规范](XXLJOB_STANDARDS.md) - XXL-Job 任务开发规范
- [示例代码](src/main/java/com/wmt/framework/xxljob/example/UserSyncJob.java) - 完整示例

## 注意事项

1. **调度中心**：确保 XXL-Job 管理台正常运行
2. **访问令牌**：如果调度中心启用了令牌验证，需要配置正确的访问令牌
3. **应用名一致性**：执行器应用名必须与管理台注册的名称一致
4. **任务方法**：任务方法必须是 `public` 且无参数
5. **JobHandler 名称**：使用 `@XxlJob` 注解指定 JobHandler 名称，需与管理台配置一致
6. **配置验证**：启动时会自动验证必填配置项，缺失时会记录警告日志
7. **健康检查**：建议启用 Spring Boot Actuator 的健康检查功能，便于监控执行器状态
8. **日志路径**：确保日志路径有写入权限，否则可能导致任务执行失败

## 技术支持

如有问题，请参考：
- [XXL-Job 官方文档](https://www.xuxueli.com/xxl-job/)
- [WMT 框架文档](../README.md)
