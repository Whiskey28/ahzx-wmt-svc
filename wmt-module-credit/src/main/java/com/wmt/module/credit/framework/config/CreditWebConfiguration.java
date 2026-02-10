package com.wmt.module.credit.framework.config;

import com.wmt.framework.swagger.config.WmtSwaggerAutoConfiguration;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * credit 模块的 web 组件的 Configuration
 *
 * @author AHC源码
 */
@Configuration(proxyBeanMethods = false)
public class CreditWebConfiguration {

    /**
     * system 模块的 API 分组
     */
    @Bean
    public GroupedOpenApi creditGroupedOpenApi() {
        return WmtSwaggerAutoConfiguration.buildGroupedOpenApi("credit");
    }

}
