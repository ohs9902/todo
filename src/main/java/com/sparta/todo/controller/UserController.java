package com.sparta.todo.controller;

import com.sparta.todo.dto.SignupRequestDto;
import com.sparta.todo.entity.UserRoleEnum;
import com.sparta.todo.jwt.JwtUtil;
import com.sparta.todo.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Map;


@Controller
@RequestMapping("/api/user")
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    @PostMapping("/signup")
    public ResponseEntity<String> signupPage(@Valid @RequestBody SignupRequestDto requestDto) {
        try{
            System.out.println("Signup request received: " + requestDto.getUsername());
            userService.signup(requestDto);
            return new ResponseEntity<>("회원가입 완료",HttpStatus.CREATED);
        }catch(IllegalArgumentException e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/refresh")
    public ResponseEntity<?> refreshAccessToken(@CookieValue("refreshToken") String refreshToken) {
        try {
            // 쿠키에서 리프레시 토큰을 읽고 유효성을 검증한 후 새로운 액세스 토큰을 생성
            String username = JwtUtil.getUserInfoFromToken(refreshToken).getSubject();
            JwtUtil jwtUtil = new JwtUtil();
            if (jwtUtil.validateRefreshToken(username, refreshToken)) {
                String newAccessToken = JwtUtil.createToken(username, UserRoleEnum.USER); // 유저 역할 예시
                return ResponseEntity.ok().body(Map.of("accessToken", newAccessToken));
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid refresh token");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid refresh token");
        }
    }

}
