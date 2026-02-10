# XXL-Job 任务开发规范

## 概述

本文档定义了基于WMT框架的XXL-Job任务开发规范，确保任务代码的一致性、可维护性和可监控性。

## 1. 基础规范

### 1.1 包结构规范

```
com.yourcompany.job
├── handler/          # 任务处理器
│   ├── UserSyncJob.java
│   ├── OrderProcessJob.java
│   └── DataCleanJob.java
├── config/           # 任务相关配置
└── util/            # 任务工具类
```

### 1.2 命名规范

- **类名**：使用`XxxJob`格式，如`UserSyncJob`、`OrderProcessJob`
- **方法名**：使用`execute`作为主要执行方法
- **JobHandler名称**：使用驼峰命名法，如`userSyncJob`、`orderProcessJob`

## 2. 任务实现规范

### 2.1 基础模板

```java
@Component
@Slf4j
public class UserSyncJob {
    
    @Autowired
    private UserService userService;
    
    @XxlJob("userSyncJob")
    public void execute() {
        String param = XxlJobHelper.getJobParam();
        XxlJobHelper.log("任务开始执行，参数：{}", param);
        
        try {
            // 1. 参数验证
            validateParams(param);
            
            // 2. 业务逻辑执行
            doBusinessLogic(param);
            
            // 3. 记录成功日志
            XxlJobHelper.log("任务执行成功");
            
        } catch (Exception e) {
            // 4. 异常处理和日志记录
            XxlJobHelper.log("任务执行失败：{}", e.getMessage());
            throw e;
        }
    }
    
    private void validateParams(String param) {
        if (StringUtils.isBlank(param)) {
            throw new IllegalArgumentException("任务参数不能为空");
        }
    }
    
    private void doBusinessLogic(String param) {
        // 具体的业务逻辑实现
    }
}
```

### 2.2 参数处理规范

#### 2.2.1 简单参数
```java
@XxlJob("simpleJob")
public void execute() {
    String param = XxlJobHelper.getJobParam();
    // 直接使用参数
}
```

#### 2.2.2 JSON参数
```java
@XxlJob("complexJob")
public void execute() {
    String param = XxlJobHelper.getJobParam();
    
    try {
        JSONObject params = JSON.parseObject(param);
        String userId = params.getString("userId");
        String action = params.getString("action");
        
        // 使用解析后的参数
    } catch (JSONException e) {
        XxlJobHelper.log("参数解析失败：{}", e.getMessage());
        throw new IllegalArgumentException("参数格式错误");
    }
}
```

#### 2.2.3 参数验证
```java
private void validateParams(String param) {
    if (StringUtils.isBlank(param)) {
        throw new IllegalArgumentException("任务参数不能为空");
    }
    
    try {
        JSONObject params = JSON.parseObject(param);
        if (!params.containsKey("requiredField")) {
            throw new IllegalArgumentException("缺少必需参数：requiredField");
        }
    } catch (JSONException e) {
        throw new IllegalArgumentException("参数格式错误，必须是有效的JSON");
    }
}
```

## 3. 日志记录规范

### 3.1 日志级别使用

```java
@XxlJob("logExampleJob")
public void execute() {
    // 任务开始
    XxlJobHelper.log("任务开始执行");
    
    try {
        // 关键步骤
        XxlJobHelper.log("开始处理数据，数量：{}", dataCount);
        
        // 业务逻辑
        processData();
        
        // 成功完成
        XxlJobHelper.log("任务执行成功，处理数据：{}条", processedCount);
        
    } catch (Exception e) {
        // 错误日志
        XxlJobHelper.log("任务执行失败：{}", e.getMessage());
        throw e;
    }
}
```

### 3.2 日志内容规范

- **开始日志**：记录任务开始执行
- **进度日志**：记录关键步骤和进度
- **结果日志**：记录处理结果和统计信息
- **错误日志**：记录异常信息和错误原因

## 4. 异常处理规范

### 4.1 异常分类处理

```java
@XxlJob("exceptionHandlingJob")
public void execute() {
    try {
        // 业务逻辑
        doBusinessLogic();
        
    } catch (IllegalArgumentException e) {
        // 参数错误，记录日志但不重试
        XxlJobHelper.log("参数错误：{}", e.getMessage());
        throw e;
        
    } catch (BusinessException e) {
        // 业务异常，可能需要重试
        XxlJobHelper.log("业务处理失败：{}", e.getMessage());
        throw e;
        
    } catch (Exception e) {
        // 系统异常，记录详细错误信息
        XxlJobHelper.log("系统异常：{}", e.getMessage());
        log.error("任务执行异常", e);
        throw e;
    }
}
```

### 4.2 重试机制

```java
@XxlJob("retryJob")
public void execute() {
    int maxRetries = 3;
    int retryCount = 0;
    
    while (retryCount < maxRetries) {
        try {
            doBusinessLogic();
            break; // 成功则跳出循环
            
        } catch (Exception e) {
            retryCount++;
            XxlJobHelper.log("第{}次尝试失败：{}", retryCount, e.getMessage());
            
            if (retryCount >= maxRetries) {
                XxlJobHelper.log("重试次数已达上限，任务失败");
                throw e;
            }
            
            // 等待后重试
            try {
                Thread.sleep(1000 * retryCount);
            } catch (InterruptedException ie) {
                Thread.currentThread().interrupt();
                throw new RuntimeException("任务被中断", ie);
            }
        }
    }
}
```

## 5. 性能优化规范

### 5.1 大数据量处理

```java
@XxlJob("batchProcessJob")
public void execute() {
    String param = XxlJobHelper.getJobParam();
    JSONObject params = JSON.parseObject(param);
    int batchSize = params.getIntValue("batchSize");
    int totalCount = getTotalCount();
    
    int processedCount = 0;
    int pageNum = 1;
    
    while (processedCount < totalCount) {
        // 分批处理
        List<Data> batchData = getBatchData(pageNum, batchSize);
        
        if (batchData.isEmpty()) {
            break;
        }
        
        processBatch(batchData);
        processedCount += batchData.size();
        
        XxlJobHelper.log("已处理：{}/{}", processedCount, totalCount);
        pageNum++;
    }
    
    XxlJobHelper.log("批量处理完成，总计：{}条", processedCount);
}
```

### 5.2 内存管理

```java
@XxlJob("memoryOptimizedJob")
public void execute() {
    try {
        // 处理逻辑
        doBusinessLogic();
        
    } finally {
        // 清理资源
        clearResources();
        
        // 建议垃圾回收（谨慎使用）
        System.gc();
    }
}

private void clearResources() {
    // 清理大对象引用
    // 关闭数据库连接
    // 清理缓存等
}
```

## 6. 监控和告警规范

### 6.1 执行时间监控

```java
@XxlJob("monitoredJob")
public void execute() {
    long startTime = System.currentTimeMillis();
    XxlJobHelper.log("任务开始执行，时间：{}", new Date(startTime));
    
    try {
        doBusinessLogic();
        
        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;
        
        XxlJobHelper.log("任务执行完成，耗时：{}ms", duration);
        
        // 性能告警
        if (duration > 300000) { // 5分钟
            XxlJobHelper.log("警告：任务执行时间过长，耗时：{}ms", duration);
        }
        
    } catch (Exception e) {
        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;
        XxlJobHelper.log("任务执行失败，耗时：{}ms，错误：{}", duration, e.getMessage());
        throw e;
    }
}
```

### 6.2 业务指标监控

```java
@XxlJob("businessMetricsJob")
public void execute() {
    int successCount = 0;
    int failureCount = 0;
    
    try {
        List<Task> tasks = getTasks();
        
        for (Task task : tasks) {
            try {
                processTask(task);
                successCount++;
            } catch (Exception e) {
                failureCount++;
                XxlJobHelper.log("任务处理失败：{}", task.getId());
            }
        }
        
        XxlJobHelper.log("任务执行统计 - 成功：{}，失败：{}，总计：{}", 
                        successCount, failureCount, tasks.size());
        
        // 失败率告警
        double failureRate = (double) failureCount / tasks.size();
        if (failureRate > 0.1) { // 失败率超过10%
            XxlJobHelper.log("警告：任务失败率过高，失败率：{:.2%}", failureRate);
        }
        
    } catch (Exception e) {
        XxlJobHelper.log("任务执行异常：{}", e.getMessage());
        throw e;
    }
}
```

## 7. 配置管理规范

### 7.1 任务配置

```java
@Component
@ConfigurationProperties(prefix = "job.user-sync")
@Data
public class UserSyncJobConfig {
    private int batchSize = 100;
    private int maxRetries = 3;
    private long retryInterval = 1000;
    private boolean enableNotification = true;
}
```

### 7.2 使用配置

```java
@Component
@Slf4j
public class UserSyncJob {
    
    @Autowired
    private UserSyncJobConfig config;
    
    @XxlJob("userSyncJob")
    public void execute() {
        XxlJobHelper.log("任务配置 - 批次大小：{}，最大重试：{}", 
                        config.getBatchSize(), config.getMaxRetries());
        
        // 使用配置参数
        processDataInBatches(config.getBatchSize());
    }
}
```

## 8. 测试规范

### 8.1 单元测试

```java
@SpringBootTest
class UserSyncJobTest {
    
    @Autowired
    private UserSyncJob userSyncJob;
    
    @Test
    void testExecuteWithValidParam() {
        // 准备测试数据
        String param = "{\"userId\":\"123\"}";
        
        // 模拟XXL-Job参数
        XxlJobHelper.setJobParam(param);
        
        // 执行任务
        assertDoesNotThrow(() -> userSyncJob.execute());
    }
    
    @Test
    void testExecuteWithInvalidParam() {
        // 测试异常情况
        String param = "";
        XxlJobHelper.setJobParam(param);
        
        assertThrows(IllegalArgumentException.class, () -> userSyncJob.execute());
    }
}
```

### 8.2 集成测试

```java
@SpringBootTest
@TestPropertySource(properties = {
    "xxl.job.enabled=true",
    "xxl.job.executor.appname=test-app"
})
class UserSyncJobIntegrationTest {
    
    @Test
    void testJobExecution() {
        // 集成测试逻辑
    }
}
```

## 9. 部署和运维规范

### 9.1 环境配置

```yaml
# 开发环境
xxl:
  job:
    enabled: true
    admin:
      addresses: http://dev-xxljob:8088/xxl-job-admin
    executor:
      appname: user-service-dev

# 生产环境
xxl:
  job:
    enabled: true
    admin:
      addresses: http://prod-xxljob:8088/xxl-job-admin
    executor:
      appname: user-service-prod
      logpath: /app/logs/xxl-job
      logretentiondays: 30
```

### 9.2 监控配置

```yaml
# 任务监控配置
job:
  user-sync:
    batch-size: 100
    max-retries: 3
    timeout: 300000  # 5分钟超时
    alert:
      enabled: true
      failure-rate-threshold: 0.1
      execution-time-threshold: 300000
```

## 10. 最佳实践总结

1. **任务单一职责**：每个任务只做一件事，保持简单
2. **参数验证**：始终验证输入参数的有效性
3. **异常处理**：合理处理异常，提供有意义的错误信息
4. **日志记录**：记录关键信息，便于问题排查
5. **性能监控**：监控执行时间和资源使用
6. **配置管理**：使用配置文件管理任务参数
7. **测试覆盖**：编写充分的单元测试和集成测试
8. **文档维护**：及时更新任务文档和注释

遵循这些规范，可以确保XXL-Job任务的高质量、可维护性和可监控性。
