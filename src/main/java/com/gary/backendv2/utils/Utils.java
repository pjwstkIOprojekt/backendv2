package com.gary.backendv2.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import lombok.SneakyThrows;

public class Utils {

    @SneakyThrows
    public static String POJOtoJsonString(Object object) {
        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();

        return ow.writeValueAsString(object);
    }
}
