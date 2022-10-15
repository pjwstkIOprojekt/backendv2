package com.gary.backendv2.utils;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class EnumUtils {
    public static List<String> getEnumValues(Class <? extends Enum<?>> enumClass) {
        return Stream.of(enumClass.getEnumConstants())
                .map(Enum::name)
                .collect(Collectors.toList());
    }
}
