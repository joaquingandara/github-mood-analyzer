package com.example.github_mood_analyzer.service;

import com.example.github_mood_analyzer.client.GitHubClient;
import com.example.github_mood_analyzer.dto.GitHubRepoDto;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class GithubService {
    private final GitHubClient gitHubClient;

    public GithubService(GitHubClient gitHubClient) {
        this.gitHubClient = gitHubClient;
    }

    public List<GitHubRepoDto> getAllPullRequestComments(String owner, String repo) {
        List<GitHubRepoDto> allComments = new ArrayList<>();
        int page = 1;

        while (true) {
            List<GitHubRepoDto> pageComments = gitHubClient.fetchPullRequestCommentsPage(owner, repo, page);

            if (pageComments.isEmpty()) {
                break;
            }

            allComments.addAll(pageComments);
            page++;
        }

        return allComments;
    }

    // For testing purposes
    public List<GitHubRepoDto> getFirstPagePullRequestComments(String owner, String repo) {
        return gitHubClient.fetchPullRequestCommentsPage(owner, repo, 1);
    }
}
