package com.metaanalyser.controller;

import com.metaanalyser.model.AnalysisResult;
import com.metaanalyser.model.Post;
import com.metaanalyser.service.AnalyticsService;
import com.metaanalyser.service.MetaGraphService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class ReportController {

    private final MetaGraphService metaGraphService;
    private final AnalyticsService analyticsService;

    @Value("${ACCESS_TOKEN}")
    private String accessToken;

    @Value("${PAGE_ID}")
    private String pageId;

    public ReportController(MetaGraphService metaGraphService, AnalyticsService analyticsService) {
        this.metaGraphService = metaGraphService;
        this.analyticsService = analyticsService;
    }

    @GetMapping("/")
    public String report(Model model) {
        List<Post> posts = metaGraphService.fetchPosts(pageId, accessToken);
        AnalysisResult analysis = analyticsService.analyze(posts);

        model.addAttribute("posts", posts);
        model.addAttribute("analysis", analysis);

        return "report";
    }
}
