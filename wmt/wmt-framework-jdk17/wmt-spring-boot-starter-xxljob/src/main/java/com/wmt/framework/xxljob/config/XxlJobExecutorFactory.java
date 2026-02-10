package com.wmt.framework.xxljob.config;

import com.xxl.job.core.executor.impl.XxlJobSpringExecutor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

/**
 * XXL-Job 执行器工厂类
 * 
 * 负责创建和管理 XXL-Job 执行器实例，提供执行器生命周期管理能力。
 * 预留多执行器扩展能力，当前以单执行器为主。
 * 
 * @author WMT
 */
@Slf4j
@Component
public class XxlJobExecutorFactory {

    /**
     * 创建执行器实例
     * 
     * @param props 配置属性
     * @return 执行器实例
     */
    public XxlJobSpringExecutor createExecutor(WmtXxlJobAutoConfiguration.XxlJobProps props) {
        log.info("开始创建 XXL-Job 执行器，应用名：{}", props.getExecutor().getAppname());
        
        XxlJobSpringExecutor executor = new XxlJobSpringExecutor();
        
        // 设置管理端地址
        String adminAddresses = props.getAdmin().getAddresses();
        if (StringUtils.hasText(adminAddresses)) {
            executor.setAdminAddresses(adminAddresses);
            log.debug("设置调度中心地址：{}", adminAddresses);
        } else {
            log.warn("调度中心地址未配置，执行器可能无法连接到调度中心");
        }
        
        // 设置执行器应用名
        String appname = props.getExecutor().getAppname();
        if (StringUtils.hasText(appname)) {
            executor.setAppname(appname);
            log.debug("设置执行器应用名：{}", appname);
        } else {
            log.warn("执行器应用名未配置，可能导致执行器注册失败");
        }
        
        // 设置执行器地址（可选，为空则自动获取）
        String address = props.getExecutor().getAddress();
        if (StringUtils.hasText(address)) {
            executor.setAddress(address);
            log.debug("设置执行器注册地址：{}", address);
        }
        
        // 设置执行器IP（可选，为空则自动获取）
        String ip = props.getExecutor().getIp();
        if (StringUtils.hasText(ip)) {
            executor.setIp(ip);
            log.debug("设置执行器IP：{}", ip);
        }
        
        // 设置执行器端口
        int port = props.getExecutor().getPort();
        if (port > 0) {
            executor.setPort(port);
            log.debug("设置执行器端口：{}", port);
        } else {
            executor.setPort(9999);
            log.warn("执行器端口未配置或无效，使用默认值：9999");
        }
        
        // 设置访问令牌
        String accessToken = props.getAccessToken();
        if (StringUtils.hasText(accessToken)) {
            executor.setAccessToken(accessToken);
            log.debug("设置访问令牌");
        } else {
            log.warn("访问令牌未配置，如果调度中心启用了令牌验证，可能导致连接失败");
        }
        
        // 设置日志路径
        String logpath = props.getExecutor().getLogpath();
        if (StringUtils.hasText(logpath)) {
            executor.setLogPath(logpath);
            log.debug("设置执行器日志路径：{}", logpath);
        } else {
            executor.setLogPath("./logs/xxl-job");
            log.warn("执行器日志路径未配置，使用默认值：./logs/xxl-job");
        }
        
        // 设置日志保留天数
        int logretentiondays = props.getExecutor().getLogretentiondays();
        if (logretentiondays > 0) {
            executor.setLogRetentionDays(logretentiondays);
            log.debug("设置日志保留天数：{}", logretentiondays);
        } else {
            executor.setLogRetentionDays(30);
            log.debug("日志保留天数未配置或无效，使用默认值：30");
        }
        
        log.info("XXL-Job 执行器创建完成，应用名：{}，端口：{}", appname, port);
        
        return executor;
    }

    /**
     * 销毁执行器
     * 
     * @param executor 执行器实例
     */
    public void destroyExecutor(XxlJobSpringExecutor executor) {
        if (executor != null) {
            try {
                log.info("开始销毁 XXL-Job 执行器");
                executor.destroy();
                log.info("XXL-Job 执行器销毁完成");
            } catch (Exception e) {
                log.error("销毁 XXL-Job 执行器时发生异常", e);
            }
        }
    }

    /**
     * 重启执行器（预留扩展能力）
     * 
     * @param executor 执行器实例
     * @param props 配置属性
     * @return 新的执行器实例
     */
    public XxlJobSpringExecutor restartExecutor(XxlJobSpringExecutor executor, 
                                                 WmtXxlJobAutoConfiguration.XxlJobProps props) {
        log.info("开始重启 XXL-Job 执行器");
        destroyExecutor(executor);
        return createExecutor(props);
    }
}

