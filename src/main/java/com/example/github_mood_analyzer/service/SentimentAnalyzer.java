package com.example.github_mood_analyzer.service;

import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class SentimentAnalyzer {

    private static final List<String> POSITIVE_WORDS =
            List.of("good", "nice", "well done", "clean", "great");

    private static final List<String> NEGATIVE_WORDS =
            List.of("bad", "wrong", "bug", "terrible", "fix this");

    public MoodService.SentimentResult analyze(String text) {
        if (text == null || text.isBlank()) {
            return new MoodService.SentimentResult(0.0, MoodService.SentimentLabel.NEUTRAL);
        }

        String lower = text.toLowerCase();

        double score = 0;

        for (String p : POSITIVE_WORDS) {
            if (lower.contains(p)) score += 1;
        }

        for (String n : NEGATIVE_WORDS) {
            if (lower.contains(n)) score -= 1;
        }

        score = Math.max(-1, Math.min(1, score));

        MoodService.SentimentLabel label =
                score > 0 ? MoodService.SentimentLabel.POSITIVE :
                        score < 0 ? MoodService.SentimentLabel.NEGATIVE :
                                MoodService.SentimentLabel.NEUTRAL;

        return new MoodService.SentimentResult(score, label);
    }
}

