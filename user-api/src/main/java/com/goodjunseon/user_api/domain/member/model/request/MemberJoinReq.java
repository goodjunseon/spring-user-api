package com.goodjunseon.user_api.domain.member.model.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class MemberJoinReq {

    @NotBlank
    @Email
    private String email;

    @NotBlank
    @Size(min = 8, max = 20, message = "비밀번호는 8자 이상 20자 이하로 입력해주세요.")
    private String password;
    private String nickname;

}
