package com.goodjunseon.user_api.domain.member.controller;

import com.goodjunseon.user_api.domain.member.service.AuthService;
import com.goodjunseon.user_api.global.common.BaseResponse;
import com.goodjunseon.user_api.global.common.BaseResponseStatus;
import com.goodjunseon.user_api.global.dto.ApiRes;
import com.goodjunseon.user_api.global.response.ErrorType.MemberErrorCode;
import com.goodjunseon.user_api.global.response.SuccessType.MemberSuccessCode;
import com.goodjunseon.user_api.global.security.jwt.service.JwtTokenService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
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
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Access Token 재발급 성공"),
            @ApiResponse(responseCode = "401", description = "Refresh Token이 유효하지 않음")
    })
    @PostMapping("token/refresh")
    public ResponseEntity<ApiRes<String>> refreshAccessToken(HttpServletRequest request) {
        String refreshToken = request.getHeader("Refresh");
        try {
            String newAccessToken = jwtTokenService.reissueAccessToken(refreshToken);
            return ResponseEntity.ok(ApiRes.success(MemberSuccessCode.TOKEN_REISSUE_SUCCESS, newAccessToken));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(ApiRes.fail(MemberErrorCode.INVALID_TOKEN));
        }



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
