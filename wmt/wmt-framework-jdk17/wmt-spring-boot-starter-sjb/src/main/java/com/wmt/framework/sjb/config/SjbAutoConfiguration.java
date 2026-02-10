package com.wmt.framework.sjb.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wmt.framework.sjb.core.SjbClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

/**
 * sjb（数据部）平台自动配置类
 *
 * <p>当业务系统配置了 {@code wmt.sjb.*} 且未禁用时，自动创建 {@link SjbClient} Bean。</p>
 */
@Slf4j
@AutoConfiguration
@ConditionalOnProperty(prefix = "wmt.sjb", name = "enabled", havingValue = "true", matchIfMissing = true)
@EnableConfigurationProperties(SjbProperties.class)
public class SjbAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public SjbClient sjbClient(SjbProperties properties, RestTemplate restTemplate, ObjectMapper objectMapper) {
        log.info("[sjb] 初始化 sjb 客户端，url={}, orgId={}, department={}",
                properties.getUrl(), properties.getOrgId(), properties.getDepartment());
        return new SjbClient(properties, restTemplate, objectMapper);
    }

    /**
     * 如果业务系统没有配置 RestTemplate，提供一个默认的
     */
    @Bean
    @ConditionalOnMissingBean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

}

