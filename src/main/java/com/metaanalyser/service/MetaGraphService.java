package com.metaanalyser.service;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.metaanalyser.model.Post;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MetaGraphService {

    private static final String GRAPH_API_URL =
            "https://graph.facebook.com/v19.0/%s/posts?fields=message,story,full_picture,attachments,created_time,likes.summary(true),comments.summary(true)&limit=20&access_token=%s";

    private static final DateTimeFormatter FB_DATE_FORMAT =
            DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssZ");

    private final RestTemplate restTemplate;

    public MetaGraphService() {
        this.restTemplate = new RestTemplate();
    }

    public List<Post> fetchPosts(String pageId, String accessToken) {
        String url = String.format(GRAPH_API_URL, pageId, accessToken);
        GraphResponse response = restTemplate.getForObject(url, GraphResponse.class);

        if (response == null || response.data == null) {
            return Collections.emptyList();
        }

        return response.data.stream()
                .map(this::toPost)
                .collect(Collectors.toList());
    }

    private Post toPost(GraphPost gp) {
        String id = gp.id;
        String displayText = resolveDisplayText(gp);
        String mediaUrl = resolveMediaUrl(gp);
        LocalDateTime createdTime = parseDate(gp.createdTime);
        int likes = (gp.likes != null && gp.likes.summary != null) ? gp.likes.summary.totalCount : 0;
        int comments = (gp.comments != null && gp.comments.summary != null) ? gp.comments.summary.totalCount : 0;
        return new Post(id, displayText, mediaUrl, createdTime, likes, comments);
    }

    private String resolveDisplayText(GraphPost gp) {
        if (gp.message != null && !gp.message.isBlank()) {
            return gp.message;
        }
        if (gp.story != null && !gp.story.isBlank()) {
            return gp.story;
        }
        if (gp.attachments != null && gp.attachments.data != null && !gp.attachments.data.isEmpty()) {
            String type = gp.attachments.data.get(0).type;
            if (type != null) {
                return switch (type) {
                    case "photo" -> "📷 Photo";
                    case "video" -> "🎬 Video";
                    case "album" -> "📸 Album";
                    default -> "📎 " + type.substring(0, 1).toUpperCase() + type.substring(1);
                };
            }
        }
        return "(no text)";
    }

    private String resolveMediaUrl(GraphPost gp) {
        if (gp.fullPicture != null && !gp.fullPicture.isBlank()) {
            return gp.fullPicture;
        }
        if (gp.attachments != null && gp.attachments.data != null && !gp.attachments.data.isEmpty()) {
            AttachmentData first = gp.attachments.data.get(0);
            if (first.media != null && first.media.image != null) {
                return first.media.image.src;
            }
        }
        return null;
    }

    private LocalDateTime parseDate(String dateStr) {
        try {
            OffsetDateTime odt = OffsetDateTime.parse(dateStr, FB_DATE_FORMAT);
            return odt.toLocalDateTime();
        } catch (Exception e) {
            return null;
        }
    }

    // ---- Inner DTOs for JSON deserialization ----

    @JsonIgnoreProperties(ignoreUnknown = true)
    static class GraphResponse {
        public List<GraphPost> data;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    static class GraphPost {
        public String id;
        public String message;
        public String story;
        @JsonProperty("full_picture")
        public String fullPicture;
        public Attachments attachments;
        @JsonProperty("created_time")
        public String createdTime;
        public LikeComments likes;
        public LikeComments comments;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    static class LikeComments {
        public Summary summary;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    static class Summary {
        @JsonProperty("total_count")
        public int totalCount;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    static class Attachments {
        public List<AttachmentData> data;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    static class AttachmentData {
        public String type;
        public Media media;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    static class Media {
        public MediaImage image;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    static class MediaImage {
        public String src;
    }
}
