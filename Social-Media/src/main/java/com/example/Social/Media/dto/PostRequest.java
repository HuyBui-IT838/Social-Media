package com.example.Social.Media.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostRequest {

    
    @NotBlank(message = "Tiêu đề bài viết không được để trống")
    private String title;
    
    @NotBlank(message = "Nội dung bài viết không được để trống")
    private String content;

}
