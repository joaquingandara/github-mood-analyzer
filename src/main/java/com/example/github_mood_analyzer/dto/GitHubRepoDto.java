package com.example.github_mood_analyzer.dto;

import lombok.Getter;

public class GitHubRepoDto {

    @Getter
    private String body;
    private UserDto user;

    public static class UserDto {
        private String login;
    }
}
