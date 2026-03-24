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
public class CommentResponse {
    private Long id;
    private String postTitle;

    private String content;
    private String authorName; // Người bình luận lấy từ Jwt
    private LocalDateTime createdAt;
}
