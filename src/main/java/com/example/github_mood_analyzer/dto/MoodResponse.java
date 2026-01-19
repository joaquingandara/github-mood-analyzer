package com.example.github_mood_analyzer.dto;

import lombok.Getter;

@Getter
public class MoodResponse {

    private String repository;
    private String source;
    private int totalComments;
    private String overallMood;
    private String badWordsMsg;

    public MoodResponse(String repository, String source, int totalComments, String overallMood, String badWordsMsg) {
        this.repository = repository;
        this.source = source;
        this.totalComments = totalComments;
        this.overallMood = overallMood;
        this.badWordsMsg = badWordsMsg;
    }

}

