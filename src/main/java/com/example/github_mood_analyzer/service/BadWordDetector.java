package com.example.github_mood_analyzer.service;

import com.example.github_mood_analyzer.dto.BadWordsConfig;
import org.springframework.stereotype.Component;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.InputStream;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class BadWordDetector {

    private final Set<String> badWords;

    public BadWordDetector(ObjectMapper objectMapper) throws IOException {
         InputStream is = getClass()
                .getClassLoader()
                .getResourceAsStream("bad-words.json"); // Load JSON file from resources

        BadWordsConfig config =
                objectMapper.readValue(is, BadWordsConfig.class); // Deserialize JSON

        this.badWords = config.badWords.stream()
                .map(String::toLowerCase)
                .collect(Collectors.toSet());
    }

    public boolean containsBadWords(String text) {
        if (text == null || text.isBlank()) return false;

        String normalized = text
                .toLowerCase()
                .replaceAll("[^a-z\\s]", " "); // Remove punctuation

        for (String badWord : badWords) {
            if (normalized.contains(badWord)) {
                return true;
            }
        }

        return false;
    }


}
