package com.example.Social.Media.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PostResponse {
    private Long id;
    private String title;
    private String content;
    private String authorName; // Lấy từ username của User
    private String imageUrl;


    private LocalDateTime createdAt;
    private long likeCount;
    private long commentCount;
}

