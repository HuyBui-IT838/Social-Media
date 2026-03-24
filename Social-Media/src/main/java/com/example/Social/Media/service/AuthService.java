package com.example.Social.Media.service;

import com.example.Social.Media.dto.AuthResponse;
import com.example.Social.Media.dto.LoginRequest;
import com.example.Social.Media.dto.RegisterRequest;
import com.example.Social.Media.entity.Role;
import com.example.Social.Media.entity.User;
import com.example.Social.Media.repository.RoleRepository;
import com.example.Social.Media.repository.UserRepository;
import com.example.Social.Media.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final CustomUserDetailsService userDetailsService;
    private final JwtUtil jwtUtil;

    public AuthResponse register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Lỗi: Email đã được sử dụng!");
        }
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new RuntimeException("Lỗi: Tên đăng nhập đã tồn tại!");
        }

        Role userRole = roleRepository.findByName("USER")
                .orElseThrow(() -> new RuntimeException("Lỗi: Không tìm thấy quyền USER trong DB"));

        User user = User.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(userRole)
                .build();

        userRepository.save(user);

        return AuthResponse.builder()
                .message("Đăng ký thành công!")
                .username(user.getUsername())
                .role(user.getRole().getName())
                .token("")
                .build();
    }

    public AuthResponse login(LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy người dùng"));

        UserDetails userDetails = userDetailsService.loadUserByUsername(user.getEmail());
        String jwtToken = jwtUtil.generateToken(userDetails);

        return AuthResponse.builder()
                .message("Đăng nhập thành công!")
                .username(user.getUsername())
                .role(user.getRole().getName())
                .token(jwtToken)
                .build();
    }
}
