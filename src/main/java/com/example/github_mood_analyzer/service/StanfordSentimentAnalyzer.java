package com.example.github_mood_analyzer.service;

import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.sentiment.SentimentCoreAnnotations;
import edu.stanford.nlp.util.CoreMap;
import org.springframework.stereotype.Component;

import java.util.Properties;

@Component
public class StanfordSentimentAnalyzer {

    private final StanfordCoreNLP pipeline;

    public StanfordSentimentAnalyzer() {
        Properties props = new Properties();
        props.setProperty("annotators", "tokenize,ssplit,parse,sentiment"); // Specify the annotators
        this.pipeline = new StanfordCoreNLP(props);
    }

    public int sentimentScore(String text) {
        if (text == null || text.isBlank()) return 2; // Neutral score

        Annotation annotation = new Annotation(text);
        pipeline.annotate(annotation);

        int total = 0;
        int count = 0;

        for (CoreMap sentence : annotation.get(CoreAnnotations.SentencesAnnotation.class)) {
            String sentimentLabel = sentence.get(SentimentCoreAnnotations.SentimentClass.class);
            int sentiment = switch (sentimentLabel) {
                case "Very negative" -> 0;
                case "Negative" -> 1;
                case "Neutral" -> 2;
                case "Positive" -> 3;
                default -> 4;
            };

            total += sentiment;
            count++;
        }


        return count == 0 ? 2 : total / count;
    }

    public MoodService.SentimentLabel analyze(String text) {
        int score = sentimentScore(text);

        if (score <= 1) return MoodService.SentimentLabel .NEGATIVE;
        if (score == 2) return MoodService.SentimentLabel .NEUTRAL;
        return MoodService.SentimentLabel .POSITIVE;
    }
}


