package com.goodjunseon.user_api.domain.member.controller;

import com.goodjunseon.user_api.global.security.util.JWTUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final JWTUtil jwtUtil;
    private final RedisTemplate<String, String> redisTemplate;

    @PostMapping("token/refresh")
    public ResponseEntity<?> refreshAccessToken(HttpServletRequest request) {
        String refreshToken = request.getHeader("Authorization").replace("Bearer ", "");

        // Refresh Token 검증
        if (!jwtUtil.validateToken(refreshToken)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid refresh token");
        }

        String email = jwtUtil.getUsername(refreshToken);
        String storedRefreshToken = redisTemplate.opsForValue().get("refreshToken:" + email);

        if (!refreshToken.equals(storedRefreshToken)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid refresh token");
        }

        String newAccessToken = jwtUtil.createToken(null, email, jwtUtil.getRole(refreshToken)); // idx는 생략가능
        return ResponseEntity.ok()
                .header("Authorization", "Bearer " + newAccessToken)
                .body("Access token refreshed successfully");

    }



}
