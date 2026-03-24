package com.example.Social.Media.repository;

import com.example.Social.Media.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    
    // Lấy các bình luận của 1 bài POST theo thứ tự cũ trước mới sau
    List<Comment> findByPostIdOrderByCreatedAtAsc(Long postId);

    // Xóa tất cả bình luận nếu bài Post bị xóa
    // Đếm số bình luận của 1 bài POST
    long countByPostId(Long postId);
}

