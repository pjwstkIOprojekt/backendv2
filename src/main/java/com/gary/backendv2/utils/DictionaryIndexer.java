package com.gary.backendv2.utils;

import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class DictionaryIndexer {
    private final String dictionaryPath = "classpath:words_alpha.txt";
    private static DictionaryIndexer instance;
    public static DictionaryIndexer getInstance() {
        if (instance == null) {
            instance = new DictionaryIndexer();
        }
        return instance;
    }

    public Map<String, List<String>> indexedDictionary = new HashMap<>();

    public void index() throws IOException {
        for (char letter = 'a'; letter <= 'z'; letter++) {
            indexedDictionary.put(String.valueOf(letter), new ArrayList<>());
        }

        ResourceLoader resourceLoader = new DefaultResourceLoader();
        Resource resource = resourceLoader.getResource(dictionaryPath);

        Scanner sc = new Scanner(resource.getInputStream(), StandardCharsets.UTF_8);
        while (sc.hasNextLine()) {
            String line = sc.nextLine();
            String firstLetter = String.valueOf(line.charAt(0));

            indexedDictionary.get(firstLetter).add(line);
        }

        if (sc.ioException() != null) {
            throw sc.ioException();
        }
    }
}
