package com.example.Social.Media.controller;

import com.example.Social.Media.dto.UserResponse;
import com.example.Social.Media.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Tag(name = "Người dùng (Users)", description = "Quản lý thông tin người dùng")
public class UserController {

    private final UserService userService;

    @GetMapping("/username/{username}")
    @Operation(summary = "Lấy thông tin người dùng bằng Username")
    public ResponseEntity<UserResponse> getUserByUsername(@PathVariable String username) {
        return ResponseEntity.ok(userService.getUserByUsername(username));
    }

    @PostMapping("/follow/{username}")

    @io.swagger.v3.oas.annotations.security.SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "Theo dõi hoặc bỏ theo dõi người dùng bằng Username")
    public ResponseEntity<String> toggleFollow(@PathVariable String username) {
        return ResponseEntity.ok(userService.toggleFollow(username));
    }
}

