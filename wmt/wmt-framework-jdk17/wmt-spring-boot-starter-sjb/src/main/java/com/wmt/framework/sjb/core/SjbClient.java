package com.wmt.framework.sjb.core;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wmt.framework.common.exception.ServiceException;
import com.wmt.framework.common.exception.enums.GlobalErrorCodeConstants;
import com.wmt.framework.sjb.config.SjbProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * sjb（数据部）平台通用客户端。
 *
 * <p>职责：</p>
 * <ul>
 *   <li>按照 sjb 协议构建 verification 请求头（包含 orgId、department、interface_no）。</li>
 *   <li>使用 JSON 请求体调用 sjb 网关。</li>
 *   <li>统一解析响应并根据 code/sourceCode 判断成功或失败，失败时抛出 {@link ServiceException}。</li>
 *   <li>将 {@code sourceBody} 反序列化为调用方需要的泛型类型。</li>
 * </ul>
 *
 * <p>使用方式（在业务系统中，通过 Spring 自动注入）：</p>
 * <pre>{@code
 * @Autowired
 * private SjbClient sjbClient;
 *
 * FaceCheckResp resp = sjbClient.invokeForBody(
 *         "sjb|1.0.0|shujubaoqueryUrl",
 *         faceCheckParam,
 *         FaceCheckResp.class
 * );
 * }</pre>
 */
@Slf4j
public class SjbClient {

    private final SjbProperties properties;

    private final RestTemplate restTemplate;

    private final ObjectMapper objectMapper;

    /**
     * 使用默认 {@link RestTemplate} 和 {@link ObjectMapper} 的构造方法。
     *
     * @param properties sjb 平台配置
     */
    public SjbClient(SjbProperties properties) {
        this(properties, new RestTemplate(), new ObjectMapper());
    }

    public SjbClient(SjbProperties properties, RestTemplate restTemplate, ObjectMapper objectMapper) {
        this.properties = properties;
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
    }

    /**
     * 通用调用接口，返回完整的 {@link SjbResponse} 结构，调用方可以自行处理。
     *
     * @param interfaceNo   sjb 接口编号，例如 {@code sjb|1.0.0|shujubaoqueryUrl}
     * @param requestBody   业务请求体，会被序列化为 JSON
     * @param sourceBodyType sourceBody 类型
     * @param <T>           sourceBody 泛型
     * @return 解析后的 {@link SjbResponse}
     */
    public <T> SjbResponse<T> invoke(String interfaceNo, Object requestBody, Class<T> sourceBodyType) {
        String url = properties.getUrl();
        if (!StringUtils.hasText(url)) {
            throw new ServiceException(GlobalErrorCodeConstants.ERROR_CONFIGURATION.getCode(),
                    "sjb 平台网关地址未配置（wmt.sjb.url）");
        }
        String requestJson = toJson(requestBody);
        HttpEntity<String> httpEntity = new HttpEntity<>(requestJson, buildHeaders(interfaceNo));

        log.info("[sjb] request url={}, interfaceNo={}, body={}", url, interfaceNo,
                abbreviate(requestJson, 2000));
        ResponseEntity<String> responseEntity;
        try {
            responseEntity = restTemplate.postForEntity(url, httpEntity, String.class);
        } catch (RestClientException ex) {
            log.error("[sjb] invoke error, interfaceNo={}", interfaceNo, ex);
            throw new ServiceException(GlobalErrorCodeConstants.INTERNAL_SERVER_ERROR.getCode(),
                    "调用 sjb 平台失败，请联系管理员");
        }

        String responseBody = responseEntity.getBody();
        log.info("[sjb] response interfaceNo={}, body={}", interfaceNo,
                abbreviate(responseBody, 2000));

        if (!StringUtils.hasText(responseBody)) {
            throw new ServiceException(GlobalErrorCodeConstants.INTERNAL_SERVER_ERROR.getCode(),
                    "sjb 平台返回为空");
        }

        JsonNode root = parseJson(responseBody);
        SjbResponse<T> sjbResponse = buildSjbResponse(root, sourceBodyType);
        // 统一成功/失败判断和异常抛出
        checkSuccessOrThrow(root);
        return sjbResponse;
    }

    /**
     * 调用 sjb 并直接返回 {@code sourceBody} 对象。
     */
    public <T> T invokeForBody(String interfaceNo, Object requestBody, Class<T> sourceBodyType) {
        SjbResponse<T> response = invoke(interfaceNo, requestBody, sourceBodyType);
        return response.getSourceBody();
    }

    private HttpHeaders buildHeaders(String interfaceNo) {
        Map<String, Object> verification = new HashMap<>();
        verification.put("orgId", properties.getOrgId());
        verification.put("department", properties.getDepartment());
        verification.put("interface_no", interfaceNo);

        String verificationJson = toJson(verification);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("verification", verificationJson);
        return headers;
    }

    private String toJson(Object obj) {
        try {
            return objectMapper.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            throw new ServiceException(GlobalErrorCodeConstants.INTERNAL_SERVER_ERROR.getCode(),
                    "JSON 序列化失败");
        }
    }

    private JsonNode parseJson(String json) {
        try {
            return objectMapper.readTree(json);
        } catch (IOException e) {
            throw new ServiceException(GlobalErrorCodeConstants.INTERNAL_SERVER_ERROR.getCode(),
                    "sjb 平台返回报文解析失败");
        }
    }

    private <T> SjbResponse<T> buildSjbResponse(JsonNode root, Class<T> sourceBodyType) {
        SjbResponse<T> response = new SjbResponse<>();
        if (root.has("code")) {
            response.setCode(root.get("code").asInt());
        }
        if (root.has("messages")) {
            response.setMessages(root.get("messages").asText(null));
        }
        if (root.has("status")) {
            response.setStatus(root.get("status").asText(null));
        }
        if (root.has("sourceCode")) {
            response.setSourceCode(root.get("sourceCode").asInt());
        }
        if (root.has("sourceMessages")) {
            response.setSourceMessages(root.get("sourceMessages").asText(null));
        }
        if (root.has("sourceStatus")) {
            response.setSourceStatus(root.get("sourceStatus").asText(null));
        }
        if (root.has("track_id")) {
            response.setTrackId(root.get("track_id").asText(null));
        }

        JsonNode sourceBodyNode = root.get("sourceBody");
        if (sourceBodyNode != null && !sourceBodyNode.isNull() && sourceBodyType != null) {
            T body;
            try {
                if (sourceBodyNode.isTextual()) {
                    // sjb 有时会把 JSON 放在字符串里，这里做一次解析
                    body = objectMapper.readValue(sourceBodyNode.asText(), sourceBodyType);
                } else {
                    body = objectMapper.treeToValue(sourceBodyNode, sourceBodyType);
                }
            } catch (Exception ex) {
                throw new ServiceException(GlobalErrorCodeConstants.INTERNAL_SERVER_ERROR.getCode(),
                        "sjb 平台返回的 sourceBody 解析失败");
            }
            response.setSourceBody(body);
        }
        return response;
    }

    /**
     * 统一成功/失败判断逻辑。
     *
     * <p>目前实现遵循存量 {@code IDataAuthService.SUCCESS_FILTER} 的逻辑：</p>
     * <pre>
     * code == "200" &amp;&amp; sourceCode == "200"
     * </pre>
     * 同时会尽量读取 {@code sourceMessages} 或 {@code messages} 作为错误提示。
     */
    private void checkSuccessOrThrow(JsonNode root) {
        String code = root.path("code").asText();
        String sourceCode = root.path("sourceCode").asText();
        boolean success = "200".equals(code) && "200".equals(sourceCode);
        if (success) {
            return;
        }
        String sourceMessages = root.path("sourceMessages").asText(null);
        String messages = root.path("messages").asText(null);
        String errorMsg = StringUtils.hasText(sourceMessages) ? sourceMessages : messages;
        if (!StringUtils.hasText(errorMsg)) {
            errorMsg = "调用 sjb 平台失败";
        }
        throw new ServiceException(GlobalErrorCodeConstants.INTERNAL_SERVER_ERROR.getCode(), errorMsg);
    }

    private String abbreviate(String text, int max) {
        if (text == null) {
            return null;
        }
        if (text.length() <= max) {
            return text;
        }
        return text.substring(0, max) + "...";
    }

}

