package com.goodjunseon.user_api.domain.member.controller;

import com.goodjunseon.user_api.global.dto.ApiRes;
import com.goodjunseon.user_api.global.response.ErrorType.MemberErrorCode;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@Tag(name = "Auth (Document Only", description = "Spring Security 기반 로그인 문서용 API")
@RestController
@RequestMapping("/docs/auth")
public class AuthDocController {


    @Operation(summary = "로그인 (문서용)", description = "Spring Security Filter를 통해 로그인됩니다.")
    @ApiResponse(responseCode = "200", description = "로그인 성공", content = @Content(
            mediaType = "application/json",
            schema = @Schema(implementation = ApiRes.class),
            examples = @ExampleObject(name = "LoginSuccessExample", value = """
                    {
                      "code": "S200",
                      "message": "로그인 성공",
                      "data": {
                        "accessToken": "Bearer eyJhbGciOi...",
                        "refreshToken": "eyJhbGciOiJIUzI1NiJ9..."
                      }
                    }
                    """)
    ))
    @PostMapping("/login")
    public ResponseEntity<ApiRes<Map<String,String>>> loginDoc() {
        return ResponseEntity.ok(null);
    }

    @Operation(summary = "로그인 실패 예시 (문서용)")
    @ApiResponse(responseCode = "401", description = "로그인 실패", content = @Content(
            mediaType = "application/json",
            schema = @Schema(implementation = ApiRes.class),
            examples = @ExampleObject(value = """
            {
              "code": "E401",
              "message": "이메일 또는 비밀번호가 틀렸습니다",
              "data": null
            }
        """)
    ))
    @PostMapping("/login/fail")
    public ResponseEntity<ApiRes<Void>> loginFailDoc() {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(ApiRes.fail(MemberErrorCode.LOGIN_FAIL));
    }
}
