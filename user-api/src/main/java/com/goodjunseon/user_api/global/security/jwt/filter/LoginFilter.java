package com.goodjunseon.user_api.global.security.jwt.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.goodjunseon.user_api.domain.member.model.request.MemberLoginReq;
import com.goodjunseon.user_api.domain.member.security.CustomUserDetails;
import com.goodjunseon.user_api.global.dto.ApiRes;
import com.goodjunseon.user_api.global.response.ErrorType.MemberErrorCode;
import com.goodjunseon.user_api.global.response.SuccessType.MemberSuccessCode;
import com.goodjunseon.user_api.global.security.jwt.service.JwtTokenService;
import com.goodjunseon.user_api.global.security.util.JWTUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

@RequiredArgsConstructor
public class LoginFilter extends UsernamePasswordAuthenticationFilter {

    private final JWTUtil jwtUtil;
    private final ObjectMapper objectMapper;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenService jwtTokenService;

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException {
//
//        // request에서 username과 password를 가져온다.
//        String username = obtainUsername(request);
//        String password = obtainPassword(request);
//
//        // 스프링 시큐리티에서 username과 password를 검증하기 위해서는 token에 담아야한다.
//        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(username, password, null);
//
//        //token에 담은 검증을 위한 AuthenticationManager로 전달
//        return authenticationManager.authenticate(authToken);
        try {
            var loginReq = objectMapper.readValue(request.getInputStream(), MemberLoginReq.class);

            var authToken = new UsernamePasswordAuthenticationToken(
                    loginReq.getEmail(), loginReq.getPassword()
            );

            return authenticationManager.authenticate(authToken);
        } catch (IOException e) {
            throw new RuntimeException("요청 본문이 올바른 JSON 형식이 아닙니다.", e);
        }
    }

    //로그인 성공시 실행하는 메소드(여기서 JWT 토큰을 생성)
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
                                            Authentication authResult) throws IOException, ServletException {

        CustomUserDetails authMember = (CustomUserDetails) authResult.getPrincipal();

        //권한 정보 추출
        Collection<? extends GrantedAuthority> authorities = authResult.getAuthorities();
        Iterator<? extends GrantedAuthority> iterator = authorities.iterator();
        GrantedAuthority auth = iterator.next();

        String role = auth.getAuthority(); // ADMIN -> ROLE_ADMIN으로 변경
        Long idx = authMember.getIdx();
        String username = authMember.getUsername();

        // JWT 토큰 발급
        String accessToken = jwtUtil.createToken(idx, username, role); // access Token 생성
        String refreshToken = jwtUtil.createRefreshToken(username); // refresh Token 생성

        // 응답 헤더에 토큰 추가
        response.setHeader("Authorization", "Bearer " + accessToken);
        response.setHeader("Refresh", refreshToken);

        // 응답 바디(JSON)
        Map<String, String> tokenMap = Map.of(
                "accessToken", accessToken,
                "refreshToken", refreshToken
        );

        ApiRes<Map<String,String>> responseBody =
                ApiRes.success(MemberSuccessCode.LOGIN_SUCCESS, tokenMap);

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(objectMapper.writeValueAsString(responseBody));

        // RefreshToken을 redis에 저장
        jwtTokenService.refreshTokenSave(username, refreshToken);

        // 응답 헤더에 토큰 추가
//        response.setHeader("Authorization", "Bearer " + accessToken);
//        response.setHeader("Refresh", refreshToken);
    }

    @Override
    public void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response,
                                            AuthenticationException failed) throws IOException, ServletException {

        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        ApiRes<String> errorResponse = ApiRes.fail(MemberErrorCode.LOGIN_FAIL);

        String json = objectMapper.writeValueAsString(errorResponse);
        response.getWriter().write(json);
    }
}
