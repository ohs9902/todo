package com.sparta.todo.entity;

import com.sparta.todo.dto.TodoRequestDto;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor

public class Todo extends Timestamped{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "제목은 필수 입니다.")
    @Column(length = 200 ,nullable=false)
    private String title;

    @NotBlank(message = "할일 내용은 필수 입니다..")
    @Column(length = 200 ,nullable=false)
    private String  contents;

    @Column
    @Email(message = "유효한 이메일 주소여야합니다.")
    @NotBlank(message = "담당자 이메일은 필수입니다.")
    private String manager;

    @Column(nullable = false)
    @NotBlank(message = "비밀번호는 필수입니다.")
    private String password;

    public Todo(TodoRequestDto todoRequestDto) {
        this.title = todoRequestDto.getTitle();
        this.contents = todoRequestDto.getContents();
        this.manager = todoRequestDto.getManager();
        this.password = todoRequestDto.getPassword();
    }

    public void update(TodoRequestDto todoRequestDto) {
        this.title = todoRequestDto.getTitle();
        this.contents = todoRequestDto.getContents();
        this.manager = todoRequestDto.getManager();
        this.password = todoRequestDto.getPassword();
    }
}
