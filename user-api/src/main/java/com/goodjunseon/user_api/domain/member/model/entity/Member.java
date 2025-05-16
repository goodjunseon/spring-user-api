package com.goodjunseon.user_api.domain.member.model.entity;

import com.goodjunseon.user_api.global.common.model.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Entity
@Table(name = "member_table")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Member extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idx;

    @Column(unique = true)
    private String email;
    private String password;
    private String nickname;

    @Column
    @Enumerated(EnumType.STRING)
    private Role role;


    public static Member toMemberEntity(String email, String name, String password, Role role) {
        return Member.builder()
                .email(email)
                .password(password)
                .nickname(name)
                .role(role)
                .build();
    }

}
