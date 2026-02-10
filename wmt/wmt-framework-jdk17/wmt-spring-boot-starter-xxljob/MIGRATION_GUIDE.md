# Job模块到XXL-Job迁移指南

## 概述

本指南帮助你将现有的基于WMT Job模块的定时任务迁移到XXL-Job。迁移后，你将获得更好的任务管理、监控和调度能力。

## 迁移步骤

### 1. 依赖变更

**移除依赖：**
```xml
<!-- 移除job模块依赖 -->
<!-- <dependency>
    <groupId>com.wmt</groupId>
    <artifactId>wmt-spring-boot-starter-job</artifactId>
</dependency> -->
```

**添加依赖：**
```xml
<!-- 添加xxljob模块依赖 -->
<dependency>
    <groupId>com.wmt</groupId>
    <artifactId>wmt-spring-boot-starter-xxljob</artifactId>
</dependency>
```

### 2. 任务实现迁移

#### 2.1 原有JobHandler实现

**原有方式（Job模块）：**
```java
@Component
public class UserSyncJobHandler implements JobHandler {
    
    @Override
    public String execute(String param) throws Exception {
        // 业务逻辑
        log.info("开始同步用户数据，参数：{}", param);
        
        // 执行具体的业务逻辑
        syncUserData(param);
        
        return "用户数据同步完成";
    }
    
    private void syncUserData(String param) {
        // 具体的业务逻辑
    }
}
```

#### 2.2 迁移后的XXL-Job实现

**新方式（XXL-Job）：**
```java
@Component
@Slf4j
public class UserSyncJob {
    
    @XxlJob("userSyncJob")
    public void execute() {
        // 获取任务参数
        String param = XxlJobHelper.getJobParam();
        
        log.info("开始同步用户数据，参数：{}", param);
        
        try {
            // 执行具体的业务逻辑
            syncUserData(param);
            
            // 记录成功日志
            XxlJobHelper.log("用户数据同步完成");
        } catch (Exception e) {
            // 记录错误日志
            XxlJobHelper.log("用户数据同步失败：{}", e.getMessage());
            throw e;
        }
    }
    
    private void syncUserData(String param) {
        // 具体的业务逻辑
    }
}
```

### 3. 配置变更

#### 3.1 移除Job模块配置

**移除以下配置：**
```yaml
# 移除job模块相关配置
# wmt:
#   job:
#     enabled: true
```

#### 3.2 添加XXL-Job配置

**添加XXL-Job配置：**
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

### 4. 任务注册变更

#### 4.1 原有方式（Job模块）

**通过SchedulerManager注册：**
```java
@Autowired
private SchedulerManager schedulerManager;

// 添加任务
schedulerManager.addJob(
    jobId, 
    "userSyncJobHandler",  // JobHandler的bean名称
    "syncParam",          // 参数
    "0 0 2 * * ?",        // cron表达式
    3,                    // 重试次数
    60                    // 重试间隔
);
```

#### 4.2 新方式（XXL-Job）

**通过XXL-Job管理台注册：**
1. 登录XXL-Job管理台
2. 进入"任务管理"
3. 添加任务：
   - 执行器：选择你的应用
   - 任务描述：用户数据同步
   - 路由策略：第一个
   - Cron：0 0 2 * * ?
   - 运行模式：BEAN
   - JobHandler：userSyncJob
   - 执行参数：syncParam

### 5. 日志记录变更

#### 5.1 原有方式（Job模块）

```java
// 通过JobLogFrameworkService记录日志
// 框架自动处理
```

#### 5.2 新方式（XXL-Job）

```java
@XxlJob("userSyncJob")
public void execute() {
    try {
        // 业务逻辑
        XxlJobHelper.log("任务开始执行");
        
        // 执行具体逻辑
        doBusinessLogic();
        
        XxlJobHelper.log("任务执行成功");
    } catch (Exception e) {
        XxlJobHelper.log("任务执行失败：{}", e.getMessage());
        throw e;
    }
}
```

## 迁移检查清单

### 代码层面
- [ ] 移除所有JobHandler实现类
- [ ] 创建对应的@XxlJob方法
- [ ] 更新参数获取方式（XxlJobHelper.getJobParam()）
- [ ] 更新日志记录方式（XxlJobHelper.log()）
- [ ] 移除SchedulerManager相关代码

### 配置层面
- [ ] 移除job模块依赖
- [ ] 添加xxljob模块依赖
- [ ] 更新配置文件
- [ ] 配置XXL-Job管理台连接

### 部署层面
- [ ] 部署XXL-Job管理台
- [ ] 在管理台注册执行器
- [ ] 创建对应的任务配置
- [ ] 测试任务执行

## 常见问题

### Q1: 如何处理任务参数？
**A:** 使用`XxlJobHelper.getJobParam()`获取参数，支持JSON格式的复杂参数。

### Q2: 如何处理异常？
**A:** 在方法中抛出异常，XXL-Job会自动记录失败状态。使用`XxlJobHelper.log()`记录详细日志。

### Q3: 如何实现任务重试？
**A:** 在XXL-Job管理台配置重试次数，或使用`XxlJobHelper.handleFail()`手动处理失败。

### Q4: 如何监控任务执行？
**A:** 通过XXL-Job管理台的"调度日志"查看任务执行历史和状态。

## 最佳实践

1. **任务命名规范**：使用驼峰命名法，如`userSyncJob`、`orderProcessJob`
2. **日志记录**：使用`XxlJobHelper.log()`记录关键信息
3. **异常处理**：合理处理异常，避免任务静默失败
4. **参数验证**：在任务开始时验证参数的有效性
5. **性能监控**：记录任务执行时间，监控性能指标

## 示例：完整迁移案例

### 原有JobHandler实现
```java
@Component
public class OrderProcessJobHandler implements JobHandler {
    
    @Autowired
    private OrderService orderService;
    
    @Override
    public String execute(String param) throws Exception {
        log.info("开始处理订单，参数：{}", param);
        
        JSONObject params = JSON.parseObject(param);
        String orderId = params.getString("orderId");
        
        if (StringUtils.isBlank(orderId)) {
            throw new IllegalArgumentException("订单ID不能为空");
        }
        
        orderService.processOrder(orderId);
        
        return "订单处理完成：" + orderId;
    }
}
```

### 迁移后的XXL-Job实现
```java
@Component
@Slf4j
public class OrderProcessJob {
    
    @Autowired
    private OrderService orderService;
    
    @XxlJob("orderProcessJob")
    public void execute() {
        String param = XxlJobHelper.getJobParam();
        XxlJobHelper.log("开始处理订单，参数：{}", param);
        
        try {
            JSONObject params = JSON.parseObject(param);
            String orderId = params.getString("orderId");
            
            if (StringUtils.isBlank(orderId)) {
                throw new IllegalArgumentException("订单ID不能为空");
            }
            
            orderService.processOrder(orderId);
            
            XxlJobHelper.log("订单处理完成：{}", orderId);
        } catch (Exception e) {
            XxlJobHelper.log("订单处理失败：{}", e.getMessage());
            throw e;
        }
    }
}
```

这样，你就完成了从Job模块到XXL-Job的完整迁移。
