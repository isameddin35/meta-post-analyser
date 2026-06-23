package com.metaanalyser.service;

import com.metaanalyser.model.AnalysisResult;
import com.metaanalyser.model.Post;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class AnalyticsService {

    public AnalysisResult analyze(List<Post> posts) {
        if (posts.isEmpty()) {
            return new AnalysisResult(Collections.emptyList(), "N/A", 0.0,
                    "No posts found to analyse.");
        }

        List<Post> top3 = posts.stream()
                .sorted(Comparator.comparingInt(Post::getEngagement).reversed())
                .limit(3)
                .collect(Collectors.toList());

        Map<DayOfWeek, DoubleSummaryStatistics> dayStats = posts.stream()
                .filter(p -> p.getCreatedTime() != null)
                .collect(Collectors.groupingBy(
                        p -> p.getCreatedTime().getDayOfWeek(),
                        Collectors.summarizingDouble(Post::getLikes)
                ));

        Map.Entry<DayOfWeek, DoubleSummaryStatistics> best = dayStats.entrySet().stream()
                .max(Comparator.comparingDouble(e -> e.getValue().getAverage()))
                .orElse(null);

        String bestDay = "N/A";
        double bestDayAvgLikes = 0.0;
        if (best != null) {
            bestDay = best.getKey().name();
            bestDayAvgLikes = best.getValue().getAverage();
        }

        Post topPost = top3.get(0);
        String summary = String.format(
                "Posts shared on %s receive the highest average likes (%.1f). " +
                "The top post has %d engagements (%d likes, %d comments). " +
                "Overall average engagement per post: %.1f.",
                bestDay, bestDayAvgLikes,
                topPost.getEngagement(), topPost.getLikes(), topPost.getComments(),
                posts.stream().mapToInt(Post::getEngagement).average().orElse(0.0)
        );

        return new AnalysisResult(top3, bestDay, bestDayAvgLikes, summary);
    }
}
