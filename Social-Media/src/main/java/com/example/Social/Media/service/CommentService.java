package com.example.Social.Media.service;

import com.example.Social.Media.dto.CommentRequest;
import com.example.Social.Media.dto.CommentResponse;
import com.example.Social.Media.entity.Comment;
import com.example.Social.Media.entity.Post;
import com.example.Social.Media.entity.User;
import com.example.Social.Media.repository.CommentRepository;
import com.example.Social.Media.repository.PostRepository;
import com.example.Social.Media.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    @Transactional
    public CommentResponse addComment(CommentRequest request) {
        // Trích xuất Email đang đăng nhập từ hệ thống bảo mật Jwt
        String userEmail = SecurityContextHolder.getContext().getAuthentication().getName();

        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("Bạn phải đăng nhập để bình luận!"));

        Post post = postRepository.findByTitle(request.getPostTitle())
                .orElseThrow(() -> new RuntimeException("Bài viết với tiêu đề này không tồn tại."));


        Comment comment = Comment.builder()
                .post(post)
                .user(user)
                .content(request.getContent())
                .build();

        Comment savedComment = commentRepository.save(comment);
        return mapToResponse(savedComment);
    }

    public List<CommentResponse> getCommentsByPost(String title) {
        Post post = postRepository.findByTitle(title)
                .orElseThrow(() -> new RuntimeException("Bài viết không tồn tại!"));

        return commentRepository.findByPostIdOrderByCreatedAtAsc(post.getId())
                .stream().map(this::mapToResponse)
                .collect(Collectors.toList());
    }


    @Transactional
    public void deleteComment(Long commentId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new RuntimeException("Bình luận không tồn tại."));

        String userEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        User currentUser = userRepository.findByEmail(userEmail).orElseThrow();

        // Chỉ được xóa nếu: (1) Mình là chủ comment OR (2) Mình là Admin OR (3) Mình là CHỦ BÀI VIẾT ĐÓ!
        boolean isCommentOwner = comment.getUser().getId().equals(currentUser.getId());
        boolean isAdmin = currentUser.getRole().getName().equals("ADMIN");
        boolean isPostOwner = comment.getPost().getUser().getId().equals(currentUser.getId());

        if (isCommentOwner || isAdmin || isPostOwner) {
            commentRepository.delete(comment);
        } else {
            throw new RuntimeException("Bạn không có quyền xóa bình luận này!");
        }
    }

    private CommentResponse mapToResponse(Comment comment) {
        return CommentResponse.builder()
                .id(comment.getId())
                .postTitle(comment.getPost().getTitle())
                .content(comment.getContent())
                .authorName(comment.getUser().getUsername())// Không trả nguyên cục User gây vòng lặp Swagger
                .createdAt(comment.getCreatedAt())
                .build();

    }
}
