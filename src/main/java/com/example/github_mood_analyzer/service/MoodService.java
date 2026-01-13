package com.example.github_mood_analyzer.service;

import com.example.github_mood_analyzer.client.GitHubClient;
import com.example.github_mood_analyzer.dto.GitHubRepoDto;
import com.example.github_mood_analyzer.dto.MoodResponse;
import org.springframework.stereotype.Service;

import java.util.Arrays;

@Service
public class MoodService {
    private final GitHubClient gitHubClient;

    public MoodService(GitHubClient gitHubClient) {
        this.gitHubClient = gitHubClient;
    }

    public MoodResponse analyzeRepoMood(String owner, String repo) {
        GitHubRepoDto [] comments =
                gitHubClient.getPullRequestComments(owner, repo);

        String moodStatus =  calculateMood(comments);

        return new MoodResponse(
                owner + "/" + repo,
                "pull_request_comments",
                comments.length,
                moodStatus
        );
    }

    private static String calculateMood(GitHubRepoDto[] comments) {
        if (comments == null || comments.length == 0) {
            return "No comments";
        }

        long positive = Arrays.stream(comments)
                .filter(c -> c.getBody().toLowerCase().contains("good")
                        || c.getBody().toLowerCase().contains("great")
                        || c.getBody().toLowerCase().contains("thanks"))
                .count();

        if (positive > comments.length / 2) {
            return "Positive repo vibe";
        }

        return "Neutral";
    }
}
