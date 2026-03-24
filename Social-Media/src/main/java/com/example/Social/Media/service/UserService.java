package com.example.Social.Media.service;

import com.example.Social.Media.dto.UserResponse;
import com.example.Social.Media.entity.Follow;
import com.example.Social.Media.entity.User;
import com.example.Social.Media.repository.FollowRepository;
import com.example.Social.Media.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;



@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final FollowRepository followRepository;


    public UserResponse getUserByUsername(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy người dùng có tên: " + username));
        
        return UserResponse.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .role(user.getRole().getName())
                .followerCount(followRepository.countByFollowingId(user.getId()))
                .followingCount(followRepository.countByFollowerId(user.getId()))
                .build();
    }

    @Transactional
    public String toggleFollow(String targetUsername) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User currentUser = userRepository.findByEmail(email).orElseThrow();

        User targetUser = userRepository.findByUsername(targetUsername)
                .orElseThrow(() -> new RuntimeException("Người dùng cần follow không tồn tại!"));

        if (currentUser.getId().equals(targetUser.getId())) {
            throw new RuntimeException("Bạn không thể tự theo dõi chính mình!");
        }

        Optional<Follow> existingFollow = followRepository.findByFollowerIdAndFollowingId(currentUser.getId(), targetUser.getId());

        if (existingFollow.isPresent()) {
            followRepository.delete(existingFollow.get());
            return "Đã bỏ theo dõi " + targetUsername;
        } else {
            Follow follow = Follow.builder()
                    .follower(currentUser)
                    .following(targetUser)
                    .build();
            followRepository.save(follow);
            return "Đã theo dõi " + targetUsername + " thành công!";
        }
    }
}


