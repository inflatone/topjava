package ru.javaops.topjava.web.json;


import com.fasterxml.jackson.core.type.TypeReference;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import static ru.javaops.topjava.web.json.JacksonObjectMapper.getMapper;

public class JsonUtil {

    public static <T> List<T> readValues(String json, Class<T> clazz) {
        var reader = getMapper().readerFor(clazz);
        try {
            return reader.<T>readValues(json).readAll();
        } catch (IOException e) {
            throw new IllegalArgumentException("Invalid read from JSON:\n'" + json + '\'', e);
        }
    }

    public static <T> T readValue(String json, Class<T> clazz) {
        try {
            return getMapper().readValue(json, clazz);
        } catch (IOException e) {
            throw new IllegalArgumentException("Invalid read from JSON:\n'" + json + '\'', e);
        }
    }

    public static <T> String writeValue(T entity) {
        try {
            return getMapper().writeValueAsString(entity);
        } catch (IOException e) {
            throw new IllegalArgumentException("Invalid write to JSON:\n'" + entity + '\'', e);
        }
    }

    public static <T> String writeWithAdditionalProperty(T entity, String key, String value) {
        return writeWithAdditionalProperties(entity, Map.of(key, value));
    }

    public static <T> String writeWithAdditionalProperties(T entity, Map<String, String> properties) {
        var entityAsPropertyMap = getMapper().convertValue(entity, new TypeReference<Map<String, Object>>() {});
        entityAsPropertyMap.putAll(properties);
        return writeValue(entityAsPropertyMap);
    }
}
