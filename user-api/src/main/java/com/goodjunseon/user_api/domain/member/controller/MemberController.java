package com.goodjunseon.user_api.domain.member.controller;

import com.goodjunseon.user_api.domain.member.model.entity.Member;
import com.goodjunseon.user_api.domain.member.model.request.MemberJoinReq;
import com.goodjunseon.user_api.domain.member.model.response.MemberRes;
import com.goodjunseon.user_api.domain.member.service.MemberService;
import com.goodjunseon.user_api.global.dto.ApiRes;
import com.goodjunseon.user_api.global.response.ErrorType.MemberErrorCode;
import com.goodjunseon.user_api.global.response.SuccessType.MemberSuccessCode;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/members")
@RequiredArgsConstructor
@Tag(name = "MemberController", description = "사용자 관련 API")
public class MemberController {

    private final MemberService memberService;


    /*
     * User (CRUD) API 구현
     */

    // 회원가입 로직
    @Operation(summary = "회원가입", description = "사용자를 등록합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "회원가입 성공"),
//            content = @Content(schema = @Schema(implementation = ApiRes.class))),
            @ApiResponse(responseCode = "409", description = "이메일 중복")
//            content = @Content(schema = @Schema(implementation = ApiRes.class)))
    })
    @PostMapping("/signup")
    public ResponseEntity<ApiRes<Void>> signup(@Valid @RequestBody MemberJoinReq req) {

        // 회원가입 처리 로직
        boolean isSuccess = memberService.signup(req);

        if (isSuccess) {
            return ResponseEntity.ok(ApiRes.success(MemberSuccessCode.MEMBER_CREATED, null));
        } else {
            // 이메일 중복일 경우 Confilict 응답
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(ApiRes.fail(MemberErrorCode.DUPLICATE_EMAIL));
        }
    }

    // 전체 사용자 목록 조회
    @Operation(summary = "전체 사용자 조회", description = "등록된 모든 사용자 목록을 반환합니다.")

    @GetMapping("/getAll")
    public ResponseEntity<ApiRes<List<MemberRes>>> getAllMembers() {
        // 사용자 목록 조회 로직
        // 예시로 "사용자 목록" 문자열 반환
        List<Member> members = memberService.findAll();
        List<MemberRes> result = members.stream()
                .map(MemberRes::toMemberDTO)
                .toList();
        return ResponseEntity.ok(ApiRes.success((MemberSuccessCode.MEMBER_VIEW), result));
    }



}
