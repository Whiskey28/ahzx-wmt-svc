package com.wmt.framework.sjb.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.io.Serializable;

/**
 * sjb（数据部）平台基础配置
 *
 * <p>
 * 在业务系统的 {@code application-*.yml} 中使用前缀 {@code wmt.sjb} 进行配置，例如：
 *
 * <pre>
 * wmt:
 *   sjb:
 *     url: https://example.com/sjb/gateway
 *     org-id: ax_0002
 *     department: ax_001
 * </pre>
 */
@Data
@ConfigurationProperties(prefix = "wmt.sjb")
public class SjbProperties implements Serializable {

    /**
     * sjb 平台统一网关地址
     *
     * <p>例如：{@code https://example.com/sjb/gateway}</p>
     */
    private String url;

    /**
     * 机构编码，对应 sjb header 中的 {@code orgId}
     */
    private String orgId;

    /**
     * 部门编码，对应 sjb header 中的 {@code department}
     */
    private String department;

}

