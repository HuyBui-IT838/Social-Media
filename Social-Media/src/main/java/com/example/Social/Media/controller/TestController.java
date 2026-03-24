package com.example.Social.Media.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/test")
@Tag(name = "Test API", description = "Dùng để kiểm tra kết nối hệ thống")
public class TestController {

    @GetMapping("/hello")
    @Operation(summary = "Kiểm tra xem API có hoạt động không")
    public String sayHello() {
        return "Chào mừng bạn đến với Social Media API! Hệ thống đã sẵn sàng!";
    }
}
