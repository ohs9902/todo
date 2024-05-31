package com.sparta.todo.config;

import com.sparta.todo.jwt.JwtUtil;
import com.sparta.todo.security.UserDetailsServiceImpl;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@EnableWebSecurity
@Configuration
public class SecurityConfig {
    private JwtUtil jwtUtil;
    private UserDetailsServiceImpl userDetailsService;
    private AuthenticationConfiguration authenticationConfiguration;
    public SecurityConfig(JwtUtil jwtUtil, UserDetailsServiceImpl userDetailsService, AuthenticationConfiguration authenticationConfiguration) {
        this.jwtUtil = jwtUtil;
        this.userDetailsService = userDetailsService;
        this.authenticationConfiguration = authenticationConfiguration;
    }
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
        http.csrf((csrf) -> csrf.disable());
        http.authorizeHttpRequests((authorizationRequests) ->
                authorizationRequests
                        .requestMatchers(PathRequest.toStaticResources().atCommonLocations()).permitAll() //resource 접근 허용
                        .requestMatchers("/api/user/**").permitAll() // api/user 로 시작하는 요청 모두 접근 허가
                        .anyRequest().authenticated() //그외 모든 요청 인증 처리
                );
        // 로그인 사용
        http.formLogin((formLogin) ->
                formLogin
                        // 로그인 View 제공 (GET /api/user/login-page)
                        .loginPage("/api/user/login-page")
                        // 로그인 처리 (POST /api/user/login)
                        .loginProcessingUrl("/api/user/login")
                        // 로그인 처리 후 성공 시 URL
                        .defaultSuccessUrl("/")
                        // 로그인 처리 후 실패 시 URL
                        .failureUrl("/api/user/login-page?error")
                        .permitAll()
        );

        return http.build();
    }


}
