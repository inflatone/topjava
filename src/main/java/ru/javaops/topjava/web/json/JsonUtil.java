package ru.javaops.topjava.web.json;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectWriter;

import java.io.IOException;
import java.util.List;

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

    public static <T> String writeValue(T entity, ObjectWriter writer) {
        try {
            return writer.writeValueAsString(entity);
        } catch (JsonProcessingException e) {
            throw new IllegalStateException("Invalid write to JSON:\n'" + entity + "'", e);
        }
    }
}
