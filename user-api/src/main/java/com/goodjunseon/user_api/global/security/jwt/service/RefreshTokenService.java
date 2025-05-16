package com.goodjunseon.user_api.global.security.jwt.service;

import com.goodjunseon.user_api.global.security.util.JWTUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {

    private final JWTUtil jwtUtil;
    private final RedisTemplate<String, String> redisTemplate;

    // Refresh Token을 Redis에 저장하는 메서드
    public void save(String email, String refreshToken) {
        Long expiration = jwtUtil.getExpiration(refreshToken);

        redisTemplate.opsForValue().set("refreshToken:" + email, // key
                refreshToken, // value
                expiration, // 만료 시간
                java.util.concurrent.TimeUnit.MILLISECONDS); // 시간 단위
    }

    // Refresh Token을 제거하는 메서드
    @Transactional
    public void delete(String refreshToken) {
        // 1. 토큰에서 email 추출
        String email = jwtUtil.getUsername(refreshToken);
        if (email != null) {
            redisTemplate.delete("refreshToken:" + email);
        }
    }

    // Access 토큰 재발급


    // Refresh 토큰 재발급
}
