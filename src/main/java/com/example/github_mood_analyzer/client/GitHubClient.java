package com.example.github_mood_analyzer.client;

import com.example.github_mood_analyzer.dto.GitHubRepoDto;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
public class GitHubClient {

    private final RestTemplate restTemplate;

    //FIXME: RestTemplateBuilder
    public GitHubClient() {
        this.restTemplate = new RestTemplate();
    }

    public List<GitHubRepoDto> getPullRequestComments(String owner, String repo) {
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

    private List<GitHubRepoDto> fetchPullRequestCommentsPage(String owner, String repo, int page) {
        String url = String.format(
                "https://api.github.com/repos/%s/%s/pulls/comments?per_page=100&page=%d",
                owner,
                repo,
                page
        );

        GitHubRepoDto[] response = restTemplate.getForObject(url, GitHubRepoDto[].class);

        return (response == null || response.length == 0) ? List.of() : Arrays.asList(response);
    }

}
