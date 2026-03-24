package com.example.Social.Media.repository;

import com.example.Social.Media.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    // Sắp xếp bài viết mới nhất lên trên
    List<Post> findAllByOrderByCreatedAtDesc();
    
    // Lấy tất cả bài viết của 1 user cụ thể
    List<Post> findByUserIdOrderByCreatedAtDesc(Long userId);

    // Lấy tất cả bài viết theo Username (Dùng cho URL web thật)
    List<Post> findByUserUsernameOrderByCreatedAtDesc(String username);

    // Tìm kiếm bài viết theo tiêu đề
    List<Post> findByTitleContainingIgnoreCaseOrderByCreatedAtDesc(String title);
    // Tìm kiếm bài viết để lấy ID khi biết tiêu đề
    java.util.Optional<Post> findByTitle(String title);
}


