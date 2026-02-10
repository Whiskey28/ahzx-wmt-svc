package com.wmt.framework.xxljob.config;

import com.xxl.job.core.executor.impl.XxlJobSpringExecutor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.util.StringUtils;

import jakarta.annotation.PostConstruct;

/**
 * XXL-Job 自动配置类
 *
 * @author WMT
 */
@Slf4j
@AutoConfiguration
@EnableConfigurationProperties(WmtXxlJobAutoConfiguration.XxlJobProps.class)
@ConditionalOnProperty(prefix = "xxl.job", name = "enabled", havingValue = "true")
public class WmtXxlJobAutoConfiguration {

    private final XxlJobProps props;
    private final XxlJobExecutorFactory executorFactory;

    public WmtXxlJobAutoConfiguration(XxlJobProps props, XxlJobExecutorFactory executorFactory) {
        this.props = props;
        this.executorFactory = executorFactory;
    }

    @PostConstruct
    public void validateProperties() {
        // 验证必填配置项，仅记录警告
        if (!StringUtils.hasText(props.getAdmin().getAddresses())) {
            log.warn("XXL-Job 配置警告：admin.addresses 未配置，可能导致执行器无法连接到调度中心");
        }
        if (!StringUtils.hasText(props.getExecutor().getAppname())) {
            log.warn("XXL-Job 配置警告：executor.appname 未配置，可能导致执行器注册失败");
        }
        if (props.getExecutor().getPort() <= 0) {
            log.warn("XXL-Job 配置警告：executor.port 未配置或无效，使用默认值 9999");
        }
        if (!StringUtils.hasText(props.getExecutor().getLogpath())) {
            log.warn("XXL-Job 配置警告：executor.logpath 未配置，使用默认值 ./logs/xxl-job");
        }
    }

    // @Bean(initMethod = "start", destroyMethod = "destroy")
    @Bean
    public XxlJobSpringExecutor xxlJobExecutor() {
        return executorFactory.createExecutor(props);
    }

    /**
     * XXL-Job 配置属性
     */
    @ConfigurationProperties(prefix = "xxl.job")
    public static class XxlJobProps {
        /**
         * 是否启用 XXL-Job
         */
        private boolean enabled = false;

        /**
         * 访问令牌（与调度中心配置一致）
         */
        private String accessToken;

        /**
         * 管理端配置
         */
        private Admin admin = new Admin();

        /**
         * 执行器配置
         */
        private Executor executor = new Executor();

        public boolean isEnabled() {
            return enabled;
        }

        public void setEnabled(boolean enabled) {
            this.enabled = enabled;
        }

        public String getAccessToken() {
            return accessToken;
        }

        public void setAccessToken(String accessToken) {
            this.accessToken = accessToken;
        }

        public Admin getAdmin() {
            return admin;
        }

        public void setAdmin(Admin admin) {
            this.admin = admin;
        }

        public Executor getExecutor() {
            return executor;
        }

        public void setExecutor(Executor executor) {
            this.executor = executor;
        }

        /**
         * 管理端配置
         */
        public static class Admin {
            /**
             * 调度中心地址（必填）
             */
            private String addresses;

            public String getAddresses() {
                return addresses;
            }

            public void setAddresses(String addresses) {
                this.addresses = addresses;
            }
        }

        /**
         * 执行器配置
         */
        public static class Executor {
            /**
             * 执行器应用名（必填，与调度中心注册名称一致）
             */
            private String appname;

            /**
             * 执行器注册地址，为空则自动获取
             */
            private String address;

            /**
             * 执行器IP，为空则自动获取
             */
            private String ip;

            /**
             * 执行器端口（必填，默认 9999）
             */
            private int port = 9999;

            /**
             * 执行器日志路径（必填，默认 ./logs/xxl-job）
             */
            private String logpath = "./logs/xxl-job";

            /**
             * 执行器日志保留天数（默认 30）
             */
            private int logretentiondays = 30;

            public String getAppname() {
                return appname;
            }

            public void setAppname(String appname) {
                this.appname = appname;
            }

            public String getAddress() {
                return address;
            }

            public void setAddress(String address) {
                this.address = address;
            }

            public String getIp() {
                return ip;
            }

            public void setIp(String ip) {
                this.ip = ip;
            }

            public int getPort() {
                return port;
            }

            public void setPort(int port) {
                this.port = port;
            }

            public String getLogpath() {
                return logpath;
            }

            public void setLogpath(String logpath) {
                this.logpath = logpath;
            }

            public int getLogretentiondays() {
                return logretentiondays;
            }

            public void setLogretentiondays(int logretentiondays) {
                this.logretentiondays = logretentiondays;
            }
        }
    }
}


