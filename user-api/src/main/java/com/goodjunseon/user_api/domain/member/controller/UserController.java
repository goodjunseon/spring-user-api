package com.goodjunseon.user_api.domain.member.controller;

import com.goodjunseon.user_api.domain.member.model.entity.Member;
import com.goodjunseon.user_api.domain.member.model.response.MemberRes;
import com.goodjunseon.user_api.domain.member.service.MemberService;
import com.goodjunseon.user_api.global.common.BaseResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final MemberService memberService;

    /*
     * User (CRUD) API 구현
     */

    // 전체 사용자 목록 조회

    @GetMapping("/")
    public BaseResponse<List<MemberRes>> getAllUsers() {
        // 사용자 목록 조회 로직
        // 예시로 "사용자 목록" 문자열 반환
        List<Member> members = memberService.findAll();
        List<MemberRes> result = members.stream()
                .map(MemberRes::toMemberDTO)
                .toList();
        return new BaseResponse<>(result);
    }
}
