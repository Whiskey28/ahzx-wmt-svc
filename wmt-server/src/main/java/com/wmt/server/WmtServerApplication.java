package com.wmt.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * 项目的启动类
 *
 *
 * @author AHC源码
 */
@SuppressWarnings("SpringComponentScan") // 忽略 IDEA 无法识别 ${wmt.info.base-package}
@SpringBootApplication(scanBasePackages = {"${wmt.info.base-package}.server", "${wmt.info.base-package}.module"})
@EnableAsync
public class WmtServerApplication {

    public static void main(String[] args) {

        SpringApplication.run(WmtServerApplication.class, args);
//        new SpringApplicationBuilder(WmtServerApplication.class)
//                .applicationStartup(new BufferingApplicationStartup(20480))
//                .run(args);
    }

}
