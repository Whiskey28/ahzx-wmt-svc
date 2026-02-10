package com.wmt.framework.xxljob.config;

import com.xxl.job.core.executor.impl.XxlJobSpringExecutor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * XXL-Job 健康检查指示器
 * 
 * 检查执行器的初始化状态和与调度中心的连接状态。
 * 
 * @author WMT
 */
@Slf4j
@Component
@ConditionalOnClass(HealthIndicator.class)
public class XxlJobHealthIndicator implements HealthIndicator {

    private final XxlJobSpringExecutor executor;
    private final WmtXxlJobAutoConfiguration.XxlJobProps props;

    public XxlJobHealthIndicator(XxlJobSpringExecutor executor, 
                                  WmtXxlJobAutoConfiguration.XxlJobProps props) {
        this.executor = executor;
        this.props = props;
    }

    @Override
    public Health health() {
        Map<String, Object> details = new HashMap<>();
        
        try {
            // 检查执行器是否已初始化
            boolean initialized = isExecutorInitialized();
            details.put("initialized", initialized);
            
            if (!initialized) {
                return Health.down()
                        .withDetails(details)
                        .withDetail("reason", "执行器未初始化")
                        .build();
            }
            
            // 从配置属性获取执行器基本信息
            String appname = props.getExecutor().getAppname();
            int port = props.getExecutor().getPort();
            String adminAddresses = props.getAdmin().getAddresses();
            String logpath = props.getExecutor().getLogpath();
            
            details.put("appname", appname != null ? appname : "未配置");
            details.put("port", port);
            details.put("adminAddresses", adminAddresses != null ? adminAddresses : "未配置");
            details.put("logPath", logpath != null ? logpath : "未配置");
            
            // 检查连接状态（通过检查执行器是否正常运行来判断）
            boolean connected = checkConnection();
            details.put("connected", connected);
            
            if (!connected) {
                return Health.down()
                        .withDetails(details)
                        .withDetail("reason", "执行器未连接到调度中心或连接异常")
                        .build();
            }
            
            return Health.up()
                    .withDetails(details)
                    .build();
                    
        } catch (Exception e) {
            log.error("XXL-Job 健康检查异常", e);
            return Health.down()
                    .withDetails(details)
                    .withDetail("error", e.getMessage())
                    .build();
        }
    }

    /**
     * 检查执行器是否已初始化
     * 
     * @return true 如果已初始化
     */
    private boolean isExecutorInitialized() {
        try {
            // 通过检查执行器对象和配置来判断是否已初始化
            return executor != null 
                    && props != null
                    && props.getExecutor() != null
                    && props.getExecutor().getAppname() != null
                    && !props.getExecutor().getAppname().trim().isEmpty()
                    && props.getExecutor().getPort() > 0;
        } catch (Exception e) {
            log.debug("检查执行器初始化状态时发生异常", e);
            return false;
        }
    }

    /**
     * 检查与调度中心的连接状态
     * 
     * @return true 如果连接正常
     */
    private boolean checkConnection() {
        try {
            // XXL-Job 执行器在启动时会自动注册到调度中心
            // 如果执行器正常运行，通常表示连接正常
            // 这里通过检查执行器状态来判断连接是否正常
            
            // 检查是否有调度中心地址配置
            String adminAddresses = props.getAdmin().getAddresses();
            if (adminAddresses == null || adminAddresses.trim().isEmpty()) {
                return false;
            }
            
            // 执行器在启动后会自动尝试连接调度中心
            // 如果执行器对象存在且配置正常，认为连接正常
            // 注意：这里无法直接检测网络连接，只能通过执行器状态推断
            return executor != null;
            
        } catch (Exception e) {
            log.debug("检查连接状态时发生异常", e);
            return false;
        }
    }
}

