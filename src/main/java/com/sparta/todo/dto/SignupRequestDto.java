package com.sparta.todo.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SignupRequestDto {
    @NotBlank
    @Size(max = 255) // 적절한 최대 길이로 지정
    private String username;
    @NotBlank
    @Size(max = 255) // 적절한 최대 길이로 지정
    private String password;
    @NotBlank
    private String nickname;

    private boolean admin = false;

    private String adminToken = "";
}
