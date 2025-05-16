package com.goodjunseon.user_api.domain.member.repository;

import com.goodjunseon.user_api.domain.member.model.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {

    boolean existsByEmail(String memberEmail);

    Optional<Member> findByEmail(String email);

//    Optional<Member> findByEmail(String username);
}
