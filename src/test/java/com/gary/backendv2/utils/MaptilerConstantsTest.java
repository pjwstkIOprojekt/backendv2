package com.gary.backendv2.utils;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.util.ReflectionTestUtils;

import java.lang.reflect.Field;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class MaptilerConstantsTest {

    @Test
    public void testMapTilerURLDefaultLang()  {
        MaptilerConstants maptiler = new MaptilerConstants();
        maptiler.setApiKey("API_KEY");

        String expectedUrl = "https://api.maptiler.com/geocoding/52.0,14.0.json?key=API_KEY&language=en";

        String url = maptiler.createGeoCodingURL(52., 14.);

        assertEquals(expectedUrl, url);
    }

    @ParameterizedTest
    @EnumSource(Language.class)
    public void testMapTilerURLDefaultProvidedLang(Language language)  {
        MaptilerConstants maptiler = new MaptilerConstants();
        maptiler.setApiKey("API_KEY");

        String expectedUrl = "https://api.maptiler.com/geocoding/52.0,14.0.json?key=API_KEY&language=" + language.getLanguageCode();

        String url = maptiler.createGeoCodingURL(52., 14., language);

        assertEquals(expectedUrl, url);
    }
}
