package com.goodjunseon.user_api.domain.member.service;

import com.goodjunseon.user_api.domain.member.model.request.MemberJoinReq;
import com.goodjunseon.user_api.domain.member.model.entity.Member;
import com.goodjunseon.user_api.domain.member.model.entity.Role;
import com.goodjunseon.user_api.domain.member.repository.MemberRepository;
import com.goodjunseon.user_api.global.common.BaseResponse;
import com.goodjunseon.user_api.global.common.BaseResponseStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    // 회원가입 로직 구현
    public BaseResponse<String> signup(MemberJoinReq req) {

        // 이메일 중복 체크 로직
        boolean isEmailExists = memberRepository.existsByEmail(req.getEmail());

        if (isEmailExists) {
            return new BaseResponse<>(BaseResponseStatus.INVALID_MEMBER_EMAIL);
        }

        Member entity = Member.toMemberEntity(
                req.getEmail(),
                req.getNickname(),
                bCryptPasswordEncoder.encode(req.getPassword()), // 비밀번호 암호화
                Role.ADMIN // 기본값으로 ADMIN 권한 부여
        );
        memberRepository.save(entity);
        return new BaseResponse<>("회원가입 성공"); // 회원가입 성공 메시지
    }

    public List<Member> findAll() {
        return memberRepository.findAll();
    }
}
