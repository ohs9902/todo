package com.sparta.todo.dto;

import lombok.Getter;

@Getter
public class TodoRequestDto {
    private String title;
    private String contents;
    private String manager;
    private String password;
}
