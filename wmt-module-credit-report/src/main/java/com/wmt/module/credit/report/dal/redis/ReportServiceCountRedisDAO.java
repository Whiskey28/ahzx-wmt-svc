package com.wmt.module.credit.report.dal.redis;

import jakarta.annotation.Resource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.Duration;

/**
 * 季报（小微贷款）- 服务次数缓存 DAO
 */
@Repository
public class ReportServiceCountRedisDAO {

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    public BigDecimal getServiceCountTotal(String reportId, String monthPeriodId) {
        if (StringUtils.isBlank(reportId) || StringUtils.isBlank(monthPeriodId)) {
            return null;
        }
        String key = formatKey(reportId, monthPeriodId);
        String value = stringRedisTemplate.opsForValue().get(key);
        if (StringUtils.isBlank(value)) {
            return null;
        }
        try {
            return new BigDecimal(value);
        } catch (NumberFormatException ignored) {
            return null;
        }
    }

    public void setServiceCountTotal(String reportId, String monthPeriodId, BigDecimal value, Duration ttl) {
        if (StringUtils.isBlank(reportId) || StringUtils.isBlank(monthPeriodId) || value == null) {
            return;
        }
        String key = formatKey(reportId, monthPeriodId);
        String str = value.toPlainString();
        if (ttl == null || ttl.isZero() || ttl.isNegative()) {
            stringRedisTemplate.opsForValue().set(key, str);
            return;
        }
        stringRedisTemplate.opsForValue().set(key, str, ttl);
    }

    private String formatKey(String reportId, String monthPeriodId) {
        return String.format(RedisKeyConstants.CREDIT_REPORT_SERVICE_COUNT_TOTAL_BY_MONTH, reportId, monthPeriodId);
    }
}

