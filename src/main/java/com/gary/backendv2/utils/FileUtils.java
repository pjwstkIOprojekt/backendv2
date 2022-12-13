package com.gary.backendv2.utils;

import lombok.experimental.UtilityClass;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

@UtilityClass
public class FileUtils {

    public static String readFile(String path) {
        String fileContents;
        try {
            fileContents = Files.readString(Paths.get(path), StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return fileContents;
    }
}
