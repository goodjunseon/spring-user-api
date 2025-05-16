package com.goodjunseon.user_api.domain.member.model.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MemberLoginReq {

    private String email;
    private String password;

    // 추가적인 필드가 필요하다면 여기에 추가
}
