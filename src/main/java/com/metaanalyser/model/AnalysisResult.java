package com.metaanalyser.model;

import java.util.List;

public class AnalysisResult {

    private List<Post> top3;
    private String bestDay;
    private double bestDayAvgLikes;
    private String summary;

    public AnalysisResult() {}

    public AnalysisResult(List<Post> top3, String bestDay, double bestDayAvgLikes, String summary) {
        this.top3 = top3;
        this.bestDay = bestDay;
        this.bestDayAvgLikes = bestDayAvgLikes;
        this.summary = summary;
    }

    public List<Post> getTop3() { return top3; }
    public void setTop3(List<Post> top3) { this.top3 = top3; }

    public String getBestDay() { return bestDay; }
    public void setBestDay(String bestDay) { this.bestDay = bestDay; }

    public double getBestDayAvgLikes() { return bestDayAvgLikes; }
    public void setBestDayAvgLikes(double bestDayAvgLikes) { this.bestDayAvgLikes = bestDayAvgLikes; }

    public String getSummary() { return summary; }
    public void setSummary(String summary) { this.summary = summary; }
}
