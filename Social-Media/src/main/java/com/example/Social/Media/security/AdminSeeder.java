package com.example.Social.Media.security;

import com.example.Social.Media.entity.Role;
import com.example.Social.Media.entity.User;
import com.example.Social.Media.repository.RoleRepository;
import com.example.Social.Media.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AdminSeeder implements CommandLineRunner {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        // Sinh quyền trước
        Role userRole = roleRepository.findByName("USER")
                .orElseGet(() -> roleRepository.save(Role.builder().name("USER").build()));

        Role adminRole = roleRepository.findByName("ADMIN")
                .orElseGet(() -> roleRepository.save(Role.builder().name("ADMIN").build()));

        String adminEmail = "Admin@gmail.com";
        String adminUsername = "Admin";

        // Sau đó sinh Admin 1
        if (userRepository.findByEmail(adminEmail).isEmpty()) {
            User admin = User.builder()
                    .username(adminUsername)
                    .email(adminEmail)
                    .password(passwordEncoder.encode("Admin123"))
                    .role(adminRole)
                    .build();

            userRepository.save(admin);
            System.out.println("============== TẠO TÀI KHOẢN ADMIN 1 THÀNH CÔNG ==============");
            System.out.println("Email: " + adminEmail);
            System.out.println("Password: Admin123");
            System.out.println("==============================================================");
        }

        // Tạo Admin 2 (Tài khoản khác)
        String adminEmail2 = "admin2@gmail.com";
        if (userRepository.findByEmail(adminEmail2).isEmpty()) {
            User admin2 = User.builder()
                    .username("admin2")
                    .email(adminEmail2)
                    .password(passwordEncoder.encode("Admin456"))
                    .role(adminRole)
                    .build();

            userRepository.save(admin2);
            System.out.println("============== TẠO TÀI KHOẢN ADMIN 2 THÀNH CÔNG ==============");
            System.out.println("Email: " + adminEmail2);
            System.out.println("Password: Admin456");
            System.out.println("==============================================================");
        }
    }

}
