package com.gary.backendv2.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

public class MaptilerConstants {
    @Value("${maptiler.api.key}")
    private String apiKey;

    public String getApiKey() {
        return this.apiKey;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    private final String BASE_URL = "https://api.maptiler.com";
    private final String GEOCODING_SERVICE = BASE_URL + "/geocoding/{{LAT}},{{LONG}}.json?key={{KEY}}&language={{LANG}}";

    public String createGeoCodingURL(Double longitude, Double latitude) {
        return GEOCODING_SERVICE
                .replace("{{LONG}}", String.valueOf(longitude))
                .replace("{{LAT}}", String.valueOf(latitude))
                .replace("{{LANG}}", "en")
                .replace("{{KEY}}", apiKey);
    }

    public String createGeoCodingURL(Double longitude, Double latitude, Language language ) {
        return GEOCODING_SERVICE
                .replace("{{LONG}}", String.valueOf(longitude))
                .replace("{{LAT}}", String.valueOf(latitude))
                .replace("{{LANG}}", language.getLanguageCode())
                .replace("{{KEY}}", apiKey);
    }
}
