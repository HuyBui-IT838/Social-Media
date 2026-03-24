package com.example.Social.Media.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthResponse {
    private String message;
    private String username;
    private String role;
    private String token; // Dành cho sau này, tạm thời để rỗng hoặc ghi chú
}
