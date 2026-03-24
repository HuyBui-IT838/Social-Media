package com.example.Social.Media.controller;

import com.example.Social.Media.dto.CommentRequest;
import com.example.Social.Media.dto.CommentResponse;
import com.example.Social.Media.service.CommentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/comments")
@RequiredArgsConstructor
@Tag(name = "Bình luận (Comments)", description = "Yêu cầu phải Đăng Nhập (JWT Token) mới được tham gia bình luận")
public class CommentController {

    private final CommentService commentService;

    @PostMapping
    @Operation(summary = "Thêm một bình luận vô bài viết dựa vào Token đang đăng nhập", security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<CommentResponse> addComment(@Valid @RequestBody CommentRequest request) {
        return ResponseEntity.ok(commentService.addComment(request));
    }

    @GetMapping("/post/{title}")
    @Operation(summary = "Lấy mọi bình luận của một bài đăng cụ thể bằng Tiêu đề")
    public ResponseEntity<List<CommentResponse>> getCommentsByPost(@PathVariable String title) {
        return ResponseEntity.ok(commentService.getCommentsByPost(title));
    }


    @DeleteMapping("/{commentId}")
    @Operation(summary = "Xóa bình luận", security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<String> deleteComment(@PathVariable Long commentId) {
        commentService.deleteComment(commentId);
        return ResponseEntity.ok("Xóa bình luận thành công.");
    }
}
