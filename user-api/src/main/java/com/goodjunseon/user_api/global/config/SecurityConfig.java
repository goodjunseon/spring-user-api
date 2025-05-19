package com.goodjunseon.user_api.global.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.goodjunseon.user_api.global.security.jwt.filter.JWTFilter;
import com.goodjunseon.user_api.global.security.util.JWTUtil;
import com.goodjunseon.user_api.global.security.jwt.filter.LoginFilter;
import com.goodjunseon.user_api.global.security.jwt.service.JwtTokenService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Slf4j
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final AuthenticationConfiguration authenticationConfiguration;
    private final JWTUtil jwtUtil;
    private final ObjectMapper objectMapper;
    private final JwtTokenService jwtTokenService;

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    // PasswordEncoder 인터페이스를 구현한 BCryptPasswordEncoder 빈 등록
    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
                .csrf(AbstractHttpConfigurer::disable) // CSRF 공격 방지 기능 disable
                .formLogin(AbstractHttpConfigurer::disable) //Form 로그인 방식 disable
                .httpBasic(AbstractHttpConfigurer::disable); //HTTP Basic 인증 disable

        // 경로별 인가 작업
        http
                .authorizeHttpRequests((auth) -> auth
                        .requestMatchers(
                                "/swagger-ui/**", "/v3/api-docs/**",
                                "/api/login", "/", "/api/join/**"
                        ,"api/auth/token/refresh",
                        "/webjars/**").permitAll()
                        .requestMatchers("/api/admin").hasRole("ADMIN")
                        .anyRequest().authenticated());

        // LoginFilter에 URL 지정
        LoginFilter loginFilter = new LoginFilter(jwtUtil, objectMapper, authenticationManager(authenticationConfiguration), jwtTokenService);
        loginFilter.setFilterProcessesUrl("/api/login");

        // JWT 필터 등록
        http.addFilterBefore(new JWTFilter(jwtUtil), UsernamePasswordAuthenticationFilter.class);
        http.addFilterAt(loginFilter,UsernamePasswordAuthenticationFilter.class);

        // 세션 관리 정책 설정
        http
                .sessionManagement((session) -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        return http.build();
    }
}
