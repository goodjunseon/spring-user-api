package com.goodjunseon.user_api.global.security.jwt.repository;

import com.goodjunseon.user_api.global.security.jwt.model.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {

}
