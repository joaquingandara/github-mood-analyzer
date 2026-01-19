package com.example.github_mood_analyzer.service;

import com.example.github_mood_analyzer.dto.GitHubRepoDto;
import com.example.github_mood_analyzer.dto.MoodResponse;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MoodService {
    private final GithubService githubService;

    private final StanfordSentimentAnalyzer sentimentAnalyzer;

    private final BadWordDetector badWordDetector;

    public enum SentimentLabel {
        POSITIVE,
        NEUTRAL,
        NEGATIVE
    }

    public MoodService(GithubService gitHubClient, StanfordSentimentAnalyzer sentimentAnalyzer, BadWordDetector badWordDetector) {
        this.githubService = gitHubClient;
        this.sentimentAnalyzer = sentimentAnalyzer;
        this.badWordDetector = badWordDetector;
    }

    public MoodResponse analyzeRepoMood(String owner, String repo) {
        List<GitHubRepoDto> comments =
                githubService.getFirstPagePullRequestComments(owner, repo);

        String moodStatus =  calculateMood(comments).toString();

        boolean hasBadWords =  comments.stream().anyMatch(comment -> badWordDetector.containsBadWords(comment.getBody()));

        return new MoodResponse(
                owner + "/" + repo,
                "pull_request_comments",
                comments.size(),
                moodStatus,
                hasBadWords ? "Contains bad words" : "No bad words"
        );
    }

    public SentimentLabel calculateMood(List<GitHubRepoDto> comments) {

        if (comments.isEmpty()) return SentimentLabel.NEUTRAL;

        double totalScore = 0;

        for (GitHubRepoDto comment : comments) {
            SentimentLabel result = sentimentAnalyzer.analyze(comment.getBody());
            totalScore += switch (result) {
                case POSITIVE -> 1;
                case NEGATIVE -> 0;
                case NEUTRAL-> -1;
            };
        }

        double avg = totalScore / comments.size();

        if (avg > 0.2) return SentimentLabel.POSITIVE;
        if (avg < -0.2) return SentimentLabel.NEGATIVE;
        return SentimentLabel.NEUTRAL;
    }


}
