package com.sparta.todo.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.todo.dto.LoginRequestDto;
import com.sparta.todo.entity.UserRoleEnum;
import com.sparta.todo.security.UserDetailsImpl;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;
import java.util.Map;

@Slf4j(topic = "로그인 및 JWT 생성")
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private final JwtUtil jwtUtil;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public JwtAuthenticationFilter(AuthenticationManager authenticationManager, JwtUtil jwtUtil) {
        super.setAuthenticationManager(authenticationManager);
        this.jwtUtil = jwtUtil;
        setFilterProcessesUrl("/api/user/login");
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        if("application/json".equals(request.getContentType())){
            try{
                LoginRequestDto loginRequestDto = objectMapper.readValue(request.getInputStream(),LoginRequestDto.class);

                UsernamePasswordAuthenticationToken authRequest =
                        new UsernamePasswordAuthenticationToken(loginRequestDto.getUsername(),loginRequestDto.getPassword());
                setDetails(request,authRequest);
                return this.getAuthenticationManager().authenticate(authRequest);
            }catch(IOException e){
                throw new RuntimeException(e);
            }
        }
        return super.attemptAuthentication(request, response);
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        log.info("로그인 성공 및 JWT 생성");
        String username = ((UserDetailsImpl) authResult.getPrincipal()).getUsername();
        UserRoleEnum role = ((UserDetailsImpl) authResult.getPrincipal()).getUser().getRole();

        String token = JwtUtil.createToken(username,role);

        String refreshToken = JwtUtil.createRefreshToken();
        JwtUtil.addJwtToCookie(token,response);
        response.addCookie(createRefreshTokenCookie(refreshToken));
    }

    // 리프레시 토큰을 쿠키에 추가하는 유틸리티 메서드
    private Cookie createRefreshTokenCookie(String refreshToken) {
        Cookie refreshTokenCookie = new Cookie("refreshToken", refreshToken);
        refreshTokenCookie.setHttpOnly(true);
        refreshTokenCookie.setPath("/"); // 쿠키의 경로 설정
        refreshTokenCookie.setMaxAge(60 * 60 * 24 * 7); // 유효 기간 7일
        return refreshTokenCookie;
    }
    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
        log.info("로그인 실패");
        response.setStatus(401);
    }
}
