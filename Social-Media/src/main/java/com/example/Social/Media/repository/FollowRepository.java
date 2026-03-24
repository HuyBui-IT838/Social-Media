package com.example.Social.Media.repository;

import com.example.Social.Media.entity.Follow;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface FollowRepository extends JpaRepository<Follow, Long> {
    Optional<Follow> findByFollowerIdAndFollowingId(Long followerId, Long followingId);
    long countByFollowingId(Long followingId); // Số người theo dõi (Followers)
    long countByFollowerId(Long followerId); // Số người đang theo dõi (Following)
}
