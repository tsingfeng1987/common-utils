package com.qing.common.utils.json;

import com.fasterxml.jackson.core.JsonProcessingException;
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

    public static Object stringToJson(String str, Class<?> clazz) throws IOException {

        ObjectMapper objectMapper = new ObjectMapper();

        return objectMapper.readValue(str, clazz);
    }

    public static List<Object> stringToList(String str, Class clazz) {
        return null;
    }
}

