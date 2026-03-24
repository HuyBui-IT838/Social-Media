package com.example.Social.Media.service;

import com.example.Social.Media.dto.PostRequest;
import com.example.Social.Media.dto.PostResponse;
import com.example.Social.Media.entity.Post;
import com.example.Social.Media.entity.PostLike;
import com.example.Social.Media.entity.User;
import com.example.Social.Media.repository.CommentRepository;
import com.example.Social.Media.repository.LikeRepository;
import com.example.Social.Media.repository.PostRepository;
import com.example.Social.Media.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Base64;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final LikeRepository likeRepository;
    private final CommentRepository commentRepository;

    @Transactional
    public PostResponse createPost(PostRequest request, MultipartFile image) throws IOException {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Lỗi: Người dùng không tồn tại. Vui lòng đăng nhập lại!"));

        String imageData = null;
        if (image != null && !image.isEmpty()) {
            // Chuyển ảnh thành Base64 để lưu vào Database trực tiếp (Dành cho test)
            String base64Image = Base64.getEncoder().encodeToString(image.getBytes());
            imageData = "data:" + image.getContentType() + ";base64," + base64Image;
        }

        Post post = Post.builder()
                .user(user)
                .title(request.getTitle())
                .content(request.getContent())
                .imageUrl(imageData)
                .build();

        Post savedPost = postRepository.save(post);
        return mapToResponse(savedPost);
    }

    public List<PostResponse> getAllPosts() {
        return postRepository.findAllByOrderByCreatedAtDesc()
                .stream().map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public List<PostResponse> getPostsByUsername(String username) {
        if (!userRepository.existsByUsername(username)) {
            throw new RuntimeException("Không tìm thấy người dùng có tên: " + username);
        }
        return postRepository.findByUserUsernameOrderByCreatedAtDesc(username)
                .stream().map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public List<PostResponse> getMyPosts() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Lỗi: Người dùng không tồn tại."));
        
        return postRepository.findByUserIdOrderByCreatedAtDesc(user.getId())
                .stream().map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public void deletePost(String title) {
        Post post = postRepository.findByTitle(title)
                .orElseThrow(() -> new RuntimeException("Bài viết với tiêu đề này không tồn tại."));

        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User currentUser = userRepository.findByEmail(email).orElseThrow();

        if (!post.getUser().getId().equals(currentUser.getId())) {
            if (!currentUser.getRole().getName().equals("ADMIN")) {
                throw new RuntimeException("Bạn không có quyền xóa bài viết này!");
            }
        }
        
        postRepository.delete(post);
    }

    public List<PostResponse> searchPostsByTitle(String title) {
        return postRepository.findByTitleContainingIgnoreCaseOrderByCreatedAtDesc(title)
                .stream().map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public String toggleLike(String title) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByEmail(email).orElseThrow();

        Post post = postRepository.findByTitle(title)
                .orElseThrow(() -> new RuntimeException("Bài viết không tồn tại."));

        Optional<PostLike> existingLike = likeRepository.findByUserIdAndPostId(user.getId(), post.getId());

        if (existingLike.isPresent()) {
            likeRepository.delete(existingLike.get());
            return "Đã bỏ thích bài viết.";
        } else {
            PostLike like = PostLike.builder()
                    .user(user)
                    .post(post)
                    .build();
            likeRepository.save(like);
            return "Đã thích bài viết thành công!";
        }
    }

    private PostResponse mapToResponse(Post post) {
        return PostResponse.builder()
                .id(post.getId())
                .title(post.getTitle())
                .content(post.getContent())
                .authorName(post.getUser().getUsername())
                .imageUrl(post.getImageUrl())
                .createdAt(post.getCreatedAt())
                .likeCount(likeRepository.countByPostId(post.getId()))
                .commentCount(commentRepository.countByPostId(post.getId()))
                .build();
    }
}
