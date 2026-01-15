package com.example.github_mood_analyzer.service;

import com.example.github_mood_analyzer.client.GitHubClient;
import com.example.github_mood_analyzer.dto.GitHubRepoDto;
import com.example.github_mood_analyzer.dto.MoodResponse;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MoodService {
    private final GitHubClient gitHubClient;

    public MoodService(GitHubClient gitHubClient) {
        this.gitHubClient = gitHubClient;
    }

    public MoodResponse analyzeRepoMood(String owner, String repo) {
        List<GitHubRepoDto> comments =
                gitHubClient.getFirstPagePullRequestComments(owner, repo);

        String moodStatus =  calculateMood(comments);

        return new MoodResponse(
                owner + "/" + repo,
                "pull_request_comments",
                comments.size(),
                moodStatus
        );
    }

    private static String calculateMood(List<GitHubRepoDto> comments) {
        if (comments == null || comments.size() == 0) {
            return "No comments";
        }

        long positive = comments.stream()
                .filter(c -> c.getBody().toLowerCase().contains("good")
                        || c.getBody().toLowerCase().contains("great")
                        || c.getBody().toLowerCase().contains("thanks"))
                .count();

        if (positive > comments.size() / 2) {
            return "Positive repo vibe";
        }

        return "Neutral";
    }
}
