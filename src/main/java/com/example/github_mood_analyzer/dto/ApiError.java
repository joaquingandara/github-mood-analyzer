package com.example.github_mood_analyzer.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ApiError {
    private int status;
    private String message;
}