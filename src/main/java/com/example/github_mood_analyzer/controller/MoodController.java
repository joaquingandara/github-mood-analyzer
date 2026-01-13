package com.example.github_mood_analyzer.controller;

import com.example.github_mood_analyzer.service.MoodService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/mood")
public class MoodController {

    private final MoodService moodService;

    public MoodController(MoodService moodService) {
        this.moodService = moodService;
    }

    @GetMapping("/{owner}/{repo}")
    public String mood(
            @PathVariable String owner,
            @PathVariable String repo
    ) {
        return moodService.analyzeRepoMood(owner, repo);
    }
}

