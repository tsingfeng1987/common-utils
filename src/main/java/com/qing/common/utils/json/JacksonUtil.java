package com.qing.common.utils.json;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.List;

/**
 * Created by Guoqingfeng on 2017/10/10.
 */
public class JacksonUtil {
    public static String jsonToString(Object o) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();

        return objectMapper.writeValueAsString(o);

    }

    public static <T> T stringToJson(String str, Class<T> clazz) throws IOException {

        ObjectMapper objectMapper = new ObjectMapper();

        return objectMapper.readValue(str, clazz);
    }

    public static <T> List<T> stringToList(String str, Class<T> clazz) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        JavaType javaType = objectMapper.getTypeFactory().constructParametricType(List.class, clazz);
        return objectMapper.readValue(str,javaType);
    }
}

