package com.example.Social.Media.controller;

import com.example.Social.Media.dto.PostRequest;
import com.example.Social.Media.dto.PostResponse;
import com.example.Social.Media.service.PostService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
// import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/posts")
@RequiredArgsConstructor
@Tag(name = "Bài đăng (Posts)", description = "Quản lý bài viết của người dùng và bảng tin")
public class PostController {

    private final PostService postService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Tạo bài viết mới kèm ảnh (Chọn file ảnh)", security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<PostResponse> createPost(
            @RequestParam("title") String title,
            @RequestParam("content") String content,
            @RequestParam(value = "image", required = false) MultipartFile image) throws IOException {
        
        PostRequest request = new PostRequest(title, content);
        return ResponseEntity.ok(postService.createPost(request, image));
    }

    @GetMapping
    @Operation(summary = "Lấy tất cả bài viết trên bảng tin")
    public ResponseEntity<List<PostResponse>> getAllPosts() {
        return ResponseEntity.ok(postService.getAllPosts());
    }

    @GetMapping("/username/{username}")
    @Operation(summary = "Lấy toàn bộ bài viết dựa theo Username")
    public ResponseEntity<List<PostResponse>> getPostsByUsername(@PathVariable String username) {
        return ResponseEntity.ok(postService.getPostsByUsername(username));
    }

    @GetMapping("/me")
    @Operation(summary = "Lấy toàn bộ bài viết của chính tôi", security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<List<PostResponse>> getMyPosts() {
        return ResponseEntity.ok(postService.getMyPosts());
    }

    @GetMapping("/search")
    @Operation(summary = "Tìm kiếm bài viết theo tiêu đề")
    public ResponseEntity<List<PostResponse>> searchPosts(@RequestParam String title) {
        return ResponseEntity.ok(postService.searchPostsByTitle(title));
    }

    @DeleteMapping("/{title}")
    @Operation(summary = "Xóa bài viết bằng Tiêu đề", security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<String> deletePost(@PathVariable String title) {
        postService.deletePost(title);
        return ResponseEntity.ok("Xóa bài viết thành công!");
    }

    @PostMapping("/{title}/like")
    @Operation(summary = "Thích hoặc bỏ thích bài viết", security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<String> toggleLike(@PathVariable String title) {
        return ResponseEntity.ok(postService.toggleLike(title));
    }
}
