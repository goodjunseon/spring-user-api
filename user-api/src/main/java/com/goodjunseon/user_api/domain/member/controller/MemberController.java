package com.goodjunseon.user_api.domain.member.controller;

import com.goodjunseon.user_api.domain.member.model.entity.Member;
import com.goodjunseon.user_api.domain.member.model.request.MemberJoinReq;
import com.goodjunseon.user_api.domain.member.model.response.MemberRes;
import com.goodjunseon.user_api.domain.member.service.MemberService;
import com.goodjunseon.user_api.global.common.BaseResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/members")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;


    /*
     * User (CRUD) API 구현
     */

    // 회원가입 로직
    @PostMapping("/signup")
    public String join(@RequestBody MemberJoinReq req) {
        // 회원가입 처리 로직
        memberService.signup(req);

        // 아직까지는 프론트에 Response를 보내는 부분이 없으므로
        return "회원가입 성공";
    }

    // 전체 사용자 목록 조회
    @GetMapping("/getAll")
    public BaseResponse<List<MemberRes>> getAllMembers() {
        // 사용자 목록 조회 로직
        // 예시로 "사용자 목록" 문자열 반환
        List<Member> members = memberService.findAll();
        List<MemberRes> result = members.stream()
                .map(MemberRes::toMemberDTO)
                .toList();
        return new BaseResponse<>(result);
    }

}
