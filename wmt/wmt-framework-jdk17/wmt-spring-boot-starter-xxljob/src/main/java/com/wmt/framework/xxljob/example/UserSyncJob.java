package com.wmt.framework.xxljob.example;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.xxl.job.core.context.XxlJobHelper;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

/**
 * 用户数据同步任务示例
 * 
 * 本示例展示了XXL-Job任务的标准实现方式，包括：
 * 1. 参数验证
 * 2. 异常处理
 * 3. 日志记录
 * 4. 性能监控
 * 
 * @author WMT
 */
@Component
@Slf4j
public class UserSyncJob {
    
    /**
     * 用户数据同步任务
     * 
     * 任务参数格式：
     * {
     *   "source": "external_system",
     *   "batchSize": 100,
     *   "lastSyncTime": "2024-01-01 00:00:00"
     * }
     */
    @XxlJob("userSyncJob")
    public void execute() {
        long startTime = System.currentTimeMillis();
        XxlJobHelper.log("用户数据同步任务开始执行");
        
        try {
            // 1. 获取和验证参数
            String param = XxlJobHelper.getJobParam();
            UserSyncParams syncParams = validateAndParseParams(param);
            
            XxlJobHelper.log("任务参数：{}", param);
            
            // 2. 执行数据同步
            SyncResult result = syncUserData(syncParams);
            
            // 3. 记录执行结果
            long endTime = System.currentTimeMillis();
            long duration = endTime - startTime;
            
            XxlJobHelper.log("用户数据同步完成 - 成功：{}，失败：{}，耗时：{}ms", 
                           result.getSuccessCount(), result.getFailureCount(), duration);
            
            // 4. 性能监控
            if (duration > 300000) { // 5分钟
                XxlJobHelper.log("警告：任务执行时间过长，耗时：{}ms", duration);
            }
            
            // 5. 失败率监控
            double failureRate = (double) result.getFailureCount() / result.getTotalCount();
            if (failureRate > 0.1) { // 失败率超过10%
                XxlJobHelper.log("警告：任务失败率过高，失败率：{:.2%}", failureRate);
            }
            
        } catch (IllegalArgumentException e) {
            // 参数错误，记录日志但不重试
            XxlJobHelper.log("参数错误：{}", e.getMessage());
            throw e;
            
        } catch (Exception e) {
            // 系统异常，记录详细错误信息
            long endTime = System.currentTimeMillis();
            long duration = endTime - startTime;
            
            XxlJobHelper.log("任务执行异常，耗时：{}ms，错误：{}", duration, e.getMessage());
            log.error("用户数据同步任务执行异常", e);
            throw e;
        }
    }
    
    /**
     * 验证和解析任务参数
     */
    private UserSyncParams validateAndParseParams(String param) {
        if (!StringUtils.hasText(param)) {
            throw new IllegalArgumentException("任务参数不能为空");
        }
        
        try {
            JSONObject params = JSON.parseObject(param);
            
            String source = params.getString("source");
            if (!StringUtils.hasText(source)) {
                throw new IllegalArgumentException("缺少必需参数：source");
            }
            
            int batchSize = params.getIntValue("batchSize");
            if (batchSize <= 0) {
                batchSize = 100; // 默认批次大小
            }
            
            String lastSyncTime = params.getString("lastSyncTime");
            
            return new UserSyncParams(source, batchSize, lastSyncTime);
            
        } catch (Exception e) {
            throw new IllegalArgumentException("参数格式错误，必须是有效的JSON：" + e.getMessage());
        }
    }
    
    /**
     * 执行用户数据同步
     */
    private SyncResult syncUserData(UserSyncParams params) {
        XxlJobHelper.log("开始同步用户数据，来源：{}，批次大小：{}", 
                        params.getSource(), params.getBatchSize());
        
        SyncResult result = new SyncResult();
        
        try {
            // 模拟数据同步逻辑
            int totalCount = getTotalUserCount(params.getSource());
            result.setTotalCount(totalCount);
            
            int processedCount = 0;
            int pageNum = 1;
            
            while (processedCount < totalCount) {
                // 分批处理
                int currentBatchSize = Math.min(params.getBatchSize(), totalCount - processedCount);
                
                XxlJobHelper.log("处理第{}批数据，批次大小：{}", pageNum, currentBatchSize);
                
                // 模拟处理一批数据
                BatchResult batchResult = processBatch(pageNum, currentBatchSize);
                
                result.addSuccessCount(batchResult.getSuccessCount());
                result.addFailureCount(batchResult.getFailureCount());
                
                processedCount += currentBatchSize;
                pageNum++;
                
                XxlJobHelper.log("已处理：{}/{}", processedCount, totalCount);
            }
            
        } catch (Exception e) {
            XxlJobHelper.log("数据同步过程中发生异常：{}", e.getMessage());
            throw e;
        }
        
        return result;
    }
    
    /**
     * 获取用户总数
     */
    private int getTotalUserCount(String source) {
        // 模拟获取用户总数
        return 1000;
    }
    
    /**
     * 处理一批数据
     */
    private BatchResult processBatch(int pageNum, int batchSize) {
        BatchResult result = new BatchResult();
        
        // 模拟处理逻辑
        for (int i = 0; i < batchSize; i++) {
            try {
                // 模拟处理单个用户
                processUser(pageNum * batchSize + i);
                result.addSuccessCount(1);
            } catch (Exception e) {
                result.addFailureCount(1);
                XxlJobHelper.log("处理用户失败：{}", e.getMessage());
            }
        }
        
        return result;
    }
    
    /**
     * 处理单个用户
     */
    private void processUser(int userId) {
        // 模拟用户处理逻辑
        if (userId % 100 == 0) {
            throw new RuntimeException("模拟处理失败");
        }
    }
    
    /**
     * 用户同步参数
     */
    private static class UserSyncParams {
        private final String source;
        private final int batchSize;
        private final String lastSyncTime;
        
        public UserSyncParams(String source, int batchSize, String lastSyncTime) {
            this.source = source;
            this.batchSize = batchSize;
            this.lastSyncTime = lastSyncTime;
        }
        
        public String getSource() { return source; }
        public int getBatchSize() { return batchSize; }
        public String getLastSyncTime() { return lastSyncTime; }
    }
    
    /**
     * 同步结果
     */
    private static class SyncResult {
        private int totalCount;
        private int successCount;
        private int failureCount;
        
        public int getTotalCount() { return totalCount; }
        public void setTotalCount(int totalCount) { this.totalCount = totalCount; }
        
        public int getSuccessCount() { return successCount; }
        public void addSuccessCount(int count) { this.successCount += count; }
        
        public int getFailureCount() { return failureCount; }
        public void addFailureCount(int count) { this.failureCount += count; }
    }
    
    /**
     * 批次处理结果
     */
    private static class BatchResult {
        private int successCount;
        private int failureCount;
        
        public int getSuccessCount() { return successCount; }
        public void addSuccessCount(int count) { this.successCount += count; }
        
        public int getFailureCount() { return failureCount; }
        public void addFailureCount(int count) { this.failureCount += count; }
    }
}
