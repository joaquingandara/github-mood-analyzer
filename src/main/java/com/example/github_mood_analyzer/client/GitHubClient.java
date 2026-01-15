package com.example.github_mood_analyzer.client;

import com.example.github_mood_analyzer.dto.GitHubRepoDto;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Slf4j
@Component
public class GitHubClient {

    private final RestTemplate restTemplate;

    @Value("${github.token}")
    private String githubToken;

    //FIXME: RestTemplateBuilder
    public GitHubClient() {
        this.restTemplate = new RestTemplate();
    }

    public List<GitHubRepoDto> getAllPullRequestComments(String owner, String repo) {
        List<GitHubRepoDto> allComments = new ArrayList<>();
        int page = 1;

        while (true) {
            List<GitHubRepoDto> pageComments = fetchPullRequestCommentsPage(owner, repo, page);

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
        return fetchPullRequestCommentsPage(owner, repo, 1);
    }

    private List<GitHubRepoDto> fetchPullRequestCommentsPage(String owner, String repo, int page) {
        String url = String.format(
                "https://api.github.com/repos/%s/%s/pulls/comments?per_page=100&page=%d",
                owner,
                repo,
                page
        );

        ResponseEntity<GitHubRepoDto[]> response =
                restTemplate.exchange(
                        url,
                        HttpMethod.GET,
                        requestEntity(),
                        GitHubRepoDto[].class
                );

        logGithubApiRequestsLimit(response);

        GitHubRepoDto[] body = response.getBody();

        return (body == null || body.length == 0) ? List.of() : Arrays.asList(body);
    }

    private static void logGithubApiRequestsLimit(ResponseEntity<GitHubRepoDto[]> response) {
        HttpHeaders headers = response.getHeaders();
        String remaining = headers.getFirst("X-RateLimit-Remaining");
        log.info("GitHub rate limit: {}/{}", remaining);
    }

    private HttpEntity<Void> requestEntity() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Accept", "application/vnd.github+json");
        headers.set("Authorization", "Bearer " + githubToken);
        headers.set("User-Agent", "github-mood-analyzer");
        return new HttpEntity<>(headers);
    }

}
