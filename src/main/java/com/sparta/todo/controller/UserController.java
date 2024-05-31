package com.sparta.todo.controller;

import com.sparta.todo.dto.SignupRequestDto;
import com.sparta.todo.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;


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
            System.out.println("Signup request received: " + requestDto);
            userService.signup(requestDto);
            return new ResponseEntity<>("회원가입 완료",HttpStatus.CREATED);
        }catch(IllegalArgumentException e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

}
