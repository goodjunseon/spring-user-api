package com.goodjunseon.user_api.global.security.jwt.service;

import com.goodjunseon.user_api.domain.member.repository.MemberRepository;
import com.goodjunseon.user_api.global.security.util.JWTUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class JwtTokenService {

    private final JWTUtil jwtUtil;
    private final RedisTemplate<String, String> redisTemplate;
    private final MemberRepository memberRepository;

    // Refresh Token을 Redis에 저장하는 메서드
    public void refreshTokenSave(String email, String refreshToken) {
        Long expiration = jwtUtil.getExpiration(refreshToken); // 만료 시간

        redisTemplate.opsForValue().set("refreshToken:" + email, // key
                refreshToken, // value
                expiration, // 만료 시간
                java.util.concurrent.TimeUnit.MILLISECONDS); // 시간 단위
    }

    // Refresh Token을 제거하는 메서드
    @Transactional
    public void refreshTokenDelete(String refreshToken) {
        // 1. 토큰에서 email 추출
        String email = jwtUtil.getUsername(refreshToken);
        if (email != null) {
            redisTemplate.delete("refreshToken:" + email);
        }
    }

    // Access 토큰 재발급
    public String reissueAccessToken(String refreshToken) {

        if (!jwtUtil.validateToken(refreshToken)){
            throw new RuntimeException("Refresh Token이 유효하지 않습니다.");
        }

        String email = jwtUtil.getUsername(refreshToken);
        String stored = redisTemplate.opsForValue().get("refreshToken:" + email);

        if (stored == null || !stored.equals(refreshToken)){
            throw new RuntimeException("Refresh Token 불일치");
        }

        // 3. 새로운 Access Token 생성
        String role = jwtUtil.getRole(refreshToken);
        var member = memberRepository.findByEmail(email)
                .orElseThrow(()-> new RuntimeException("사용자를 찾을 수 없습니다."));
        Long idx = member.getIdx();
        return jwtUtil.createToken(idx, email, role);
    }
}
