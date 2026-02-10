package com.wmt.module.infra.framework.file.config;

import com.wmt.module.infra.framework.file.core.client.FileClientFactory;
import com.wmt.module.infra.framework.file.core.client.FileClientFactoryImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 文件配置类
 *
 * @author AHC源码
 */
@Configuration(proxyBeanMethods = false)
public class WmtFileAutoConfiguration {

    @Bean
    public FileClientFactory fileClientFactory() {
        return new FileClientFactoryImpl();
    }

}
