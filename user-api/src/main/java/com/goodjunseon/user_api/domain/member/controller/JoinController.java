package com.goodjunseon.user_api.domain.member.controller;

import com.goodjunseon.user_api.domain.member.model.request.MemberJoinReq;
import com.goodjunseon.user_api.domain.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/join")
@RequiredArgsConstructor
public class JoinController {

    private final MemberService memberService;
    // 회원가입 API
    @PostMapping("/")
    public String join(@RequestBody MemberJoinReq req) {
        // 회원가입 처리 로직
        memberService.signup(req);

        // 아직까지는 프론트에 Response를 보내는 부분이 없으므로
        return "회원가입 성공";
    }
}
