package com.example.Social.Media.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserResponse {
    private Long id;
    private String username;
    private String email;
    private String role;
    private long followerCount;
    private long followingCount;
}

