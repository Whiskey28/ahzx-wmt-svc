package com.wmt.module.credit.framework.mybatis;

import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wmt.framework.common.util.json.JsonUtils;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;
import org.apache.ibatis.type.MappedTypes;
import org.postgresql.util.PGobject;

import java.lang.reflect.Field;
import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

/**
 * PostgreSQL JSONB 类型处理器
 * 用于处理 PostgreSQL 的 JSONB 字段类型
 *
 * @author AHC源码
 */
@MappedTypes({Map.class})
@MappedJdbcTypes(JdbcType.OTHER)
public class JsonbTypeHandler extends JacksonTypeHandler {

    private static final String JSONB = "jsonb";
    private static final String JSON = "json";

    public JsonbTypeHandler() {
        super(Object.class);
    }

    public JsonbTypeHandler(Class<?> type) {
        super(type);
    }

    public JsonbTypeHandler(Class<?> type, Field field) {
        super(type, field);
    }

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, Object parameter, JdbcType jdbcType) throws SQLException {
        try {
            // 将Map序列化为JSON字符串
            ObjectMapper objectMapper = JsonUtils.getObjectMapper();
            String json = objectMapper.writeValueAsString(parameter);
            
            // 创建PGobject并设置为jsonb类型
            PGobject pgObject = new PGobject();
            pgObject.setType(JSONB);
            pgObject.setValue(json);
            
            ps.setObject(i, pgObject);
        } catch (Exception e) {
            throw new SQLException("Failed to convert parameter to JSONB", e);
        }
    }

    @Override
    public Object getNullableResult(ResultSet rs, String columnName) throws SQLException {
        Object o = rs.getObject(columnName);
        return parseDbObject(o);
    }

    @Override
    public Object getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        Object o = rs.getObject(columnIndex);
        return parseDbObject(o);
    }

    @Override
    public Object getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        Object o = cs.getObject(columnIndex);
        return parseDbObject(o);
    }

    /**
     * 解析数据库对象
     */
    @SuppressWarnings("unchecked")
    private Object parseDbObject(Object o) throws SQLException {
        if (o == null) {
            return null;
        }
        
        try {
            ObjectMapper objectMapper = JsonUtils.getObjectMapper();
            String json;
            
            // 如果是PGobject，提取JSON字符串
            if (o instanceof PGobject) {
                PGobject pgObject = (PGobject) o;
                String type = pgObject.getType();
                if (JSONB.equalsIgnoreCase(type) || JSON.equalsIgnoreCase(type)) {
                    json = pgObject.getValue();
                } else {
                    json = pgObject.getValue();
                }
            } else if (o instanceof String) {
                json = (String) o;
            } else {
                json = o.toString();
            }
            
            if (json == null || json.trim().isEmpty()) {
                return null;
            }
            
            // 解析JSON字符串为Map
            return objectMapper.readValue(json, new TypeReference<Map<String, Object>>() {});
        } catch (Exception e) {
            throw new SQLException("Failed to parse JSONB: " + o, e);
        }
    }
}
