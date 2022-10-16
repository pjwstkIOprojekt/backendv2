package com.gary.backendv2.utils;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class EnumUtilsTest {

    @Test
    void getEnumValues() {
        enum TestEnum {
            OK, TEST, HELLO, WORLD
        }

        List<String> expected = List.of("OK", "TEST", "HELLO", "WORLD");
        List<String> actual = EnumUtils.getEnumValues(TestEnum.class);

        assertEquals(expected.size(), actual.size());
        assertEquals(expected, actual);
    }
}