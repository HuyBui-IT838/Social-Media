package com.example.Social.Media.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CommentRequest {

    @NotBlank(message = "Tiêu đề của bài viết không được trống")
    private String postTitle;


    @NotBlank(message = "Nội dung bình luận không được để trống")
    private String content;

    // KHÔNG cn userId nữa! Hệ thống sẽ tự dùng JWT Token để lấy thông tin người bình luận!
}
