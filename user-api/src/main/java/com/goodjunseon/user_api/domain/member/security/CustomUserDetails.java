package com.goodjunseon.user_api.domain.member.security;

import com.goodjunseon.user_api.domain.member.model.entity.Member;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

@Getter
@RequiredArgsConstructor
public class CustomUserDetails implements UserDetails {
    private final Member member;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // Role.USER or ROLE.ADMIN -> ROLE_USER / ROLE_ADMIN 형식으로 변환
        return Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + member.getRole().name()));
    }

    @Override
    public String getPassword() {
        return member.getPassword();
    }

    @Override
    public String getUsername() {
        return member.getEmail();
    }

    public Long getIdx() {
        return member.getIdx();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true; // 만료 안됨
    }

    @Override
    public boolean isAccountNonLocked() {
        return true; // 잠금 안됨
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true; // 자격 증명 만료 안됨
    }

    @Override
    public boolean isEnabled() {
        return true; // 활성화 상태
    }


}
