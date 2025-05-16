package com.goodjunseon.user_api.domain.member.model.entity;

public enum Role {
    //  GUEST는 권한이 없는 사용자
    USER, ADMIN, GUEST;


    public String getAuthority() {
        return "ROLE_" + this.name();
    }
}
