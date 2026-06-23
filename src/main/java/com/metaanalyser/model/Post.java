package com.metaanalyser.model;

import java.time.LocalDateTime;

public class Post {

    private String id;
    private String message;
    private String mediaUrl;
    private LocalDateTime createdTime;
    private int likes;
    private int comments;

    public Post() {}

    public Post(String id, String message, String mediaUrl, LocalDateTime createdTime, int likes, int comments) {
        this.id = id;
        this.message = message;
        this.mediaUrl = mediaUrl;
        this.createdTime = createdTime;
        this.likes = likes;
        this.comments = comments;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public String getMediaUrl() { return mediaUrl; }
    public void setMediaUrl(String mediaUrl) { this.mediaUrl = mediaUrl; }

    public LocalDateTime getCreatedTime() { return createdTime; }
    public void setCreatedTime(LocalDateTime createdTime) { this.createdTime = createdTime; }

    public int getLikes() { return likes; }
    public void setLikes(int likes) { this.likes = likes; }

    public int getComments() { return comments; }
    public void setComments(int comments) { this.comments = comments; }

    public int getEngagement() {
        return likes + comments;
    }

    public boolean isHasMedia() {
        return mediaUrl != null && !mediaUrl.isBlank();
    }
}
