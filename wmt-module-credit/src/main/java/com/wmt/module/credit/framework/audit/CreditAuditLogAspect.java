package com.wmt.module.credit.framework.audit;

import cn.hutool.core.bean.BeanUtil;
import com.wmt.framework.common.util.servlet.ServletUtils;
import com.wmt.module.credit.dal.dataobject.audit.CreditAuditLogDO;
import com.wmt.module.credit.dal.dataobject.form.CreditFormDataDO;
import com.wmt.module.credit.dal.mysql.form.CreditFormDataMapper;
import com.wmt.module.credit.service.audit.CreditAuditLogService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.annotation.Order;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.lang.reflect.Method;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import static com.wmt.framework.security.core.util.SecurityFrameworkUtils.getLoginUserId;

/**
 * 征信审计日志AOP切面
 *
 * @author AHC源码
 */
@Aspect
@Component
@Order(1) // 确保在事务之前执行
@Slf4j
public class CreditAuditLogAspect {

    @Resource
    private CreditAuditLogService auditLogService;

    @Resource
    private CreditFormDataMapper formDataMapper;

    private final ExpressionParser parser = new SpelExpressionParser();

    @Around("@annotation(com.wmt.module.credit.framework.audit.CreditAuditLog)")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        // 获取注解
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        CreditAuditLog auditLog = method.getAnnotation(CreditAuditLog.class);
        if (auditLog == null) {
            return joinPoint.proceed();
        }

        // 构建SpEL上下文
        EvaluationContext context = buildSpelContext(joinPoint);

        // 获取变更前数据（如果是更新或删除操作）
        Map<String, Object> beforeData = null;
        Long bizId = parseBizId(null, auditLog, context); // 先尝试解析业务ID
        if (("UPDATE".equals(auditLog.operationType()) || "DELETE".equals(auditLog.operationType())) && bizId != null) {
            beforeData = getBeforeData(auditLog, bizId);
        }

        // 执行方法
        Object result = joinPoint.proceed();

        // 重新解析业务ID（因为CREATE操作返回的是新ID）
        bizId = parseBizId(result, auditLog, context);

        // 获取变更后数据（如果是创建或更新操作）
        Map<String, Object> afterData = null;
        if ("CREATE".equals(auditLog.operationType()) || "UPDATE".equals(auditLog.operationType())) {
            afterData = getAfterData(result, auditLog, bizId);
        }

        // 业务ID已在上面解析

        // 解析操作描述
        String operationDesc = parseOperationDesc(auditLog, context);

        // 计算变更字段
        String changeFields = calculateChangeFields(beforeData, afterData);

        // 获取IP和UserAgent
        String ipAddress = getIpAddress();
        String userAgent = getUserAgent();

        // 保存审计日志
        try {
            CreditAuditLogDO logDO = CreditAuditLogDO.builder()
                    .bizType(auditLog.bizType())
                    .bizId(bizId)
                    .operationType(auditLog.operationType())
                    .operationUserId(getLoginUserId())
                    .operationDesc(operationDesc)
                    .beforeData(beforeData)
                    .afterData(afterData)
                    .changeFields(changeFields)
                    .ipAddress(ipAddress)
                    .userAgent(userAgent)
                    .createTime(LocalDateTime.now())
                    .build();
            auditLogService.createLog(logDO);
        } catch (Exception e) {
            // 审计日志记录失败不应该影响主业务流程
            log.error("记录审计日志失败", e);
        }

        return result;
    }

    /**
     * 构建SpEL上下文
     */
    private EvaluationContext buildSpelContext(ProceedingJoinPoint joinPoint) {
        StandardEvaluationContext context = new StandardEvaluationContext();
        // 设置方法参数
        Object[] args = joinPoint.getArgs();
        String[] paramNames = ((MethodSignature) joinPoint.getSignature()).getParameterNames();
        for (int i = 0; i < paramNames.length; i++) {
            context.setVariable(paramNames[i], args[i]);
        }
        return context;
    }

    /**
     * 获取变更前数据
     */
    private Map<String, Object> getBeforeData(CreditAuditLog auditLog, Long bizId) {
        try {
            if (bizId == null) {
                return null;
            }

            // 根据业务类型查询数据
            if ("FORM_DATA".equals(auditLog.bizType())) {
                CreditFormDataDO formData = formDataMapper.selectById(bizId);
                if (formData != null) {
                    return BeanUtil.beanToMap(formData, false, true);
                }
            }
            // 其他业务类型可以在这里扩展
            return null;
        } catch (Exception e) {
            log.warn("获取变更前数据失败", e);
            return null;
        }
    }

    /**
     * 获取变更后数据
     */
    private Map<String, Object> getAfterData(Object result, CreditAuditLog auditLog, Long bizId) {
        try {
            // 如果是UPDATE操作，从数据库查询最新数据
            if ("UPDATE".equals(auditLog.operationType()) && bizId != null) {
                if ("FORM_DATA".equals(auditLog.bizType())) {
                    CreditFormDataDO formData = formDataMapper.selectById(bizId);
                    if (formData != null) {
                        return BeanUtil.beanToMap(formData, false, true);
                    }
                }
            }
            
            // CREATE操作，从返回值获取
            if (result == null) {
                return null;
            }
            // 将结果转换为Map
            if (result instanceof Map) {
                return (Map<String, Object>) result;
            } else {
                return BeanUtil.beanToMap(result, false, true);
            }
        } catch (Exception e) {
            log.warn("获取变更后数据失败", e);
            return null;
        }
    }

    /**
     * 解析业务ID
     */
    private Long parseBizId(Object result, CreditAuditLog auditLog, EvaluationContext context) {
        try {
            // 如果指定了SpEL表达式，使用表达式解析
            if (auditLog.bizIdExpression() != null && !auditLog.bizIdExpression().isEmpty()) {
                Expression expression = parser.parseExpression(auditLog.bizIdExpression());
                Object value = expression.getValue(context);
                if (value instanceof Long) {
                    return (Long) value;
                } else if (value instanceof Number) {
                    return ((Number) value).longValue();
                }
            }

            // 否则尝试从返回值中获取id
            if (result != null) {
                if (result instanceof Long) {
                    return (Long) result;
                } else if (result instanceof Number) {
                    return ((Number) result).longValue();
                } else {
                    // 尝试通过反射获取id字段
                    try {
                        java.lang.reflect.Field idField = result.getClass().getDeclaredField("id");
                        idField.setAccessible(true);
                        Object idValue = idField.get(result);
                        if (idValue instanceof Long) {
                            return (Long) idValue;
                        } else if (idValue instanceof Number) {
                            return ((Number) idValue).longValue();
                        }
                    } catch (Exception e) {
                        // 忽略
                    }
                }
            }

            return null;
        } catch (Exception e) {
            log.warn("解析业务ID失败", e);
            return null;
        }
    }

    /**
     * 解析操作描述
     */
    private String parseOperationDesc(CreditAuditLog auditLog, EvaluationContext context) {
        try {
            if (auditLog.operationDescExpression() != null && !auditLog.operationDescExpression().isEmpty()) {
                Expression expression = parser.parseExpression(auditLog.operationDescExpression());
                Object value = expression.getValue(context);
                return value != null ? value.toString() : null;
            }
            return null;
        } catch (Exception e) {
            log.warn("解析操作描述失败", e);
            return null;
        }
    }

    /**
     * 计算变更字段
     */
    private String calculateChangeFields(Map<String, Object> beforeData, Map<String, Object> afterData) {
        if (beforeData == null && afterData == null) {
            return null;
        }
        if (beforeData == null) {
            return String.join(",", afterData.keySet());
        }
        if (afterData == null) {
            return String.join(",", beforeData.keySet());
        }
        // 找出变更的字段
        java.util.Set<String> changedFields = new java.util.HashSet<>();
        changedFields.addAll(beforeData.keySet());
        changedFields.addAll(afterData.keySet());
        return String.join(",", changedFields);
    }

    /**
     * 从参数中提取id
     */
    private Long extractIdFromArgs(Object[] args) {
        for (Object arg : args) {
            if (arg instanceof Long) {
                return (Long) arg;
            } else if (arg != null) {
                try {
                    java.lang.reflect.Field idField = arg.getClass().getDeclaredField("id");
                    idField.setAccessible(true);
                    Object idValue = idField.get(arg);
                    if (idValue instanceof Long) {
                        return (Long) idValue;
                    } else if (idValue instanceof Number) {
                        return ((Number) idValue).longValue();
                    }
                } catch (Exception e) {
                    // 忽略
                }
            }
        }
        return null;
    }

    /**
     * 获取IP地址
     */
    private String getIpAddress() {
        try {
            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            if (attributes != null) {
                HttpServletRequest request = attributes.getRequest();
                return ServletUtils.getClientIP(request);
            }
        } catch (Exception e) {
            log.warn("获取IP地址失败", e);
        }
        return null;
    }

    /**
     * 获取UserAgent
     */
    private String getUserAgent() {
        try {
            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            if (attributes != null) {
                HttpServletRequest request = attributes.getRequest();
                return request.getHeader("User-Agent");
            }
        } catch (Exception e) {
            log.warn("获取UserAgent失败", e);
        }
        return null;
    }

}
