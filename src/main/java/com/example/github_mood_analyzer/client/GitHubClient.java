package com.example.github_mood_analyzer.client;

import com.example.github_mood_analyzer.dto.GitHubRepoDto;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class GitHubClient {

    private final RestTemplate restTemplate;

    //FIXME: RestTemplateBuilder
    public GitHubClient() {
        this.restTemplate = new RestTemplate();
    }

    public GitHubRepoDto[] getPullRequestComments(String owner, String repo) {
        String url =
                "https://api.github.com/repos/" + owner + "/" + repo + "/pulls/comments";

        return restTemplate
                .getForEntity(url, GitHubRepoDto[].class)
                .getBody();
    }
}
