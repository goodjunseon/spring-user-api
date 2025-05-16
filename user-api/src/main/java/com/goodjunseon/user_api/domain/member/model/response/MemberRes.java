package com.goodjunseon.user_api.domain.member.model.response;

import com.goodjunseon.user_api.domain.member.model.entity.Member;
import lombok.*;

@Getter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MemberRes {

    private Long idx;
    private String email;
    private String nickname;

    public static MemberRes toMemberDTO(Member member) {
        return MemberRes.builder()
                .idx(member.getIdx())
                .email(member.getEmail())
                .nickname(member.getNickname())
                .build();
    }

}
