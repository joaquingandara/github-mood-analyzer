package com.example.github_mood_analyzer.dto;

import lombok.Getter;

@Getter
public class MoodResponse {

    private String repository;
    private String source;
    private int totalComments;
    private String overallMood;

    public MoodResponse(String repository, String source, int totalComments, String overallMood) {
        this.repository = repository;
        this.source = source;
        this.totalComments = totalComments;
        this.overallMood = overallMood;
    }

}

