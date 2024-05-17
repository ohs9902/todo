package com.sparta.todo.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TodoRequestDto {
    @NotBlank(message = "제목은 필수 입니다.")
    @Size(max = 200, message = "제목은 최대 200자까지 가능합니다.")
    private String title;

    @NotBlank(message = "할일 내용은 필수 입니다..")
    @Size(max = 200, message = "할일 내용은 최대 200자까지 가능합니다.")
    private String contents;

    @NotBlank(message = "담당자 이메일은 필수입니다.")
    @Email(message = "유효한 이메일 주소여야합니다.")
    private String manager;

    @NotBlank(message = "비밀번호는 필수입니다.")
    private String password;
}
