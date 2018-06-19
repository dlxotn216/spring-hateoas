package me.strong.hateoas.common.application.model;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * Created by taesu on 2018-06-19.
 */
@Component
public class ObjectUtils {

    private static ObjectMapper objectMapper;

    @Autowired
    public ObjectUtils(ObjectMapper objectMapper) {
        ObjectUtils.objectMapper = objectMapper;
    }

    public static <T> T convertValue(Object fromValue, TypeReference<?> toValueTypeRef) throws IllegalArgumentException {
        return objectMapper.convertValue(fromValue, toValueTypeRef);
    }

}
