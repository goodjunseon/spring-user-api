package com.goodjunseon.user_api.domain.member.model.request;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class MemberJoinReq {


    private String email;
    private String password;
    private String nickname;

}
