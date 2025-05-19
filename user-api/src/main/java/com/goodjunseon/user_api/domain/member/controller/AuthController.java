package com.goodjunseon.user_api.domain.member.controller;

import com.goodjunseon.user_api.domain.member.service.AuthService;
import com.goodjunseon.user_api.global.common.BaseResponse;
import com.goodjunseon.user_api.global.common.BaseResponseStatus;
import com.goodjunseon.user_api.global.security.jwt.service.JwtTokenService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Tag(name = "AuthController", description = "인증 관련 API")
public class AuthController {

    private final JwtTokenService jwtTokenService;
    private final AuthService authService;

    @Operation(summary = "Access Token 재발급", description = "Refresh Token을 사용하여 Access Token을 재발급합니다.")
    @PostMapping("token/refresh")
    public ResponseEntity<?> refreshAccessToken(HttpServletRequest request) {
        String refreshToken = request.getHeader("Refresh");
        String newAccessToken = jwtTokenService.reissueAccessToken(refreshToken);
        return ResponseEntity.ok()
                .header("Authorization", "Bearer " + newAccessToken)
                .build();
    }


    @Operation(summary = "로그아웃", description = "사용자가 로그아웃합니다.")
    @PostMapping("/logout")
    public BaseResponse<String> logout(HttpServletRequest request) {

        String authorization = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (authorization == null || !authorization.startsWith("Bearer ")) {
            return new BaseResponse<>(BaseResponseStatus.FAIL);
        }

        String accessToken = authorization.substring(7); // Bearer 제거
        authService.logout(accessToken); // 서비스 계층에서 로그아웃 처리
        return new BaseResponse<>("로그아웃 성공");
    }
}
