package com.gary.backendv2.utils;

public enum Language {

    POLISH("pl"),
    ENGLISH("en");

    private final String languageCode;

    private Language(String languageCode) {
        this.languageCode = languageCode;
    }

    public String getLanguageCode() {
        return this.languageCode;
    }
}
