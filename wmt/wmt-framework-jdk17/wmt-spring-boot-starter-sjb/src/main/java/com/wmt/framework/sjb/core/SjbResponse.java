package com.wmt.framework.sjb.core;

import lombok.Data;

import java.io.Serializable;

/**
 * sjb 平台统一响应模型的通用封装。
 *
 * <p>服务端实际返回结构示例：</p>
 * <pre>
 * {
 *   "code": 200,
 *   "messages": "该笔请求为模拟请求 仅供参考",
 *   "originHeader": {
 *     "department": "ax_001",
 *     "interface_no": "sjb|1.0.0|download",
 *     "orgId": "ax_0002"
 *   },
 *   "sourceBody": { ... },
 *   "sourceCode": 0,
 *   "sourceMessages": "成功",
 *   "sourceStatus": "success",
 *   "status": "success",
 *   "track_id": "ff0237..."
 * }
 * </pre>
 *
 * @param <T> sourceBody 的类型
 */
@Data
public class SjbResponse<T> implements Serializable {

    /**
     * 调用 sjb 平台网关的返回码，例如 200
     */
    private Integer code;

    /**
     * 平台网关提示信息
     */
    private String messages;

    /**
     * 平台网关状态，一般为 success
     */
    private String status;

    /**
     * sjb 内部真实接口的业务返回码
     */
    private Integer sourceCode;

    /**
     * sjb 内部真实接口的业务返回信息
     */
    private String sourceMessages;

    /**
     * sjb 内部真实接口的业务状态，一般为 success
     */
    private String sourceStatus;

    /**
     * 业务数据 body，具体结构由不同接口定义
     */
    private T sourceBody;

    /**
     * 跟踪 ID
     */
    private String trackId;

}

